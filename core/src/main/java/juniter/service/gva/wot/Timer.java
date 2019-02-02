package juniter.service.gva.wot;

import io.leangen.graphql.annotations.GraphQLQuery;

import java.time.LocalDateTime;

public class Timer {
    private LocalDateTime localDateTime;
    public Timer() {
        // NO-OP
    }
    public Timer(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
    @GraphQLQuery(name = "localDateTime")
    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}