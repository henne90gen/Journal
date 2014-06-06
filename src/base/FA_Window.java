package base;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class FA_Window extends JFrame{
	
	
	FA_Window() {
		super("My First Application");
		JFrame.setDefaultLookAndFeelDecorated(true);
		
		setContentPane(createContentPane());
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(250, 190);
		setVisible(true);
	}
	
	public JPanel createContentPane() {
		JPanel mainGUI = new JPanel();
		mainGUI.setLayout(null);
		
		JPanel titlePane = new JPanel();
		titlePane.setLayout(null);
		titlePane.setLocation(10, 0);
		titlePane.setSize(250, 30);
		mainGUI.add(titlePane);
		
		JLabel redLabel = new JLabel("Red Team");
		redLabel.setLocation(0, 0);
		redLabel.setSize(100, 30);
		redLabel.setHorizontalAlignment(0);
		redLabel.setForeground(Color.red);
		titlePane.add(redLabel);
		
		JLabel blueLabel = new JLabel("Blue Team");
		blueLabel.setLocation(120, 0);
		blueLabel.setSize(100, 30);
		blueLabel.setHorizontalAlignment(0);
		blueLabel.setForeground(Color.blue);
		titlePane.add(blueLabel);
		
		return mainGUI;
	}
}
