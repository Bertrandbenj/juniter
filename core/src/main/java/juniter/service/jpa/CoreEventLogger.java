package juniter.service.jpa;

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

        if(event.getMessage() != null && !event.getMessage().isEmpty()){
            LOG.info(event.getName() + " " + event.getMessage() + " " + event.getWhat());
        }else{
            LOG.debug(event.getName() + " " + event.getMessage() + " " + event.getWhat());
        }

    }
}