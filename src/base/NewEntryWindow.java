package base;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.time.LocalDate;

public class NewEntryWindow extends JFrame implements ActionListener {

    public static final String WRITE_BUTTON = "Save";

    private JTextArea comment;
    private JRadioButton[] feelings;
    private Journal journal;

    public NewEntryWindow(Journal journal) {
        super("How was your Day?");
        this.journal = journal;

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(400, 250));
        setMinimumSize(new Dimension(400, 250));

        add(createGUI());

        pack();
        setVisible(true);
    }

    private JPanel createGUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        // Feeling choices
        JPanel choicePanel = new JPanel();
        choicePanel.setLayout(new GridLayout(7, 0));
        choicePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        feelings = new JRadioButton[Entry.Mood.values().length];
        for (int i = 0; i < Entry.Mood.values().length; i++) {
            feelings[i] = new JRadioButton(Entry.Mood.values()[i].toString());
        }
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < feelings.length; i++) {
            group.add(feelings[i]);
            choicePanel.add(feelings[i]);
        }
        feelings[(int)(feelings.length/2)].setSelected(true);

        // Comment text
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        comment = new JTextArea(10, 20);
        comment.setEditable(true);
        comment.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(comment);
        textPanel.add(scrollPane);

        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JButton writeBtn = new JButton(WRITE_BUTTON);
        writeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        writeBtn.addActionListener(this);
        textPanel.add(writeBtn);

        mainPanel.add(choicePanel);
        mainPanel.add(textPanel);
        return mainPanel;
    }

    private Entry.Mood getSelectedMood() {
        for (int i = 0; i < feelings.length; i++) {
            if (feelings[i].isSelected()) {
                return Entry.Mood.values()[i];
            }
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        EntryWriter.write(new Entry(journal.getNextID(), LocalDate.now(), getSelectedMood(), comment.getText()));

        journal.entries.add(EntryReader.readLast());

        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}

