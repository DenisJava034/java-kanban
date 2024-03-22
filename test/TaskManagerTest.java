package test;

import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager = createManager();


    @BeforeEach
    public abstract T createManager();

    @Test
    void getListOfTasks() throws IOException {
        LocalDateTime localDateTime = null;
        Task task1 = new Task("Test addNewTask1", "Test addNewTask description1", TaskStatus.NEW,
                localDateTime, 0);
        Task task2 = new Task("Test addNewTask2", "Test addNewTask description2", TaskStatus.NEW,
                LocalDateTime.of(2024, 2, 5, 18, 11, 16), 5);

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        List<Task> tasksInManager = taskManager.getListOfTasks();
        List<Task> taskCheck = new ArrayList<>();
        taskCheck.add(task1);
        taskCheck.add(task2);

        assertEquals(taskCheck.size(), tasksInManager.size(), "Размеры списков Task не равны");
        assertTrue(taskCheck.containsAll(tasksInManager), "Task не совпадают");
    }

    @Test
    void getListOfEpics() throws IOException {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);
        Epic epic2 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        List<Epic> epicInManager = taskManager.getListOfEpics();
        List<Epic> epicCheck = new ArrayList<>();
        epicCheck.add(epic1);
        epicCheck.add(epic2);

        assertEquals(epicCheck.size(), epicInManager.size(), "Размеры списков Epic не равны");
        assertTrue(epicCheck.containsAll(epicInManager), "Epic не совпадают");
    }

    @Test
    void getListOfSubTasks() throws IOException {
        LocalDateTime localDateTime = null;
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.NEW, 1, LocalDateTime.of(
                2024, 1, 1, 10, 33, 0), 20);
        Subtask subtask2 = new Subtask("Test addNewSubtask2", "Test addNewSubtask description2",
                TaskStatus.NEW, 1, localDateTime, 0);

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask2);

        List<Subtask> subtaskListInManager = taskManager.getListOfSubTasks();
        List<Subtask> subtaskCheck = new ArrayList<>();
        subtaskCheck.add(subtask);
        subtaskCheck.add(subtask2);

        assertEquals(subtaskCheck.size(), subtaskListInManager.size(), "Размеры списков Subtask не равны");
        assertTrue(subtaskCheck.containsAll(subtaskListInManager), "Subtask не совпадают");
    }

    @Test
    void deleteAllTasks() throws IOException {
        Task task1 = new Task("Test addNewTask1", "Test addNewTask description1", TaskStatus.NEW,
                LocalDateTime.of(2024, 1, 1, 10, 33, 0), 20);
        Task task2 = new Task("Test addNewTask2", "Test addNewTask description2", TaskStatus.NEW,
                LocalDateTime.of(2024, 2, 5, 18, 11, 16), 30);

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        assertTrue(taskManager.getHistory().isEmpty(), "История не пуста");
        taskManager.getTaskById(1); // Добавляем в историю просмотров.
        assertFalse(taskManager.getHistory().isEmpty(), "История пуста");
        taskManager.deleteAllTasks();


        assertTrue(taskManager.getHistory().isEmpty(), "Список Task не пуст");
        assertTrue(taskManager.getHistory().isEmpty(), "История не пуста");
    }

    @Test
    void deleteAllEpic() throws IOException {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);
        Epic epic2 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.NEW, 1, LocalDateTime.of(
                2024, 1, 1, 10, 33, 0), 20);
        Subtask subtask2 = new Subtask("Test addNewSubtask2", "Test addNewSubtask description2",
                TaskStatus.NEW, 1, LocalDateTime.of(
                2024, 2, 5, 18, 11, 16), 30);

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask2);
        taskManager.createEpic(epic2);

        assertTrue(taskManager.getHistory().isEmpty(), "История не пуста");
        taskManager.getEpicById(1); // Добавляем в историю просмотров.
        taskManager.getSubtaskById(2);
        taskManager.getSubtaskById(3);
        assertEquals(3, taskManager.getHistory().size(), "История пуста или не равна 3");

        taskManager.deleteAllEpic();

        assertTrue(taskManager.getListOfEpics().isEmpty(), "Список Epic не пуст");
        assertTrue(taskManager.getListOfSubTasks().isEmpty(), "Список Subtack не пуст");
        assertTrue(taskManager.getHistory().isEmpty(), "История не пуста");
    }

    @Test
    void deleteAllSubtask() throws IOException {
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

        assertTrue(taskManager.getHistory().isEmpty(), "История не пуста");
        taskManager.getSubtaskById(2);// Добавляем в историю просмотров.
        taskManager.getSubtaskById(3);
        assertEquals(2, taskManager.getHistory().size(), "История пуста или не равна 2");

        taskManager.deleteAllSubtask();

        assertEquals(1, taskManager.getListOfEpics().size(), "Список Epic пуст");
        assertTrue(taskManager.getListOfSubTasks().isEmpty(), "Список Subtack не пуст");
        assertTrue(taskManager.getHistory().isEmpty(), "История не пуста");
    }

    @Test
    void getTaskById() throws IOException {
        Task task1 = new Task("Test addNewTask1", "Test addNewTask description1", TaskStatus.NEW,
                LocalDateTime.of(2024, 1, 1, 10, 33, 0), 20);

        taskManager.createTask(task1);

        assertTrue(taskManager.getHistory().isEmpty(), "История не пуста");
        taskManager.getTaskById(1);// Добавляем в историю просмотров.
        assertEquals(1, taskManager.getHistory().size(), "История пуста или не равна 1");
        assertTrue(taskManager.getHistory().contains(task1), "Task отсутсвует в истории");

        assertEquals(task1, taskManager.getTaskById(1), "Task не совпадает");
        assertNull(taskManager.getTaskById(-1), "Введен правильный id задачи"); // проверка на не верный id.
    }

    @Test
    void getEpicById() throws IOException {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);

        taskManager.createEpic(epic1);

        assertTrue(taskManager.getHistory().isEmpty(), "История не пуста");
        taskManager.getEpicById(1);// Добавляем в историю просмотров.
        assertEquals(1, taskManager.getHistory().size(), "История пуста или не равна 1");
        assertTrue(taskManager.getHistory().contains(epic1), "Epic отсутсвует в истории");

        assertEquals(epic1, taskManager.getEpicById(1), "Epic не совпадает");
        assertNull(taskManager.getEpicById(-1), "Введен правильный id Epic"); // проверка на не верный id.
    }

    @Test
    void getSubtaskById() throws IOException {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.NEW, 1, LocalDateTime.of(
                2024, 1, 1, 10, 33, 0), 20);

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask);

        assertTrue(taskManager.getHistory().isEmpty(), "История не пуста");
        taskManager.getSubtaskById(2);// Добавляем в историю просмотров.
        assertEquals(1, taskManager.getHistory().size(), "История пуста или не равна 1");
        assertTrue(taskManager.getHistory().contains(subtask), "Subtask отсутсвует в истории");

        assertEquals(subtask, taskManager.getSubtaskById(2), "Subtask не совпадает");
        assertNull(taskManager.getSubtaskById(-1), "Введен правильный id Subtask"); // проверка на не верный id.
    }

    @Test
    void createTask() throws IOException {
        Task task1 = new Task("Test addNewTask1", "Test addNewTask description1", TaskStatus.NEW,
                LocalDateTime.of(2024, 1, 1, 10, 33, 0), 20);
        taskManager.createTask(task1);

        final int taskId = taskManager.getTaskById(1).getId();
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getListOfTasks();

        assertNotNull(tasks, "Задачи нe возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void createEpic() throws IOException {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);
        taskManager.createEpic(epic1);

        final int epicId = taskManager.getEpicById(1).getId();
        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Epic не найден.");
        assertEquals(epic1, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getListOfEpics();

        assertNotNull(epics, "Epic нe возвращается.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic1, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void createSubtask() throws IOException {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.NEW, 1, LocalDateTime.of(
                2024, 1, 1, 10, 33, 0), 20);

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask);

        final int subtaskId = taskManager.getSubtaskById(2).getId();
        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Subtask не найден.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getListOfSubTasks();

        assertNotNull(subtasks, "Подзадачи нe возвращается.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадаачи не совпадают.");

        assertTrue(taskManager.getListSubtaskByEpicId(1).contains(subtask), "У Эпика нет такой подзадачи");
        assertEquals(savedSubtask.getStatus(), epic1.getStatus(), "Статус Эпика изменился");
        assertEquals(savedSubtask.getEpicId(), epic1.getId(), "Сабтаск хранит указание на другой Эпик");
    }

    @Test
    void updateTask() throws IOException {
        Task task1 = new Task("Test addNewTask1", "Test addNewTask description1", TaskStatus.NEW,
                LocalDateTime.of(2024, 1, 1, 10, 33, 0), 20);
        taskManager.createTask(task1);

        Task task2 = new Task(1, "Обновленное название", "Обновленное описание", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2024, 1, 1, 10, 33, 0), 20);

        taskManager.updateTask(task2);

        assertEquals(task2, taskManager.getTaskById(1), "Задача не обновилась");
        assertTrue(taskManager.getListOfTasks().contains(task2), "Задача в списке не обновилась");

        //Проверка на неверный id

        Task task3 = new Task(2, "Обновленное название", "Обновленное описание", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2024, 1, 1, 10, 33, 0), 20);

        taskManager.updateTask(task3);
        assertFalse(task3.equals(taskManager.getTaskById(1)), "Задача обновилась");
        assertFalse(taskManager.getListOfTasks().contains(task3), "Задача в списке обновилась");

        // Проверка на пересечение задач при обновлении

        Task task4 = new Task("Задача 2", "Описание", TaskStatus.IN_PROGRESS, // добавляем еще одну задачу
                LocalDateTime.of(2024, 1, 1, 10, 53, 1), 20);
        taskManager.createTask(task4);

        Task task5 = new Task(1, "Новое название", "Обновленное описание", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2024, 1, 1, 10, 53, 1), 20);
        taskManager.updateTask(task5);

        assertFalse(task5.equals(taskManager.getTaskById(1)), "Задача обновилась");
        assertFalse(taskManager.getListOfTasks().contains(task5), "Задача в списке обновилась");

        // Проверка на обновление задачи при уменьшее времени старта
        Task task6 = new Task(1, "Новое название", "Обновленное описание", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2024, 1, 1, 8, 52, 1), 20);
        taskManager.updateTask(task6);

        assertTrue(task6.equals(taskManager.getTaskById(1)), "Задача не обновилась");
        assertTrue(taskManager.getListOfTasks().contains(task6), "Задача в списке не обновилась");
    }

    @Test
    void updateEpic() throws IOException {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);
        taskManager.createEpic(epic1);

        Epic epic2 = new Epic(1, "Обновленное название", "Обновленное описание", TaskStatus.NEW);

        taskManager.updateEpic(epic2);

        assertEquals(epic2, taskManager.getEpicById(1), "Эпик не обновился");
        assertTrue(taskManager.getListOfEpics().contains(epic2), "Эпик в списке не обновился");

        //Проверка на неверный id

        Epic epic3 = new Epic(2, "Обновленное название", "Обновленное описание", TaskStatus.NEW);

        taskManager.updateEpic(epic3);
        assertFalse(epic3.equals(taskManager.getEpicById(1)), "Эпик обновился");
        assertFalse(taskManager.getListOfEpics().contains(epic3), "Эпик в списке обновился");

        // проверка на изменения времени старта и длительности задачи, изменять эти поля вручную у Эпика нельзя

        Epic epic4 = new Epic(1, "Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW,
                LocalDateTime.of(2024, 1, 1, 10, 53, 1), 20);
        taskManager.updateEpic(epic4);

        assertFalse(epic4.equals(taskManager.getEpicById(1)), "Эпик обновился");
        assertFalse(taskManager.getListOfEpics().contains(epic4), "Эпик в списке обновился");
        assertFalse(taskManager.getPrioritizedTasks().contains(epic4), "Эпик добавился в список сортировки");
    }

    @Test
    void updateSubtask() throws IOException {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.NEW, 1, LocalDateTime.of(
                2024, 1, 1, 10, 33, 0), 20);

        Subtask subtask2 = new Subtask("Test addNewSubtask2", "Test addNewSubtask description2",
                TaskStatus.NEW, 1, LocalDateTime.of(
                2024, 1, 10, 10, 33, 0), 20);

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask2);

        Subtask subtask3 = new Subtask(3, "Обновленное название", "Test addNewSubtask description2",
                TaskStatus.IN_PROGRESS, 1, LocalDateTime.of(
                2024, 1, 10, 10, 33, 0), 20);

        taskManager.updateSubtask(subtask3);
        assertEquals(subtask3, taskManager.getSubtaskById(3), "Эпик не обновился");
        assertTrue(taskManager.getListOfSubTasks().contains(subtask3), "Эпик в списке не обновился");
        // Проверка на то что статус эпика поменялся на IN_PROGRESS
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика не обновился");

        // Проверка на то что статус Эпика останется IN_PROGRESS в случае если 1 подзадача имееи статус DONE
        subtask3.setStatus(TaskStatus.DONE);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика не обновился");

        // Статус эпика меняется на DONE если все подзадачи имеют статус DONE
        Subtask subtask1 = new Subtask(2, "Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.DONE, 1, LocalDateTime.of(
                2024, 1, 1, 10, 33, 0), 20);
        taskManager.updateSubtask(subtask1);

        assertEquals(TaskStatus.DONE, taskManager.getEpicById(1).getStatus(), "Статус эпика не обновился");

        // Проверка на пересечение задач при обновлении

        Subtask subtask5 = new Subtask(2, "Новое название", "Новое описание",
                TaskStatus.IN_PROGRESS, 1, LocalDateTime.of(
                2024, 1, 10, 10, 33, 0), 20);

        taskManager.updateSubtask(subtask5);

        assertFalse(subtask5.equals(taskManager.getSubtaskById(2)), "Задача обновилась");
        assertFalse(taskManager.getListOfSubTasks().contains(subtask5), "Задача в списке обновилась");

        //Проверка на неверный id

        taskManager.deleteSubtaskById(3);
        Subtask subtask4 = new Subtask(4, "Обновленное название", "Test addNewSubtask description2",
                TaskStatus.IN_PROGRESS, 1, LocalDateTime.of(
                2024, 1, 10, 10, 33, 0), 20);

        taskManager.updateSubtask(subtask4);
        assertFalse(subtask4.equals(taskManager.getSubtaskById(2)), "Сабтаск обновился");
        assertFalse(taskManager.getListOfSubTasks().contains(subtask4), "Сабтаск в списке обновился");


        // Проверка на обновление задачи при уменьшее времени старта
        Subtask subtask6 = new Subtask(2, "Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.NEW, 1, LocalDateTime.of(
                2024, 1, 1, 9, 33, 0), 20);
        taskManager.updateSubtask(subtask6);

        assertTrue(subtask6.equals(taskManager.getSubtaskById(2)), "Задача не обновилась");
        assertTrue(taskManager.getListOfSubTasks().contains(subtask6), "Задача в списке не обновилась");
    }

    @Test
    void deleteTaskById() throws IOException {
        Task task1 = new Task("Test addNewTask1", "Test addNewTask description1", TaskStatus.NEW,
                LocalDateTime.of(2024, 1, 1, 10, 33, 0), 20);
        taskManager.createTask(task1);
        taskManager.getTaskById(1); // Записали в истории

        // Проверка что история не пуста и в списоке сортировки есть задача

        assertEquals(1, taskManager.getHistory().size(), "Не верный размер истории");
        assertTrue(taskManager.getHistory().contains(task1), "Задача не записалась в историю");
        assertTrue(taskManager.getPrioritizedTasks().contains(task1), "Задача не попала в список сортировки");

        // Проверка что после удаления задачи, задача удалится из списка просмотров, из списка сортировки,
        // из списка задач.

        taskManager.deleteTaskById(1);

        assertEquals(0, taskManager.getHistory().size(), "Не верный размер истории");
        assertFalse(taskManager.getHistory().contains(task1), "Задача не удалаилась из истории");
        assertFalse(taskManager.getPrioritizedTasks().contains(task1), "Задача не удалилась из списока " +
                "сортировки");
        assertFalse(taskManager.getListOfTasks().contains(task1), "Задача не удалена из списка задач");

    }

    @Test
    void deleteEpicById() throws IOException {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.NEW, 1, LocalDateTime.of(
                2024, 1, 1, 10, 33, 0), 20);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask);
        taskManager.getEpicById(1);
        taskManager.getSubtaskById(2);

        // Проверка что история не пуста и в списоке сортировки есть Subtask
        assertEquals(2, taskManager.getHistory().size(), "Не верный размер истории");
        assertTrue(taskManager.getHistory().contains(epic1), "Эпик не записался в историю");

        assertTrue(taskManager.getHistory().contains(subtask), "Сабтаск не записался в историю");
        assertTrue(taskManager.getPrioritizedTasks().contains(subtask), "Сабтаск не попал в список сортировки");

        // Проверка что при удалении Эпика, Эпик удалится из истории просмотров, из списка Эпиков, и списка сортировки,
        // также удалится пренадлежащий эпику сабтаск, из истории просмотров, из списка Эпиков, и списка сортировки
        taskManager.deleteEpicById(1);

        assertEquals(0, taskManager.getHistory().size(), "Не верный размер истории");
        assertFalse(taskManager.getHistory().contains(epic1), "Эпик не удалился из истории");
        assertFalse(taskManager.getPrioritizedTasks().contains(epic1), "Эпик не удалился из списока сортировки");
        assertFalse(taskManager.getListOfEpics().contains(epic1), "Эпик не удалися исписка Эпиков");

        assertFalse(taskManager.getHistory().contains(subtask), "Сабтаск не удалился из истории");
        assertFalse(taskManager.getPrioritizedTasks().contains(subtask), "Сабтаск не удалился из списока" +
                " сортировки");
        assertFalse(taskManager.getListOfSubTasks().contains(subtask), "Сабтаск не удалися исписка Сабтасков");
    }

    @Test
    void deleteSubtaskById() throws IOException {
        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.IN_PROGRESS, 1, LocalDateTime.of(
                2024, 1, 1, 10, 33, 0), 20);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask);
        taskManager.getEpicById(1);
        taskManager.getSubtaskById(2);

        // Проверка что история не пуста и в списоке сортировки есть Subtask, статус эпика IN_PROGRESS,
        // дата начала и продолжительность задачи равна Сабтаску
        assertEquals(2, taskManager.getHistory().size(), "Не верный размер истории");
        assertTrue(taskManager.getHistory().contains(epic1), "Эпик не записался в историю");

        assertTrue(taskManager.getHistory().contains(subtask), "Сабтаск не записался в историю");
        assertTrue(taskManager.getPrioritizedTasks().contains(subtask), "Сабтаск не попал в список сортировки");

        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика не обновился");
        assertTrue(subtask.getStartTime().equals(taskManager.getEpicById(1).getStartTime()),
                "Время старта Эпика не равно времени старта Сабтска");
        assertEquals(subtask.getDuration(), taskManager.getEpicById(1).getDuration(),
                "Продолжительность Эпика не равно продолжительности Сабтска");

        // Проверка что после удаления в истории, в списке Сабтасков и в списке сортировки нет Сабтаска, статус эпика NEW,
        // дата начала и продолжительность задачи Эпика равна null и 0, У эпика удалился id абтаска.
        taskManager.deleteSubtaskById(2);

        assertEquals(1, taskManager.getHistory().size(), "Не верный размер истории");

        assertFalse(taskManager.getHistory().contains(subtask), "Сабтаск не удалился из истории");
        assertFalse(taskManager.getPrioritizedTasks().contains(subtask),
                "Сабтаск не удалился из списока сортировки");

        assertEquals(TaskStatus.NEW, taskManager.getEpicById(1).getStatus(), "Статус эпика не обновился");
        assertNull(taskManager.getEpicById(1).getStartTime(),
                "Время старта Эпика не равно null");
        assertEquals(0, taskManager.getEpicById(1).getDuration(),
                "Продолжительность Эпика не равно 0");

        assertFalse(taskManager.getEpicById(1).getSubtaskId().contains(2),
                "id сабтаска не удалился из списка id сабтасков у Эпика");
    }

    @Test
    void getListSubtaskByEpicId() throws IOException {
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

        List<Subtask> subtaskListInManager = taskManager.getListSubtaskByEpicId(1);
        List<Subtask> subtaskCheck = new ArrayList<>();
        subtaskCheck.add(subtask);
        subtaskCheck.add(subtask2);

        assertEquals(subtaskCheck.size(), subtaskListInManager.size(), "Размеры списков Subtask не равны");
        assertTrue(subtaskCheck.containsAll(subtaskListInManager), "Subtask не совпадают");
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

        assertEquals(0, taskManager.getHistory().size(), "История не пуста");

        taskManager.getSubtaskById(3);
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(4);


        List<Task> historyListInManager = taskManager.getHistory();
        List<Task> historyCheck = new ArrayList<>();

        historyCheck.add(subtask);
        historyCheck.add(task1);
        historyCheck.add(epic1);
        historyCheck.add(subtask2);

        assertEquals(historyListInManager.size(), historyCheck.size(), "Размеры списков истории не равны");
        assertTrue(historyListInManager.containsAll(historyCheck), "Задачи истории не совпадают");

        // проверка что история формируется в правильном порядке

        assertEquals(historyListInManager.get(0), historyCheck.get(0), "Задачи не совподают");
        assertEquals(historyListInManager.get(1), historyCheck.get(1), "Задачи не совподают");
        assertEquals(historyListInManager.get(2), historyCheck.get(2), "Задачи не совподают");
        assertEquals(historyListInManager.get(3), historyCheck.get(3), "Задачи не совподают");
    }

    @Test
    void getPrioritizedTasks() throws IOException {
        Task task1 = new Task("Test addNewTask1", "Test addNewTask description1", TaskStatus.NEW,
                LocalDateTime.of(2024, 10, 5, 10, 33, 0), 20);

        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);
        Epic epic2 = new Epic("Test addNewEpic2", "Test addNewEpic description2", TaskStatus.NEW);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.NEW, 2, LocalDateTime.of(
                2024, 1, 1, 12, 33, 0), 20);
        Subtask subtask2 = new Subtask("Test addNewSubtask2", "Test addNewSubtask description2",
                TaskStatus.NEW, 2, LocalDateTime.of(
                2024, 11, 5, 18, 11, 16), 30);

        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask2);

        TreeSet startTimeSet = taskManager.getPrioritizedTasks();

        ArrayList<Task> prioritizedList = new ArrayList<>(); // добавляем в список задачи согласно сортировки
        prioritizedList.add(subtask);
        prioritizedList.add(task1);
        prioritizedList.add(subtask2);

        assertEquals(startTimeSet.size(), prioritizedList.size(), "Размеры списков не равны");
        assertTrue(prioritizedList.containsAll(startTimeSet), "Задачи не совпадают");
    }
}
