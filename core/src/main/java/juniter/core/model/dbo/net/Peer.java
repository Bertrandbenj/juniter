package juniter.core.model.dbo.net;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.meta.DUPPeer;
import juniter.core.utils.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString
@Table(name = "net_peer", schema = "public", indexes = {
        @Index(columnList = "pubkey")
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"pubkey"})
})
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Peer implements DUPPeer {
    // private static final Logger LOG = LogManager.getLogger();


//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long id;

    private Short version;

    @Size(max = 42)
    private String currency;

    @Size(max = 10)
    private String status;

//    @Size(max = 42)
//    private String first_down;
//
//    @Size(max = 42)
//    private String last_try;

    @Id
    @Pattern(regexp = Constants.Regex.PUBKEY)
    @Size(max = 45)
    private String pubkey;

    @NotNull
    private BStamp block;


    @Size(max = 88)
    @Pattern(regexp = Constants.Regex.SIGNATURE)
    private String signature;


    @OrderColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "peer", referencedColumnName = "pubkey")
    private List<EndPoint> endpoints = new ArrayList<>();

    public List<EndPoint> endpoints() {
        return endpoints;
    }

}
