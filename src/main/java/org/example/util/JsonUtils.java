package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.model.Snapshot;
import java.io.File;
import java.io.IOException;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static void writeSnapshot(File file, Snapshot snapshot) throws IOException {
        mapper.writeValue(file, snapshot);
    }

    public static Snapshot readSnapshot(File file) throws IOException {
        return mapper.readValue(file, Snapshot.class);
    }
}