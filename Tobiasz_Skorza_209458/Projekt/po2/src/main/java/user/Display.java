package user;

public interface Display
    {
    void addToFileList(String fileName);

    void addToUserList(String userName);

    //void sendRequestForUser(String userName);

    void deleteFromFileList(String fileName);

    void writePrompt(String text);

    void cleanGui();

    int userCount();

    }
