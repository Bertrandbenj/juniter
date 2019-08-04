package juniter.service;


import juniter.core.event.CoreEvent;
import juniter.service.ws2p.WebSocketPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@ConditionalOnExpression("${juniter.useRobot:false}")
public class ForkHead implements ApplicationListener<CoreEvent>  {


    private static final Logger LOG = LogManager.getLogger(ForkHead.class);

    @Autowired
    private Index index;

    @Autowired
    private BlockService blockService;

    @Autowired
    private WebSocketPool websockets;

    @PostConstruct
    public void init() {
        LOG.info("Init ForkHead");

//        websockets.reconnectWebSockets();

//         index.indexUntil(blockService.currentBlockNumber(), false);
    }


    @Override
    public void onApplicationEvent(CoreEvent event) {
        LOG.info("onApplicationEvent " + event);


    }
}