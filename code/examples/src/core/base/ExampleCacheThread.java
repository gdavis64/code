package core.base;

import common.Logger;

public class ExampleCacheThread extends Thread {
	
	public ExampleCacheThread(PropertiesCache exampleCache) {
		this.exampleCache = exampleCache;
	}
	
	private PropertiesCache exampleCache;
	
	public void run() {
		Logger.logDebug("Begin");
		exampleCache.load();
		Logger.logDebug("End");
	}

}
