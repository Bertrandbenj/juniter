package juniter.service.bma;

import com.fasterxml.jackson.databind.ObjectMapper;
import juniter.core.model.wot.Certification;
import juniter.repository.jpa.BlockRepository;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.ManyToOne;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@Controller
@RequestMapping(value = "/jena")
public class JenaController {
    private static final Logger LOG = LogManager.getLogger();

    public static List<Class> ACCEPTED_LIST_CLASS = Arrays.asList(List.class, ArrayList.class);


    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BlockRepository blockRepo;

    @Autowired
    NetworkService peerRepo;


    public static Resource toModel(Model model, Object clazz) {


        var namespace = "http://duniter.org/"  + clazz.getClass().getCanonicalName().replaceAll("\\.","/");

        var x = clazz.getClass().getName() + "@" + Integer.toHexString(clazz.hashCode());

        var resource = model.createResource(new AnonId( "TOMODEL_" + new Random().nextInt(1000000)));
       // model.add (resource, RDF.type, FOAF.Person);


        for (Field field : clazz.getClass().getDeclaredFields()) {

            var type = field.getGenericType();
            var annots = field.getAnnotations();

            // handle public fields
            try {
                if (field.canAccess(clazz) && field.getModifiers() == Field.PUBLIC ) {
                    var pred = model.createProperty(namespace, "#" + field.getName());


                    //resource.addProperty ( pred, "" + field.get(clazz));
                    //res = handleList(res, type);
                    //LOG.info("adding field : " + pred);
                }
            } catch (Exception e) {
                //LOG.error(e.getMessage());
            }


            // handle annotation

            for (Annotation a : annots) {
                if (a instanceof ManyToOne) {
                    var fk = ((ManyToOne) a).targetEntity();
                } else {
                    //LOG.debug("  -  " + a.annotationType() + a.toString());
                }

            }

        }

        // Handle Method
        for (Method met : clazz.getClass().getMethods()) {
            if (met.getName().startsWith("get") && !"getBytes".equals(met.getName())&& met.getParameterCount()==0) {
                try {
                    var invoked = met.invoke(clazz);
                    var pred = model.createProperty(namespace, "#" + met.getName().replace("get", ""));

                    LOG.info("found getter : " + pred + " - " );

                    if("getClass".equals(met.getName())){
                        resource.addProperty(RDF.type, pred);
                    }else if (met.getGenericReturnType() instanceof ParameterizedType ) {

                        if(invoked!=null){
                            var anonId = model.createResource(new AnonId("PARAMETRIZED_" + new Random().nextInt(1000000)));
                            //resource.addProperty( pred, anonId);

                            var listNode = handleList(model, met.getGenericReturnType(), invoked, pred, anonId );
                            if(listNode.isPresent()){
                                LOG.info(" --and res  : " + listNode.get().getURI() );
                                resource.addProperty(pred, listNode.get());
                            }


                        }
                        //
                    }else{
                        if(invoked!=null)
                            resource.addProperty( pred, invoked +"");
                    }

                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }

        return resource;
    }


    static public Optional<Resource> handleList(Model model, Type type, Object  listObject, Property prop, Resource fieldId ){
        // handle ParameterizedType
        if (type instanceof ParameterizedType) {
            var parameterized = (ParameterizedType) type;// This would be Class<List>, say
            Type raw = parameterized.getRawType();
            Type own = parameterized.getOwnerType();
            Type[] typeArgs = parameterized.getActualTypeArguments();

            LOG.info("  - ParameterizedType  " +
                    raw.getTypeName() + " <" + typeArgs[0].getTypeName() + ">" +
                   // " - owner " +  own  +
                    " - going to cast  " + listObject);//+ Arrays.toString(typeArgs));

            if (ACCEPTED_LIST_CLASS.stream().anyMatch(x ->   x.getCanonicalName().equals(raw.getTypeName()) )) {

// Create a list containing the subjects of the role assignments in one go

                List<RDFNode> nodes = new ArrayList<>();
                var asList = castListSafe((List<?>)listObject, Certification.class);

                if(asList.isEmpty()){
                    LOG.warn(" - empty list, ignoring " );
                    return Optional.empty();
                }
                for (Object x : asList) {
                    var listItem = toModel(model, x);
                    nodes.add(listItem);

                }

                RDFList list = model.createList(nodes.toArray(new RDFNode[nodes.size()]));

                LOG.info("  - rdflist " + list.size() + " : " + list);
//var tmp = model.createProperty("sdfsdfsdf"+new Random().nextInt(10000000));
                fieldId.addProperty(prop,list);
  //              fieldId.addProperty(tmp ,model.createList(list));
                return Optional.of(list);

            }

        }
        return Optional.empty();
    }

    /**
     * Performs a forced cast.
     * Returns null if the collection type does not match the items in the list.
     *
     * @param data     The list to cast.
     * @param listType The type of list to cast to.
     */
    static <T> List<? super T> castListSafe(List<?> data, Class<T> listType) {
        List<T> retval = null;
        //This test could be skipped if you trust the callers, but it wouldn't be safe then.
        if (data != null && !data.isEmpty() && listType.isInstance(data.iterator().next().getClass())) {
            LOG.info("  - castListSafe passed check ");

            @SuppressWarnings("unchecked")//It's OK, we know List<T> contains the expected type.
                    List<T> foo = (List<T>) data;
            return foo;
        }

        LOG.info("  - castListSafe failed check  forcing it though");
        return (List<T>) data;
    }

    Model loadModel() {
        // load some data into the model
        Model model = ModelFactory.createDefaultModel();
        blockRepo.block(198536).ifPresent(b -> {
            toModel(model, b);
        });


        toModel(model, peerRepo.endPointPeer(blockRepo.currentBlockNumber()));

        return model;
    }

    // XML is default
    public static final Map<String, String> FORMATS = Map.of(
            "json", "JSON",
            "ttl", "TURTLE",
            "ntriple", "N-TRIPLE",
            "n3", "N3"

    );

    @GetMapping(value = "/block/{format}")
    public @ResponseBody
    String block(@PathVariable("format") String format) {

        Model model = ModelFactory.createDefaultModel();
        var b = blockRepo.block(42);

        if (b.isPresent()) {
            toModel(model, b.get());


            // New method to serialize to n-triple
            try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                model.write(os, FORMATS.get(format));
                return os.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return "";
    }

    @GetMapping(value = "/json", produces = {"application/x-javascript", "application/json", "application/ld+json"})
    public @ResponseBody
    String getModelAsJson() {
        LOG.info(" getModelAsJson ");
        var model = loadModel();

        // New method to serialize to n-triple
        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            model.write(os, "JSON");
            return os.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @GetMapping(value = "/xml", produces = {"application/xml", "application/rdf+xml"})
    @ResponseBody
    public String getModelAsXml() {
        LOG.info(" getModelAsXml ");
        var model = loadModel();
        // Note that we added "application/rdf+xml" as one of the supported types
        // for this method. Otherwise, we utilize your existing xml serialization

        // New method to serialize to n-triple
        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            model.write(os);
            return os.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @GetMapping(value = "/ntriple", produces = {"application/n-triples", "text/n-triples"})
    @ResponseBody
    public String getModelAsNTriples() {
        LOG.info(" getModelAsNTriples ");
        var model = loadModel();

        // New method to serialize to n-triple
        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            model.write(os, "N-TRIPLE");
            return os.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }

    @GetMapping(value = "/ttl", produces = {"text/turtle"})
    @ResponseBody
    public String getModelAsTurtle() {
        var model = loadModel();

        LOG.info(" getModelAsTurtle " + model.size());
        // New method to serialize to turtle
        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            model.write(os, "TURTLE");
            return os.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @GetMapping(value = "/n3", produces = {"text/n3"})
    @ResponseBody
    public String getModelAsN3() {
        var model = loadModel();

        LOG.info(" getModelAsN3 ");
        // New method to serialize to N3
        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            model.write(os, "N3");
            return os.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }
}