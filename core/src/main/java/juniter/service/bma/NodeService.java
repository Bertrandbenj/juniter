package juniter.service.bma;

import juniter.core.model.dto.node.NodeSummaryDTO;
import juniter.core.model.dto.node.SandBoxesDTO;
import juniter.service.jpa.Sandboxes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnExpression("${juniter.useBMA:true}")
public class NodeService {

    @Autowired
    private Sandboxes sandboxes;

    /**
     * node/summary
     * <p>
     * Goal
     * GET technical informations about this peer.
     * Parameters
     * None.
     *
     * @return Technical informations about the node.
     * {
     * "duniter": {
     * "software": "duniter",
     * "getVersion": "0.10.3",   require > 1.1.0
     * "forkWindowSize": 10
     * }
     * }
     */
    @CrossOrigin(origins = "*") // https://localhost:8443/node/summary
    @GetMapping(value = {"", "/", "/node/summary"})
    public NodeSummaryDTO summary() {
        return new NodeSummaryDTO();
    }


    /**
     * node/sandboxes
     * <p>
     * Goal
     * GET filling and capacity of indentities, membership and transactions sandboxes of the requested peer.
     * Parameters
     * None.
     *
     * @return Technical informations about identities, membership and transactions sandboxes.
     * {
     * "identities": {
     * "getSize": 5000,
     * "free": 4626
     * },
     * "memberships": {
     * "getSize": 5000,
     * "free": 4750
     * },
     * "transactions": {
     * "getSize": 200,
     * "free": 190
     * }
     * }
     */
    @GetMapping(value = "/node/sandboxes")
    public SandBoxesDTO sandboxes() {
        return sandboxes.status();

    }


}
