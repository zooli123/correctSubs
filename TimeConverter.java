import java.util.ArrayList;
import java.util.List;


public class TimeConverter {
	private String timeLabel;
	private View.timeTypes timeType;
	private int seconds;
	
	public TimeConverter(String timeLabel, View.timeTypes timeType, int seconds)
	{
		this.timeLabel = timeLabel;
		this.timeType = timeType;
		this.seconds = seconds;
	}
	
	public String convert()
	{
		if(timeType != View.timeTypes.NEITHER)
		{
			String[] times = timeLabel.split(" --> ");
			List<String> convertedTimes = new ArrayList<String>();
			
			for(int i = 0; i < times.length; i++)
			{
				String time = "";
				int hours 		= Integer.parseInt(times[i].split(":")[0]);	
				int minutes 	= Integer.parseInt(times[i].split(":")[1]);	
				int seconds 	= Integer.parseInt(times[i].split(":")[2].split(",")[0]);
				int miliseconds = Integer.parseInt(times[i].split(",")[1]);
				
				int timeInSeconds 	= (hours * 3600) + (minutes * 60) + seconds;
				timeInSeconds 		= timeType == View.timeTypes.GAIN ? timeInSeconds + this.seconds : timeInSeconds - this.seconds;
				
				if(timeInSeconds < 0)
				{
					timeInSeconds = 0;
					miliseconds = 0;
				}
				
				hours 	= timeInSeconds / 3600;				
				minutes = (timeInSeconds - hours * 3600) / 60;
				seconds = timeInSeconds - (hours * 3600) - (minutes * 60);
				
				time 	= hours + ":" + minutes + ":" + seconds + "," + miliseconds;
				convertedTimes.add(time);
			}
			
			timeLabel = String.join(" --> ", convertedTimes);
		}
		return timeLabel;
	}
}
