package Manager;

import Tasks.Task;

import java.util.LinkedList;

public interface HistoryManager {

    LinkedList<Task> getHistory();

    void addTaskOfListHistory(Task task);
}
