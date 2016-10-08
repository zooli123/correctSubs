import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class FileHandler {

	private File inputFile;
	private String outputFilePath;
	
	public FileHandler(File inputFile)
	{
		this.inputFile = inputFile;
		this.outputFilePath = setOutputFileName();
	}

	public FileHandler(){}
	
	private String setOutputFileName()
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
