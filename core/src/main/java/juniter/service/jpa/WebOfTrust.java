package juniter.service.jpa;

import juniter.core.model.dbo.index.CINDEX;
import juniter.core.model.dbo.index.CertRecord;
import juniter.core.model.dto.wot.lookup.MetaLookupString;
import juniter.core.model.dto.wot.requirements.IdtyCerts;
import juniter.core.model.dto.wot.requirements.ReqIdtyDTO;
import juniter.core.model.dto.wot.requirements.Requirements;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
public class WebOfTrust {

    private static final Logger LOG = LogManager.getLogger(WebOfTrust.class);


    @PersistenceContext
    private EntityManager entityManager;
//
//    @Autowired
//    private AccountRepository accountRepo;

    @Autowired
    private Index index;

//    /**
//     * hack to create account VIEW after SQL table's init.
//     * TODO: might not work on every SQL backend, please adapt
//     */
//    @PostConstruct
//    @Transactional
//    public void createAccountView() {
//        LOG.info("createAccountView");
//        //LOG.info(certificationRecordsOf("BnimajneB"));
//        try {
//
//            if (accountRepo.count() <= 0) {
//                entityManager.getTransaction().begin();
//
//                var q1 = entityManager.createNativeQuery("DROP TABLE IF EXISTS account;");
//
//
//                var q2 = entityManager.createNativeQuery(
//                        "CREATE OR REPLACE VIEW account AS " +
//                                "SELECT conditions, sum(case WHEN consumed THEN 0-getAmount ELSE getAmount end) bSum " +
//                                "FROM index_s GROUP BY conditions ORDER by conditions;");
//
//                entityManager.joinTransaction();
//
//                q1.executeUpdate();
//                q2.executeUpdate();
//                entityManager.getTransaction().commit();
//                LOG.info("Successfully added Account view ");
//            }
//
//        } catch (Exception e) {
//            LOG.error("creating Account view failed, please do manually  ", e.getMessage());
//        }
//
//    }

    public Requirements requirements(String pubkeyOrUid) {
        LOG.info("Entering /wot/requirements/{pubkey= " + pubkeyOrUid + "}");

        return Requirements.builder().identities(index.getIRepo().search(pubkeyOrUid).stream()
                .map(i -> {

                    var mindex = index.reduceM(pubkeyOrUid);
                    long now = new Date().getTime() / 1000;

                    var certs = index.getC(null, i.getPub())
                            .filter(c->c.getExpires_on() != null)
                            .sorted(Comparator.comparing(CINDEX::getWritten))
                            .map(c -> IdtyCerts.builder()
                                    .from(c.getIssuer())
                                    .to(c.getReceiver())
                                    .expiresIn(c.getExpires_on() - now)
                                    .sig(c.getSig())
                                    .timestamp(c.getWritten().getMedianTime())
                                    .build())
                            .collect(Collectors.toList());

                    return ReqIdtyDTO.builder()
                            .expired(false)
                            .isSentry(true)
                            .pubkey(i.getPub())
                            .uid(i.getUid())
                            .outdistanced(false)
                            .revoked(false)
                            .revocation_sig(null)
                            .revoked_on(null)
                            .sig(i.getSig())
                            .certifications(certs)
                            .pendingCerts(new ArrayList<>())
                            .pendingMemberships(new ArrayList<>())
                            .membershipExpiresIn(mindex.map(m -> m.getExpires_on() - now).orElse(0L))
                            .membershipPendingExpiresIn(0)
                            .wasMember(true)
                            .meta(MetaLookupString.builder().timestamp(i.getSigned().stamp()).build())
                            .build();
                })
                .collect(Collectors.toList()))
                .build();
    }

    public List<CertRecord> certificationRecordsOf(String search) {
        var res = (List<CertRecord>) entityManager.createQuery(
                "SELECT new juniter.core.model.dbo.index.CertRecord(cert, issuer, receiver) " +
                        "FROM CINDEX cert INNER JOIN IINDEX search, IINDEX issuer, IINDEX receiver " +
                        "WHERE search.uid LIKE CONCAT('%',:search,'%') " +
                        "AND ((cert.receiver = search.pub AND issuer.pub = cert.issuer) " +
                        "OR (cert.issuer = search.pub AND receiver.pub = cert.receiver))",
                CertRecord.class)
                .setParameter("search", search)
                .getResultList();

        return res;
    }

    public List<CertRecord> certRecord(String search) {
        try {
            LOG.info("Entering certRecord " + search);

            var searchIndex = index.getIRepo().search(search).get(0);

            return Stream.concat(
                    index.getC(searchIndex.getPub(), null)
                            .map(c -> new CertRecord(c,
                                    searchIndex,
                                    index.reduceI(c.getReceiver()).get()))
                    , index.getC(null, searchIndex.getPub())
                            .map(c -> new CertRecord(c,
                                    index.reduceI(c.getIssuer()).get(),
                                    searchIndex))
            )
                    .sorted(Comparator.comparing(c -> c.getCert().getWritten()))
                    .collect(toList());
        } finally {
            LOG.info("Quit certRecord " + search);
        }
    }


}
