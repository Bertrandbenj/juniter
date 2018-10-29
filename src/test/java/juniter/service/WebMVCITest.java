package juniter.service;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import juniter.core.model.Block;
import juniter.core.utils.TimeUtils;
import juniter.core.validation.LocalValid;

@RunWith(SpringRunner.class)
//@SpringBootTest(
//	    classes = BlockchainService.class,
//	    webEnvironment = WebEnvironment.RANDOM_PORT
//	)
@SpringBootTest
@AutoConfigureMockMvc
public class WebMVCITest implements LocalValid {

	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	private MockMvc mvc;

	@Value("${juniter.network.bulkSize:500}")
	private Integer bulkSize;

	final ObjectMapper jsonMapper = new ObjectMapper();

	AtomicInteger intt = new AtomicInteger();

	List<String> erroringBlock = new ArrayList<>();

	@Test
	public void fetchingFirstBlock() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/blockchain/block/0"))//
		.andExpect(status().isOk())//
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//
		.andExpect(jsonPath("$.currency", is("g1")))
		.andExpect(jsonPath("$.number", is(0)));
	}

	@Before
	public void init() {
	}

	@Test
	public void testBMABlockOutputValidity() {

		final long time = System.currentTimeMillis();

		final var current = 160000;
		final DecimalFormat decimalFormat = new DecimalFormat("##.###%");

		final var nbPackage = Integer.divideUnsigned(current, bulkSize);

		IntStream.range(0, nbPackage)// get nbPackage Integers
		.map(nbb -> (nbb * bulkSize)) // with an offset of bulkSize
		.boxed() //
		.sorted() //
		.parallel() // parallel stream if needed
		.map(i -> "/blockchain/blocks/" + bulkSize + "/" + i) // url
		.forEach(url -> testOneChunk(url));

		final long delta = System.currentTimeMillis() - time;

		final var perBlock = delta / current;
		final var estimate = current * perBlock;
		LOG.info(", elapsed time: " + TimeUtils.format(delta) //
		+ " which is " + perBlock + " ms per block validated, " //
		+ "estimating: " + TimeUtils.format(estimate));

		LOG.info("blocks erroring :"
				+ erroringBlock.stream().sorted().map(i -> i.toString().split("\n")[0])
						.collect(Collectors.joining("\n")));

	}

	@Test
	public void testOne() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/blockchain/block/52"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andDo(result -> {
			final var body = result.getResponse().getContentAsString();
			final Block block = jsonMapper.readValue(body, Block.class);
			assertBlock(block);
		});
	}

	public void testOneChunk(String url) {

		try {
			mvc.perform(MockMvcRequestBuilders.get(url))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(result -> {
				final var body = result.getResponse().getContentAsString();
				final List<Block> blocks = jsonMapper.readValue(body,
						new TypeReference<List<Block>>() {
				});
				blocks.forEach(block -> {
					try {
						assertBlock(block);
					} catch (final AssertionError e) {
						erroringBlock.add(block.getNumber() + " - " + e.getMessage());

					}

				});
			});
		} catch (final Exception e) {

			LOG.info("erroring " + erroringBlock.stream().collect(Collectors.joining("\n")));
			e.printStackTrace();
		}
	}

}
