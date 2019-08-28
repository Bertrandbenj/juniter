package juniter.service.rdf;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

public interface Bean2Owl extends Helpers {

    Logger LOG = LogManager.getLogger(Bean2Owl.class);

    /**
     * Returns and adds OntClass to the model
     *
     * @param ontology
     * @param clazz
     * @return
     */
    default OntClass classToOwl(OntModel ontology, Class clazz, Map<OntClass, List<OntClass>> mutualyDisjoint, boolean addInterface, boolean addMethods) {

        Resource schema = ontology.listSubjectsWithProperty(RDF.type, OWL.Ontology).nextResource();

        try {
            //OntResource r = m.createOntResource(SCHEMA_URL+ ent.getName().replaceAll("\\.", "/"));
            //OntProperty pred = m.createOntProperty(SCHEMA_URL+ ent.getName().replaceAll("\\.", "/"));
            //String classBase = ent.getName().replaceAll("\\.", "/");
//                        LOG.info("Building Ont of " + classBase);

            OntClass aClass = ontology.createClass(classToURI(schema, clazz));
            aClass.setIsDefinedBy(schema);

            //aClass.addSameAs(ontology.createResource(classToURI(schema, ent)));

            String label = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
            //aClass.addLabel(label + "e", "fr");
            aClass.addLabel(label, "en");

            String entityName = clazz.getName();
            if (!"".equals(entityName))
                aClass.addComment(entityName, "en");


            if (mutualyDisjoint != null && addInterface) {
                Stream.of(clazz.getGenericInterfaces())
                        .filter(interfaze -> !ParameterizedType.class.equals(interfaze))
                        .forEach(interfaze -> {
                            // LOG.info(interfaze+" "+Class.class + " " +  interfaze.getClass() );
                            OntClass r = typeToUri(schema, interfaze);
                            r.setIsDefinedBy(schema);
                            if (!mutualyDisjoint.containsKey(r)) {
                                mutualyDisjoint.put(r, new ArrayList<>());
                            }
                            mutualyDisjoint.get(r).add(aClass);

                            aClass.addSuperClass(r);
                        });
            }


            Stream.of(clazz.getGenericSuperclass())
                    .filter(c -> c != null && !Object.class.equals(c))
                    .forEach(i -> aClass.addSuperClass(typeToUri(schema, i)));

            Stream.of(clazz.getGenericSuperclass())
                    .filter(c -> c != null && !Object.class.equals(c))
                    .forEach(i -> aClass.addSuperClass(typeToUri(schema, i)));


            if (addMethods) {
                Stream.of(clazz.getMethods())
                        .filter(m -> !isSetter(m))
                        .filter(m -> !isGetter(m))
                        .filter(m -> !"getBytes".equals(m.getName()) && !"getClass".equals(m.getName()))

                        .forEach(met -> {

                            String name = classToURI(schema, clazz) + "#" + met.getName();
                            OntProperty function = ontology.createObjectProperty(name, true);
                            function.addDomain(aClass.asResource());
                            if (isJavaType(met.getReturnType())) {
                                function.addRange(getStdType(met.getReturnType()));
                            } else {
                                OntClass o = typeToUri(schema, met.getReturnType());
                                if (!o.getURI().endsWith("void"))
                                    function.addRange(o);
                                else
                                    function.addRange(RDFS.Literal);
                            }
                            function.setIsDefinedBy(schema);
                            function.addLabel(met.getName(), "en");
                        });
            }


            Stream.of(clazz.getMethods())
                    .filter(this::isGetter)
                    .map(this::getFieldOfGetteR)
                    .forEach(field -> {
                        //LOG.info("processing Field : " + field + " - type?" + isJavaType(field) + " - list?" + isListType(field.getGenericType()));
                        String fieldName = classToURI(schema, clazz) + "#" + field.getName();

                        if (isJavaType(field)) {
                            Resource type = getStdType(field);

                            OntProperty stdType = ontology.createDatatypeProperty(fieldName, true);

                            stdType.setDomain(aClass.asResource());
                            stdType.setRange(type);
                            stdType.setIsDefinedBy(schema);


                            //link.addRDFType(ontology.createResource(type));
                            stdType.addLabel(field.getName(), "en");
                            //LOG.info("Simple property of type " + type + " for " + fieldName + "\n" + link);
                        } else if (isListType(field.getGenericType())) {
                            Type contained = getListType(field.getGenericType());
                            OntProperty list = null;
                            Resource resou = null;
                            LOG.info("List property x " + contained.getTypeName() + " for " + fieldName);

                            if (isJavaType(contained)) {
                                list = ontology.createDatatypeProperty(fieldName, true);
                                resou = getStdType(contained);
                            } else {
                                list = ontology.createObjectProperty(fieldName, false);
                                resou = typeToUri(schema, contained);
                            }

                            list.addRange(resou);
                            list.addDomain(aClass.asResource());
                            list.setIsDefinedBy(schema);
                            list.addLabel("list" + field.getName(), "en");

                            createZeroToMany(ontology, aClass, list, resou);

                        } else {

                            // var type = ontology.createObjectProperty();
                            OntProperty bean = ontology.createObjectProperty(fieldName, true);
                            bean.addDomain(aClass.asResource());
                            bean.addRange(typeToUri(schema, field.getType()));
                            bean.setIsDefinedBy(schema);
                            bean.addLabel(field.getName(), "en");
                            // LOG.info("Default Object property x " + link);

                        }
                    });
            return aClass;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    default OntClass typeToUri(Resource schema, Type t) {

        String uri = schema + t.getTypeName();
        if (t instanceof ParameterizedType) {
            uri = uri.substring(0, uri.indexOf("<"));
        }

        OntClass ont = ((OntModel) schema.getModel()).getOntClass(uri);

        if (ont == null) {
            ont = interfaceToOwl((OntModel) schema.getModel(), t);
        }

        return ont;

    }

    default OntClass interfaceToOwl(OntModel model, Type type) {
        return model.createClass(MY_PREFIX + type.getTypeName());
    }

    default Resource bean2Owl(OntModel model, Object obj, int depth) {
        Resource schema = model.listSubjectsWithProperty(RDF.type, OWL.Ontology).nextResource();

        if (obj == null) {
            LOG.error("toModel received a null object as parameter");
            return null;
        }
        String classURI = classToURI(schema, obj.getClass());
        OntClass ontClazz = model.getOntClass(classURI);
        if (ontClazz == null) {
            LOG.warn("ontClazz " + ontClazz + ", not found in model, making one at " + classURI);
            ontClazz = classToOwl(model, obj.getClass(), null, true, true);
        }

        // try using the ID field if exists to represent the node
        String individualURI;
        try {
            Method m = findGetterAnnotatedID(obj.getClass());
            individualURI = classURI + "#" + m.invoke(obj);
            LOG.info("Created objectIdentifier " + individualURI);
        } catch (Exception e) {
            individualURI = "";
            LOG.error(e.getClass().getName() + " bean2Owl " + classURI + " - ");
        }
        Resource individual = ontClazz.createIndividual(individualURI); // model.createResource(node);
        if (depth < 0) {
            LOG.error("Max depth reached " + depth);
            return individual;
        } else {
            // LOG.warn("depth reached " + depth);

        }


        // Handle Methods
        Stream.of(obj.getClass().getMethods())
                .filter(this::isGetter)
                .filter(met -> BLACKLIST.stream().noneMatch(x -> x.equals(met)))
                .filter(met -> (!isManyToOne(met)) || WHITELIST.contains(met))
                .forEach(met -> {

                    //LOG.info("processing method " + met.getDeclaringClass().getSimpleName()+"."+ met.getName()+" "+met.getGenericReturnType());
                    try {
                        Object invoked = met.invoke(obj);
                        if (invoked == null) {
                            //LOG.warn("invoked function null "+ met.getName() + " skipping... " );
                            return;
                        }
                        Property pred = model.createProperty(classURI, "-" + met.getName().replace("get", ""));

                        if (isId(met)) {
                            individual.addProperty(pred, invoked + "");
                        } else if ("getClass".equals(met.getName())) {
                            individual.addProperty(RDF.type, pred);
                        } else if (invoked.getClass().getCanonicalName().contains("$")) {
                            //skip inner classes. mostly handles generated code issues
                        } else if (!isJavaType(met)) {

                            //LOG.info("not java generic, recurse on node..." + invoked);
                            Resource recurse = bean2Owl(model, invoked, (depth - 1));
                            LOG.warn("recurse null for " + met.getName() + "  " + invoked.getClass() + "  ");
                            if (recurse != null)
                                individual.addProperty(pred, recurse);
                        } else if (met.getGenericReturnType() instanceof ParameterizedType) {

                            Resource anonId = model.createResource(new AnonId("params" + new Random().nextInt(1000000)));
                            //individual.addProperty( pred, anonId);

                            Optional<Resource> listNode = fillDataList(model, met.getGenericReturnType(), invoked, pred, anonId, depth - 1);
                            if (listNode.isPresent()) {
                                LOG.info(" --and res  : " + listNode.get().getURI());
                                individual.addProperty(pred, listNode.get());
                            }
                            //
                        } else {
                            if (met.getName().toLowerCase().contains("date")) {
                                String d = sdf.format((Date) invoked);
                                individual.addProperty(pred, d);

                            } else {
                                individual.addProperty(pred, invoked + "");
                            }
                        }

                    } catch (Exception e) {
                        LOG.error(e.getMessage(), e);
                    }

                });

        return individual;
    }


    default Method findGetterAnnotatedID(Class clazz) {
        for (Field f : clazz.getDeclaredFields())
            for (Annotation an : f.getDeclaredAnnotations())
                if (an instanceof Id)
                    return getterOfField(clazz, f.getName());

        return null;
    }


    static Method getterOfField(Class t, String field) {
        try {
            Method res = t.getMethod("get" + field.substring(0, 1).toUpperCase() + field.substring(1));
            return res;
        } catch (NoSuchMethodException e) {
            LOG.error("error in the declaration of allowed ManyToOne " + e.getMessage());
        }
        return null;
    }


    default Optional<Resource> fillDataList(OntModel model, Type type, Object listObject, Property prop, Resource fieldId, int depth) {

        if (isListType(type)) {

// Create a list containing the subjects of the role assignments in one go

            List<RDFNode> nodes = new ArrayList<>();
            List<? extends Object> asList = castListSafe((List<? extends Object>) listObject, Object.class);

            if (asList.isEmpty()) {
                LOG.warn(" - empty list, ignoring ");
                return Optional.empty();
            }
            for (Object x : asList) {
                Resource listItem = bean2Owl(model, x, (depth - 1));
                nodes.add(listItem);

            }

            RDFList list = model.createList(nodes.toArray(new RDFNode[nodes.size()]));

            LOG.info("  - rdflist " + list.size() + " : " + list);
//var tmp = model.createProperty("sdfsdfsdf"+new Random().nextInt(10000000));
            fieldId.addProperty(prop, list);
            //              fieldId.addProperty(tmp ,model.createList(list));
            return Optional.of(list);

        }

//        }
        return Optional.empty();
    }

    /**
     * Performs a forced cast.
     * Returns null if the collection type does not match the items in the list.
     *
     * @param data     The list to cast.
     * @param listType The type of list to cast to.
     */
    default <T> List<? super T> castListSafe(List<?> data, Class<T> listType) {
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


}
