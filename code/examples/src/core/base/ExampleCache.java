package core.base;

import java.util.Calendar;
import java.util.HashMap;

import base.Cache;
import base.Constants;
import common.Logger;

public class ExampleCache extends Cache {
	
	private ExampleCache() {
		super(Constants.CACHE_EXPIRATION_HOURS, Constants.MAXIMUM_WAIT_SECONDS_FOR_RELOAD_WHEN_EXPIRED);
	}
	
	private volatile static ExampleCache instance = null;
	
	private HashMap<String, String> properties = null;
	private Object propertiesLock = new Object();
	
	public static ExampleCache getInstance() {
		
		// Use double checked locking implementation for a thread safe implementation of the singleton pattern
		if (instance == null) {

			// Synchronize inside the if statement so calls to getInstance after instance is not null will not take a performance hit using synchronization
			synchronized (ExampleCache.class) {
				if (instance == null) {
					instance = new ExampleCache();
				}
			}
		}
		return instance;
	}
	
	public HashMap<String, String> getProperties(Integer maximumWaitSecondsForReloadWhenExpired) {
		if (properties == null) {
			
			// Synchronize to prevent multiple calls to loadProperties when properties is null. No need to start multiple loadProperties.
			synchronized (propertiesLock) {
				if (properties == null) {
					loadProperties();
				}
			}
		} else if (isForceRefreshRequested()) {
			
			// Synchronize to prevent multiple calls to loadProperties when a force refresh is requested. No need to start multiple loadProperties.
			synchronized (propertiesLock) {
				if (isForceRefreshRequested()) {
					loadProperties();
				}
			}
		} else if (isExpired()) {
//			loadProperties();
			//TODO Remove synchronized on load properties and do not intoiate more than one thread at a time...
			
			// Load the properties with a thread. This can cause multiple calls to loadProperties so loadProperties is synchronized.
			ExampleCacheLoadThread thread = new ExampleCacheLoadThread(instance);
			thread.start();

			// If requested, wait on the refresh of properties for a maximum amount of seconds
			if (maximumWaitSecondsForReloadWhenExpired != null) {
				try {
					thread.join(maximumWaitSecondsForReloadWhenExpired);
					Logger.log("after reload properties: " + properties.toString() + properties.hashCode());
				} catch (InterruptedException e) {
				}
			}
		}
		return properties;
	}
		
	public  HashMap<String, String> getProperties() {
		return getProperties(null);
	}

	// This method is synchronized as it can be called from multiple threads started through the isExpired logic in getProperties 
	public synchronized void loadProperties() {
		Logger.log("loadProperties() start");	
//		HashMap<String, String> atempProperties = new HashMap<String, String>();		
		HashMap<String, String> tempProperties = new HashMap<String, String>();
		Calendar c = Calendar.getInstance();
		tempProperties.put("application_name", "Fun Application" + c.getTimeInMillis());
		properties = tempProperties;
		loaded();
		Logger.log("loadProperties(): " + properties.toString() + " hc: " + properties.hashCode());	
	}	
	
	public String toString() {
		return "Add code here"; //TODO Add toStrings to all base classes
	}

}