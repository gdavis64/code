package core.base;

import java.util.Calendar;
import java.util.HashMap;

import base.Cache;
import base.Constants;
import common.Logger;

public class PropertiesCache extends Cache {
	
	private PropertiesCache() {
		super(1, Calendar.SECOND, Constants.CACHE_MAXIMUM_REQUEST_WAIT_FOR_RELOAD_WHEN_EXPIRED_SECONDS);
//		super(Constants.CACHE_EXPIRES_TIME, Constants.CACHE_EXPIRES_TIME_UNIT, Constants.CACHE_MAXIMUM_REQUEST_WAIT_FOR_RELOAD_WHEN_EXPIRED_SECONDS);
	}
	
	private volatile static PropertiesCache instance = null;
	
	private HashMap<String, String> data = null;
	private Object dataLock = new Object();
	private Object loadLock = new Object();
	
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
	
	public HashMap<String, String> getData(Integer maximumWaitForReloadWhenExpiredSeconds) {
		if (data == null) {
			
			// Synchronize to prevent multiple calls to the load method when data is null and concurrent calls to getData occur
			synchronized (dataLock) {
				if (data == null) {
					load();
				}
			}
		} else if (isForceRefreshRequested()) {

			// Synchronize to prevent multiple calls to the load method when force refresh is requested and concurrent calls to getData occur		
			synchronized (dataLock) {
				if (isForceRefreshRequested()) {
					load();
				}
			}
		} else if (isExpired()) {
//			loadProperties();
			//TODO Add a size limit to lopg file . When it got too big it had an issue uploading to git. Maybe see about ignoring it as well.
			//TODO Remove synchronized on load properties and do not intoiate more than one thread at a time...
			
			// Load the properties with a thread. This can cause multiple calls to loadProperties so loadProperties is synchronized.
			ExampleCacheLoadThread thread = new ExampleCacheLoadThread(instance);
			Logger.logDebug("isExpired starting thread");
			thread.start();

			// If requested, wait on the refresh of properties for a maximum amount of seconds
			if (maximumWaitForReloadWhenExpiredSeconds != null) {
				try {
					thread.join(maximumWaitForReloadWhenExpiredSeconds);
				} catch (InterruptedException e) {
				}
				Logger.logDebug("after reload properties: " + data.toString() + data.hashCode());			}
		}
		return data;
	}
		
	public  HashMap<String, String> getData() {
		return getData(null);
	}

	public void load() {
		Logger.logDebug("load() begin");	
		if (!isLoadInProgress()) {
//			synchronized (loadLock) {
//				if (!isLoadInProgress()) {
					loadStarted();
					HashMap<String, String> tempProperties = new HashMap<String, String>();
					Calendar c = Calendar.getInstance();
					tempProperties.put("application_name", "Fun Application" + c.getTimeInMillis());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					data = tempProperties;
					loadCompleted();
//				}
//			}
		}
		Logger.logDebug("load() end");	
	}	
	
	public String toString() {
		return "Add code here"; //TODO Add toStrings to all base classes
	}

}