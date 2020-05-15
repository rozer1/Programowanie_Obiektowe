package user;

import java.io.IOException;

public interface User
    {

    String getName();

    boolean refreshFileList() throws IOException;

    boolean refreshServerFileList() throws IOException, InterruptedException;

    boolean getUsersFromServer();

    boolean sendFileToServer(FileEntity myFile);

    boolean sendFileToServer(String name, String to);
    }
