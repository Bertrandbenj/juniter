package net.sumaris.server.service.technical.rdf;

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

    Logger LOG = LogManager.getLogger();

    default boolean classEquals(Class c, Class<?> d) {
        return Objects.equals(d.getTypeName(), c.getTypeName());
    }


    default Object getTranslatedReference(RDFNode val, Class<?> setterParam, Object obj) {


        String identifier = val.toString();
        String ontClass = null;
        if (identifier.contains("#")) {
            ontClass= identifier.substring(0,identifier.indexOf("#")  );
            identifier = identifier.substring(identifier.indexOf("#") + 1);
        }



        // Default case, try to fetch reference (@Id) as Integer or String
        LOG.warn("mapping getEntityManager().getReference " + identifier + " - " + val + " - " + obj);
        Object ref;
        try {
            Integer asInt = Integer.parseInt(identifier);
            ref = getEntityManager().getReference(setterParam, asInt);
        } catch (NumberFormatException e) {
            ref = getEntityManager().getReference(setterParam, identifier);
        }
        return ref;
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
                            String fName = pred.substring(pred.indexOf("#") + 1);
                            fName = fName.substring(0, 1).toLowerCase() + fName.substring(1);
                            try {
                                Method setter;
                                if (fName.equals("id")) {
                                    LOG.warn("searching id field ");
                                    setter = findSetterAnnotatedID(ont, clazz);
                                } else {
                                    setter = setterOfField(ont, clazz, fName).get();
                                }
                                if (setter == null) {
                                    LOG.warn("no setter found for field " + fName + " on class " + clazz);
                                    return;
                                }
                                Class<?> setterParam = setter.getParameterTypes()[0];
                                // LOG.info("trying to insert  " + fName + " => " + val + " using method " + me + " !!  " + huhu);

                                if (classEquals(setterParam, String.class)) {
                                    setter.invoke(obj, val.asLiteral().getString());
                                } else if (classEquals(setterParam, Long.class) || classEquals(setterParam, long.class)) {
                                    setter.invoke(obj, val.asLiteral().getLong());
                                } else if (classEquals(setterParam, Integer.class) || classEquals(setterParam, int.class)) {
                                    setter.invoke(obj, Integer.parseInt(val.toString()));
                                } else if (classEquals(setterParam, Date.class)) {
                                    setter.invoke(obj, sdf.parse(val.asLiteral().getString()));
                                } else if (classEquals(setterParam, Boolean.class) || classEquals(setterParam, boolean.class)) {
                                    setter.invoke(obj, val.asLiteral().getBoolean());
                                }
//                              else if (val.isURIResource()) {
//                                  setter.invoke(obj, val.asLiteral().getBoolean());
//                              }
                                else {
                                    setter.invoke(obj, getTranslatedReference(val, setterParam, obj));
                                }
                                //myAccessor.setPropertyValue(fName,safeCastRDFNode(val, fName,clazz));
                            } catch (Exception e) {
                                LOG.error(e.getClass().getSimpleName() + " on field " + fName + " => " + val + " using class " + clazz + " using method " + setterOfField(ont, clazz, fName) + " " + e.getMessage(), e);
                            }

                            //values.put(fName, safeCastRDFNode(val, fName, clazz));
                        }

                    });
            //getEntityManager().merge(obj);
            LOG.info("  - created object " + ontResource + " - " + " of class " + ontResource.getClass() + "  - " );
            return Optional.of(obj);
        } catch (Exception e) {
            LOG.error(" processing individual " + ontResource + " - " + clazz, e);
        }
        return Optional.empty();
    }


    default Method findSetterAnnotatedID(Resource ont, Class clazz) {
        for (Field f : clazz.getDeclaredFields())
            for (Annotation an : f.getDeclaredAnnotations())
                if (an instanceof Id)
                    return setterOfField(ont, clazz, f.getName()).get();

        return null;
    }

}
