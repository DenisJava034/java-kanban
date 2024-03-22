package test;

import manager.FileBackedTasksManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static manager.FileBackedTasksManager.loadFromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class FileBackedTasksManagerTest extends test.TaskManagerTest<FileBackedTasksManager> {

    @Override
    public FileBackedTasksManager createManager() {
        File file = new File("FileSaveTest.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        return fileBackedTasksManager;
    }

    @Test
    void restoringTasksFromFile() throws IOException {
        File files = new File("FileSaveTest.csv");
        Writer fileClean = new FileWriter(files, false);
        fileClean.write("");
        FileBackedTasksManager manager = loadFromFile(files);
        assertEquals(0, files.length(), "Файл не пустой");

        LocalDateTime localDateTime = null;
        Task task1 = new Task("Test addNewTask1", "Test addNewTask description1", TaskStatus.NEW,
                LocalDateTime.of(2024, 1, 1, 10, 33, 0), 20);
        Task task2 = new Task("Test addNewTask2", "Test addNewTask description2", TaskStatus.NEW,
                localDateTime, 0);

        manager.createTask(task1);
        manager.createTask(task2);

        assertNotEquals(0, files.length(), "Файл пустой");

        //Считываем файл, проверяем все ли задачи считались
        FileBackedTasksManager managerNew = loadFromFile(files);

        ArrayList<Task> checkTaskList = managerNew.getListOfTasks();

        assertEquals(2, checkTaskList.size(), "Количество задач не равно 2");
        // Пустой список истории
        assertEquals(0, managerNew.getHistory().size(), "История не пустая");
    }

    @Test
    void restoringEpicAndSubtasksFromAFile() throws IOException {
        File files = new File("FileSaveTest.csv");
        Writer fileClean = new FileWriter(files, false);
        fileClean.write("");
        FileBackedTasksManager manager = loadFromFile(files);
        assertEquals(0, files.length(), "Файл не пустой");

        LocalDateTime localDateTime = null;
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.NEW, 1, localDateTime, 0);
        Subtask subtask2 = new Subtask("Test addNewSubtask2", "Test addNewSubtask description2",
                TaskStatus.NEW, 1, LocalDateTime.of(
                2024, 2, 5, 18, 11, 16), 30);

        manager.createEpic(epic1);
        manager.createSubtask(subtask);
        manager.createSubtask(subtask2);
        assertNotEquals(0, files.length(), "Файл пустой");

        //Считываем файл, проверяем все ли задачи считались
        FileBackedTasksManager managerNew = loadFromFile(files);

        ArrayList<Epic> checkEpicList = managerNew.getListOfEpics();
        ArrayList<Subtask> checkSubtaskList = managerNew.getListOfSubTasks();

        assertEquals(1, checkEpicList.size(), "Количество Эпиков не равно 1");
        assertEquals(2, checkSubtaskList.size(), "Количество Сабтасков не равно 2");

        // Пустой список истории
        assertEquals(0, managerNew.getHistory().size(), "История не пустая");

    }

    @Test
    void restoringEpicWithoutSubtasksFromFile() throws IOException {
        File files = new File("FileSaveTest.csv");
        Writer fileClean = new FileWriter(files, false);
        fileClean.write("");
        FileBackedTasksManager manager = loadFromFile(files);
        assertEquals(0, files.length(), "Файл не пустой");

        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);

        manager.createEpic(epic1);
        assertNotEquals(0, files.length(), "Файл пустой");

        //Считываем файл, проверяем все ли задачи считались
        FileBackedTasksManager managerNew = loadFromFile(files);

        ArrayList<Epic> checkEpicList = managerNew.getListOfEpics();

        assertEquals(1, checkEpicList.size(), "Количество Эпиков не равно 1");

        // Пустой список истории
        assertEquals(0, managerNew.getHistory().size(), "История не пустая");
    }

    @Test
    void recoveringHistoryFromFile() throws IOException {
        File files = new File("FileSaveTest.csv");
        Writer fileClean = new FileWriter(files, false);
        fileClean.write("");
        FileBackedTasksManager manager = loadFromFile(files);
        assertEquals(0, files.length(), "Файл не пустой");


        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.NEW, 1, LocalDateTime.of(
                2024, 1, 1, 10, 33, 0), 20);
        Subtask subtask2 = new Subtask("Test addNewSubtask2", "Test addNewSubtask description2",
                TaskStatus.NEW, 1, LocalDateTime.of(
                2024, 2, 5, 18, 11, 16), 30);
        Task task1 = new Task("Test addNewTask1", "Test addNewTask description1", TaskStatus.NEW,
                LocalDateTime.of(2024, 6, 1, 10, 33, 0), 20);

        manager.createEpic(epic1);
        manager.createSubtask(subtask);
        manager.createSubtask(subtask2);
        manager.createTask(task1);

        manager.getTaskById(4);
        manager.getSubtaskById(2);
        manager.getSubtaskById(3);
        manager.getEpicById(1);

        assertNotEquals(0, files.length(), "Файл пустой");

        //Считываем файл, проверяем считалась ли история
        FileBackedTasksManager managerNew = loadFromFile(files);

        ArrayList<Task> checkHistoryList = managerNew.getHistory();

        assertEquals(4, checkHistoryList.size(), "Размер истории не равно 4");

    }
}