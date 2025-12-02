package org.example.service;

import org.example.config.AppConfig;
import org.example.model.FileEntry;
import org.example.model.Snapshot;

import java.io.IOException;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ScanService {
    private final HashService hashService;
    private final AppConfig config;

    public ScanService(AppConfig config) {
        this.config = config;
        this.hashService = new HashService(config.getHashAlgorithm());
    }

    public Snapshot scanDirectory(String directoryPath) throws IOException {
        Path root = Paths.get(directoryPath);
        if (!Files.exists(root)) throw new IllegalArgumentException("Path not found: " + directoryPath);

        List<FileEntry> files = new ArrayList<>();
        List<String> ignores = config.getIgnorePatterns();

        try (Stream<Path> paths = Files.walk(root)) {
            paths.filter(Files::isRegularFile).forEach(path -> {
                if (ignores.stream().noneMatch(ext -> path.toString().endsWith(ext))) {
                    try {
                        String relPath = root.relativize(path).toString().replace("\\", "/");
                        String hash = hashService.calculateHash(path.toString());
                        files.add(new FileEntry(relPath, hash));
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
            });
        }
        return new Snapshot(directoryPath, files);
    }
}