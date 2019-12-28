package juniter.service.bma;

import juniter.core.model.dto.tx.UdDTO;
import juniter.core.model.dto.tx.UdHistory;
import juniter.service.core.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * Handles publication of transaction documents
 *
 * @author ben
 */
@RestController
@ConditionalOnExpression("${juniter.useBMA:false}")
@RequestMapping("/ud")
public class UDService {

    @Autowired
    private TransactionService txService;

    @Transactional(readOnly = true)
    @GetMapping(value = "/history/{pubkey}")
    public UdHistory UDHistory(@PathVariable("pubkey") String pubkey) {
        return new UdHistory(pubkey, txService
                .dividendsOf(pubkey)
                .stream()
                .map(ud -> new UdDTO(ud.getMedianTime(), ud.getNumber(), ud.getConsumed(), ud.getDividend(), ud.getBase()))
                .collect(Collectors.toList()));
    }


}
