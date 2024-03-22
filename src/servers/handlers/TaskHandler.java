package servers.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
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

    public Gson getGson() {
        return gson;
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
                        int id = ServerUtility.getId(pathId);

                        if (id != -1) {
                            String response = gson.toJson(taskManager.getTaskById(id));
                            if (response.equals("null")) {
                                httpExchange.sendResponseHeaders(404, 0);
                                break;
                            }
                            ServerUtility.sendText(httpExchange, response, 200);
                            break;
                        } else {
                            System.out.println("Получен некорректный id" + id);
                            httpExchange.sendResponseHeaders(404, 0);
                            break;
                        }
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
                            int id = ServerUtility.getId(idParam);

                            String body = ServerUtility.readText(httpExchange); //получаем тело
                            Task task;

                            if (id != -1) {
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
                            } else {
                                System.out.println("Получен некорректный id" + id);
                                httpExchange.sendResponseHeaders(404, 0);
                                break;
                            }
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
                            int id = ServerUtility.getId(idParam);
                            if (id != -1) {
                                String response = gson.toJson(taskManager.getTaskById(id));
                                if (response.equals("null")) {  // если null то закой задачи нет
                                    httpExchange.sendResponseHeaders(404, 0);
                                    break;
                                }
                                taskManager.deleteTaskById(id);
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                System.out.println("Получен некорректный id" + idParam);
                                httpExchange.sendResponseHeaders(404, 0);
                                break;
                            }
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
