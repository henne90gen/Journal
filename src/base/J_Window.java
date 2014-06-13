package base;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class J_Window extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private J_ListSelectionListener listSelectionListener = new J_ListSelectionListener();
	private J_ActionListener actionListener = new J_ActionListener();
	public static JTextPane textPane = new JTextPane();
	public static JList<String> list = new JList<String>();
	public static JTextField searchBar = new JTextField();
	private JButton searchButton = new JButton();
	
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
		GridBagConstraints gbcList = new GridBagConstraints();
		GridBagConstraints gbcSearch = new GridBagConstraints();
		GridBagConstraints gbcText = new GridBagConstraints();
		GridBagConstraints gbcButton = new GridBagConstraints();
		mainPanel.setLayout(layout);
		mainPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		
		//List:
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(2);
		list.addListSelectionListener(listSelectionListener);
		
		JScrollPane listScrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		listScrollPane.setPreferredSize(new Dimension(130, 430));
		gbcList.fill = GridBagConstraints.BOTH;
		gbcList.gridheight = 2;
		gbcList.gridx = 0;
		gbcList.gridy = 0;
		mainPanel.add(listScrollPane, gbcList);
		
		//Searchbar:
		searchBar.setPreferredSize(new Dimension(400, 20));
		searchBar.setToolTipText("Datumsformat: DD.MM.JJJJ");
		gbcSearch.fill = GridBagConstraints.BOTH;
		gbcSearch.gridx = 1;
		gbcSearch.gridy = 0;
		mainPanel.add(searchBar, gbcSearch);
		
		//Searchbutton:
		searchButton.setPreferredSize(new Dimension(20, 20));
		searchButton.setText("Search");
		searchButton.addActionListener(actionListener);
		gbcButton.fill = GridBagConstraints.BOTH;
		gbcButton.gridx = 2;
		gbcButton.gridy = 0;
		mainPanel.add(searchButton, gbcButton);
		
		//Textfield:
		gbcText.fill = GridBagConstraints.BOTH;
		gbcText.gridwidth = 2;
		gbcText.gridx = 1;
		gbcText.gridy = 1;
		textPane.setEditable(false);
		JScrollPane textScrollPane = new JScrollPane(textPane);
		textScrollPane.setPreferredSize(new Dimension(500, 430));
		mainPanel.add(textScrollPane, gbcText);
		
		return mainPanel;
	}
}
