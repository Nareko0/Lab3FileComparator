package org.example.service;

import org.example.config.AppConfig;
import org.example.model.FileEntry;
import org.example.model.Snapshot;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DiffService {
    private final AppConfig config;
    private final PrintStream out; // Куда писать результат

    // Конструктор принимает PrintStream
    public DiffService(AppConfig config, PrintStream out) {
        this.config = config;
        this.out = out;
    }

    public void compareSnapshots(Snapshot oldSnap, Snapshot newSnap) {
        Map<String, String> oldFiles = toMap(oldSnap.getFiles());
        Map<String, String> newFiles = toMap(newSnap.getFiles());

        for (Map.Entry<String, String> entry : oldFiles.entrySet()) {
            String path = entry.getKey();
            if (newFiles.containsKey(path)) {
                if (!entry.getValue().equals(newFiles.get(path))) {
                    out.println("CHANGED: " + path); // Пишем в out, а не в System.out
                }
            } else {
                out.println("REMOVED: " + path);
            }
        }
        for (String path : newFiles.keySet()) {
            if (!oldFiles.containsKey(path)) {
                out.println("ADDED: " + path);
            }
        }
    }

    private Map<String, String> toMap(List<FileEntry> files) {
        List<String> ignores = config.getIgnorePatterns();
        return files.stream()
                .filter(f -> ignores.stream().noneMatch(ext -> f.getPath().endsWith(ext)))
                .collect(Collectors.toMap(FileEntry::getPath, FileEntry::getHash));
    }
}//dadasdasdasds