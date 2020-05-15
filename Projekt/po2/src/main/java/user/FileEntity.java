package user;

import java.nio.file.Path;

/**
 * klasa reprezentuje plik
 */
public class FileEntity
    {
    private String name;
    private String owner;
    private String otherUser = "";
    public static Path path;

    public String getOwner()
        {
        return owner;
        }

    public Path getFilePath()
        {
        return path.resolve(name);
        }

    public void setOwner(String owner)
        {
        this.owner = owner;
        }

    public String getOtherUser()
        {
        return otherUser;
        }

    public void setOtherUser(String otherUser)
        {
        this.otherUser = otherUser;
        }

    public FileEntity(String name)
        {
        this.name = name;
        }

    /**
     * @param name nazwa usera
     * @param owner owner pliku
     * @param otherUser user do ktorego wyslamy
     */
    public FileEntity(String name, String owner, String otherUser)
        {
        this.name = name;
        this.owner = owner;
        this.otherUser = otherUser;
        }

    public String getName()
        {
        return name;
        }

    public void setName(String name)
        {
        this.name = name;
        }
    }
