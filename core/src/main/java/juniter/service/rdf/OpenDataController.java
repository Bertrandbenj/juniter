package juniter.service.rdf;
//import de.uni_stuttgart.vis.vowl.owl2vowl.Owl2Vowl;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

@Transactional
public abstract class OpenDataController implements Synchro {

    private static final Logger LOG = LogManager.getLogger(OpenDataController.class);

    public void initModule() {
        initConfig();
        new Reflections(BASE_SYNCHRONIZE_MODEL_PACKAGE, new SubTypesScanner(false))
                .getSubTypesOf(Object.class)
                .forEach(c -> URI_2_CLASS.putIfAbsent(c.getSimpleName(), c));
    }

    /*
     * ************* Service methods *************
     */
    @GetMapping(value = "/ntriple/{q}/{name}", produces = {"application/n-triples", "text/n-triples"})
    @ResponseBody
    public String getModelAsNTriples(@PathVariable String q, @PathVariable String name,
                                     @RequestParam(defaultValue = "false") String disjoints,
                                     @RequestParam(defaultValue = "false") String methods,
                                     @RequestParam(defaultValue = "false") String packages) {
        return doWrite(execute(q, name, disjoints, methods, packages), "N-TRIPLE");
    }

    @GetMapping(value = "/ttl/{q}/{name}", produces = {"text/turtle"})
    @ResponseBody
    public String getModelAsTurtle(@PathVariable String q, @PathVariable String name,
                                   @RequestParam(defaultValue = "false") String disjoints,
                                   @RequestParam(defaultValue = "false") String methods,
                                   @RequestParam(defaultValue = "false") String packages) {
        return doWrite(execute(q, name, disjoints, methods, packages), "TURTLE");
    }

    @GetMapping(value = "/n3/{q}/{name}", produces = {"text/n3", "application/text", "text/text"})
    @ResponseBody
    public String getModelAsN3(@PathVariable String q, @PathVariable String name,
                               @RequestParam(defaultValue = "false") String disjoints,
                               @RequestParam(defaultValue = "false") String methods,
                               @RequestParam(defaultValue = "false") String packages) {
        return doWrite(execute(q, name, disjoints, methods, packages), "N3");
    }

    @GetMapping(value = "/json/{q}/{name}", produces = {"application/x-javascript", "application/json", "application/ld+json"})
    @ResponseBody
    public String getModelAsrdfjson(@PathVariable String q, @PathVariable String name,
                                    @RequestParam(defaultValue = "false") String disjoints,
                                    @RequestParam(defaultValue = "false") String methods,
                                    @RequestParam(defaultValue = "false") String packages) {
        return doWrite(execute(q, name, disjoints, methods, packages), "RDF/JSON");
    }

    @GetMapping(value = "/trig/{q}/{name}", produces = {"text/trig"})
    @ResponseBody
    public String getModelAsTrig(@PathVariable String q, @PathVariable String name,
                                 @RequestParam(defaultValue = "false") String disjoints,
                                 @RequestParam(defaultValue = "false") String methods,
                                 @RequestParam(defaultValue = "false") String packages) {
        return doWrite(execute(q, name, disjoints, methods, packages), "TriG");
    }

    @GetMapping(value = "/jsonld/{q}/{name}", produces = {"application/x-javascript", "application/json", "application/ld+json"})
    @ResponseBody
    public String getModelAsJson(@PathVariable String q, @PathVariable String name,
                                 @RequestParam(defaultValue = "false") String disjoints,
                                 @RequestParam(defaultValue = "false") String methods,
                                 @RequestParam(defaultValue = "false") String packages) {
        return doWrite(execute(q, name, disjoints, methods, packages), "JSON-LD");
    }

    @GetMapping(value = "/rdf/{q}/{name}", produces = {"application/xml", "application/rdf+xml"})
    @ResponseBody
    public String getModelAsXml(@PathVariable String q, @PathVariable String name,
                                @RequestParam(defaultValue = "false") String disjoints,
                                @RequestParam(defaultValue = "false") String methods,
                                @RequestParam(defaultValue = "false") String packages) {
        return doWrite(execute(q, name, disjoints, methods, packages), "RDF/XML");
    }

    @GetMapping(value = "/trix/{q}/{name}", produces = {"text/trix"})
    @ResponseBody
    public String getModelAsTrix(@PathVariable String q, @PathVariable String name,
                                 @RequestParam(defaultValue = "false") String disjoints,
                                 @RequestParam(defaultValue = "false") String methods,
                                 @RequestParam(defaultValue = "false") String packages) {
        //fillObjectWithStdAttribute(null,null,null);
        return doWrite(execute(q, name, disjoints, methods, packages), "TriX");
    }


    // ===========  protected methods =============
    @Transactional
    protected Model execute(String q, String name, String disjoints, String methods, String packages) {
        LOG.info("executing /jena/{fmt}/{" + q + "}/{" + name + "}?disjoints=" + disjoints + "&methods=" + methods + "&packages=" + packages);
        Map<String, String> opts = new HashMap<>();
        opts.put("disjoints", disjoints);
        opts.put("methods", methods);
        opts.put("packages", packages);

        OntModel res = null;

        switch (q) {
            case "query":
                String query = NAMED_QUERIES.getOrDefault(name, "from DBBlock");
                res = ontOfData(MY_PREFIX + name,
                        getEntityManager().createQuery(query)
                                .setMaxResults(200)
                                .getResultList(),
                        opts);
                break;
            case "sync":
                res = overwriteFromRemote(
                        "http://localhost:8081/jena/rdf/referentials/" + name,
                        MY_PREFIX + "sync");
                break;
            case "ontologies":
            case "ontology":
                res = onto(name, opts);
        }

        if (res == null)
            // report error to user
            return null;

        try {
            //save ontology as file
            File owl = File.createTempFile(name, "owl");
            res.write(new FileOutputStream(owl), "N3");

            //convert to webVowl
//            new Owl2Vowl(new FileInputStream(owl))
//                    .writeToFile(new File("/home/bbertran/ws/WebVOWL/deploy/data/" + name + ".json"));

        } catch (Exception e) {
            LOG.warn("error saving OWL and VOWL", e);
        }

        return res;
    }

    protected abstract OntModel onto(String name, Map<String, String> opts);


}