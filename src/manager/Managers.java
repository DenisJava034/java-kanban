package manager;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(new File("FileSave.csv"));
        fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("FileSave.csv"));
        return fileBackedTasksManager;
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
