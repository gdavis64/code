package core.base;

import common.Logger;

public class ExampleCacheLoadThread extends Thread {
	
	public ExampleCacheLoadThread(ExampleCache exampleCache) {
		this.exampleCache = exampleCache;
		setName(this.getClass().getSimpleName());
	}
	
	private ExampleCache exampleCache;
	
	public void run() {
		Logger.log("Begin");
		exampleCache.loadProperties();
//		Logger.log("properties: " + exampleCache.getProperties().toString() + exampleCache.getProperties().hashCode());
		Logger.log("End");
	}

}