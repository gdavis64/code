package core.base;

import java.util.HashMap;

import org.apache.logging.log4j.Level;

import common.Logger;

public class RequestThread extends Thread {

	public void run() {
		try {
			Logger.logBegin();	
			HashMap<String, String> properties = PropertiesCache.getInstance().getData(this.getName(), 1);
			Logger.log(Level.DEBUG, "properties: " + properties.toString() + properties.hashCode());
		} catch (Exception e) {
			Logger.log(e);
		} finally {
			Logger.logEnd();			
		}
	}

}