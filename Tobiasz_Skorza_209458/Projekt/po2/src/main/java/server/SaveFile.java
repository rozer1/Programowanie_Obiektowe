package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.abs;


/**
 * Klasa realizuje zapis jednego pliku, pobiera sciezki do serwerow i pule taskow.
 */
public class SaveFile implements Runnable
    {
    private LinkedBlockingQueue<Path> paths;
    private PriorityBlockingQueue<TaskInfo> tasks;
    private OutputStream out;
    private InputStream in;
    byte[] bytes = new byte[4096];

    /**
     * @param paths Lista serwerow
     * @param tasks Lista zadani do zapisania
     */
    public SaveFile(LinkedBlockingQueue<Path> paths, PriorityBlockingQueue<TaskInfo> tasks)
        {
        this.paths = paths;
        this.tasks = tasks;
        }

    @Override
    public void run()
        {
        try
            {
            Path path = paths.take();//czeka az bedzie woly server
            TaskInfo task = tasks.take();//pobranie taska o naj. piorytecie
            Path filePath = makeFile(path, task);
            in = task.getSocket().getInputStream();
            out = null;

            if (!SaveHelper.fileExist(task.getUserName(), task.getFileName()))
                {
                wait(task);
                saveAndCopy(path, task, filePath);
                }
            else if (task.getSendTo().length() > 0 && !SaveHelper.fileExist(task.getSendTo(),
                    task.getFileName()))
                {
                wait(task);
                saveOnlyToOtherUser(path, task);
                }

            if (out != null)
                {
                out.close();
                }
            paths.put(path);
            task.getSocket().close();
            task.getDone().set(true);
            }
        catch (Exception e)
            {
            e.printStackTrace();
            Logger.getAnonymousLogger().log(Level.WARNING, "problem with save");
            }

        }

    private void saveOnlyToOtherUser(Path path, TaskInfo task) throws IOException
        {
        out =
                Files.newOutputStream(path.resolve(task.getSendTo()).resolve(task.getFileName()));
        writeFromUser();
        SaveHelper.addUserFilesRegister(task.getSendTo(),
                path.resolve(task.getSendTo()).resolve(task.getFileName()));
        }

    private void saveAndCopy(Path path, TaskInfo task, Path filePath) throws IOException
        {
        out = Files.newOutputStream(filePath, StandardOpenOption.CREATE);
        writeFromUser();
        SaveHelper.addUserFilesRegister(task.getUserName(), filePath);

        if (task.getSendTo().length() > 0 && !SaveHelper.fileExist(task.getSendTo(), task.getFileName()))
            {
            Files.copy(filePath, path.resolve(task.getSendTo()).resolve(task.getFileName()),
                    StandardCopyOption.REPLACE_EXISTING);
            //SaveHelper.addUserFilesRegister(task.getSendTo(), path.resolve(task.getSendTo()).resolve(task.getFileName()));
            }
        }

    private void writeFromUser() throws IOException
        {
        int count;//ilosc danych odczytu

        while ((count = in.read(bytes)) > 0)
            {
            out.write(bytes, 0, count);
            }
        }

    private Path makeFile(Path path, TaskInfo task) throws IOException
        {
        if (!Files.exists(path.resolve(task.getUserName())))
            {
            Files.createDirectories(path.resolve(task.getUserName()));
            }

        if (!Files.exists(path.resolve(task.getSendTo())) && task.getSendTo().length() > 0)
            {
            Files.createDirectories(path.resolve(task.getSendTo()));
            }

        Path filePath = path.resolve(task.getUserName()).resolve(task.getFileName());

        return filePath;
        }

    private void wait(TaskInfo task)
        {
        ServerGraphic.setField(task.getUserName() + " zapisuje plik" + task.getFileName() + " ma" +
                " " +
                "piorytet " + task.getPriority());
        try
            {
            Thread.sleep(abs(new Random().nextInt()) % 1000 * 20);
            }
        catch (InterruptedException e)
            {
            e.printStackTrace();
            }
        }
    }
