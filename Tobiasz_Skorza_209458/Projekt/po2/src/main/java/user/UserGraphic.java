package user;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.*;

import static javax.swing.JOptionPane.showMessageDialog;


/**
 * reperezentuje wyswietlacz stanu usera
 */
public class UserGraphic extends JFrame
    {

    private JList<String> fileList;
    private JList<String> userList;
    private DefaultListModel<String> fliesListData;
    private DefaultListModel<String> userListData;
    private User user;
    private JTextField action;
    private Lock lock = new ReentrantLock();



    public UserGraphic(User user)
        {
        super(user.getName());
        this.user = user;

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter(){

                @Override
                public void windowClosing(WindowEvent e) {
                    File path = new File(System.getProperty("user.home") + File.separator + "log" + File.separator + String.valueOf(user.getName()) + ".txt");
                    showMessageDialog(null, "User: "+String.valueOf(user.getName())+" was logged out.");
                    System.out.println(path);
                    deleteFromUserList(String.valueOf(user.getName()));
                    path.delete();
                    e.getWindow().dispose();
                    System.exit(0);
                }
            });

       // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(new Dimension(500, 800));

        fliesListData = new DefaultListModel<>();

        fileList = new JList(fliesListData); //data has type Object[]
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        fileList.setVisibleRowCount(-1);
        fileList.setBounds(0, 200, 300, 440);
        fileList.setBackground(Color.LIGHT_GRAY);

        userListData = new DefaultListModel<>();

        userList = new JList(userListData);
        userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        userList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        userList.setVisibleRowCount(-1);
        userList.setBounds(0, 0, 200, 150);
        userList.setBackground(Color.LIGHT_GRAY);


        JButton share = new JButton("Udostepnij");
        share.setBounds(0, 650, 100, 50);
        share.addActionListener(new AbstractAction()
            {
            @Override
            public void actionPerformed(ActionEvent e)
                {
                if (fileList.getSelectedValue() == null || userList.getSelectedValue() == null)
                    {
                    return;
                    }
                user.sendFileToServer(fileList.getSelectedValue(), userList.getSelectedValue());
                action.setText("wyslano " + fileList.getSelectedValue() + " do " + userList.getSelectedValue());
                }
            });

        action = new JTextField("pobieranie pikow z serwera");
        action.setBounds(260, 660, 200, 50);

        setLayout(null);
        add(fileList);
        add(share);
        add(userList);
        add(action);

        setVisible(true);
        }

    /**
     * doanie pliku do listy wyswietlanej
     *
     * @param fileName nawa pliku
     */
    public void showFile(String fileName)
        {
        lock.lock();
        fliesListData.addElement(fileName);
        action.setText("dodano plik " + fileName);
        lock.unlock();
        }

    /**
     * dodanie nazy usera do listy userow
     *
     * @param userName nazwa usera
     */
    public void addToUserList(String userName)
        {
        lock.lock();
        userListData.addElement(userName);
            System.out.println(userName);
        action.setText("dodano uzytwownika " + userName);
        lock.unlock();
        }

    public void deleteFromUserList(String userName)
        {
        lock.lock();
        userListData.removeElement(userName);
        System.out.println(userName);
        action.setText("usunieto uzytkownika " + userName);
        lock.unlock();
        }

    /**
     * usuwa plik z listy

     * @param fileName nazwa plku
     */
    public void deleteFile(String fileName)
        {
        lock.lock();
        fliesListData.removeElement(fileName);
        action.setText("usunieto plik " + fileName);
        lock.unlock();
        }

    public void cleanList()
        {
        lock.lock();
        userListData.clear();
        lock.unlock();
        }

    /**
     * @return licza wyswietlonych userow
     */
    public int userCount()
        {
        return userListData.size();
        }

    /**
     * pokazuje powiadomienie
     *
     * @param text text do wyswietlenia
     */
    public void writePrompt(String text)
        {
        lock.lock();
        action.setText(text);
        lock.unlock();
        }

    }
