package ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.debug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class QueryFileReader {
    private static final String SEPARATOR = "/";

    public Collection<String> getQueries(String[] fileLocations) {
        Collection<String> out = new ArrayList<>();
        for (String location : fileLocations) {
            out.addAll(getQueries(location));
        }
        return out;
    }

    public Collection<String> getQueries(String fileLocation) {
        InputStream stream = getClass().getResourceAsStream(fileLocation);
        System.out.println("Opening resource file: " + fileLocation);
        if (stream == null) System.out.println("failed");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        reader.lines().forEach(line -> builder.append(line).append("\n"));
        List<String> out = Arrays.stream(builder.toString().split(SEPARATOR))
                .filter(s -> !s.trim().isEmpty()).collect(Collectors.toList());
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }
}
