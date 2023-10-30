package Manager;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    protected List<Task> historyListTask = new ArrayList<>();

    @Override
    public void addTaskOfListHistory(Task task) {
        if (historyListTask.size() == 10){
            historyListTask.remove(0);
            historyListTask.add(task);
        }else {
            historyListTask.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyListTask;
    }
}
