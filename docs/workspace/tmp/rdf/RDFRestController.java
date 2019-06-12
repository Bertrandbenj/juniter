package juniter.service.rdf;

import org.apache.jena.ontology.OntModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/jena")
public class RDFRestController extends OpenDataController {

    Logger LOG = LogManager.getLogger(RDFRestController.class);
    @Autowired
    private EntityManager entityManager;

    private OntModel ontologyOfJPAEntities;

    private OntModel ontologyOfVOPackages;

    private OntModel ontologyOfModule;


    @PostConstruct
    public void buildOntologies() {
        initModule();

        Map<String, String> opts = new HashMap<>();
        opts.put("disjoints", "false");
        opts.put("methods", "false");




//        ontologyOfJPAEntities = ontOfCapturedClasses(MY_PREFIX + "entities",
//                Reflections.collect().getTypesAnnotatedWith(Entity.class).stream(),
//                opts);


        opts.put("packages", "juniter.core.model");
        ontologyOfVOPackages = ontOfPackage(MY_PREFIX + "model", opts);


        opts.put("methods", "true");
        opts.put("packages", "juniter.service.rdf");
        ontologyOfModule = ontOfPackage(MY_PREFIX + "module", opts);


        LOG.info("Loaded Ontoglogies : " +
//                " JPA => " + ontologyOfJPAEntities.size() +
                " VO => " + ontologyOfVOPackages.size() +
                " O2B => " + ontologyOfModule.size());

    }


    public OntModel onto(String name, Map<String, String> opts) {

        switch (name) {
            case "vo":
                return ontologyOfVOPackages;
            case "entities":
                return ontologyOfJPAEntities;
            case "module":
                return ontologyOfModule;
        }


        return ontOfPackage(MY_PREFIX + name, opts);
    }


    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}