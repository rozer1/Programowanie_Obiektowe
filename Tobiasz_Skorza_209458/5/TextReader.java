import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TextReader {

    private static int getRandomNumberInRange(int min, int max) {

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static void WaitForKey() throws IOException, InterruptedException {

		String[] cmd = {"/bin/sh", "-c", "stty raw -echo </dev/tty"};
		Runtime.getRuntime().exec(cmd).waitFor();
		Console console = System.console();
		Reader reader = console.reader();
		StringBuilder KeyBind = new StringBuilder();
        for(int i = 0;i<1;i++) 
		KeyBind.append(reader.read());

        cmd = new String[] {"/bin/sh", "-c", "stty sane </dev/tty"};
        Runtime.getRuntime().exec(cmd).waitFor();
    }

    public static void readFile(File file) throws IOException {
        try (FileReader fr = new FileReader(file)) {

            char c;
            int i = 0, Iteration = 0;
            int RandomNumber = getRandomNumberInRange(1, 5);

            while ((i = fr.read()) != -1) {

                c = (char) i;
                if (Iteration == RandomNumber) {
                    //System.out.print("      >>>>" + RandomNumber);
                    WaitForKey();
                    RandomNumber = getRandomNumberInRange(1, 5);
                    Iteration = 0;
                }
                System.out.print(c);
                Iteration++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {


        String FolderPath = args[0];
        File file = new File(FolderPath);
        if (file.exists()) {
            Path path = FileSystems.getDefault().getPath(FolderPath).toAbsolutePath();

            try {
                Files.write(Paths.get("MyFilePath.txt"), Collections.singleton(path.toString()));
                readFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
