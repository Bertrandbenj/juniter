package juniter.conf;


public class ServiceLocator {

    private static Cache cache = new Cache();

    public static MessagingService getService(String serviceName) {

        var service = cache.getService(serviceName);

        if (service != null) {
            return service;
        }

        var context = new InitialContext();
        var service1 = (MessagingService) context.lookup(serviceName);
        cache.addService(service1);
        return service1;
    }

    private static class Cache {
        public void addService(MessagingService service1) {

        }

        public MessagingService getService(String serviceName) {
            return null;
        }
    }

    private static class MessagingService {
    }


    public static class InitialContext {
        public Object lookup(String serviceName) {
            if (serviceName.equalsIgnoreCase("EmailService")) {
                return null;
            } else if (serviceName.equalsIgnoreCase("SMSService")) {
                return null;
            }
            return null;
        }
    }
}