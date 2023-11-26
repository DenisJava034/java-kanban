package Manager;

import Tasks.Task;

import java.util.ArrayList;

public interface HistoryManager {

    ArrayList<Task> getHistory();

    void addTaskOfListHistory(Task task);

    void remove(int id);
}
