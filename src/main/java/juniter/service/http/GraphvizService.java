package juniter.service.http;

import java.util.Date;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.servlet.ServletContext;
import javax.servlet.http.*;
import static java.util.stream.Collectors.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import juniter.model.persistence.Hash;
import juniter.model.persistence.PubKey;
import juniter.model.persistence.tx.Transaction;
import juniter.model.persistence.tx.TxInput;
import juniter.model.persistence.tx.TxOutput;
import juniter.model.persistence.tx.TxType;
import juniter.model.persistence.tx.TxUnlock;
import juniter.repository.BlockRepository;
import juniter.repository.CertsRepository;
import juniter.repository.TxRepository;
import juniter.service.rest.BlockchainService;

@Controller
@ConditionalOnExpression("${juniter.graphviz.enabled:false} && ${juniter.bma.enabled:false}")
@RequestMapping("/graphviz")
public class GraphvizService {
	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private CertsRepository certsRepo;

	@Autowired
	private BlockRepository blockRepo;

	@Autowired
	private TxRepository txRepo;

	@Autowired
	private BlockchainService blockService;

	private static final String svgFile = "src/main/resources/static/dot/%s.svg";
	private static final String dotFile = "src/main/resources/static/dot/%s.dot";
	private static final Integer RANGE = 2;

	@Transactional
	@RequestMapping(value = "/dot/block/{block}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> blockDot(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("block") Integer block) throws IOException {

		var headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf("text/plain"));
		var svgEntity = new ResponseEntity<String>(blockGraph(block), headers, HttpStatus.OK);
		return svgEntity;
	}

	@Transactional
	@RequestMapping(value = "/svg/block/{block}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> blockSVG(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("block") Integer block) throws IOException {

		// build the SVG XML as a string
		String SVG = localConvertToSVG(blockGraph(block), block + "");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf("image/svg+xml"));
		ResponseEntity<String> svgEntity = new ResponseEntity<String>(SVG, headers, HttpStatus.OK);
		return svgEntity;
	}

	@Transactional
	@RequestMapping(value = "/dot/certs/{pubkey}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> certsDot(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("pubkey") String pubkey) throws IOException {

		var headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf("text/plain"));
		var svgEntity = new ResponseEntity<String>(certsGraph(pubkey), headers, HttpStatus.OK);
		return svgEntity;
	}

	@Transactional
	@RequestMapping(value = "/svg/certs/{pubkey}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> certsSVG(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("pubkey") String pubkey) throws IOException {

		// build the SVG XML as a string
		String SVG = localConvertToSVG(certsGraph(pubkey), pubkey);

		var headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf("image/svg+xml"));
		var svgEntity = new ResponseEntity<String>(SVG, headers, HttpStatus.OK);
		return svgEntity;
	}

	@Transactional
	@RequestMapping(value = "/dot/tx/{hash}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> txDot(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("hash") String hash) throws IOException {

		var headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf("text/plain"));
		var svgEntity = new ResponseEntity<String>(txGraph(hash), headers, HttpStatus.OK);
		return svgEntity;
	}

	@Transactional
	@RequestMapping(value = "/svg/tx/{hash}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> txSVG(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("hash") String hash) throws IOException {
		logger.info("Entering... /graphviz/svg/tx/{hash=" + hash + "}");
		// build the SVG XML as a string
		String SVG = localConvertToSVG(txGraph(hash), hash);

		var headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf("image/svg+xml"));
		var svgEntity = new ResponseEntity<String>(SVG, headers, HttpStatus.OK);
		return svgEntity;
	}

	public String txGraph(String hash) {
		var res = "";

		var tx = txRepo.findByTHash(hash).get(0);
		

		res += "digraph{\n" //
				+ "\tgraph [rankdir=TB];\n";

		res += "\tinfo [labeljust=l, shape=folder, label=\"" //
				+ "blockstamp: " + mini(tx.getBlockstamp()) //
				+ "\\lCur: " + tx.getCurrency()//
				+ "\\lhash: " + mini(tx.getHash())//
				+ "\\llocktime: " + tx.getLocktime() //
				+ "\\l\", URL=\"/graphviz/svg/block/"+tx.getBlockstamp().split("-")[0]+"\"];\n";


		res += "\tgood [label=\"Product/Service\\n" + tx.getComment() + "\"];\n";
		res += "\tsum [label=\"sum\\n"+tx.inputs().stream().mapToInt(i->i.Amount()).sum()+"\"];\n";
		res += "\t{rank = same; good; sum;}\n";

		
		
		//
		//							DRAW THE INPUTS 
		//
		res += "\n\tsubgraph cluster_inputs{\n" //
				+ "\t\tlabel=\"Inputs\";\n" // 
				+ "\t\tcolor=blue;\n"//
				+ "\t\tlabelloc=t;\n";
		for (TxInput txi : tx.inputs()) {

			var input = "input" + txi.hashCode();
			
			
			if (txi.Type().equals(TxType.D)) {
				res += "\t\t" + input + " ["//
						+ "label=\"UD\", URL=\"/graphviz/svg/block/" + txi.dBlockID() //
						+ "\", shape=circle, color=yellow, style=filled];\n"; //
			} else if (txi.Type().equals(TxType.T)) {
				res += "\n\tsubgraph cluster_input" + txi.hashCode() + "{\n" //
						+ "\t\tgraph [rankdir=LR];\n"//
						+ "\t\tlabel=\"TxInput\";\n"//
						+ "\t\t" + input + " [label=\"amount: " + txi.Amount() + "\"];\n"; //

				res += "\t\tthash" + txi.hashCode() + " [label=\"Thash: " + mini(txi.tHash().toString()) + "\"];\n" //
						+ "\t\ttindex" + txi.hashCode() + " [label=\"Tindex: " + txi.tIndex() + "\"];\n"; //
				res += "\t}\n";//

			}

//			res += "\t\tamount" + txi.hashCode() + " -> sum [label=\"" + txi.Amount() + "\", weight=10];\n";
		}
		res += "\t}\n\n";//
		
		
		//
		//							DRAW THE UNLOCKS 
		//
		res += "\tsubgraph cluster_unlocks{\n" //
				+ "\t\tlabel=\"Unlocks\";\n" // 
				+ "\t\tcolor=blue;\n"//
				+ "\t\tlabelloc=t;\n"
				+ "\t\tdbu [label=\"useful\\nfor\\nstate\\nmachine\", shape=cylinder];\n";
		for(TxUnlock unlock : tx.unlocks()) {
			var UnlockedInput = tx.inputs().get(unlock.Id());
			var UnlockedIssuer = tx.issuers().get(unlock.sigFuncReference()); // case SIG
			
			res += "\t\tunlock"+unlock.hashCode()+" [label=\"unlock\\n" + unlock.Function() + "\", shape=diamond];\n";
			
			//link 
			res += "\t\tinput"+ UnlockedInput.hashCode()+" -> unlock"+unlock.hashCode()+" ;\n";
//			res += "\tissuer"+UnlockedIssuer.getPubkey()+" -> unlock"+ unlock.hashCode()+" ;\n";
		}
		
		res += "\t}\n\n";//
		for(TxUnlock unlock : tx.unlocks()) {
			res += "\t\tunlock"+unlock.hashCode()+" -> sum ;\n";
		}
		
		
		
		//
		//							DRAW THE OUTPUTS 
		//
		res += "\n\tsubgraph cluster_outputs{\n" //
				+ "\t\tlabel=\"Outputs\";\n" // 
				+ "\t\tcolor=blue;\n"//
				+ "\t\tlabelloc=t;\n"//
				+ "\t\tdbo [label=\"useful\\nfor\\nstate\\nmachine\", shape=cylinder];\n";
		// NODES 
		for (TxOutput out : tx.outputs()) {
			
			res += "\t\tlockOut"+out.hashCode()+" [label=\"lockOut\\n" + mini(out.Function()) + "\", shape=diamond];\n";

			
		}
		res += "\t}\n";//
		
		// EDGES
		for (TxOutput out : tx.outputs()) {
			res += "\t\tamountOut" + out.hashCode() + " [label=\"" + out.Amount() + "\", shape=signature];\n"; //

			res += "\t\tsum -> " + "lockOut" + out.hashCode() + " [label=\"SIG\",weight=10];\n";
			if (out.Function().startsWith("SIG(")) {
				var dest = out.Function().substring(4, out.Function().length() - 1);
				res += "\t\t_dest" + dest + " [label=\""+mini(dest)+"\", weight=0];\n";
				res += "\t\tamountOut" + out.hashCode() + " -> _dest" + dest + " [weight=0];\n";
				//res += "\t\t_" + dest + " -> good [weight=0];\n";
				if(tx.issuers().stream().map(p->p.getPubkey()).anyMatch(s-> dest.equals(s))) { // is Issuer? 
//					res += "\t\t_dest" + dest + " -> good [weight=0];\n";
					res += "\t\t_" + dest + " -> _dest"+dest+" [weight=0, dir=back, label=\"the rest\"];\n";
				}else {
					res += "\t\tgood -> _dest" + dest + "[label=\"sell\", weight=0];\n";
				}
			}
			res += "\t\tlockOut"+out.hashCode()+" -> amountOut" + out.hashCode()+";\n";
		}
		for(PubKey issuer : tx.issuers()) {
			res += "\t_"+issuer.getPubkey()+" [label=\"issuer\\n" + mini(issuer.getPubkey()) + "\"];\n";
			res += "\t_" + issuer.getPubkey() + " -> unlock"+tx.unlocks().get(0).hashCode()+" [weight=0];\n";
			res += "\t_" + issuer.getPubkey() + " -> good [label=\"buy\", weight=0];\n";
		}
		return res + "\n}";

	}

	public String blockGraph(Integer blockNumber) {

		var blocks = IntStream.range(blockNumber - RANGE, blockNumber + RANGE + 1)//
				.filter(i -> i >= 0) //
				.filter(i -> i <= blockRepo.current()) //
				.mapToObj(b -> blockService.block(b))//
				.sorted((b1, b2) -> b1.getNumber().compareTo(b2.getNumber()))//
				.collect(toList());

		String res = "digraph{\n\t" //
				+ "graph [rankdir=LR ]\n\n\t";

		// Print the nodes
		res += blocks.stream().map(b -> {

			var prefix = "_" + b.getNumber();
			var formattedDtm = Instant //
					.ofEpochSecond(b.getMedianTime()) //
					.atZone(ZoneId.of("GMT+1")) //
					.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			var sub = "";

			// Draw the detailed subgraph
			sub += "\n\tsubgraph cluster_" + b.getNumber() + "{\n\t\t" //
					+ "graph [rankdir=TB]\n\t\t" //
					+ "node [shape=underline]\n\t\t" //
					+ "style=filled;\n\t\t" //
					+ "color=lightgrey;\n";

			// print the main node
			sub += "\t\t" + prefix + " [label=\"  Block #\\n" + b.getNumber() + "  \", URL=\"/graphviz/svg/block/"
					+ b.getNumber() + "\", shape=box3d];\n";

			sub += "\t\t" + prefix + "hash [label=\"Hash: " + mini(b.getHash()) + "\"];\n";
			if (b.getNumber() > 0) {
				sub += "\t\t" + prefix + "phash [label=\"PHash: " + mini(b.getPreviousHash()) + "\"];\n";
			}

			sub += "\t\t" + prefix + "issuer [label=\"issuer:\\n" + mini(b.getIssuer())
					+ "\", URL=\"/graphviz/svg/certs/" + b.getIssuer() + "\", shape=house];\n";
			if (b.getNumber() > 0) {
				sub += "\t\t" + prefix + "pissuer [label=\"pissuer:\\n" + mini(b.getPreviousIssuer())
						+ "\", shape=house];\n";
			}

			var deltaTime = "N/A";
			if (b.getNumber() != 0) {
				var delta = (b.getMedianTime() - blockService.block(b.getNumber() - 1).getMedianTime());
				deltaTime = Long.divideUnsigned(delta, 60) + "m " + delta % 60 + "s";
			}

			sub += "\t\t" + prefix + "info [labeljust=l, shape=plaintext, " + "label=\"Mbr: " + b.getMembersCount() //
					+ "\\lCur: " + b.getCurrency()//
					+ "\\lVer: " + b.getVersion()//
					+ "\\lPow: " + b.getPowMin() //
					+ "\\lissCnt: " + b.getIssuersCount() //
					+ "\\lissFrm: " + b.getIssuersFrame() //
					+ "\\lissFVar: " + b.getIssuersFrameVar()//
					+ "\\lM.M: " + b.getMonetaryMass() //
					+ "\\lTim: " + formattedDtm //
					+ "\\lDeltaTime: " + deltaTime//
					+ "\\lSign: " + mini(b.getSignature()) //
					+ "\\l\"];\n";

			if (b.getTransactions().size() > 0) {

				sub += "\n\t\tsubgraph cluster_" + b.getNumber() + "Tx {\n" //
						+ "\t\t\tgraph [rankdir=LR, style=dotted, color=black]\n" + "\t\t\tlabelloc=\"t\";\n"
						+ "\t\t\tlabel=\"Transactions\";";
//						+ "\t\t\t" + prefix + "tx [label=\"Transaction\", shape=circle];\n"; //

				for (Transaction tx : b.getTransactions()) {
					sub += "\t\t\ttx" + tx.getHash() + " [label=\"  "
							+ tx.getIssuers().stream().map(i -> mini(i)).collect(joining("\\n")) + "  \","
							+ "URL=\"/graphviz/svg/tx/" + tx.getHash() + "\"," + " shape=rarrow];\n";
				}

				sub += "\t\t}\n";

			}
			if (b.getCertifications().size() > 0)
				sub += "\t\t" + prefix + "Ce [label=\"Certs: " + b.getCertifications().size()
						+ "\", shape=octagon, URL=\"/graphviz/svg/certs/" + b.certifications.get(0).getCertified()
						+ "\"];\n";

			if (b.getIdentities().size() > 0)
				sub += "\t\t" + prefix + "idty [label=\"Idty: " + b.getIdentities().size() + "\", shape=octagon];\n";

			if (b.getExcluded().size() > 0)
				sub += "\t\t" + prefix + "exc [label=\"Excluded: " + b.getExcluded().size() + "\", shape=octagon];\n";

			if (b.getActives().size() > 0)
				sub += "\t\t" + prefix + "act [label=\"Actives: " + b.getActives().size() + "\", shape=octagon];\n";

			if (b.getRevoked().size() > 0)
				sub += "\t\t" + prefix + "rev [label=\"Revoked: " + b.getRevoked().size() + "\", shape=octagon];\n";

			if (b.getJoiners().size() > 0)
				sub += "\t\t" + prefix + "joi [label=\"Joiners: " + b.getJoiners().size() + "\", shape=octagon];\n";

			if (b.getLeavers().size() > 0)
				sub += "\t\t" + prefix + "lea [label=\"Leavers: " + b.getLeavers().size() + "\", shape=octagon];\n";

			if (b.getDividend() != null)
				sub += "\t\t" + prefix + "Ud [label=\"UD\\n" + b.getDividend()
						+ "\", shape=circle, color=yellow, style=filled];\n";

			sub += "\t}";

			// link it to its details block
			// sub += "\n\t"+ prefix + " -> " + prefix + "hash [lhead=cluster_" +
			// b.getNumber() + "];\n";

			return sub;
		}).collect(joining("\n\t"));

		res += "\n\n\t";

		// print the edges

		res += IntStream.range(blockNumber - RANGE, blockNumber + RANGE)//
				.filter(i -> i >= 0) //
				.filter(i -> i <= blockRepo.current()) //
				.mapToObj(i -> "_" + i + "hash -> _" + (i + 1) + "phash [weight=0, style=dotted];")//
				.collect(joining("\n\t"));
		res += "\n\t";
		res += IntStream.range(blockNumber - RANGE, blockNumber + RANGE)//
				.filter(i -> i >= 0) //
				.filter(i -> i <= blockRepo.current()) //
				.mapToObj(i -> "_" + i + "issuer -> _" + (i + 1) + "pissuer [weight=0, style=dotted];")//
				.collect(joining("\n\t"));
		res += "\n\n\t";

		res += "\n\tsubgraph cluster_Blocks {" + "\n\t\tcolor=black; \n\t\t";

		// print the edges
		res += IntStream.range(blockNumber - RANGE, blockNumber + RANGE)//
				.filter(i -> i >= 0).limit(blockRepo.current())
				.mapToObj(i -> "_" + i + " -> _" + (i + 1) + " [weight=10];")//
				.collect(joining("\n\t\t"));

		res += "\n\t}";

		res += "\n\n\t";

//		// print the edges
//		res += IntStream.range(blockNumber - RANGE, blockNumber + RANGE + 1) //
//				.mapToObj(i -> "_" + i + " -> _" + i + "hash [weight=0];") // 
//				.collect(joining("\n\t"));

		return res + "\n}";
	}

	public String mini(String s) {
		return s.substring(0, 7) + "..." + s.substring(s.length() - 7);
	}

	public String certsGraph(String pk) {
		String res = "digraph{\n\t" //
				+ "graph [rankdir=LR ]\n\n\t";

		// load data
		var certified = certsRepo.streamCertifiedBy(pk).collect(toList());
		var certifier = certsRepo.streamCertifiersOf(pk).collect(toList());

		res += "subgraph cluster_Identity{\n\t\t" //
				+ "style=filled;\n\t\t" //
				+ "color=green;\n\t\t" //
				+ "label=\"" + pk + "\";\n\t\t" //
				+ "__" + pk + " [label=\"" + mini(pk) + "\"];\n\t\t" //
				+ "_" + pk + " [label=\"" + mini(pk) + "\"];\n\t\t" //
				+ "__" + pk + " -> " + "_" + pk + ";\n\t" //
				+ "}\n\n\t";

		// build certified subgraph
		res += "subgraph cluster_certified{\n\t\t" //
				+ "style=filled;\n\t\t" //
				+ "color=lightgrey;\n\t\t"//
				+ "node [fontsize = 8];\n\t\t" //
				+ "label=\"Certified By\";\n\t\t";

		// print node
		res += certified.stream().map(c -> {
			var pubk = c.getCertified().getPubkey();
			return "_" + pubk + " [label=\"" + mini(pubk) + "\", URL=\"/graphviz/svg/certs/" + pubk + "\"];";
		}).collect(joining("\n\t\t"));

		// build certifier subgraph
		res += "\n\t}\n\n\t" //
				+ "subgraph cluster_certifier{\n\t\t" //
				+ "style=filled;\n\t\t" //
				+ "node [fontsize = 8];\n\t\t" //
				+ "color=lightgrey;\n\t\t" //
				+ "label=\"Certifier Of\";\n\t\t";

		// print node
		res += certifier.stream().map(c -> {
			var pubk = c.getCertifier().getPubkey();
			return "__" + pubk + " [label=\"" + mini(pubk) + "\", URL=\"/graphviz/svg/certs/" + pubk + "\"];";
		}).collect(joining("\n\t\t"));

		res += "\n\t}\n\t";

		// print edges
		var allNodes = Stream.concat(certified.stream(), certifier.stream());
		res += allNodes.map(cert -> "_" + cert.getCertified() + " -> " + "__" + cert.getCertifier()
				+ " [URL=\"/graphviz/svg/block/" + cert.getBlockNumber() + "\"]").collect(joining(";\n\t"));

		return res + "\n}";
	}

	/**
	 * Fail safe command execution
	 * 
	 * @param cmd
	 * @return
	 */
	public static Object run(String cmd) {
		try {
			logger.info("Executing : " + cmd);
			Process process = new ProcessBuilder(new String[] { "bash", "-c", cmd }).start();

			ArrayList<String> output = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				output.add(line);
				logger.info(line);
			}
			// There should really be a timeout here.
			if (0 != process.waitFor())
				return null;

			return output;

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	private String localConvertToSVG(String graph, String fileName) {
		String svgOut = String.format(svgFile, fileName);
		String dotOut = String.format(dotFile, fileName);

		try (PrintWriter out = new PrintWriter(dotOut)) {
			out.println(graph);
			out.close();
			logger.info("Created output graphviz file:" + dotOut);
			run("/usr/bin/dot -Tsvg " + dotOut + " -o " + svgOut);
			logger.info("Created output graphviz SVG:" + svgOut);
			return Files.readAllLines(Paths.get(svgOut)).stream().collect(joining());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
