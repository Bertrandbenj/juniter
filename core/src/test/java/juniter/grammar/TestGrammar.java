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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestGrammar implements BlockExamples {

    private static final Logger LOG = LogManager.getLogger(TestGrammar.class);


    JuniterGrammar visitor = new JuniterGrammar();


    private ClassLoader classLoader = getClass().getClassLoader();

    private Path documents = new File(classLoader.getResource("documents/").getFile()).toPath();


    private String getRaw(WotContext idtc) {
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

        assertEquals("10", idty.version().getText());
        assertEquals(idty.issuer().getText(), pk);
        assertEquals(signature, wot.signature().getText());
        assertTrue(Crypto.verify(getRaw(wot), wot.signature().getText(), idty.issuer().getText()));

        // assertTrue("Assert generated Doc is valid",
        // visitor.visit(parser.Doc()).isValid());

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
    public void WRONG_SIGNATURE() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_SIGNATURE));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
    public void WRONGLY_SIGNED_JOIN() {
        final var parser = juniterParser(CharStreams.fromString(WRONGLY_SIGNED_JOIN));
        parser.doc();
    }


    @Test
    public void WRONGLY_SIGNED_ACTIVE() {
        final var parser = juniterParser(CharStreams.fromString(WRONGLY_SIGNED_ACTIVE));
        parser.doc();
    }


    @Test
    public void WRONGLY_SIGNED_LEAVE() {
        final var parser = juniterParser(CharStreams.fromString(WRONGLY_SIGNED_LEAVE));
        parser.doc();
    }

    @Test
    public void CORRECTLY_SIGNED_LEAVE() {
        final var parser = juniterParser(CharStreams.fromString(CORRECTLY_SIGNED_LEAVE));
        parser.doc();
    }

    @Test
    public void WRONGLY_SIGNED_CERTIFICATION() {
        final var parser = juniterParser(CharStreams.fromString(WRONGLY_SIGNED_CERTIFICATION));
        parser.doc();
    }

    @Test
    public void UNKNOWN_CERTIFIER() {
        final var parser = juniterParser(CharStreams.fromString(UNKNOWN_CERTIFIER));
        parser.doc();
    }

    @Test
    public void UNKNOWN_CERTIFIED() {
        final var parser = juniterParser(CharStreams.fromString(UNKNOWN_CERTIFIED));
        parser.doc();
    }

    @Test
    public void EXISTING_UID() {
        final var parser = juniterParser(CharStreams.fromString(EXISTING_UID));
        parser.doc();
    }

    @Test
    public void EXISTING_PUBKEY() {
        final var parser = juniterParser(CharStreams.fromString(EXISTING_PUBKEY));
        parser.doc();
    }

    @Test
    public void TOO_EARLY_CERTIFICATION_REPLAY() {
        final var parser = juniterParser(CharStreams.fromString(TOO_EARLY_CERTIFICATION_REPLAY));
        parser.doc();
    }

    @Test
    public void EXPIRED_CERTIFICATIONS() {
        final var parser = juniterParser(CharStreams.fromString(EXPIRED_CERTIFICATIONS));
        parser.doc();
    }

    @Test
    public void EXPIRED_MEMBERSHIP() {
        final var parser = juniterParser(CharStreams.fromString(EXPIRED_MEMBERSHIP));
        parser.doc();
    }

    @Test
    public void REVOKED_JOINER() {
        final var parser = juniterParser(CharStreams.fromString(REVOKED_JOINER));
        parser.doc();
    }

    @Test
    public void NOT_ENOUGH_CERTIFICATIONS_JOINER() {
        final var parser = juniterParser(CharStreams.fromString(NOT_ENOUGH_CERTIFICATIONS_JOINER));
        parser.doc();
    }

    @Test
    public void NOT_ENOUGH_CERTIFICATIONS_JOINER_BLOCK_0() {
        final var parser = juniterParser(CharStreams.fromString(NOT_ENOUGH_CERTIFICATIONS_JOINER_BLOCK_0));
        parser.doc();
    }

    @Test
    public void OUTDISTANCED_JOINER() {
        final var parser = juniterParser(CharStreams.fromString(OUTDISTANCED_JOINER));
        parser.doc();
    }

    @Test
    public void VALID_NEXT() {
        final var parser = juniterParser(CharStreams.fromString(VALID_NEXT));
        parser.doc();
    }

    @Test
    public void WRONG_PREVIOUS_HASH() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_PREVIOUS_HASH));
        parser.doc();
    }

    @Test
    public void WRONG_PREVIOUS_ISSUER() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_PREVIOUS_ISSUER));
        parser.doc();
    }

    @Test
    public void WRONG_DIFFERENT_ISSUERS_COUNT_FOLLOWING_V2() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_DIFFERENT_ISSUERS_COUNT_FOLLOWING_V2));
        parser.doc();
    }

    @Test
    public void WRONG_DIFFERENT_ISSUERS_COUNT_FOLLOWING_V3() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_DIFFERENT_ISSUERS_COUNT_FOLLOWING_V3));
        parser.doc();
    }

    @Test
    public void WRONG_ISSUERS_FRAME_FOLLOWING_V2() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_ISSUERS_FRAME_FOLLOWING_V2));
        parser.doc();
    }

    @Test
    public void WRONG_ISSUERS_FRAME_FOLLOWING_V3() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_ISSUERS_FRAME_FOLLOWING_V3));
        parser.doc();
    }

    @Test
    public void WRONG_ISSUER() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_ISSUER));
        parser.doc();
    }

    @Test
    public void WRONG_JOIN_BLOCK_TARGET_ROOT() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_JOIN_BLOCK_TARGET_ROOT));
        parser.doc();
    }

    @Test
    public void WRONG_JOIN_ROOT_NUMBER() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_JOIN_ROOT_NUMBER));
        parser.doc();
    }

    @Test
    public void WRONG_JOIN_ROOT_HASH() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_JOIN_ROOT_HASH));
        parser.doc();
    }

    @Test
    public void WRONG_JOIN_NUMBER_TOO_LOW() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_JOIN_NUMBER_TOO_LOW));
        parser.doc();
    }

    @Test
    public void WRONG_JOIN_BLOCK_TARGET() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_JOIN_BLOCK_TARGET));
        parser.doc();
    }

    @Test
    public void WRONG_JOIN_ALREADY_MEMBER() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_JOIN_ALREADY_MEMBER));
        parser.doc();
    }

    @Test
    public void WRONG_ACTIVE_BLOCK_TARGET() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_ACTIVE_BLOCK_TARGET));
        parser.doc();
    }

    @Test
    public void KICKED_NOT_EXCLUDED() {
        final var parser = juniterParser(CharStreams.fromString(KICKED_NOT_EXCLUDED));
        parser.doc();
    }

    @Test
    public void KICKED_EXCLUDED() {
        final var parser = juniterParser(CharStreams.fromString(KICKED_EXCLUDED));
        parser.doc();
    }

    @Test
    public void WRONG_MEMBERS_COUNT() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_MEMBERS_COUNT));
        parser.doc();
    }

    @Test
    public void NO_LEADING_ZERO() {
        final var parser = juniterParser(CharStreams.fromString(NO_LEADING_ZERO));
        parser.doc();
    }

    @Test
    public void REQUIRES_4_LEADING_ZEROS() {
        final var parser = juniterParser(CharStreams.fromString(REQUIRES_4_LEADING_ZEROS));
        parser.doc();
    }

    @Test
    public void REQUIRES_7_LEADING_ZEROS() {
        final var parser = juniterParser(CharStreams.fromString(REQUIRES_7_LEADING_ZEROS));
        parser.doc();
    }

    @Test
    public void REQUIRES_6_LEADING_ZEROS() {
        final var parser = juniterParser(CharStreams.fromString(REQUIRES_6_LEADING_ZEROS));
        parser.doc();
    }

    @Test
    public void REQUIRES_5_LEADING_ZEROS() {
        final var parser = juniterParser(CharStreams.fromString(REQUIRES_5_LEADING_ZEROS));
        parser.doc();
    }

    @Test
    public void REQUIRES_7_LEADING_ZEROS_AGAIN() {
        final var parser = juniterParser(CharStreams.fromString(REQUIRES_7_LEADING_ZEROS_AGAIN));
        parser.doc();
    }

    @Test
    public void FIRST_BLOCK_OF_NEWCOMER() {
        final var parser = juniterParser(CharStreams.fromString(FIRST_BLOCK_OF_NEWCOMER));
        parser.doc();
    }

    @Test
    public void SECOND_BLOCK_OF_NEWCOMER() {
        final var parser = juniterParser(CharStreams.fromString(SECOND_BLOCK_OF_NEWCOMER));
        parser.doc();
    }

    @Test
    public void WRONG_ROOT_DATES() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_ROOT_DATES));
        parser.doc();
    }

    @Test
    public void WRONG_MEDIAN_TIME_ODD() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_MEDIAN_TIME_ODD));
        parser.doc();
    }

    @Test
    public void WRONG_MEDIAN_TIME_EVEN() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_MEDIAN_TIME_EVEN));
        parser.doc();
    }

    @Test
    public void GOOD_MEDIAN_TIME_ODD() {
        final var parser = juniterParser(CharStreams.fromString(GOOD_MEDIAN_TIME_ODD));
        parser.doc();
    }

    @Test
    public void GOOD_MEDIAN_TIME_EVEN() {
        final var parser = juniterParser(CharStreams.fromString(GOOD_MEDIAN_TIME_EVEN));
        parser.doc();
    }

    @Test
    public void WRONG_CONFIRMED_DATE_MUST_CONFIRM() {
        final var parser = juniterParser(CharStreams.fromString(WRONG_CONFIRMED_DATE_MUST_CONFIRM));
        parser.doc();
    }

    @Test
    public void ROOT_BLOCK_WITH_UD() {
        final var parser = juniterParser(CharStreams.fromString(ROOT_BLOCK_WITH_UD));
        parser.doc();
    }

    @Test
    public void UD_BLOCK_WIHTOUT_UD() {
        final var parser = juniterParser(CharStreams.fromString(UD_BLOCK_WIHTOUT_UD));
        parser.doc();
    }

    @Test
    public void UD_BLOCK_WIHTOUT_BASE() {
        final var parser = juniterParser(CharStreams.fromString(UD_BLOCK_WIHTOUT_BASE));
        parser.doc();
    }

    @Test
    public void V3_ROOT_BLOCK_NOBASE() {
        final var parser = juniterParser(CharStreams.fromString(V3_ROOT_BLOCK_NOBASE));
        parser.doc();
    }

    @Test
    public void V3_ROOT_BLOCK_POSITIVE_BASE() {
        final var parser = juniterParser(CharStreams.fromString(V3_ROOT_BLOCK_POSITIVE_BASE));
        parser.doc();
    }

    @Test
    public void BLOCK_WITH_WRONG_UD() {
        final var parser = juniterParser(CharStreams.fromString(BLOCK_WITH_WRONG_UD));
        parser.doc();
    }

    @Test
    public void BLOCK_WITH_WRONG_UD_V3() {
        final var parser = juniterParser(CharStreams.fromString(BLOCK_WITH_WRONG_UD_V3));
        parser.doc();
    }

    @Test
    public void BLOCK_WITH_WRONG_UNIT_BASE() {
        final var parser = juniterParser(CharStreams.fromString(BLOCK_WITH_WRONG_UNIT_BASE));
        parser.doc();
    }

    @Test
    public void BLOCK_WITH_WRONG_UNIT_BASE_NO_UD() {
        final var parser = juniterParser(CharStreams.fromString(BLOCK_WITH_WRONG_UNIT_BASE_NO_UD));
        parser.doc();
    }

    @Test
    public void BLOCK_UNLEGITIMATE_UD() {
        final var parser = juniterParser(CharStreams.fromString(BLOCK_UNLEGITIMATE_UD));
        parser.doc();
    }

    @Test
    public void BLOCK_UNLEGITIMATE_UD_2() {
        final var parser = juniterParser(CharStreams.fromString(BLOCK_UNLEGITIMATE_UD_2));
        parser.doc();
    }

    @Test
    public void FIRST_UD_BLOCK_WITH_UD_THAT_SHOULDNT() {
        final var parser = juniterParser(CharStreams.fromString(FIRST_UD_BLOCK_WITH_UD_THAT_SHOULDNT));
        parser.doc();
    }

    @Test
    public void FIRST_UD_BLOCK_WITH_UD_THAT_SHOULD() {
        final var parser = juniterParser(CharStreams.fromString(FIRST_UD_BLOCK_WITH_UD_THAT_SHOULD));
        parser.doc();
    }

    @Test
    public void BLOCK_WITHOUT_TRANSACTIONS() {
        final var parser = juniterParser(CharStreams.fromString(BLOCK_WITHOUT_TRANSACTIONS));
        parser.doc();
    }

    @Test
    public void BLOCK_WITH_GOOD_TRANSACTIONS() {
        final var parser = juniterParser(CharStreams.fromString(BLOCK_WITH_GOOD_TRANSACTIONS));
        parser.doc();
    }

    @Test
    public void BLOCK_WITH_WRONG_TRANSACTION_SUMS() {
        final var parser = juniterParser(CharStreams.fromString(BLOCK_WITH_WRONG_TRANSACTION_SUMS));
        parser.doc();
    }

    @Test
    public void BLOCK_WITH_WRONG_TRANSACTION_UNIT_BASES() {
        final var parser = juniterParser(CharStreams.fromString(BLOCK_WITH_WRONG_TRANSACTION_UNIT_BASES));
        parser.doc();
    }

    @Test
    public void BLOCK_WITH_WRONG_UD_SOURCE() {
        final var parser = juniterParser(CharStreams.fromString(BLOCK_WITH_WRONG_UD_SOURCE));
        parser.doc();
    }

    @Test
    public void BLOCK_WITH_WRONG_TX_SOURCE() {
        final var parser = juniterParser(CharStreams.fromString(BLOCK_WITH_WRONG_TX_SOURCE));
        parser.doc();
    }

    @Test
    public void BLOCK_WITH_UNAVAILABLE_UD_SOURCE() {
        final var parser = juniterParser(CharStreams.fromString(BLOCK_WITH_UNAVAILABLE_UD_SOURCE));
        parser.doc();
    }

    @Test
    public void BLOCK_WITH_UNAVAILABLE_TX_SOURCE() {
        final var parser = juniterParser(CharStreams.fromString(BLOCK_WITH_UNAVAILABLE_TX_SOURCE));
        parser.doc();
    }

    @Test
    public void TRANSACTION_WITHOUT_ISSUERS() {
        final var parser = juniterParser(CharStreams.fromString(TRANSACTION_WITHOUT_ISSUERS));
        parser.doc();
    }

    @Test
    public void TRANSACTION_WITHOUT_SOURCES() {
        final var parser = juniterParser(CharStreams.fromString(TRANSACTION_WITHOUT_SOURCES));
        parser.doc();
    }

    @Test
    public void TRANSACTION_WITHOUT_RECIPIENT() {
        final var parser = juniterParser(CharStreams.fromString(TRANSACTION_WITHOUT_RECIPIENT));
        parser.doc();
    }

    @Test
    public void TRANSACTION_WITH_DUPLICATED_SOURCE_SINGLE_TX() {
        final var parser = juniterParser(CharStreams.fromString(TRANSACTION_WITH_DUPLICATED_SOURCE_SINGLE_TX));
        parser.doc();
    }

    @Test
    public void TRANSACTION_WITH_EMPTY_TX_CONDITIONS() {
        final var parser = juniterParser(CharStreams.fromString(TRANSACTION_WITH_EMPTY_TX_CONDITIONS));
        parser.doc();
    }

    @Test
    public void WRONGLY_SIGNED_IDENTITIES() {
        final var parser = juniterParser(CharStreams.fromString(WRONGLY_SIGNED_IDENTITIES));
        parser.doc();
    }

    @Test
    public void TRANSACTION_WRONG_TOTAL() {
        final var parser = juniterParser(CharStreams.fromString(TRANSACTION_WRONG_TOTAL));
        parser.doc();
    }

    @Test
    public void TRANSACTION_V3_GOOD_AMOUNTS() {
        final var parser = juniterParser(CharStreams.fromString(TRANSACTION_V3_GOOD_AMOUNTS));
        parser.doc();
    }

    @Test
    public void TRANSACTION_TOO_LONG() {
        final var parser = juniterParser(CharStreams.fromString(TRANSACTION_TOO_LONG));
        parser.doc();
    }

    @Test
    public void OUTPUT_TOO_LONG() {
        final var parser = juniterParser(CharStreams.fromString(OUTPUT_TOO_LONG));
        parser.doc();
    }

    @Test
    public void UNLOCK_TOO_LONG() {
        final var parser = juniterParser(CharStreams.fromString(UNLOCK_TOO_LONG));
        parser.doc();
    }

    @Test
    public void TRANSACTION_WRONG_TRANSFORM() {
        final var parser = juniterParser(CharStreams.fromString(TRANSACTION_WRONG_TRANSFORM));
        parser.doc();
    }

    @Test
    public void TRANSACTION_WRONG_TRANSFORM_LOW_BASE() {
        final var parser = juniterParser(CharStreams.fromString(TRANSACTION_WRONG_TRANSFORM_LOW_BASE));
        parser.doc();
    }

    @Test
    public void BLOCK_TX_V3_TOO_HIGH_OUTPUT_BASE() {
        final var parser = juniterParser(CharStreams.fromString(BLOCK_TX_V3_TOO_HIGH_OUTPUT_BASE));
        parser.doc();
    }

    @Test
    public void TRANSACTION_WITH_DUPLICATED_SOURCE_MULTIPLE_TX() {
        final var parser = juniterParser(CharStreams.fromString(TRANSACTION_WITH_DUPLICATED_SOURCE_MULTIPLE_TX));
        parser.doc();
    }

    @Test
    public void TRANSACTION_WITH_WRONG_SIGNATURES() {
        final var parser = juniterParser(CharStreams.fromString(TRANSACTION_WITH_WRONG_SIGNATURES));
        parser.doc();
    }

    @Test
    public void CERT_BASED_ON_NON_ZERO_FOR_ROOT() {
        final var parser = juniterParser(CharStreams.fromString(CERT_BASED_ON_NON_ZERO_FOR_ROOT));
        parser.doc();
    }

    @Test
    public void CERT_BASED_ON_NON_EXISTING_BLOCK() {
        final var parser = juniterParser(CharStreams.fromString(CERT_BASED_ON_NON_EXISTING_BLOCK));
        parser.doc();
    }

    @Test
    public void REVOKED_WITH_MEMBERSHIPS() {
        final var parser = juniterParser(CharStreams.fromString(REVOKED_WITH_MEMBERSHIPS));
        parser.doc();
    }

    @Test
    public void REVOKED_WITH_DUPLICATES() {
        final var parser = juniterParser(CharStreams.fromString(REVOKED_WITH_DUPLICATES));
        parser.doc();
    }

    @Test
    public void REVOKED_NOT_IN_EXCLUDED() {
        final var parser = juniterParser(CharStreams.fromString(REVOKED_NOT_IN_EXCLUDED));
        parser.doc();
    }

    @Test
    public void BLOCK_UNKNOWN_REVOKED() {
        final var parser = juniterParser(CharStreams.fromString(BLOCK_UNKNOWN_REVOKED));
        parser.doc();
    }

    @Test
    public void BLOCK_WITH_YET_REVOKED() {
        final var parser = juniterParser(CharStreams.fromString(BLOCK_WITH_YET_REVOKED));
        parser.doc();
    }

    @Test
    public void BLOCK_WITH_WRONG_REVOCATION_SIG() {
        final var parser = juniterParser(CharStreams.fromString(BLOCK_WITH_WRONG_REVOCATION_SIG));
        parser.doc();
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

    @Test
    public void COLLIDING_UIDS() {
        final var parser = juniterParser(CharStreams.fromString(COLLIDING_UIDS));
        var doc = parser.doc();
        assert doc != null : "Doc is null";
        LOG.info(doc);
    }

    @Test
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
