package base;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class J_Window extends JFrame{
	
	
	private static final long serialVersionUID = 1L;
	private J_ListSelectionListener listSelectionListener = new J_ListSelectionListener();
	public static JTextPane textPane = new JTextPane();
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
		//Main Panel:
		JPanel mainPanel = new JPanel();
		mainPanel.setOpaque(true);
		mainPanel.setSize(this.getWidth(), this.getHeight());
		
		//Layout:
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		mainPanel.setLayout(layout);
		mainPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		
		
		//List:
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(2);
		list.addListSelectionListener(listSelectionListener);
		
		JScrollPane listScrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		listScrollPane.setPreferredSize(new Dimension(130, 430));
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 1;
		mainPanel.add(listScrollPane, gbc);

		gbc.gridheight = 2;
		gbc.gridx = 1;
		gbc.gridy = 0;
		textPane.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setPreferredSize(new Dimension(500, 430));
		mainPanel.add(scrollPane, gbc);
		
		return mainPanel;
	}
}
