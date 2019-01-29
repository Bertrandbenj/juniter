package juniter.service.gva.wot;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import juniter.core.model.wot.Identity;
import juniter.repository.jpa.index.IINDEXRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WoTService {

	private static final Logger LOG = LogManager.getLogger();

	@Autowired ModelMapper modelMapper;

	@Autowired IINDEXRepository iRepo;


	@Transactional
	@GraphQLQuery(name = "member", description = "return an identity")
	public Identity member(@GraphQLArgument(name = "uid") String uid,
								 @GraphQLArgument(name = "pubkey") String pubkey) {
		LOG.info(" GVA - getIdentity");
		return modelMapper.map(iRepo.byUidOrPubkey(uid, pubkey), Identity.class);
	}

	@Transactional
	@GraphQLQuery(name = "pendingIdentities", description = "return an identity")
	@GraphQLNonNull
	public List<@GraphQLNonNull PendingIdentity> pendingIdentities(@GraphQLNonNull @GraphQLArgument(name = "search") String search) {
		LOG.info(" GVA - pendingIdentities");
		return iRepo.byUidOrPubkey(search, search)
				.stream()
					.map(x-> modelMapper.map(x, PendingIdentity.class))
					.collect(Collectors.toList());
	}

	@Transactional
	@GraphQLQuery(name = "pendingIdentityByHash", description = "return an identity")
	public PendingIdentity pendingIdentityByHash(@GraphQLNonNull @GraphQLArgument(name = "hash") String hash) {
		LOG.info(" GVA - pendingIdentityByHash");
		return modelMapper.map(iRepo.pendingIdentityByHash(hash), PendingIdentity.class);
	}

	// 						=============  Next comes the mutations =============

	@Transactional
	@GraphQLMutation(name = "submitIdentity", description = "post an identity document")
	@GraphQLNonNull
	public PendingIdentity submitIdentity(@GraphQLNonNull @GraphQLArgument(name = "rawDocument") String raw) {
		LOG.info(" GVA - submitIdentity");
		return new PendingIdentity();
	}

	@Transactional
	@GraphQLMutation(name = "submitCertification", description = "post a certification document")
	@GraphQLNonNull
	public PendingIdentity submitCertification(@GraphQLNonNull @GraphQLArgument(name = "rawDocument") String raw) {
		LOG.info(" GVA - submitCertification");
		return new PendingIdentity();
	}

	@Transactional
	@GraphQLMutation(name = "submitMembership", description = "post a membership document")
	@GraphQLNonNull
	public PendingIdentity submitMembership(@GraphQLNonNull @GraphQLArgument(name = "rawDocument") String raw) {
		LOG.info(" GVA - submitMembership");
		return new PendingIdentity();
	}



}