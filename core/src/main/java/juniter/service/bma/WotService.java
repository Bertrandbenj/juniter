package juniter.service.bma;

import juniter.core.model.dbo.index.MINDEX;
import juniter.core.model.dbo.wot.Certification;
import juniter.core.model.dbo.wot.Identity;
import juniter.core.model.dbo.wot.Revoked;
import juniter.core.model.dto.wot.MemberDTO;
import juniter.core.model.dto.wot.lookup.*;
import juniter.core.model.dto.wot.requirements.IdtyCerts;
import juniter.core.model.dto.wot.requirements.ReqDTO;
import juniter.core.model.dto.wot.requirements.ReqIdtyDTO;
import juniter.repository.jpa.block.CertsRepository;
import juniter.repository.jpa.index.CINDEXRepository;
import juniter.repository.jpa.index.IINDEXRepository;
import juniter.repository.jpa.index.MINDEXRepository;
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
    private CINDEXRepository cRepo;

    @Autowired
    MINDEXRepository mRepo;

    @Autowired
    IINDEXRepository iRepo;

    @Autowired
    Index index;



    @GetMapping(value = "/requirements/{pubkey}")
    public ReqDTO requirements(@PathVariable("pubkey") String pubkeyOrUid) {
        LOG.info("Entering /wot/requirements/{pubkey= " + pubkeyOrUid + "}");


        return ReqDTO.builder().identities(iRepo.search(pubkeyOrUid).stream()
                .map(i -> {

                    var mindex = index.reduceM(pubkeyOrUid);
                    long now = new Date().getTime();

                    var certs = cRepo.receivedBy(i.getPub()).stream()
                            .map(c -> IdtyCerts.builder()
                                    .from(c.getIssuer())
                                    .to(c.getReceiver())
                                    .expiresIn( c.getExpires_on() - now)
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
                            .membershipExpiresIn(mindex.map(m->m.getExpires_on()- now).orElse(0L))
                            .membershipPendingExpiresIn(0)
                            .wasMember(true)
                            .meta(MetaLookupString.builder().timestamp(i.getSigned().stamp()).build())
                            .build();
                })
                .collect(Collectors.toList()))
                .build();
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/certifiers-of/{pubkeyOrUid}")
    public List<Certification> certifiersOf(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
        LOG.info("Entering /wot/certifiers-of/{pubkeyOrUid= " + pubkeyOrUid + "}");

        return certsRepo.streamCertifiersOf(pubkeyOrUid).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/certified-by/{pubkeyOrUid}")
    public List<Certification> certifiedBy(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
        LOG.info("Entering /wot/certified-by/{pubkeyOrUid= " + pubkeyOrUid + "}");
        return certsRepo.streamCertifiedBy(pubkeyOrUid).collect(Collectors.toList());
    }

    @GetMapping(value = "/identity-of/{pubkeyOrUid}")
    public String identityOf(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
        LOG.info("Entering /wot/identity-of/{pubkeyOrUid= " + pubkeyOrUid + "}");
        return "not implemented yet";
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/lookup/{pubkeyOrUid}")
    public WotLookup lookup(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {

        LOG.info("Entering /wot/lookup/{pubkeyOrUid= " + pubkeyOrUid + "}");


        var ids = iRepo.search(pubkeyOrUid).stream().map(i -> {
            var m = mRepo.member(i.getPub()).stream().reduce(MINDEX.reducer);
            var c = cRepo.receivedBy(i.getPub()).stream()
                    .map(cert -> OtherLookup.builder()
                            .isMember(true)
                            .meta(MetaLookup.builder()
                                    .timestamp(cert.getWritten())
                                    .build())
                            .pubkey(cert.getIssuer())
                            .wasMember(true)
                            .uids(List.of(""))
                            .signature(cert.getSig())
                            .build())
                    .collect(Collectors.toList());

            return UserID.builder()
                    .meta(MetaLookup.builder()
                            .timestamp(i.getWritten())
                            .build())
                    .uid(i.getUid())
                    .revoked(m.get().getRevoked() != null)
                    .revocation_sig(m.get().getRevocation())
                    .revoked_on(null)
                    .others(c)
                    .self("self")
                    .build();
        }).collect(Collectors.toList());

        var res = WotLookup.builder()
                .partial(false)
                .results(List.of(WotLookupResult.builder()
                        .signed(null)
                        .uids(ids)
                        .pubkey(pubkeyOrUid)
                        .build()))
                .build();

        return res;
    }


    @CrossOrigin(origins = "*")
    @GetMapping(value = "/members")
    public MembersDTO members() {
        LOG.info("Entering /wot/members");
        return new MembersDTO(iRepo.members());
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
