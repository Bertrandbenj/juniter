package juniter.core.model.wso;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wrapper {
	private Doc  body;

	public static Wrapper buildBlockDoc(String data){
		var doc = new BlockDoc();
		doc.setData(data);
		return new Wrapper(doc);
	}

	public static Wrapper buildIdtyDoc(String data){
		var doc = new IdtyDoc();
		doc.setData(data);
		return new Wrapper(doc);
	}

	public static Wrapper buildMemberDoc(String data){
		var doc = new BlockDoc();
		doc.setData(data);
		return new Wrapper(doc);
	}

	public static Wrapper buildPeerDoc(String data){
		var doc = new PeerDoc();
		doc.setData(data);
		return new Wrapper(doc);
	}

	public static Wrapper buildTxDoc(String data){
		var doc = new BlockDoc();
		doc.setData(data);
		return new Wrapper(doc);
	}

	public static Wrapper buildCertDoc(String data){
		var doc = new BlockDoc();
		doc.setData(data);
		return new Wrapper(doc);
	}
}
