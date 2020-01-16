package juniter.grammar.translate;

import generated.antlr.JuniterParser;
import generated.antlr.JuniterParserBaseVisitor;
import juniter.grammar.Document;

public class Translator extends JuniterParserBaseVisitor<Document> {

	public enum LANG {
		JSON, YAML, DUP
    }

	LANG in, out;

	public Translator(LANG input, LANG output) {
		in = input;
		out = output;
	}


	@Override
	public Document visitCertification(JuniterParser.CertificationContext ctx) {

		return super.visitCertification(ctx);
	}
}
