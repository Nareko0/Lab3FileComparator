package org.example.model;

import java.util.List;

/**
 * Модель снимка файловой системы (root + список файлов).
 */
public class Snapshot {
    private String rootPath;
    private List<FileEntry> files;

    public Snapshot() {}

    public Snapshot(String rootPath, List<FileEntry> files) {
        this.rootPath = rootPath;
        this.files = files;
    }

    public String getRootPath() { return rootPath; }
    public void setRootPath(String rootPath) { this.rootPath = rootPath; }

    public List<FileEntry> getFiles() { return files; }
    public void setFiles(List<FileEntry> files) { this.files = files; }
}