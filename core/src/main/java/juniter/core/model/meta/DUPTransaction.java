package juniter.core.model.meta;

import juniter.core.model.dbo.BStamp;

import java.util.List;
import java.util.stream.Collectors;

public interface DUPTransaction extends DUPDocument, MultiIssuer, HasWritten, HasComment {

    List<String> getSignatures();

    List<String> getIssuers();

    List<? extends DUPInput> getInputs();

    List<? extends DUPOutput> getOutputs();

    List<? extends DUPUnlock> getUnlocks();

    Long getLocktime();

    BStamp getBlockstamp();

    @Override
    default List<String> issuers() {
        return getIssuers();
    }

    @Override
    default List<String> signatures() {
        return getSignatures();
    }

    @Override
    default String type() {
        return "Transaction";
    }


    /**
     * <pre>
     * ex :
     * Version: 10
     * Type: Transaction
     * Currency: g1
     * Blockstamp: 12345
     * Locktime: 98765
     * Issuers:
     * HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd
     * GgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd
     * Inputs:
     * 25:2:T:6991C993631BED4733972ED7538E41CCC33660F554E3C51963E2A0AC4D6453D3:0
     * 25:2:T:6991C993631BED4733972ED7538E41CCC33660F554E3C51963E2A0AC4D6453D3:65
     * Unlocks:
     * 0:SIG(1)
     * 0:XHX(1)
     * Outputs:
     * 50:2:(SIG(HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd) || (SIG(DNann1Lh55eZMEDXeYt59bzHbA3NJR46DeQYCS2qQdLV) && XHX(309BC5E644F797F53E5A2065EAF38A173437F2E6)))
     * 50:2:XHX(8AFC8DF633FC158F9DB4864ABED696C1AA0FE5D617A7B5F7AB8DE7CA2EFCD4CB)
     * Signatures:
     * DpVMlf6vaW0q+WVtcZlEs/XnDz6WtJfA448qypOqRbpi7voRqDaS9R/dG4COctxPg6sqXRbfQDieeDKU7IZWBA==
     * DpVMlf6vaW0q+WVtcZlEs/XnDz6WtJfA448qypOqRbpi7voRqDaS9R/dG4COctxPg6sqXRbfQDieeDKU7IZWBA==
     * Comment: huhuhaha
     */
    default String toDUPdoc(boolean signed) {
        return "Version: " + getVersion() +
                "\nType: Transaction" +
                "\nCurrency: g1" +
                "\nBlockstamp: " + getBlockstamp().stamp() +
                "\nLocktime: " + getLocktime() +
                "\nIssuers:\n" + String.join("\n", issuers()) +
                "\nInputs:\n" + getInputs().stream().map(DUPInput::toDUP).collect(Collectors.joining("\n")) +
                "\nUnlocks:\n" + getUnlocks().stream().map(DUPUnlock::toDUP).collect(Collectors.joining("\n")) +
                "\nOutputs:\n" + getOutputs().stream().map(DUPOutput::toDUP).collect(Collectors.joining("\n")) +
                "\nComment: " + getComment() + "\n" +
                (signed ? String.join("\n", signatures()) + "\n" : "")

                ;
    }

    @Override
    default String toDUP() {
        return toDUPdoc(true);
    }




    /**
     * <pre>
     * TX:VERSION:NB_ISSUERS:NB_INPUTS:NB_UNLOCKS:NB_OUTPUTS:HAS_COMMENT:LOCKTIME
     *
     * ex : Transactions:\nTX:10:1:1:1:2:0:0
     * 127129-00000232C91EF53648DA67D5DA32DA54C766238B48C512F66C7CC769585DFCBE
     * 8ysx7yQe47ffx379Evv3R6Qys86ekmVxwYTiVTqWq73e
     * 9506:0:T:97A239CA02FA2F97B859C2EA093FE68FEADF90A1FDE8EE69711C2048BD328128:1
     * 0:SIG(0)
     * 1000:0:SIG(CCdjH7Pd8GPe74ZbiD1DdZ1CXQ2ggYVehk2c7iVV6NwJ)
     * 8506:0:SIG(8ysx7yQe47ffx379Evv3R6Qys86ekmVxwYTiVTqWq73e)
     * EP9BhAMIbDSy9nfplSmmvp7yI6t79kO0/7/bdecGjayH+hrZxT2R4xkpEVyV3qo6Ztc1TwK+F2Hf2big5pVrCA==
     * </pre>
     */
    default String toDUPshort() {

        final var hasComment = getComment() != null && !getComment().equals("");

        return "TX:" + getVersion() + ":" + getIssuers().size() + ":" + getInputs().size() + ":" + getUnlocks().size() + ":"
                + getOutputs().size() + ":" + (hasComment ? 1 : 0) + ":" + getLocktime() + "\n" + getBlockstamp().stamp() + "\n"
                + String.join("\n", getIssuers()) + "\n"
                + getInputs().stream().map(DUPInput::toDUP).collect(Collectors.joining("\n")) + "\n" //
                + getUnlocks().stream().map(DUPUnlock::toDUP).collect(Collectors.joining("\n")) + "\n"
                + getOutputs().stream().map(DUPOutput::toDUP).collect(Collectors.joining("\n")) + "\n"
                + (hasComment ? getComment() + "\n" : "")
                + String.join("\n", getSignatures());
    }

}
