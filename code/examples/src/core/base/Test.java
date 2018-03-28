package core.base;

import common.Logger;
import common.Stopwatch;

public class Test {
	
	public static void main(String[] args) throws InterruptedException {
	
		try {
			Logger.logBegin();
			Stopwatch stopwatch = new Stopwatch();
			Thread.sleep(150);
			stopwatch.recordInterval("First");
			Thread.sleep(400);
			stopwatch.recordInterval("Second");
			Thread.sleep(900);
			stopwatch.recordInterval("Database retrieval");
			Thread.sleep(400);
			Logger.log("elapsed: " + stopwatch.elapsedMillisWithUnits());
			Logger.log("intervals: " + stopwatch.formatIntervals());
		} catch (Exception e) {
			Logger.log(e);
		} finally {
			Logger.logEnd();
		}
	}

}
