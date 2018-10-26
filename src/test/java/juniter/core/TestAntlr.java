package juniter.core;

import static org.junit.Assert.assertTrue;

import java.nio.file.Paths;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.Interval;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import antlr.main.JuniterLexer;
import antlr.main.JuniterParser;
import antlr.main.JuniterParser.WotContext;
import juniter.core.crypto.CryptoUtils;
import juniter.grammar.JuniterListener;

public class TestAntlr {

	private static final Logger LOG = LogManager.getLogger();

	private static final String folder = "/home/ben/ws/juniter/grammar/tests/";

	JuniterListener visitor = new JuniterListener();

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
	public void testBuid() throws Exception {

		final var TEST_BUID = "32-DB30D958EE5CB75186972286ED3F4686B8A1C2CD";
		final var parser = juniterParser(CharStreams.fromString(TEST_BUID));
		final var buidContext = parser.buid();
		assertTrue(TEST_BUID.equals(buidContext.getText()));
		LOG.info("buid : " + buidContext.getText());
	}

	@Test
	public void testCertification() throws Exception {
		final var parser = juniterParser(CharStreams.fromFileName(folder + "certif.dup"));
		parser.doc();
	}

	@Test
	public void testIdentity() throws Exception {
		final var path = Paths.get(folder + "identity2.dup");
		LOG.info(" testIdentity : " + path.toString());
		assertTrue("path is null ", path != null);
		assertTrue("pathName is null ", path.getFileName() != null);

		final String signature = "L4AiZBYrmriQT3+r/WIpxobEnX2pdPgDg7Cf50vJ74c0GsL1CaEfJ3JmbqwARvzNkiewB3GR77gxFjZDIM0XAg==";
		final var pk = "4tsFXtKofD6jK8zwLymgXWAoMBzhFx7KNANhtK86mKzA";
		final var unSignedDoc = "Version: 10\n" //
				+ "Type: Identity\n" //
				+ "Currency: g1\n" //
				+ "Issuer: " + pk + "\n" //
				+ "UniqueID: AlainLebrun\n" //
				+ "Timestamp: 1184-00000C17FA48A4681377DFA9BF1CE747CE82B869E694EC9E41F9F530C45E8F19\n";
		final var doc = unSignedDoc + signature;// + "\n";

		assertTrue("Assert before parsing", CryptoUtils.verify(unSignedDoc, signature, pk));

		final var parser = juniterParser(CharStreams.fromString(doc));
		final var wot = parser.doc().wot();
		final var idty = wot.identity();
		LOG.info(idty + " " + getRaw(wot));

		assertTrue(idty.version().getText().equals("10"));
		assertTrue(idty.issuer().getText().equals(pk));
		assertTrue(signature.equals(wot.signature().getText()));
		assertTrue(CryptoUtils.verify( //
				getRaw(wot), //
				wot.signature().getText(), //
				idty.issuer().getText()));

		// assertTrue("Assert generated doc is valid",
		// visitor.visit(parser.doc()).isValid());

	}

	@Test
	public void testIssuer() throws Exception {
		final var parser = juniterParser(CharStreams.fromString("DNann1Lh55eZMEDXeYt59bzHbA3NJR46DeQYCS2qQdLV"));

		final var issuerContext = parser.issuer();

		assertTrue("DNann1Lh55eZMEDXeYt59bzHbA3NJR46DeQYCS2qQdLV"//
				.equals(issuerContext.toString()));

	}

	@Test
	public void testListener() throws Exception {
		final var juniterListener = new JuniterListener();

//		final var testFiles = Paths.get(folder).toFile().list();
		final var testFiles = new String[] { folder + "peer.dup", folder + "identity2.dup" };

		for (final String file : testFiles) {
			if (file.endsWith(".dup")) {
				final var parser = juniterParser(CharStreams.fromFileName(file));

				final var doc = juniterListener.visit(parser.doc());
				LOG.info("testListener " + doc.toString());
				assertTrue(doc.isValid());
			}
		}

	}

	@Test
	public void testMembership() throws Exception {
		final var parser = juniterParser(CharStreams.fromFileName(folder + "membership.dup"));
		parser.member();
	}

	@Test
	public void testPeer() throws Exception {
		final var parser = juniterParser(CharStreams.fromFileName(folder + "peer.dup"));
		parser.doc();
	}

	@Test
	public void testRevocation() throws Exception {
		final var parser = juniterParser(CharStreams.fromFileName(folder + "revocation.dup"));
		parser.doc();
	}

}
