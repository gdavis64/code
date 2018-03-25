package common;

public class Common {
	
	public static String formatException(Throwable t) {
		StringBuffer sb = new StringBuffer();
		sb.append(t.toString() + " at ");
		StackTraceElement [] ste = t.getStackTrace();
		for (int i = 0; i < ste.length; i++) {
			if (i > 0) {
				sb.append(" "); 
			}
			sb.append(ste[i]);
		}
		return sb.toString();
	}
	
}