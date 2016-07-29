package base;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class NewEntryWindow extends JFrame implements ActionListener {

    private JTextArea m_comment;
    private JRadioButton[] m_feelings;
    private Journal m_journal;

    public NewEntryWindow(Journal j) {
        super("How was your Day?");
        m_journal = j;
        setPreferredSize(new Dimension(400, 250));
        setMinimumSize(new Dimension(400, 250));
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        add(getChoicePanel());
        add(getTextPanel());

        pack();
        setVisible(true);
    }

    private JPanel getChoicePanel() {
        JPanel panel = new JPanel(new GridLayout(7, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        m_feelings = new JRadioButton[Entry.Mood.values().length];
        for (int i = 0; i < Entry.Mood.values().length; i++) {
            m_feelings[i] = new JRadioButton(Entry.Mood.values()[i].toString());
        }
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < m_feelings.length; i++) {
            group.add(m_feelings[i]);
            panel.add(m_feelings[i]);
        }
        m_feelings[(int)(m_feelings.length/2)].setSelected(true);

        return panel;
    }

    private JPanel getTextPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        m_comment = new JTextArea(10, 20);
        m_comment.setEditable(true);
        m_comment.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(m_comment);
        panel.add(scrollPane);

        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        JButton m_write = new JButton("Write to database");
        m_write.setAlignmentX(Component.CENTER_ALIGNMENT);
        m_write.addActionListener(this);
        panel.add(m_write);

        return panel;
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
    public void actionPerformed(ActionEvent arg0) {
        EntryWriter.write(new Entry(LocalDate.now(), m_comment.getText(), getSelectedMood()));

        m_journal.getEntries().add(EntryReader.readLast());

        System.exit(0);
    }
}

