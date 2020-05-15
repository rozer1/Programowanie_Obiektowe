package server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import javax.swing.*;

/**
 * klasa wyswietla stan serwera na ekran
 *
 */
public class ServerGraphic extends JFrame
    {


    private JList<String> fileList1;

    private DefaultListModel<String> fliesListData1;
        private DefaultListModel<String> fliesListData2;

    private Path serverPath;

    /**
     * @param text umozliwia ustawienie komunikatu o aktualnej akcji
     */
    public static synchronized void setField(String text)
        {
        ServerGraphic.field.setText(text);
        }

    private static JTextField field = new JTextField("serwer gotowy");

    public ServerGraphic() throws IOException
        {
        super("Server GUI");
        field.setEditable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(new Dimension(250, 600));


        serverPath = Paths.get(System.getProperty("user.home"));

        fliesListData1 = new DefaultListModel<>();

        fileList1 = new JList(fliesListData1); //data has type Object[]
        fileList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList1.setLayoutOrientation(JList.WHEN_FOCUSED);
        fileList1.setVisibleRowCount(1);
        fileList1.setBounds(0, 0, 250, 440);
        fileList1.setBackground(Color.orange);

            listFiles(serverPath, 0, fliesListData1);
            fliesListData2 = new DefaultListModel<>();

            fileList2 = new JList(fliesListData2); //data has type Object[]
            fileList2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            fileList2.setLayoutOrientation(JList.WHEN_FOCUSED);
            fileList2.setVisibleRowCount(1);
            fileList2.setBounds(0, 0, 250, 440);
            fileList2.setBackground(Color.orange);

            listFiles(serverPath, 0, fliesListData2);


        field.setBounds(0, 500, 300, 50);
        field.setDisabledTextColor(Color.LIGHT_GRAY);


        setLayout(null);
        add(fileList1);
            add(fileList2);
        add(field);

        setVisible(true);
        }

    /**
     * aktualizuje graficzna zawartosc serwera
     *
     * @throws IOException
     */
    public void refresh() throws IOException
        {
        listFiles(serverPath, 0, fliesListData1);
        }

    private void listFiles(Path serverPath, int server, DefaultListModel<String> list) throws IOException
        {
        SaveHelper.lockForLogs.lock();
        list.clear();
        Files.list(serverPath.resolve("server" + server)).flatMap((e) ->
        {
        try
            {
            return Files.list(e);
            }
        catch (IOException ex)
            {
            ex.printStackTrace();
            }
        return null;
        }).map(e -> e.getFileName().toString()).filter(Objects::nonNull).forEach(e -> list.addElement(e));
        SaveHelper.lockForLogs.unlock();
        }
    }
