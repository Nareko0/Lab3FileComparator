package org.example.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Класс конфигурации приложения.
 * Отвечает за чтение настроек из файла app.properties.
 */
public class AppConfig {
    private final String hashAlgorithm;
    private final List<String> ignorePatterns;

    public AppConfig(String configPath) {
        Properties props = new Properties();
        // Пытаемся загрузить сначала из внешнего пути, если нет - из ресурсов (для тестов/IDE)
        try (InputStream fis = new FileInputStream(configPath)) {
            props.load(fis);
        } catch (IOException e) {
            // Если файл не найден снаружи, пробуем найти в classpath
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(configPath)) {
                if (is != null) props.load(is);
            } catch (IOException ignored) {}
        }

        this.hashAlgorithm = props.getProperty("hash.algorithm", "MD5");

        String patterns = props.getProperty("scan.ignore.patterns", "");
        if (patterns.isEmpty()) {
            this.ignorePatterns = Collections.emptyList();
        } else {
            this.ignorePatterns = Arrays.stream(patterns.split(","))
                    .map(String::trim)
                    .map(s -> s.replace("*", "")) // Приводим *.log к .log
                    .collect(Collectors.toList());
        }
    }

    public String getHashAlgorithm() { return hashAlgorithm; }
    public List<String> getIgnorePatterns() { return ignorePatterns; }
}