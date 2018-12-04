package juniter.service.bma;

import juniter.service.bma.dto.NodeSummaryDTO;
import juniter.service.bma.dto.SandBoxesDTO;
import juniter.service.bma.dto.UnitDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnExpression("${juniter.bma.enabled:false}")
public class NodeService {


    /**
     * node/summary
     *
     * Goal
     * GET technical informations about this peer.
     * Parameters
     * None.
     * @return
     * Technical informations about the node.
     * {
     *   "duniter": {
     *     "software": "duniter",
     *     "version": "0.10.3",
     *     "forkWindowSize": 10
     *   }
     * }
     *
     */
    @RequestMapping(value = {"","/", "/node/summary"}, method = RequestMethod.GET)
    public NodeSummaryDTO summary() {
        return new NodeSummaryDTO();
    }


    /**
     * node/sandboxes
     *
     * Goal
     * GET filling and capacity of indentities, membership and transactions sandboxes of the requested peer.
     * Parameters
     * None.
     *
     * @return
     * Technical informations about identities, membership and transactions sandboxes.
     * {
     *   "identities": {
     *     "size": 5000,
     *     "free": 4626
     *   },
     *   "memberships": {
     *     "size": 5000,
     *     "free": 4750
     *   },
     *   "transactions": {
     *     "size": 200,
     *     "free": 190
     *   }
     * }
     *
     */
    @RequestMapping(value = "/node/sandboxes", method = RequestMethod.GET)
    public SandBoxesDTO sandboxes() {

        var res =  new SandBoxesDTO();
        res.setIdentities(new UnitDTO(5000,5000 ));
        res.setMemberships(new UnitDTO(5000,5000 ));
        res.setTransactions(new UnitDTO(5000,5000 ));
        return res ;
    }


}
