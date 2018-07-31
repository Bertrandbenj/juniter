package juniter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import juniter.service.FileBlocksService;

@RunWith(SpringRunner.class)
//@SpringBootTest(
//	    classes = BlockchainService.class,
//	    webEnvironment = WebEnvironment.RANDOM_PORT
//	)
@WebMvcTest(FileBlocksService.class)
public class BlockServiceTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private FileBlocksService blocks;

	@Before
	public void setUp() {
	}

	@Test
	public void testLoad() {
		assertThat(blocks._0).isNotNull();
		assertThat(blocks._0.getJoiners().size()).isEqualTo(59);
		assertThat(blocks._0.getNumber()).isEqualTo(0);

		assertThat(blocks._127128).isNotNull();
		assertThat(blocks._127128.getTransactions().size()).isEqualTo(9);
		assertThat(blocks._127128.getNumber()).isEqualTo(127128);
	}

	@Test
	public void fetchingFirstBlock() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/blockchain/block/0"))//
				.andExpect(status().isOk())//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//
				.andExpect(jsonPath("$.currency", is("g1")));
	}

	@Test
	public void fetching10Blocks() {

	}

//	@Autowired
//	private MockMvc mvc;
//
//	@Test
//	public void getHello() throws Exception {
//		mvc.perform(MockMvcRequestBuilders.get("/blockchain/").accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk());
//	}

}
