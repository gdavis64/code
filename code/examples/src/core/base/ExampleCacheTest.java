package core.base;

import java.util.ArrayList;

import common.Logger;

public class ExampleCacheTest {

	public static void main(String[] args) throws InterruptedException {
		
		ArrayList<ExampleThread> threads = new ArrayList<ExampleThread>();
		for (int i = 1; i <= 3; i++) {
			ExampleThread t = new ExampleThread();
			threads.add(t);
			t.setName("t" + i);
			t.start();
		}
		
		Thread.sleep(2000);

//		for (int i = 3; i <= 6; i++) {
			ExampleThread t = new ExampleThread();
//			threads.add(t);
			t.setName("nt" + 4);
			t.start();
//		}
		
		Logger.log(ExampleCache.getInstance().getProperties().get("application_name"));
	}
	
}