package juniter.service.async;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import juniter.service.PeeringService;

@Component
public class AppRunner implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

	private final PeeringService peeringService;

	public AppRunner(PeeringService gitHubLookupService) {
		this.peeringService = gitHubLookupService;
	}

	@Override
	public void run(String... args) throws Exception {
		// Start the clock
		long start = System.currentTimeMillis();

		// Kick of multiple, asynchronous lookups
		var peerDoc = CompletableFuture.runAsync(() -> contactPeers());

		// Wait until they are all done
		CompletableFuture.allOf(peerDoc).join();

		// Print results, including elapsed time
		logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
		logger.info("--> " + peerDoc.get());

	}

	@Async
	@Transactional
	public void contactPeers() {
		logger.info("Contacting other peers ");
		CompletableFuture.completedStage(peeringService.findFirstPeers()) //
				.thenCompose(doc -> peeringService.findOtherPeers()) //
				.handle((stPeerDocs, ex) -> {
					if (stPeerDocs != null) {
						logger.info("handle: " + stPeerDocs + ""
								+ stPeerDocs.stream()
										.map(pd -> pd.getPeers().size()+" ")
										//.map(p -> p.endpoints().size() + " endpoints for " + p.getPubkey())
										.collect(Collectors.joining(" ")));
						return stPeerDocs;
					} else {
						logger.warn("Error handle: " + ex.getMessage());
						return "Error handle: " + ex.getMessage();
					}

				})
				;
	}

}