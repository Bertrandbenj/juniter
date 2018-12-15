package juniter.service.gva.wot;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WoTMutation {

	private static final Logger LOG = LogManager.getLogger();

	@Transactional
	@GraphQLMutation(name = "submitIdentity", description = "post an identity document")
	public void submitIdentity(@GraphQLArgument(name = "raw") String raw) {
		LOG.info(" GVA - identify");
	}

	@Transactional
	@GraphQLMutation(name = "submitCertification", description = "post a certification document")
	public void submitCertification(@GraphQLArgument(name = "raw") String raw) {
		LOG.info(" GVA - certify");
	}

	@Transactional
	@GraphQLMutation(name = "submitMembership", description = "post a membership document")
	public void submitMembership(@GraphQLArgument(name = "raw") String raw) {
		LOG.info(" GVA - membership");
	}

	@Transactional
	@GraphQLMutation(name = "submitTransaction", description = "post a transaction document")
	public void submitTransaction(@GraphQLArgument(name = "raw") String raw) {
		LOG.info(" GVA - transaction");
	}






}