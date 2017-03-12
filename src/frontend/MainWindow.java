package frontend;

import backend.Entry;
import backend.MainController;
import backend.MainView;
import backend.entries.CalendarEntry;
import backend.entries.EntryType;
import backend.entries.ReminderEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by dovydas on 11/03/17.
 */
public class MainWindow extends JFrame implements MainView, Observer, WindowListener {
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
        if (_inputField.getText().equals("")){
            if (_tabbedPane.getSelectedIndex() == 0) {
                _calendarEntries.clearSelection();
            } else {
                _reminderEntries.clearSelection();
            }

            return;
        }

        boolean edited = false;
        int tabIndex = _tabbedPane.getSelectedIndex();
        //edit entry
        if (tabIndex == 0) {
            Entry entry = _calendarEntries.getSelectedValue();
            //if nothing is selected edit existing entry
            if (entry != null) {
                _controller.editEntry(_inputField.getText(), entry);
                edited = true;
            }
        } else {
            Entry entry = _reminderEntries.getSelectedValue();
            //if nothing is selected edit existing entry
            if (entry != null) {
                _controller.editEntry(_inputField.getText(), entry);
                edited = true;
            }
        }

        //if entry was not edited then make new entry with input
        if (!edited) {
            String input = _inputField.getText();
            if (input.matches("[Rr]emind [Mm]e [Tt]o.+")) {
                Entry item = _controller.makeEntry(input, EntryType.REMINDER);
                _reminderModel.addElement(item);
                _reminderEntries.setSelectedIndex(_reminderModel.indexOf(item));
            } else {
                Entry item = _controller.makeEntry(input, EntryType.CALENDAR);
                _calendarModel.addElement(item);
                _calendarEntries.setSelectedIndex(_calendarModel.indexOf(item));
            }
        }

        _inputField.setText("");
    };

    private KeyListener _keyListener = new KeyAdapter() {

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                _calendarEntries.clearSelection();
                _reminderEntries.clearSelection();
            }
        }
    };

    public MainWindow() {
        super("");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        _tabbedPane.addTab("Calendar", _calendarEntries);
        _calendarEntries.addMouseListener(_calendarMouseListener);
        _tabbedPane.addTab("Reminders", _reminderEntries);
        _reminderEntries.addMouseListener(_reminderMouseListener);

        add(_tabbedPane, BorderLayout.CENTER);
        add(_inputField, BorderLayout.SOUTH);
        _inputField.addActionListener(_inputActionListener);
        addWindowListener(this);
        addKeyListener(_keyListener);
        _inputField.addKeyListener(_keyListener);
        _calendarEntries.addKeyListener(_keyListener);
        _reminderEntries.addKeyListener(_keyListener);

        setPreferredSize(new Dimension(640, 480));
        pack();

        setVisible(true);
    }

    @Override
    public void setMainController(MainController controller) {
        _controller = controller;
    }

    @Override
    public Observer getObserver() {
        return this;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (_tabbedPane.getSelectedIndex() == 0) {
            _calendarEntries.repaint();
        } else {
            _reminderEntries.repaint();
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        //sets focus on the textfield when window is opened;
        _inputField.requestFocus();

        File file = new File("entries.dat");
        if (!file.exists()) return;

        FileInputStream fileIn = null;
        ObjectInputStream in = null;

        try {
            fileIn = new FileInputStream("entries.dat");
            in = new ObjectInputStream(fileIn);

            while (true){
                Object o = in.readObject();
                if (o instanceof CalendarEntry) {
                    Entry entry = ((Entry) o);
                    entry.addObserver(this);
                    _calendarModel.addElement(entry);
                }
                else if (o instanceof ReminderEntry) {
                    Entry entry = ((Entry) o);
                    entry.addObserver(this);
                    _reminderModel.addElement(entry);
                }
            }
        } catch (EOFException i) {
            try {
                in.close();
                fileIn.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException|ClassNotFoundException i) {
            i.printStackTrace();
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            FileOutputStream fileOut = new FileOutputStream("entries.dat", false);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            Enumeration<Entry> calendarItems = _calendarModel.elements();
            while (calendarItems.hasMoreElements())
                out.writeObject(calendarItems.nextElement());

            Enumeration<Entry> reminderItems = _reminderModel.elements();
            while (reminderItems.hasMoreElements())
                out.writeObject(reminderItems.nextElement());

            out.close();
            fileOut.close();
        }catch(IOException i) {
            i.printStackTrace();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
