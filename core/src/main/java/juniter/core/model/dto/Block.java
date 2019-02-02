package juniter.core.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Block implements Serializable {

	private static final long serialVersionUID = -6400285529088830671L;

	private Short version;

	private Long nonce;

	private Integer number;

	private Integer powMin;

	private Long time;

	private Long medianTime;

	private Integer membersCount;

	private Long monetaryMass;

	private Integer unitbase;

	private Integer issuersCount;

	private Integer issuersFrame;

	private Integer issuersFrameVar;

	private String currency;

	private String issuer;

	private String signature;

	private String hash;

	private String parameters;

	private String previousHash;

	private String previousIssuer;

	private String inner_hash;

	private Integer dividend;

	private List<String> identities = new ArrayList<>();

	private List<String> joiners = new ArrayList<>();

	private List<String> actives = new ArrayList<>();

	private List<String> leavers = new ArrayList<>();

	private List<String> revoked = new ArrayList<>();

	private List<String> excluded = new ArrayList<>();

	private List<String> certifications = new ArrayList<>();

	private List<TransactionDTO> transactions = new ArrayList<>();

	private String raw;

}
