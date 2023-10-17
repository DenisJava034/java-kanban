package Manager;
import Tasks.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    protected int id = 0;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public ArrayList<Task> getListOfTasks() {
        ArrayList<Task> taskArrayList = new ArrayList<>(tasks.values());
        return taskArrayList;
    }

    public ArrayList<Epic> getListOfEpics() {
        ArrayList<Epic> epicArrayList = new ArrayList<>(epics.values());
        return epicArrayList;
    }

    public ArrayList<Subtask> getListOfSubTasks() {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>(subtasks.values());
        return subtaskArrayList;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpic() {
        epics.clear();
        deleteAllSubtask();
    }

    public void deleteAllSubtask() {
        subtasks.clear();
        for (int idEpic : epics.keySet()) {
            epics.get(idEpic).getSubtaskId().clear();
        }
        for (int epicKey : epics.keySet()) {
            updateStatusEpic(epics.get(epicKey).getId());
        }
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void createTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
    }

    public void createEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
    }

    public void createSubtask(Subtask subtask) {
        id++;
        epics.get(subtask.getEpicId()).getSubtaskId().add(id);
        subtask.setId(id);
        subtasks.put(id, subtask);
    }

    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Tasks.Task с id - " + task.getId() + " не найдено");
            return;
        }
        tasks.remove(task.getId());
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {

        if (!epics.containsKey(epic.getId())) {
            System.out.println("Tasks.Epic с id - " + epic.getId() + " не найдено");
            return;
        }
        epics.get(epic.getId()).setNameTask(epic.getName());
        epics.get(epic.getId()).setTaskDescription(epic.getTaskDescription());

        updateStatusEpic(epics.get(epic.getId()).getId());
    }

    public void updateSubtask(Subtask subtask) {

        if (!subtasks.containsKey(subtask.getId())) {
            System.out.println("Tasks.Subtask с id - " + subtask.getId() + " не найдено");
            return;
        }
        subtasks.get(subtask.getId()).setNameTask(subtask.getName());
        subtasks.get(subtask.getId()).setTaskDescription(subtask.getTaskDescription());
        subtasks.get(subtask.getId()).setStatus(subtask.getStatus());

        updateStatusEpic(subtasks.get(subtask.getId()).getEpicId());
    }

    public void deleteTaskById(int id) {

        if (!tasks.containsKey(id)) {
            System.out.println("Tasks.Task с id - " + id + " не найдено");
            return;
        }
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {

        if (!epics.containsKey(id)) {
            System.out.println("Tasks.Epic с id - " + id + " не найдено");
            return;
        }
        for (int idSub : epics.get(id).getSubtaskId()) {
            subtasks.remove(idSub);
        }
        epics.remove(id);
    }

    public void deleteSubtaskById(int id) {
        int epicId;

        if (!subtasks.containsKey(id)) {
            System.out.println("Tasks.Subtask с id - " + id + " не найдено");
            return;
        }
        epicId = subtasks.get(id).getEpicId();
        int idSub = epics.get(subtasks.get(id).getEpicId()).getSubtaskId().indexOf(id);
        epics.get(subtasks.get(id).getEpicId()).getSubtaskId().remove(idSub);
        subtasks.remove(id);

        updateStatusEpic(epicId);
    }

    public ArrayList<Subtask> getListSubtaskByEpicId(int id) {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();

        for (int idSub : epics.get(id).getSubtaskId()) {
            subtaskArrayList.add(subtasks.get(idSub));
        }
        return subtaskArrayList;
    }

    public void updateStatusEpic(int epicId) {
        int sumNEW = 0;
        int sumDONE = 0;

        if (!epics.get(epicId).getSubtaskId().isEmpty()) {
            for (int idSub : epics.get(epicId).getSubtaskId()) {
                if (subtasks.get(idSub).getStatus().equals("NEW")) {
                    ++sumNEW;
                } else if (subtasks.get(idSub).getStatus().equals("DONE")) {
                    ++sumDONE;
                }
            }
            if (sumDONE == epics.get(epicId).getSubtaskId().size()) {
                epics.get(epicId).setStatus("DONE");
            } else if (sumNEW == epics.get(epicId).getSubtaskId().size()) {
                epics.get(epicId).setStatus("NEW");
            } else {
                epics.get(epicId).setStatus("IN_PROGRESS");
            }
        }else{
            epics.get(epicId).setStatus("NEW");
        }
    }
}
