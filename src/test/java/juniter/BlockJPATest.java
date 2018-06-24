package juniter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import juniter.model.Block;
import juniter.repository.BlockRepository;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class BlockJPATest {

	@Autowired
	BlockRepository repository;

	/**
	 * Streaming data from the store by using a repository method that returns a
	 * {@link Stream}. Note, that since the resulting {@link Stream} contains state
	 * it needs to be closed explicitly after use!
	 */
	@Test
	public void useJava8StreamsWithCustomQuery() {

		Block block1 = repository.save(new Block());
		Block block2 = repository.save(new Block());

		List<Block> result = repository.findByHash("hash");
		assertThat(result.size()).isEqualTo(1);
		assertThat(result.get(0).equals(block1));

	}

}
