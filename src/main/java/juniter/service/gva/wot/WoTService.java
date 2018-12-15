package juniter.service.gva.wot;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import juniter.core.model.wot.Identity;
import juniter.repository.jpa.index.CINDEXRepository;
import juniter.repository.jpa.index.IINDEXRepository;
import juniter.repository.jpa.index.MINDEXRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class WoTService {

	private static final Logger LOG = LoggerFactory.getLogger(WoTService.class);

	@Autowired ModelMapper modelMapper;

	@Autowired IINDEXRepository iRepo;

	@Autowired MINDEXRepository mRepo;

	@Autowired CINDEXRepository cRepo;



	@Transactional
	@GraphQLQuery(name = "member", description = "return an identity")
	public Identity member(@GraphQLArgument(name = "uid") String uid,
								 @GraphQLArgument(name = "pubkey") String pubkey) {
		LOG.info(" GVA - getIdentity");
		return modelMapper.map(iRepo.memberByUidOrPubkey(uid, pubkey), Identity.class);
	}

	@Transactional
	@GraphQLQuery(name = "pendingIdentities", description = "return an identity")
	@GraphQLNonNull
	public List<@GraphQLNonNull PendingIdentity> pendingIdentities(@GraphQLNonNull @GraphQLArgument(name = "search") String search) {
		LOG.info(" GVA - pendingIdentities");
		return Collections.singletonList(new PendingIdentity());
	}

	@Transactional
	@GraphQLQuery(name = "pendingIdentityByHash", description = "return an identity")
	public PendingIdentity pendingIdentityByHash(@GraphQLNonNull @GraphQLArgument(name = "hash") String hash) {
		LOG.info(" GVA - pendingIdentityByHash");
		return modelMapper.map(iRepo.pendingIdentityByHash(hash), PendingIdentity.class);
	}





}