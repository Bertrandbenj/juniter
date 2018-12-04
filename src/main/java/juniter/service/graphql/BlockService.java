package juniter.service.graphql;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import juniter.repository.jpa.BlockRepository;
import juniter.service.bma.dto.Block;
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
public class BlockService {

	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	private BlockRepository blockRepository;

	@Autowired
	ModelMapper modelMapper;

//	@Transactional
//	@GraphQLQuery(name = "bblock", description = "return the valid block for the given number")
//	public Optional<Block> getbBlock(@GraphQLArgument(name = "number") Integer number) {
//		LOG.info(" - /graphql/block/{number} ");
//		return blockRepository.findTop1ByNumber(number);
//	}

	/**
	 * return a block
	 *
	 * @param number the block number
	 * @return the block
	 */
	@Transactional
	@GraphQLQuery(name = "block", description = "return the valid block for the given number")
	public Optional<Block> getBlock(@GraphQLArgument(name = "number") Integer number) {
		LOG.info(" - /graphql/block/{number} ");
		return blockRepository.findTop1ByNumber(number).map(b -> modelMapper.map(b, Block.class));
	}

	@Transactional
	@GraphQLQuery(name = "blocks", description = "return a batchSize Blocks starting from number")
	public List<Block> getBlocks(@GraphQLArgument(name = "number") Integer number,
								 @GraphQLArgument(name = "batchSize") Integer batchSize) {
		try (var bl = blockRepository.streamBlocksFromTo(number, number + batchSize)) {
			return bl.map(b -> modelMapper.map(b, Block.class))//
					.collect(Collectors.toList());
		} catch (final Exception e) {
			LOG.error("blocks ", e);
			return null;
		}
	}

	/**
	 * return a block
	 *
	 * @return the current block
	 */
	@Transactional
	@GraphQLQuery(name = "current", description = "return the current block ")
	public Optional<Block> getCurrent() {
		LOG.info(" - /graphql/block/current ");
		return blockRepository.current().map(b -> modelMapper.map(b, Block.class));
	}

//  @GraphQLQuery(name = "car")
//  public Optional<Car> getCarById(@GraphQLArgument(name = "id") Long id) {
//      return carRepository.findById(id);
//  }
//  @GraphQLMutation(name = "saveBlock")
//  public Block saveCar(@GraphQLArgument(name = "car") Block block) {
//      return blockRepository.save(block);
//  }
//	@GraphQLMutation(name = "deleteBlock", description = "Delete a block, use with care")
//	public void deleteBlock(@GraphQLArgument(name = "number") Integer number) {
//		blockRepository.delete(blockRepository.findTop1ByNumber(number).get());
//	}
}