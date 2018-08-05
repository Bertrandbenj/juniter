package juniter.service.rest;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import juniter.model.bma.PeerDoc;
import juniter.model.net.Peer;
import juniter.repository.EndPointsRepository;
import juniter.repository.PeersRepository;

/**
 * Handles network discovery, publication of up-to-date documents and getting
 * the state of the blockchain on other nodes
 *
 *
 * @author ben
 *
 */
@RestController
@ConditionalOnExpression("${juniter.bma.enabled:false}")
@RequestMapping("/network")
public class PeeringService {

	public static final Logger logger = LogManager.getLogger();
	// private static final String ERROR_MESSAGE = "";

	Random random = new Random();

	/**
	 * Initialized from trusted sources
	 */
	@Value("#{'${juniter.network.trusted}'.split(',')}")
	private List<String> nodesURL;

	@Autowired
	private PeersRepository peerRepo;

	@Autowired
	private EndPointsRepository endPointRepo;

	private RestTemplate restTpl = new RestTemplate();

	public PeerDoc fetchPeers(String nodeURL) {
		final ResponseEntity<PeerDoc> responseEntity = new RestTemplate().exchange(nodeURL + "/network/peers",
				HttpMethod.GET, null, new ParameterizedTypeReference<PeerDoc>() {
				});
		final var peers = responseEntity.getBody();
		final var contentType = responseEntity.getHeaders().getContentType();
		final var statusCode = responseEntity.getStatusCode();
		save(peers);

		logger.info("Found peers: " + peers.getPeers().size() + ", status : " + statusCode.getReasonPhrase()
				+ ", ContentType: " + contentType.toString());
		return peers;
	}

	@Async
	public CompletableFuture<PeerDoc> findFirstPeers() {
		return findPeers(nodesURL.get(0));
	}

	@Async
	@Transactional
	public CompletableFuture<List<PeerDoc>> findOtherPeers() {
		logger.info("Find other peers ");

		final var compute = reloadURL() //
				.parallelStream() //
				.map(url -> CompletableFuture.supplyAsync(() -> findPeers(url)).join()).map(CompletableFuture::join)
				.collect(Collectors.toList());

		logger.info("Found other peers ");

		return CompletableFuture.completedFuture(compute);
	}

	@Async
	public CompletableFuture<PeerDoc> findPeers(String nodeURL) {
		final var url = nodeURL + (nodeURL.endsWith("/") ? "" : "/") + "network/peers";

		logger.info("Looking up for peers at  " + url);
		final var time = System.nanoTime();
		PeerDoc results;
		try {
			results = restTpl.getForObject(url, PeerDoc.class);
			final var elapsedTime = Long.divideUnsigned(System.nanoTime() - time, 1000000);
			logger.info(
					"... took " + elapsedTime + " ms for " + url + " " + results.getPeers().size() + " found peers");
			results.timeMillis = elapsedTime;
			save(results);
			return CompletableFuture.completedFuture(results)//
					.handle((peerDoc, ex) -> {
						if (ex == null) {
							logger.debug("Completed Future : " + peerDoc + " " + peerDoc.getPeers().size());
						} else {
							logger.error("UNcompleted Future for peerDoc: " + peerDoc, ex);
						}
						return peerDoc;
					});
		} catch (final Exception e) {
			logger.error("TODO : stop  findPeers : " + e.getMessage());
		}
		return null;
	}

	@Transactional
	@RequestMapping("/")
	public List<String> index() {
		logger.info("Entering /network/ ... ");
		return endPointRepo.enpointsURL();
	}

	@RequestMapping(value = "/html", method = RequestMethod.GET)
	public String init(@ModelAttribute("model") ModelMap model) {
		logger.info("Entering /network/html ... ");
		model.addAttribute("carList", "huhu");
		return "index";
	}

	@Transactional(readOnly = true)
	@RequestMapping(value = "/peers", method = RequestMethod.GET)
	public PeerDoc peers() {

		logger.info("Entering /network/peers ...");

		try (var peers = peerRepo.streamAllPeers()) {
			final var peerL = peers.collect(Collectors.toList());
			return new PeerDoc(peerL);
		} catch (final Exception e) {
			logger.error("PeeringService.peers() peerRepo.streamAllPeers ->  ", e);
		}
		return null;
	}

	public String randomPeer() {
		reloadURL();
		Collections.shuffle(nodesURL);
		return nodesURL.get(random.nextInt(nodesURL.size()));
	}

	private List<String> reloadURL() {
		endPointRepo.enpointsURL() //
				.forEach(url -> {//
					if (!nodesURL.contains(url)) {
						nodesURL.add(url);
					}
				});
		return nodesURL;
	}

//	var futures = reloadURL() //
//			.stream() //
//			.map(url -> CompletableFuture //
//					.completedFuture(restTpl.getForObject(url, PeerDoc.class)) //
//					.completeOnTimeout(null, 5, TimeUnit.SECONDS) //
//					.exceptionally(ex -> null))
//			.map(CompletableFuture::join)//
//			.collect(Collectors.toList());

	public void save(PeerDoc peers) {
		peers.getPeers().stream() // parsed peers
				.filter(Peer.blockHigherThan(128000)) // with filtering
				.forEach(p -> {
					peerRepo.saveAndFlush(p); // save the peer object
					p.endpoints().stream() // iterate endpoints
							.map(ep -> endPointRepo.findByPeerAndEndpoint(p, ep.getEndpoint()).orElse(ep.linkPeer(p))) // fetch
																														// existing
							.forEach(ep -> endPointRepo.saveAndFlush(ep)); // save individual endpoints
				});
	}
}
