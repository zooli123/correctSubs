import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
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

public class View 
{	
	private JFrame window;
	private JPanel panel;
	private JButton startButton;
	private JButton openFileButton;
	private JFileChooser inputTextChooser;
	private ButtonGroup buttonGroup;
	private JRadioButton gain;
	private JRadioButton lose;
	private JFormattedTextField secondsField;
	
	private int seconds;
	private File inputFile;
	private String outputFileName;
	
	public enum timeTypes
	{
		GAIN,
		LOSE,		
		NEITHER
	}
	private timeTypes timeType; 
	
	public View()
	{
		window = new JFrame("CorrectSubs");
		panel = new JPanel();
		startButton = new JButton("Start");
		openFileButton = new JButton("Fájl megnyitása");		
		inputTextChooser = new JFileChooser();
		File defaultDir = new File("D:\\Filmek");
		inputTextChooser.setCurrentDirectory(defaultDir);
		buttonGroup = new ButtonGroup();
		gain = new JRadioButton();
		lose = new JRadioButton();
		secondsField = new JFormattedTextField();
		timeType = timeTypes.NEITHER;
		
		
		openFileButton.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				int returnVal = inputTextChooser.showOpenDialog(window);
				if(returnVal == JFileChooser.APPROVE_OPTION)
				{
					File file = inputTextChooser.getSelectedFile();
					inputFile = file;
					//creating "felirat" directory in current dir and subtitle file in it
					String path = file.getPath();
					//path of the opened file:
					String pathDir = path.substring(0,path.lastIndexOf(File.separator));
					//creating "felirat" directory:
					File felirat = new File(pathDir, "felirat");
					felirat.mkdir();
					//creating file and it's absolute path:
					File feliratDir = new File(felirat, file.getName());
					//setting output file's name:
					outputFileName = feliratDir.getAbsolutePath();
				}
			}});
		
		startButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if((gain.isSelected() || lose.isSelected()) && (inputFile != null) && secondsField.getText() != "" && outputFileName != null)
				{
					timeType = gain.isSelected() ? timeTypes.GAIN : timeTypes.LOSE;
					seconds = Integer.parseInt(secondsField.getText());
					try {
						@SuppressWarnings("resource")
						BufferedReader reader = new BufferedReader(new FileReader(inputFile));
						List<String> text = new ArrayList<String>() ;
						Path output = Paths.get(outputFileName);
						
						for(String line = reader.readLine(); line != null; line = reader.readLine())
						{					
							TimeRecognizer tr = new TimeRecognizer(line);
							if(tr.isTimeLabel())
							{
								TimeConverter tc = new TimeConverter(line, timeType, seconds);
								text.add(tc.convert());
							}
							else
							{
								text.add(line);
							}
						}
						Files.write(output, text, Charset.forName("UTF-8"));
						
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}			
		});
	}
	
	public void initWindow()
	{
		JLabel gainLabel = new JLabel("Siet");
		JLabel loseLabel = new JLabel("Késik");
		JLabel secondsLabel = new JLabel("Hány másodpercet:");
		buttonGroup.add(gain);
		buttonGroup.add(lose);
		JPanel radioPanel = new JPanel();
		radioPanel.add(gainLabel);
		radioPanel.add(gain);
		radioPanel.add(loseLabel);
		radioPanel.add(lose);
		JPanel secondsPanel = new JPanel();
		secondsPanel.add(secondsLabel);
		secondsPanel.add(secondsField);
		secondsField.setColumns(5);
		JPanel startPanel = new JPanel();
		startPanel.add(startButton);	
		panel.setLayout(new FlowLayout());
		panel.add(openFileButton);
		panel.add(radioPanel);
		panel.add(secondsPanel);
		panel.add(startPanel);
		window.add(panel);
		window.setSize(300,150);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
	
}
