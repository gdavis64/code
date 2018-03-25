package core.base;

import common.Logger;

public class PropertiesCacheLoadThread extends Thread {
	
	public PropertiesCacheLoadThread(PropertiesCache propertiesCache, String loadThreadNamePrefix) {
		this.propertiesCache = propertiesCache;
		setName(loadThreadNamePrefix + " " + this.getClass().getSimpleName());
	}
	
	private PropertiesCache propertiesCache;
	
	public void run() {
		Logger.logBegin();
		propertiesCache.loadData();
		Logger.logEnd();
	}

}