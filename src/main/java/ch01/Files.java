package ch01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Solarex at 12:51 PM/2/20/21
 * Desc:
 */
public class Files {
    public static List<String> readAllLines(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> result = new ArrayList<>();
        for (;;) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            result.add(line);
        }
        return result;
    }
}
