package juniter.repository.hadoop;

import juniter.core.model.Block;
import juniter.core.utils.TimeUtils;
import juniter.core.validation.GlobalValid;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.jpa.index.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.sql.*;
import org.apache.spark.storage.StorageLevel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.*;


@ConditionalOnExpression("${juniter.useSpark:true}")
@Service
public class SparkTest {

    private static final Logger LOG = LogManager.getLogger();

    //juniter.Block yeap ;

    @Autowired
    SparkSession spark;

    @Autowired
    BlockRepository blockRepo;

    @Autowired
    BINDEXRepository bindexRepo;
    @Autowired
    CINDEXRepository cindexRepo;
    @Autowired
    IINDEXRepository iindexRepo;
    @Autowired
    MINDEXRepository mindexRepo;

    @Autowired
    SINDEXRepository sindexRepo;

    @Autowired
    ModelMapper modelMapper;

    @Value("${juniter.dataPath:/tmp/juniter/data/}")
    private String dataPath;


    public void dumpIndexes() {
        var bindex = spark.createDataset(bindexRepo.findAll(), Encoders.bean(BINDEX.class));
        var cindex = spark.createDataFrame(cindexRepo.findAll(), CINDEX.class);
        var iindex = spark.createDataFrame(iindexRepo.findAll(), IINDEX.class);
        var mindex = spark.createDataFrame(mindexRepo.findAll(), MINDEX.class);
        var sindex = spark.createDataFrame(sindexRepo.findAll(), SINDEX.class);

        bindex.write().mode(SaveMode.Overwrite).parquet(dataPath + "bindex");
        cindex.write().mode(SaveMode.Overwrite).parquet(dataPath + "cindex");
        iindex.write().mode(SaveMode.Overwrite).parquet(dataPath + "iindex");
        mindex.write().mode(SaveMode.Overwrite).parquet(dataPath + "mindex");
        sindex.write().mode(SaveMode.Overwrite).parquet(dataPath + "sindex");

    }


    public void parseBlockchain() {
        long start = System.nanoTime();
        var blockchain = spark.read()
                .json(dataPath + "dump/*.jsonrows")
                .withColumn("written_on", concat(col("number"), lit("-"), col("hash")))
                .persist(StorageLevel.DISK_ONLY());

        LOG.info("parsed Blockchain :" + blockchain.count());
        blockchain.printSchema();

        //blockchain.show();

        var conf = new GlobalValid.ChainParameters();


        //blockchain.write().mode(SaveMode.Overwrite).parquet(dataPath+"dump/chain");
        //var pq = spark.read().parquet(dataPath+"blockchain/chain").orderBy("number");
        // pq.select("number", "membersCount", "monetaryMass", "version").orderBy("number");


        var idty = blockchain.select(col("identities"),
                col("written_on"),
                col("medianTime").as("writtenTime"))
                .withColumn("x", explode(col("identities"))).drop("identities")
                .withColumn("pubkey", split(col("x"), "\\:").getItem(0))
                .withColumn("signature", split(col("x"), "\\:").getItem(1))
                .withColumn("createdOnOn", split(col("x"), "\\:").getItem(2))
                .withColumn("uid", split(col("x"), "\\:").getItem(3))
                .drop("x")
                .repartition(1);

        var certs = blockchain.select(col("certifications"),
                concat(col("number"), lit("-"), col("hash")).as("written_on"),
                col("medianTime").as("writtenTime"))
                .withColumn("x", explode(col("certifications"))).drop("certifications")
                .withColumn("issuer", split(col("x"), "\\:").getItem(0))
                .withColumn("receiver", split(col("x"), "\\:").getItem(1))
                .withColumn("signedOn", split(col("x"), "\\:").getItem(2))
                .withColumn("signature", split(col("x"), "\\:").getItem(3))
                .drop("x")
                .repartition(2);


        var actives = blockchain.select(col("actives"),
                col("written_on"),
                col("number").as("writtenOn"),
                col("medianTime").as("writtenTime"))
                .withColumn("x", explode(col("actives"))).drop("actives")
                .withColumn("pubkey", split(col("x"), "\\:").getItem(0))
                .withColumn("signature", split(col("x"), "\\:").getItem(1))
                .withColumn("createdOn", split(col("x"), "\\:").getItem(2))
                .withColumn("idtyOn", split(col("x"), "\\:").getItem(3))
                .withColumn("uid", split(col("x"), "\\:").getItem(4))
                .join(blockchain.select(
                        col("written_on").as("createdOn"),
                        col("medianTime").plus(conf.msValidity).as("expires_on"),
                        col("medianTime").plus(conf.msValidity * 2).as("revokes_on"))
                        , "createdOn")
                .withColumn("chainable_on", col("writtenTime").plus(conf.msPeriod))
                .drop("x")
                .repartition(1);


        var joiners = blockchain.select(col("joiners"),
                col("written_on"),
                col("number").as("writtenOn"),
                col("medianTime").as("writtenTime"))
                .withColumn("x", explode(col("joiners"))).drop("joiners")
                .withColumn("pubkey", split(col("x"), "\\:").getItem(0))
                .withColumn("signature", split(col("x"), "\\:").getItem(1))
                .withColumn("createdOn", split(col("x"), "\\:").getItem(2))
                .withColumn("idtyOn", split(col("x"), "\\:").getItem(3))
                .withColumn("uid", split(col("x"), "\\:").getItem(4))
                .join(blockchain.select(
                        col("written_on").as("createdOn"),
                        col("medianTime").plus(conf.msValidity).as("expires_on"),
                        col("medianTime").plus(conf.msValidity * 2).as("revokes_on"))
                        , "createdOn")
                .withColumn("chainable_on", col("writtenTime").plus(conf.msPeriod))
                .drop("x")
                .repartition(1);

        var leavers = blockchain.select(col("leavers"),
                concat(col("number"), lit("-"), col("hash")).as("written_on"),
                col("medianTime").as("writtenTime"))
                .withColumn("x", explode(col("leavers"))).drop("leavers")
                .withColumn("pubkey", split(col("x"), "\\:").getItem(0))
                .withColumn("signature", split(col("x"), "\\:").getItem(1))
                .withColumn("createdOn", split(col("x"), "\\:").getItem(2))
                .withColumn("idtyOn", split(col("x"), "\\:").getItem(3))
                .withColumn("uid", split(col("x"), "\\:").getItem(4))
                .drop("x")
                .repartition(1);

        var excluded = blockchain.select(col("excluded"),
                concat(col("number"), lit("-"), col("hash")).as("written_on"),
                col("medianTime").as("writtenTime"))
                .withColumn("x", explode(col("excluded"))).drop("excluded")
                .withColumn("issuer", split(col("x"), "\\:").getItem(0))
                .drop("x")
                .repartition(1);

        var revocs = blockchain.select(col("revoked"),
                concat(col("number"), lit("-"), col("hash")).as("written_on"),
                col("medianTime").as("writtenTime"))
                .withColumn("x", explode(col("revoked"))).drop("revoked")
                .withColumn("revoked", split(col("x"), "\\:").getItem(0))
                .withColumn("signature", split(col("x"), "\\:").getItem(1))
                .drop("x")
                .repartition(1);


        certs.write().mode(SaveMode.Overwrite).parquet(dataPath + "blockchain/certs");
        idty.write().mode(SaveMode.Overwrite).parquet(dataPath + "blockchain/idty");
        revocs.write().mode(SaveMode.Overwrite).parquet(dataPath + "blockchain/revocs");
        actives.write().mode(SaveMode.Overwrite).parquet(dataPath + "blockchain/actives");
        excluded.write().mode(SaveMode.Overwrite).parquet(dataPath + "blockchain/excluded");
        leavers.write().mode(SaveMode.Overwrite).parquet(dataPath + "blockchain/leavers");
        joiners.write().mode(SaveMode.Overwrite).parquet(dataPath + "blockchain/joiners");


        var mindex = actives.union(
                    joiners.join(actives.select(
                            col("written_on").as("awritten_on"),
                            col("pubkey").as("apubkey"))
                        , col("written_on").equalTo(col("awritten_on")).and(col("pubkey").equalTo(col("apubkey"))),
                        "left_outer")
                        .drop("awritten_on")
        )
                .withColumn("op", lit("CREATE"))
                .withColumn("leaving", lit(false));

        mindex.write().mode(SaveMode.Overwrite).parquet(dataPath + "blockchain/mindex");


        var transactions = blockchain.select(explode(col("transactions")).as("tx"),
                concat(col("number"), lit("-"), col("hash")).as("written_on"),
                col("medianTime").as("writtenTime"))
                .select(col("writtenTime"),
                        col("written_on"),
                        col("tx.blockstamp").as("txStamp"),
                        col("tx.blockstampTime"),
                        col("tx.comment"),
                        col("tx.currency"),
                        col("tx.hash"),
                        col("tx.locktime"),
                        col("tx.version"),
                        col("tx.inputs"),
                        col("tx.unlocks"),
                        col("tx.outputs"),
                        col("tx.signatures"))
                .cache();


        var txInputs = transactions
                .withColumn("in", explode(col("inputs"))).drop("inputs")
                .withColumn("amount", split(col("in"), "\\:").getItem(0))
                .withColumn("base", split(col("in"), "\\:").getItem(1))
                .withColumn("type", split(col("in"), "\\:").getItem(2))
                .withColumn("identifier", split(col("in"), "\\:").getItem(3))
                .withColumn("index", split(col("in"), "\\:").getItem(4))
                .drop("in", "version", "currency", "comment", "inputs", "outputs", "signatures", "unlocks", "locktime", "hash", "blockstampTime")
                .repartition(1);

        var txOutputs = transactions
                .withColumn("out", explode(col("outputs"))).drop("outputs")
                .withColumn("amount", split(col("out"), "\\:").getItem(0))
                .withColumn("base", split(col("out"), "\\:").getItem(1))
                .withColumn("condition", split(col("out"), "\\:").getItem(2))
                .drop("out", "version", "currency", "comment", "inputs", "outputs", "signatures", "unlocks", "locktime", "hash", "blockstampTime")
                .repartition(1);

        var txUnlocks = transactions
                .withColumn("un", explode(col("unlocks"))).drop("unlocks")
                .withColumn("inRef", split(col("un"), "\\:").getItem(0))
                .withColumn("function", split(col("un"), "\\:").getItem(1))
                .drop("un", "version", "currency", "comment", "inputs", "outputs", "signatures", "unlocks", "locktime", "hash", "blockstampTime")
                .repartition(1);


        transactions.write().mode(SaveMode.Overwrite).parquet(dataPath + "blockchain/transactions");
        txInputs.write().mode(SaveMode.Overwrite).parquet(dataPath + "blockchain/inputs");
        txUnlocks.write().mode(SaveMode.Overwrite).parquet(dataPath + "blockchain/unlocks");
        txOutputs.write().mode(SaveMode.Overwrite).parquet(dataPath + "blockchain/outputs");

        //blockchain.where("dividend != null").select("number","hash","dividend").show

        // Scala join certs table wit
        //certs
        // .join(idty.select($"uid".as("issuerUID"), $"pubkey".as("issuer")), "issuer")
        // .join(idty.select($"uid".as("receiverUID"), $"pubkey".as("receiver")), "receiver")
        // .where("issuerUID LIKE '%bul%'").count

        final var elapsed = Long.divideUnsigned(System.nanoTime() - start, 1000000);
        LOG.info("Spark parsing blockchain :" +
                " Elapsed time: " + TimeUtils.format(elapsed));

    }

    private void stuff() {
        var df = spark.createDataFrame(
                blockRepo.blocksFromTo(0, 52)
                        .stream()
//                        .map(b -> {
//                            // modelMapper.map(b, juniter.Block.class)
//                            var res = new Block(b.getVersion(), b.getNonce(), b.getNumber(), b.getPowMin(), b.getTime(), b.getMedianTime(), b.getMembersCount(), b.getMonetaryMass(), b.getUnitbase(), b.getIssuersCount(), b.getIssuersFrame(), b.getIssuersFrameVar(), b.getCurrency(), b.getIssuer(), b.getSignature().getSignature(), b.getHash(), b.getParameters(), b.getPreviousHash(), b.getPreviousIssuer(), b.getInner_hash()
//
//                                    //, b.getDividend()
//
////                                    ,
////                                    b.getIdentities().stream().map(i->i.toDUP()).collect(Collectors.toList()),
////                                    b.getJoiners(),
////                                    b.getActives(),
////                                    b.getLeavers(),
////                                    b.getRevoked(),
////                                    b.getExcluded(),
////                                    b.getCertifications(),
////                                    b.getTransactions()
//                            );
//                            return res;
//                        })
                        .collect(Collectors.toList())
                , Block.class);
        df.show();
        df.write().mode(SaveMode.Overwrite).parquet(dataPath + "blocks");
        df.printSchema();


        var df2 = spark.read().parquet(dataPath + "blocks");
        LOG.info("reread count  = " + df2.count());
        df2.show();
        df2.printSchema();
        Encoder<Block> encoder = Encoders.bean(Block.class);
        Dataset<Block> ds = df2.as(encoder);


        LOG.info("reopen count  = " + ds.count() + " ");
        ds.show();

        ds.collectAsList().forEach(b -> {
            LOG.info("that really works => " + b);

        });
    }

}
