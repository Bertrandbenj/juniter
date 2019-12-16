package juniter.service.rdf;

import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public interface Owl2Bean extends Helpers {

    Logger LOG = LogManager.getLogger(Owl2Bean.class);

    default boolean classEquals(Class c, Class<?> d) {
        return Objects.equals(d.getTypeName(), c.getTypeName());
    }



    default Object getTranslatedReference(RDFNode val, Class<?> setterParam, Object obj) {


        String identifier = val.toString();
        String ontClass = null;
        if (identifier.contains("#")) {
            ontClass = identifier.substring(0, identifier.indexOf("#"));
            identifier = identifier.substring(identifier.indexOf("#") + 1);
        }
//        if (setterParam == TaxonomicLevel.class) {
//
//            for (Object ctl : getCacheTL()) {
//                String lab = ((TaxonomicLevel) ctl).getLabel();
//                if (identifier.endsWith(lab)) {
//                    return ctl;
//                }
//            }
//
//
//            // if none were cached, create a new TaxonomicLevel
//            TaxonomicLevel tl = (TaxonomicLevel) URI_2_OBJ_REF.getOrDefault(val.toString(), new TaxonomicLevel());
//            tl.setLabel(identifier);
//            tl.setCreationDate(new Date());
//            tl.setName("");
//            tl.setRankOrder(1);
//            tl.setStatus((Status) getCacheStatus().get(0));
//
//            getEntityManager().persist(tl);
//            LOG.warn("getEntityManager().persist(  TaxonomicLevel ) " + tl);
//
//            //B2O_ARBITRARY_MAPPER.get(ontClass).apply( val.as(OntResource.class));
//
//            return URI_2_OBJ_REF.putIfAbsent(val.toString(), tl);
//        }


        // Default case, try to fetch reference (@Id) as Integer or String
        LOG.warn("getTranslatedReference " + identifier + " - " + val + " - " + obj);
        Object ref;
        try {
            Integer asInt = Integer.parseInt(identifier);
            ref = getEntityManager().getReference(setterParam, asInt);
        } catch (NumberFormatException e) {
            ref = getEntityManager().getReference(setterParam, identifier);
        }
        return ref;
    }

    private String attributeOf(String pred) {
        String fName = pred.substring(pred.indexOf("#") + 1);
        fName = fName.substring(0, 1).toLowerCase() + fName.substring(1);
        return fName;
    }


    default void fillObjectWithStdAttribute(Method setter, Object obj, RDFNode val) {
        String value = val.isURIResource() ? val.toString().substring(val.toString().lastIndexOf("#") + 1) : val.toString();
        Class<?> setterParam = setter.getParameterTypes()[0];
        try {
            if (classEquals(setterParam, String.class)) {
                setter.invoke(obj, val.asLiteral().getString());
            }
            if (classEquals(setterParam, Long.class) || classEquals(setterParam, long.class)) {
                setter.invoke(obj, val.asLiteral().getLong());
            } else if (classEquals(setterParam, Integer.class) || classEquals(setterParam, int.class)) {
                setter.invoke(obj, Integer.parseInt(value));
            } else if (classEquals(setterParam, Date.class)) {
                setter.invoke(obj, SIMPLE_DATE_FORMAT.parse(val.asLiteral().getString()));
            } else if (classEquals(setterParam, Boolean.class) || classEquals(setterParam, boolean.class)) {
                setter.invoke(obj, val.asLiteral().getBoolean());
            }
        } catch (Exception e) {
            LOG.warn("fillObjectWithStdAttribute couldnt reconstruct attribute "
                    + setter.getDeclaringClass().getSimpleName() + "." + setter.getName() + "(" + setterParam.getSimpleName() + ") for val " + val, e);
        }
    }

    default Optional<Object> owl2Bean(Resource ont, OntResource ontResource, Class clazz) {
        LOG.info("processing ont Instance " + ontResource + " - " +
                ontResource
                        .asIndividual()
                        .listProperties().toList().size());

        try {
            Object obj = clazz.newInstance();

            ontResource
                    .asIndividual()
                    .listProperties()
                    .toList()
                    .forEach(stmt -> {
                        String pred = stmt.getPredicate().getURI();
                        RDFNode val = stmt.getObject();
                        if ((pred.startsWith(MY_PREFIX) || pred.startsWith(ADAGIO_PREFIX)) && pred.contains("#")) {
                            String fName = attributeOf(pred);
                            try {
                                Optional<Method> setter = null;

                                if ("setId".equals(fName)) {
                                    setter = findSetterAnnotatedID(ont, clazz);
                                } else {
                                    setter = setterOfField(ont, clazz, fName);
                                }

                                if (setter.isPresent()) {
                                    Class<?> setterParam = setter.get().getParameterTypes()[0];
                                    // LOG.info("trying to insert  " + fName + " => " + val + " using method " + me + " !!  " + huhu);

                                    if (isJavaType(setterParam)) {
                                        fillObjectWithStdAttribute(setter.get(), obj, val);
                                    } else {
                                        //FIXME if entity  is different we shouldn't use the invoked method
                                        setter.get().invoke(obj, getTranslatedReference(val, setterParam, obj));
                                    }

                                }


                            } catch (Exception e) {
                                LOG.error(e.getClass().getSimpleName() + " on field " + fName + " => " + val + " using class " + clazz + " using method " + setterOfField(ont, clazz, fName) + " " + e.getMessage(), e);
                            }

                            //values.queue(fName, safeCastRDFNode(val, fName, clazz));
                        }

                    });
//            if (obj instanceof TaxonName) {
//                TaxonName tn = (TaxonName) obj;
//                // tn.setName("tn");
//                tn.setReferenceTaxon(null);
//                getEntityManager().merge(tn);
//            }
            //getEntityManager().merge(obj);
            LOG.info("  - created object " + ontResource + " - " + " of class " + ontResource.getClass() + "  - ");
            return Optional.of(obj);
        } catch (Exception e) {
            LOG.error(" processing individual " + ontResource + " - " + clazz, e);
        }
        return Optional.empty();
    }


    default Optional<Method> findSetterAnnotatedID(Resource ont, Class clazz) {
        for (Field f : clazz.getDeclaredFields())
            for (Annotation an : f.getDeclaredAnnotations())
                if (an instanceof Id) {
                    return setterOfField(ont, clazz, f.getName());

                }
        return Optional.empty();
    }

}