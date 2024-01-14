package test;

import manager.FileBackedTasksManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    TaskManager taskManager;

    @BeforeEach
    public void createManager() {
        File file = new File("FileSaveTest.csv");
        taskManager = new FileBackedTasksManager(file);
    }

   @Test
   void returnsTheNewStatusIfTheListOfSubtasksIsEmpty() throws IOException {
       Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);
       taskManager.createEpic(epic1);
       assertEquals(TaskStatus.NEW, taskManager.getEpicById(1).getStatus(), "Неверный статус");
    }


    @Test
    void returnsTheStatusNewIfAllSubtasksHaveTheStatusNew() throws IOException {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.NEW, 1, LocalDateTime.of(
                2024, 1, 1, 10, 33, 0), 20);
        Subtask subtask2 = new Subtask("Test addNewSubtask2", "Test addNewSubtask description2",
                TaskStatus.NEW, 1, LocalDateTime.of(
                2024, 2, 5, 18, 11, 16), 30);

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask2);

        assertEquals(TaskStatus.NEW, taskManager.getEpicById(1).getStatus(), "Неверный статус");
    }

    @Test
    void returnsTheStatusDoneIfAllSubtasksHaveTheStatusDone() throws IOException {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.DONE, 1, LocalDateTime.of(
                2024, 1, 1, 10, 33, 0), 20);
        Subtask subtask2 = new Subtask("Test addNewSubtask2", "Test addNewSubtask description2",
                TaskStatus.DONE, 1, LocalDateTime.of(
                2024, 2, 5, 18, 11, 16), 30);

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask2);

        assertEquals(TaskStatus.DONE, taskManager.getEpicById(1).getStatus(), "Неверный статус");
    }

    @Test
    void returnsStatusInProgressIfSubtasksHaveStatusNewAndDone() throws IOException {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.NEW, 1, LocalDateTime.of(
                2024, 1, 1, 10, 33, 0), 20);
        Subtask subtask2 = new Subtask("Test addNewSubtask2", "Test addNewSubtask description2",
                TaskStatus.DONE, 1, LocalDateTime.of(
                2024, 2, 5, 18, 11, 16), 30);

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Неверный статус");
    }

    @Test
    void returnsStatusInProgressIfSubtasksHaveStatusInProgress() throws IOException {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.IN_PROGRESS, 1, LocalDateTime.of(
                2024, 1, 1, 10, 33, 0), 20);
        Subtask subtask2 = new Subtask("Test addNewSubtask2", "Test addNewSubtask description2",
                TaskStatus.IN_PROGRESS, 1, LocalDateTime.of(
                2024, 2, 5, 18, 11, 16), 30);

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Неверный статус");
    }








}