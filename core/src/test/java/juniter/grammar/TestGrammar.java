package juniter.grammar;

import antlr.generated.JuniterLexer;
import antlr.generated.JuniterParser;
import antlr.generated.JuniterParser.WotContext;
import juniter.core.crypto.Crypto;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;

import static org.junit.Assert.assertTrue;

public class TestGrammar implements BlockExamples {

    private static final Logger LOG = LogManager.getLogger(TestGrammar.class);


    JuniterGrammar visitor = new JuniterGrammar();


    ClassLoader classLoader = getClass().getClassLoader();

    Path documents = new File(classLoader.getResource("documents/").getFile()).toPath();


    String getRaw(WotContext idtc) {
        final CharStream cs = idtc.start.getTokenSource().getInputStream();
        final var idtyIdx = idtc.signature().start.getStartIndex() - 1;

        return cs.getText(Interval.of(0, idtyIdx));
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

    @Test
    public void testBuid() {

        final var TEST_BUID = "32-DB30D958EE5CB75186972286ED3F4686B8A1C2CD";
        final var parser = juniterParser(CharStreams.fromString(TEST_BUID));
        final var buidContext = parser.buid();
        assertTrue(TEST_BUID.equals(buidContext.getText()));
        LOG.info("buid : " + buidContext.getText());
    }

    @Test
    public void testCertification() throws Exception {
        final var parser = juniterParser(CharStreams.fromPath(documents.resolve("certif.dup")));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        System.out.println(doc);
    }


    @Test
    public void testIdentity() throws Exception {
        final var parser = juniterParser(CharStreams.fromPath(documents.resolve("identity.dup")));

        var doc = parser.doc();

        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void testBlockFromFile() throws Exception {
        final var parser = juniterParser(CharStreams.fromPath(documents.resolve("block.dup")));

        var doc = parser.doc();

        assert doc != null : "Doc is null";
        LOG.info(doc);
    }


    @Test
    public void testInlinedIdentity() {

        final String signature = "L4AiZBYrmriQT3+r/WIpxobEnX2pdPgDg7Cf50vJ74c0GsL1CaEfJ3JmbqwARvzNkiewB3GR77gxFjZDIM0XAg==";
        final var pk = "4tsFXtKofD6jK8zwLymgXWAoMBzhFx7KNANhtK86mKzA";
        final var unSignedDoc = "Version: 10\n" //
                + "Type: Identity\n" //
                + "Currency: g1\n" //
                + "Issuer: " + pk + "\n" //
                + "UniqueID: AlainLebrun\n" //
                + "Timestamp: 1184-00000C17FA48A4681377DFA9BF1CE747CE82B869E694EC9E41F9F530C45E8F19\n";
        final var doc = unSignedDoc + signature;// + "\n";

        assertTrue("Assert before parsing", Crypto.verify(unSignedDoc, signature, pk));

        final var parser = juniterParser(CharStreams.fromString(doc));
        final var wot = parser.doc().wot();
        final var idty = wot.identity();
        LOG.info(idty + " " + getRaw(wot));

        assertTrue(idty.version().getText().equals("10"));
        assertTrue(idty.issuer().getText().equals(pk));
        assertTrue(signature.equals(wot.signature().getText()));
        assertTrue(Crypto.verify( //
                getRaw(wot), //
                wot.signature().getText(), //
                idty.issuer().getText()));

        // assertTrue("Assert generated Doc is valid",
        // visitor.visit(parser.Doc()).isValid());

    }

    @Test
    public void testIssuer() {
        final var parser = juniterParser(CharStreams.fromString("DNann1Lh55eZMEDXeYt59bzHbA3NJR46DeQYCS2qQdLV"));

        final var issuerContext = parser.issuer();

        assertTrue("DNann1Lh55eZMEDXeYt59bzHbA3NJR46DeQYCS2qQdLV"//
                .equals(issuerContext.toString()));

    }

    @Test
    public void testListener() throws Exception {
        final var juniterListener = new JuniterGrammar();

//		final var testFiles = Paths.get(folder).toFile().list();
        final Path[] testFiles = new Path[]{documents.resolve("peer.dup"), documents.resolve("identity2.dup")};

        for (final Path file : testFiles) {
            if (file.endsWith(".dup")) {
                final var parser = juniterParser(CharStreams.fromPath(file));

                final var doc = juniterListener.visit(parser.doc());
                LOG.info("testListener " + doc.toString());
                assertTrue(doc.isValid());
            }
        }

    }

    @Test
    public void testMembership() throws Exception {

        //File file = new File(classLoader.getResource("documents/membership.dup").getFile());
        final var parser = juniterParser(CharStreams.fromPath(documents.resolve("membership.dup")));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
    }

    @Test
    public void testPeer() throws Exception {
        final var parser = juniterParser(CharStreams.fromPath(documents.resolve("peer.dup")));
        var doc = parser.doc();

        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void testRevocation() throws Exception {


        final var parser = juniterParser(CharStreams.fromPath(documents.resolve("revocation.dup")));

        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);

        //Document d = visitor.visitDoc(Doc);
        //assert ! d.isValid() : "document is invalid ";

    }


    @Test
    public void testDuniterAllBlocks() {

        testFailDoc(WRONG_SIGNATURE);
        testOneDoc(VALID_ROOT);
        testFailDoc(WRONG_PROOF_OF_WORK);
        testOneDoc(ROOT_WITHOUT_PARAMETERS);
        testOneDoc(NON_ROOT_WITH_PARAMETERS);
        testOneDoc(ROOT_WITH_PREVIOUS_HASH);
        testOneDoc(ROOT_WITH_PREVIOUS_ISSUER);
        testOneDoc(NON_ROOT_WITHOUT_PREVIOUS_HASH);
        testOneDoc(NON_ROOT_WITHOUT_PREVIOUS_ISSUER);
        testOneDoc(COLLIDING_UIDS);

        testOneDoc(COLLIDING_PUBKEYS);
        testOneDoc(WRONG_DATE_LOWER);
        testOneDoc(WRONG_DATE_HIGHER_BUT_TOO_FEW);
        testOneDoc(WRONG_DATE_HIGHER_BUT_TOO_HIGH);
        testOneDoc(WRONG_ROOT_TIMES);
        testOneDoc(GOOD_DATE_HIGHER);
        testOneDoc(WRONG_IDTY_MATCH_JOINS);
        testOneDoc(MULTIPLE_JOINERS);
        testOneDoc(MULTIPLE_ACTIVES);
        testOneDoc(MULTIPLE_LEAVES);

        testOneDoc(MULTIPLE_EXCLUDED);
        testOneDoc(MULTIPLE_OVER_ALL);
        testOneDoc(MULTIPLE_CERTIFICATIONS_FROM_SAME_ISSUER);
        testOneDoc(IDENTICAL_CERTIFICATIONS);
        testOneDoc(LEAVER_WITH_CERTIFICATIONS);
        testOneDoc(EXCLUDED_WITH_CERTIFICATIONS);
        testOneDoc(WRONGLY_SIGNED_IDENTITIES);
        testOneDoc(WRONGLY_SIGNED_JOIN);
        testOneDoc(WRONGLY_SIGNED_ACTIVE);
        testOneDoc(WRONGLY_SIGNED_LEAVE);

        testOneDoc(CORRECTLY_SIGNED_LEAVE);
        testOneDoc(WRONGLY_SIGNED_CERTIFICATION);
        testOneDoc(UNKNOWN_CERTIFIER);
        testOneDoc(UNKNOWN_CERTIFIED);
        testOneDoc(EXISTING_UID);
        testOneDoc(EXISTING_PUBKEY);
        testOneDoc(TOO_EARLY_CERTIFICATION_REPLAY);
        testOneDoc(EXPIRED_CERTIFICATIONS);
        testOneDoc(EXPIRED_MEMBERSHIP);
        testOneDoc(REVOKED_JOINER);

        testOneDoc(NOT_ENOUGH_CERTIFICATIONS_JOINER);
        testOneDoc(NOT_ENOUGH_CERTIFICATIONS_JOINER_BLOCK_0);
        testOneDoc(OUTDISTANCED_JOINER);
        testOneDoc(VALID_NEXT);
        testOneDoc(WRONG_PREVIOUS_HASH);
        testOneDoc(WRONG_PREVIOUS_ISSUER);
        testOneDoc(WRONG_DIFFERENT_ISSUERS_COUNT_FOLLOWING_V2);
        testOneDoc(WRONG_DIFFERENT_ISSUERS_COUNT_FOLLOWING_V3);
        testOneDoc(WRONG_ISSUERS_FRAME_FOLLOWING_V2);
        testOneDoc(WRONG_ISSUERS_FRAME_FOLLOWING_V3);

        testOneDoc(WRONG_ISSUER);
        testOneDoc(WRONG_JOIN_BLOCK_TARGET_ROOT);
        testOneDoc(WRONG_JOIN_ROOT_NUMBER);
        testOneDoc(WRONG_JOIN_ROOT_HASH);
        testOneDoc(WRONG_JOIN_NUMBER_TOO_LOW);
        testOneDoc(WRONG_JOIN_BLOCK_TARGET);
        testOneDoc(WRONG_JOIN_ALREADY_MEMBER);
        testOneDoc(WRONG_ACTIVE_BLOCK_TARGET);
        testOneDoc(KICKED_NOT_EXCLUDED);
        testOneDoc(KICKED_EXCLUDED);


        testOneDoc(WRONG_MEMBERS_COUNT);
        testOneDoc(NO_LEADING_ZERO);
        testOneDoc(REQUIRES_4_LEADING_ZEROS);
        testOneDoc(REQUIRES_7_LEADING_ZEROS);
        testOneDoc(REQUIRES_6_LEADING_ZEROS);
        testOneDoc(REQUIRES_5_LEADING_ZEROS);
        testOneDoc(REQUIRES_7_LEADING_ZEROS_AGAIN);
        testOneDoc(FIRST_BLOCK_OF_NEWCOMER);
        testOneDoc(SECOND_BLOCK_OF_NEWCOMER);
        testOneDoc(WRONG_ROOT_DATES);


        testOneDoc(WRONG_MEDIAN_TIME_ODD);
        testOneDoc(WRONG_MEDIAN_TIME_EVEN);
        testOneDoc(GOOD_MEDIAN_TIME_ODD);
        testOneDoc(GOOD_MEDIAN_TIME_EVEN);
        testOneDoc(WRONG_CONFIRMED_DATE_MUST_CONFIRM);
        testOneDoc(ROOT_BLOCK_WITH_UD);
        testOneDoc(UD_BLOCK_WIHTOUT_UD);
        testOneDoc(UD_BLOCK_WIHTOUT_BASE);
        testOneDoc(V3_ROOT_BLOCK_NOBASE);
        testOneDoc(V3_ROOT_BLOCK_POSITIVE_BASE);

        testOneDoc(BLOCK_WITH_WRONG_UD);
        testOneDoc(BLOCK_WITH_WRONG_UD_V3);
        testOneDoc(BLOCK_WITH_WRONG_UNIT_BASE);
        testOneDoc(BLOCK_WITH_WRONG_UNIT_BASE_NO_UD);
        testOneDoc(BLOCK_UNLEGITIMATE_UD);
        testOneDoc(BLOCK_UNLEGITIMATE_UD_2);
        testOneDoc(FIRST_UD_BLOCK_WITH_UD_THAT_SHOULDNT);
        testOneDoc(FIRST_UD_BLOCK_WITH_UD_THAT_SHOULD);
        testOneDoc(BLOCK_WITHOUT_TRANSACTIONS);
        testOneDoc(BLOCK_WITH_GOOD_TRANSACTIONS);

        testOneDoc(BLOCK_WITH_WRONG_TRANSACTION_SUMS);
        testOneDoc(BLOCK_WITH_WRONG_TRANSACTION_UNIT_BASES);
        testOneDoc(BLOCK_WITH_WRONG_UD_SOURCE);
        testOneDoc(BLOCK_WITH_WRONG_TX_SOURCE);
        testOneDoc(BLOCK_WITH_UNAVAILABLE_UD_SOURCE);
        testOneDoc(BLOCK_WITH_UNAVAILABLE_TX_SOURCE);
        testOneDoc(TRANSACTION_WITHOUT_ISSUERS);
        testOneDoc(TRANSACTION_WITHOUT_SOURCES);
        testOneDoc(TRANSACTION_WITHOUT_RECIPIENT);
        testOneDoc(TRANSACTION_WITH_DUPLICATED_SOURCE_SINGLE_TX);


        testOneDoc(TRANSACTION_WITH_EMPTY_TX_CONDITIONS);
        testOneDoc(TRANSACTION_WRONG_TOTAL);
        testOneDoc(TRANSACTION_V3_GOOD_AMOUNTS);
        testOneDoc(TRANSACTION_TOO_LONG);
        testOneDoc(OUTPUT_TOO_LONG);
        testOneDoc(UNLOCK_TOO_LONG);
        testOneDoc(TRANSACTION_WRONG_TRANSFORM);
        testOneDoc(TRANSACTION_WRONG_TRANSFORM_LOW_BASE);
        testOneDoc(BLOCK_TX_V3_TOO_HIGH_OUTPUT_BASE);
        testOneDoc(TRANSACTION_WITH_DUPLICATED_SOURCE_MULTIPLE_TX);

        testOneDoc(TRANSACTION_WITH_WRONG_SIGNATURES);
        testOneDoc(CERT_BASED_ON_NON_ZERO_FOR_ROOT);
        testOneDoc(CERT_BASED_ON_NON_EXISTING_BLOCK);
        testOneDoc(REVOKED_WITH_MEMBERSHIPS);
        testOneDoc(REVOKED_WITH_DUPLICATES);
        testOneDoc(REVOKED_NOT_IN_EXCLUDED);
        testOneDoc(BLOCK_UNKNOWN_REVOKED);
        testOneDoc(BLOCK_WITH_YET_REVOKED);
        testOneDoc(BLOCK_WITH_WRONG_REVOCATION_SIG);


    }

    private void testOneDoc(String docString) {
        var doc = juniterParser(CharStreams.fromString(docString)).doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    private void testFailDoc(String docString) {
        var doc = juniterParser(CharStreams.fromString(docString)).doc();
        assert doc == null : "Doc should be null";
        LOG.info(doc);
    }


    @Test
    public void WRONG_SIGNATURE() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_SIGNATURE));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void VALID_ROOT() {
        final var parser = juniterParser(CharStreams.fromString(VALID_ROOT));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void WRONG_PROOF_OF_WORK() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_PROOF_OF_WORK));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void ROOT_WITHOUT_PARAMETERS() {
        final var parser = juniterParser(CharStreams.fromString(ROOT_WITHOUT_PARAMETERS));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void NON_ROOT_WITH_PARAMETERS() {
        final var parser = juniterParser(CharStreams.fromString(NON_ROOT_WITH_PARAMETERS));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void ROOT_WITH_PREVIOUS_HASH() {
        final var parser = juniterParser(CharStreams.fromString(ROOT_WITH_PREVIOUS_HASH));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void ROOT_WITH_PREVIOUS_ISSUER() {
        final var parser = juniterParser(CharStreams.fromString(ROOT_WITH_PREVIOUS_ISSUER));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void NON_ROOT_WITHOUT_PREVIOUS_HASH() {
        final var parser = juniterParser(CharStreams.fromString(NON_ROOT_WITHOUT_PREVIOUS_HASH));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void NON_ROOT_WITHOUT_PREVIOUS_ISSUER() {
        final var parser = juniterParser(CharStreams.fromString(NON_ROOT_WITHOUT_PREVIOUS_ISSUER));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test(expected = Exception.class)
    public void COLLIDING_UIDS() {
        final var parser = juniterParser(CharStreams.fromString(COLLIDING_UIDS));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test(expected = IllegalStateException.class)
    public void COLLIDING_PUBKEYS() {
        final var parser = juniterParser(CharStreams.fromString(COLLIDING_PUBKEYS));

        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void WRONG_DATE_LOWER() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_DATE_LOWER));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void WRONG_DATE_HIGHER_BUT_TOO_FEW() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_DATE_HIGHER_BUT_TOO_FEW));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void WRONG_DATE_HIGHER_BUT_TOO_HIGH() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_DATE_HIGHER_BUT_TOO_HIGH));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void WRONG_ROOT_TIMES() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_ROOT_TIMES));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void GOOD_DATE_HIGHER() {
        final var parser = juniterParser(CharStreams.fromString(GOOD_DATE_HIGHER));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }


    @Test
    public void WRONG_IDTY_MATCH_JOINS() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_IDTY_MATCH_JOINS));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }


    @Test
    public void MULTIPLE_JOINERS() {
        final var parser = juniterParser(CharStreams.fromString(MULTIPLE_JOINERS));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void MULTIPLE_ACTIVES() {
        final var parser = juniterParser(CharStreams.fromString(MULTIPLE_ACTIVES));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }


    @Test
    public void MULTIPLE_LEAVES() {
        final var parser = juniterParser(CharStreams.fromString(MULTIPLE_LEAVES));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void MULTIPLE_EXCLUDED() {
        final var parser = juniterParser(CharStreams.fromString(MULTIPLE_EXCLUDED));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void MULTIPLE_OVER_ALL() {
        final var parser = juniterParser(CharStreams.fromString(MULTIPLE_OVER_ALL));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }


    @Test
    public void MULTIPLE_CERTIFICATIONS_FROM_SAME_ISSUER() {
        final var parser = juniterParser(CharStreams.fromString(MULTIPLE_CERTIFICATIONS_FROM_SAME_ISSUER));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void IDENTICAL_CERTIFICATIONS() {
        final var parser = juniterParser(CharStreams.fromString(IDENTICAL_CERTIFICATIONS));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void LEAVER_WITH_CERTIFICATIONS() {
        final var parser = juniterParser(CharStreams.fromString(LEAVER_WITH_CERTIFICATIONS));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void EXCLUDED_WITH_CERTIFICATIONS() {
        final var parser = juniterParser(CharStreams.fromString(EXCLUDED_WITH_CERTIFICATIONS));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }


}
