package juniter.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import juniter.core.model.tx.OutCondition;
import juniter.core.model.tx.OutCondition.And;
import juniter.core.model.tx.OutCondition.Or;
import juniter.core.model.tx.OutCondition.SIG;
import juniter.core.model.tx.OutCondition.XHX;

public class TestOutCondition {
	private static final Logger log = LogManager.getLogger();

	public final String _singleSIG = "SIG(HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY)";
	public final String _singleXHX = "XHX(8AFC8DF633FC158F9DB4864ABED696C1AA0FE5D617A7B5F7AB8DE7CA2EFCD4CB)";
	public final String _SIGandXHX = "(" + _singleSIG + " && " + _singleXHX + ")";
	public final String _XHXandSIG = "(" + _singleXHX + " && " + _singleSIG + ")";
	public final String _SIGorXHX = "(" + _singleSIG + " || " + _singleXHX + ")";
	public final String _andORand = "(" + _SIGandXHX + " || " + _XHXandSIG + ")";

	public SIG _sig;
	public XHX _xhx;
	public And _and, _and2;
	public Or _or;

	@Before
	public void init() throws Exception {
		_sig = new SIG("HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY");
		_xhx = new XHX("8AFC8DF633FC158F9DB4864ABED696C1AA0FE5D617A7B5F7AB8DE7CA2EFCD4CB");
		_and = new And(_sig, _xhx);
		_and2 = new And(_xhx, _sig);
		_or = new Or(_and, _and2);
	}

	@Test
	public void testAND() throws Exception {
		OutCondition out = null;

		out = OutCondition.parse(_SIGandXHX);
		assertThat(out).isNotNull();
		assertThat(out).isInstanceOf(And.class);
		assertThat(out).isEqualTo(_and);

	}

	@Test
	public void testAndOrAnd() throws Exception {
		OutCondition out = null;

		out = OutCondition.parse(_andORand);
		assertThat(out).isNotNull();
		assertThat(out).isInstanceOf(Or.class);
		assertThat(out).isEqualTo(_or);

	}

	@Test
	public void testSIG() throws Exception {
		OutCondition out = null;

		out = OutCondition.parse(_singleSIG);
		assertThat(out).isNotNull();
		assertThat(out).isInstanceOf(SIG.class);
		assertThat(out).isEqualTo(_sig);

	}

	@Test
	public void testXHX() throws Exception {
		OutCondition out = null;

		out = OutCondition.parse(_singleXHX);
		assertThat(out).isNotNull();
		assertThat(out).isInstanceOf(XHX.class);
		assertThat(out).isEqualTo(_xhx);

	}
}
