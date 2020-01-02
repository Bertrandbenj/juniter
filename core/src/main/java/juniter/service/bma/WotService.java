package juniter.service.bma;

import juniter.core.model.dbo.index.CINDEX;
import juniter.core.model.dbo.index.MINDEX;
import juniter.core.model.dbo.wot.Certification;
import juniter.core.model.dbo.wot.Identity;
import juniter.core.model.dbo.wot.Revoked;
import juniter.core.model.dto.wot.MemberDTO;
import juniter.core.model.dto.wot.lookup.*;
import juniter.core.model.dto.wot.requirements.IdtyCerts;
import juniter.core.model.dto.wot.requirements.ReqIdtyDTO;
import juniter.core.model.dto.wot.requirements.Requirements;
import juniter.repository.jpa.block.CertsRepository;
import juniter.service.core.Index;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ConditionalOnExpression("${juniter.useBMA:false}")
@RequestMapping("/wot")
public class WotService {
    private static final Logger LOG = LogManager.getLogger(WotService.class);

    @Autowired
    private CertsRepository certsRepo;

    @Autowired
    private Index index;

    @CrossOrigin("*")
    @GetMapping(value = "/requirements/{pubkey}")
    public Requirements requirements(@PathVariable("pubkey") String pubkeyOrUid) {
        LOG.info("Entering /wot/requirements/{pubkey= " + pubkeyOrUid + "}");


        return Requirements.builder().identities(index.getIRepo().search(pubkeyOrUid).stream()
                .map(i -> {

                    var mindex = index.reduceM(pubkeyOrUid);
                    long now = new Date().getTime() / 1000;

                    var certs = index.getCRepo().receivedBy(i.getPub()).stream()
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

    @CrossOrigin("*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/certifiers-of/{pubkeyOrUid}")
    public CertLookup certifiersOf(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
        LOG.info("Entering /wot/certifiers-of/{pubkeyOrUid= " + pubkeyOrUid + "}");

        var idty = index.reduceI(pubkeyOrUid).orElseThrow();


        return CertLookup.builder()
                .isMember(idty.getMember())
                .sigDate(idty.getSigned().stamp())
                .pubkey(pubkeyOrUid)
                .uid(idty.getUid())
                .certifications(
                        index.getCRepo().receivedBy(pubkeyOrUid).stream()
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

        //return certsRepo.streamCertifiersOf(pubkeyOrUid).collect(Collectors.toList());
    }

    @CrossOrigin("*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/certified-by/{pubkeyOrUid}")
    public CertLookup certifiedBy(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
        LOG.info("Entering /wot/certified-by/{pubkeyOrUid= " + pubkeyOrUid + "}");

        var idty = index.reduceI(pubkeyOrUid).orElseThrow();


        return CertLookup.builder()
                .isMember(idty.getMember())
                .sigDate(idty.getSigned().stamp())
                .pubkey(pubkeyOrUid)
                .uid(idty.getUid())
                .certifications(
                        index.getCRepo().issuedBy(pubkeyOrUid).stream()
                                .map(c -> {
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

    }

    @CrossOrigin("*")
    @GetMapping(value = "/identity-of/{pubkeyOrUid}")
    public IdentityOf identityOf(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
        return index.reduceI(pubkeyOrUid).map(i -> IdentityOf.builder()
                .pubkey(i.getPub())
                .uid(i.getUid())
                .sigDate(i.getSigned().stamp())
                .build())
                .orElseThrow();
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/lookup/{pubkeyOrUid}")
    public WotLookup lookup(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {

        LOG.info("Entering /wot/lookup/{pubkeyOrUid= " + pubkeyOrUid + "}");


        var ids = index.getIRepo().search(pubkeyOrUid).stream().map(i -> {
            var m = index.getMRepo().member(i.getPub()).stream().reduce(MINDEX.reducer);

            var others = index.getCRepo().receivedBy(i.getPub()).stream()
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

        var signedSection = index.getCRepo().issuedBy(pubkeyOrUid).stream()
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
