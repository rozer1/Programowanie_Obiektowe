package server;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

import lombok.Getter;
import lombok.Setter;


/**
 * Klasa reprezentuje dane potrzebne do zapisania pliku na dysk serwera
 */
@Getter
public class TaskInfo implements Comparable<TaskInfo>
    {
    private String userName;
    private String fileName;
    private Socket socket;
    private String sendTo = "";
    @Setter
    private Integer priority;
    private final AtomicReference<Boolean> done = new AtomicReference<>(false);

    /**
     * @param userName nazwa uzytkownika
     * @param fileName nazwa zapisywango pliku
     * @param socket   referencja na socket uzytkownika
     * @param sendTo   nazwa uzytkownika ktoremu udostepniamy plik
     */
    public TaskInfo(String userName, String fileName, Socket socket, String sendTo)
        {
        this.userName = userName;
        this.fileName = fileName;
        this.socket = socket;
        this.sendTo = sendTo;
        }

    /**
     * jezeli nie udostepniamy innym to uzywamy tego konstruktora
     *
     * @param userName nazwa uzytkownika
     * @param fileName nazwa zapisywango pliku
     * @param socket   referencja na socket uzytkownika
     */
    public TaskInfo(String userName, String fileName, Socket socket)
        {
        this.userName = userName;
        this.fileName = fileName;
        this.socket = socket;
        }

    /**
     * @return zwraca true jezeli plik jest przeslany tz. task jest wykonany
     */
    public AtomicReference<Boolean> getDone()
        {
        return done;
        }

    @Override
    public int compareTo(TaskInfo o)//20  1 =-1 | 2 99 = 1
    {
    return this.priority-o.priority;
    }
    }
