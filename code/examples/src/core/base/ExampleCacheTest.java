package core.base;

import common.Logger;

public class ExampleCacheTest {

	public static void main(String[] args) throws InterruptedException {
		
		for (int i = 1; i <= 3; i++) {
			ExampleThread t = new ExampleThread();
			t.setName("t" + i);
			t.start();
		}
		
		Thread.sleep(6000);

		for (int i = 4; i <= 6; i++) {
			ExampleThread t = new ExampleThread();
			t.setName("nt" + i);
			t.start();
			Thread.sleep(750);
		}
		
		Logger.logDebug(PropertiesCache.getInstance().getData(60).get("application_name"));
	}
	
}