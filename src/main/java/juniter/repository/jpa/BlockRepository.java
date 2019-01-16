package juniter.repository.jpa;

import juniter.core.model.DBBlock;
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
 * Repository to manage {@link DBBlock} instances.
 */
@Repository
public interface BlockRepository extends JpaRepository<DBBlock, Long>, BlockLocalValid {
	Logger LOG = LogManager.getLogger();

	default Optional<DBBlock> block(Integer number) {
		return findTop1ByNumber(number);
	}

	@Query("select b from DBBlock b where number = ?1 AND hash = ?2 ")
	Optional<DBBlock> block(Integer number, String hash) ;

	default Optional<DBBlock> cachedBlock(Integer bstamp) {

		if(cache.containsKey(bstamp) )
			return Optional.of(cache.get(bstamp));

		var res = block(bstamp);

		res.ifPresent(b->{
			 //cache.put(b.getNumber(), b);
		});

		return  res;
	}


	@Query("select number from DBBlock")
	List<Integer> blockNumbers();

    @Query("select number from DBBlock WHERE number NOT IN :notIn")
    List<Integer> blockNumbers(List<Integer> notIn);

	/**
	 * highest block by block number in base
	 *
	 * @return Optional<DBBlock>
	 */
	default Optional<DBBlock> current() {
		return findTop1ByOrderByNumberDesc();
	}

	default Integer currentBlockNumber() {
		return current().map(DBBlock::getNumber).orElse(-1);
	}


	Map<Integer, DBBlock> cache = new HashMap<>();




	Stream<DBBlock> findByNumberIn(List<Integer> number);

	Stream<DBBlock> findTop10ByOrderByNumberDesc();

	// TODO clean this up
	Optional<DBBlock> findTop1ByNumber(Integer number);

	/**
	 * Alias for current()
	 *
	 * @return Optional<DBBlock>
	 */
	Optional<DBBlock> findTop1ByOrderByNumberDesc();

	default Optional<DBBlock> localSave(DBBlock block) throws AssertionError {

		if (checkBlockIsLocalValid(block)){

			try{
				return Optional.of(save(block));
			}catch(Exception e){
				LOG.error("Error saving block "+block.getNumber());
				return Optional.empty();
			}
		}


		return Optional.empty();
	}


    default <S extends DBBlock> DBBlock override(S block) {
		final var existingBlock = findTop1ByNumber(block.getNumber());
		final var bl = existingBlock.orElse(block);
		return save(bl);
	}

	/**
	 * Saves the given {@link DBBlock}. unsafe
	 *
	 * @param block the block to save
	 * @return the block saved
	 */
	@Override
	<S extends DBBlock> S save(S block) throws GenericJDBCException;

	@Query("select c from DBBlock c")
	Stream<DBBlock> streamAllBlocks();

	@Query("select c from DBBlock c where number >= ?1 AND number < ?2 ")
	Stream<DBBlock> streamBlocksFromTo(int from, int to);

	default Stream<DBBlock> with(Predicate<DBBlock> predicate) {
		return streamAllBlocks()
				.filter(predicate)
				.sorted();
	}

	@Query("select c from DBBlock c where number >= ?1 AND number < ?2")
	List<DBBlock> blocksFromTo(int from, int to);

}