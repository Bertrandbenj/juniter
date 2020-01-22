package juniter.model;

import juniter.core.model.dbo.tx.AND;
import juniter.core.model.dbo.tx.OR;
import juniter.core.model.dbo.tx.SIG;
import juniter.core.model.dbo.tx.XHX;
import juniter.core.model.meta.DUPOutCondition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestOutCondition {
	private static final Logger log = LogManager.getLogger(TestOutCondition.class);

	public final String _singleSIG = "SIG(HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY)";
	public final String _singleXHX = "XHX(8AFC8DF633FC158F9DB4864ABED696C1AA0FE5D617A7B5F7AB8DE7CA2EFCD4CB)";
	public final String _SIGandXHX = "(" + _singleSIG + " && " + _singleXHX + ")";
	public final String _XHXandSIG = "(" + _singleXHX + " && " + _singleSIG + ")";
	public final String _SIGorXHX = "(" + _singleSIG + " || " + _singleXHX + ")";
	public final String _andORand = "(" + _SIGandXHX + " || " + _XHXandSIG + ")";

	public SIG _sig;
	public XHX _xhx;
	public AND _and, _and2;
	public OR _or;

	@Before
	public void init() throws Exception {
		_sig = new SIG("HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY");
		_xhx = new XHX("8AFC8DF633FC158F9DB4864ABED696C1AA0FE5D617A7B5F7AB8DE7CA2EFCD4CB");
		_and = new AND(_sig, _xhx);
		_and2 = new AND(_xhx, _sig);
		_or = new OR(_and, _and2);
	}

	@Test
	public void testAND() throws Exception {
		DUPOutCondition out = null;

		out = DUPOutCondition.parse(_SIGandXHX);
		assertThat(out).isNotNull();
		assertThat(out).isInstanceOf(AND.class);
		assertThat(out).isEqualTo(_and);

	}

	@Test
	public void testAndOrAnd() throws Exception {
		DUPOutCondition out = null;

		out = DUPOutCondition.parse(_andORand);
		assertThat(out).isNotNull();
		assertThat(out).isInstanceOf(OR.class);
		assertThat(out).isEqualTo(_or);

	}

	@Test
	public void testSIG() throws Exception {
		DUPOutCondition out = null;

		out = DUPOutCondition.parse(_singleSIG);
		assertThat(out).isNotNull();
		assertThat(out).isInstanceOf(SIG.class);
		assertThat(out).isEqualTo(_sig);

	}

	@Test
	public void testXHX() throws Exception {
		DUPOutCondition out = null;

		out = DUPOutCondition.parse(_singleXHX);
		assertThat(out).isNotNull();
		assertThat(out).isInstanceOf(XHX.class);
		assertThat(out).isEqualTo(_xhx);

	}
}
