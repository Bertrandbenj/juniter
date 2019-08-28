package juniter.service.rdf;

import org.apache.jena.ontology.OntModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;


@RestController
@Order(100000)
@RequestMapping(value = "/jena")
@ConditionalOnExpression("${juniter.useJenaRDF:false}")
public class RDFRestController extends OpenDataController {

    private Logger LOG = LogManager.getLogger(RDFRestController.class);
    @Autowired
    private EntityManager entityManager;

    private OntModel ontologyDTO;

    private OntModel ontologyDBO;

    private OntModel ontologyOfModule;


    @PostConstruct
    public void buildOntologies() {
        initModule();

        Map<String, String> opts = new HashMap<>();
        opts.put("disjoints", "false");
        opts.put("methods", "false");


//        ontologyDTO = ontOfCapturedClasses(MY_PREFIX + "entities",
//                Reflections.collect().getTypesAnnotatedWith(Entity.class).stream(),
//                opts);


        opts.put("packages", "juniter.core.model.dto");
        ontologyDTO = ontOfPackage(MY_PREFIX + "dto", opts);

        opts.put("packages", "juniter.core.model.dbo");
        ontologyDBO = ontOfPackage(MY_PREFIX + "dbo", opts);


        opts.put("methods", "true");
        opts.put("packages", "juniter.service.rdf");
        ontologyOfModule = ontOfPackage(MY_PREFIX + "module", opts);


        LOG.info("Loaded Ontoglogies : " +
//                " JPA => " + ontologyDTO.size() +
                " VO => " + ontologyDBO.size() +
                " O2B => " + ontologyOfModule.size());

    }


    public OntModel onto(String name, Map<String, String> opts) {

        switch (name) {
            case "dbo":
                return ontologyDBO;
            case "dto":
                return ontologyDTO;
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