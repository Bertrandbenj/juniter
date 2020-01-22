package juniter.grammar.translate;

import generated.antlr.JuniterParser;
import generated.antlr.JuniterParserBaseVisitor;
import juniter.core.model.meta.DUPDocument;

public class Translator extends JuniterParserBaseVisitor<DUPDocument> {

	public enum LANG {
		JSON, YAML, DUP
    }

	LANG in, out;

	public Translator(LANG input, LANG output) {
		in = input;
		out = output;
	}


	@Override
	public DUPDocument visitCertification(JuniterParser.CertificationContext ctx) {

		return super.visitCertification(ctx);
	}
}
