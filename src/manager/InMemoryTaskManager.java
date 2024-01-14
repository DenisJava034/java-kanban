package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    protected int id = 0;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected TreeSet<Task> startTimeSet = new TreeSet<>(Comparator.comparing(
           Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task :: getId));


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
        for (Task task : tasks.values()){
            startTimeSet.remove(task);
        }

        tasks.clear();

    }

    @Override
    public void deleteAllEpic() {
        for (int idSub : epics.keySet()) {
            historyManager.remove(idSub);
        }
        for (Subtask subtask : subtasks.values()){
            startTimeSet.remove(subtask);
        }

        for (Epic epic : epics.values()){
            startTimeSet.remove(epic);
        }
        epics.clear();
        deleteAllSubtask();

    }

    @Override
    public void deleteAllSubtask() {
        for (int idSub : subtasks.keySet()) {
            historyManager.remove(idSub);
        }
        for (Subtask subtask : subtasks.values()){
            startTimeSet.remove(subtask);
        }
        subtasks.clear();
        for (int idEpic : epics.keySet()) {
            epics.get(idEpic).getSubtaskId().clear();
            startTimeSet.remove(epics.get(idEpic));
            startTimeEndTimeEpic(idEpic);
            startTimeSet.add(epics.get(idEpic));
        }
        for (int epicKey : epics.keySet()) {
            updateStatusEpic(epics.get(epicKey).getId());
        }

    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.addTaskOfListHistory(tasks.get(id));
        }
        return task;
    }
    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.addTaskOfListHistory(epics.get(id));
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.addTaskOfListHistory(subtasks.get(id));
        }
        return subtask;
    }

    @Override
    public void createTask(Task task) {
        if(checkStartTime(task)){
            id++;
            task.setId(id);
            tasks.put(id, task);
            startTimeSet.add(task);
       }else {
            System.out.println("Имеется пересечение задачи");
        }
    }

    @Override
    public void createEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
        startTimeEndTimeEpic(epic.getId());
        startTimeSet.add(epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if(checkStartTime(subtask)){
            id++;
            epics.get(subtask.getEpicId()).getSubtaskId().add(id);
            subtask.setId(id);
            subtasks.put(id, subtask);
            updateStatusEpic(subtask.getEpicId());
            startTimeSet.add(subtask);
            startTimeSet.remove(epics.get(subtask.getEpicId()));
            startTimeEndTimeEpic(subtask.getEpicId());
            startTimeSet.add(epics.get(subtask.getEpicId()));

        }else {
           System.out.println("Имеется пересечение задачи");
        }
    }

    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Task с id - " + task.getId() + " не найдено");
            return;
        }
        if (tasks.get(task.getId()).getStartTime().equals(task.getStartTime()) &&
                tasks.get(task.getId()).getDuration() == task.getDuration()){
            tasks.put(task.getId(), task);
        }
        else if(checkStartTime(task)){
            tasks.put(task.getId(), task);
        }else {
            System.out.println("Имеется пересечение задачи");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            System.out.println("Epic с id - " + epic.getId() + " не найдено");
            return;
        }
        if(epics.get(epic.getId()).getStartTime() == epic.getStartTime() && // Проверка на то что не были изменены
                epics.get(epic.getId()).getDuration() == epic.getDuration()){  // поля которые должны расчитываться
                                                                               // автоматически исходя из подзадач.
            epic.setSubtaskId(epics.get(epic.getId()).getSubtaskId());
            epics.put(epic.getId(),epic);

            updateStatusEpic(epics.get(epic.getId()).getId());
            startTimeEndTimeEpic(epics.get(epic.getId()).getId());

        }else {
            System.out.println("Время начала и продолжительность Эпика не могут быть изменены вручную");
        }

    }

    @Override
    public void updateSubtask(Subtask subtask) {

        if (!subtasks.containsKey(subtask.getId())) {
            System.out.println("Subtask с id - " + subtask.getId() + " не найдено");
            return;
        }
        if (subtasks.get(subtask.getId()).getStartTime().equals(subtask.getStartTime()) &&
                subtasks.get(subtask.getId()).getDuration() == subtask.getDuration()){

            subtask.setEpicId(subtasks.get(subtask.getId()).getEpicId());
            subtasks.put(subtask.getId(),subtask);

            updateStatusEpic(subtasks.get(subtask.getId()).getEpicId());

        }
        else if(checkStartTime(subtask)){
            subtask.setEpicId(subtasks.get(subtask.getId()).getEpicId());
            subtasks.put(subtask.getId(),subtask);

            updateStatusEpic(subtasks.get(subtask.getId()).getEpicId());
        }else {
            System.out.println("Имеется пересечение задачи");
        }

    }

    @Override
    public void deleteTaskById(int id) {

        if (!tasks.containsKey(id)) {
            System.out.println("Tasks.Task с id - " + id + " не найдено");
            return;
        }
        startTimeSet.remove(tasks.get(id));
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
            startTimeSet.remove(subtasks.get(idSub));
            subtasks.remove(idSub);
            historyManager.remove(idSub);
        }
        startTimeSet.remove(epics.get(id));
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
        startTimeSet.remove(subtasks.get(id));
        subtasks.remove(id);
        historyManager.remove(id);


        startTimeEndTimeEpic(epicId);
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

    protected void startTimeEndTimeEpic(int epicId) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        int durationSum = 0;

        for(int idSub : epics.get(epicId).getSubtaskId()){
            durationSum += subtasks.get(idSub).getDuration();

            if (startTime == null && endTime == null){
                startTime = subtasks.get(idSub).getStartTime();
                endTime = subtasks.get(idSub).getStartTime();
                continue;
            }

            if(subtasks.get(idSub).getStartTime().isBefore(startTime)){  //min time
                startTime = subtasks.get(idSub).getStartTime();
            }

            if(subtasks.get(idSub).getStartTime().isAfter(endTime)){       // max time
                endTime = subtasks.get(idSub).getStartTime().plusMinutes(subtasks.get(idSub).getDuration());
            }
        }
        epics.get(epicId).setStartTime(startTime);
        epics.get(epicId).setEndTime(endTime);
        epics.get(epicId).setDuration(durationSum);
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() { // Проверка пересечений
        return startTimeSet;
    }

    protected boolean checkStartTime(Task task) {

        for (Task taskSet : startTimeSet) {
            if(taskSet instanceof Epic){
                continue;
            }
            if(taskSet.getStartTime().equals(task.getStartTime())) {
                return false;
            }
            else if(taskSet.getStartTime().isAfter(task.getStartTime())) {
                if (taskSet.getStartTime().isAfter(task.getEndTime())) {
                    continue;
                }else {
                    return false;
                }
            }
            else if(taskSet.getStartTime().isBefore(task.getStartTime())) {
                if (taskSet.getEndTime().isBefore(task.getStartTime())) {
                    continue;
                }else {
                    return false;
                }
            }
        }
        return true;
    }
}
