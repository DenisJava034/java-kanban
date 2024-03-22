package test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.FileBackedTasksManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servers.HttpTaskServer;
import servers.util.ServerUtility;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    public HttpTaskServer httpTaskServer;

    TaskManager taskManager;
    static Gson gson;
    Task task1;
    Epic epic1;
    Subtask subtask;
    Subtask subtask2;

    @BeforeEach
    public void createHttpTaskServer() throws IOException { // создаем каждый раз Http сервер и добавляем задачи
        File file = new File("FileSaveTestHttp.csv");
        taskManager = new FileBackedTasksManager(file);

        task1 = new Task("Test addNewTask1", "Test addNewTask description1", TaskStatus.NEW,
                LocalDateTime.of(
                        2020, 12, 8, 2, 51, 16), 70);

        epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description1", TaskStatus.NEW);

        subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                TaskStatus.NEW, 2, LocalDateTime.of(
                2023, 2, 6, 10, 14, 16), 120);
        subtask2 = new Subtask("Test addNewSubtask2", "Test addNewSubtask description2",
                TaskStatus.NEW, 2, LocalDateTime.of(
                2024, 2, 5, 18, 11, 16), 30);

        taskManager.createTask(task1); // id 1
        taskManager.createEpic(epic1); // id 2
        taskManager.createSubtask(subtask); // id 3
        taskManager.createSubtask(subtask2); // id 4
        httpTaskServer = new HttpTaskServer(taskManager);
        gson = ServerUtility.getGson();
        httpTaskServer.start();
    }

    @AfterEach
    public void stopHttpTaskServer() throws IOException { // останавливаем после каждого теста HTTP-клиент
        httpTaskServer.stop();
    }

    @Test
    public void getAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());


        assertEquals(task1.getId(), tasks.get(0).getId(), "id не сопадают");
        assertEquals(task1.getName(), tasks.get(0).getName(), "name не сопадают");
        assertEquals(task1.getTaskDescription(), tasks.get(0).getTaskDescription(),
                "taskDescription не сопадают");
        assertEquals(task1.getStatus(), tasks.get(0).getStatus(), "status не сопадаeт");
        assertEquals(task1.getStartTime(), tasks.get(0).getStartTime(), "StartTime не сопадаeт");
        assertEquals(task1.getDuration(), tasks.get(0).getDuration(), "duration не сопадаeт");

        // проверка на неверный url
        URI url1 = URI.create("http://localhost:8080/tasks/впв11");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void getAllEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        List<Epic> epics = gson.fromJson(response.body(), new TypeToken<List<Epic>>() {
        }.getType());


        assertEquals(epic1.getId(), epics.get(0).getId(), "id не сопадаtт");
        assertEquals(epic1.getName(), epics.get(0).getName(), "name не сопадаtт");
        assertEquals(epic1.getTaskDescription(), epics.get(0).getTaskDescription(),
                "taskDescription не сопадаtт");
        assertEquals(epic1.getStatus(), epics.get(0).getStatus(), "status не сопадаeт");
        assertEquals(epic1.getStartTime(), epics.get(0).getStartTime(), "StartTime не сопадаeт");
        assertEquals(epic1.getDuration(), epics.get(0).getDuration(), "duration не сопадаeт");

        // проверка на неверный url
        URI url1 = URI.create("http://localhost:8080/epics/epic");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void getAllSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        List<Subtask> subtasks = gson.fromJson(response.body(), new TypeToken<List<Subtask>>() {
        }.getType());


        assertEquals(subtask2.getId(), subtasks.get(1).getId(), "id не сопадаtт");
        assertEquals(subtask2.getName(), subtasks.get(1).getName(), "name не сопадаtт");
        assertEquals(subtask2.getTaskDescription(), subtasks.get(1).getTaskDescription(),
                "taskDescription не сопадаtт");
        assertEquals(subtask2.getStatus(), subtasks.get(1).getStatus(), "status не сопадаeт");
        assertEquals(subtask2.getStartTime(), subtasks.get(1).getStartTime(), "StartTime не сопадаeт");
        assertEquals(subtask2.getDuration(), subtasks.get(1).getDuration(), "duration не сопадаeт");

        // проверка на неверный url
        URI url1 = URI.create("http://localhost:8080/subtask");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void getTaskByID() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        Task task = gson.fromJson(response.body(), Task.class);


        assertEquals(task1.getId(), task.getId(), "id не сопадает");
        assertEquals(task1.getName(), task.getName(), "name не сопадает");
        assertEquals(task1.getTaskDescription(), task.getTaskDescription(), "taskDescription не сопадает");
        assertEquals(task1.getStatus(), task.getStatus(), "status не сопадаeт");
        assertEquals(task1.getStartTime(), task.getStartTime(), "StartTime не сопадаeт");
        assertEquals(task1.getDuration(), task.getDuration(), "duration не сопадаeт");

        // проверка наневерный id url
        URI url1 = URI.create("http://localhost:8080/tasks/15");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }


    @Test
    public void getEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        Epic epic = gson.fromJson(response.body(), Epic.class);


        assertEquals(epic1.getId(), epic.getId(), "id не сопадает");
        assertEquals(epic1.getName(), epic.getName(), "name не сопадает");
        assertEquals(epic1.getTaskDescription(), epic.getTaskDescription(), "taskDescription не сопадает");
        assertEquals(epic1.getStatus(), epic.getStatus(), "status не сопадаeт");
        assertEquals(epic1.getStartTime(), epic.getStartTime(), "StartTime не сопадаeт");
        assertEquals(epic1.getDuration(), epic.getDuration(), "duration не сопадаeт");

        // проверка на неверный id url
        URI url1 = URI.create("http://localhost:8080/epics/6");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void getSubtasksByID() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/4");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        Subtask subtask = gson.fromJson(response.body(), Subtask.class);


        assertEquals(subtask2.getId(), subtask.getId(), "id не сопадает");
        assertEquals(subtask2.getName(), subtask.getName(), "name не сопадает");
        assertEquals(subtask2.getTaskDescription(), subtask.getTaskDescription(), "taskDescription не сопадает");
        assertEquals(subtask2.getStatus(), subtask.getStatus(), "status не сопадаeт");
        assertEquals(subtask2.getStartTime(), subtask.getStartTime(), "StartTime не сопадаeт");
        assertEquals(subtask2.getDuration(), subtask.getDuration(), "duration не сопадаeт");

        // проверка на неверный id url
        URI url1 = URI.create("http://localhost:8080/subtasks/28");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void getSubtasksEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/2/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> subtasks = gson.fromJson(response.body(), new TypeToken<List<Subtask>>() {
        }.getType());

        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        assertEquals(taskManager.getListSubtaskByEpicId(2).size(), subtasks.size(),
                "Список полученых сабтасков не равняется количеству сабтасков эпика");
        assertEquals(subtask.getEpicId(), subtasks.get(0).getEpicId(), "сабтаск отсутсвует в полученом списке");
        assertEquals(subtask2.getEpicId(), subtasks.get(1).getEpicId(), "сабтаск отсутсвует в полученом списке");

        // проверка на неверный id url
        URI url1 = URI.create("http://localhost:8080/epics/88/subtasks");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");

        // проверка на неверный url
        URI url2 = URI.create("http://localhost:8080/epics/2/subtaskeeff");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url2)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response2.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void getHistory() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        //Проверяем пустую историю
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> newHistory = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());
        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        assertEquals(0, newHistory.size(), "История не пуста");

        //Создаем историю просмотров id 2 4 1 3
        taskManager.getSubtaskById(3);
        taskManager.getTaskById(1);
        taskManager.getSubtaskById(4);
        taskManager.getEpicById(2);

        List<Task> history = taskManager.getHistory();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        newHistory = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());

        assertEquals(4, newHistory.size(), "Размеры истории не совпадают");
        assertEquals(history.get(0).getId(), newHistory.get(0).getId(), "id не сопадают");
        assertEquals(history.get(1).getId(), newHistory.get(1).getId(), "id не сопадают");
        assertEquals(history.get(2).getId(), newHistory.get(2).getId(), "id не сопадают");
        assertEquals(history.get(3).getId(), newHistory.get(3).getId(), "id не сопадают");

        // проверка на неверный url
        URI url1 = URI.create("http://localhost:8080/histor");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void getPrioritized() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        TreeSet<Task> prioritized = taskManager.getPrioritizedTasks();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> newPrioritized = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());
        assertEquals(200, response.statusCode(), "Код ответа не равен 200");

        assertEquals(3, newPrioritized.size(), "Размеры списка сортировки не совпадают");
        assertEquals(prioritized.first().getId(), newPrioritized.get(0).getId(), "id не сопадают");
        assertEquals(prioritized.last().getId(), newPrioritized.get(2).getId(), "id не сопадают");

        // проверка на неверный url
        URI url1 = URI.create("http://localhost:8080/prioritiz");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void deleteAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .DELETE()
                .build();
        // Добавляем еще одн эпик
        Task task2 = new Task("Test addNewTask1", "Test addNewTask description1", TaskStatus.NEW,
                LocalDateTime.of(
                        2021, 12, 8, 3, 51, 6), 80);
        taskManager.createTask(task2);
        List<Task> tasks = taskManager.getListOfTasks();
        assertEquals(2, tasks.size(), "Размер списка задач не равен 2");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        tasks = taskManager.getListOfTasks();
        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        assertEquals(0, tasks.size(), "Размер списка задач не равен 0");

        // проверка на неверный url
        URI url1 = URI.create("http://localhost:8080/task");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void deleteAllEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .DELETE()
                .build();
        // Добавляем еще одну задачу
        Epic epic2 = new Epic("Test addNewEpic2", "Test addNewEpic description2", TaskStatus.NEW);
        taskManager.createEpic(epic2);
        List<Epic> epics = taskManager.getListOfEpics();
        assertEquals(2, epics.size(), "Размер списка пиков' не равен 2");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        epics = taskManager.getListOfEpics();
        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        assertEquals(0, epics.size(), "Размер списка пиков' не равен 0");

        // проверка на не верный url
        URI url1 = URI.create("http://localhost:8080/epic");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void deleteAllSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .DELETE()
                .build();

        List<Subtask> subtasks = taskManager.getListOfSubTasks();
        assertEquals(2, subtasks.size(), "Размер списка сабтасков не равен 2");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        subtasks = taskManager.getListOfSubTasks();

        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        assertEquals(0, subtasks.size(), "Размер списка сабтасков не равен 0");

        // проверка на неверный url
        URI url1 = URI.create("http://localhost:8080/subtask");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void deleteTaskByID() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .DELETE()
                .build();
        // Добавляем еще одн эпик
        Task task2 = new Task("Test addNewTask1", "Test addNewTask description1", TaskStatus.NEW,
                LocalDateTime.of(
                        2021, 12, 8, 3, 51, 6), 80);
        taskManager.createTask(task2);
        List<Task> tasks = taskManager.getListOfTasks();
        assertEquals(2, tasks.size(), "Размер списка задач не равен 2");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        tasks = taskManager.getListOfTasks();

        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        assertEquals(1, tasks.size(), "Размер списка задач не равен 1");
        assertFalse(tasks.contains(taskManager.getTaskById(1)), "В списке задач присуствует удаляемая задача");

        // проверка на неверный url
        URI url1 = URI.create("http://localhost:8080/taskfss?id=1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void deleteEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics?id=2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .DELETE()
                .build();
        // Добавляем еще одну задачу
        Epic epic2 = new Epic("Test addNewEpic2", "Test addNewEpic description2", TaskStatus.NEW);
        taskManager.createEpic(epic2);
        List<Epic> epics = taskManager.getListOfEpics();
        assertEquals(2, epics.size(), "Размер списка эпиков не равен 2");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        epics = taskManager.getListOfEpics();

        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        assertEquals(1, epics.size(), "Размер списка эпиков не равен 1");
        assertFalse(epics.contains(taskManager.getEpicById(2)), "В списке эпиков присуствует удаляемый эпик");

        // проверка на неверный url
        URI url1 = URI.create("http://localhost:8080/epicaefs?id=2");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void deleteSubtaskByID() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks?id=4");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .DELETE()
                .build();

        List<Subtask> subtasks = taskManager.getListOfSubTasks();
        assertEquals(2, subtasks.size(), "Размер списка сабтасков не равен 2");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        subtasks = taskManager.getListOfSubTasks();

        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        assertEquals(1, subtasks.size(), "Размер списка сабтасков не равен 1");
        assertFalse(subtasks.contains(taskManager.getSubtaskById(4)),
                "В списке сабтасков  присуствует удаляемый сабтаск");

        // проверка на неверный url
        URI url1 = URI.create("http://localhost:8080/subtasksa?id=4");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void addTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        // Добавляем задачу
        String name = "Test addNewTask2";
        String taskDescription = "Test addNewTask description2";
        TaskStatus status = TaskStatus.NEW;
        LocalDateTime localDateTime = LocalDateTime.of(
                2016, 11, 1, 2, 41, 16);
        int duration = 18;
        Task task2 = new Task(name, taskDescription, status, localDateTime, duration);

        String gsonTask = gson.toJson(task2);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gsonTask))
                .build();

        List<Task> tasks = taskManager.getListOfTasks();
        assertEquals(1, tasks.size(), "Размер списка задач не равен 1");

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        tasks = taskManager.getListOfTasks();

        assertEquals(201, response.statusCode(), "Код ответа не равен 201");
        assertEquals(2, tasks.size(), "Размер списка задач не равен 2");

        assertEquals(5, tasks.get(1).getId(), "id не сопадают");
        assertEquals(name, tasks.get(1).getName(), "name не сопадают");
        assertEquals(taskDescription, tasks.get(1).getTaskDescription(), "taskDescription не сопадаtт");
        assertEquals(status, tasks.get(1).getStatus(), "status не сопадаeт");
        assertEquals(localDateTime, tasks.get(1).getStartTime(), "StartTime не сопадаeт");
        assertEquals(duration, tasks.get(1).getDuration(), "duration не сопадаeт");

        // проверка на неверный url
        URI url1 = URI.create("http://localhost:8080/tasks/tasks");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gsonTask))
                .build();
        HttpResponse<Void> response1 = client.send(request1, HttpResponse.BodyHandlers.discarding());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void addEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        // Добавляем эпик
        String name = "Test addNewEpic2";
        String taskDescription = "Test addNewEpic description2";
        TaskStatus status = TaskStatus.NEW;
        Epic epic2 = new Epic(name, taskDescription, status);

        String gsonEpic = gson.toJson(epic2);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gsonEpic))
                .build();

        List<Epic> epics = taskManager.getListOfEpics();
        assertEquals(1, epics.size(), "Размер списка эпиков не равен 1");

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        epics = taskManager.getListOfEpics();

        assertEquals(201, response.statusCode(), "Код ответа не равен 201");
        assertEquals(2, epics.size(), "Размер списка епиков не равен 2");

        assertEquals(5, epics.get(1).getId(), "id не сопадают");
        assertEquals(name, epics.get(1).getName(), "name не сопадают");
        assertEquals(taskDescription, epics.get(1).getTaskDescription(), "taskDescription не сопадаtт");
        assertEquals(status, epics.get(1).getStatus(), "status не сопадаeт");

        // проверка на неверный url
        URI url1 = URI.create("http://localhost:8080/epics/epics");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gsonEpic))
                .build();
        HttpResponse<Void> response1 = client.send(request1, HttpResponse.BodyHandlers.discarding());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void addSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        // Добавляем сабтаск
        String name = "Test addNewSubtask3";
        String taskDescription = "Test addNewESubtask description3";
        TaskStatus status = TaskStatus.NEW;
        int epicId = 2;
        LocalDateTime localDateTime = LocalDateTime.of(2004, 8, 5, 18, 21, 36);
        int duration = 85;
        Subtask subtask3 = new Subtask(name, taskDescription, status, epicId, localDateTime, duration);

        String gsonSubtask = gson.toJson(subtask3);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gsonSubtask))
                .build();

        List<Subtask> subtasks = taskManager.getListOfSubTasks();
        assertEquals(2, subtasks.size(), "Размер списка сабтасков не равен 2");

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        subtasks = taskManager.getListOfSubTasks();

        assertEquals(201, response.statusCode(), "Код ответа не равен 201");
        assertEquals(3, subtasks.size(), "Размер списка сабтасков не равен 3");

        assertEquals(5, subtasks.get(2).getId(), "id не сопадают");
        assertEquals(epicId, subtasks.get(2).getEpicId(), "epicId не сопадают");
        assertEquals(name, subtasks.get(2).getName(), "name не сопадают");
        assertEquals(taskDescription, subtasks.get(2).getTaskDescription(), "taskDescription не сопадаtт");
        assertEquals(status, subtasks.get(2).getStatus(), "status не сопадаeт");
        assertEquals(localDateTime, subtasks.get(2).getStartTime(), "StartTime не сопадаeт");
        assertEquals(duration, subtasks.get(2).getDuration(), "duration не сопадаeт");

        // проверка на неверный url
        URI url1 = URI.create("http://localhost:8080/subtasks/subtasks");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gsonSubtask))
                .build();
        HttpResponse<Void> response1 = client.send(request1, HttpResponse.BodyHandlers.discarding());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void updateTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks?id=1");
        // Создаем задачу
        int id = 1;
        String name = "Test addNewTask2";
        String taskDescription = "Test addNewTask description2";
        TaskStatus status = TaskStatus.DONE;
        LocalDateTime localDateTime = LocalDateTime.of(
                2000, 11, 1, 15, 41, 16);
        int duration = 7;
        Task task2 = new Task(id, name, taskDescription, status, localDateTime, duration);

        String gsonTask = gson.toJson(task2);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gsonTask))
                .build();

        List<Task> tasks = taskManager.getListOfTasks();
        assertEquals(1, tasks.size(), "Размер списка задач не равен 1");
        assertEquals(id, tasks.get(0).getId(), "id задачи не равен 1");

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        tasks = taskManager.getListOfTasks();

        assertEquals(1, tasks.size(), "Размер списка задач не равен 1");
        assertEquals(id, tasks.get(0).getId(), "id задачи не равен 1");
        assertEquals(201, response.statusCode(), "Код ответа не равен 201");

        assertEquals(name, tasks.get(0).getName(), "name не сопадают");
        assertEquals(taskDescription, tasks.get(0).getTaskDescription(), "taskDescription не сопадаtт");
        assertEquals(status, tasks.get(0).getStatus(), "status не сопадаeт");
        assertEquals(localDateTime, tasks.get(0).getStartTime(), "StartTime не сопадаeт");
        assertEquals(duration, tasks.get(0).getDuration(), "duration не сопадаeт");

        // проверка на неверный url
        URI url1 = URI.create("http://localhost:8080/tasks/tas?id=1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gsonTask))
                .build();
        HttpResponse<Void> response1 = client.send(request1, HttpResponse.BodyHandlers.discarding());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void updateEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics?id=2");
        // Создаем эпик
        int id = 2;
        String name = "Test addNewEpic2";
        String taskDescription = "Test addNewEpic description2";
        TaskStatus status = TaskStatus.NEW;
        LocalDateTime localDateTime = LocalDateTime.of(
                2023, 2, 6, 10, 14, 16);
        int duration = 150;
        Epic epic2 = new Epic(id, name, taskDescription, status, localDateTime, duration);

        String gsonEpic = gson.toJson(epic2);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gsonEpic))
                .build();

        List<Epic> epics = taskManager.getListOfEpics();
        assertEquals(1, epics.size(), "Размер списка эпиков не равен 1");
        assertEquals(id, epics.get(0).getId(), "id задачи не равен 2");

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        epics = taskManager.getListOfEpics();

        assertEquals(1, epics.size(), "Размер списка эпиков не равен 1");
        assertEquals(id, epics.get(0).getId(), "id задачи не равен 1");
        assertEquals(201, response.statusCode(), "Код ответа не равен 201");

        assertEquals(name, epics.get(0).getName(), "name не сопадает");
        assertEquals(taskDescription, epics.get(0).getTaskDescription(), "taskDescription не сопадает");
        assertEquals(status, epics.get(0).getStatus(), "status не сопадаeт");
        assertEquals(localDateTime, epics.get(0).getStartTime(), "StartTime не сопадаeт");
        assertEquals(duration, epics.get(0).getDuration(), "duration не сопадаeт");

        // проверка на неверный url
        URI url1 = URI.create("http://localhost:8080/tasks/tas?id=1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gsonEpic))
                .build();
        HttpResponse<Void> response1 = client.send(request1, HttpResponse.BodyHandlers.discarding());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }

    @Test
    public void updateSubtaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks?id=4");
        // Создаем эпик
        int id = 4;
        int idEpic = 2;
        String name = "Test addNewSubtask3";
        String taskDescription = "Test addNewSubtask description3";
        TaskStatus status = TaskStatus.DONE;
        LocalDateTime localDateTime = LocalDateTime.of(
                2026, 2, 6, 10, 14, 16);
        int duration = 170;
        Subtask subtask2 = new Subtask(id, name, taskDescription, status, idEpic, localDateTime, duration);

        String gsonSubtask = gson.toJson(subtask2);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gsonSubtask))
                .build();

        List<Subtask> subtasks = taskManager.getListOfSubTasks();
        assertEquals(2, subtasks.size(), "Размер списка сабтасков не равен 2");
        assertEquals(id, subtasks.get(1).getId(), "id сабтаска не равен 4");

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        subtasks = taskManager.getListOfSubTasks();

        assertEquals(2, subtasks.size(), "Размер списка сабтасков не равен 2");
        assertEquals(id, subtasks.get(1).getId(), "id сабтаск не равен 4");
        assertEquals(201, response.statusCode(), "Код ответа не равен 201");

        assertEquals(name, subtasks.get(1).getName(), "name не сопадает");
        assertEquals(taskDescription, subtasks.get(1).getTaskDescription(), "taskDescription не сопадает");
        assertEquals(status, subtasks.get(1).getStatus(), "status не сопадаeт");
        assertEquals(localDateTime, subtasks.get(1).getStartTime(), "StartTime не сопадаeт");
        assertEquals(duration, subtasks.get(1).getDuration(), "duration не сопадаeт");

        // проверка на неверный url
        URI url1 = URI.create("http://localhost:8080/subtasks/jud?id=4");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gsonSubtask))
                .build();
        HttpResponse<Void> response1 = client.send(request1, HttpResponse.BodyHandlers.discarding());
        assertEquals(404, response1.statusCode(), "Код ответа не равен 404");
    }


    @Test
    public void intersectionsWhenAddingATaskEnter406() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        // Добавляем задачу
        String name = "Test addNewTask2";
        String taskDescription = "Test addNewTask description2";
        TaskStatus status = TaskStatus.NEW;
        LocalDateTime localDateTime = LocalDateTime.of(
                2020, 12, 8, 2, 51, 16);
        int duration = 70;
        Task task2 = new Task(name, taskDescription, status, localDateTime, duration);
        String gsonTask = gson.toJson(task2);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gsonTask))
                .build();

        List<Task> tasks = taskManager.getListOfTasks();
        assertEquals(1, tasks.size(), "Размер списка задач не равен 1");

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        tasks = taskManager.getListOfTasks();

        assertEquals(406, response.statusCode(), "Код ответа не равен 406");
        assertEquals(1, tasks.size(), "Размер списка задач не равен 1"); // задача не добавилась
    }

    @Test
    public void intersectionsWhenUpdateATaskEnter406() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks?id=1");
        // Создаем задачу
        int id = 1;
        String name = "Test addNewTask2";
        String taskDescription = "Test addNewTask description2";
        TaskStatus status = TaskStatus.DONE;
        LocalDateTime localDateTime = LocalDateTime.of(
                2024, 2, 5, 18, 11, 16);
        int duration = 30;
        Task task2 = new Task(id, name, taskDescription, status, localDateTime, duration);

        String gsonTask = gson.toJson(task2);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gsonTask))
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        List<Task> tasks = taskManager.getListOfTasks();

        assertEquals(406, response.statusCode(), "Код ответа не равен 406");

        assertNotEquals(localDateTime, tasks.get(0).getStartTime(), "StartTime сопадаeт");
        assertNotEquals(duration, tasks.get(0).getDuration(), "duration сопадаeт");
    }

    @Test
    public void intersectionsWhenAddingASubtaskEnter406() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        // Добавляем сабтаск
        String name = "Test addNewSubtask3";
        String taskDescription = "Test addNewESubtask description3";
        TaskStatus status = TaskStatus.NEW;
        int epicId = 2;
        LocalDateTime localDateTime = LocalDateTime.of(
                2020, 12, 8, 2, 51, 16);
        int duration = 70;
        Subtask subtask3 = new Subtask(name, taskDescription, status, epicId, localDateTime, duration);

        String gsonSubtask = gson.toJson(subtask3);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gsonSubtask))
                .build();

        List<Subtask> subtasks = taskManager.getListOfSubTasks();
        assertEquals(2, subtasks.size(), "Размер списка сабтасков не равен 2");

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        subtasks = taskManager.getListOfSubTasks();

        assertEquals(406, response.statusCode(), "Код ответа не равен 406");
        assertEquals(2, subtasks.size(), "Размер списка сабтасков не равен 2");
    }

    @Test
    public void intersectionsWhenUpdateASubtaskEnter406() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks?id=4");
        // Создаем эпик
        int id = 4;
        int idEpic = 2;
        String name = "Test addNewSubtask3";
        String taskDescription = "Test addNewSubtask description3";
        TaskStatus status = TaskStatus.DONE;
        LocalDateTime localDateTime = LocalDateTime.of(
                2020, 12, 8, 2, 51, 16);
        int duration = 70;
        Subtask subtask2 = new Subtask(id, name, taskDescription, status, idEpic, localDateTime, duration);

        String gsonSubtask = gson.toJson(subtask2);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gsonSubtask))
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        List<Subtask> subtasks = taskManager.getListOfSubTasks();
        assertEquals(406, response.statusCode(), "Код ответа не равен 406");

        assertNotEquals(localDateTime, subtasks.get(1).getStartTime(), "StartTime сопадаeт");
        assertNotEquals(duration, subtasks.get(1).getDuration(), "duration сопадаeт");
    }
}
