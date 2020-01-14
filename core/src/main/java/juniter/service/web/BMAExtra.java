package juniter.service.web;

import juniter.core.model.dbo.ChainParameters;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.CertRecord;
import juniter.service.core.BlockService;
import juniter.service.core.ForkHead;
import juniter.service.core.Index;
import juniter.service.core.WebOfTrust;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/extra")
public class BMAExtra {
    private static final Logger LOG = LogManager.getLogger(WebOfTrust.class);

    @Autowired
    private WebOfTrust webOfTrust;

    @Autowired
    private Index index;
    @Autowired
    private BlockService blockService;

    @Autowired
    private ForkHead forkHead;



    @CrossOrigin(origins = "*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/certRecord/{search}")
    public List<CertRecord> certRecord(@PathVariable("search") String search) {
        return webOfTrust.certRecord(search);
    }

    @CrossOrigin(origins = "*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/distance/{search}")
    public List<?> testDistance(@PathVariable("search") String search) {
        return index.getCRepo().distance(search);
    }

    @CrossOrigin(origins = "*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/blockToForge/{issuer}")
    public DBBlock blockToForge(@PathVariable("issuer") String search) {
        return forkHead.forge(index.prepareIndexForForge(search));
    }
    @CrossOrigin(origins = "*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/currencies")
    public List<ChainParameters> existingCCy() {
        return blockService.existingCCYs( );
    }


}
