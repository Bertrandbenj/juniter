package juniter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import juniter.repository.jpa.BlockRepository;
import juniter.service.bma.BlockchainService;
import juniter.service.bma.dto.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.stream.Collectors;

@Service
public class UtilsService {

    private static final Logger LOG = LogManager.getLogger();

    @Value("${juniter.dataPath:/tmp/juniter/data/}")
    private String dataPath;

    @Autowired
    BlockchainService blockService;

    @Autowired
    BlockRepository blockRepo;

    @Transactional(readOnly = true)
    @Async
    public void dumpJsonRows() {
        LOG.info("starting dumpJsonRows");
        try {

            var dumpSize = 15000;
            for (int i = 0; i <= blockService.current().getNumber(); i+=dumpSize ) {
                final var finali = i;
                final var end = (i+dumpSize-1);
                if(i%dumpSize==0){
                   final var  file = dataPath + "dump/blockchain_"+i+"to"+end+".jsonrows";
                   new Thread(()->write(finali, end, file)).start();
                }
            }
        } catch (final Exception e) {

            LOG.info("erroring ");
            e.printStackTrace();
        }
        LOG.info("finished dumpJsonRows");
    }

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    @Async
    private void write(int from, int to , String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();

        try (var bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName))))){

            try(var blocks = blockRepo.streamBlocksFromTo(from, to)) {
                blocks.collect(Collectors.toList()).forEach(block -> {
                    try {
                        String output = objectMapper.writeValueAsString(modelMapper.map(block, Block.class));
                        bw.write(output + "\n");
                    } catch (final Exception e) {
                        LOG.warn("erroring writing jsonrows ", e);
                    }

                });
            }

        }catch (FileNotFoundException e) {
            LOG.warn("FileNotFoundException jsonrows ", e);
        } catch (IOException e) {
            LOG.warn("IOException jsonrows ", e);
        }

    }
}
