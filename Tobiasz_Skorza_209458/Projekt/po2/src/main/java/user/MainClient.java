package user;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Klasa uruchomieniowa dla klienta
 */
public class MainClient
    {
    public static void main(String[] args) throws IOException, InterruptedException
        {
        String userName = "Skorza";
        String userPath = "Skorza";

        if (args.length == 2)
            {
            userName = args[0];
            userPath = args[1];
            }


        MyDisplay screen = new MyDisplay();
        MyUser user = new MyUser(userName, userPath, screen);
        Executor executor = Executors.newCachedThreadPool();


        //watek gui

        executor.execute(() ->
        {
        UserGraphic userGraphic1 = new UserGraphic(user);
        screen.setUserGraphic(userGraphic1);
        });

        //dodawanie pliku.
        executor.execute(() ->
        {
        try
            {
            Thread.sleep(5000);

            while (true)
                {
                user.refreshFileList();
                Thread.sleep(1000);
                user.getUsersFromServer();
                Thread.sleep(1000);
                }
            }
        catch (IOException | InterruptedException e)
            {
            e.printStackTrace();
            }

        });

        executor.execute(() ->
        {
        try
            {
            while (true)
                {
                user.cleanFileList();
                Thread.sleep(3000);
                }
            }
        catch (IOException | InterruptedException e)
            {
            e.printStackTrace();
            }

        });
        executor.execute(() ->
        {
        try
            {
            while (true)
                {
                user.refreshServerFileList();
                Thread.sleep(5000);
                }
            }
        catch (InterruptedException e)
            {
            e.printStackTrace();
            }

        });


        }


    }

