import static org.junit.Assert.assertTrue;

import java.nio.file.Paths;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import juniter.grammar.JuniterListener;
import juniter.grammar.antlr4.JuniterLexer;
import juniter.grammar.antlr4.JuniterParser;

public class AntlrTests {

	private static final Logger logger = LogManager.getLogger();

	private JuniterParser juniterParser(CharStream file) {
		final JuniterLexer l = new JuniterLexer(file);
		final JuniterParser p = new JuniterParser(new CommonTokenStream(l));

		p.addParseListener(new JuniterListener());
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
	public void testBuid() throws Exception {

		final var TEST_BUID = "32-DB30D958EE5CB75186972286ED3F4686B8A1C2CD";
		final var parser = juniterParser(CharStreams.fromString(TEST_BUID));
		final var buidContext = parser.buid();
		assertTrue(TEST_BUID.equals(buidContext.getText()));
		logger.info("buid : " + buidContext.getText());
	}

	@Test
	public void testCertification() throws Exception {
		final var parser = juniterParser(CharStreams.fromFileName("certif_spec.juniter"));
		parser.certificationDoc();
	}

	@Test
	public void testIdentity() throws Exception {
		final var path = Paths.get("/home/ben/ws/juniter/grammar/tests/identity_spec");
		assertTrue("path is null ", path != null);
		assertTrue("pathName is null ", path.getFileName() != null);

		final var doc = "Version: 10\n" //
				+ "Type: Identity\n" //
				+ "Currency: beta_brousouf\n" //
				+ "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" //
				+ "UniqueID: lolcat\n" //
				+ "Timestamp: 32-DB30D958EE5CB75186972286ED3F4686B8A1C2CD\n" //
				+ "J3G9oM5AKYZNLAB5Wx499w61NuUoS57JVccTShUbGpCMjCqj9yXXqNq7dyZpDWA6BxipsiaMZhujMeBfCznzyci\n";

		final var parser = juniterParser(CharStreams.fromPath(path));
		final var idtyContext = parser.identityDoc();
		logger.info(idtyContext.getText() + "\n=======\n" + doc);

		assertTrue("parsed identity different", idtyContext.getText().equals(doc));

		logger.info(path.toString() + "\n" + idtyContext.toStringTree());

	}

	@Test
	public void testIssuer() throws Exception {
		final var parser = juniterParser(CharStreams.fromString("DNann1Lh55eZMEDXeYt59bzHbA3NJR46DeQYCS2qQdLV"));

		final var issuerContext = parser.issuer();

		assertTrue("DNann1Lh55eZMEDXeYt59bzHbA3NJR46DeQYCS2qQdLV"//
				.equals(issuerContext.PUBKEY().getText()));

	}

	@Test
	public void testMembership() throws Exception {
		final var parser = juniterParser(CharStreams.fromFileName("/membership_spec"));
		parser.membershipDoc();
	}

	@Test
	public void testPeer() throws Exception {
		final var parser = juniterParser(CharStreams.fromFileName("/peer_spec"));
		parser.peerDoc();
	}

	@Test
	public void testRevocation() throws Exception {
		final var parser = juniterParser(CharStreams.fromFileName("/revocation_spec"));
		parser.revocationDoc();
	}

	@Test
	public void testSignature() throws Exception {

		final var TEST_SIGN = "J3G9oM5AKYZNLAB5Wx499w61Nu/UoS57+VccTShUbGMjCqj9yXXqNq7dyZpDWA6BxipsiaMZhujMeBfCznzyci";
		final var parser = juniterParser(CharStreams.fromString(TEST_SIGN));
		final var signContext = parser.signature();
		assertTrue(TEST_SIGN.equals(signContext.getText()));
	}

	@Test
	public void testTX() throws Exception {
		final var parser = juniterParser(CharStreams.fromFileName("/tx_spec"));
		parser.transactionDoc();
	}

	@Test
	public void testUniqueID() throws Exception {
		final var parser = juniterParser(CharStreams.fromString("UniqueID: lolcat\n"));
		final var userCtxt = parser.userID();

		assertTrue("lolcat".equals(userCtxt.getText()));

	}

}
