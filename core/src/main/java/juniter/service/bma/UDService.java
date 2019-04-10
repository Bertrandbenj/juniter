package juniter.service.bma;

import juniter.core.model.dto.tx.TxHistory;
import juniter.repository.jpa.block.TxRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Handles publication of transaction documents
 *
 * @author ben
 */
@RestController
@ConditionalOnExpression("${juniter.useBMA:false}")
@RequestMapping("/ud")
public class UDService {

    private static final Logger LOG = LogManager.getLogger(UDService.class);

    @Autowired
    private TxRepository repository;


    @Autowired
    private ModelMapper modelMapper;



    @Transactional(readOnly = true)
    @GetMapping(value = "/history/{pubkey}")
    public TxHistory UDHistory(@PathVariable("pubkey") String pubkey) {


        return new TxHistory(pubkey, "UDLIST");
    }


}
