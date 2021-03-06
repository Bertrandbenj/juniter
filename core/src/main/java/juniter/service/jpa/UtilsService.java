package juniter.service.jpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import juniter.core.event.LogMemory;
import juniter.core.model.dto.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.InetAddress;
import java.util.GregorianCalendar;
import java.util.stream.IntStream;

@Service
public class UtilsService {

    private static final Logger LOG = LogManager.getLogger(UtilsService.class);

    @Value("${juniter.dataPath:${user.home}/.config/juniter/data/}")
    private String dataPath;

    @Autowired
    private ApplicationEventPublisher coreEventBus;



    @Autowired
    private JPABlockService blockService;

    @Transactional(readOnly = true)
    @Async("AsyncJuniterPool")
    public void dumpJsonRows() {
        LOG.info("starting dumpJsonRows");
        try {

            var dumpSize = 5000;

            IntStream.iterate(0, x -> x <= blockService.currentBlockNumber(), x -> x + dumpSize)
                    .parallel()
                    .forEach(i -> {
                        final var end = (i + dumpSize - 1);
                        if (i % dumpSize == 0) {
                            final var file = dataPath + "dump/blockchain_" + i + "to" + end + ".jsonrows";
                            write(i, end, file);
                        }
                    });
        } catch (final Exception e) {

            LOG.info("erroring ");
            e.printStackTrace();
        }
        LOG.info("finished dumpJsonRows");
    }

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    @Async("AsyncJuniterPool")
    private void write(int from, int to, String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        //BufferedWriter bw;
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName))))) {

            var blocks = blockService.listBlocksFromTo(from, to);
            blocks.forEach(block -> {
                try {
                    String output = objectMapper.writeValueAsString(modelMapper.map(block, Block.class));
                    bw.write(output + "\n");
                } catch (final Exception e) {
                    LOG.warn("erroring writing jsonrows ", e);
                }

            });

        } catch (FileNotFoundException e) {
            LOG.warn("FileNotFoundException jsonrows ", e);
        } catch (IOException e) {
            LOG.warn("IOException jsonrows ", e);
        }

    }


    @Scheduled(fixedRate = 60 * 1000, initialDelay = 60 * 1000)
    public void checkMemory() {
        coreEventBus.publishEvent(new LogMemory());
    }


    public void pingRTT(String ipAddress) {
        try {
            InetAddress inet = InetAddress.getByName(ipAddress);

            LOG.info("Sending Ping Request to " + ipAddress);

            long finish = 0;
            long start = new GregorianCalendar().getTimeInMillis();

            if (inet.isReachable(5000)) {
                finish = new GregorianCalendar().getTimeInMillis();
                LOG.info("Ping RTT: " + (finish - start + "ms"));
            } else {
                LOG.info(ipAddress + " NOT reachable.");
            }
        } catch (Exception e) {
            LOG.info("Exception:" + e.getMessage());
        }
    }


}
