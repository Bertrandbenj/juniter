package juniter.service.jpa;

import generated.antlr.JuniterLexer;
import generated.antlr.JuniterParser;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.sandbox.CertificationSandboxed;
import juniter.core.model.dbo.sandbox.IdentitySandboxed;
import juniter.core.model.dbo.sandbox.MemberSandboxed;
import juniter.core.model.dbo.sandbox.TransactionSandboxed;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.dbo.wot.Certification;
import juniter.core.model.dbo.wot.Identity;
import juniter.core.model.dbo.wot.Member;
import juniter.core.model.dto.node.SandBoxesDTO;
import juniter.core.model.dto.node.UnitDTO;
import juniter.core.model.meta.DUPDocument;
import juniter.core.model.technical.DocumentType;
import juniter.grammar.JuniterGrammar;
import juniter.repository.jpa.sandbox.CertsSandboxRepository;
import juniter.repository.jpa.sandbox.IdtySandboxRepository;
import juniter.repository.jpa.sandbox.MembershipSandboxRepository;
import juniter.repository.jpa.sandbox.TxSandboxRepository;
import org.antlr.v4.runtime.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Sandboxes {
    private static final Logger LOG = LogManager.getLogger(Sandboxes.class);


    @Autowired
    private TxSandboxRepository tSandRepo;

    @Autowired
    private CertsSandboxRepository cSandRepo;

    @Autowired
    private IdtySandboxRepository iSandRepo;

    @Autowired
    private MembershipSandboxRepository mSandRepo;

    @Autowired
    private ModelMapper modelMapper;


    @Value("${juniter.sandboxTxField:100}")
    private Integer sandboxTxSize;

    @Value("${juniter.sandboxIMem:100}")
    private Integer sandboxMemSize;

    @Value("${juniter.sandboxIdtyField:100}")
    private Integer sandboxIdtySize;

    private JuniterGrammar visitor = new JuniterGrammar();


    public SandBoxesDTO status() {
        return new SandBoxesDTO(
                new UnitDTO(sandboxIdtySize, sandboxIdtySize - (int) iSandRepo.count()),
                new UnitDTO(sandboxMemSize, sandboxMemSize - (int) mSandRepo.count()),
                new UnitDTO(sandboxTxSize, sandboxTxSize - (int) tSandRepo.count()));
    }

    public void put(DUPDocument document) {
        if (document instanceof Transaction) {
            tSandRepo.save(modelMapper.map(document, TransactionSandboxed.class));
        } else if (document instanceof TransactionSandboxed) {
            tSandRepo.save((TransactionSandboxed) document);
        } else if (document instanceof Member) {
            mSandRepo.save(modelMapper.map(document, MemberSandboxed.class));
        } else if (document instanceof MemberSandboxed) {
            mSandRepo.save((MemberSandboxed) document);
        } else if (document instanceof Identity) {
            iSandRepo.save(modelMapper.map(document, IdentitySandboxed.class));
        } else if (document instanceof IdentitySandboxed) {
            iSandRepo.save((IdentitySandboxed) document);
        } else if (document instanceof Certification) {
            cSandRepo.save(modelMapper.map(document, CertificationSandboxed.class));
        } else if (document instanceof CertificationSandboxed) {
            cSandRepo.save((CertificationSandboxed) document);
        }
    }

    public void put(String rawDoc) throws AssertionError, Exception {
        LOG.info("received raw Document without type " + rawDoc.substring(0, 80) + "...");
        var type = rawDoc.substring(rawDoc.indexOf("Type:") + 5);
        type = type.substring(0, type.indexOf("\n")).trim().toUpperCase();
        put(rawDoc, DocumentType.valueOf(type));
    }

    public void put(String rawDoc, DocumentType type) throws AssertionError, Exception {
        LOG.info("received raw Document with type " + type);
        final var parser = juniterParser(CharStreams.fromString(rawDoc));
        var doc = parser.doc();
        assert doc != null : "Doc is null";

        var docObject = visitor.visitDoc(doc);
        LOG.info("Visited : " + docObject.getClass().getSimpleName() + " : " + docObject);


        switch (type) {
            case MEMBERSHIP:
                Member  mem = (Member ) docObject;
                var sbMem = new MemberSandboxed();
                sbMem.setPubkey(mem.getPubkey());
                sbMem.setSignature(mem.getSignature());
                mSandRepo.save(sbMem);
                break;
            case IDENTITY:
                Identity  idty = (Identity ) docObject;

//                iSandRepo.save(idty);

                break;
            case PEER:
//                PeerDoc peer = (PeerDoc) docObject;

                break;
            case REVOCATION:
//                RevocationDoc rev = (RevocationDoc) docObject;

                break;
            case TRANSACTION:
                break;
            default:
                LOG.error("Unknown doc class name " + docObject.getClass().getSimpleName());
                break;
        }


    }


    @Transactional(readOnly = true)
    public List<Transaction> getPendingTransactions() {
        return tSandRepo.findAll().stream().map(ts -> modelMapper.map(ts, Transaction.class)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Identity> getPendingIdentities() {
        return iSandRepo.findAll().stream().map(is -> modelMapper.map(is, Identity.class)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Member> getPendingMemberships() {
        return mSandRepo.findAll().stream().map(is -> modelMapper.map(is, Member.class)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    List<Certification> getPendingCertifications() {
        return cSandRepo.findAll().stream().map(is -> modelMapper.map(is, Certification.class)).collect(Collectors.toList());
    }

    /**
     * @param block the block that contains data to be cleared from the sandbox
     */
    @Transactional
    void trim(DBBlock block) {

        block.getTransactions().stream().map(Transaction::getHash).forEach(t -> tSandRepo.deleteByHash(t));

        block.getIdentities().stream().map(Identity::getPubkey).forEach(i -> iSandRepo.deleteByPubkey(i));

        block.getMembers().stream().map(Member::getPubkey).forEach(m -> mSandRepo.deleteByPubkey(m));
    }


    private JuniterParser juniterParser(CharStream file) {
        final JuniterLexer l = new JuniterLexer(file);
        final JuniterParser p = new JuniterParser(new CommonTokenStream(l));

        p.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                                    int charPositionInLine, String msg, RecognitionException e) {
                throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
            }
        });

        return p;
    }

}
