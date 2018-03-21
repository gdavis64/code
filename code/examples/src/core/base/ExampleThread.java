package core.base;

import java.util.HashMap;

import common.Logger;

public class ExampleThread extends Thread {

	public void run() {
		Logger.logDebug("Begin");
		HashMap<String, String> properties = PropertiesCache.getInstance().getData(60);
		Logger.logDebug("properties: " + properties.toString() + properties.hashCode());
		Logger.logDebug("End");
	}
	
}