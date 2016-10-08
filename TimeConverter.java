import java.util.ArrayList;
import java.util.List;


public class TimeConverter {
	private String timeLabel;
	private View.timeTypes timeType;
	private int seconds;
	private float originalFrameRate;
	private float wantedFrameRate;
	
	public TimeConverter(String timeLabel, View.timeTypes timeType, int seconds)
	{
		this.timeLabel = timeLabel;
		this.timeType = timeType;
		this.seconds = seconds;
	}
	
	public TimeConverter(String timeLabel, float original, float wanted)
	{
		this.timeLabel = timeLabel;
		this.originalFrameRate = original;
		this.wantedFrameRate = wanted;
	}
	
	public String convertStatic()
	{
		if(timeType != View.timeTypes.NEITHER)
		{
			String[] times = timeLabel.split(" --> ");
			List<String> convertedTimes = new ArrayList<String>();
			
			for(int i = 0; i < times.length; i++)
			{
				String time = "";
				int timeInSeconds = convertTimeToSec(times[i]);
				int miliseconds = Integer.parseInt(times[i].split(",")[1]);
				timeInSeconds 		= timeType == View.timeTypes.GAIN ? timeInSeconds + this.seconds : timeInSeconds - this.seconds;
				
				if(timeInSeconds < 0)
				{
					timeInSeconds = 0;
					miliseconds = 0;
				}
				
				int hours 	= timeInSeconds / 3600;				
				int minutes = (timeInSeconds - hours * 3600) / 60;
				int seconds = timeInSeconds - (hours * 3600) - (minutes * 60);
				
				time 	= formatTime(hours, minutes, seconds) + "," + miliseconds;
				convertedTimes.add(time);
			}
			
			timeLabel = String.join(" --> ", convertedTimes);
		}
		return timeLabel;
	}
	
	public String convertDynamic()
	{
		String[] times = timeLabel.split(" --> ");
		List<String> convertedTimes = new ArrayList<String>();
		
		for(int i = 0; i < times.length; i++)
		{
			String time = "";
			int timeInMiliSeconds = convertTimeToMiliSec(times[i]);
			float originToWantedModifier = originalFrameRate / wantedFrameRate;
			float modifiedTime = timeInMiliSeconds * originToWantedModifier;
			timeInMiliSeconds = Math.round(modifiedTime);

			int hours = timeInMiliSeconds / 1000 / 3600;
			int ah = timeInMiliSeconds - hours * 3600 * 1000;
			
			int minutes = ah / 1000 / 60;
			int am = ah - minutes * 60 * 1000;
			
			int seconds = am / 1000;
			int miliseconds = am - seconds * 1000;
			
			String milisecs = "";
			if (miliseconds < 10)
				milisecs = "00" + miliseconds;
			else if (miliseconds < 100)
				milisecs = "0" + miliseconds;
			else
				milisecs = "" + miliseconds;
			
			
			time 	= formatTime(hours, minutes, seconds) + "," + milisecs;
			convertedTimes.add(time);		
		}
		
		timeLabel = String.join(" --> ", convertedTimes);
		
		return timeLabel;
	}
	
	private int convertTimeToSec(String time)
	{
		int hours 		= Integer.parseInt(time.split(":")[0]);	
		int minutes 	= Integer.parseInt(time.split(":")[1]);	
		int seconds 	= Integer.parseInt(time.split(":")[2].split(",")[0]);
		
		return (hours * 3600) + (minutes * 60) + seconds;
	}
	
	private int convertTimeToMiliSec(String time)
	{
		int hours 		= Integer.parseInt(time.split(":")[0]);	
		int minutes 	= Integer.parseInt(time.split(":")[1]);	
		int seconds 	= Integer.parseInt(time.split(":")[2].split(",")[0]);
		int miliseconds = Integer.parseInt(time.split(",")[1]);
		
		return (hours * 3600) * 1000 + (minutes * 60) * 1000 + seconds * 1000 + miliseconds;
	}
	
	private String formatTime(int hours, int minutes, int seconds)
	{		
		return format(hours) + ":" + format(minutes) + ":" + format(seconds);
	}
	
	private String format(int param)
	{
		return param < 10 ? "0" + param : "" + param;
	}
}
