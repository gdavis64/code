package core.base;

import java.util.HashMap;

import common.Logger;

public class ExampleThread extends Thread {

	public void run() {
		Logger.log("Begin");
		HashMap<String, String> properties = ExampleCache.getInstance().getProperties();
		Logger.log("properties: " + properties.toString() + properties.hashCode());
		Logger.log("End");
	}
	
}