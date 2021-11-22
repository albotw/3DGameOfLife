package graphics;


import java.io.*;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Util {

    public static String loadResource(String fileName) throws Exception
    {

        String result = "";
        try
        {
            InputStream in = ClassLoader.getSystemResourceAsStream(fileName);
            Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name());
            result = scanner.useDelimiter("\\A").next();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;

    }
}
