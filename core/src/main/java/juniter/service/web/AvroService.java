package juniter.service.web;


import juniter.avro.*;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.CertRecord;
import juniter.service.core.BlockService;
import juniter.service.core.Index;
import juniter.service.core.WebOfTrust;
import org.apache.avro.Schema;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/avro")
public class AvroService {
    private static final Logger LOG = LogManager.getLogger(AvroService.class);

    @Value("${juniter.dataPath:${user.home}/.config/juniter/data/}")
    private String dataPath;
    private String AVRO_DATA;
    private File AVRO_SCHEMAS =   new File(getClass().getResource("src/main/avro/").getFile());
    private Path AVRO_FILES;


    @Autowired
    private WebOfTrust webOfTrust;

    @Autowired
    private Index index;

    @Autowired
    private BlockService blockService;

    @Autowired
    private ModelMapper modelMapper;

    private Schema SCHEMAB = null, SCHEMAC = null, SCHEMAI = null, SCHEMAM = null, SCHEMAS = null, SCHEMABLOCKS = null;


    private void dumpB() {
        var t = new Thread(() -> {
            try (Stream<ArchiveBINDEX> streamB = index.getBRepo().allAsc().stream().map(b -> modelMapper.map(b, ArchiveBINDEX.class))) {
                //dump(new File(AVRO_DATA, "bDump.avro"), SCHEMAB, streamB, BINDEX.class);
                var bout = new DataFileWriter<>(new GenericDatumWriter<ArchiveBINDEX>())
                        .setCodec(CodecFactory.deflateCodec(9))
                        .create(SCHEMAB, AVRO_FILES.resolve("bDump.avro").toFile());
                streamB.map(s -> modelMapper.map(s, ArchiveBINDEX.class))
                        .forEach(index -> {
                            try {
                                bout.append(index);
                            } catch (IOException e) {
                                LOG.error("error IO ", e);
                            }
                        });
                bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        t.start();

    }

    private void dumpI() {
        new Thread(() -> {
            try (Stream<ArchiveIINDEX> streamI = index.getIRepo().all().stream().map(s -> modelMapper.map(s, ArchiveIINDEX.class))) {
                //dump(new File(AVRO_DATA, "iDump.avro"), SCHEMAI, streamI, IINDEX.class);
                var bout = new DataFileWriter<>(new GenericDatumWriter<ArchiveIINDEX>())
                        .setCodec(CodecFactory.deflateCodec(9))
                        .create(SCHEMAI, new File(AVRO_DATA, "iDump.avro"));

                streamI.forEach(index -> {
                    try {
                        bout.append(index);
                    } catch (IOException e) {
                        LOG.error("error IO ", e);
                    }
                });
                bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Async
    @Transactional(readOnly = true)
    private void dumpM() {
        new Thread(() -> {
            try (Stream<ArchiveMINDEX> streamM = index.getMRepo().all().stream().map(s -> modelMapper.map(s, ArchiveMINDEX.class))) {
                //dump(new File(AVRO_DATA, "iDump.avro"), SCHEMAI, streamI, IINDEX.class);
                var bout = new DataFileWriter<>(new GenericDatumWriter<ArchiveMINDEX>())
                        .setCodec(CodecFactory.deflateCodec(9))
                        .create(SCHEMAM, new File(AVRO_DATA, "mDump.avro"));

                streamM.forEach(index -> {
                    try {
                        bout.append(index);
                    } catch (IOException e) {
                        LOG.error("error IO ", e);
                    }
                });
                bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }


    @Async
    @Transactional(readOnly = true)
    private void dumpC() {
        new Thread(() -> {
            try (Stream<ArchiveCINDEX> streamM = index.getCRepo().all().stream().map(s -> modelMapper.map(s, ArchiveCINDEX.class))) {

                var bout = new DataFileWriter<>(new GenericDatumWriter<ArchiveCINDEX>())
                        .setCodec(CodecFactory.deflateCodec(9))
                        .create(SCHEMAC, new File(AVRO_DATA, "cDump.avro"));

                streamM.forEach(index -> {
                    try {
                        bout.append(index);
                    } catch (IOException e) {
                        LOG.error("error IO ", e);
                    }
                });
                bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }


    @Async
    @Transactional(readOnly = true)
    private void dumpS() {
        try (Stream<juniter.core.model.dbo.index.SINDEX> streamM = index.getSRepo().all()) {

            var bout = new DataFileWriter<>(new GenericDatumWriter<ArchiveSINDEX>())
                    .setCodec(CodecFactory.deflateCodec(9))
                    .create(SCHEMAS, new File(AVRO_DATA, "sDump.avro"));

            streamM.map(s -> modelMapper.map(s, ArchiveSINDEX.class))
                    .forEach(index -> {
                        try {
                            bout.append(index);
                        } catch (IOException e) {
                            LOG.error("error IO ", e);
                        }
                    });
            bout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @CrossOrigin(origins = "*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/dumpIndex")
    public List<?> dumpIndex() {
        LOG.info("Entering dumpIndex");
        dumpB();
        dumpI();
        dumpM();
        //dumpS();
        dumpC();
        dumpS();

        return List.of(SCHEMAB, SCHEMAC, SCHEMAI, SCHEMAM, SCHEMAS);
    }

    @CrossOrigin(origins = "*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/dumpBlocks")
    public List<?> dumpBlocks() {
        LOG.info("Entering dumpBlocks");
        try (Stream<DBBlock> streamM = blockService.streamBlocksFromTo(1, 100)) {

            var bout = new DataFileWriter<>(new GenericDatumWriter<ArchiveBlock>())
                    .setCodec(CodecFactory.deflateCodec(9))
                    .create(SCHEMABLOCKS, new File(AVRO_DATA, "chainDump.avro"));

            streamM.map(s -> modelMapper.map(s, ArchiveBlock.class))
                    .forEach(index -> {
                        try {
                            bout.append(index);
                        } catch (IOException e) {
                            LOG.error("error IO ", e);
                        }
                    });
            bout.close();
        } catch (IOException e) {
            LOG.error("dumping blocks", e);
        }

        return List.of(SCHEMAB, SCHEMAC, SCHEMAI, SCHEMAM, SCHEMAS);
    }


    @PostConstruct
    public void buildSchemas() {

        AVRO_DATA = dataPath + "avro/";
       // AVRO_SCHEMAS = new File(AVRO_DATA, "schema/");
        AVRO_FILES = Paths.get(AVRO_DATA);
        try {
            SCHEMAB = new Schema.Parser().parse(new File(AVRO_SCHEMAS, "bindex.avsc"));
            SCHEMAC = new Schema.Parser().parse(new File(AVRO_SCHEMAS, "cindex.avsc"));
            SCHEMAI = new Schema.Parser().parse(new File(AVRO_SCHEMAS, "iindex.avsc"));
            SCHEMAM = new Schema.Parser().parse(new File(AVRO_SCHEMAS, "mindex.avsc"));
            SCHEMAS = new Schema.Parser().parse(new File(AVRO_SCHEMAS, "sindex.avsc"));
            SCHEMABLOCKS = new Schema.Parser().parse(new File(AVRO_SCHEMAS, "blocks.avsc"));
        } catch (IOException e) {
            LOG.error("postConstruct Avro ", e);
        }
    }

    @Transactional
    private void dump(File file, Schema schema, Stream<?> data, Class<?> clazz) throws IOException {
        LOG.info("Dumping " + file + " schema " + schema + " on clazz " + clazz);
        // =============== dumpping I
        GenericDatumWriter gdw = new GenericDatumWriter<>();
        switch (clazz.getSimpleName()) {
            case "BINDEX":
                var bout = new DataFileWriter<>(new GenericDatumWriter<ArchiveBINDEX>())
                        .setCodec(CodecFactory.deflateCodec(9))
                        .create(schema, file);
                data.map(s -> modelMapper.map(s, ArchiveBINDEX.class))
                        .forEach(index -> {
                            try {
                                bout.append(index);
                            } catch (IOException e) {
                                LOG.error("error IO ", e);
                            }
                        });
                bout.close();
                break;
            case "CINDEX":
                var cout = new DataFileWriter<>(new GenericDatumWriter<ArchiveCINDEX>())
                        .setCodec(CodecFactory.deflateCodec(9))
                        .create(schema, file);
                data.map(s -> modelMapper.map(s, ArchiveCINDEX.class))
                        .forEach(index -> {
                            try {
                                cout.append(index);
                            } catch (IOException e) {
                                LOG.error("error IO ", e);
                            }
                        });
                cout.close();
                break;
            case "IINDEX":
                var iout = new DataFileWriter<>(new GenericDatumWriter<ArchiveMINDEX>())
                        .setCodec(CodecFactory.deflateCodec(9))
                        .create(schema, file);
                data.map(s -> modelMapper.map(s, ArchiveMINDEX.class))
                        .forEach(index -> {
                            try {
                                iout.append(index);
                            } catch (IOException e) {
                                LOG.error("error IO ", e);
                            }
                        });
                iout.close();
                break;
            case "MINDEX":
                var mout = new DataFileWriter<>(new GenericDatumWriter<ArchiveMINDEX>())
                        .setCodec(CodecFactory.deflateCodec(9))
                        .create(schema, file);
                data.map(s -> modelMapper.map(s, ArchiveMINDEX.class))
                        .forEach(index -> {
                            try {
                                mout.append(index);
                            } catch (IOException e) {
                                LOG.error("error IO ", e);
                            }
                        });
                mout.close();
                break;
            case "SINDEX":
                var sout = new DataFileWriter<>(new GenericDatumWriter<ArchiveSINDEX>())
                        .setCodec(CodecFactory.deflateCodec(9))
                        .create(schema, file);
                data.map(s -> modelMapper.map(s, ArchiveSINDEX.class))
                        .forEach(index -> {
                            try {
                                sout.append(index);
                            } catch (IOException e) {
                                LOG.error("error IO ", e);
                            }
                        });
                sout.close();
                break;
        }

//        var out = new DataFileWriter<>(gdw)
//                .setCodec(CodecFactory.deflateCodec(9))
//                .create(schema, file);
//
//
//        data.map(s -> modelMapper.map(s, clazz))
//                .forEach(index -> {
//                    try {
//                        out.append(index);
//                    } catch (IOException e) {
//                        LOG.error("error IO ", e);
//                    }
//                });
//        out.close();

    }


    private void avroWrite(File file, Schema schema) throws IOException {
        var data = webOfTrust.certRecord("4weakHxDBMJG9NShULG1g786eeGh7wwntMeLZBDhJFni");
        LOG.info("found data " + data.size());

        var dataFileWriter = new DataFileWriter<>(new ReflectDatumWriter<CertRecord>())
                .setCodec(CodecFactory.deflateCodec(9))
                .create(schema, file);
        GenericRecord root = new GenericData.Record(schema);
        GenericRecordBuilder grb = new GenericRecordBuilder(schema);
        for (CertRecord x : data) {
            LOG.info("writing rec " + x);
            dataFileWriter.append(x);
        }
        dataFileWriter.close();

    }


    private List avroRead(File file, Schema schema) throws IOException {
        // Deserialize Users from disk
        var dataFileReader = new DataFileReader<>(file, new SpecificDatumReader<>(CertRecord.class));
        CertRecord cert = null;
        while (dataFileReader.hasNext()) {
// Reuse user object by passing it to next(). This saves us from
// allocating and garbage collecting many objects for files with
// many items.
            cert = dataFileReader.next(cert);
            LOG.info(cert);
        }
        return List.of();
    }


}
