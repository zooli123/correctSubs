import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TimeRecognizer {
	private Pattern pattern;
	private String line;
	public TimeRecognizer(String line)
	{
		pattern = Pattern.compile("[0-9]+:[0-9]+:[0-9]+,[0-9]{3}[\" \"]-->[\" \"][0-9]+:[0-9]+:[0-9]+,[0-9]{3}");
		this.line = line;
	}
	
	public boolean isTimeLabel()
	{
		Matcher match = pattern.matcher(line);
		return match.matches();
	}
}
