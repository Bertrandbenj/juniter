package juniter.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public interface Constants {

    interface Regex {
        String USER_ID = "[A-Za-z0-9_-]+";
        String CURRENCY_NAME = "[A-Za-z0-9_-]";
        
        /**
         * ex: D9D2zaJoWYWveii1JRYLVK3J4Z7ZH3QczoKrnQeiM6mx
         */
        String PUBKEY = "[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{43,45}";
        
        /**
         * ex: "DpVMlf6vaW0q+WVtcZlEs/XnDz6WtJfA448qypOqRbpi7voRqDaS9R/dG4COctxPg6sqXRbfQDieeDKU7IZWBA=="
         */
        String SIGNATURE = "^.{86}==$"; 
        
        /**
         * ex: "53521DF50E07EC71A8DCB618A65F4BACE4538846DC2D5B12CDD6307E2B667336"
         */
        String HASH = "[A-Z0-9]{64}";
        
        /**
         * ex: ""
         */
        String EMPTY_STRING = "^$";
        
        /**
         * ex: 
         */
        String BUID = "[1234567890]+-"+HASH;
        

        String IP4 = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
        String IP6 = "^(((?=(?>.*?::)(?!.*::)))(::)?([0-9A-F]{1,4}::?){0,5}"
    			+ "|([0-9A-F]{1,4}:){6})(\\2([0-9A-F]{1,4}(::?|$)){0,2}|((25[0-5]"
    			+ "|(2[0-4]|1\\d|[1-9])?\\d)(\\.|$)){4}|[0-9A-F]{1,4}:[0-9A-F]{1," + "4})(?<![^:]:|\\.)\\z";
        String PORT = "^443|80|(6553[0-5]|655[0-2]\\d|65[0-4]\\d\\d|6[0-4]\\d{3}|[1-5]\\d{4}|[2-9]\\d{3}|1[1-9]\\d{2}|10[3-9]\\d|102[4-9])$";
        String IP6_ADRESS = "^\\[([0-9a-fA-F:]*)\\]:(.*)";
        String DOMAIN = "[a-zA-Z0-9-]{0,62}(?:\\\\.[a-z0-9][a-z0-9-]{0,62})+$"; 
        String DOMAIN2 = "^([a-zA-Z0-9][a-zA-Z0-9-]{0,62}\\.)+[A-Za-z0-9]{0,62}$";
        String WS2P_SMETHING = "[a-zA-Z0-9]{8}";
        String G1 = "^g1$";
    }

    interface HttpStatus {
        int SC_TOO_MANY_REQUESTS = 429;
    }

    interface Config {
        int TOO_MANY_REQUEST_RETRY_TIME = 500; // 500 ms
        int MAX_SAME_REQUEST_COUNT = 5; // 5 requests before to get 429 error
    }
    
    interface Defaults {
    	int BULK_BATCH_SIZE = 20;
    	String NODE = "https://g1.bertrandbenjamin.com/";
    }
   
    
    interface Logs {
    	String INTERRUPTED = "THE Improbable happened : Thread.sleep failed" ;
    }
}
