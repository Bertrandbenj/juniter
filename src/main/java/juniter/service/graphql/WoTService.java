package juniter.service.graphql;

import com.google.common.collect.Lists;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import juniter.service.bma.dto.Identity;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WoTService {

	private static final Logger LOG = LoggerFactory.getLogger(WoTService.class);

	@Autowired
	ModelMapper modelMapper;




	@Transactional
	@GraphQLQuery(name = "getIdentity", description = "return an identity")
	public List<Identity> getIdentity(@GraphQLArgument(name = "uid") String uid,
									  @GraphQLArgument(name = "pubkey") String pubkey,
									  @GraphQLArgument(name = "blockstamp") String blockstamp) {
		LOG.info(" GVA - getIdentity");
		return Lists.newArrayList();
	}

	@Transactional
	@GraphQLQuery(name = "getIdentityRaw", description = "return an identity")
	public List<Identity> getIdentityRaw(@GraphQLArgument(name = "uid") String uid,
									  @GraphQLArgument(name = "pubkey") String pubkey,
									  @GraphQLArgument(name = "blockstamp") String blockstamp) {
		LOG.info(" GVA - getIdentityRaw");
		return Lists.newArrayList();
	}




}