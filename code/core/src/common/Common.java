package common;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Common {

	public static final DateTimeFormatter FORMAT_DATE_TIME_TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	public static final DecimalFormat FORMAT_NUMBER_WHOLE_WITH_COMMAS = new DecimalFormat("#,###");
	
	/**
	 * Format exception including the entire stack trace. For example, Exception: java.lang.NullPointerException at core.base.Test2.testMethod2(Test2.java:7) at core.base.Test1.testMethod1(Test1.java:6) at core.base.CacheTest.main(CacheTest.java:14) 
	 */
	public static String formatException(Throwable t) {
		StringBuffer sb = new StringBuffer();
		sb.append(t.toString() + " at ");
		StackTraceElement [] ste = t.getStackTrace();
		for (int i = 0; i < ste.length; i++) {
			if (i > 0) {
				sb.append(" at "); 
			}
			sb.append(ste[i]);
		}
		return sb.toString();
	}
	
	/**
	 * Format LocalDateTime to YYYY-MM-DD HH:MM:SS.HHH. For example, 2017-12-03 13:22:59.869 
	 */
	public static String formatLocalDateTimeToYYYYMMDDHHMMSSHHH(LocalDateTime ldt) {
		return ldt != null ? FORMAT_DATE_TIME_TIMESTAMP.format(LocalDateTime.now()) : null;		
	}
	
}