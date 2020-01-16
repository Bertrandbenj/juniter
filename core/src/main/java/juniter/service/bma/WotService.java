package juniter.service.bma;

import juniter.core.exception.DuniterException;
import juniter.core.exception.UCode;
import juniter.core.model.dbo.index.CINDEX;
import juniter.core.model.dbo.index.MINDEX;
import juniter.core.model.dbo.wot.Certification;
import juniter.core.model.dbo.wot.Identity;
import juniter.core.model.dbo.wot.Revoked;
import juniter.core.model.dto.wot.MemberDTO;
import juniter.core.model.dto.wot.lookup.*;
import juniter.core.model.dto.wot.requirements.Requirements;
import juniter.repository.jpa.block.CertsRepository;
import juniter.service.core.Index;
import juniter.service.core.WebOfTrust;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ConditionalOnExpression("${juniter.useBMA:true}")
@RequestMapping("/wot")
public class WotService {
    private static final Logger LOG = LogManager.getLogger(WotService.class);

    @Autowired
    private CertsRepository certsRepo;

    @Autowired
    private WebOfTrust wot;

    @Autowired
    private Index index;

    @CrossOrigin("*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/requirements/{pubkey}", name = "Requirements ")
    public Requirements requirements(@PathVariable("pubkey") String pubkeyOrUid) {
        try {
            return wot.requirements(pubkeyOrUid);
        } catch (Exception e) {
            throw new DuniterException(e);
        }
    }

    @CrossOrigin("*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/certifiers-of/{pubkeyOrUid}")
    public CertLookup certifiersOf(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
        LOG.info("Entering /wot/certifiers-of/{pubkeyOrUid= " + pubkeyOrUid + "}");

        try {
            var idty = index.reduceI(pubkeyOrUid).orElseThrow(() -> new DuniterException(UCode.NO_IDTY_MATCHING_PUB_OR_UID));


            return CertLookup.builder()
                    .isMember(idty.getMember())
                    .sigDate(idty.getSigned().stamp())
                    .pubkey(pubkeyOrUid)
                    .uid(idty.getUid())
                    .certifications(
                            index.getC(null, pubkeyOrUid)
                                    .sorted(Comparator.comparing(CINDEX::getCreatedOn))
                                    .map(c -> {
                                        var idt = index.reduceI(c.getIssuer()).orElseThrow();

                                        return CertificationSection.builder()
                                                .cert_time(Certtime.builder()
                                                        .block(c.getCreatedOn())
                                                        .medianTime(index.medianAt(c.getCreatedOn()))
                                                        .build())
                                                .pubkey(c.getIssuer())
                                                .isMember(idt.getMember())
                                                .wasMember(idt.getWasMember())
                                                .sigDate(idt.getSigned().stamp())
                                                .signature(idt.getSig())
                                                .written(idt.getWritten())

                                                .build();
                                    })
                                    .collect(Collectors.toList()))
                    .build();
        } catch (Exception e) {
            LOG.error(e);
            throw new DuniterException(UCode.UNHANDLED);
        }
        //return certsRepo.streamCertifiersOf(pubkeyOrUid).collect(Collectors.toList());
    }

    @CrossOrigin("*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/certified-by/{pubkeyOrUid}")
    public CertLookup certifiedBy(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
        LOG.info("Entering /wot/certified-by/{pubkeyOrUid= " + pubkeyOrUid + "}");

        try {
            var searchedIdentity = index.reduceI(pubkeyOrUid)
                    .orElseThrow(() -> new DuniterException(UCode.NO_IDTY_MATCHING_PUB_OR_UID));


            return CertLookup.builder()
                    .isMember(searchedIdentity.getMember())
                    .sigDate(searchedIdentity.getSigned().stamp())
                    .pubkey(searchedIdentity.getPub())
                    .uid(searchedIdentity.getUid())
                    .certifications(
                            index.getC(searchedIdentity.getPub(), null).map(c -> {
                                var idt = index.reduceI(c.getReceiver()).orElseThrow();

                                return CertificationSection.builder()
                                        .cert_time(Certtime.builder()
                                                .block(c.getCreatedOn())
                                                .medianTime(index.medianAt(c.getCreatedOn()))
                                                .build())
                                        .pubkey(idt.getPub())
                                        .isMember(idt.getMember())
                                        .wasMember(idt.getWasMember())
                                        .sigDate(idt.getSigned().stamp())
                                        .signature(idt.getSig())
                                        .written(idt.getWritten())
                                        .build();
                            })
                                    .collect(Collectors.toList()))
                    .build();
        } catch (Exception e) {
            throw new DuniterException(UCode.UNHANDLED);
        }
    }

    @CrossOrigin("*")
    @GetMapping(value = "/identity-of/{pubkeyOrUid}")
    public IdentityOf identityOf(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
        return index.reduceI(pubkeyOrUid).map(i -> IdentityOf.builder()
                .pubkey(i.getPub())
                .uid(i.getUid())
                .sigDate(i.getSigned().stamp())
                .build())
                .orElseThrow(() -> new DuniterException(UCode.NO_IDTY_MATCHING_PUB_OR_UID));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/lookup/{pubkeyOrUid}")
    public WotLookup lookup(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {

        LOG.info("Entering /wot/lookup/{pubkeyOrUid= " + pubkeyOrUid + "}");


        var ids = index.getIRepo().search(pubkeyOrUid).stream().map(i -> {
            var m = index.getMRepo().member(i.getPub()).stream().reduce(MINDEX.reducer);

            var others = index.getC(null, i.getPub())
                    .map(cert -> {
                        var idt = index.reduceI(cert.getIssuer()).get();
                        return OtherLookup.builder()
                                .isMember(idt.getMember())
                                .meta(OtherBnumSection.builder()
                                        .block_hash(idt.getSigned().getHash())
                                        .block_number(idt.getSigned().getNumber())
                                        .build())
                                .pubkey(cert.getIssuer())
                                .wasMember(idt.getWasMember())
                                .uids(List.of(idt.getUid()))
                                .signature(cert.getSig())
                                .build();
                    })
                    .collect(Collectors.toList());


            return UserID.builder()
                    .meta(MetaLookup.builder()
                            .timestamp(i.getSigned().stamp())
                            .build())
                    .uid(i.getUid())
                    .revoked(m.get().getRevoked() != null)
                    .revocation_sig(m.get().getRevocation())
                    .revoked_on(null)
                    .others(others)
                    .self(i.getSig())
                    .build();
        }).collect(Collectors.toList());

        var signedSection = index.getC(pubkeyOrUid, null)
                .map(cert -> {
                            var idt = index.reduceI(cert.getReceiver()).get();

                            return SignedSection.builder()
                                    .meta(MetaLookupString.builder()
                                            .timestamp(idt.getSigned().stamp())
                                            .build())
                                    .pubkey(cert.getReceiver())
                                    .uid(idt.getUid())
                                    .signature(cert.getSig())
                                    .isMember(idt.getMember())
                                    .wasMember(idt.getWasMember())
                                    .cert_time(BnumSection.builder()
                                            .block(cert.getCreatedOn())
                                            .block_hash(index.hashAt(cert.getCreatedOn()))
                                            .build())
                                    .build();
                        }
                ).collect(Collectors.toList());

        return WotLookup.builder()
                .partial(false)
                .results(List.of(WotLookupResult
                        .builder()
                        .signed(signedSection)
                        .uids(ids)
                        .pubkey(pubkeyOrUid)
                        .build()))
                .build();
    }


    @CrossOrigin(origins = "*")
    @GetMapping(value = "/members")
    public MembersDTO members() {
        LOG.info("Entering /wot/members");
        return new MembersDTO(index.getIRepo().members());
    }

    @Data
    @AllArgsConstructor
    public class MembersDTO {
        List<MemberDTO> results;
    }


    @PostMapping(value = "/add")
    ResponseEntity<Identity> add(HttpServletRequest request, HttpServletResponse response) {

        LOG.info("POSTING /wot/add ..." + request.getRemoteHost());


        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            LOG.info(in.lines().collect(Collectors.joining("\n")));
        } catch (Exception e) {
            LOG.error("error reading wot/add inputStream ", e);
        }


        Identity idty = new Identity();
        final var headers = new HttpHeaders();

        return new ResponseEntity<>(idty, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/certify")
    ResponseEntity<Certification> certify(HttpServletRequest request, HttpServletResponse response) {

        LOG.info("POSTING /wot/certify ...");
        String remote = request.getRemoteHost();


        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            LOG.info(in.lines().collect(Collectors.joining("\n")));
        } catch (Exception e) {
            LOG.error("error reading wot/certify inputStream ", e);
        }


        Certification idty = new Certification();
        final var headers = new HttpHeaders();


        LOG.info("remote " + remote);

        return new ResponseEntity<>(idty, headers, HttpStatus.OK);
    }


    @PostMapping(value = "/revoke")
    ResponseEntity<Revoked> revoke(HttpServletRequest request, HttpServletResponse response) {

        LOG.info("POSTING /wot/revoke ...");
        String remote = request.getRemoteHost();

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            LOG.info(in.lines().collect(Collectors.joining("\n")));
        } catch (Exception e) {
            LOG.error("error reading wot/revoke inputStream ", e);
        }


        Revoked idty = new Revoked();
        final var headers = new HttpHeaders();


        LOG.info("remote " + remote);

        return new ResponseEntity<>(idty, headers, HttpStatus.OK);
    }

}
