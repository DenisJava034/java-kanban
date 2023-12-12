package manager;
import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    protected int id = 0;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();

    protected HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public ArrayList<Task> getListOfTasks() {
        ArrayList<Task> taskArrayList = new ArrayList<>(tasks.values());
        return taskArrayList;
    }

    @Override
    public ArrayList<Epic> getListOfEpics() {
        ArrayList<Epic> epicArrayList = new ArrayList<>(epics.values());
        return epicArrayList;
    }

    @Override
    public ArrayList<Subtask> getListOfSubTasks() {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>(subtasks.values());
        return subtaskArrayList;
    }

    @Override
    public void deleteAllTasks() {
        for (int idSub : tasks.keySet()) {
            historyManager.remove(idSub);
        }

        tasks.clear();
    }

    @Override
    public void deleteAllEpic() {
        for (int idSub : epics.keySet()) {
            historyManager.remove(idSub);
        }
        epics.clear();
        deleteAllSubtask();
    }

    @Override
    public void deleteAllSubtask() {
        for (int idSub : subtasks.keySet()) {
            historyManager.remove(idSub);
        }
        subtasks.clear();
        for (int idEpic : epics.keySet()) {
            epics.get(idEpic).getSubtaskId().clear();
        }
        for (int epicKey : epics.keySet()) {
            updateStatusEpic(epics.get(epicKey).getId());
        }
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.addTaskOfListHistory(tasks.get(id));
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        historyManager.addTaskOfListHistory(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.addTaskOfListHistory(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void createTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void createEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        id++;
        epics.get(subtask.getEpicId()).getSubtaskId().add(id);
        subtask.setId(id);
        subtasks.put(id, subtask);
    }

    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Tasks.Task с id - " + task.getId() + " не найдено");
            return;
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {

        if (!epics.containsKey(epic.getId())) {
            System.out.println("Tasks.Epic с id - " + epic.getId() + " не найдено");
            return;
        }
        epic.setSubtaskId(epics.get(epic.getId()).getSubtaskId());
        epics.put(epic.getId(),epic);

        updateStatusEpic(epics.get(epic.getId()).getId());
    }

    @Override
    public void updateSubtask(Subtask subtask) {

        if (!subtasks.containsKey(subtask.getId())) {
            System.out.println("Tasks.Subtask с id - " + subtask.getId() + " не найдено");
            return;
        }
        subtask.setEpicId(subtasks.get(subtask.getId()).getEpicId());
        subtasks.put(subtask.getId(),subtask);

        updateStatusEpic(subtasks.get(subtask.getId()).getEpicId());
    }

    @Override
    public void deleteTaskById(int id) {

        if (!tasks.containsKey(id)) {
            System.out.println("Tasks.Task с id - " + id + " не найдено");
            return;
        }
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {

        if (!epics.containsKey(id)) {
            System.out.println("Tasks.Epic с id - " + id + " не найдено");
            return;
        }
        for (int idSub : epics.get(id).getSubtaskId()) {
            subtasks.remove(idSub);
            historyManager.remove(idSub);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        int epicId = subtasks.get(id).getEpicId();

        if (!subtasks.containsKey(id)) {
            System.out.println("Tasks.Subtask с id - " + id + " не найдено");
            return;
        }
        int idSub = epics.get(subtasks.get(id).getEpicId()).getSubtaskId().indexOf(id);
        epics.get(subtasks.get(id).getEpicId()).getSubtaskId().remove(idSub);
        subtasks.remove(id);
        historyManager.remove(id);

        updateStatusEpic(epicId);
    }

    @Override
    public ArrayList<Subtask> getListSubtaskByEpicId(int id) {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();

        for (int idSub : epics.get(id).getSubtaskId()) {
            subtaskArrayList.add(subtasks.get(idSub));
        }
        return subtaskArrayList;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    protected void updateStatusEpic(int epicId) {
        int sumNEW = 0;
        int sumDONE = 0;

        if (!epics.get(epicId).getSubtaskId().isEmpty()) {
            for (int idSub : epics.get(epicId).getSubtaskId()) {
                if (subtasks.get(idSub).getStatus() == TaskStatus.NEW) {
                    ++sumNEW;
                } else if (subtasks.get(idSub).getStatus() == TaskStatus.DONE) {
                    ++sumDONE;
                }
            }
            if (sumDONE == epics.get(epicId).getSubtaskId().size()) {
                epics.get(epicId).setStatus(TaskStatus.DONE);
            } else if (sumNEW == epics.get(epicId).getSubtaskId().size()) {
                epics.get(epicId).setStatus(TaskStatus.NEW);
            } else {
                epics.get(epicId).setStatus(TaskStatus.IN_PROGRESS);
            }
        }else{
            epics.get(epicId).setStatus(TaskStatus.NEW);
        }
    }
}
