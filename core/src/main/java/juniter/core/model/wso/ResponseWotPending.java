package juniter.core.model.wso;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.BStamp;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseWotPending {

    private static final Logger LOG = LogManager.getLogger(ResponseWotPending.class);

    private String resId;

    private Object body;

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Identities {

        @Data
        @NoArgsConstructor
        class Idty {

            class Certifs {
                String from;
                String to;
                String sig;
                Long timestamp;
                Long expiresIn;

                public Certifs() {

                }
            }

            class Meta {

                BStamp timestamp;

                public Meta() {

                }
            }

            @Data
            @NoArgsConstructor
            class PendingCerts {
                private String from;
                private String to;
                private String target;
                private Long block_number;
                private String block_hash;
                private Long block;
                private Boolean linked;
                private Boolean written;
                private Object written_block;
                private Object written_hash;
                private Long expires_on;
                private Long expired;
                private BStamp blockstamp;
                private String sig;

                Long timestamp;
                Long expiresIn;

            }

            @Data
            @NoArgsConstructor
            class PendingMembs {
                private String membership;
                private String issuer;
                private Long number;
                private Long blockNumber;
                private String blockHash;
                private String userid;
                private String certts;
                private String block;
                private String fpr;
                private String idtyHash;
                private Boolean written;
                private Object written_number;
                private Long expires_on;
                private String signature;
                private Long expired;
                private BStamp blockstamp;
                private String sig;
                private String type;


            }

            String pubkey;
            String uid;
            String sig;
            Meta meta;
            String revocation_sig;
            Boolean revoked;
            Object revoked_on;
            Boolean expired;
            Boolean outdistanced;
            Boolean isSentry;
            Boolean wasMember;
            List<Certifs> certifications = new ArrayList<>();
            List<PendingCerts> pendingCerts = new ArrayList<>();
            List<PendingMembs> pendingMemberships = new ArrayList<>();
            Long membershipExpiresIn;


        }

        List<Idty> identities = new ArrayList<>();

        public Identities() {

        }

        public List<Idty> getIdentities() {
            return identities;
        }

        public void setIdentities(List<Idty> identities) {
            this.identities = identities;
        }
    }

    //private static final long serialVersionUID = 2497514270739293189L;


}
