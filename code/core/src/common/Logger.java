package common;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class Logger {
	
	private static final org.apache.logging.log4j.Logger logger = LogManager.getRootLogger();
	
	public static void log(Level level, String message) {
		logger.log(level, message);
	}

	public static void log(String message) {
		logger.log(Level.INFO, message);
	}
	
	public static void log(Throwable e) {
		if (e instanceof Exception) {
			Logger.log(Level.ERROR, "Exception: " + Common.formatException(e));
		} else if (e instanceof Error) {
			Logger.log(Level.ERROR, "Error: " + Common.formatException(e));
		} else if (e instanceof Throwable) {
			Logger.log(Level.ERROR, "Throwable: " + Common.formatException(e));
		}
	}
	
	public static void logBegin() {
		log(Thread.currentThread().getStackTrace()[2].getMethodName() + " Begin"); 
	}
	
	public static void logEnd() {
		log(Thread.currentThread().getStackTrace()[2].getMethodName() + " End"); 
	}

}