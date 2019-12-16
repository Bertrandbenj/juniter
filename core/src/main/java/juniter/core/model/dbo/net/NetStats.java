package juniter.core.model.dbo.net;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Id;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
//@Entity
//@Table(name = "net_stats", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"host"}))
public class NetStats implements Comparable<NetStats> {

    private AtomicInteger count;
    private AtomicInteger success;
    private AtomicInteger error;
    private Long lastUpdate;
    private Long lastResponseTime;
    private Double lastNormalizedScore;

    @Id
    private String host;


    public Double score() {
        var ratio = success.doubleValue() / count.doubleValue();
        var age = System.currentTimeMillis() - lastUpdate;

        var x = ratio * age;

        return x * success.doubleValue();
    }

    public Double normalizedScore(Double sum) {
        lastNormalizedScore = score() / sum;
        return lastNormalizedScore;
    }

    @Override
    public int compareTo(NetStats o) {
        return lastNormalizedScore.compareTo(o.lastNormalizedScore);
    }
}
