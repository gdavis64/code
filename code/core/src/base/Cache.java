package base;

import java.util.Calendar;

public class Cache {

	public Cache(int cacheExpirationHours, int maximumWaitSecondsForReloadWhenExpired) {
		this.cacheExpirationHours = cacheExpirationHours;
		this.maximumWaitSecondsForReloadWhenExpired = maximumWaitSecondsForReloadWhenExpired;
	}

	public Cache() {
		this(Constants.CACHE_EXPIRATION_HOURS, Constants.MAXIMUM_WAIT_SECONDS_FOR_RELOAD_WHEN_EXPIRED);
	}

	// Used to indicate that all classes that extend this one to refresh their data 
	private static Calendar forceRefreshRequested;

	private int cacheExpirationHours;
	private Calendar loaded = null;
	private int maximumWaitSecondsForReloadWhenExpired;

	public static void forceRefresh() {
		forceRefreshRequested = Calendar.getInstance();
	}

	protected boolean isExpired() {
		boolean expired = false;
		if (loaded == null) {
			expired = true;
		} else {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.HOUR, - cacheExpirationHours);
			if (c.after(loaded)) {
				expired = true;
			}
		}
		return expired;
	}

	protected boolean isForceRefreshRequested() {
		boolean forceRefresh = false;
		if (forceRefreshRequested != null && loaded != null && forceRefreshRequested.after(loaded)) {
			forceRefresh = true;
		}
		return forceRefresh;
	}
	
	protected void loaded() {
		loaded = Calendar.getInstance();
	}
	
	public int getMaximumWaitSecondsForReloadWhenExpired() {
		return maximumWaitSecondsForReloadWhenExpired;
	}

}