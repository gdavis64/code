package core.base;

import java.util.Calendar;
import java.util.HashMap;

import org.apache.logging.log4j.Level;

import base.Cache;
import common.Constants;
import common.Logger;

public class PropertiesCache extends Cache {
	
	private PropertiesCache() {
		super(Calendar.SECOND, 1, Constants.CACHE_MAXIMUM_REQUEST_WAIT_FOR_RELOAD_WHEN_EXPIRED_SECONDS);
//		super(Constants.CACHE_EXPIRES_TIME_UNIT, Constants.CACHE_EXPIRES_TIME, Constants.CACHE_MAXIMUM_REQUEST_WAIT_FOR_RELOAD_WHEN_EXPIRED_SECONDS);
	}
	
	private volatile static PropertiesCache instance = null;
	
	private HashMap<String, String> data = null;

	private static PropertiesCacheLoadThread loadThread = null;
	private Object loadThreadLock = new Object();
	
	public static PropertiesCache getInstance() {
		
		// Use double checked locking implementation for a thread safe implementation of the singleton pattern
		if (instance == null) {

			// Synchronize inside the if statement so calls to getInstance after instance is not null will not take a performance hit using synchronization
			synchronized (PropertiesCache.class) {
				if (instance == null) {
					instance = new PropertiesCache();
				}
			}
		}
		return instance;
	}

	public HashMap<String, String> getData(String loadThreadNamePrefix, Integer maximumWaitForLoadSeconds) {
		if (data == null || isForceReloadRequested()) {
			load(loadThreadNamePrefix, null);
		} else if (isExpired()) {
			load(loadThreadNamePrefix, maximumWaitForLoadSeconds);
		}
		return data;
	}
		
	private void load(String loadThreadNamePrefix, Integer maximumWaitForLoadSeconds) {
		Logger.logBegin();	

		// Use double checked locking implementation to ensure only one loadThread will be running at a time
		if (loadThread == null || !loadThread.isAlive()) {
			synchronized (loadThreadLock) {
				if (loadThread == null || !loadThread.isAlive()) {
					loadThread = new PropertiesCacheLoadThread(instance, loadThreadNamePrefix);
					loadThread.start();
				}
			}
		}

		// Irregardless if the current call to this load method started the loadThread, join on the active loadThread for the amount of time specified
		try {
			if (maximumWaitForLoadSeconds == null) {
				Logger.log(Level.DEBUG, "loadThread.join()");	
				loadThread.join();
			} else {
				Logger.log(Level.DEBUG, "loadThread.join(" + maximumWaitForLoadSeconds * 1000 + ")");	
				loadThread.join(maximumWaitForLoadSeconds * 1000);
			}
		} catch (InterruptedException e) {
		}
		Logger.logEnd();	
	}

	public void loadData() {
		Logger.logBegin();	
		loadStarted();
		HashMap<String, String> tempData = new HashMap<String, String>();
		Calendar c = Calendar.getInstance();
		tempData.put("application_name", "Fun Application" + c.getTimeInMillis());
		try {
			Logger.log(Level.DEBUG, "about to sleep 5 secs");	
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		data = tempData;
		loadCompleted();
		Logger.log("completed in x ms, data.hashCode " + data.hashCode()); 
		Logger.logEnd();	
	}	
	
	public String toString() {
		return "Add code here"; //TODO Add toStrings to all base classes
	}

}