package core.base;

import java.util.HashMap;

import common.Logger;

public class RequestThread extends Thread {

	public void run() {
		Logger.logDebugBegin();	
		HashMap<String, String> properties = PropertiesCache.getInstance().getData(this.getName(), 1);
		Logger.logDebug("properties: " + properties.toString() + properties.hashCode());
		Logger.logDebugEnd();
	}
	
}