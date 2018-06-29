package service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import juniter.service.BlockchainService;
import juniter.utils.Constants;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DataJpaTest
public class BlockServiceTest {

//	@Value("${juniter.network.remote:" + Constants.Defaults.NODE + "}")
//	public String default_node;

	@Autowired
	BlockchainService blockService;

	@Test
	public void fetching10Blocks() {
		var blocks = blockService.fetchBlocks(Constants.Defaults.NODE+"blockchain/blocks/10/50");
		assertThat(blocks.size() == 10);
			
		blocks.forEach(bl->{
			assertThat(true);
		});
	}

	@Autowired
	private MockMvc mvc;

	@Test
	public void getHello() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/blockchain/").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

}
