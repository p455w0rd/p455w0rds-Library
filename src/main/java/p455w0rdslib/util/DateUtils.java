package p455w0rdslib.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author p455w0rd
 *
 */
public class DateUtils {

	public static int getMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(2) + 1;
	}

	public static int getDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(5);
	}

	public static boolean isXmas() {
		return getMonth() == 12 && getDay() == 25;
	}

	public static boolean isXmasEve() {
		return getMonth() == 12 && getDay() == 24;
	}
}
