package juniter.service.rdf;


import org.apache.jena.ontology.OntResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;


public interface O2BConfig {

    Logger LOG = LoggerFactory.getLogger(O2BConfig.class);

    // ============== Project Description ==============
    String TITLE = "Juniter";

    String LABEL = "A first representation of the model";

    String LICENCE = "http://www.gnu.org/licenses/gpl-3.0.html";

    String MY_PREFIX = "http://www.juniter.com/2019/03/ontology/";

    String[] AUTHORS = new String[]{"BERTRAND Benjamin"};

    String ADAGIO_PREFIX = "http://www.e-is.pro/2019/03/adagio/";

    // ============== Converter Configuration ==============

    List<Method> WHITELIST = new ArrayList<>();
    List<Method> BLACKLIST = new ArrayList<>();

    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    ZoneId ZONE_ID = ZoneId.systemDefault();

    Map<String, Class> URI_2_CLASS = new HashMap<>();
    Map<String, Object> URI_2_OBJ_REF = new HashMap<>();

    Map<String, Function<OntResource, Object>> B2O_ARBITRARY_MAPPER = new HashMap<>();
    Map<String, Function<Object, OntResource>> O2B_ARBITRARY_MAPPER = new HashMap<>();

    SimpleDateFormat sdf = new SimpleDateFormat();


    // ============== Application Configurations ==============
    Map<String, String> NAMED_QUERIES = new HashMap<>();


    EntityManager getEntityManager();

    default List getCacheStatus() {
        if (statuses == null || statuses.isEmpty())
            statuses.addAll(getEntityManager()
                    .createQuery("from Status")
                    .getResultList());
        return statuses;
    }

    List tl = new ArrayList();
    List statuses= new ArrayList();

    default List getCacheTL() {

        if (tl == null || tl.isEmpty())
            tl.addAll(getEntityManager()
                    .createQuery("from TaxonomicLevel")
                    .getResultList());
        return tl;


    }



    default void initConfig() {
        try {
            WHITELIST.addAll(Arrays.asList(
//                    Bean2Owl.getterOfField(TaxonName.class, TaxonName.PROPERTY_TAXONOMIC_LEVEL),
//                    Bean2Owl.getterOfField(TaxonName.class, TaxonName.PROPERTY_REFERENCE_TAXON),
//                    Bean2Owl.getterOfField(TaxonName.class, TaxonName.PROPERTY_STATUS),
//                    Bean2Owl.getterOfField(Location.class, Location.PROPERTY_STATUS),
//                    Bean2Owl.getterOfField(Location.class, Location.PROPERTY_LOCATION_LEVEL),
//                    Bean2Owl.getterOfField(PmfmStrategy.class, PmfmStrategy.PROPERTY_STRATEGY),
//                    Bean2Owl.getterOfField(PmfmStrategy.class, PmfmStrategy.PROPERTY_ACQUISITION_LEVEL)
            ));
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }

        BLACKLIST.addAll(Arrays.asList(
//                Bean2Owl.getterOfField(Gear.class, Gear.PROPERTY_STRATEGIES),
//                Bean2Owl.getterOfField(Gear.class, Gear.PROPERTY_CHILDREN)
        ));


        // ============== NAMED_QUERIES ==============
        NAMED_QUERIES.put("taxons", "select tn from TaxonName as tn" +
                " left join fetch tn.taxonomicLevel as tl" +
                " join fetch tn.referenceTaxon as rt" +
                " join fetch tn.status st" +
                " where tn.updateDate > '2015-01-01 23:59:50'");
        NAMED_QUERIES.put("transcriptions", "select ti from TranscribingItem ti" +
                " join fetch ti.type tit " +
                " join fetch ti.status st");
        NAMED_QUERIES.put("locations", "select l from Location l " +
                " join fetch l.locationLevel ll " +
                " join fetch l.validityStatus vs " +
                " join fetch l.status st");
        NAMED_QUERIES.put("gears", "select g from Gear g " +
                " join fetch g.gearLevel gl " +
                " join fetch g.strategies s " +
                " join fetch g.status st");


//        URI_2_CLASS.queue("definedURI", TaxonomicLevel.class);


        O2B_ARBITRARY_MAPPER.put("uri", obj -> {

            return (OntResource) null;
        });

        B2O_ARBITRARY_MAPPER.put(ADAGIO_PREFIX + "TaxonomicLevel", ontResource -> {

//            String clName = ADAGIO_PREFIX + TaxonomicLevel.class.getTypeName();
//            TaxonomicLevel tl = (TaxonomicLevel) URI_2_OBJ_REF.get(ontResource.getURI());
//
//            try {
//                // first try to get it from cache
//                Property propCode = ontResource.getModel().getProperty(clName + "#Code");
//                String label = ontResource.asIndividual().getPropertyValue(propCode).toString();
//
//
//                for (Object ctl : getCacheTL()) {
//                    if (((TaxonomicLevel) ctl).getLabel().equals(label)) {
//                        return URI_2_OBJ_REF.putIfAbsent(ontResource.getURI(), ctl);
//                    }
//                }
//
//
//                // not in cache, create a new object
//                Property name = ontResource.getModel().getProperty(clName + "#Name");
//                tl.setName(ontResource
//                        .asIndividual()
//                        .getProperty(name)
//                        .getObject()
//                        .asLiteral()
//                        .getString());
//
//                Property cd = ontResource.getModel().getProperty(clName + "#CreationDate");
//                LocalDateTime ld = LocalDateTime.parse(ontResource.asIndividual().getPropertyValue(cd).asLiteral().getString(), DATE_TIME_FORMATTER);
//
//                tl.setCreationDate(convertToDateViaInstant(ld));
//
//                Property order = ontResource.getModel().getProperty(clName + "#RankOrder");
//                tl.setRankOrder(ontResource.asIndividual().getPropertyValue(order).asLiteral().getInt());
//
//
//                tl.setStatus(getEntityManager().getReference(Status.class, 1));
//                tl.setLabel(label);
//
//                getEntityManager().persist(tl);
//                return tl;
//
//            } catch (Exception e) {
//                LOG.error("Arbitrary Mapper error " + ontResource + " - " + tl, e);
//            }
//
            return null;
        });

        B2O_ARBITRARY_MAPPER.put(ADAGIO_PREFIX + "Status", ontResource -> {
//            String clName = ADAGIO_PREFIX + Status.class.getTypeName();
//            Status st = new Status();
//
//            try {
//
//// first try to get it from cache
//                Property propCode = ontResource.getModel().getProperty(clName + "#Code");
//                Integer id = Integer.parseInt(ontResource.asIndividual().getPropertyValue(propCode).toString());
//
//                int max = -1;
//                for (Object ctl : getCacheStatus()) {
//                    if (((Status) ctl).getId().equals(id)) {
//                        return ctl;
//                    } else {
//                        max = Math.max(max, ((Status) ctl).getId());
//                    }
//                }
//
//                Property name = ontResource.getModel().getProperty(clName + "#Name");
//                st.setLabel(ontResource
//                        .asIndividual()
//                        .getProperty(name)
//                        .getObject()
//                        .asLiteral()
//                        .getString());
//
//                Property cd = ontResource.getModel().getProperty(clName + "#UpdateDate");
//
//                LocalDateTime ld = LocalDateTime.parse(ontResource.asIndividual().getPropertyValue(cd).asLiteral().getString(), DATE_TIME_FORMATTER);
//
//                st.setUpdateDate(convertToDateViaInstant(ld));
//
//                st.setId(max + 1);
//
//                st.setName("DEFAULT GENERATED VALUE");
//            } catch (Exception e) {
//                LOG.error("Arbitrary Mapper error " + ontResource + " - " + st, e);
//            }
//
            return null;
        });


    }


}