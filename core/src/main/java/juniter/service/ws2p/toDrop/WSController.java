package juniter.service.ws2p.toDrop;

/**
 *  ATTENTTION  this is too good to be true
 */

//@Controller
//public class WSController {
//
//    private static final Logger LOG = LogManager.getLogger();
//
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    @Autowired
//    private BlockRepository blockRepo;
//
//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public Block greeting(Object message) throws Exception {
//
//        LOG.info("entering  /hello " +message);
//        Thread.sleep(1000); // simulated delay
//        return modelMapper.map(blockRepo.current().orElseThrow(), Block.class);
//    }
//
//
//    @MessageMapping("/block")
//    @SendTo("/topic/block")
//    public Block block(Object message) throws Exception {
//
//        LOG.info("entering  /ws/block " +message);
//        Thread.sleep(1000); // simulated delay
//        return modelMapper.map(blockRepo.current().orElseThrow(), Block.class);
//    }
//
//}
