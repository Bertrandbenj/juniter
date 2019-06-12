package juniter.service.ipfs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.cid.Cid;
import io.ipfs.multihash.Multihash;
import juniter.core.model.dto.node.Block;
import juniter.repository.jpa.block.BlockRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class Interplanetary {
    private static final Logger LOG = LogManager.getLogger(Interplanetary.class);

    @Autowired
    private IPFS ipfs;

    @Autowired
    private BlockRepository blockRepo;


    @Async
    public void resolve(Cid cid) {
        try {
            var resolve = ipfs.resolve("ipfs", cid, true);
            resolve.forEach((k, v) -> LOG.info("  - resolve " + resolve));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void publish(Cid cid) {
        try {
            var st = new String(ipfs.dag.get(cid));
            LOG.info("publishing " + st);
            var publ = ipfs.name.publish(cid);
            publ.entrySet().forEach(v -> LOG.info(" - " + v));

            var name = Multihash.fromBase58(publ.get("Name").toString());

            var ls = ipfs.ls(name);
            for (MerkleNode l : ls) {
                LOG.info("  -  " + l);
            }

            ipfs.pin.add(name);

        } catch (IOException e) {
            LOG.error("Publishing ", e);
        }

    }

    @Async
    public void IPFSBlock(int number) {
        blockRepo.block(number).ifPresent(b -> {

            try {
                var json = new ObjectMapper().writeValueAsBytes(new ModelMapper().map(b, Block.class));
                LOG.info("json " + new String(json));
                var json2 = "{\"data\":1234,\"Type\":\"json\"}".getBytes(StandardCharsets.UTF_8);
                LOG.info("json2 " + new String(json2));
                var merkleNode = ipfs.block.put( json, Optional.of("json"));
                LOG.info("mNode " + merkleNode.toJSONString());

                var map = ipfs.name.publish(merkleNode.hash);
                map.values().stream().forEach(v -> LOG.info("v: " + v));
                LOG.info(map);

            } catch (Exception e) {
                LOG.error("Inserting Block ", e);
            }


        });
    }

//            var x = 123459;
//            do {
//
//                var b = blockRepo.block(x);
//                if (b.isPresent()) {
//
//                    // Json
//                    LOG.info(" JSON");
//                    var json = new ObjectMapper().writeValueAsString(new ModelMapper().map(b.get(), Block.class));
//                    var jsonFile = new NamedStreamable.InputStreamWrapper("block_" + x + ".json", new ByteArrayInputStream(json.getBytes()));
//
//                    // DUP
//                    LOG.info("DUP");
//                    var dupFile = new NamedStreamable.InputStreamWrapper("Block_" + x + ".dup", new ByteArrayInputStream(b.get().toDUP().getBytes()));
//
//                    // Folder
//                    LOG.info("Folder");
//                    var folder = new NamedStreamable.DirWrapper("Block_" + x, List.of(jsonFile, dupFile));
//
//
//                    try {
//                        MerkleNode addResult = ipfs.add(folder).get(0);
//                        var map = ipfs.name.publish(addResult.hash, Optional.of("QmSik1xc3HGsZbKkuANy9QJbfRLGp8u4PMjXBQAm7GEZko"));
//                        LOG.info(" Added https://gateway.ipfs.io/ipns/" + map.get(map.keySet().iterator().next()) + " - " + addResult.toJSONString() + "\n" + new String(ipfs.cat(addResult.hash)));
//                    } catch (Exception e) {
//                        LOG.error("already existing block " + x + " - " + "https://gateway.ipfs.io/ipns/Block_" + x, e);
//                    }
//
//
//                }
//
//            } while (blockRepo.currentBlockNumber() < 10);


    // ipfs.refs.local();
//            ipfs.config.show();

//
//            NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File("/home/bnimajneb/.profile"));
//            MerkleNode addResult = ipfs.add(file).get(0);
//            LOG.info("IPFS add " + addResult.toJSONString() + "\n" + new String(ipfs.cat(addResult.hash)));


//            Multihash read = Multihash.fromBase58("QmfVWqwneMy34qirQw69f1CcK8FrsEfs6hdExtkncikS34");
//            byte[] fileContents = ipfs.cat(read);
//            LOG.info(read + "\nRead content: \n" + new String(fileContents));
//
//
//            var lss = ipfs.ls(Multihash.fromBase58("QmZC7uovmhA3iA66571kRTaeKsnVmaEkq8HUe3vRDzhWtU"));
//
//            lss.forEach(ls -> {
//                LOG.info(ls.toJSONString());
//
//                if (ls.type.orElse(0).equals(1)) {
//                    try {
//                        var lss2 = ipfs.ls(ls.hash);
//                        lss2.forEach(ls2 -> {
//                            LOG.info(" - " + ls2.toJSONString());
//                        });
//                    } catch (IOException e) {
//                        LOG.error(e);
//                    }
//
//
//                }
//            });


}
