package server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.nio.file.StandardOpenOption.APPEND;

/**
 * Klasa organizujaca ruch na dysku, rozdziela pliki rowno na 5 dyskow
 */
public class SaveHelper
    {
    private LinkedBlockingQueue<Path> paths;
    private static Path logPath;
    private PriorityBlockingQueue<TaskInfo> tasks;
    private Map<String, Integer> usersTransfers;
    public static volatile Lock lockForLogs = new ReentrantLock();
    private Map<String, Integer> countDownloaded = new HashMap<>();


    /**
     * @param paths lista 5 serwerow
     */
    public SaveHelper(ArrayList<Path> paths)
        {
        usersTransfers = new HashMap<>();
        logPath = Paths.get(System.getProperty("user.home")).resolve("log");
        tasks = new PriorityBlockingQueue<>();
        this.paths = new LinkedBlockingQueue<>(paths);
        }

    /**
     * @param task generuje task do zapisu pliku na dysk, wyznacza piorytet tasku
     */
    public synchronized void saveFile(TaskInfo task)
        {
        lockForLogs.lock();
        countDownloaded.clear();//liczba pikow sie zmienia
        Integer priority = usersTransfers.getOrDefault(task.getUserName(), 0);
        usersTransfers.put(task.getUserName(), ++priority);
        task.setPriority(priority);
        tasks.add(task);
        new Thread(new SaveFile(paths, tasks)).start();
        lockForLogs.unlock();
        }

    /**
     *
     * dodanie logow o zapisie na dysku
     *
     * @param user user dla ktorego zapisujemy
     * @param file plik, ktory zostal zapisany
     * @throws IOException
     */
    public static synchronized void addUserFilesRegister(String user, Path file) throws IOException
        {
        try
            {
            ServerGraphic.setField(user + " zapisuje plik");
            lockForLogs.lock();
            OutputStream os = Files.newOutputStream(logPath.resolve(user + ".txt"), APPEND);
            PrintWriter writer = new PrintWriter(os);
            writer.println(file.toString());
            writer.flush();
            writer.close();
            }
        finally
            {
            lockForLogs.unlock();
            }
        }

    /**
     * sprawdzamy czy user ma juz ten plik
     *
     * @param user user dla ktorego sprawdzamy
     * @param fileName nazwa plku ktory sprawdzamy
     * @return czy uzytkownik posiada ten plik
     * @throws IOException
     */
    public static synchronized boolean fileExist(String user, String fileName) throws
            IOException
        {
        try
            {
            lockForLogs.lock();
            if (Files.exists(logPath.resolve(user + ".txt")))
                {
                return Files.lines(logPath.resolve(user + ".txt")).map(e -> Paths.get(e).getFileName().toString()).anyMatch(e -> e.equals(fileName));
                }
            }
        finally
            {
            lockForLogs.unlock();
            }
        return false;
        }

    /**
     * @return zwraca liste zarejestrowanych userow
     * @throws IOException
     */
    public static synchronized String listUsers() throws IOException
        {
        lockForLogs.lock();
        ServerGraphic.setField("pobieranie listy userow");
        StringBuilder stringBuilder = new StringBuilder();
        Files.list(logPath).map(e -> e.getFileName().toString().replaceAll(".txt", "")).forEach(e -> stringBuilder.append(e + ":"));
        lockForLogs.unlock();
        return stringBuilder.toString();
        }

    /**
     * @param user user dla ktorego sprawdzamy ile plikow pobral z serweras
     * @return ilosc pobranych plikow
     */
    public Integer getCount(String user)
        {
        Integer count = countDownloaded.getOrDefault(user, 0);
        countDownloaded.put(user, ++count);
        return countDownloaded.get(user) - 1;
        }
    }
