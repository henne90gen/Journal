package base;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.*;

public class J_Window extends JFrame{
	
	
	private static final long serialVersionUID = 1L;
	private J_ListSelectionListener listSelectionListener = new J_ListSelectionListener();
	private JProgressBar progressBar = new JProgressBar();
	private JTextPane textPane = new JTextPane();
	public static JList<String> list = new JList<String>();
	
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
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(-1);
		list.addListSelectionListener(listSelectionListener);
		mainGUI.add(list);
		
		/*JScrollPane listScroller = new JScrollPane();
		listScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		listScroller.setPreferredSize(new Dimension(250, 80));
		mainGUI.add(listScroller);*/
		
		return mainGUI;
	}
	
	public void setTextPane(String text) {
		textPane.setText(text);
	}
	
	public void setProgressBar(int value) {
		progressBar.setValue(value);
	}
	
	public void setListData(String[] listData) {
		list.setListData(listData);
	}
}
