package Manager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    ArrayList<Task> getListOfTasks();

    ArrayList<Epic> getListOfEpics();

    ArrayList<Subtask> getListOfSubTasks();

    void deleteAllTasks() throws IOException;

    void deleteAllEpic() throws IOException;

    void deleteAllSubtask() throws IOException;

    Task getTaskById(int id) throws IOException;

    Epic getEpicById(int id) throws IOException;

    Subtask getSubtaskById(int id) throws IOException;

    void createTask(Task task) throws IOException;

    void createEpic(Epic epic) throws IOException;

    void createSubtask(Subtask subtask) throws IOException;

    void updateTask(Task task) throws IOException;

    void updateEpic(Epic epic) throws IOException;

    void updateSubtask(Subtask subtask) throws IOException;

    void deleteTaskById(int id) throws IOException;

    void deleteEpicById(int id) throws IOException;

    void deleteSubtaskById(int id) throws IOException;

    ArrayList<Subtask> getListSubtaskByEpicId(int id);

    List<Task> getHistory();
}
