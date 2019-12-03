package juniter.core.model;

import lombok.Data;
import lombok.Setter;

@Data
public class CcyStats implements Comparable<CcyStats> {

    private Integer number;
    private Long medianTime;
    private Integer membersCount;
    private Long monetaryMass;

    private Integer membersGrowth;
    private Double membersGrowthRate;
    private Double moneyShare;

    public CcyStats(Integer number, Long medianTime, Integer membersCount, Long monetaryMass) {
        this.number = number;
        this.medianTime = medianTime;
        this.membersCount = membersCount;
        this.monetaryMass = monetaryMass;
    }

    @Override
    public int compareTo(CcyStats ccyStats) {
        return this.getNumber().compareTo(ccyStats.getNumber());
    }
}
