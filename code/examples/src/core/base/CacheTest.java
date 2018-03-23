package core.base;

import java.util.ArrayList;

import common.Logger;
//TODO Add a size limit to lopg file . When it got too big it had an issue uploading to git. Maybe see about ignoring it as well.
//TODO Add error handling such as error(e)
public class CacheTest {

	public static void main(String[] args) throws InterruptedException {
		Logger.logDebugBegin();
		
		ArrayList<RequestThread> requestThreads = new ArrayList<>();
		int i = 0;
		for (int j = 1; j <= 10; j++) {
			i++;
			RequestThread t = new RequestThread();
			requestThreads.add(t);
			t.setName("t" + i);
			t.start();
//			Thread.sleep(2000);
		}
		
		Thread.sleep(6100);

		for (int j = 1; j <= 10; j++) {
			i++;
			RequestThread t = new RequestThread();
			requestThreads.add(t);
			t.setName("t" + i);
			t.start();
			Thread.sleep(100);
		}
		
//		Logger.logDebug(PropertiesCache.getInstance().getData("main", 60).get("application_name"));
		for (RequestThread requestThread : requestThreads) {
			requestThread.join();
		}
		Logger.logDebugEnd();
	}
	
}