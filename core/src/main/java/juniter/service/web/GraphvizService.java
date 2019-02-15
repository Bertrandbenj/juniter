package juniter.service.web;

import juniter.core.model.DBBlock;
import juniter.core.model.business.tx.*;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.jpa.CertsRepository;
import juniter.repository.jpa.TxRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Controller
@ConditionalOnExpression("${juniter.useGraphViz:false} && ${juniter.useBMA:false}")
@RequestMapping("/graphviz")
public class GraphvizService {

    public enum FileOutputType {
        dot("dot"), //
        svg("svg");

        private final String gvOutputType;

        FileOutputType(String ept) {
            gvOutputType = ept;
        }

        public String getFileOutputType() {
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

        public String getGraphOutputType() {
            return gvOutput;
        }
    }

    private static final Logger LOG = LogManager.getLogger();

    @Value("${juniter.dataPath:/tmp/juniter/data/}")
    private String dataPath;

    private final String svgFile = "%sdot/%s.svg";

    private final String dotFile = "%sdot/%s.dot";


    @Autowired
    private CertsRepository certsRepo;

    @Autowired
    private BlockRepository blockRepo;

    @Autowired
    private TxRepository txRepo;

    /**
     * Fail safe command execution
     * TODO : streamline that for god sake, no need to have 2 intermediate disk write
     *
     * @param cmd :
     * @return :
     */
    private static Object run(String cmd) {
        try {
            LOG.info("Executing : " + cmd);
            final Process process = new ProcessBuilder("bash", "-c", cmd).start();

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


    private String errorSVG(String message) {
        return "digraph { \n" +
                "\tgraph [rankdir=LR ]\n"+
                "\terror ->  \"" + message + "\"\n " +
                "}";
    }

    private String blockGraph(Integer blockNumber, Map<String, String[]> extraParams) {

        final Integer RANGE = extraParams.containsKey("range") ? Integer.valueOf(extraParams.get("range")[0]) : 2;

        List<DBBlock> blocks = null;

        try (var bl = blockRepo.streamBlocksFromTo(blockNumber - RANGE, blockNumber + RANGE + 1)) {
            blocks = bl.sorted(Comparator.comparing(DBBlock::getNumber))
                    .collect(toList());
        } catch (final Exception e) {
            LOG.error("blockGraph : fetching blocs ", e);
            return errorSVG(e.getMessage());
        }

        String res = "digraph{\n\t" //
                + "graph [rankdir=LR ]\n\n\t";

        // Print the nodes
        res += blocks.stream().map(b -> {

            final var prefix = "_" + b.getNumber();
            final var formattedDtm = Instant //
                    .ofEpochSecond(b.getMedianTime()) //
                    .atZone(ZoneId.of("GMT+1")) //
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            StringBuilder sub = new StringBuilder();

            // Draw the detailed subgraph
            sub.append("\n\tsubgraph cluster_").append(b.getNumber()).append("{\n\t\t" //
            ).append("graph [rankdir=TB]\n\t\t" //
            ).append("node [shape=underline]\n\t\t" //
            ).append("style=filled;\n\t\t" //
            ).append("color=lightgrey;\n");

            // print the main node
            sub.append("\t\t").append(prefix).append(" [label=\"  Block #\\n").append(b.getNumber()).append("  \", URL=\"/graphviz/svg/block/").append(b.getNumber()).append("\", shape=box3d];\n");

            sub.append("\t\t").append(prefix).append("hash [label=\"Hash: ").append(mini(b.getHash())).append("\"];\n");
            if (b.getNumber() > 0) {
                sub.append("\t\t").append(prefix).append("phash [label=\"PHash: ").append(mini(b.getPreviousHash())).append("\"];\n");
            }

            sub.append("\t\t").append(prefix).append("issuer [label=\"issuer:\\n").append(mini(b.getIssuer())).append("\", URL=\"/graphviz/svg/certs/").append(b.getIssuer()).append("\", shape=pentagon];\n");
            if (b.getNumber() > 0) {
                sub.append("\t\t").append(prefix).append("pissuer [label=\"pissuer:\\n").append(mini(b.getPreviousIssuer())).append("\", shape=pentagon];\n");
            }

            var deltaTime = "N/A";
            if (b.getNumber() > 0) {
                final var delta = -blockRepo
                        .block(b.getNumber() - 1)
                        .map(bb -> b.getMedianTime() - bb.getMedianTime())
                        .orElse(0L);
                deltaTime = Long.divideUnsigned(delta, 60) + "m " + delta % 60 + "s";
            }

            sub.append("\t\t").append(prefix).append("info [labeljust=l, shape=plaintext, ").append("label=\"Mbr: ").append(b.getMembersCount() //
            ).append("\\lCur: ").append(b.getCurrency()//
            ).append("\\lVer: ").append(b.getVersion()//
            ).append("\\lPow: ").append(b.getPowMin() //
            ).append("\\lissCnt: ").append(b.getIssuersCount() //
            ).append("\\lissFrm: ").append(b.getIssuersFrame() //
            ).append("\\lissFVar: ").append(b.getIssuersFrameVar()//
            ).append("\\lM.M: ").append(b.getMonetaryMass() //
            ).append("\\lTim: ").append(formattedDtm //
            ).append("\\lDeltaTime: ").append(deltaTime//
            ).append("\\lSign: ").append(mini(b.getSignature().toString()) //
            ).append("\\l\"];\n");

            if (b.getTransactions().size() > 0) {

                sub.append("\n\t\tsubgraph cluster_").append(b.getNumber()).append("Tx {\n" //
                ).append("\t\t\tgraph [rankdir=LR, style=dotted, color=black]\n" //
                ).append("\t\t\tlabelloc=\"t\";\n" //
                ).append("\t\t\tlabel=\"Transactions\";");
//						+ "\t\t\t" + prefix + "tx [label=\"TransactionDTO\", shape=circle];\n"; //

                for (final Transaction tx : b.getTransactions()) {
                    sub.append("\t\t\ttx").append(tx.getThash()).append(" [label=\"  ").append(tx.getIssuers().stream().map(i -> mini(i.toString())).collect(joining("\\n"))).append("  \",").append("URL=\"/graphviz/svg/tx/").append(tx.getThash()).append("\",").append(" shape=rarrow];\n");
                }

                sub.append("\t\t}\n");

            }
            if (b.getCertifications().size() > 0) {
                sub.append("\t\t").append(prefix).append("Ce [label=\"Certs: ").append(b.getCertifications().size()
//						+ "\", shape=octagon, URL=\"/graphviz/svg/certs/" + b.getCertifications().get(0).getCertified() TODO fix LIST TO SET
                ).append("\"];\n");
            }

            if (b.getIdentities().size() > 0) {
                sub.append("\t\t").append(prefix).append("idtyByPubkey [label=\"Idty: ").append(b.getIdentities().size()).append("\", shape=octagon];\n");
            }

            if (b.getExcluded().size() > 0) {
                sub.append("\t\t").append(prefix).append("exc [label=\"Excluded: ").append(b.getExcluded().size()).append("\", shape=octagon];\n");
            }

            if (b.getActives().size() > 0) {
                sub.append("\t\t").append(prefix).append("act [label=\"Actives: ").append(b.getRenewed().size()).append("\", shape=octagon];\n");
            }

            if (b.getRevoked().size() > 0) {
                sub.append("\t\t").append(prefix).append("rev [label=\"Revoked: ").append(b.getRevoked().size()).append("\", shape=octagon];\n");
            }

            if (b.getJoiners().size() > 0) {
                sub.append("\t\t").append(prefix).append("joi [label=\"Joiners: ").append(b.getJoiners().size()).append("\", shape=octagon];\n");
            }

            if (b.getLeavers().size() > 0) {
                sub.append("\t\t").append(prefix).append("lea [label=\"Leavers: ").append(b.getLeavers().size()).append("\", shape=octagon];\n");
            }

            if (b.getDividend() != null) {
                sub.append("\t\t").append(prefix).append("Ud [label=\"UD\\n").append(b.getDividend()).append("\", shape=circle, color=yellow, style=filled];\n");
            }

            sub.append("\t}");

            // link it to its details block
            // sub += "\n\t"+ prefix + " -> " + prefix + "hash [lhead=cluster_" +
            // b.getNumber() + "];\n";

            return sub.toString();
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
            final var pubk = c.getCertified();
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
            final var pubk = c.getCertifier();
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
     * @param request    :
     * @param response   :
     * @param fileType   :
     * @param output     :
     * @param identifier :
     * @return :
     */
    @Transactional(readOnly = true)
    @RequestMapping(value = "/{fileType}/{output}/{identifier}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<String> generic(
            HttpServletRequest request,
            HttpServletResponse response, //
            @PathVariable("fileType") FileOutputType fileType,
            @PathVariable("output") GraphOutput output,
            @PathVariable("identifier") String identifier) {

        LOG.info("[GET] /graphviz/{}/{}/{}", fileType, output, identifier);

        // handle GET parameters map
        Map<String, String[]> extraParams = null;
        if (request != null) {
            extraParams = request.getParameterMap();
            extraParams.forEach((k, v) -> {
                LOG.info("extraParams  -  {} -> {} ", k, v);
                // TODO: forward parameters
            });
        }

        // build the graph
        String outContent = build(fileType, output, identifier, extraParams);

        // add http headers
        final var headers = new HttpHeaders();
        switch (fileType) {
            case dot:
                headers.setContentType(MediaType.valueOf("text/plain"));
                break;
            case svg:
                headers.setContentType(MediaType.valueOf("image/svg+xml"));
                break;
        }


        return new ResponseEntity<>(outContent, headers, HttpStatus.OK);
    }

    /**
     * Build the graphviz .dot and / or the corresponding .svg
     *
     * @param fileType    :
     * @param output      :
     * @param identifier  :
     * @param extraParams :
     * @return :
     */
    @Transactional(readOnly = true)
    public String build(FileOutputType fileType,
                        GraphOutput output,
                        String identifier, Map<String, String[]> extraParams) {

        var outContent = "";
        var gv = "";


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
                outContent = gv;
                break;
            case svg:
                outContent = localConvertToSVG(gv, identifier);
                break;
        }

        return outContent;
    }

    private String localConvertToSVG(String graph, String fileName) {
        final String svgOut = String.format(svgFile, dataPath, fileName);
        final String dotOut = String.format(dotFile, dataPath, fileName);

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

        StringBuilder res = new StringBuilder();

        final var tx = txRepo.findByTHash(hash).get(0);

        res.append("digraph{\n" //
                + "\tgraph [rankdir=").append(rankdir).append("];\n");
        res.append("\tsum [label=\"sum\\n").append(tx.getInputs().stream().mapToInt(i -> i.getAmount()).sum()).append("\", tooltip=\"huhuhaha\"];\n");

        //
        // DRAW THE ACTUAL TRANSACTION
        //
        res.append("\n\tsubgraph cluster_actual{\n" //
                + "\t\tlabel=\"Actual transaction\";\n" //
                + "\t\tlabelloc=t;\n" //
                + "\t\tcolor=blue;\n"//
                + "\t\tgood [label=\"Product/Service\\n").append(tx.getComment()).append("\"];\n").append("\t\tgood -> seller [dir=back, label=\"sell\"];\n" //
        ).append("\t\tbuyer -> good [label=\"buy\"];\n"//
        ).append("\n");//

        res.append("\t\tinfo [labeljust=l, shape=box3d, label=\"" //
                + "Bstamp: ").append(mini(tx.getBlockstamp().toString()) //
        ).append("\\lCur: ").append(tx.getCurrency()//
        ).append("\\lhash: ").append(mini(tx.getThash())//
        ).append("\\llocktime: ").append(tx.getLocktime() //
        ).append("\\l\", URL=\"/graphviz/svg/block/").append(tx.getBlockstamp().toString().split("-")[0]).append("\"];\n");

        res.append("\tinfo -> good; \n");

        res.append("\t}\n\n");//

        //
        // DRAW THE INPUTS
        //
        var links = "";
        res.append("\n\tsubgraph cluster_inputs {\n" //
                + "\t\tlabel=\"Inputs\";\n" //
                + "\t\tcolor=blue;\n"//
                + "\t\tlabelloc=t;\n");

        int i = 0;
        for (final TxInput txi : tx.getInputs()) {
            final var input = "input_" + i;

            if (txi.getType().equals(TxType.D)) {
                res.append("\t\t").append(input).append(" ["//
                ).append("label=\"UD\\n").append(txi.getAmount()).append("\", URL=\"/graphviz/svg/block/").append(txi.getDBlockID() //
                ).append("\", shape=circle, color=yellow, style=filled, fontsize = 8];\n"); //
                links += "\t\t_" + txi.getDsource() + " -> " + input + "[color=lightgrey];\n";
            } else if (txi.getType().equals(TxType.T)) {
                res.append("\t\t").append(input).append(" ["//
                ).append("label=\"").append(txi.getAmount()).append(" g1 | index :").append(txi.getTIndex()).append(" | ").append(mini(txi.getTHash().toString())).append("\", URL=\"/graphviz/svg/tx/").append(txi.getTHash() //
                ).append("\", shape=record, fontsize = 8 ];\n"); //

            }

        }
        res.append("\t}\n").append(links).append("\n\n");//

        //
        // DRAW THE UNLOCKS
        //
        links = "";
        res.append("\tsubgraph cluster_unlocks{\n" //
                + "\t\tlabel=\"Unlocks\";\n" //
                + "\t\tcolor=blue;\n"//
                + "\t\tlabelloc=t;\n" //
                + "\t\tdbu [label=\"useful\\nfor\\nstate\\nmachine\", shape=cylinder];\n");
        i = 0;
        for (final String sign : tx.getSignatures()) {
            res.append("\t\tsignature_").append(i).append(" [label=\"Signature:\\n").append(mini(sign)).append("\"];\n");
            links += "\t\tsignature_" + i + " -> sum; \n";
        }

        res.append("\t\tunlocks [shape=record, label=\"");
        i = 0;
        for (final TxUnlock unlock : tx.getUnlocks()) {
            res.append("{ <k").append(i).append("> ").append(unlock.getInputRef()).append(" | <v").append(i).append("> ").append(unlock.getFunction()).append(" }");
            if (i != tx.getUnlocks().size() - 1) {
                res.append(" | ");
            }

            // link
            links += "\t\tinput_" + i + " -> unlocks:k" + i + " [color=lightgrey, style=dashed];\n";
            links += "\t\tunlocks:v" + i + " -> signature_" + unlock.getFctParam()
                    + " [color=lightgrey, style=dashed];\n";
        }

        res.append("\"];\n\n");

        res.append("\t}\n\n").append(links);//

        links = "";
        //
        // DRAW THE OUTPUTS
        //
        res.append("\n\tsubgraph cluster_outputs{\n" //
                + "\t\tlabel=\"Outputs\";\n" //
                + "\t\tcolor=blue;\n"//
                + "\t\tlabelloc=t;\n"//
                + "\t\tdbo [label=\"useful\\nfor\\nstate\\nmachine\", shape=cylinder];\n");

        res.append("\t\tlockouts [shape=record, label=\"");

        i = 0;
        for (final TxOutput out : tx.getOutputs()) {
            res.append("{<a").append(i).append("> "
//			+ out.Amount() + " | <x" + i + "> " + out.Base() + " | <y" + i + "> "
            ).append(mini(out.getOutputCondition())).append(" }");
            if (i != tx.getOutputs().size() - 1) {
                res.append(" | ");
            }

            if (out.getOutputCondition().startsWith("SIG(")) {
                final var dest = out.getOutputCondition().substring(4, out.getOutputCondition().length() - 1);
                links += "\t\tamountOut" + i + " [label=\"" + out.getAmount() + "\"" //
                        + ", URL=\"#\"" //
                        + ", shape=signature, fontsize=9];\n"; //

                links += "\t\t_dest" + dest + " [label=\"" + mini(dest)
                        + "\", weight=0, shape=pentagon, fontsize=9];\n";

                if (tx.getIssuers().stream().map(p -> p).anyMatch(s -> dest.equals(s))) { // is Issuer?
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
        res.append("\"];\n\t");
        res.append("\t}\n\n").append(links);//

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
        for (final String issuer : tx.getIssuers()) {
            res.append("\t_").append(issuer).append(" [label=\"issuer\\n").append(mini(issuer)).append("\", shape=pentagon];\n");
//			res += "\t_" + issuer.getPubkey() + " -> cluster_inputs;\n";
            res.append("\t{rank = same; _").append(issuer).append("; buyer;}\n");

        }
        res.append("\t{rank = same; good; sum; info};\n");
        res.append("\t{rank = same; buyer; input_0 };\n");
        return res + "\n}";

    }

}
