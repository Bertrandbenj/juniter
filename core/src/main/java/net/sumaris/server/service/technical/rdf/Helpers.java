package net.sumaris.server.service.technical.rdf;

import net.sumaris.server.config.O2BConfig;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.*;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Helpers extends O2BConfig {

    Map<Class, Resource> Class2Resources = new HashMap<>();
    Map<Resource, Class> Resources2Class = initStandardTypeMapper();

    static Map<Resource, Class> initStandardTypeMapper() {
        Map<Class, Resource> res = new HashMap<>();
        res.put(Date.class, XSD.date);
        res.put(LocalDateTime.class, XSD.dateTime);
        res.put(Timestamp.class, XSD.dateTimeStamp);
        res.put(Integer.class, XSD.integer);
        res.put(Short.class, XSD.xshort);
        res.put(Long.class, XSD.xlong);
        res.put(Double.class, XSD.xdouble);
        res.put(Float.class, XSD.xfloat);
        res.put(Boolean.class, XSD.xboolean);
        res.put(long.class, XSD.xlong);
        res.put(int.class, XSD.integer);
        res.put(float.class, XSD.xfloat);
        res.put(double.class, XSD.xdouble);
        res.put(short.class, XSD.xshort);
        res.put(boolean.class, XSD.xboolean);
        res.put(String.class, XSD.xstring);

        Class2Resources.putAll(res);
        return res.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (x, y) -> x));

    }


    default Optional<Class> ontToJavaClass(OntClass ontClass) {
        String uri = ontClass.getURI();
        if (uri != null) {
            if (uri.contains("#")) {
                uri = uri.substring(0, uri.indexOf("#"));
                LOG.warn(" tail '#'  " + uri);
            }
            if (uri.contains("<")) {
                uri = uri.substring(0, uri.indexOf("<"));
                LOG.warn(" tail <parametrized> " + uri);
            }
        }

        if (uri == null) {
            LOG.error(" uri null for OntClass " + ontClass);
            return Optional.empty();
        }


        Class clazz = URI_2_CLASS.get(uri);

        if (clazz == null) {
            LOG.warn(" clazz not mapped for uri " + uri);
            return Optional.empty();
        }

        if (clazz.isInterface()) {
            LOG.warn(" corresponding Type is interface, skip instances " + clazz);
            return Optional.empty();
        }
        return Optional.of(clazz);

    }


    default OntModel ontModelWithMetadata(String uri) {

        uri += (uri.endsWith("/") || uri.endsWith("#")) ? "" : "/";

        OntModel ontology = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        ontology.setNsPrefix("this", uri);
        ontology.setNsPrefix("foaf", FOAF.getURI());
        ontology.setNsPrefix("purl", DC_11.getURI()); // http://purl.org/dc/elements/1.1/

        Resource schema = ontology.createResource(uri)
                .addProperty(RDF.type, OWL.Ontology.asResource())
                .addProperty(OWL2.versionInfo, "1.1.0")
                .addProperty(OWL2.versionIRI, uri)
                .addProperty(RDFS.label, TITLE)
                .addProperty(RDFS.comment, LABEL)
                .addProperty(DC.title, TITLE)
                .addProperty(DC.date, "2019-03-05")
                .addProperty(DC.rights, LICENCE)
                .addProperty(DC.description, LABEL);

        for (String a : AUTHORS)
            schema.addProperty(DC.creator, a);

        return ontology;

    }


    default Optional<Method> setterOfField(Resource schema, Class t, String field) {
        try {
            Field f = fieldOf(schema, t, field);
            Method met = t.getMethod("set" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1), f.getType());
            return Optional.of(met);
        } catch (NoSuchMethodException e) {
            LOG.error("NoSuchMethodException setterOfField " + field + " - ", e);
        } catch (NullPointerException e) {
            LOG.error("NullPointerException setterOfField " + field + " - ", e);
        }
        return Optional.empty();
    }

    default Field fieldOf(Resource schema, Class t, String name) {
        try {
            return URI_2_CLASS.get(classToURI(schema, t)).getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            LOG.error("error fieldOf " + t.getSimpleName() + " " + name + " - " + e.getMessage());
        }
        return null;
    }


    default String classToURI(Resource ont, Class c) {
        String uri = ont + c.getSimpleName();
        if (uri.substring(1).contains("<")) {
            uri = uri.substring(0, uri.indexOf("<"));
        }
//        if (uri.endsWith("<java.lang.Integer, java.util.Date>")) {
//            uri = uri.replace("<java.lang.Integer, java.util.Date>", "");
//        }

        if (uri.contains("$")) {
            LOG.error("Inner classes not handled " + uri);
        }

        return uri;
    }


    default boolean isJavaType(Type type) {
        return Class2Resources.keySet().stream().anyMatch(type::equals);
    }

    default boolean isJavaType(Method getter) {
        return isJavaType(getter.getGenericReturnType());
    }

    default boolean isJavaType(Field field) {
        return isJavaType(field.getType());
    }


    /**
     * check the getter and its corresponding field's annotations
     *
     * @param met the getter method to test
     * @return true if it is a technical id to exclude from the model
     */
    default boolean isId(Method met) {
        return "getId".equals(met.getName())
                && Stream.concat(annotsOfField(getFieldOfGetter(met)), Stream.of(met.getAnnotations()))
                .anyMatch(annot -> annot instanceof Id || annot instanceof org.springframework.data.annotation.Id);
    }

    default boolean isManyToOne(Method met) {
        return annotsOfField(getFieldOfGetter(met)).anyMatch(annot -> annot instanceof ManyToOne) // check the corresponding field's annotations
                ||
                Stream.of(met.getAnnotations()).anyMatch(annot -> annot instanceof ManyToOne)  // check the method's annotations
                ;
    }

    default Stream<Annotation> annotsOfField(Optional<Field> field) {
        return field.map(field1 -> Stream.of(field1.getAnnotations())).orElseGet(Stream::empty);
    }

    default boolean isGetter(Method met) {
        return met.getName().startsWith("get") // only getters
                && !"getBytes".equals(met.getName()) // ignore ugly
                && met.getParameterCount() == 0 // ignore getters that are not getters
                && getFieldOfGetter(met).isPresent()
                ;
    }


    default boolean isSetter(Method met) {
        return met.getName().startsWith("set");
    }

    default Field getFieldOfGetteR(Method getter) {
        String fieldName = getter.getName().substring(3, 4).toLowerCase() + getter.getName().substring(4);
        try {
            return getter.getDeclaringClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null; // this is never going to happen right ?
        }
    }


    default Optional<Field> getFieldOfGetter(Method getter) {

        String fieldName = getter.getName().substring(3, 4).toLowerCase() + getter.getName().substring(4);
        //LOG.info("searching field : " + fieldName);
        try {
            return Optional.of(getter.getDeclaringClass().getDeclaredField(fieldName));
        } catch (Exception e) {
            //LOG.error("field not found : " + fieldName + " for class " + getter.getDeclaringClass() + "  " + e.getMessage());
            return Optional.empty();
        }
    }

    default Resource getStdType(Field f) {
        return Class2Resources.getOrDefault(f.getType(), RDFS.Literal);
//        return Class2Resources.entrySet().stream()
//                .filter((entry) -> entry.getKey().getTypeName().equals(f.getStdType().getSimpleName()))
//                .map(Map.Entry::getValue)
//                .findAny()
//                .orElse(RDFS.Literal);
    }

    default Resource getStdType(Type type) {
        return Class2Resources.getOrDefault(type, RDFS.Literal);
//        return Class2Resources.entrySet().stream()
//                .filter((entry) -> entry.getKey().getTypeName().equals(type.getTypeName()))
//                .map(Map.Entry::getValue)
//                .findAny()
//                .orElse(RDFS.Literal);
    }


    // =============== List handling ===============

    List<Class> ACCEPTED_LIST_CLASS = Arrays.asList(List.class, ArrayList.class);

    default boolean isListType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterized = (ParameterizedType) type;// This would be Class<List>, say
            Type raw = parameterized.getRawType();

            return (ACCEPTED_LIST_CLASS.stream() // add set LinkedList... if you wish
                    .anyMatch(x -> x.getCanonicalName().equals(raw.getTypeName())));
        }

        return false;

    }

    default Type getListType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterized = (ParameterizedType) type;// This would be Class<List>, say
            Type raw = parameterized.getRawType();
            Type own = parameterized.getOwnerType();
            Type[] typeArgs = parameterized.getActualTypeArguments();

            if (ACCEPTED_LIST_CLASS.stream()
                    .anyMatch(x -> x.getCanonicalName().equals(raw.getTypeName()))) {
                return typeArgs[0];
            }
        }
        return null;
    }


    // =============== Define relation  ===============

    default void createOneToMany(OntModel ontoModel, OntClass ontoClass, OntProperty prop, Resource resource) {
        OntClass minCardinalityRestriction = ontoModel.createMinCardinalityRestriction(null, prop, 1);
        ontoClass.addSuperClass(minCardinalityRestriction);
    }

    default void createZeroToMany(OntModel ontoModel, OntClass ontoClass, OntProperty prop, Resource resource) {
        OntClass minCardinalityRestriction = ontoModel.createMinCardinalityRestriction(null, prop, 0);
        ontoClass.addSuperClass(minCardinalityRestriction);
    }

    default void createZeroToOne(OntModel ontoModel, OntClass ontoClass1, OntProperty prop, OntClass ontoClass2) {
        OntClass maxCardinalityRestriction = ontoModel.createMaxCardinalityRestriction(null, prop, 1);
        ontoClass1.addSuperClass(maxCardinalityRestriction);
    }

    default void createOneToOne(OntModel ontoModel, OntClass ontoClass1, OntProperty prop, OntClass ontoClass2) {
        OntClass maxCardinalityRestriction = ontoModel.createMaxCardinalityRestriction(null, prop, 1);
        ontoClass1.addSuperClass(maxCardinalityRestriction);
    }


    // ==== pur utils ====

    static String delta(long nanoStart) {
        long elapsedTime = System.nanoTime() - nanoStart;
        double seconds = (double) elapsedTime / 1_000_000_000.0;
        return " elapsed " + seconds;
    }

    /**
     * Serialize model in requested format
     *
     * @param model  input model
     * @param format output format if null then output to RDF/XML
     * @return a string representation of the model
     */
    static String doWrite(Model model, String format) {

        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            if (format == null) {
                model.write(os);
            } else {
                model.write(os, format);
            }
            return os.toString();
        } catch (IOException e) {
            LOG.error("doWrite ", e);
        }
        return "there was an error writing the model ";
    }


}
