package juniter.service.ws2p.toDrop;

//
//@ConditionalOnExpression("${juniter.useWS2P:true}")
//@Controller
//@RequestMapping(value = "/ws", headers = "*")
//public class WSService {
//    private static final Logger LOG = LogManager.getLogger();
//
//    //@CrossOrigin(origins = "*", allowedHeaders = "*")
//
//    @RequestMapping(value = "/peer",
//            headers = "*",
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    public Map<String, Object> peer(@RequestBody Map<String, Object> request, HttpServletRequest raw) {
//        LOG.info("Entering /ws/peer ...");
//
//        return Maps.newHashMap();
//    }
//
//   // @CrossOrigin(origins = "*", allowedHeaders = "*")
//
//    @RequestMapping(value = "/block",
//            headers = "*",
//            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE ,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    public Map<String, Object> block(@RequestBody Map<String, Object> request, HttpServletRequest raw) {
//        LOG.info("Entering /ws/block ...");
//
//        return Maps.newHashMap();
//    }
//
//}
