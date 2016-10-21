package ch.danielsuter.gridcalculator.util;

import java.text.MessageFormat;

import org.apache.log4j.Logger;

public class MyLogger {
	private final Logger logger;

	private MyLogger(Logger logger) {
		this.logger = logger;
	}
	
	public static MyLogger getLogger(Class<?> classToLog) {
		return new MyLogger(Logger.getLogger(classToLog));
	}
	
	public void debug(Object message) {
		logger.debug(message);
	}
	
	public void debug(String message, Object... arguments) {
		logger.debug(MessageFormat.format(message, arguments));
	}

	public void error(Object message, Throwable t) {
		logger.error(message, t);
	}

	public void info(Object message) {
		logger.info(message);
	}
	
	public void info(String message, Object... arguments) {
		logger.info(MessageFormat.format(message, arguments));
	}

	public void trace(Object message) {
		logger.trace(message);
	}

	public void warn(Object message) {
		logger.warn(message);
	}
}
