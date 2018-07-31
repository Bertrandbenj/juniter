package juniter.service.graphql;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import juniter.model.Block;
import juniter.repository.BlockRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService  {

	@Autowired
    private BlockRepository carRepository;

    /**
     * return 10 blocks
     * @param number
     * @return
     */
    @GraphQLQuery(name = "blocks")
    public List<Block> getCars() {
        return carRepository.streamAllBlocks().limit(10L).collect(Collectors.toList());
    }
    
    /**
     * return a block
     * @param number
     * @return
     */
    @GraphQLQuery(name = "block")
    public Block getBlock(@GraphQLArgument(name = "number") Integer number) {
        return carRepository.findTop1ByNumber(number).get();
    }

//    @GraphQLQuery(name = "car")
//    public Optional<Car> getCarById(@GraphQLArgument(name = "id") Long id) {
//        return carRepository.findById(id);
//    }

    @GraphQLMutation(name = "saveBlock")
    public Block saveCar(@GraphQLArgument(name = "car") Block block) {
        return carRepository.save(block);
    }

    @GraphQLMutation(name = "deleteCar")
    public void deleteCar(@GraphQLArgument(name = "id") Long id) {
        carRepository.deleteById(id);
    }
}