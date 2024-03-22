package servers.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import servers.util.ServerUtility;
import tasks.Epic;

import java.io.IOException;
import java.util.regex.Pattern;

public class EpicsHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicsHandler(TaskManager taskManager, Gson gson) {
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
                    if (path.split("/")[1].equals("epics")) {

                        if (Pattern.matches("^/epics$", path)) {
                            String response = gson.toJson(taskManager.getListOfEpics());
                            ServerUtility.sendText(httpExchange, response, 200);
                            break;
                        }
                        if (Pattern.matches("^/epics/\\d+$", path)) {
                            String pathId = path.replaceFirst("/epics/", "");
                            int id = ServerUtility.getId(pathId);

                            if (id != -1) {
                                String response = gson.toJson(taskManager.getEpicById(id));
                                if (response.equals("null")) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    break;
                                }
                                ServerUtility.sendText(httpExchange, response, 200);
                                break;

                            } else {
                                System.out.println("Получен некорректный id" + pathId);
                                httpExchange.sendResponseHeaders(404, 0);
                                break;
                            }
                        }

                        if (Pattern.matches("^/epics/+\\d+/subtasks$", path)) {

                            String pathId = path.split("/")[2];
                            int id = ServerUtility.getId(pathId);

                            if (id != -1) {

                                String response = gson.toJson(taskManager.getEpicById(id)); // проверка на наличие Эпика
                                if (response.equals("null")) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    break;
                                }
                                response = gson.toJson(taskManager.getListSubtaskByEpicId(id));
                                ServerUtility.sendText(httpExchange, response, 200);
                                break;

                            } else {
                                System.out.println("Получен некорректный id" + pathId);
                                httpExchange.sendResponseHeaders(404, 0);
                                break;
                            }
                        }

                    } else {
                        System.out.println("Не верный адрес" + path);
                        httpExchange.sendResponseHeaders(404, 0);
                    }

                }
                case "POST": {
                    if (Pattern.matches("^/epics$", path)) {
                        if (query == null) {
                            String body = ServerUtility.readText(httpExchange);
                            Epic epic;
                            try {
                                epic = gson.fromJson(body, Epic.class); // Создаем TEpic
                            } catch (JsonSyntaxException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                                break;
                            }
                            taskManager.createEpic(epic); // Добавляем эпик

                            if (!taskManager.getListOfEpics().contains(epic)) {   // Если эпик не добавился, то есть
                                httpExchange.sendResponseHeaders(406, 0); // пересечения
                                break;
                            }
                            httpExchange.sendResponseHeaders(201, 0);
                            break;

                        } else {
                            String idParam = query.substring(3);
                            int id = ServerUtility.getId(idParam);

                            String body = ServerUtility.readText(httpExchange); //получаем тело
                            Epic epic;

                            if (id != -1) {
                                String response = gson.toJson(taskManager.getEpicById(id));
                                if (response.equals("null")) {  // если нет такой заачи
                                    httpExchange.sendResponseHeaders(404, 0);
                                    break;
                                }

                                try {
                                    epic = gson.fromJson(body, Epic.class); // Создаем Epic
                                } catch (JsonSyntaxException e) {
                                    httpExchange.sendResponseHeaders(400, 0);
                                    break;
                                }
                                taskManager.updateEpic(epic);

                                if (!taskManager.getListOfEpics().contains(epic)) {   // Если задача не обновилась, то есть
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
                    if (Pattern.matches("^/epics$", path)) {
                        if (query == null) {
                            taskManager.deleteAllEpic();
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        } else {
                            String idParam = query.substring(3);
                            int id = ServerUtility.getId(idParam);
                            if (id != -1) {
                                String response = gson.toJson(taskManager.getEpicById(id));
                                if (response.equals("null")) {  // если null то закой задачи нет
                                    httpExchange.sendResponseHeaders(404, 0);
                                    break;
                                }
                                taskManager.deleteEpicById(id);
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
