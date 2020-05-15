import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;


public class fileProcedure {
    public static void procedureIO(char[] tab, long startTimeIO) throws IOException {
        int i = 0;
        FileWriter writer = null;
        FileReader reader = null;
        char[] cbuf = new char[1000];

        try { // Zapis
            writer = new FileWriter("1000.txt");
            writer.write(tab, 0, 1000);
            long endWriteTimeIO = System.nanoTime();

            long durationWriteIO = (endWriteTimeIO - startTimeIO);
	    System.out.println("\nCzas zapisu procedury z biblioteki  IO: " + durationWriteIO + "\n");
        
	} catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }


        try { // Odczyt
            reader = new FileReader("1000.txt");
            reader.read(cbuf, 0, 1000);
            System.out.println(cbuf);
            long endReadTimeIO = System.nanoTime();
            long durationReadIO = (endReadTimeIO - startTimeIO);
            System.out.println("\nCzas zapisu procedury z biblioteki  IO: " + durationReadIO + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    public static void procedureNIO(char[] tab, long startTimeIO) throws IOException {
        Path path = Paths.get("1000.txt");
        int i = 0;
        try {
            byte[] bs = new byte[1000];
            for (i = 0; i < 1000; i++) {
                bs[i] = (byte) tab[i];
            }
            Files.write(path, bs);
            long endWriteTimeNIO = System.nanoTime();
            long durationWriteNIO = (endWriteTimeNIO - startTimeIO);
            System.out.println("\nCzas zapisu procedury z biblioteki  NIO: " + durationWriteNIO + "\n");

            List<String> strings = Files.readAllLines(path);
            System.out.println(strings);
            long endReadTimeNIO = System.nanoTime();
            long durationReadNIO = (endReadTimeNIO - startTimeIO);
            System.out.println("\nCzas zapisu procedury z biblioteki  NIO: " + durationReadNIO + "\n");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        FileReader in = null;
        FileWriter out = null;
        String line = null;
        Random generator = new Random();
        int rand = 0;
        //System.out.println(Character.toString ((char) rand));
        int i = 0;
        int c;
        char[] tab = new char[1000];
        for (; i < 1000; i++) {
            rand = generator.nextInt(94) + 33;
            tab[i] = (char) rand;
        }
	
        long startTimeIO = System.nanoTime();
        procedureIO(tab, startTimeIO);
        long endTimeIO = System.nanoTime();
        long durationIO = (endTimeIO - startTimeIO);
        double elapsedTimeInSecondIO = (double) durationIO / 1_000_000_000;


        long startTimeNIO = System.nanoTime();
        procedureNIO(tab, startTimeNIO);
        long endTimeNIO = System.nanoTime();
        long durationNIO = (endTimeNIO - startTimeNIO);
        double elapsedTimeInSecondNIO = (double) durationNIO / 1_000_000_000;

        System.out.println("\n\nCzas wykonywania procedura z biblioteki  IO: " + durationIO + " w sekundach: " + elapsedTimeInSecondIO);
        System.out.println("Czas wykonywania procedura z biblioteki NIO: " + durationNIO + " w sekundach: " + elapsedTimeInSecondNIO);
        if (durationIO < durationNIO)
            System.out.println("Procedura IO jest szybsza");
        else
            System.out.println("Procedura NIO jest szybsza");
    }


}

