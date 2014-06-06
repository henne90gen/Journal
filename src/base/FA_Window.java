package base;

import java.awt.*;
import javax.swing.*;

public class FA_Window extends JFrame{
	
	private FA_ActionListener actionListener = new FA_ActionListener();
	
	public static JButton redButton = new JButton("Red Score!");
	public static JButton blueButton = new JButton("Blue Score!");
	public static JButton resetButton = new JButton("Reset Score");
	
	private static JLabel redScore = new JLabel("0");
	private static JLabel blueScore = new JLabel("0");
	
	private static int redScoreAmount = 0;
	private static int blueScoreAmount = 0;
	
	FA_Window() {
		super("My First Application");
		JFrame.setDefaultLookAndFeelDecorated(true);
		
		setContentPane(createContentPane());
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(285, 200);
		setVisible(true);
	}
	
	public JPanel createContentPane() {
		JPanel mainGUI = new JPanel();
		mainGUI.setLayout(null);
		
		//Panel for the title labels
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(null);
		titlePanel.setLocation(10, 0);
		titlePanel.setSize(250, 30);
		mainGUI.add(titlePanel);
		
		//Red title label
		JLabel redLabel = new JLabel("Red Team");
		redLabel.setLocation(0, 0);
		redLabel.setSize(100, 30);
		redLabel.setHorizontalAlignment(0);
		redLabel.setForeground(Color.red);
		titlePanel.add(redLabel);
		
		//Blue title label
		JLabel blueLabel = new JLabel("Blue Team");
		blueLabel.setLocation(120, 0);
		blueLabel.setSize(100, 30);
		blueLabel.setHorizontalAlignment(0);
		blueLabel.setForeground(Color.blue);
		titlePanel.add(blueLabel);
		
		//Panel for the score labels
		JPanel scorePanel= new JPanel();
		scorePanel.setLayout(null);
		scorePanel.setLocation(10, 40);
		scorePanel.setSize(250, 30);
		mainGUI.add(scorePanel);
		
		//Red score label
		redScore.setLocation(0,0);
		redScore.setSize(100, 30);
		redScore.setHorizontalAlignment(0);
		scorePanel.add(redScore);
		
		//Blue score label
		blueScore.setLocation(120,0);
		blueScore.setSize(100, 30);
		blueScore.setHorizontalAlignment(0);
		scorePanel.add(blueScore);
		
		//Panel to contain all the buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		buttonPanel.setLocation(10, 80);
		buttonPanel.setSize(250, 70);
		mainGUI.add(buttonPanel);
		
		//Red button
		redButton.setLocation(0, 0);
		redButton.setSize(120, 30);
		redButton.addActionListener(actionListener);
		buttonPanel.add(redButton);
		
		//Blue button
		blueButton.setLocation(130, 0);
		blueButton.setSize(120, 30);
		blueButton.addActionListener(actionListener);
		buttonPanel.add(blueButton);
		
		//Reset button
		resetButton.setLocation(0, 40);
		resetButton.setSize(250, 30);
		resetButton.addActionListener(actionListener);
		buttonPanel.add(resetButton);
		
		mainGUI.setOpaque(true);
		return mainGUI;
	}

	public static void addRedScore() {
		redScoreAmount = redScoreAmount + 1;
		redScore.setText(""+redScoreAmount);	
	}

	public static void addBlueScore() {
		blueScoreAmount = blueScoreAmount + 1;
		blueScore.setText(""+blueScoreAmount);
	}

	public static void resetScore() {
		redScoreAmount = 0;
		blueScoreAmount = 0;
		redScore.setText(""+redScoreAmount);
		blueScore.setText(""+blueScoreAmount);
	}
}
