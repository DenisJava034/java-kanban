package servers.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import servers.util.ServerUtility;

import java.io.IOException;
import java.util.regex.Pattern;

public class HistoryHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public HistoryHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();

            if (Pattern.matches("^/history$", path) && requestMethod.equals("GET")) {
                String response = gson.toJson(taskManager.getHistory());
                ServerUtility.sendText(httpExchange, response, 200);
            } else {
                httpExchange.sendResponseHeaders(404, 0);
            }
        } catch (Exception exception) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }
}
