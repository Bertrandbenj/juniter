package juniter.service;


import juniter.repository.jpa.block.BlockRepository;
import juniter.service.ws2p.WebSocketPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@ConditionalOnExpression("${juniter.useRobot:false}")
public class Robot {
    private static final Logger LOG = LogManager.getLogger(Robot.class);

    @Autowired
    private Index index;

    @Autowired
    private BlockRepository blockRepo;

    @Autowired
    private WebSocketPool websockets;

    @PostConstruct
    public void init(){
        LOG.info("Init Robot");

//        websockets.reconnectWebSockets();

//         index.indexUntil(blockRepo.currentBlockNumber(), false);
    }

}