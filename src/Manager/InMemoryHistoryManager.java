package Manager;

import Tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    protected LinkedList<Task> historyListTask = new LinkedList<>();

    @Override
    public void addTaskOfListHistory(Task task) {
        historyListTask.addLast(task);
        if (historyListTask.size() > 10){
            historyListTask.remove(0);
        }
    }

    @Override
    public LinkedList<Task> getHistory() {
        LinkedList<Task> historyList = historyListTask;
        return historyList;
    }
}
