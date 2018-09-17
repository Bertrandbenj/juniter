package juniter.utils;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import juniter.model.persistence.Block;

public class LocalValidation implements LocalValid {

	Function<Object, Boolean> isV10 = (x) -> {
		if (x == null)
			return false;
		if (x instanceof Block)
			return isV10(((Block) x).getVersion());

		return isV10(x);
	};

	Predicate<Object> predV10 = (x) -> isV10.apply(x);

	Function<Stream<Object>, Boolean> areVersion10 = (stream) -> {
		return stream.map(bl -> bl).allMatch(predV10);
	};

}
