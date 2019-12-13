package juniter.gui.technical;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public interface Formats {

    DecimalFormat DECIMAL_2 = new DecimalFormat("#.##");

    DecimalFormat DECIMAL_4 = new DecimalFormat("#.####");

    DateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
}
