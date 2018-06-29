package juniter.repository.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import juniter.model.Block;
import juniter.repository.BlockRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BlockRepositoryTest {
 
    @Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private BlockRepository blockRepository;
    
    
    
 
    // write test cases here
    @Test
    public void whenFindByNumber_thenReturnBlock() {
        // given
        Block alex = new Block();
        
        entityManager.persist(alex);
        entityManager.flush();
     
        // when
        Block found = blockRepository.findByNumber(3).get();
     
        // then
        assertThat(found.getIssuer())
          .isEqualTo(alex.getIssuer());
    }
    
	@Test
	public void useJava8StreamsWithCustomQuery() {

		Block block1 = blockRepository.save(new Block());
		Block block2 = blockRepository.save(new Block());

		List<Block> result = blockRepository.findByHash("hash");
		assertThat(result.size()).isEqualTo(1);
		assertThat(result.get(0).equals(block1));

	}
	

}
