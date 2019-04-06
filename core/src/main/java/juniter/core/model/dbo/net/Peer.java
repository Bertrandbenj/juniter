package juniter.core.model.dbo.net;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.utils.Constants;
import lombok.Data;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name = "peer", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Peer  {
   // private static final Logger LOG = LogManager.getLogger();


//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long id;

    private Integer version;

    @Size(max = 42)
    private String currency;

    @Size(max = 10)
    private String status;

    @Size(max = 42)
    private String first_down;

    @Size(max = 42)
    private String last_try;

    /**
     * ex : HnFcSms8jzwngtVomTTnzudZx7SHUQY8sVE1y8yBmULk
     */
//	@OneToOne(cascade = { CascadeType.ALL })
//	@JoinColumn(name = "pubkey", referencedColumnName= "pubkey")
    @Id
    @Pattern(regexp = Constants.Regex.PUBKEY)
    @Size(max = 45)
    private String pubkey;

    /**
     * ex : 45180-00000E577DD4B308B98D0ED3E43926CE4D22E9A8
     */
    //@OneToOne(cascade = CascadeType.DETACH)
    //@Pattern(regexp = Constants.Regex.BUID)
    private String block;


    @Size(max = 88)
    @Pattern(regexp = Constants.Regex.SIGNATURE)
    private String signature;


//    @OneToMany(mappedBy = "peer", cascade = CascadeType.ALL, orphanRemoval = true, fetch =FetchType.EAGER)
    @OneToMany(mappedBy = "peer")
    private List<EndPoint> endpoints = new ArrayList<>();


    public String getRaw() {
        return toDUP(true);
    }

    public List<EndPoint> endpoints() {
        return endpoints;
    }

    public List<URI> getUris() {


        var uriList = new ArrayList<URI>();
        if(!"UP".equals(status))
            return uriList;

                    var builder = new DefaultUriBuilderFactory().builder();


        Map<String, String> apis = endpoints.stream()
                .map(ep -> {
                    var x = "1";
                    if (ep.getApi().equals(EndPointType.WS2P))
                        x = ep.getTree();
                    else if (ep.getOther() != null)
                        x = ep.getOther();

                    return new BasicNameValuePair(ep.getApi().toString(), x); // + "=" + x;
                })
                .distinct()
                .collect(Collectors.toMap(bnvp-> bnvp.getName(), o -> o.getValue()));


        MultiValueMap mvm = new LinkedMultiValueMap();
        apis.forEach((k,v)-> mvm.add(k,v) );


        var domains = endpoints.stream()
                .map(ep -> {
                    var dom = ep.getDomain() != null ? ep.getDomain()
                            : ep.getIp4() != null ? ep.getIp4()
                            : ep.getIp6() != null ? ep.getIp6()
                            : "";
                    return dom + ( ep.getPort()!=null? ":" + ep.getPort():"");
                })
                .distinct()
                .collect(Collectors.toList());


        var status = "&status=" + getStatus();
        //var node = "&node=" + getBlock();
        var sign = getSignature();
        var pub = getPubkey();


        for (String domain : domains) {

            try {
                uriList.add(builder
                        .scheme("http" + (domain.endsWith("443")|| apis.containsKey("BMAS")?"s":""))
                        //.setUserInfo(pubkey)//,signature)
                        .host(domain)
                        .path((domain.endsWith("/") ? "" : "/") + getStatus() + "/" + getBlock().replaceAll("-", "/"))
                        .queryParams(mvm)
                        .build());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        var cnt1 = uriList.stream().mapToInt(u -> u.toASCIIString().length()).sum();
        var cnt2 = getRaw().length();
       // LOG.info(" getUris " + cnt1 + "  -  " + cnt2 + "\n" + uriList.stream().map(URI::toString).collect(Collectors.joining("\n")));


        return uriList;
    }


    public String toDUP(boolean signed) {
        return "Version: " + version +
                "\nType: Peer" +
                "\nCurrency: " + currency + "" +
                "\nPublicKey: " + pubkey + " " +
                "\nBlock: " + block + "" +
                "\nEndpoints:\n"
                + endpoints.stream().map(EndPoint::getEndpoint).collect(Collectors.joining("\n"))
                + "\n" +
                (signed ? signature + "\n" : "");
    }


}
