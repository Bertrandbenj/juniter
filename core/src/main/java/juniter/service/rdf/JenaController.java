package juniter.service.rdf;

import de.uni_stuttgart.vis.vowl.owl2vowl.Owl2Vowl;
import net.sumaris.server.service.technical.rdf.Synchro;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import static net.sumaris.server.service.technical.rdf.Helpers.doWrite;

@RestController
@RequestMapping(value = "/jena")
public class JenaController {
    private static final Logger LOG = LogManager.getLogger();


    private final static String IFREMER_URL = "http://ifremer.com/";

    @Autowired
    private EntityManager entityManager;

    private Synchro sync = new Synchro();


    private OntModel ontologyOfVOPackages;


    private OntModel ontologyOfCurrent;

    @PostConstruct
    public void buildOntologies() {

        Map<String, String> opts = new HashMap<>();
        opts.put("disjoints", "false");
        opts.put("methods", "false");


        ontologyOfVOPackages = sync.ontOfPackage(sync.MY_PREFIX + "modele",
                "juniter.core.model",
                opts);


        ontologyOfCurrent = sync.ontOfData(sync.MY_PREFIX + "current",
                entityManager.createQuery("from DBBlock").setMaxResults(10).getResultList(),
                opts);


        try {
            ontologyOfVOPackages.write(new FileOutputStream("/home/bbertran/ws/WebVOWL/deploy/data/modele.json"), "RDF/JSON");
            ontologyOfCurrent.write(new FileOutputStream("/home/bbertran/ws/WebVOWL/deploy/data/current.json"), "RDF/JSON");


            Owl2Vowl o2v = new Owl2Vowl(new FileInputStream("/home/bbertran/ws/WebVOWL/deploy/data/modele.json"));
            o2v.writeToFile(new File("/home/bbertran/ws/WebVOWL/deploy/data/modeles.json"));

            Owl2Vowl o2v2 = new Owl2Vowl(new FileInputStream("/home/bbertran/ws/WebVOWL/deploy/data/current.json"));
            o2v2.writeToFile(new File("/home/bbertran/ws/WebVOWL/deploy/data/currents.json"));


        } catch ( Exception e) {
            LOG.error("Error saving ontologies to WebVOWL directory", e);
        }
    }


    public Model onto(String name, Map<String, String> opts) {

        switch (name) {
            case "vo":
                return ontologyOfVOPackages;

        }

        return sync.ontOfPackage(sync.MY_PREFIX + name,
                opts.getOrDefault("package", "net.sumaris.server.service.technical.rdf"),
                opts);
    }



    /*
     * ************* Service methods *************
     */


    @GetMapping(value = "/ntriple/{q}/{name}", produces = {"application/n-triples", "text/n-triples"})
    @ResponseBody
    public String getModelAsNTriples(@PathVariable String q, @PathVariable String name,
                                     @RequestParam(defaultValue = "false") String disjoints,
                                     @RequestParam(defaultValue = "false") String methods,
                                     @RequestParam(defaultValue = "false") String packaze) {
        return doWrite(execute(q, name, disjoints, methods, packaze), "N-TRIPLE");
    }

    @GetMapping(value = "/ttl/{q}/{name}", produces = {"text/turtle"})
    @ResponseBody
    public String getModelAsTurtle(@PathVariable String q, @PathVariable String name,
                                   @RequestParam(defaultValue = "false") String disjoints,
                                   @RequestParam(defaultValue = "false") String methods,
                                   @RequestParam(defaultValue = "false") String packaze) {
        return doWrite(execute(q, name, disjoints, methods, packaze), "TURTLE");
    }

    @GetMapping(value = "/n3/{q}/{name}", produces = {"text/n3"})
    @ResponseBody
    public String getModelAsN3(@PathVariable String q, @PathVariable String name,
                               @RequestParam(defaultValue = "false") String disjoints,
                               @RequestParam(defaultValue = "false") String methods,
                               @RequestParam(defaultValue = "false") String packaze) {
        return doWrite(execute(q, name, disjoints, methods, packaze), "N3");
    }

    @GetMapping(value = "/json/{q}/{name}", produces = {"application/x-javascript", "application/json", "application/ld+json"})
    @ResponseBody
    public String getModelAsrdfjson(@PathVariable String q, @PathVariable String name,
                                    @RequestParam(defaultValue = "false") String disjoints,
                                    @RequestParam(defaultValue = "false") String methods,
                                    @RequestParam(defaultValue = "false") String packaze) {
        return doWrite(execute(q, name, disjoints, methods, packaze), "RDF/JSON");
    }

    @GetMapping(value = "/trig/{q}/{name}", produces = {"text/trig"})
    @ResponseBody
    public String getModelAsTrig(@PathVariable String q, @PathVariable String name,
                                 @RequestParam(defaultValue = "false") String disjoints,
                                 @RequestParam(defaultValue = "false") String methods,
                                 @RequestParam(defaultValue = "false") String packaze) {
        return doWrite(execute(q, name, disjoints, methods, packaze), "TriG");
    }

    @GetMapping(value = "/jsonld/{q}/{name}", produces = {"application/x-javascript", "application/json", "application/ld+json"})
    public @ResponseBody
    String getModelAsJson(@PathVariable String q, @PathVariable String name,
                          @RequestParam(defaultValue = "false") String disjoints,
                          @RequestParam(defaultValue = "false") String methods,
                          @RequestParam(defaultValue = "false") String packaze) {
        return doWrite(execute(q, name, disjoints, methods, packaze), "JSON-LD");
    }

    @GetMapping(value = "/rdf/{q}/{name}", produces = {"application/xml", "application/rdf+xml"})
    @ResponseBody
    public String getModelAsXml(@PathVariable String q, @PathVariable String name,
                                @RequestParam(defaultValue = "false") String disjoints,
                                @RequestParam(defaultValue = "false") String methods,
                                @RequestParam(defaultValue = "false") String packaze) {
        return doWrite(execute(q, name, disjoints, methods, packaze), "RDF/XML");
    }

    @GetMapping(value = "/trix/{q}/{name}", produces = {"text/trix"})
    @ResponseBody
    public String getModelAsTrix(@PathVariable String q, @PathVariable String name,
                                 @RequestParam(defaultValue = "false") String disjoints,
                                 @RequestParam(defaultValue = "false") String methods,
                                 @RequestParam(defaultValue = "false") String packaze) {
        return doWrite(execute(q, name, disjoints, methods, packaze), "TriX");
    }


    // ===========  private methods =============

    private Model execute(String q, String name, String disjoints, String methods, String packaze) {
        LOG.info("executing /jena/{fmt}/{" + q + "}/{" + name + "}?disjoints=" + disjoints + "&methods=" + methods);
        Map<String, String> opts = new HashMap<>();
        opts.put("disjoints", disjoints);
        opts.put("methods", methods);

        Model res = null;
        if ("referentials".equals(q)) {
            res = referential(name, opts);
        } else if ("sync".equals(q)) {
            res = sync.overwriteFromRemote("http://localhost:8081" + "/jena/referentials/" + name, "");
        } else if ("ontologies".equals(q)) {
            res = onto(name, opts);
        }
        return res == null ? ontologyOfVOPackages : res;
    }


    private OntModel referential(String name, Map<String, String> opts) {
        String qRy;

        switch (name) {
            case "taxons":
                qRy = "select tn from TaxonName as tn" +
                        " left join fetch tn.taxonomicLevel as tl" +
                        " join fetch tn.referenceTaxon as rt";
                break;
            case "gears":
                qRy = "select g from Gears g";
                break;
            case "transcriptions":
                qRy = "select ti from TranscribingItem ti ";
                break;
            case "location":
                qRy = "select l from Location ";
                break;
            default:
                qRy = "from Status";
        }
        return sync.ontOfData(sync.MY_PREFIX + "queried",
                entityManager.createQuery(qRy).getResultList(),
                opts);

    }


}