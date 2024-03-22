package test;

import manager.FileBackedTasksManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    TaskManager taskManager;

    @BeforeEach
    public void createManager() {
        File file = new File("FileSaveTest.csv");

        taskManager = new FileBackedTasksManager(file);
    }

    @Test
    void addTaskOfListHistory() throws IOException {
        Task task1 = new Task("Test addNewTask1", "Test addNewTask description1", TaskStatus.NEW,
                LocalDateTime.of(2024, 1, 1, 10, 33, 0), 20);

        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.NEW, 2, LocalDateTime.of(
                2024, 1, 1, 12, 33, 0), 20);
        Subtask subtask2 = new Subtask("Test addNewSubtask2", "Test addNewSubtask description2",
                TaskStatus.NEW, 2, LocalDateTime.of(
                2024, 2, 5, 18, 11, 16), 30);

        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask2);

        assertEquals(0, taskManager.getHistory().size(), "История не пустая.");

        taskManager.getSubtaskById(3);
        taskManager.getSubtaskById(3);
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(4);

        assertEquals(4, taskManager.getHistory().size(), "История пустая.");

        List<Task> historyListInManager = taskManager.getHistory();
        List<Task> historyCheck = new ArrayList<>();

        historyCheck.add(subtask);
        historyCheck.add(task1);
        historyCheck.add(epic1);
        historyCheck.add(subtask2);
        // Проверка что история сохранена в верном порядке
        assertEquals(historyListInManager.get(0), historyCheck.get(0), "Задачи не совподают");
        assertEquals(historyListInManager.get(1), historyCheck.get(1), "Задачи не совподают");
        assertEquals(historyListInManager.get(2), historyCheck.get(2), "Задачи не совподают");
        assertEquals(historyListInManager.get(3), historyCheck.get(3), "Задачи не совподают");

    }

    @Test
    void getHistory() throws IOException {
        Task task1 = new Task("Test addNewTask1", "Test addNewTask description1", TaskStatus.NEW,
                LocalDateTime.of(2024, 1, 1, 10, 33, 0), 20);

        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.NEW, 2, LocalDateTime.of(
                2024, 1, 1, 12, 33, 0), 20);
        Subtask subtask2 = new Subtask("Test addNewSubtask2", "Test addNewSubtask description2",
                TaskStatus.NEW, 2, LocalDateTime.of(
                2024, 2, 5, 18, 11, 16), 30);

        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask2);

        assertTrue(taskManager.getHistory().isEmpty(), "История не пустая.");

        taskManager.getSubtaskById(3);
        taskManager.getSubtaskById(3);
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(4);

        assertFalse(taskManager.getHistory().isEmpty(), "История пустая.");
        assertEquals(4, taskManager.getHistory().size(), "Количество задач в истории не равно 4");

        List<Task> historyListInManager = taskManager.getHistory();
        List<Task> historyCheck = new ArrayList<>();

        historyCheck.add(subtask);
        historyCheck.add(task1);
        historyCheck.add(epic1);
        historyCheck.add(subtask2);

        assertEquals(historyListInManager.size(), historyCheck.size(), "Размеры списков истории  не равны");
        assertTrue(historyListInManager.containsAll(historyCheck), "Списки не совпадают");
    }


    @Test
    void remove() throws IOException {
        Task task1 = new Task("Test addNewTask1", "Test addNewTask description1", TaskStatus.NEW,
                LocalDateTime.of(2024, 1, 1, 10, 33, 0), 20);

        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.NEW, 2, LocalDateTime.of(
                2024, 1, 1, 12, 33, 0), 20);
        Subtask subtask2 = new Subtask("Test addNewSubtask2", "Test addNewSubtask description2",
                TaskStatus.NEW, 2, LocalDateTime.of(
                2024, 2, 5, 18, 11, 16), 30);

        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask2);

        assertTrue(taskManager.getHistory().isEmpty(), "История не пустая.");

        taskManager.getSubtaskById(3);
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(4);

        assertFalse(taskManager.getHistory().isEmpty(), "История пустая.");
        assertEquals(4, taskManager.getHistory().size(), "Количество задач в истории не равно 4");

        taskManager.deleteTaskById(1);
        assertEquals(3, taskManager.getHistory().size(), "Количество задач в истории не равно 3");
        assertFalse(taskManager.getHistory().contains(task1), "Задача не удалена из истории");

        taskManager.deleteSubtaskById(3);
        assertEquals(2, taskManager.getHistory().size(), "Количество задач в истории не равно 2");
        assertFalse(taskManager.getHistory().contains(subtask), "Подзадача не удалена из истории");

        taskManager.deleteSubtaskById(4);
        assertEquals(1, taskManager.getHistory().size(), "Количество задач в истории не равно 1");
        assertFalse(taskManager.getHistory().contains(subtask2), "Подзадача не удалена из истории");

        taskManager.deleteEpicById(2);
        assertEquals(0, taskManager.getHistory().size(), "Количество задач в истории не равно 0");
        assertFalse(taskManager.getHistory().contains(epic1), "Подзадача не удалена из истории");
    }


}