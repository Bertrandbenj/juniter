package juniter.service.bma;

import juniter.core.model.wot.Certification;
import juniter.core.model.wot.Identity;
import juniter.core.model.wot.Revoked;
import juniter.repository.jpa.CertsRepository;
import juniter.repository.jpa.index.CINDEXRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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


    @RequestMapping(value = "/", method = RequestMethod.GET)
    void handle(HttpServletResponse response) throws IOException {
        response.sendRedirect("/html/");
    }

    @RequestMapping(value = "/requirements/{pubkey}", method = RequestMethod.GET)
    public String requirements(@PathVariable("pubkey") String pubkeyOrUid) {
        LOG.info("Entering /wot/requirements/{pubkey= " + pubkeyOrUid + "}");
        return "not implemented yet";
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


    @RequestMapping(value = "/lookup/{pubkeyOrUid}", method = RequestMethod.GET)
    public String lookup(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
        LOG.info("Entering /wot/lookup/{pubkeyOrUid= " + pubkeyOrUid + "}");
        return "not implemented yet";
    }

    @RequestMapping(value = "/members", method = RequestMethod.GET)
    public String members() {
        LOG.info("Entering /wot/members");
        return "not implemented yet";
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
