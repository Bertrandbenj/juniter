package juniter.service.rdf;


import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdfxml.xmlinput.JenaReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Stream;
import static juniter.service.rdf.Helpers.delta;

public interface Synchro extends OwlMappers {
    Logger LOG = LogManager.getLogger(Synchro.class);

    String BASE_SYNCHRONIZE_MODEL_PACKAGE = "net.sumaris.core.model.referential";


    default OntModel overwriteFromRemote(String url, String ontIRI) {

        long start = System.nanoTime();

        OntModel m = ModelFactory.createOntologyModel();
        new JenaReader().read(m, url);
        LOG.info("Found " + m.size() + " triples remotely, reconstructing model now " + delta(start));

        List<? extends Object> recomposed = objectsFromOnt(m);
        LOG.info("Mapped ont to list of " + recomposed.size() + " objects, Making it OntClass again " + delta(start));



        Stream<Class> classes = Stream.of();

        OntModel m2 =  ontOfClasses(ontIRI, classes, null);

        recomposed.forEach(r -> bean2Owl(m2, r, 2));

        LOG.info("Recomposed list of Object is " + m2.size() + " triple  " + delta(start) + " - " + (100.0 * m2.size() / m.size()) + "%");

        if (m.size() == m2.size()) {
            LOG.info(" QUANTITATIVE SUCCESSS   ");
            if (m.isIsomorphicWith(m2)) {
                LOG.info(" ISOMORPHIC SUCCESS " + delta(start));

            }
        }

        // recomposed.forEach(obj->getEntityManager().persist(obj));

        return m2;
    }

}
