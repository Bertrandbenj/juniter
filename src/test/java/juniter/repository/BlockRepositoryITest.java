package juniter.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import juniter.core.model.Block;
import juniter.repository.jpa.BlockRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BlockRepositoryITest {

	@Autowired
	private TestEntityManager entityManager;

	@Mock
	private BlockRepository blockRepo;



	@Test
	public void useJava8StreamsWithCustomQuery() {

		final Block block1 = blockRepo.save(new Block());
		final Block block2 = blockRepo.save(new Block());

		final List<Block> result = blockRepo.findByHash("hash");
		assertThat(result.size()).isEqualTo(1);
		assertThat(result.get(0).equals(block1));

	}

	// write test cases here
	@Test
	public void whenFindByNumber_thenReturnBlock() {
		// given
		final Block alex = new Block();

		entityManager.persist(alex);
		entityManager.flush();

		// when
		final Block found = blockRepo.findTop1ByNumber(3).get();

		// then
		assertThat(found.getIssuer()).isEqualTo(alex.getIssuer());
	}


}
