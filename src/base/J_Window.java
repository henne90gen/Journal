package base;

import javax.swing.*;

public class J_Window extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private J_ActionListener actionListener = new J_ActionListener();
	private JProgressBar progressBar = new JProgressBar();
	private JTextPane textPane = new JTextPane();
	private JList<?> list = new JList<Object>();
	
	public J_Window() {
		super("My First Application");
		JFrame.setDefaultLookAndFeelDecorated(true);
		
		this.setContentPane(createContentPane());
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(700, 500);
		setVisible(true);
	}
	
	public JPanel createContentPane() {
		JPanel mainGUI = new JPanel();
		
		mainGUI.setOpaque(true);
		mainGUI.setLayout(null);
		
		progressBar.setBounds(10, 11, 146, 14);
		mainGUI.add(progressBar);
		
		textPane.setEditable(false);
		textPane.setBounds(166, 11, 508, 439);
		mainGUI.add(textPane);
		
		list.setToolTipText("");
		list.setBounds(10, 36, 146, 414);
		mainGUI.add(list);
		return mainGUI;
	}
	
	public void setTextPane(String text) {
		textPane.setText(text);
	}
	
	public void setProgressBar(int value) {
		progressBar.setValue(value);
	}
}
