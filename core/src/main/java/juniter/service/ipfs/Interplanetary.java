package juniter.service.ipfs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ipfs.api.IPFS;
import io.ipfs.multiaddr.MultiAddress;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dto.net.DifficultiesDTO;
import juniter.core.model.dto.node.Block;
import juniter.core.model.dto.node.WithDTO;
import juniter.service.bma.BlockchainService;
import juniter.service.bma.loader.BMABlockFetcher;
import juniter.service.core.BlockService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@ConditionalOnExpression("${juniter.useIPFS:true}")
public class Interplanetary {
    private static final Logger LOG = LogManager.getLogger(Interplanetary.class);


    // ====== FILES AND DIRS ========
    private static final String ROOT_DIR = "/Juniter";

    private static final String G1_DIR = ROOT_DIR + "/ğ1";
    private static final String G1_TEST_DIR = ROOT_DIR + "/ğ1-test";

    private static final String CHAIN_DIR = G1_DIR + "/chain";
    private static final String BLOCKS_DIR = G1_DIR + "/blocks";
    private static final String DAILY_DIR = G1_DIR + "/daily";
    private static final String INDEX_DIR = G1_DIR + "/index";


    // Services
    private static final String SERVICE_DIR = G1_DIR + "/service";
    private static final String WITH_DIR = SERVICE_DIR + "/with";
    private static final String WOT_DIR = SERVICE_DIR + "/wot";
    private static final String PARAMETERS_FILE = SERVICE_DIR + "/parameters";
    private static final String DIFFICULTIES_DIR = SERVICE_DIR + "/difficulties";
    private static final String BRANCHES_FILE = SERVICE_DIR + "/branches";
    private static final String MEMEBERSHIPS_DIR = SERVICE_DIR + "/memberships";


    /**
     * * newcomers,certs,actives,revoked,leavers,excluded,ud,tx"
     */

    private static final String WITH_UD_FILE = WITH_DIR + "/ud";
    private static final String WITH_TX_FILE = WITH_DIR + "/tx";
    private static final String WITH_LEAVERS_FILE = WITH_DIR + "/leavers";
    private static final String WITH_ACTIVES_FILE = WITH_DIR + "/actives";
    private static final String WITH_EXCLUDED_FILE = WITH_DIR + "/excluded";
    private static final String WITH_REVOKED_FILE = WITH_DIR + "/revoked";
    private static final String WITH_CERTS_FILE = WITH_DIR + "/certs";
    private static final String WITH_NEWCOMERS_FILE = WITH_DIR + "/newcomers";


    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private BlockService blockService;

    @Autowired
    private BMABlockFetcher blockLoader;


    @PostConstruct
    private IPFS ipfs() {
        IPFS ipfs = null;
        try {
            ipfs = new IPFS(new MultiAddress("/ip4/127.0.0.1/tcp/5001"));
            ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
//            LOG.info(" ==== IPFS INIT =====");

            ipfs.config.show().forEach((k, v) -> LOG.info("  --  kv: " + k + " : " + v));
//            ipfs.pin.add(Multihash.fromBase58("QmUhVpSmXnTTnpyRivjYADjBEG5MYtr4eP4JEE2qxfVjMd"));
//            ipfs.pin.add(Multihash.fromBase58("QmRBFKnivhKQxy3kZ4vZUCEGMtrckdu9GMeNjkcM497P9z"));
//            ipfs.pin.add(Multihash.fromBase58("QmNqToxUD8nUh476UsFyMUiSTTSgH2WAAnrSR3qL95iHXK"));
//            ipfs.pin.add(Multihash.fromBase58("QmUNLLsPACCz1vLxQVkXqqLX5R1X345qqfHbsf67hvA3Nn"));

        } catch (RuntimeException e) {
            LOG.error("Connecting to IPFS ", e);
        } catch (IOException e) {
            LOG.error("Initializing IPFS ", e);
        }

        return ipfs;
    }


    private void mkdirs() {

        Process process = null;

        try {
            process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "ipfs files mkdir " + ROOT_DIR});
            process.waitFor();
            process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "ipfs files mkdir " + G1_DIR});
            process.waitFor();
            process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "ipfs files mkdir " + G1_TEST_DIR});
            process.waitFor();
            process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "ipfs files mkdir " + SERVICE_DIR});
            process.waitFor();
            process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "ipfs files mkdir " + BLOCKS_DIR});
            process.waitFor();
            process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "ipfs files mkdir " + DAILY_DIR});
            process.waitFor();
            process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "ipfs files mkdir " + INDEX_DIR});
            process.waitFor();
            process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "ipfs files mkdir " + WITH_DIR});
            process.waitFor();
            process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "ipfs files mkdir " + CHAIN_DIR});
            process.waitFor();
            process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "ipfs files mkdir " + MEMEBERSHIPS_DIR});
            process.waitFor();
            process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "ipfs files mkdir " + WOT_DIR});
            process.waitFor();
            process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "ipfs files mkdir " + DIFFICULTIES_DIR});
            process.waitFor();
        } catch (Exception e) {
            LOG.error("mkdirs ", e);
        }
    }

    private void cp(String file, String folder) {
        String[] cmd = {"/bin/bash", "-c", "ipfs files cp /ipfs/" + file + " " + folder};
        LOG.info(" >> ipfs files cp /ipfs/" + file + " " + folder);
        Process process = null;


        try {
            process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
            var stdout = process.getInputStream();

            var line = new BufferedReader(new InputStreamReader(stdout)).readLine();
            var lineerr = new BufferedReader(new InputStreamReader(process.getErrorStream())).readLine();


            LOG.info(" -- cp stdout " + line);
            LOG.info(" -- cp stderr " + lineerr);

        } catch (Exception e) {
            LOG.error(e);
        } finally {
            LOG.info("destroying " + cmd[2]);
            process.destroy();

        }
    }

    private boolean pin(String hash) {
        String[] cmd = {"/bin/bash", "-c", "ipfs pin add " + hash};
        Process process = null;
        String line = null;

        try {
            LOG.info(" >> ipfs pin add " + hash);

            process = Runtime.getRuntime().exec(cmd);
//            var stdout = process.getInputStream();
//
//            var input = new BufferedReader(new InputStreamReader(stdout));
//            while ((line = input.readLine()) != null  )
//                LOG.info(" -- pin stdout: " + line);

            return process.waitFor(1, TimeUnit.SECONDS);

        } catch (Exception e) {
            LOG.error(e);
        } finally {
            LOG.info("destroying " + cmd[2]);
            process.destroy();
        }
        return line != null;

    }


    /**
     * API broken as of writing so using cmd
     */
    private String dagPut(String json) throws Exception {

        String[] cmd = {"/bin/bash", "-c", "ipfs dag queue"};

        LOG.info(" >> echo '" + json + "' | ipfs dag queue");
//        var merkleNode = ipfs.dag.queue("json", json2.getBytes());
//        LOG.info("mNode " + merkleNode.toJSONString());


        Process process = null;
        try {

            process = Runtime.getRuntime().exec(cmd);
            LOG.info(" -- PID " + process.pid());

            var stdin = process.getOutputStream();
            var stderr = process.getErrorStream();
            var stdout = process.getInputStream();

            stdin.write(json.getBytes());
            stdin.flush();

            //process.waitFor(10, TimeUnit.SECONDS);


            var input = new BufferedReader(new InputStreamReader(stdout));
            var line = input.readLine();
            input.close();

            LOG.info(" - stdout: " + line);


            return line;
        } catch (Exception e) {
            throw new Exception("dag queue failed", e);
        } finally {
            LOG.info("destroying " + cmd[2]);
            process.destroy();

        }

    }

    private String saveMemberships() {
        LOG.info("saving memberships ");

        try {
            var json = new String(new ObjectMapper().writeValueAsBytes(new ModelMapper().map(blockchainService.memberships("BnimajneB"), DifficultiesDTO.class)));
            //LOG.info(" - json " + json);

            var path = dagPut(json);

            LOG.info(" - path " + path);

            pin(path);
            cp(json, MEMEBERSHIPS_DIR);

            return path;
        } catch (Exception e) {
            LOG.error("Inserting memberships ", e);
        }

        return null;
    }

    private String saveBranches() {
        LOG.info("saving Branches ");

        try {
            var json = new String(new ObjectMapper().writeValueAsBytes(new ModelMapper().map(blockchainService.branches(), List.class)));
            //LOG.info(" - json " + json);

            var path = dagPut(json);

            LOG.info(" - path " + path);

            pin(path);
            cp(json, BRANCHES_FILE);

            return path;
        } catch (Exception e) {
            LOG.error("Inserting branches ", e);
        }

        return null;
    }

    private String saveDifficulties() {
        LOG.info("saving difficulties ");

        try {
            var json = new String(new ObjectMapper().writeValueAsBytes(new ModelMapper().map(blockchainService.difficulties(), DifficultiesDTO.class)));
            //LOG.info(" - json " + json);

            var path = dagPut(json);

            LOG.info(" - path " + path);

            pin(path);
            cp(json, PARAMETERS_FILE);

            return path;
        } catch (Exception e) {
            LOG.error("Inserting difficulties ", e);
        }

        return null;
    }

    private String saveParameters() {

        LOG.info("saving params ");

        try {
            var json = new String(new ObjectMapper().writeValueAsBytes(blockchainService.parameters(Optional.of("g1"))));
            //LOG.info(" - json " + json);

            var path = dagPut(json);

            LOG.info("path - " + path);

            pin(path);
            cp(json, PARAMETERS_FILE);

            return path;
        } catch (Exception e) {
            LOG.error("Inserting parameters ", e);
        }

        return null;
    }

    private String saveIPFSBlock(DBBlock b) {

        LOG.info("saving block " + b.getNumber() + " " + b);

        try {
            var json = new String(new ObjectMapper().writeValueAsBytes(new ModelMapper().map(b, Block.class)));
            //LOG.info(" - json " + json);

            String path;
            do {
                path = dagPut(json);

                LOG.info("path - " + path);
            } while (!pin(path));


            //publish(path, true);

            return path;
        } catch (Exception e) {
            LOG.error("Inserting Block ", e);
        }

        return null;
    }

    private void saveWith(String what, String file) {
        try {

            var json = new String(new ObjectMapper().writeValueAsBytes(new ModelMapper().map(blockchainService.with(what), WithDTO.class)));
            var path = dagPut(json);
            LOG.info(" - excluded " + path);
            pin(path);
            cp(path, file);

        } catch (Exception e) {
            LOG.error("Inserting With ..  ", e);
        }
    }


    private String summarize(Map<String, String> map) {

        // make a file per UD
        var summary = "{" + map.entrySet().stream()
                .map((e) -> "\"" + e.getKey() + "\":{\"/\":\"" + e.getValue() + "\"}")
                .collect(Collectors.joining(",")) + "}";

        LOG.info(" - sumarized : " + summary);

        String linkFile = null;

        do {
            try {
                linkFile = dagPut(summary);
            } catch (Exception e) {
                LOG.info("summarize ", e);
            }
        } while (!pin(linkFile));


        LOG.info("link file : " + linkFile);
        return linkFile;
    }

    @Async
    public void dumpChain() {
        var time = System.currentTimeMillis();

        var dailyBuffer = new HashMap<String, String>();
        var duBuffer = new HashMap<String, String>();
        var leaversBuffer = new HashMap<String, String>();

        var globalBuffer = new HashMap<String, String>();
        var timeT1 = 0L;

        mkdirs();

        // sample services
        saveDifficulties();
        saveBranches();
        //saveMemberships();
        saveParameters();
        saveWith("ud", WITH_UD_FILE);
        saveWith("tx", WITH_TX_FILE);
        saveWith("excluded", WITH_EXCLUDED_FILE);
        saveWith("leavers", WITH_LEAVERS_FILE);
        saveWith("revoked", WITH_REVOKED_FILE);
        saveWith("actives", WITH_ACTIVES_FILE);
        saveWith("certs", WITH_CERTS_FILE);
        saveWith("newcomers", WITH_NEWCOMERS_FILE);
        //saveWith("ud", WITH_UD_FILE);


        for (int i = 0; i < blockService.currentBlockNumber(); i++) {

            int finalI = i;
            var b = blockService.block(i).orElseGet(() -> blockLoader.fetchAndSaveBlock(finalI));

            var bl = saveIPFSBlock(b);

            var date = format(b.getMedianTime());

            if (format(timeT1) != date) {
                // Handle block

                dailyBuffer.put(i + "", bl);
                cp(summarize(dailyBuffer), DAILY_DIR + "/" + date);
                dailyBuffer.clear();
            }


            cp(bl, BLOCKS_DIR + "/" + i);


            LOG.info("dailyBuffer: " + dailyBuffer);

        }

        // make a file per UD
        var global = summarize(globalBuffer);
        LOG.info("Global: " + global);


        LOG.info("Execution took :" + (System.currentTimeMillis() - time));
    }


    private String format(long unixSeconds) {

        Date date = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);

        return formattedDate;
    }


}
