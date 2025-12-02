package org.example.model;

/**
 * Модель данных, представляющая информацию об одном файле.
 */
public class FileEntry {
    private String path;
    private String hash;

    public FileEntry() {}

    public FileEntry(String path, String hash) {
        this.path = path;
        this.hash = hash;
    }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }
}