package cn.org.y24.utils;

import cn.org.y24.interfaces.IHandler;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UrlHandler implements IHandler<String, Map<String, String>, Boolean> {
    private BufferedReader reader;

    public BufferedReader getReader() {
        return reader;
    }
    @Override
    public Boolean handle(String target, Map<String, String> options) {
        try {
            if (reader != null)
                reader.close();
            final var connection = new URL(target).openConnection();
            options.forEach(connection::addRequestProperty);
            reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(),
                            StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public List<String> readLines(int count) {
        if (reader == null || count < 0)
            return null;
        final List<String> result = new ArrayList<>(count);
        try {
            for (int i = 0; i < count; i++)
                result.add(reader.readLine());
            return result;
        } catch (IOException e) {
            System.err.printf("UrlHandler.readLines(%d) exception: %s%n", count, e.getMessage());
            return null;
        }
    }


    @Override
    public void dispose() {
        try {
            if (reader != null) reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        reader = null;
    }
}
