package juniter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import juniter.core.model.dbo.DBBlock;
import juniter.core.utils.TimeUtils;
import juniter.core.validation.BlockLocalValid;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@SpringBootTest(
//	    classes = BlockchainService.class,
//	    webEnvironment = WebEnvironment.RANDOM_PORT
//	)
@SpringBootTest
@AutoConfigureMockMvc
public class WebMVCITest implements BlockLocalValid {

    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private MockMvc mvc;

    @Value("${juniter.network.bulkSize:500}")
    private Integer bulkSize;


    @Value("${juniter.dataPath:/tmp/juniter/data/}")
    private String dataPath;

    final ObjectMapper jsonMapper = new ObjectMapper();

    AtomicInteger intt = new AtomicInteger();

    List<String> erroringBlock = new ArrayList<>();


    private void dirtyReload(Integer bnumber) {
        try {
            LOG.error("Deleting to reload node #" + bnumber);

            mvc.perform(MockMvcRequestBuilders
                    .get("/blockchain/deleteBlock/" + bnumber))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        } catch (final Exception e1) {
            LOG.error("Retried but failed ", e1);
            erroringBlock.add(bnumber + " - " + e1.getMessage().split("\n")[0]);

        }

    }

    @Test
    public void test90830() {

    }

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
                + " which is " + perBlock + " ms per node validated, " //
                + "estimating: " + TimeUtils.format(estimate));

        LOG.info("blocks erroring :" + erroringBlock.stream().collect(Collectors.joining("\n")));

    }


    public void testOne(Integer i) throws AssertionError, Exception {

        mvc.perform(MockMvcRequestBuilders.get("/blockchain/block/" + i))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(result -> {
                    final var body = result.getResponse().getContentAsString();
                    final DBBlock block = jsonMapper.readValue(body, DBBlock.class);
                    assertBlockLocalValid(block, true);
                });
    }

    public void testOneChunk(String url) {

        try {
            mvc.perform(MockMvcRequestBuilders.get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(result -> {
                        final var body = result.getResponse().getContentAsString();
                        final List<DBBlock> blocks = jsonMapper.readValue(body,
                                new TypeReference<List<DBBlock>>() {
                                });
                        blocks.forEach(block -> {
                            try {
                                assertBlockLocalValid(block, true);
                            } catch (final AssertionError e) {
                                dirtyReload(block.getNumber());
                            }

                        });
                    });
        } catch (final Exception e) {

            LOG.info("erroring " + erroringBlock.stream().collect(Collectors.joining("\n")));
            e.printStackTrace();
        }
    }


    @Test
    public void testSome() {

        final List<Integer> numbers = List.of(15144, 109327, 87566, 90830, 85448);

        numbers.forEach(n -> dirtyReload(n));

        numbers.forEach(i -> {
            try {
                testOne(i);
            } catch (final AssertionError e) {
                e.printStackTrace();
            } catch (final Exception e) {
                LOG.error("testSome ", e);
            }
        });
    }

}
