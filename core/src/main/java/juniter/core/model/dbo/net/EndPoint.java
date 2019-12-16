package juniter.core.model.dbo.net;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.utils.Constants;
import juniter.core.validation.NetValid;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static juniter.core.model.dbo.net.EndPointType.*;

/**
 * ex : [ "BASIC_MERKLED_API metab.ucoin.io 88.174.120.187 9201" ]
 *
 * @author ben
 */
@Entity
@Data
@ToString
@NoArgsConstructor
@Table(name = "net_endpoint", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EndPoint {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @JsonIgnore
//    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
//    @JoinColumn(name = "peer", referencedColumnName = "pubkey")
//    private Peer peer;

    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    private EndPointType api;

    @Size(min = 7, max = 15)
    @Pattern(regexp = Constants.Regex.IP4)
    private String ip4;

    @Size(max = 64)
    //@Pattern(regexp=Constants.Regex.IP6)
    private String ip6;

    @Size(max = 6)
    @Pattern(regexp = Constants.Regex.PORT)
    private String port;

    private String domain;
    private String other;
    private String tree;
    private String endpoint;

    public EndPoint(String endPT) {
        this.endpoint = endPT;
        var it = endPT.split(" ");
        for (int i = 1; i < it.length; i++) {

            String item = it[i];
            if (NetValid.validatePortNumber(item)) {
                port = item;
                continue;
            }
            if (NetValid.validateIP4Address(item)) {
                ip4 = item;
                continue;
            }
            if (NetValid.validateIP6Address(item)) {
                ip6 = item;
                continue;
            }
            if (NetValid.validateDomain(item) || NetValid.validateDomain2(item)) {
                domain = item;
                continue;
            }
            if (NetValid.validateWS2PSTH(item)) {
                tree = item;
                continue;
            }
            other = item;
        }
        setApi(EndPointType.valueOf(it[0]));

    }

    public EndPointType api() {
        return api;
    }

    public void setApi(EndPointType api) {
        this.api = api;
    }

    public EndPointType getApi() {

        if (api == null) { // used for loading from bootstrap list
            System.out.println("api null : " + endpoint);
            switch (endpoint.substring(0, endpoint.indexOf("://") + 3)) {
                case "https://":
                    api = BMAS;
                    break;
                case "http://":
                    api = BASIC_MERKLED_API;
                    break;
                case "wss://":
                    api = WS2PS;
                    break;
                case "ws://":
                    api = WS2P;
                    break;
            }
        }
        return api;
    }

    public String url() {

        String scheme = "";

        String res = "";
        switch (api) {
            case BMAS:
                scheme += "https://";
                break;
            case BASIC_MERKLED_API:
                scheme += "http://";
                break;
            case WS2P:
                scheme += "wss://";
                break;
        }

        if ("443".equals(port)) { // trick to help bad endpoint definition (BMA on 443 is actually BMAS)
            scheme = "https://";
        }

        res += scheme;


        if (domain != null) {
            res += domain;
        } else if (ip6 != null) {
            res += "[" + ip6 + "]";
        } else if (ip4 != null) {
            res += ip4;
        }

        res += ":" + port;

        if (!res.endsWith("/"))
            res += "/";
        //LOG.debug("url: " +res);

        if ("/ws2p".equals(getOther())) {
            res += "ws2p";
        }

        return res;
    }


//    public EndPoint linkPeer(Peer peer) {
//        this.peer = peer;
//        return this;
//    }


}
