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
		mainPanel.setSize(this.getWidth(), this.getHeight());
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 1;
		mainPanel.add(list, gbc);
		
		//List:
		list.setPreferredSize(new Dimension(150, 430));
		list.setToolTipText("");
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(-1);
		list.addListSelectionListener(listSelectionListener);

		gbc.gridheight = 2;
		gbc.gridx = 1;
		gbc.gridy = 0;
		textPane.setEditable(false);
		textPane.setPreferredSize(new Dimension(500, 430));;
		JScrollPane scrollPane = new JScrollPane(textPane);
		mainPanel.add(scrollPane, gbc);
		
		return mainPanel;
	}
}
