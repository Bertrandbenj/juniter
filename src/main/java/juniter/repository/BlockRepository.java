package juniter.repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import juniter.model.Block;

/**
 * Repository to manage {@link Block} instances.
 */
@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

	/**
	 * Special customization of {@link CrudRepository#findOne(java.io.Serializable)}
	 * to return a JDK 8 {@link Optional}.
	 *
	 * @param id
	 * @return
	 */
	@Override
	Optional<Block> findById(Long id);

	Optional<Block> findByNumber(Integer number);
		
	Stream<Block> findByNumberIn(List<Integer> number);

	Optional<Block> findTop1ByOrderByNumberDesc();

	Stream<Block> findTop10ByOrderByNumberDesc();

	
	/**
	 * Saves the given {@link Block}.
	 *
	 * @param block
	 * @return
	 */
	@Override
	<S extends Block> S save(S block);

	default <S extends Block> Block override(S block) {
		var existingBlock = findByNumber(block.getNumber());
		var bl = existingBlock.orElse(block);
		return save(bl);
	};
	
	
	
	List<Block> findByHash(String hash);

	/**
	 * Sample method to demonstrate support for {@link Stream} as a return type with
	 * a custom query. The query is executed in a streaming fashion which means that
	 * the method returns as soon as the first results are ready.
	 *
	 * @return
	 */
	@Query("select c from Block c")
	Stream<Block> streamAllBlocks();
	

	default Stream<Block> with(Predicate<Block> predicate) {
		return streamAllBlocks().filter(predicate)
				.sorted((b1, b2) -> b1.getNumber().compareTo(b2.getNumber()));
	};




	@Async
	CompletableFuture<List<Block>> readAllBy();

	

	

}