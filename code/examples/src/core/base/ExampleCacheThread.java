package core.base;

import common.Logger;

public class ExampleCacheThread extends Thread {
	
	public ExampleCacheThread(ExampleCache exampleCache) {
		this.exampleCache = exampleCache;
	}
	
	private ExampleCache exampleCache;
	
	public void run() {
		Logger.log("Begin");
		exampleCache.loadProperties();
		Logger.log("End");
	}

}
