package core.base;

import common.Logger;

public class ExampleCacheLoadThread extends Thread {
	
	public ExampleCacheLoadThread(PropertiesCache exampleCache) {
		this.exampleCache = exampleCache;
		setName(this.getClass().getSimpleName());
	}
	
	private PropertiesCache exampleCache;
	
	public void run() {
		Logger.logDebug("Begin");
		exampleCache.load();
//		Logger.log("properties: " + exampleCache.getProperties().toString() + exampleCache.getProperties().hashCode());
		Logger.logDebug("End");
	}

}