package common;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class Logger {
	
	private static final org.apache.logging.log4j.Logger logger = LogManager.getRootLogger();
	
	public static void log(Level level, String message) {
		logger.log(level, message);
	}
	
	public static void log(String message) {
		log(Level.INFO, message);
	}
	
}