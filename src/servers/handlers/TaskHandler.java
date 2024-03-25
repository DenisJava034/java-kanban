package servers.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.FormatIdException;
import manager.TaskManager;
import servers.util.ServerUtility;
import tasks.Task;

import java.io.IOException;
import java.util.regex.Pattern;

public class TaskHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TaskHandler(TaskManager taskManager, Gson gson) {

        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getQuery();

            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/\\d+$", path)) {

                        String pathId = path.replaceFirst("/tasks/", "");
                        int id;

                        try {
                            id = ServerUtility.getId(pathId);
                        } catch (NumberFormatException | FormatIdException e) {
                            System.out.println(e.getMessage());
                            httpExchange.sendResponseHeaders(404, 0);
                            break;
                        }
                        String response = gson.toJson(taskManager.getTaskById(id));
                        if (response.equals("null")) {
                            httpExchange.sendResponseHeaders(404, 0);
                            break;
                        }
                        ServerUtility.sendText(httpExchange, response, 200);
                        break;
                    }
                    if (Pattern.matches("^/tasks$", path)) {
                        String response = gson.toJson(taskManager.getListOfTasks());
                        ServerUtility.sendText(httpExchange, response, 200);
                        break;
                    } else {
                        System.out.println("Не верный адрес" + path);
                        httpExchange.sendResponseHeaders(404, 0);
                    }
                }
                case "POST": {
                    if (Pattern.matches("^/tasks$", path)) {
                        if (query == null) {
                            String body = ServerUtility.readText(httpExchange);
                            Task task;
                            try {
                                task = gson.fromJson(body, Task.class); // Создаем Task
                            } catch (JsonSyntaxException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                                break;
                            }
                            taskManager.createTask(task); // Добавляем задачу

                            if (!taskManager.getListOfTasks().contains(task)) {   // Если задача не добавилась, то есть
                                httpExchange.sendResponseHeaders(406, 0); // пересечения
                                break;
                            }
                            httpExchange.sendResponseHeaders(201, 0);
                            break;

                        } else {
                            String idParam = query.substring(3);
                            String body = ServerUtility.readText(httpExchange); //получаем тело
                            int id;
                            Task task;

                            try {
                                id = ServerUtility.getId(idParam);
                            } catch (NumberFormatException | FormatIdException e) {
                                System.out.println(e.getMessage());
                                httpExchange.sendResponseHeaders(404, 0);
                                break;
                            }
                            String response = gson.toJson(taskManager.getTaskById(id));
                            if (response.equals("null")) {  // если нет такой заачи
                                httpExchange.sendResponseHeaders(404, 0);
                                break;
                            }

                            try {
                                task = gson.fromJson(body, Task.class); // Создаем Task
                            } catch (JsonSyntaxException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                                break;
                            }
                            taskManager.updateTask(task);

                            if (!taskManager.getListOfTasks().contains(task)) {   // Если задача не обновилась, то есть
                                httpExchange.sendResponseHeaders(406, 0); // пересечения
                                break;
                            }

                            httpExchange.sendResponseHeaders(201, 0);
                            break;
                        }
                    } else {
                        System.out.println("Получен некорректный адрес" + path);
                        httpExchange.sendResponseHeaders(404, 0);
                        break;
                    }
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks$", path)) {
                        if (query == null) {
                            taskManager.deleteAllTasks();
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        } else {
                            String idParam = query.substring(3);
                            int id;

                            try {
                                id = ServerUtility.getId(idParam);
                            } catch (NumberFormatException | FormatIdException e) {
                                System.out.println(e.getMessage());
                                httpExchange.sendResponseHeaders(404, 0);
                                break;
                            }
                            String response = gson.toJson(taskManager.getTaskById(id));
                            if (response.equals("null")) {  // если null то закой задачи нет
                                httpExchange.sendResponseHeaders(404, 0);
                                break;
                            }
                            taskManager.deleteTaskById(id);
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                    } else {
                        System.out.println("Получен некорректный адрес" + path);
                        httpExchange.sendResponseHeaders(404, 0);
                        break;
                    }
                }
            }
        } catch (Exception exception) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }
}
