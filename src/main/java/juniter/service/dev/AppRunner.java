package juniter.service.dev;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import juniter.service.bma.PeeringService;

@ConditionalOnExpression("${juniter.bma.enabled:false}")
@Component
public class AppRunner implements CommandLineRunner {

	private static final Logger logger = LogManager.getLogger();

	private final PeeringService peeringService;

	public AppRunner(PeeringService gitHubLookupService) {
		peeringService = gitHubLookupService;
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
								+ stPeerDocs.stream().map(pd -> pd.getPeers().size() + " ")
										// .map(p -> p.endpoints().size() + " endpoints for " + p.getPubkey())
										.collect(Collectors.joining(" ")));
						return stPeerDocs;
					} else {
						logger.warn("Error handle: " + ex.getMessage());
						return "Error handle: " + ex.getMessage();
					}

				});
	}

	@Override
	public void run(String... args) throws Exception {
		// Start the clock
		final long start = System.currentTimeMillis();

		// Kick of multiple, asynchronous lookups
		// var peerDoc = CompletableFuture.runAsync(() -> contactPeers());

		// Wait until they are all done
		// CompletableFuture.allOf(peerDoc).join();

		// Print results, including elapsed time
		logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
		// logger.info("--> " + peerDoc.get());

	}

}