package juniter.repository;

import juniter.core.model.dbo.DBBlock;
import juniter.repository.jpa.block.BlockRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DBBlockRepositoryITest {

	@Autowired
	private TestEntityManager entityManager;

	@Mock
	private BlockRepository blockRepo;



	@Test
	public void useJava8StreamsWithCustomQuery() {

		final DBBlock block1 = blockRepo.save(new DBBlock());
		final DBBlock block2 = blockRepo.save(new DBBlock());

		final List<DBBlock> result = blockRepo.findAll();
		assertThat(result.size()).isEqualTo(1);
		assertThat(result.get(0).equals(block1));

	}

	// write test cases here
	@Test
	public void whenFindByNumber_thenReturnBlock() {
		// given
		final DBBlock alex = new DBBlock();

		entityManager.persist(alex);
		entityManager.flush();

		// when
		final DBBlock found = blockRepo.findTop1ByNumber(3).get();

		// then
		assertThat(found.getIssuer()).isEqualTo(alex.getIssuer());
	}


}
