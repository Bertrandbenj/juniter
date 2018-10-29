package juniter.core.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils {
	public static String format(long millis) {
		return String.format("%d min, %d sec - %d ms", //
				TimeUnit.MILLISECONDS.toMinutes(millis), //
				TimeUnit.MILLISECONDS.toSeconds(millis)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
				millis);
	}
}
