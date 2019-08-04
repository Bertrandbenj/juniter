package juniter.service;

import juniter.core.event.CoreEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class CoreEventLogger implements ApplicationListener<CoreEvent> {


    private Logger LOG = LogManager.getLogger(CoreEventLogger.class);


    @Override
    public void onApplicationEvent(CoreEvent event) {
        LOG.info("onApplicationEvent " + event.getName() + " " + event.getMessage() + " " + event.getWhat());


    }
}