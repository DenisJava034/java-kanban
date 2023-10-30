package Manager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    ArrayList<Task> getListOfTasks();

    ArrayList<Epic> getListOfEpics();

    ArrayList<Subtask> getListOfSubTasks();

    void deleteAllTasks();

    void deleteAllEpic();

    void deleteAllSubtask();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    ArrayList<Subtask> getListSubtaskByEpicId(int id);

    List<Task> getHistory();
}
