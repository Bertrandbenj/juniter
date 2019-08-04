import juniter.core.model.ChainParameters;
import juniter.core.model.DBBlock;
import juniter.core.model.dbo.index.*;
import juniter.core.utils.TimeUtils;
import juniter.repository.jpa.index.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.sql.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.spark.sql.functions.*;


@ConditionalOnExpression("${juniter.useSpark:false}")
@Service
public class SparkService {

    private static final Logger LOG = LogManager.getLogger(SparkService.class);

    @Autowired
    SparkSession spark;

    @Autowired
    BlockService blockService;

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

    @Value("${juniter.dataPath:${user.home}/.config/juniter/data/}")
    private String dataPath;

    public static Dataset<Row> iindex;
    public static Dataset<Row> mindex;
    public static Dataset<Row> cindex;
    public static Dataset<Row> sindex;
    public static Dataset<Row> tree;
    public static Dataset<Row> certs;
    public static Dataset<Row> idty;
    public static Dataset<Row> actives;
    public static Dataset<Row> leavers;
    public static Dataset<Row> revoked;
    public static Dataset<Row> transactions;
    public static Dataset<Row> excluded;
    public static Dataset<Row> joiners;
    public static Dataset<Row> unlocks;
    public static Dataset<Row> inputs;
    public static Dataset<Row> outputs;


    ChainParameters conf = new ChainParameters();


    @Async("AsyncJuniterPool")
    public void duniterToParquet() {
        var tables = Stream.of("b_index", "node", "c_index", "cert", "i_index", "idty", "m_index", "membership", "meta", "peer", "s_index", "txs", "wallet");

        tables.forEach(t -> {
            spark.read().format("jdbc")
                    .option("url", "jdbc:sqlite:/home/ben/.config/duniter/duniter_default/duniter.db")
                    .option("driver", "org.sqlite.JDBC")
                    .option("dbtable", t)
                    .load()
                    .write()
                    .mode(SaveMode.Overwrite)
                    .parquet(dataPath + "duniter/" + t);
        });

    }


    public void compare() {
        var mindex = spark.read().parquet(dataPath + "blockchain/mindex");

        var m_index = spark.read().parquet(dataPath + "duniter/m_index");


        if (mindex.schema() != m_index.schema()) {
            var x = List.of(mindex.columns());
            x.removeAll(List.of(m_index.columns()));
            var y = List.of(m_index.columns());
            y.removeAll(List.of(mindex.columns()));

            LOG.info("Different Schema +" + x + "  - " + y);
            mindex.printSchema();
            m_index.printSchema();
            return;
        }


    }

    @Async("AsyncJuniterPool")
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

    public Dataset<Row> blockchain;

    @Async("AsyncJuniterPool")
    public void jsonToParquet() {
        long start = System.nanoTime();

        spark.read().json(dataPath + "dump/*.jsonrows")
                .withColumn("x", when(col("number").equalTo(0), "E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855").otherwise(col("hash")))
                .drop("hash").withColumnRenamed("x", "hash")
                .withColumn("written", concat(col("number"), lit("-"), col("hash")))

                .repartition(4)
                .orderBy("written")
                .write()
                .mode(SaveMode.Overwrite)
                .parquet(dataPath + "blockchain/tree");

        final var elapsed = Long.divideUnsigned(System.nanoTime() - start, 1000000);
        LOG.info("jsonToParquet  Elapsed time: " + TimeUtils.format(elapsed));
    }

    @Async("AsyncJuniterPool")
    public void parseBlockchain() {
        long start = System.nanoTime();

        //jsonToParquet();

        if (blockchain == null)
            blockchain = spark.read().parquet(dataPath + "blockchain/tree")
                    //.persist(StorageLevel.DISK_ONLY())
                    //.cache()
                    ;

        LOG.info("parsed Blockchain :" + blockchain.count());
        blockchain.printSchema();

        //blockchain.show();
        tree = blockchain.select("written", "medianTime").cache();


        idty = blockchain.select(col("identities"),
                col("written"),
                col("number").as("writtenOn"),
                col("medianTime").as("writtenTime"))
                .withColumn("x", explode(col("identities"))).drop("identities")
                .withColumn("pubkey", split(col("x"), "\\:").getItem(0))
                .withColumn("signature", split(col("x"), "\\:").getItem(1))
                .withColumn("signedHash", split(col("x"), "\\:").getItem(2))
                .withColumn("userid", split(col("x"), "\\:").getItem(3))
                .join(tree.select(
                        col("written").as("signedHash"),
                        col("medianTime").plus(conf.getMsValidity()).as("expires_on"),
                        col("medianTime").plus(conf.getMsValidity() * 2).as("revokes_on"))
                        , "signedHash")
                .withColumn("chainable_on", col("writtenTime").plus(conf.msPeriod))
                .drop("x")
                .cache();

        certs = blockchain.select(col("certifications"),
                col("written"),
                col("number").as("writtenOn"),
                col("medianTime").as("writtenTime"))
                .withColumn("x", explode(col("certifications"))).drop("certifications")
                .withColumn("issuer", split(col("x"), "\\:").getItem(0))
                .withColumn("receiver", split(col("x"), "\\:").getItem(1))
                .withColumn("signedHash", split(col("x"), "\\:").getItem(2))
                .withColumn("signature", split(col("x"), "\\:").getItem(3))
                .join(tree.select(
                        split(col("written"), "-").getItem(0).as("signedHash"),
                        col("medianTime").plus(conf.getSigValidity()).as("expires_on")),
                        "signedHash")

                .withColumn("chainable_on", col("writtenTime").plus(conf.getSigPeriod()))
                .drop("x")
                .coalesce(2)
                .cache();


        actives = blockchain.select(col("actives"),
                col("written"),
                col("number").as("writtenOn"),
                col("medianTime").as("writtenTime"))
                .withColumn("x", explode(col("actives"))).drop("actives")
                .withColumn("pubkey", split(col("x"), "\\:").getItem(0))
                .withColumn("signature", split(col("x"), "\\:").getItem(1))
                .withColumn("signedHash", split(col("x"), "\\:").getItem(2))
                .withColumn("idtyOn", split(col("x"), "\\:").getItem(3))
                .withColumn("userid", split(col("x"), "\\:").getItem(4))
                .join(tree.select(
                        col("written").as("signedHash"),
                        col("medianTime").plus(conf.getMsValidity()).as("expires_on"),
                        col("medianTime").plus(conf.getMsValidity() * 2).as("revokes_on"))
                        , "signedHash")
                .withColumn("chainable_on", col("writtenTime").plus(conf.msPeriod))
                .drop("x")
                .cache();


        joiners = blockchain.select(col("joiners"),
                col("written"),
                col("number").as("writtenOn"),
                col("medianTime").as("writtenTime"))
                .withColumn("x", explode(col("joiners"))).drop("joiners")
                .withColumn("pubkey", split(col("x"), "\\:").getItem(0))
                .withColumn("signature", split(col("x"), "\\:").getItem(1))
                .withColumn("signedHash", split(col("x"), "\\:").getItem(2))
                .withColumn("idtyOn", split(col("x"), "\\:").getItem(3))
                .withColumn("userid", split(col("x"), "\\:").getItem(4))
                .join(tree.select(
                        col("written").as("signedHash"),
                        col("medianTime").plus(conf.getMsValidity()).as("expires_on"),
                        col("medianTime").plus(conf.getMsValidity() * 2).as("revokes_on"))
                        , "signedHash")
                .withColumn("chainable_on", col("writtenTime").plus(conf.msPeriod))
                .drop("x")
                .cache();

        leavers = blockchain.select(col("leavers"),
                col("written"),
                col("number").as("writtenOn"),
                col("medianTime").as("writtenTime"))
                .withColumn("x", explode(col("leavers"))).drop("leavers")
                .withColumn("pubkey", split(col("x"), "\\:").getItem(0))
                .withColumn("signature", split(col("x"), "\\:").getItem(1))
                .withColumn("signedHash", split(col("x"), "\\:").getItem(2))
                .withColumn("idtyOn", split(col("x"), "\\:").getItem(3))
                .withColumn("userid", split(col("x"), "\\:").getItem(4))
                .drop("x")
                .cache();

        excluded = blockchain.select(col("excluded"),
                col("written"),
                col("number").as("writtenOn"),
                col("medianTime").as("writtenTime"))
                .withColumn("x", explode(col("excluded"))).drop("excluded")
                .withColumn("issuer", split(col("x"), "\\:").getItem(0))
                .drop("x")
                .cache();

        revoked = blockchain.select(col("revoked"),
                col("written"),
                col("number").as("writtenOn"),
                col("medianTime").as("writtenTime"))
                .withColumn("x", explode(col("revoked"))).drop("revoked")
                .withColumn("revoked", split(col("x"), "\\:").getItem(0))
                .withColumn("signature", split(col("x"), "\\:").getItem(1))
                .drop("x")
                .cache();


        saveAsync(certs, "certs");
        saveAsync(idty, "idty");
        saveAsync(revoked, "revocs");
        saveAsync(actives, "actives");
        saveAsync(excluded, "excluded");
        saveAsync(leavers, "leavers");
        saveAsync(joiners, "joiners");


        // specs says :  "Each join whose PUBLIC_KEY does not match a local MINDEX CREATE, PUBLIC_KEY produces 2 new entries"
        var actualJoiners = joiners
                .join(idty.select(
                        idty.col("written").as("awritten_on"),
                        idty.col("pubkey").as("apubkey")),
                        col("written").equalTo(col("awritten_on")).and(col("pubkey").equalTo(col("apubkey"))),
                        "leftanti")
                .drop("awritten_on", "apubkey");

        mindex = idty
                .withColumn("op", lit("CREATE"))
                .withColumn("idtyOn", col("written"))
                .select("signedHash", "written", "writtenOn", "writtenTime", "pubkey", "signature", "expires_on", "revokes_on", "chainable_on", "op", "idtyOn")
                .union(actualJoiners
                        .withColumn("op", lit("UPDATE"))
                        .select("signedHash", "written", "writtenOn", "writtenTime", "pubkey", "signature", "expires_on", "revokes_on", "chainable_on", "op", "idtyOn")
                )
                //.join(revoked.select(revoked.col("written").as("revoked"), revoked.col("revoked"), revoked.col("revoked")))

                .withColumn("type", lit("JOIN"))
                .withColumn("leaving", lit(false))
                .union(actives
                        .withColumn("op", lit("UPDATE"))
                        .select("signedHash", "written", "writtenOn", "writtenTime", "pubkey", "signature", "expires_on", "revokes_on", "chainable_on", "op", "idtyOn")
                        .withColumn("type", lit("RENEW"))
                        .withColumn("leaving", lit(false))
                )
                .union(leavers
                        .withColumn("op", lit("UPDATE"))
                        .withColumn("revokes_on", lit(null))
                        .withColumn("expires_on", lit(null))
                        .withColumn("chainable_on", lit(null))
                        .select("signedHash", "written", "writtenOn", "writtenTime", "pubkey", "signature", "expires_on", "revokes_on", "chainable_on", "op", "idtyOn")
                        .withColumn("type", lit("LEAVE"))
                        .withColumn("leaving", lit(true))
                )
                .union(revoked
                        .withColumn("op", lit("UPDATE"))
                        .withColumn("revokes_on", lit(null))
                        .withColumn("expires_on", lit(null))
                        .withColumn("chainable_on", lit(null))
                        .withColumn("idtyOn", lit(null))
                        .withColumn("signedHash", col("written"))
                        .withColumn("pubkey", col("revoked"))
                        .select("signedHash", "written", "writtenOn", "writtenTime", "pubkey", "signature", "expires_on", "revokes_on", "chainable_on", "op", "idtyOn")
                        .withColumn("type", lit("REV"))
                        .withColumn("leaving", lit(true))
                ).cache();


        saveAsync(mindex, "mindex");

        cindex = certs.select(
                col("written"),
                col("writtenOn"),
                col("signedHash"),
                col("issuer"),
                col("receiver"),
                col("signature"),
                col("expires_on"),
                col("chainable_on"),
                lit("CREATE").as("op")
        ).cache();

        saveAsync(cindex, "cindex");

        iindex = idty
                .select(lit("CREATE").as("op"),
                        col("userid"),
                        col("pubkey"),
                        col("signedHash"),
                        col("written"),
                        lit(true).as("member"),
                        lit(true).as("wasMember"),
                        lit(false).as("kick"),
                        col("signature"))
                .union(joiners.select(
                        lit("UPDATE").as("op"),
                        col("userid"),
                        col("pubkey"),
                        col("signedHash"),
                        col("written"),
                        lit(true).as("member"),
                        lit(true).as("wasMember"),
                        lit(false).as("kick"),
                        col("signature"))
                )
                .union(excluded.select(
                        lit("UPDATE").as("op"),
                        lit(null).as("userid"),
                        col("issuer").as("pubkey"),
                        col("written").as("signedHash"),
                        col("written"),
                        lit(false).as("member"),
                        lit(true).as("wasMember"),
                        lit(false).as("kick"),
                        lit(null).as("signature"))
                );

        saveAsync(iindex, "iindex");


        transactions = blockchain.select(explode(col("transactions")).as("tx"),
                concat(col("number"), lit("-"), col("hash")).as("written"),
                col("medianTime").as("writtenTime"))
                .select(col("writtenTime"),
                        col("written"),
                        col("tx.signed").as("txStamp"),
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
        saveAsync(transactions, "transactions");


        inputs = transactions
                .withColumn("in", explode(col("inputs"))).drop("inputs")
                .withColumn("amount", split(col("in"), "\\:").getItem(0))
                .withColumn("base", split(col("in"), "\\:").getItem(1))
                .withColumn("type", split(col("in"), "\\:").getItem(2))
                .withColumn("identifier", split(col("in"), "\\:").getItem(3))
                .withColumn("index", split(col("in"), "\\:").getItem(4))
                .drop("in", "version", "currency", "comment", "inputs", "outputs", "signatures", "unlocks", "locktime", "hash", "blockstampTime")
                .cache();

        outputs = transactions
                .withColumn("out", explode(col("outputs"))).drop("outputs")
                .withColumn("amount", split(col("out"), "\\:").getItem(0))
                .withColumn("base", split(col("out"), "\\:").getItem(1))
                .withColumn("condition", split(col("out"), "\\:").getItem(2))
                .drop("out", "version", "currency", "comment", "inputs", "outputs", "signatures", "unlocks", "locktime", "hash", "blockstampTime")
                .cache();

        unlocks = transactions
                .withColumn("un", explode(col("unlocks"))).drop("unlocks")
                .withColumn("inRef", split(col("un"), "\\:").getItem(0))
                .withColumn("function", split(col("un"), "\\:").getItem(1))
                .drop("un", "version", "currency", "comment", "inputs", "outputs", "signatures", "unlocks", "locktime", "hash", "blockstampTime")
                .cache();


        saveAsync(inputs, "inputs");
        saveAsync(unlocks, "unlocks");
        saveAsync(outputs, "outputs");

        //blockchain.where("dividend != null").select("number","hash","dividend").show

        // Scala join certs table wit
        //certs
        // .join(idty.select($"userid".as("issuerUID"), $"pubkey".as("issuer")), "issuer")
        // .join(idty.select($"userid".as("receiverUID"), $"pubkey".as("receiver")), "receiver")
        // .where("issuerUID LIKE '%bul%'").count

        final var elapsed = Long.divideUnsigned(System.nanoTime() - start, 1000000);
        LOG.info("Spark parsing blockchain :" +
                " Elapsed time: " + TimeUtils.format(elapsed));

    }

    public Dataset<Row> membersAsOf;

    public void computeAsOf() {
        membersAsOf = tree.select(col("medianTime").as("asOf"))
                .join(mindex, col("asOf").between(0, mindex.col("writtenTime")), "left_outer")
                .select(col("asOf"),col("writtenTime").as("mtime"), col("pubkey"))
        //.drop("leaving", "type", "op", "chainable_on","revokes_on", "signature", "writtenOn", "written", "signed", "idtyOn")
        ;
//        spark.sparkContext()
        saveAsync(membersAsOf, "membersAsOf");
    }

    @Async("AsyncJuniterPool")
    private void saveAsync(Dataset<Row> ds, String name) {
        // new Thread(()->{

        LOG.info("saving Async " + name);
        ds.coalesce(1).write().mode(SaveMode.Overwrite).parquet(dataPath + "blockchain/" + name);
        LOG.info("saved Async " + name);

        // }).start();

    }

    private void stuff() {
        var df = spark.createDataFrame(
                blockService.blocksFromTo(0, 52)
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
                , DBBlock.class);
        df.show();
        df.write().mode(SaveMode.Overwrite).parquet(dataPath + "blocks");
        df.printSchema();


        var df2 = spark.read().parquet(dataPath + "blocks");
        LOG.info("reread count  = " + df2.count());
        df2.show();
        df2.printSchema();
        Encoder<DBBlock> encoder = Encoders.bean(DBBlock.class);
        Dataset<DBBlock> ds = df2.as(encoder);


        LOG.info("reopen count  = " + ds.count() + " ");
        ds.show();

        ds.collectAsList().forEach(b -> {
            LOG.info("that really works => " + b);

        });
    }

}
