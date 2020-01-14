package juniter.service.gva;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import juniter.core.model.dto.node.Block;
import juniter.service.core.BlockService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GVABlockService {

	private static final Logger LOG = LogManager.getLogger(GVABlockService.class);

	@Autowired
	private BlockService blockService;

	@Autowired
	private ModelMapper modelMapper;


	/**
	 * return a node
	 *
	 * @param number the node number
	 * @return the node at 'number' or currentChained of null
	 */
	@Transactional
	@GraphQLQuery(name = "node", description = "return the valid node for the given number or currentChained if null ")
	public Optional<Block> block(@GraphQLArgument(name = "number") Integer number) {
		LOG.info(" - /graphql/block/{number} ");
		if(number==null){
			return blockService.currentChained().map(b -> modelMapper.map(b, Block.class));
		}else{
			return blockService.findTop1ByNumber(number).map(b -> modelMapper.map(b, Block.class));
		}
	}

	@Transactional
	@GraphQLQuery(name = "blocks", description = "return 'batchSize' Blocks starting from 'number' ")
	public List<Block> blocks(@GraphQLArgument(name = "number") Integer number,
								 @GraphQLArgument(name = "batchSize") Integer batchSize) {

		LOG.info(" - /graphql/blocks/{batch}/{from} ");

		try (var bl = blockService.streamBlocksFromTo(number, number + batchSize)) {
			return bl.map(b -> modelMapper.map(b, Block.class))
					.collect(Collectors.toList());
		} catch (final Exception e) {
			LOG.error("blocks ", e);
			return null;
		}
	}

    /**
     * the endpoint's currency
     *
     * @return a currency name
     */
    @Transactional
    @GraphQLQuery(name = "currency", description = "the endpoint's currency name")
    @GraphQLNonNull
    public String currency() {
        return "g1";
    }

//	@Transactional
//	@GraphQLMutation(name = "node", description = "post a node document")
//	public void node(@GraphQLArgument(name = "rawDocument") String rawDocument) {
//		LOG.info(" GVA - node");
//	}
}