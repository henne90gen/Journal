package base;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.time.LocalDate;

public class NewEntryWindow extends JFrame implements ActionListener {

    private JTextArea m_comment;
    private JRadioButton[] m_feelings;
    private Journal m_journal;

    public NewEntryWindow(Journal j) {
        super("How was your Day?");
        m_journal = j;

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
        m_feelings = new JRadioButton[Entry.Mood.values().length];
        for (int i = 0; i < Entry.Mood.values().length; i++) {
            m_feelings[i] = new JRadioButton(Entry.Mood.values()[i].toString());
        }
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < m_feelings.length; i++) {
            group.add(m_feelings[i]);
            choicePanel.add(m_feelings[i]);
        }
        m_feelings[(int)(m_feelings.length/2)].setSelected(true);

        // Comment text
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        m_comment = new JTextArea(10, 20);
        m_comment.setEditable(true);
        m_comment.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(m_comment);
        textPanel.add(scrollPane);

        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JButton writeBtn = new JButton("Write to database");
        writeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        writeBtn.addActionListener(this);
        textPanel.add(writeBtn);

        mainPanel.add(choicePanel);
        mainPanel.add(textPanel);
        return mainPanel;
    }

    private Entry.Mood getSelectedMood() {
        for (int i = 0; i < m_feelings.length; i++) {
            if (m_feelings[i].isSelected()) {
                return Entry.Mood.values()[i];
            }
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        EntryWriter.write(new Entry(LocalDate.now(), m_comment.getText(), getSelectedMood()));

        m_journal.getEntries().add(EntryReader.readLast());

        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}

