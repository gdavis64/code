package base;

import java.util.Calendar;

import common.Logger;

public class Cache {

	public Cache(int cacheExpiresTime, int cacheExpiresTimeUnits, int maximumRequestWaitForReloadWhenExpiredSeconds) {
		this.cacheExpiresTime = cacheExpiresTime;
		this.cacheExpiresTimeUnit = cacheExpiresTimeUnits;
		this.maximumRequestWaitForReloadWhenExpiredSeconds = maximumRequestWaitForReloadWhenExpiredSeconds;
	}

	public Cache() {
		this(Constants.CACHE_EXPIRES_TIME, Constants.CACHE_EXPIRES_TIME_UNIT, Constants.CACHE_MAXIMUM_REQUEST_WAIT_FOR_RELOAD_WHEN_EXPIRED_SECONDS);
	}

	// Used to indicate that extended classes need to refresh their data 
	private static Calendar forceRefreshRequested;

	private int cacheExpiresTime;
	private int cacheExpiresTimeUnit;	
	private Calendar loadStarted = null;	
	private Calendar loadCompleted = null;
	private int maximumRequestWaitForReloadWhenExpiredSeconds;

	public static void forceRefresh() {
		forceRefreshRequested = Calendar.getInstance();
	}
	
	protected boolean isExpired() {
		boolean expired = false;
		if (loadCompleted == null) {
			expired = true;
		} else {
			Calendar c = Calendar.getInstance();
			c.add(cacheExpiresTimeUnit, - cacheExpiresTime);			
			if (c.after(loadCompleted)) {
				expired = true;
			}
		}
		return expired;
	}

	protected boolean isForceRefreshRequested() {
		boolean forceRefresh = false;
		if (forceRefreshRequested != null && loadCompleted != null && forceRefreshRequested.after(loadCompleted)) {
			forceRefresh = true;
		}
		return forceRefresh;
	}
	
	protected boolean isLoadInProgress() {
		boolean loadInProgress = false;
		if (loadStarted != null) {
			if (loadCompleted == null) {
				loadInProgress = true;
			} else {
				if (loadStarted.after(loadCompleted)) {
//					Calendar c = Calendar.getInstance();
//					c.add(Calendar.MINUTE, - Constants.CACHE_ASSUME_LOAD_FAILED_MINUTES);
//					if (c.after(loadStarted)) {
						loadInProgress =true;
//					}
				}
			}
		}
		Logger.logDebug("isLoadInProgress: " + loadInProgress);	
		return loadInProgress;
	}
	
	protected void loadCompleted() {
		loadCompleted = Calendar.getInstance();
	}
	
	protected void loadStarted() {
		loadStarted = Calendar.getInstance();
	}
	
	public int getMaximumRequestWaitForReloadWhenExpiredSeconds() {
		return maximumRequestWaitForReloadWhenExpiredSeconds;
	}

}