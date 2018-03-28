package common;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;

public class Stopwatch {

	public Stopwatch(boolean start) {
		if (start) {
			this.started = Instant.now();
		}
	}

	public Stopwatch() {
		this(true);
	}

	private ArrayList<Interval> intervals = new ArrayList<Interval>();
	private Instant started = null;	
	private Instant stopped = null;

	private class Interval {
		public Interval(String label) {
			this.label = label;
			this.instant = Instant.now();
		}
		public String label;
		public Instant instant;
	}
	
	public static Long elapsedMillis(Instant i1, Instant i2) {
		return i1 != null ? Duration.between(i1, i2 != null ? i2 : Instant.now()).toMillis() : null;
	}
	
	public Long elapsedMillis() {
		return elapsedMillis(started, stopped);
	}
	
	public static String elapsedMillisWithUnits(Instant i1, Instant i2) {
		String result = null;
		Long elapsed = elapsedMillis(i1, i2);
		if (elapsed != null) {
			result = Common.FORMAT_NUMBER_WHOLE_WITH_COMMAS.format(elapsed) + " ms";
		}
		return result;
	}
	
	public String elapsedMillisWithUnits() {
		return elapsedMillisWithUnits(started, stopped); 
	}
	
	public static Long elapsedMinutes(Instant i1, Instant i2) {
		return i1 != null ? Duration.between(i1, i2 != null ? i2 : Instant.now()).toMinutes() : null;
	}
	
	public Long elapsedMinutes() {
		return elapsedMinutes(started, stopped);
	}
	
	public static String elapsedMinutesWithUnits(Instant i1, Instant i2) {
		String result = null;
		Long elapsed = elapsedMinutes(i1, i2);
		if (elapsed != null) {
			result = Common.FORMAT_NUMBER_WHOLE_WITH_COMMAS.format(elapsed) + " mins";
		}
		return result;
	}
	
	public String elapsedMinutesWithUnits() {
		return elapsedMinutesWithUnits(started, stopped); 
	}
	
	public static Long elapsedSeconds(Instant i1, Instant i2) {
		return i1 != null ? Duration.between(i1, i2 != null ? i2 : Instant.now()).toMillis() / 1000 : null;
	}
	
	public Long elapsedSeconds() {
		return elapsedSeconds(started, stopped);
	}
	
	public static String elapsedSecondsWithUnits(Instant i1, Instant i2) {
		String result = null;
		Long elapsed = elapsedSeconds(i1, i2);
		if (elapsed != null) {
			result = Common.FORMAT_NUMBER_WHOLE_WITH_COMMAS.format(elapsed) + " secs";
		}
		return result;
	}
	
	public String elapsedSecondsWithUnits() {
		return elapsedSecondsWithUnits(started, stopped); 
	}
	
	public String formatIntervals() {
		StringBuffer sb = new StringBuffer();
		sb.append("[Total ").append(elapsedMillisWithUnits(started, intervals.get(intervals.size() - 1).instant)).append(", ");
		if (started != null && !intervals.isEmpty()) {
			for (int i = 0; i < intervals.size(); i++) {
				if (i > 0) {
					sb.append(", ");
				}
				Instant i1 = i == 0 ? started : intervals.get(i-1).instant;
				Instant i2 = intervals.get(i).instant;
				sb.append(intervals.get(i).label).append(" ").append(elapsedMillis(i1, i2));
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	public String formatStarted() {
		return started != null ? Common.FORMAT_DATE_TIME_TIMESTAMP.format(started.atZone(ZoneId.systemDefault())) : null;
	}
	
	public void recordInterval(String label) {
		intervals.add(new Interval(label)); 
	}
	
	public void start() {
		if (started == null) {
			started = Instant.now();
		}
	}
	
	public void stop() {
		if (started != null) {
			stopped = Instant.now();
		}
	}
	
}