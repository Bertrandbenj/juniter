package juniter.service.core;

import juniter.core.model.technical.DUPDocument;
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
import juniter.repository.jpa.sandbox.CertsSandboxRepository;
import juniter.repository.jpa.sandbox.IdtySandboxRepository;
import juniter.repository.jpa.sandbox.MembershipSandboxRepository;
import juniter.repository.jpa.sandbox.TxSandboxRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Sandboxes {


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
    public List<Certification> getPendingCertifications() {
        return cSandRepo.findAll().stream().map(is -> modelMapper.map(is, Certification.class)).collect(Collectors.toList());
    }


}
