package com.dk.demo;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TestPlugin extends AnAction {
    private static final Map<String, LocalDateTime> fileOpenTime = new HashMap<>();

    @Override
    // Содержит всю информацию о событии
    public void actionPerformed(AnActionEvent e) {
        // Получаем контекст проекта(текущего проекта IDE)
        Project project = e.getProject();
        if (project == null) return;

        // Получаем менеджер файлов редактора для текущего проекта
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);

        // Получаем текущий открытый файл: берем первый файл из массива открытых файлов
        VirtualFile currentFile = fileEditorManager.getSelectedFiles().length > 0
                ? fileEditorManager.getSelectedFiles()[0]
                : null;

        if (currentFile == null) {
            Messages.showInfoMessage("Нет открытых файлов", "File Time Tracker");
            return;
        }

        String fileName = currentFile.getName();
        // Получаем полный путь к файлу
        String filePath = currentFile.getPath();

        if (!fileOpenTime.containsKey(filePath)) {
            fileOpenTime.put(filePath, LocalDateTime.now());

            Messages.showInfoMessage(
                    "Начало отслеживания файла: " + fileName +
                            "\nВремя начала: " + LocalDateTime.now().toString(),
                    "File Time Tracker"
            );
        } else {
            // Если файл уже отслеживается:

            LocalDateTime openTime = fileOpenTime.get(filePath);

            Duration duration = Duration.between(openTime, LocalDateTime.now());

            long totalSeconds = duration.getSeconds();

            String timeString = String.format("%02d ч %02d мин %02d сек",
                    totalSeconds / 3600, (totalSeconds % 3600) / 60, totalSeconds % 60);

            Messages.showInfoMessage(
                    "Файл: " + fileName +  // Имя файла
                            "\nВремя открытия: " + openTime.toString() +  // Когда начали отслеживать
                            "\nОткрыт: " + timeString,  // Сколько времени открыт
                    "Время редактирования"  // Заголовок окна
            );
        }
    }
}