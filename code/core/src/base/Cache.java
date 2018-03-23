package base;

import java.util.Calendar;

import common.Logger;

public class Cache {

	public Cache(int cacheExpiresTimeUnit, int cacheExpiresTime, int maximumRequestWaitForReloadWhenExpiredSeconds) {
		this.cacheExpiresTimeUnit = cacheExpiresTimeUnit;
		this.cacheExpiresTime = cacheExpiresTime;
		this.maximumRequestWaitForReloadWhenExpiredSeconds = maximumRequestWaitForReloadWhenExpiredSeconds;
	}

	public Cache() {
		this(Constants.CACHE_EXPIRES_TIME, Constants.CACHE_EXPIRES_TIME_UNIT, Constants.CACHE_MAXIMUM_REQUEST_WAIT_FOR_RELOAD_WHEN_EXPIRED_SECONDS);
	}

	// Used to indicate that extended classes need to refresh their data 
	private static Calendar forceReloadRequested;

	private int cacheExpiresTimeUnit;	
	private int cacheExpiresTime;
	private Calendar loadStarted = null;	
	private Calendar loadCompleted = null;
	private int maximumRequestWaitForReloadWhenExpiredSeconds;

	public static void forceRefresh() {
		forceReloadRequested = Calendar.getInstance();
	}
	
	protected boolean isExpired() {
		boolean expired = false;
		if (loadCompleted == null) {
			expired = true;
			Logger.logDebug("loadCompleted == null");	
		} else {
			Calendar c = Calendar.getInstance();
			c.add(cacheExpiresTimeUnit, - cacheExpiresTime);			
			if (c.after(loadCompleted)) {
				expired = true;
				Logger.logDebug("c after loadCompleted");	
			}
		}
		Logger.logDebug("" + expired);	
		return expired;
	}

	protected boolean isForceReloadRequested() {
		boolean forceReload = false;
		if (forceReloadRequested != null && loadCompleted != null && forceReloadRequested.after(loadCompleted)) {
			forceReload = true;
		}
		return forceReload;
	}
	
	//TODO Is this method needed? I do not think so now that is in progress is determined by the loadThread being alive
	public boolean isLoadInProgress() {
		boolean loadInProgress = false;
		if (loadStarted != null) {
			if (loadCompleted == null) {
				loadInProgress = true;
			} else {
				if (loadStarted.after(loadCompleted)) {
					Logger.logDebug("started after completed");	
//					Calendar c = Calendar.getInstance();
//					c.add(Calendar.MINUTE, - Constants.CACHE_ASSUME_LOAD_FAILED_MINUTES);
//					if (c.after(loadStarted)) {
						loadInProgress =true;
//					}
				}
			}
		}
		Logger.logDebug("return: " + loadInProgress);	
		return loadInProgress;
	}
	
	public void loadCompleted() {
		loadCompleted = Calendar.getInstance();
	}
	
	public void loadStarted() {
		loadStarted = Calendar.getInstance();
	}
	
	public int getMaximumRequestWaitForReloadWhenExpiredSeconds() {
		return maximumRequestWaitForReloadWhenExpiredSeconds;
	}

}