package juniter.service.bma.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PeeringDTO implements Serializable {

    private static final long serialVersionUID = -4464417074954356696L;

    private  Integer depth ;

    private Integer nodeCounts;

    private Integer leavesCount;

    private String root ;

    private List<String> leaves;

    private LeafDTO leaf ;

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Integer getNodeCounts() {
        return nodeCounts;
    }

    public void setNodeCounts(Integer nodeCounts) {
        this.nodeCounts = nodeCounts;
    }

    public Integer getLeavesCount() {
        return leavesCount;
    }

    public void setLeavesCount(Integer leavesCount) {
        this.leavesCount = leavesCount;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public List<String> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<String> leaves) {
        this.leaves = leaves;
    }

    public LeafDTO getLeaf() {
        return leaf;
    }

    public void setLeaf(LeafDTO leaf) {
        this.leaf = leaf;
    }



}
