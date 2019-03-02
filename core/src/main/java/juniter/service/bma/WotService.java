package juniter.service.bma;

import juniter.core.model.business.BStamp;
import juniter.core.model.dto.MemberVO;
import juniter.core.model.dto.naughtylookup.*;
import juniter.core.model.dto.requirements.IdtyCerts;
import juniter.core.model.dto.requirements.ReqDTO;
import juniter.core.model.dto.requirements.ReqIdtyDTO;
import juniter.core.model.index.MINDEX;
import juniter.core.model.wot.Certification;
import juniter.core.model.wot.Identity;
import juniter.core.model.wot.Revoked;
import juniter.repository.jpa.CertsRepository;
import juniter.repository.jpa.index.CINDEXRepository;
import juniter.repository.jpa.index.IINDEXRepository;
import juniter.repository.jpa.index.MINDEXRepository;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ConditionalOnExpression("${juniter.useBMA:false}")
@RequestMapping("/wot")
public class WotService {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private CertsRepository certsRepo;

    @Autowired
    private CINDEXRepository cRepo;

    @Autowired
    MINDEXRepository mRepo;

    @Autowired
    IINDEXRepository iRepo;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    void handle(HttpServletResponse response) throws IOException {
        response.sendRedirect("/html/");
    }

    @RequestMapping(value = "/requirements/{pubkey}", method = RequestMethod.GET)
    public ReqDTO requirements(@PathVariable("pubkey") String pubkeyOrUid) {
        LOG.info("Entering /wot/requirements/{pubkey= " + pubkeyOrUid + "}");
        return ReqDTO.builder()
                .identities(iRepo.search(pubkeyOrUid).stream()
                        .map(i -> {

                            var certs = cRepo.receivedBy(i.getPub()).stream()
                                    .map(c-> IdtyCerts.builder()

                                            .build())
                                    .collect(Collectors.toList());
                            return ReqIdtyDTO.builder()
                                    .certifications(certs)
                                    .build();
                        })
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "/certifiers-of/{pubkeyOrUid}", method = RequestMethod.GET)
    public List<Certification> certifiersOf(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
        LOG.info("Entering /wot/certifiers-of/{pubkeyOrUid= " + pubkeyOrUid + "}");

        return certsRepo.streamCertifiersOf(pubkeyOrUid).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "/certified-by/{pubkeyOrUid}", method = RequestMethod.GET)
    public List<Certification> certifiedBy(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
        LOG.info("Entering /wot/certified-by/{pubkeyOrUid= " + pubkeyOrUid + "}");
        return certsRepo.streamCertifiedBy(pubkeyOrUid).collect(Collectors.toList());
    }

    @RequestMapping(value = "/identity-of/{pubkeyOrUid}", method = RequestMethod.GET)
    public String identityOf(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
        LOG.info("Entering /wot/identity-of/{pubkeyOrUid= " + pubkeyOrUid + "}");
        return "not implemented yet";
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/lookup/{pubkeyOrUid}", method = RequestMethod.GET)
    public WotLookup lookup(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {

        LOG.info("Entering /wot/lookup/{pubkeyOrUid= " + pubkeyOrUid + "}");


        var ids = iRepo.search(pubkeyOrUid).stream().map(i -> {
            var m = mRepo.member(i.getPub()).stream().reduce(MINDEX.reducer);
            var c = cRepo.receivedBy(i.getPub()).stream()
                    .map(cert -> OtherLookup.builder()
                            .isMember(true)
                            .meta(MetaLookup.builder()
                                    .timestamp(new BStamp(cert.getWritten_on()))
                                    .build())
                            .pubkey(cert.getIssuer())
                            .wasMember(true)
                            .uids(List.of(""))
                            .signature(cert.getSig())
                            .build())
                    .collect(Collectors.toList());

            return UserID.builder()
                    .meta(MetaLookup.builder()
                            .timestamp(new BStamp(i.getWritten_on()))
                            .build())
                    .uid(i.getUid())
                    .revoked(m.get().getRevoked_on() != null)
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
    @RequestMapping(value = "/members", method = RequestMethod.GET)
    public MembersDTO members() {
        LOG.info("Entering /wot/members");
        return new MembersDTO (iRepo.members());
    }

    @Data
    @AllArgsConstructor
    public class MembersDTO{
        List<MemberVO> results;
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
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

    @RequestMapping(value = "/certify", method = RequestMethod.POST)
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


    @RequestMapping(value = "/revoke", method = RequestMethod.POST)
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
