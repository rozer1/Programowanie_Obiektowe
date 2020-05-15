package server;

import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * klasa reprezentuje watek dla klienta
 */
public class SocketTread implements Runnable
    {
    private Socket socket;
    private SaveHelper saveHelper;
    private TaskInfo task;

    /**
     * @param socket socket z polaczenia z klientem
     * @param saveHelper organizer ruchu na dysku
     */
    public SocketTread(Socket socket, SaveHelper saveHelper)
        {
        this.socket = socket;
        this.saveHelper = saveHelper;
        }

    @Override
    public void run()
        {

        String head = getHead();
        String[] data = head.split(":");

        if (data[DataType.TYPE.value].trim().equals("save"))
            {
            task = new TaskInfo(
                    data[DataType.OWNER.value].trim(),
                    data[DataType.FILE_NAME.value].trim(),
                    socket,
                    data[DataType.FRIEND.value]);

            saveHelper.saveFile(task);
            }

        if (data[DataType.TYPE.value].trim().equals("user"))
            {
            try
                {

                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                String listUsers = SaveHelper.listUsers();
                writer.println(listUsers);
                }
            catch (IOException e)
                {
                Logger.getAnonymousLogger().log(Level.WARNING, "error with socket");
                }
            }

        if (data[DataType.TYPE.value].trim().equals("file"))
            {
            ServerGraphic.setField("udostepniam pliki");
            try
                {
                SaveHelper.lockForLogs.lock();
                String user = data[DataType.OWNER.value].trim();
                Path path =
                        Paths.get(System.getProperty("user.home")).resolve("log").resolve(user + ".txt");
                if (Files.exists(path))
                    {
                    try
                        {
                        Integer count = saveHelper.getCount(user);

                        Optional<Path> filePath =
                                Files.lines(path).map(e -> Paths.get(e)).skip(count).findFirst();

                        if (filePath.isPresent())
                            {
                            sendFile(Files.newInputStream(filePath.get()), filePath.get().getFileName().toString());
                            }
                        else
                            {
                            endOfList();
                            }
                        }
                    catch (IOException e)
                        {
                        e.printStackTrace();
                        }
                    }
                else
                    {
                    Files.createFile(path);
                    endOfList();
                    }
                }
            catch (IOException e)
                {
                e.printStackTrace();
                }
            finally
                {
                SaveHelper.lockForLogs.unlock();
                }
            }
        }

    private void endOfList() throws IOException
        {
        byte[] close = ("close:" + ":" + "\n").getBytes();
        socket.getOutputStream().write(close, 0, close.length);
        socket.close();
        }

    private void sendFile(InputStream in, String name)
        {
        try
            {
            byte[] head = ("file:" + name + ":" + "\n").getBytes();
            socket.getOutputStream().write(head, 0, head.length);


            int count;
            byte[] bytes = new byte[4096];
            while ((count = in.read(bytes)) > 0)
                {
                socket.getOutputStream().write(bytes, 0, count);
                }
            socket.close();
            }
        catch (Exception e)
            {
            e.printStackTrace();
            }
        }

    private String getHead()
        {
        byte[] charFromHead = new byte[1];
        byte[] head = new byte[1024];

        int i = 0;
        try
            {
            while (socket.getInputStream().read(charFromHead) > 0 & charFromHead[0] != '\n')
                {
                head[i] = charFromHead[0];
                i++;
                }

            }
        catch (IOException e)
            {
            Logger.getAnonymousLogger().log(Level.WARNING, "save file exception");
            }
        return new String(head);
        }

    enum DataType
        {
            OWNER(1), FILE_NAME(3), FRIEND(2), TYPE(0);

        int value;

        DataType(int value)
            {
            this.value = value;
            }
        }
    }

