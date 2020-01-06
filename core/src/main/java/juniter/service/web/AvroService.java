package juniter.service.web;

import juniter.avro.*;
import juniter.core.model.dbo.index.CertRecord;
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
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/avro")
public class AvroService {
    private static final Logger LOG = LogManager.getLogger(AvroService.class);

    @Value("${juniter.dataPath:${user.home}/.config/juniter/data/}")
    private String dataPath;
    private String AVRO_DATA;
    private File AVRO_SCHEMA;

    @Autowired
    private WebOfTrust webOfTrust;

    @Autowired
    private Index index;

    @Autowired
    private ModelMapper modelMapper;

    private Schema SCHEMAB = null, SCHEMAC = null, SCHEMAI = null, SCHEMAM = null, SCHEMAS = null;


    private void dumpB() {
        var t = new Thread(() -> {
            try (Stream<BINDEX> streamB = index.getBRepo().allAsc().stream().map(b -> modelMapper.map(b, BINDEX.class))) {
                //dump(new File(AVRO_DATA, "bDump.avro"), SCHEMAB, streamB, BINDEX.class);
                var bout = new DataFileWriter<>(new GenericDatumWriter<BINDEX>())
                        .setCodec(CodecFactory.deflateCodec(9))
                        .create(SCHEMAB, new File(AVRO_DATA, "bDump.avro"));
                streamB.map(s -> modelMapper.map(s, BINDEX.class))
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
            try (Stream<IINDEX> streamI = index.getIRepo().all().stream().map(s -> modelMapper.map(s, IINDEX.class))) {
                //dump(new File(AVRO_DATA, "iDump.avro"), SCHEMAI, streamI, IINDEX.class);
                var bout = new DataFileWriter<>(new GenericDatumWriter<IINDEX>())
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
            try (Stream<MINDEX> streamM = index.getMRepo().all().stream().map(s -> modelMapper.map(s, MINDEX.class))) {
                //dump(new File(AVRO_DATA, "iDump.avro"), SCHEMAI, streamI, IINDEX.class);
                var bout = new DataFileWriter<>(new GenericDatumWriter<MINDEX>())
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
            try (Stream<CINDEX> streamM = index.getCRepo().all().stream().map(s -> modelMapper.map(s, CINDEX.class))) {
                //dump(new File(AVRO_DATA, "iDump.avro"), SCHEMAI, streamI, IINDEX.class);
                var bout = new DataFileWriter<>(new GenericDatumWriter<CINDEX>())
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
                //dump(new File(AVRO_DATA, "iDump.avro"), SCHEMAI, streamI, IINDEX.class);
                var bout = new DataFileWriter<>(new GenericDatumWriter<SINDEX>())
                        .setCodec(CodecFactory.deflateCodec(9))
                        .create(SCHEMAS, new File(AVRO_DATA, "sDump.avro"));

                streamM.map(s -> modelMapper.map(s, SINDEX.class))
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
    public List<?> dumpIndex(Object object) {
        LOG.info("Entering dumpIndex");
        dumpB();
        dumpI();
        dumpM();
        //dumpS();
        dumpC();
        dumpS();

        return List.of(SCHEMAB, SCHEMAC, SCHEMAI, SCHEMAM, SCHEMAS);
    }


    @PostConstruct
    public void buildSchemas() {

        AVRO_DATA = dataPath + "avro/";
        AVRO_SCHEMA = new File(AVRO_DATA, "schema/");
        try {
            SCHEMAB = new Schema.Parser().parse(new File(AVRO_SCHEMA, "bindex.avsc"));
            SCHEMAC = new Schema.Parser().parse(new File(AVRO_SCHEMA, "cindex.avsc"));
            SCHEMAI = new Schema.Parser().parse(new File(AVRO_SCHEMA, "iindex.avsc"));
            SCHEMAM = new Schema.Parser().parse(new File(AVRO_SCHEMA, "mindex.avsc"));
            SCHEMAS = new Schema.Parser().parse(new File(AVRO_SCHEMA, "sindex.avsc"));
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
                var bout = new DataFileWriter<>(new GenericDatumWriter<BINDEX>())
                        .setCodec(CodecFactory.deflateCodec(9))
                        .create(schema, file);
                data.map(s -> modelMapper.map(s, BINDEX.class))
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
                var cout = new DataFileWriter<>(new GenericDatumWriter<CINDEX>())
                        .setCodec(CodecFactory.deflateCodec(9))
                        .create(schema, file);
                data.map(s -> modelMapper.map(s, CINDEX.class))
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
                var iout = new DataFileWriter<>(new GenericDatumWriter<MINDEX>())
                        .setCodec(CodecFactory.deflateCodec(9))
                        .create(schema, file);
                data.map(s -> modelMapper.map(s, MINDEX.class))
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
                var mout = new DataFileWriter<>(new GenericDatumWriter<MINDEX>())
                        .setCodec(CodecFactory.deflateCodec(9))
                        .create(schema, file);
                data.map(s -> modelMapper.map(s, MINDEX.class))
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
                var sout = new DataFileWriter<>(new GenericDatumWriter<SINDEX>())
                        .setCodec(CodecFactory.deflateCodec(9))
                        .create(schema, file);
                data.map(s -> modelMapper.map(s, SINDEX.class))
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
