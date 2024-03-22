package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {

    ArrayList<Task> getListOfTasks();

    ArrayList<Epic> getListOfEpics();

    ArrayList<Subtask> getListOfSubTasks();

    Object deleteAllTasks() throws IOException;

    Object deleteAllEpic() throws IOException;

    Object deleteAllSubtask() throws IOException;

    Task getTaskById(int id) throws IOException;

    Epic getEpicById(int id) throws IOException;

    Subtask getSubtaskById(int id) throws IOException;

    void createTask(Task task) throws IOException;

    void createEpic(Epic epic) throws IOException;

    void createSubtask(Subtask subtask) throws IOException;

    void updateTask(Task task) throws IOException;

    void updateEpic(Epic epic) throws IOException;

    void updateSubtask(Subtask subtask) throws IOException;

    Object deleteTaskById(int id) throws IOException;

    Object deleteEpicById(int id) throws IOException;

    Object deleteSubtaskById(int id) throws IOException;

    ArrayList<Subtask> getListSubtaskByEpicId(int id);

    List<Task> getHistory();

    TreeSet getPrioritizedTasks();


}
