package juniter.repository.jpa;

import juniter.core.model.Block;
import juniter.core.validation.BlockLocalValid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Repository to manage {@link Block} instances.
 */
@Repository
public interface BlockRepository extends JpaRepository<Block, Long>, BlockLocalValid {

	default Optional<Block> block(Integer number) {
		return findTop1ByNumber(number);
	}

	@Query("select number from Block")
	List<Integer> blockNumbers();

    @Query("select number from Block WHERE number NOT IN :notIn")
    List<Integer> blockNumbers(List<Integer> notIn);

	/**
	 * highest block by block number in base
	 *
	 * @return
	 */
	default Optional<Block> current() {
		return findTop1ByOrderByNumberDesc();
	}

	default Integer currentBlockNumber() {
		return current().map(b -> b.getNumber()).orElse(140000);
	};

	List<Block> findByHash(String hash);

	//	@Override
	//	Optional<Block> findById(Long id);

	Stream<Block> findByNumberIn(List<Integer> number);

	Stream<Block> findTop10ByOrderByNumberDesc();

	// TODO clean this up
	Optional<Block> findTop1ByNumber(Integer number);

	/**
	 * Alias for current()
	 *
	 * @return
	 */
	Optional<Block> findTop1ByOrderByNumberDesc();

	default Optional<Block> localSave(Block block) throws AssertionError {

		if (checkBlockisLocalValid(block))
			return Optional.of(save(block));

		return Optional.empty();
	};



	default <S extends Block> Block override(S block) {
		final var existingBlock = findTop1ByNumber(block.getNumber());
		final var bl = existingBlock.orElse(block);
		return save(bl);
	}

	@Async
	CompletableFuture<List<Block>> readAllBy();

	/**
	 * Saves the given {@link Block}.
	 *
	 * @param block
	 * @return the block saved
	 */
	@Override
	<S extends Block> S save(S block);

	@Query("select c from Block c")
	Stream<Block> streamAllBlocks();

	@Query("select c from Block c where number >= ?1 AND number < ?2")
	Stream<Block> streamBlocksFromTo(int from, int to);

	default Stream<Block> with(Predicate<Block> predicate) {
		return streamAllBlocks() //
				.filter(predicate) //
				.sorted((b1, b2) -> b1.getNumber().compareTo(b2.getNumber()));
	}

}