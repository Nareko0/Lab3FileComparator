package org.example;

import org.example.config.AppConfig;
import org.example.model.Snapshot;
import org.example.service.DiffService;
import org.example.service.ScanService;
import org.example.util.JsonUtils;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Если аргументов нет вообще, по умолчанию запускаем SCAN (для удобства в IDEA)
        String mode = args.length > 0 ? args[0] : "scan";

        System.out.println("=== RUNNING MODE: " + mode.toUpperCase() + " ===");

        AppConfig config = new AppConfig("app.properties");

        try {
            if ("scan".equalsIgnoreCase(mode)) {
                // Пытаемся взять из ENV, если нет — берем дефолтные значения
                String scanPath = getEnvOrDefault("SCAN_PATH", "src"); // Сканируем папку src
                String outPath = getEnvOrDefault("SNAPSHOT_OUTPUT", "snapshot.json");

                System.out.println("Path to scan: " + scanPath);
                System.out.println("Output file: " + outPath);

                Snapshot snapshot = new ScanService(config).scanDirectory(scanPath);
                JsonUtils.writeSnapshot(new File(outPath), snapshot);
                System.out.println("SUCCESS: Snapshot saved.");

            } else if ("diff".equalsIgnoreCase(mode)) {
                String oldPath = getEnvOrDefault("SNAPSHOT_OLD", "snapshot.json");
                String newPath = getEnvOrDefault("SNAPSHOT_NEW", "snapshot_new.json"); // Для сравнения нужен второй файл

                System.out.println("Old snapshot: " + oldPath);
                System.out.println("New snapshot: " + newPath);

                Snapshot oldSnap = JsonUtils.readSnapshot(new File(oldPath));
                Snapshot newSnap = JsonUtils.readSnapshot(new File(newPath));

                // Передаем System.out, чтобы вывод шел в консоль
                new DiffService(config, System.out).compareSnapshots(oldSnap, newSnap);
            } else {
                System.out.println("Unknown mode: " + mode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Вспомогательный метод: берет переменную или дефолтное значение
    private static String getEnvOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);
        if (value == null) {
            // Можно закомментировать эту строку перед сдачей, если препод строгий
            System.out.println("Warning: ENV " + name + " is missing. Using default: " + defaultValue);
            return defaultValue;
        }
        return value;
    }
}