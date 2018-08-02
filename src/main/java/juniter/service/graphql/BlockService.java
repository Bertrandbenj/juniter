package juniter.service.graphql;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import juniter.model.Block;
import juniter.model.persistence.tx.Transaction;
import juniter.repository.BlockRepository;
import juniter.repository.TxRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BlockService  {

	@Autowired
    private BlockRepository blockRepository;


    /**
     * return 10 blocks
     * @param number
     * @return
     */
    @GraphQLQuery(name = "blocks", description="stream latest cars")
    public Stream<Block> getCars() {
        return blockRepository.streamAllBlocks().limit(50L);
    }
    
    /**
     * return a block
     * @param number
     * @return
     */
    @GraphQLQuery(name = "block", description="return the valid block for the given number")
    public Block getBlock(@GraphQLArgument(name = "number") Integer number) {
        return blockRepository.findTop1ByNumber(number).get();
    }
 

//    @GraphQLQuery(name = "car")
//    public Optional<Car> getCarById(@GraphQLArgument(name = "id") Long id) {
//        return carRepository.findById(id);
//    }

//    @GraphQLMutation(name = "saveBlock")
//    public Block saveCar(@GraphQLArgument(name = "car") Block block) {
//        return blockRepository.save(block);
//    }

    @GraphQLMutation(name = "deleteBlock", description="Delete a block, use with care")
    public void deleteBlock(@GraphQLArgument(name = "number") Integer number) {
        blockRepository.delete(blockRepository.findTop1ByNumber(number).get());
    }
}