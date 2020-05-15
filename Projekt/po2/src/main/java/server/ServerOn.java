package server;

/**
 * Klasa uruchomieniowa dla serwera
 */
public class ServerOn
    {
    public static void main(String[] args)
        {
        try
            {
            new Thread(new Server()).start();
            ServerGraphic gui = new ServerGraphic();

            new Thread(() ->
            {
            try
                {
                while (true)
                    {
                    gui.refresh();
                    gui.setField("refresh listy");
                    Thread.sleep(4000);
                    }
                }
            catch (Exception e)
                {
                e.printStackTrace();
                }
            }).start();

            }
        catch (Exception e)
            {
            e.printStackTrace();
            }
        }
    }
