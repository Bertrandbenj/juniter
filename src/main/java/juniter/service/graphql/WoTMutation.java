package juniter.service.graphql;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLSubscription;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class WoTMutation {

	private static final Logger LOG = LogManager.getLogger();

	@Transactional
	@GraphQLMutation(name = "identify", description = "post an identity document")
	public void identify(@GraphQLArgument(name = "raw") String raw) {
		LOG.info(" GVA - identify");
	}

	@Transactional
	@GraphQLMutation(name = "certify", description = "post a certification document")
	public void certify(@GraphQLArgument(name = "raw") String raw) {
		LOG.info(" GVA - certify");
	}

	@Transactional
	@GraphQLMutation(name = "membership", description = "post a membership document")
	public void membership(@GraphQLArgument(name = "raw") String raw) {
		LOG.info(" GVA - membership");
	}

	@Transactional
	@GraphQLMutation(name = "peer", description = "post a peer document")
	public void peer(@GraphQLArgument(name = "raw") String raw) {
		LOG.info(" GVA - peer");
	}


	@Transactional
	@GraphQLMutation(name = "transaction", description = "post a transaction document")
	public void transaction(@GraphQLArgument(name = "raw") String raw) {
		LOG.info(" GVA - transaction");
	}

	@Transactional
	@GraphQLMutation(name = "block", description = "post a block document")
	public void block(@GraphQLArgument(name = "raw") String raw) {
		LOG.info(" GVA - block");
	}

	@Transactional
	@GraphQLSubscription(name = "sub", description = "subscribe test")
	public Publisher<Timer> sub(@GraphQLArgument(name = "raw") String raw) {
		LOG.info(" GVA - sub");
			Observable<Timer> observable = Observable
					.interval(1, TimeUnit.SECONDS)
					.flatMap(n -> Observable.create(observableEmitter -> {
						observableEmitter.onNext(new Timer(LocalDateTime.now()));
					}));
			return observable.toFlowable(BackpressureStrategy.LATEST);

	}




}