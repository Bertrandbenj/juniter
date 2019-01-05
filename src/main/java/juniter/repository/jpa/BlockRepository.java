package juniter.repository.jpa;

import juniter.core.model.Block;
import juniter.core.validation.BlockLocalValid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Repository to manage {@link Block} instances.
 */
@Repository
public interface BlockRepository extends JpaRepository<Block, Long>, BlockLocalValid {
	Logger LOG = LogManager.getLogger();

	default Optional<Block> block(Integer number) {
		return findTop1ByNumber(number);
	}

	@Query("select b from Block b where number = ?1 AND hash = ?2 ")
	Optional<Block> block(Integer number, String hash) ;

	default Optional<Block> cachedBlock(Integer bstamp) {

		if(cache.containsKey(bstamp) )
			return Optional.of(cache.get(bstamp));

		var res = block(bstamp);

		res.ifPresent(b->{
			 cache.put(b.getNumber(), b);
		});

		return  res;
	}


	@Query("select number from Block")
	List<Integer> blockNumbers();

    @Query("select number from Block WHERE number NOT IN :notIn")
    List<Integer> blockNumbers(List<Integer> notIn);

	/**
	 * highest block by block number in base
	 *
	 * @return Optional<Block>
	 */
	default Optional<Block> current() {
		return findTop1ByOrderByNumberDesc();
	}

	default Integer currentBlockNumber() {
		return current().map(Block::getNumber).orElse(-1);
	}


	Map<Integer, Block> cache = new HashMap<>();




	Stream<Block> findByNumberIn(List<Integer> number);

	Stream<Block> findTop10ByOrderByNumberDesc();

	// TODO clean this up
	Optional<Block> findTop1ByNumber(Integer number);

	/**
	 * Alias for current()
	 *
	 * @return Optional<Block>
	 */
	Optional<Block> findTop1ByOrderByNumberDesc();

	default Optional<Block> localSave(Block block) throws AssertionError {

		if (checkBlockisLocalValid(block)){
			if(block.size()>0){
				cache.put(block.getNumber(), block);
			}
			try{
				return Optional.of(save(block));
			}catch(Exception e){
				LOG.error("Error saving block "+block.getNumber());
				return Optional.empty();
			}
		}


		return Optional.empty();
	}


    default <S extends Block> Block override(S block) {
		final var existingBlock = findTop1ByNumber(block.getNumber());
		final var bl = existingBlock.orElse(block);
		return save(bl);
	}

	/**
	 * Saves the given {@link Block}. unsafe
	 *
	 * @param block the block to save
	 * @return the block saved
	 */
	@Override
	<S extends Block> S save(S block) throws GenericJDBCException;

	@Query("select c from Block c")
	Stream<Block> streamAllBlocks();

	@Query("select c from Block c where number >= ?1 AND number < ?2 ")
	Stream<Block> streamBlocksFromTo(int from, int to);

	default Stream<Block> with(Predicate<Block> predicate) {
		return streamAllBlocks() //
				.filter(predicate) //
				.sorted((b1, b2) -> b1.getNumber().compareTo(b2.getNumber()));
	}

	@Query("select c from Block c where number >= ?1 AND number < ?2")
	List<Block> blocksFromTo(int from, int to);

}