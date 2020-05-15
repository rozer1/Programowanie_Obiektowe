package user;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


import lombok.Getter;

/**
 * klasa reprezentuje klienta, zawiera wszytkie mozliwe akcje dla niego
 */
public class MyUser implements User
    {
    private String userName;
    @Getter
    private Path userPath;
    private List<FileEntity> fileList;
    private Lock lock = new ReentrantLock();
    private Display display;
    private Socket socket;
    private volatile boolean ready = false;
    Executor executor = Executors.newCachedThreadPool();

    /**
     * wywolanie symbolizuje ze pliki z serera zostaly pobrane wiec klient jest gotowy do
     * wyswietlenia na ekran
     */
    public void setReady()
        {
        ready = true;
        }


    /**
     * @param userName nazwa usera
     * @param path     scieka jego na dysku
     * @param display   interfejs do wyswietlania informacji
     * @throws IOException
     * @throws InterruptedException
     */
    public MyUser(String userName, String path, Display display) throws IOException, InterruptedException
        {
        this.userName = userName;
        this.userPath = Paths.get(System.getProperty("user.home"));
        this.display = display;
        socket = new Socket();
        makeUserFolder(path);
        refreshServerFileList();
        }

    @Override
    public String getName()
        {
        return userName;
        }

    /**
     * odwierza liste plikow, jezeli dodano nowe to wysla je na dysk i dodaje do Gui usera
     *
     * @return czy bylo wymagane odswierzenie
     * @throws IOException
     */
    public boolean refreshFileList() throws IOException
        {
        int oldSize;
        try
            {
            lock.lock();
            display.writePrompt("odswiezanie widoku");
            oldSize = fileList.size();
            Files.list(userPath).forEach(this::makeFileIfNotExist);
            }
        finally
            {
            lock.unlock();
            }
        return oldSize != fileList.size();
        }

    /**
     * usuwa pliki z gui ktore uzytkownik lokalnie usunol
     *
     * @return czy bylo wymagane odswierzenie
     * @throws IOException
     */
    public boolean cleanFileList() throws IOException
        {
        try
            {
            lock.lock();
            if (Files.list(userPath).count() == fileList.size())
                {
                return false;
                }

            fileList.removeAll(fileList.stream()
                    .filter(e -> ifFileWasDeletedRefreshGui(e.getName()))
                    .collect(Collectors.toList()));
            }
        finally
            {
            lock.unlock();
            }
        return true;
        }

    /**
     * pobiera z serwera pliki
     *
     * @return true oznacza gotowosc usera do dalszego dzialania
     * @throws InterruptedException
     */
    public boolean refreshServerFileList() throws InterruptedException
        {
        try
            {
            lock.lock();
            ready = false;

            while (!ready)
                {
                Task taskDispatcher = new Task(display,
                        new TaskData(null, userName, userName, this),
                        Task.OperationType.GET_FILES);
                Thread t = new Thread(taskDispatcher);
                t.start();
                t.join(1000);
                }

            }
        finally
            {
            lock.unlock();
            }

        return true;
        }

    /**
     * pobiera liste userow z serwera
     *
     * @return czy pobrano?
     */
    public boolean getUsersFromServer()
        {
        Task taskDispatcher = new Task(display,
                Task.OperationType.GET_USERS);

        new Thread(taskDispatcher).start();
        display.writePrompt("odswiezanie listy userow");
        return true;
        }

    /**
     * wysla plik do serwera
     *
     * @param myFile plik do wyslania
     * @return
     */
    @Override
    public boolean sendFileToServer(FileEntity myFile)
        {
        Task taskDispatcher = new Task(display, new TaskData(myFile.getFilePath(),
                userName, myFile.getOtherUser()));
        executor.execute(taskDispatcher);
        return true;
        }

    /**
     * @param name nazwa pliku
     * @param to   do kogo wyslamy
     * @return czy wyslano?
     * @see MyUser#sendFileToServer(FileEntity)
     */
    @Override
    public boolean sendFileToServer(String name, String to)
        {
        FileEntity myFile = new FileEntity(name, userName, to);
        sendFileToServer(myFile);
        return true;
        }

    private void makeUserFolder(String path) throws IOException
        {
        userPath = userPath.resolve(path);
        fileList = new ArrayList<>();
        Files.createDirectories(userPath);
        FileEntity.path = userPath;
        }

    private Boolean makeFileIfNotExist(Path path)
        {
        String fileName = path.getFileName().toString();

        Optional<String> file =
                fileList.stream()
                        .map(e -> e.getName())
                        .filter(e -> e.equals(fileName))
                        .findFirst();


        /*pliku nie ma na liscie wiec zostal nowo dodany*/

        if (!file.isPresent())
            {
            FileEntity myFileImpl = new FileEntity(fileName);
            fileList.add(myFileImpl);
            display.addToFileList(fileName);
            sendFileToServer(myFileImpl);
            return true;
            }

        return false;
        }

    private Boolean ifFileWasDeletedRefreshGui(String fileName)
        {
        Optional<String> file = null;
        try
            {
            file = Files.list(userPath)
                    .map(e -> e.getFileName().toString())
                    .filter(e -> e.equals(fileName))
                    .findFirst();
            }
        catch (IOException e)
            {
            display.writePrompt(e.getMessage());
            }
        /*plik jest na liscie wiec nie jest usuniety*/

        if (file.isPresent())
            {
            return false;
            }
        display.deleteFromFileList(fileName);
        return true;
        }
    }
