package org.example.service;

import org.example.config.AppConfig;
import org.example.model.FileEntry;
import org.example.model.Snapshot;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class DiffServiceTest {

    @Test
    void testCompare() {
        // 1. Mock конфига
        AppConfig config = Mockito.mock(AppConfig.class);
        when(config.getIgnorePatterns()).thenReturn(Collections.singletonList(".log"));

        // 2. Создаем "ловушку" для текста. Это НЕ System.out, это наша личная память.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        // 3. Данные
        Snapshot s1 = new Snapshot("root", Arrays.asList(
                new FileEntry("main.java", "111"),
                new FileEntry("del.txt", "222")
        ));
        Snapshot s2 = new Snapshot("root", Arrays.asList(
                new FileEntry("main.java", "111"),
                new FileEntry("new.txt", "333")
        ));

        // 4. Запускаем сервис, передавая ему нашу ловушку вместо консоли
        DiffService service = new DiffService(config, printStream);
        service.compareSnapshots(s1, s2);

        // 5. Проверяем, что попало в ловушку
        String result = outputStream.toString();

        // Для отладки (выведет в реальную консоль при запуске тестов)
        System.out.println("TEST OUTPUT: " + result);

        assertTrue(result.contains("REMOVED: del.txt"));
        assertTrue(result.contains("ADDED: new.txt"));
    }
}