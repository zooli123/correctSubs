import javax.swing.*;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class View 
{	
	private JFrame window;
	private JPanel panel;
	private JPanel staticPanel;
	private JPanel dynamicPanel;
	private JButton startButton;
	private JButton openFileButton;
	private JFileChooser inputTextChooser;
	private ButtonGroup gainLoseGroup;
	private ButtonGroup convertTypeGroup;
	private JRadioButton gain;
	private JRadioButton lose;
	private JRadioButton staticConvert;
	private JRadioButton dynamicConvert;
	private JFormattedTextField secondsField;
	private JComboBox originalFrameRate;
	private JComboBox wantedFrameRate;
	private JLabel openedFile;
	
	private int seconds;
	private FileHandler fileHandler;
	private float[] frameRates = 
	{
		(float)30.000, 
		(float)29.97, 
		(float)25.000,
		(float)24.000, 
		(float)23.99, 
		(float)23.978, 
		(float)23.976, 
		(float)20, 
		(float)15.000, 
		(float)12.000
	};
	
	public enum timeTypes
	{
		GAIN,
		LOSE,		
		NEITHER
	}
	private timeTypes timeType; 
	
	public enum convertTypes
	{
		STATIC,
		DYNAMIC
	} 
	private convertTypes convertType;
	private Font italic;

	
	public View()
	{
		window = new JFrame("CorrectSubs");
		panel = new JPanel();
		staticPanel = new JPanel();
		dynamicPanel = new JPanel();
		startButton = new JButton("Start");
		openFileButton = new JButton("Fájl megnyitása");		
		inputTextChooser = new JFileChooser();
		File defaultDir = new File(System.getProperty("user.home"));
		inputTextChooser.setCurrentDirectory(defaultDir);
		gainLoseGroup = new ButtonGroup();
		convertTypeGroup = new ButtonGroup();
		gain = new JRadioButton();
		lose = new JRadioButton();
		staticConvert = new JRadioButton();
		dynamicConvert = new JRadioButton();
		secondsField = new JFormattedTextField(NumberFormat.getNumberInstance());
		openedFile = new JLabel("", SwingConstants.CENTER);
		italic  = new Font(openedFile.getFont().getName(),Font.ITALIC,openedFile.getFont().getSize());
		originalFrameRate = new JComboBox();
		wantedFrameRate = new JComboBox();
		timeType = timeTypes.NEITHER;
		
		for(int i = 0; i < frameRates.length; ++i)
		{
			originalFrameRate.addItem(frameRates[i]);
			wantedFrameRate.addItem(frameRates[i]);		
		}
		
		staticConvert.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				staticPanel.setVisible(true);
				dynamicPanel.setVisible(false);
				convertType = convertTypes.STATIC;
			}
		});
		
		dynamicConvert.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				dynamicPanel.setVisible(true);
				staticPanel.setVisible(false);
				convertType = convertTypes.DYNAMIC;
			}
		});
		
		openFileButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int returnVal = inputTextChooser.showOpenDialog(window);
				if(returnVal == JFileChooser.APPROVE_OPTION)
				{
					File file = inputTextChooser.getSelectedFile();
					fileHandler = new FileHandler(file);
					openedFile.setText(file.getName());
				}
			}});
		
		startButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{				
				if(conditionsOK())
				{
					try {
						@SuppressWarnings("resource")
						BufferedReader reader = new BufferedReader(new FileReader(fileHandler.getInputFile()));
						List<String> text = new ArrayList<String>() ;
						Path output = Paths.get(fileHandler.getOutputFilePath());
						
						for(String line = reader.readLine(); line != null; line = reader.readLine())
						{					
							TimeRecognizer tr = new TimeRecognizer(line);
							if(tr.isTimeLabel())
							{
								if(convertType == convertTypes.STATIC)
								{
									timeType = gain.isSelected() ? timeTypes.GAIN : timeTypes.LOSE;
									seconds = Integer.parseInt(secondsField.getText());
									TimeConverter tc = new TimeConverter(line, timeType, seconds);
									text.add(tc.convertStatic());
								}
								else if (convertType == convertTypes.DYNAMIC)
								{
									TimeConverter tc = new TimeConverter(line, 
																		Float.parseFloat(originalFrameRate.getSelectedItem().toString()), 
																		Float.parseFloat(wantedFrameRate.getSelectedItem().toString()));
									text.add(tc.convertDynamic());
								}
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
		//static panel
		
		
		//gain or lose
		JPanel radioPanel = new JPanel();
		JPanel selectionPanel1 = new JPanel();
		JPanel selectionPanel2 = new JPanel();
		JLabel gainLabel = new JLabel("Siet");
		JLabel loseLabel = new JLabel("Késik");
		gainLoseGroup.add(gain);
		gainLoseGroup.add(lose);
		selectionPanel1.add(gainLabel);
		selectionPanel1.add(gain);
		selectionPanel2.add(loseLabel);
		selectionPanel2.add(lose);
		
		selectionPanel1.setLayout(new FlowLayout());
		selectionPanel2.setLayout(new FlowLayout());
		radioPanel.add(selectionPanel1);
		radioPanel.add(selectionPanel2);
		
		//how much
		JPanel secondsPanel = new JPanel();
		JLabel secondsLabel = new JLabel("Hány másodpercet:");
		secondsPanel.add(secondsLabel);
		secondsPanel.add(secondsField);
		secondsField.setColumns(5);
		
		radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
		staticPanel.setLayout(new FlowLayout());
		staticPanel.add(radioPanel);
		staticPanel.add(secondsPanel);
		staticPanel.setVisible(false);
		
		
		//convertType
		JPanel convertTypePanel = new JPanel();
		JLabel staticConvertLabel = new JLabel("Statikus igazítás");
		JLabel dynamicConvertLabel = new JLabel("Dinamikus igazítás");
		convertTypeGroup.add(staticConvert);
		convertTypeGroup.add(dynamicConvert);
		
		convertTypePanel.add(staticConvertLabel);
		convertTypePanel.add(staticConvert);
		convertTypePanel.add(dynamicConvertLabel);
		convertTypePanel.add(dynamicConvert);
		
		//Dynamic panel
		JLabel original = new JLabel("Eredeti framerate");
		JLabel wanted = new JLabel("Kívánt framerate");
		JPanel originalPanel = new JPanel();
		JPanel wantedPanel = new JPanel();
		
		originalPanel.add(original);
		originalPanel.add(originalFrameRate);
		originalPanel.setLayout(new FlowLayout());
		wantedPanel.add(wanted);
		originalFrameRate.setSelectedIndex(0);
		wantedFrameRate.setSelectedIndex(0);
		wantedPanel.add(wantedFrameRate);
		wantedPanel.setLayout(new FlowLayout());
		dynamicPanel.add(originalPanel);		
		dynamicPanel.add(wantedPanel);
		dynamicPanel.setLayout(new BoxLayout(dynamicPanel, BoxLayout.Y_AXIS));
		dynamicPanel.setVisible(false);
		
		
		JPanel startPanel = new JPanel();
		startPanel.add(startButton);
		JPanel openPanel = new JPanel();
		JPanel openedPanel = new JPanel();
		openPanel.add(openFileButton);
		openedFile.setPreferredSize(new Dimension(1000, 20));
		openedFile.setFont(italic);
		openedPanel.add(openedFile);
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		panel.add(openPanel);
		panel.add(openedPanel);
		panel.add(convertTypePanel);
		panel.add(dynamicPanel);
		panel.add(staticPanel);
		panel.add(startPanel);
		
		
		window.add(panel);
		window.setSize(300,260);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	private boolean conditionsOK()
	{
		
		boolean isOk = false;
		switch (convertType)
		{
		case STATIC:
			isOk = (gain.isSelected() || lose.isSelected()) 
					&& (fileHandler.getInputFile() != null) 
					&& !("".equals(secondsField.getText())) 
					&& fileHandler.getOutputFilePath() != null 
					&& !("".equals(fileHandler.getOutputFilePath()));
			break;
		case DYNAMIC:
			isOk = fileHandler.getInputFile() != null;
			break;
		default:
			break;
		}
		return isOk;
	}
}
