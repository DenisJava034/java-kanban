package servers;

import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import servers.handlers.*;
import servers.util.ServerUtility;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;


    public HttpTaskServer(TaskManager taskManager) throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);

        server.createContext("/tasks", new TaskHandler(taskManager, ServerUtility.getGson()));
        server.createContext("/epics", new EpicsHandler(taskManager, ServerUtility.getGson()));
        server.createContext("/subtasks", new SubtasksHandler(taskManager, ServerUtility.getGson()));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager, ServerUtility.getGson()));
        server.createContext("/history", new HistoryHandler(taskManager, ServerUtility.getGson()));
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(Managers.getDefault());
        httpTaskServer.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        server.start();
    }
}




