package juniter.service.front;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.ResponseBody;

import juniter.core.model.Pubkey;
import juniter.core.model.tx.Transaction;
import juniter.core.model.tx.TxInput;
import juniter.core.model.tx.TxOutput;
import juniter.core.model.tx.TxType;
import juniter.core.model.tx.TxUnlock;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.jpa.CertsRepository;
import juniter.repository.jpa.TxRepository;
import juniter.service.bma.BlockchainService;
import juniter.service.bma.DefaultLoader;

@Controller
@ConditionalOnExpression("${juniter.graphviz.enabled:false} && ${juniter.bma.enabled:false}")
@RequestMapping("/graphviz")
public class GraphvizService {

	public enum FileOutputType {
		dot("dot"), //
		svg("svg");

		private final String gvOutputType;

		FileOutputType(String ept) {
			gvOutputType = ept;
		}

		public String getEndPointType() {
			return gvOutputType;
		}
	}

	public enum GraphOutput {
		certs("certs"), //
		block("block"), //
		tx("tx");

		private final String gvOutput;

		GraphOutput(String ept) {
			gvOutput = ept;
		}

		public String getEndPointType() {
			return gvOutput;
		}
	}

	private static final Logger LOG = LogManager.getLogger();

	private static final String svgFile = "src/main/resources/static/dot/%s.svg";

	private static final String dotFile = "src/main/resources/static/dot/%s.dot";

	/**
	 * Fail safe command execution
	 *
	 * @param cmd
	 * @return
	 */
	public static Object run(String cmd) {
		try {
			LOG.info("Executing : " + cmd);
			final Process process = new ProcessBuilder(new String[] { "bash", "-c", cmd }).start();

			final ArrayList<String> output = new ArrayList<>();
			final BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				output.add(line);
				LOG.info(line);
			}
			// There should really be a timeout here.
			if (0 != process.waitFor())
				return null;

			return output;

		} catch (final Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	@Autowired
	private CertsRepository certsRepo;

	@Autowired
	private DefaultLoader defaultLoader;

	@Autowired
	private BlockRepository blockRepo;

	@Autowired
	private TxRepository txRepo;

	@Autowired
	private BlockchainService blockService;

	public String blockGraph(Integer blockNumber, Map<String, String[]> extraParams) {

		final Integer RANGE = extraParams.containsKey("range") ? Integer.valueOf(extraParams.get("range")[0]) : 2;

		final var blocks = IntStream.range(blockNumber - RANGE, blockNumber + RANGE + 1)//
				.filter(i -> i >= 0) //
				.filter(i -> i <= blockRepo.currentBlockNumber()) //
				.mapToObj(b -> blockRepo.block(b).orElseGet(() -> defaultLoader.fetchAndSaveBlock(b)))//
				.sorted((b1, b2) -> b1.getNumber().compareTo(b2.getNumber()))//
				.collect(toList());

		String res = "digraph{\n\t" //
				+ "graph [rankdir=LR ]\n\n\t";

		// Print the nodes
		res += blocks.stream().map(b -> {

			final var prefix = "_" + b.getNumber();
			final var formattedDtm = Instant //
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

			sub += "\t\t" + prefix + "hash [label=\"Hash: " + mini(b.getHash().toString()) + "\"];\n";
			if (b.getNumber() > 0) {
				sub += "\t\t" + prefix + "phash [label=\"PHash: " + mini(b.getPreviousHash().toString()) + "\"];\n";
			}

			sub += "\t\t" + prefix + "issuer [label=\"issuer:\\n" + mini(b.getIssuer())
					+ "\", URL=\"/graphviz/svg/certs/" + b.getIssuer() + "\", shape=pentagon];\n";
			if (b.getNumber() > 0) {
				sub += "\t\t" + prefix + "pissuer [label=\"pissuer:\\n" + mini(b.getPreviousIssuer())
						+ "\", shape=pentagon];\n";
			}

			var deltaTime = "N/A";
			if (b.getNumber() != 0) {
				final var delta = b.getMedianTime() - blockRepo.block(b.getNumber() - 1)//
						.orElse(defaultLoader.fetchAndSaveBlock(b.getNumber())) //
						.getMedianTime();
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
					+ "\\lSign: " + mini(b.getSignature().toString()) //
					+ "\\l\"];\n";

			if (b.getTransactions().size() > 0) {

				sub += "\n\t\tsubgraph cluster_" + b.getNumber() + "Tx {\n" //
						+ "\t\t\tgraph [rankdir=LR, style=dotted, color=black]\n" //
						+ "\t\t\tlabelloc=\"t\";\n" //
						+ "\t\t\tlabel=\"Transactions\";";
//						+ "\t\t\t" + prefix + "tx [label=\"Transaction\", shape=circle];\n"; //

				for (final Transaction tx : b.getTransactions()) {
					sub += "\t\t\ttx" + tx.getThash() + " [label=\"  "
							+ tx.getIssuers().stream().map(i -> mini(i.toString())).collect(joining("\\n")) + "  \","
							+ "URL=\"/graphviz/svg/tx/" + tx.getThash() + "\"," + " shape=rarrow];\n";
				}

				sub += "\t\t}\n";

			}
			if (b.getCertifications().size() > 0) {
				sub += "\t\t" + prefix + "Ce [label=\"Certs: " + b.getCertifications().size()
//						+ "\", shape=octagon, URL=\"/graphviz/svg/certs/" + b.getCertifications().get(0).getCertified() TODO fix LIST TO SET
						+ "\"];\n";
			}

			if (b.getIdentities().size() > 0) {
				sub += "\t\t" + prefix + "idty [label=\"Idty: " + b.getIdentities().size() + "\", shape=octagon];\n";
			}

			if (b.getExcluded().size() > 0) {
				sub += "\t\t" + prefix + "exc [label=\"Excluded: " + b.getExcluded().size() + "\", shape=octagon];\n";
			}

			if (b.getActives().size() > 0) {
				sub += "\t\t" + prefix + "act [label=\"Actives: " + b.getActives().size() + "\", shape=octagon];\n";
			}

			if (b.getRevoked().size() > 0) {
				sub += "\t\t" + prefix + "rev [label=\"Revoked: " + b.getRevoked().size() + "\", shape=octagon];\n";
			}

			if (b.getJoiners().size() > 0) {
				sub += "\t\t" + prefix + "joi [label=\"Joiners: " + b.getJoiners().size() + "\", shape=octagon];\n";
			}

			if (b.getLeavers().size() > 0) {
				sub += "\t\t" + prefix + "lea [label=\"Leavers: " + b.getLeavers().size() + "\", shape=octagon];\n";
			}

			if (b.getDividend() != null) {
				sub += "\t\t" + prefix + "Ud [label=\"UD\\n" + b.getDividend()
						+ "\", shape=circle, color=yellow, style=filled];\n";
			}

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
				.filter(i -> i <= blockRepo.currentBlockNumber()) //
				.mapToObj(i -> "_" + i + "hash -> _" + (i + 1) + "phash [weight=0, style=dotted];")//
				.collect(joining("\n\t"));
		res += "\n\t";
		res += IntStream.range(blockNumber - RANGE, blockNumber + RANGE)//
				.filter(i -> i >= 0) //
				.filter(i -> i <= blockRepo.currentBlockNumber()) //
				.mapToObj(i -> "_" + i + "issuer -> _" + (i + 1) + "pissuer [weight=0, style=dotted];")//
				.collect(joining("\n\t"));
		res += "\n\n\t";

		res += "\n\tsubgraph cluster_Blocks {" + "\n\t\tcolor=black; \n\t\t";

		// print the edges
		res += IntStream.range(blockNumber - RANGE, blockNumber + RANGE)//
				.filter(i -> i >= 0).limit(blockRepo.currentBlockNumber())
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

	public String certsGraph(String pk) {
		String res = "digraph{\n\t" //
				+ "graph [rankdir=LR ]\n\n\t";

		// load data
		final var certified = certsRepo.streamCertifiedBy(pk).collect(toList());
		final var certifier = certsRepo.streamCertifiersOf(pk).collect(toList());

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
			final var pubk = c.getCertified().getPubkey();
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
			final var pubk = c.getCertifier().getPubkey();
			return "__" + pubk + " [label=\"" + mini(pubk) + "\", URL=\"/graphviz/svg/certs/" + pubk + "\"];";
		}).collect(joining("\n\t\t"));

		res += "\n\t}\n\t";

		// print edges
		final var allNodes = Stream.concat(certified.stream(), certifier.stream());
		res += allNodes.map(cert -> "_" + cert.getCertified() + " -> " + "__" + cert.getCertifier()
				+ " [URL=\"/graphviz/svg/block/" + cert.getBlockNumber() + "\"]").collect(joining(";\n\t"));

		return res + "\n}";
	}

	/**
	 * Generic Graphviz service of the form
	 * /graphviz/{fileType=[dot|svg]}/{output=[certs|block|tx]}/{identifier}
	 *
	 * @param request
	 * @param response
	 * @param fileType
	 * @param output
	 * @param identifier
	 * @return
	 * @throws IOException
	 */
	@Transactional
	@RequestMapping(value = "/{fileType}/{output}/{identifier}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> generic(HttpServletRequest request, HttpServletResponse response, //
			@PathVariable("fileType") FileOutputType fileType, @PathVariable("output") GraphOutput output,
			@PathVariable("identifier") String identifier) throws IOException {

		final var extraParams = request.getParameterMap();

		final var headers = new HttpHeaders();
		var outContent = "";
		var gv = "";

		LOG.info("[GET] /graphviz/{}/{}/{}", fileType, output, identifier);

		extraParams.forEach((k, v) -> {
			LOG.info("  -  {} -> {} ", k, v);
			// headers.setAtt(k, Arrays.asList(v));
			// TODO: forward parameters
		});

		// Build the dot file

		switch (output) {
		case block:
			gv = blockGraph(Integer.valueOf(identifier), extraParams);
			break;
		case certs:
			gv = certsGraph(identifier);
			break;
		case tx:
			gv = txGraph(identifier, extraParams);
			break;
		}

		// decide whether or not to compute the graph
		switch (fileType) {
		case dot:
			headers.setContentType(MediaType.valueOf("text/plain"));
			outContent = gv;
			break;
		case svg:
			headers.setContentType(MediaType.valueOf("image/svg+xml"));
			outContent = localConvertToSVG(gv, identifier);
			break;
		}

		return new ResponseEntity<>(outContent, headers, HttpStatus.OK);
	}

	private String localConvertToSVG(String graph, String fileName) {
		final String svgOut = String.format(svgFile, fileName);
		final String dotOut = String.format(dotFile, fileName);

		try (PrintWriter out = new PrintWriter(dotOut)) {
			out.println(graph);
			out.close();
			LOG.info("Created output graphviz file:" + dotOut);
			run("/usr/bin/dot -Tsvg " + dotOut + " -o " + svgOut);
			LOG.info("Created output graphviz SVG:" + svgOut);
			return Files.readAllLines(Paths.get(svgOut)).stream().collect(joining());
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String mini(String s) {
		return s.substring(0, 7) + "..." + s.substring(s.length() - 7);
	}

	public String txGraph(String hash, Map<String, String[]> extraParams) {

		final var rankdir = extraParams.keySet().contains("rankdir") ? extraParams.get("rankdir") : "LR";

		var res = "";

		final var tx = txRepo.findByTHash(hash).get(0);

		res += "digraph{\n" //
				+ "\tgraph [rankdir=" + rankdir + "];\n";
		res += "\tsum [label=\"sum\\n" + tx.getInputs().stream().mapToInt(i -> i.getAmount()).sum()
				+ "\", tooltip=\"huhuhaha\"];\n";

		//
		// DRAW THE ACTUAL TRANSACTION
		//
		res += "\n\tsubgraph cluster_actual{\n" //
				+ "\t\tlabel=\"Actual transaction\";\n" //
				+ "\t\tlabelloc=t;\n" //
				+ "\t\tcolor=blue;\n"//
				+ "\t\tgood [label=\"Product/Service\\n" + tx.getComment() + "\"];\n"
				+ "\t\tgood -> seller [dir=back, label=\"sell\"];\n" //
				+ "\t\tbuyer -> good [label=\"buy\"];\n"//
				+ "\n";//

		res += "\t\tinfo [labeljust=l, shape=box3d, label=\"" //
				+ "Bstamp: " + mini(tx.getBlockstamp().toString()) //
				+ "\\lCur: " + tx.getCurrency()//
				+ "\\lhash: " + mini(tx.getThash().toString())//
				+ "\\llocktime: " + tx.getLocktime() //
				+ "\\l\", URL=\"/graphviz/svg/block/" + tx.getBlockstamp().toString().split("-")[0] + "\"];\n";

		res += "\tinfo -> good; \n";

		res += "\t}\n\n";//

		//
		// DRAW THE INPUTS
		//
		var links = "";
		res += "\n\tsubgraph cluster_inputs {\n" //
				+ "\t\tlabel=\"Inputs\";\n" //
				+ "\t\tcolor=blue;\n"//
				+ "\t\tlabelloc=t;\n";

		int i = 0;
		for (final TxInput txi : tx.getInputs()) {
			final var input = "input_" + i;

			if (txi.getType().equals(TxType.D)) {
				res += "\t\t" + input + " ["//
						+ "label=\"UD\\n" + txi.getAmount() + "\", URL=\"/graphviz/svg/block/" + txi.getDBlockID() //
						+ "\", shape=circle, color=yellow, style=filled, fontsize = 8];\n"; //
				links += "\t\t_" + txi.getDsource() + " -> " + input + "[color=lightgrey];\n";
			} else if (txi.getType().equals(TxType.T)) {
				res += "\t\t" + input + " ["//
						+ "label=\"" + txi.getAmount() + " g1 | index :" + txi.getTIndex() + " | "
						+ mini(txi.getTHash().toString()) + "\", URL=\"/graphviz/svg/tx/" + txi.getTHash() //
						+ "\", shape=record, fontsize = 8 ];\n"; //

			}

		}
		res += "\t}\n" + links + "\n\n";//

		//
		// DRAW THE UNLOCKS
		//
		links = "";
		res += "\tsubgraph cluster_unlocks{\n" //
				+ "\t\tlabel=\"Unlocks\";\n" //
				+ "\t\tcolor=blue;\n"//
				+ "\t\tlabelloc=t;\n" //
				+ "\t\tdbu [label=\"useful\\nfor\\nstate\\nmachine\", shape=cylinder];\n";
		i = 0;
		for (final String sign : tx.getSignatures()) {
			res += "\t\tsignature_" + i + " [label=\"Signature:\\n" + mini(sign) + "\"];\n";
			links += "\t\tsignature_" + i + " -> sum; \n";
		}

		res += "\t\tunlocks [shape=record, label=\"";
		i = 0;
		for (final TxUnlock unlock : tx.getUnlocks()) {
			res += "{ <k" + i + "> " + unlock.getInputRef() + " | <v" + i + "> " + unlock.getFunction() + " }";
			if (i != tx.getUnlocks().size() - 1) {
				res += " | ";
			}

			// link
			links += "\t\tinput_" + i + " -> unlocks:k" + i + " [color=lightgrey, style=dashed];\n";
			links += "\t\tunlocks:v" + i + " -> signature_" + unlock.getFctParam()
					+ " [color=lightgrey, style=dashed];\n";
		}

		res += "\"];\n\n";

		res += "\t}\n\n" + links;//

		links = "";
		//
		// DRAW THE OUTPUTS
		//
		res += "\n\tsubgraph cluster_outputs{\n" //
				+ "\t\tlabel=\"Outputs\";\n" //
				+ "\t\tcolor=blue;\n"//
				+ "\t\tlabelloc=t;\n"//
				+ "\t\tdbo [label=\"useful\\nfor\\nstate\\nmachine\", shape=cylinder];\n";

		res += "\t\tlockouts [shape=record, label=\"";

		i = 0;
		for (final TxOutput out : tx.getOutputs()) {
			res += "{<a" + i + "> "
//			+ out.Amount() + " | <x" + i + "> " + out.Base() + " | <y" + i + "> "
					+ mini(out.getOutputCondition()) + " }";
			if (i != tx.getOutputs().size() - 1) {
				res += " | ";
			}

			if (out.getOutputCondition().startsWith("SIG(")) {
				final var dest = out.getOutputCondition().substring(4, out.getOutputCondition().length() - 1);
				links += "\t\tamountOut" + i + " [label=\"" + out.getAmount() + "\"" //
						+ ", URL=\"#\"" //
						+ ", shape=signature, fontsize=9];\n"; //

				links += "\t\t_dest" + dest + " [label=\"" + mini(dest)
						+ "\", weight=0, shape=pentagon, fontsize=9];\n";

				if (tx.getIssuers().stream().map(p -> p.getPubkey()).anyMatch(s -> dest.equals(s))) { // is Issuer?
					links += "\t\tamountOut" + i + " -> _dest" + dest + " [weight=0, label=\"the rest\"];\n";
				} else {
					links += "\t\tamountOut" + i + " -> _dest" + dest + " [weight=0];\n";
				}
				links += "\t{rank = same; _dest" + dest + "; seller;}\n";
			}

			// link
			links += "\t\tsum -> lockouts:a" + i + " [color=lightgrey, style=dashed];\n";

			links += "\t\tlockouts:a" + i + " -> amountOut" + i + " [color=lightgrey, style=dashed];\n";
		}
		res += "\"];\n\t";
		res += "\t}\n\n" + links;//

//		// EDGES
//		for (int i = 0; i < tx.outputs().size(); i++) {
//			final TxOutput out = tx.outputs().get(i);
//			res += "\t\tamountOut" + i + " [label=\"" + out.Amount() + "\"" //
//					+ ", URL=\"/graphviz/svg/tx/" + out.functionReferenceValue() + "\"" //
//					+ ", shape=signature];\n"; //
//
////			res += "\t\tsum -> " + "lockOut" + out.hashCode() + " [label=\"SIG\",weight=10, color=lightgrey];\n";

//			res += "\t\tlockOut" + out.hashCode() + " -> amountOut" + i + ";\n";
//		}
		for (final Pubkey issuer : tx.getIssuers()) {
			res += "\t_" + issuer.getPubkey() + " [label=\"issuer\\n" + mini(issuer.getPubkey())
					+ "\", shape=pentagon];\n";
//			res += "\t_" + issuer.getPubkey() + " -> cluster_inputs;\n";
			res += "\t{rank = same; _" + issuer.getPubkey() + "; buyer;}\n";

		}
		res += "\t{rank = same; good; sum; info};\n";
		res += "\t{rank = same; buyer; input_0 };\n";
		return res + "\n}";

	}

}
