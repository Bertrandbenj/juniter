package juniter.service.rdf;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface OwlMappers extends Owl2Bean, Bean2Owl {
    Logger LOG = LogManager.getLogger(OwlMappers.class);

    default List<Object> objectsFromOnt(OntModel m) {
        Resource schema = m.listSubjectsWithProperty(RDF.type, OWL.Ontology).nextResource();

        List<Object> ret = new ArrayList<>();

        for (OntClass ontClass : m.listClasses().toList()) {
            LOG.info("objectsFromOnt " + ontClass + " " + ontClass.listInstances().toList().size());
            ontToJavaClass(ontClass).ifPresent(clazz -> {
                for (OntResource ontResource : ontClass.listInstances().toList()) {
                    LOG.info("  ontResource " +ontResource);

                    Function<OntResource, Object> f = B2O_ARBITRARY_MAPPER.get(ontClass.getURI());
                    if (f != null) {
                        ret.add(f.apply(ontResource));
                    } else {
                        owl2Bean(schema, ontResource, clazz).ifPresent(ret::add);
                    }
                }
            });
        }
        return ret;
    }

    default void withDisjoints(Map<OntClass, List<OntClass>> mutualyDisjoint) {

        if (mutualyDisjoint != null && !mutualyDisjoint.isEmpty()) {
            LOG.info("setting disjoints " + mutualyDisjoint.size());
            // add mutually disjoint classes
            mutualyDisjoint.entrySet().stream()
                    .filter(e -> e.getValue().size() > 1) // having more than one child
                    .forEach(e -> {
                        List<OntClass> list = e.getValue();
                        for (int i = 0; i < list.size(); i++) {
                            OntClass r1 = list.get(i);
                            for (int j = i + 1; j < list.size(); j++) {
                                OntClass r2 = list.get(j);
                                //LOG.info("setting disjoint " + i + " " + j + " " + r1 + " " + r2);
                                r1.addDisjointWith(r2);
                            }
                        }
                    });
        }

    }

    default OntModel ontOfPackage(String uri, Map<String, String> options) {

        boolean addDisjoints = options.getOrDefault("disjoints", "false").contains("true");
        boolean addInterfaces = options.getOrDefault("interface", "true").contains("true");
        boolean addMethods = options.getOrDefault("methods", "false").contains("true");
        String[] packages = options.getOrDefault("packages", "juniter.service.rdf").split(",");

        var reflections = new Reflections(packages, this.getClass().getClassLoader(), new SubTypesScanner(false));
        var allClasses = reflections.getSubTypesOf(Object.class);


//        Reflections reflections = Reflections.collect(packag, x -> true);
        LOG.info("all " + allClasses.size());


//        Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);

        OntModel model = ontModelWithMetadata(uri);

        Map<OntClass, List<OntClass>> mutualyDisjoint = null;
        if (options != null && addDisjoints) {
            mutualyDisjoint = new HashMap<>();
        }

        for (Class<?> ent : allClasses) {
            classToOwl(model, ent, mutualyDisjoint, addInterfaces, addMethods);
        }

        withDisjoints(mutualyDisjoint);
        return model;

    }

    default OntModel ontOfData(String uri, Stream objects, Map<String, String> options) {

        OntModel model = ontModelWithMetadata(uri);

        Map<OntClass, List<OntClass>> mutualyDisjoint = null;
        if (options != null && options.getOrDefault("disjoints", "false").contains("true")) {
            mutualyDisjoint = new HashMap<>();
        }

        objects.forEach(obj -> bean2Owl(model, obj, 2));

        withDisjoints(mutualyDisjoint);

        return model;

    }


    default OntModel ontOfData(String uri, List objects, Map<String, String> options) {

        OntModel model = ontModelWithMetadata(uri);

        Map<OntClass, List<OntClass>> mutualyDisjoint = null;
        if (options != null && options.getOrDefault("disjoints", "false").contains("true")) {
            mutualyDisjoint = new HashMap<>();
        }

        objects.forEach(r -> bean2Owl(model, r, 2));

        withDisjoints(mutualyDisjoint);

        return model;

    }



    default OntModel ontOfClasses(String uri, Stream<Class> classes, Map<String, String> options) {

        boolean addDisjoints = options.getOrDefault("disjoints", "false").contains("true");
        boolean addInterfaces = options.getOrDefault("interface", "true").contains("true");
        boolean addMethods = options.getOrDefault("methods", "false").contains("true");


        OntModel model = ontModelWithMetadata(uri);

        Map<OntClass, List<OntClass>> mutualyDisjoint = null;
        if (addDisjoints) {
            mutualyDisjoint = new HashMap<>();
        }

        for (Class<?> ent : classes.collect(Collectors.toList())) {
            classToOwl(model, ent, mutualyDisjoint, addInterfaces, addMethods);
        }
        withDisjoints(mutualyDisjoint);
        return model;

    }

    default OntModel ontOfCapturedClasses(String uri, Stream<Class<?>> classes, Map<String, String> options) {

        boolean addDisjoints = options.getOrDefault("disjoints", "false").contains("true");
        boolean addInterfaces = options.getOrDefault("interface", "true").contains("true");
        boolean addMethods = options.getOrDefault("methods", "false").contains("true");


        OntModel model = ontModelWithMetadata(uri);

        Map<OntClass, List<OntClass>> mutualyDisjoint = null;
        if (addDisjoints) {
            mutualyDisjoint = new HashMap<>();
        }

        for (Class<?> ent : classes.collect(Collectors.toList())) {
            classToOwl(model, ent, mutualyDisjoint, addInterfaces, addMethods);
        }

        withDisjoints(mutualyDisjoint);
        return model;

    }



}
