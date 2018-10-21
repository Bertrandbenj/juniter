package juniter.service.graphql;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import juniter.repository.jpa.BlockRepository;
import juniter.service.bma.model.BlockDTO;

@Service
public class BlockService {

	private static final Logger logger = LoggerFactory.getLogger(BlockService.class);

	@Autowired
	private BlockRepository blockRepository;

	@Autowired
	ModelMapper modelMapper;

//	@Transactional
//	@GraphQLQuery(name = "bblock", description = "return the valid block for the given number")
//	public Optional<Block> getbBlock(@GraphQLArgument(name = "number") Integer number) {
//		logger.info(" - /graphql/block/{number} ");
//		return blockRepository.findTop1ByNumber(number);
//	}

	/**
	 * return a block
	 *
	 * @param number
	 * @return
	 */
	@Transactional
	@GraphQLQuery(name = "block", description = "return the valid block for the given number")
	public Optional<BlockDTO> getBlock(@GraphQLArgument(name = "number") Integer number) {
		logger.info(" - /graphql/block/{number} ");
		return blockRepository.findTop1ByNumber(number).map(b -> modelMapper.map(b, BlockDTO.class));
	}

	@Transactional
	@GraphQLQuery(name = "blocks", description = "return a batchSize Blocks starting from number")
	public List<BlockDTO> getBlocks(@GraphQLArgument(name = "number") Integer number,
			@GraphQLArgument(name = "batchSize") Integer batchSize) {
		try (var bl = blockRepository.streamBlocksFromTo(number, number + batchSize)) {
			return bl.map(b -> modelMapper.map(b, BlockDTO.class))//
					.collect(Collectors.toList());
		} catch (final Exception e) {
			logger.error("blocks ", e);
			return null;
		}
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