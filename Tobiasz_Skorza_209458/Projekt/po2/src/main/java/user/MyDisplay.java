package user;

/**
 * Klasa reprezentuje interfejs do wyswietlanie informacji na ekran
 */
public class MyDisplay implements Display
    {
    private UserGraphic userGraphic;

    public void setUserGraphic(UserGraphic userGraphic)
        {
        this.userGraphic = userGraphic;
        }

    public synchronized void cleanGui()
        {
        waitIfUserGuiNull();
        userGraphic.writePrompt(" pobieram liste userow " );
        userGraphic.cleanList();
        }

    @Override
    public int userCount()
        {
        return userGraphic.userCount();
        }

    private void waitIfUserGuiNull()
        {
        while (userGraphic == null)
            {
            try
                {
                /*czekamy az gui sie robi*/
                Thread.sleep(1000);
                }
            catch (InterruptedException e)
                {
                e.printStackTrace();
                }
            }
        }


    public synchronized void addToFileList(String fileName)
        {
        waitIfUserGuiNull();
        userGraphic.writePrompt("dodawanie pliku " + fileName);
        userGraphic.showFile(fileName);
        }

    @Override
    public void addToUserList(String userName)
        {
        waitIfUserGuiNull();
        userGraphic.writePrompt("dodawanie usera " + userName);
        userGraphic.addToUserList(userName);
        }


    @Override
    public synchronized void deleteFromFileList(String fileName)
        {
        waitIfUserGuiNull();
        userGraphic.writePrompt("usuwanie pliku " + fileName);
        userGraphic.deleteFile(fileName);
        }


    @Override
    public void writePrompt(String text)
        {
        waitIfUserGuiNull();
        userGraphic.writePrompt(text);
        }


    }
