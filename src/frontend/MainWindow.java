package frontend;

import backend.Entry;
import backend.MainController;
import backend.MainView;
import backend.entries.EntryType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by dovydas on 11/03/17.
 */
public class MainWindow extends JFrame implements MainView, Observer {
    private MainController _controller;
    private JTabbedPane _tabbedPane = new JTabbedPane();
    private DefaultListModel<Entry> _calendarModel = new DefaultListModel<>();
    private DefaultListModel<Entry> _reminderModel =  new DefaultListModel<>();

    private JList<Entry> _calendarEntries = new JList<>(_calendarModel);
    private MouseListener _calendarMouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (e.getClickCount() == 2)
                _calendarModel.remove(_calendarEntries.getSelectedIndex());
        }
    };

    private JList<Entry> _reminderEntries = new JList<>(_reminderModel);
    private MouseListener _reminderMouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (e.getClickCount() == 2)
                _reminderModel.remove(_reminderEntries.getSelectedIndex());
        }
    };

    private JTextField _inputField = new JTextField();
    private ActionListener _inputActionListener = e -> {
        //if input is empty do nothing
        if (_inputField.getText().equals(""))
            return;

        //create, add element to the selected tab and clear input
        int tabIndex = _tabbedPane.getSelectedIndex();
        if (tabIndex == 0) {
            _calendarModel.addElement(
                    _controller.makeEntry(_inputField.getText(), EntryType.CALENDAR)
            );
        }
        else {
            _reminderModel.addElement(
                    _controller.makeEntry(_inputField.getText(), EntryType.REMINDER)
            );
        }

        _inputField.setText("");
    };

    public MainWindow(MainController controller) {
        super("");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        _controller = controller;
        _controller.setMainView(this);

        _tabbedPane.addTab("Calendar", _calendarEntries);
        _calendarEntries.addMouseListener(_calendarMouseListener);
        _tabbedPane.addTab("Reminders", _reminderEntries);
        _reminderEntries.addMouseListener(_reminderMouseListener);

        add(_tabbedPane, BorderLayout.CENTER);
        add(_inputField, BorderLayout.SOUTH);
        _inputField.addActionListener(_inputActionListener);

        setPreferredSize(new Dimension(640, 480));
        pack();

        setVisible(true);
    }

    @Override
    public Observer getObserver() {
        return this;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
