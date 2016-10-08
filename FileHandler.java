import java.io.File;

public class FileHandler {

	private File inputFile;
	private String outputFilePath;
	
	public FileHandler(File inputFile)
	{
		this.inputFile = inputFile;
		this.outputFilePath = setOutputFilePath();
	}

	public FileHandler(){}
	
	private String setOutputFilePath()
	{
		String path = this.inputFile.getPath();
		//path of the opened file:
		String pathDir = path.substring(0,path.lastIndexOf(File.separator));
		//creating "felirat" directory:
		File felirat = new File(pathDir, "felirat");
		felirat.mkdir();
		//creating file and it's absolute path:
		File feliratDir = new File(felirat, this.inputFile.getName());
		//setting output file's name:
		return feliratDir.getAbsolutePath();		
	}
	
	public File getInputFile()
	{
		return this.inputFile;
	}
	public String getOutputFilePath()
	{
		return this.outputFilePath;
	}
	
}
