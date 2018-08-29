package juniter.utils;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import juniter.model.persistence.Block;

public class LocalValidation {

	Function<Object, Boolean> isV10 = (x) -> {
		if (x == null)
			return false;
		if (x instanceof Block)
			return ((Block) x).getVersion() == 10;
		if (x instanceof String)
			return "10".equals(x);
		if (x instanceof Integer || x instanceof Short)
			return (Integer) x == 10;
		return false;
	};

	Predicate<Object> predV10 = (x) -> isV10.apply(x);

	Function<Stream<Object>, Boolean> areVersion10 = (stream) -> {
		return stream.map(bl -> bl).allMatch(predV10);
	};

}
