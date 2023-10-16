import java.util.HashMap;
public class Manager {
    int id = 0;
    HashMap <Integer, Task> tasks = new HashMap<>();
    HashMap <Integer, Epic> epics = new HashMap<>();
    HashMap <Integer, Subtask> subtasks = new HashMap<>();

    public void getListOfTasks() {
        for (int id : tasks.keySet()){
            System.out.println(tasks.get(id).name);
        }
    }

    public void getListOfEpics() {
        for (int id : epics.keySet()){
            System.out.println(epics.get(id).name);
        }
    }

    public void getListOfSubTasks() {
        for (int id : subtasks.keySet()){
            System.out.println(subtasks.get(id).name);
        }
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
        for (int idEpic : epics.keySet()){
            epics.get(idEpic).getSubtaskId().clear();
        }
    }

    public void getTaskById(int id){
        if(!tasks.containsKey(id)){
            System.out.println("Task с id - "+id+" не найдено");
            return;
        }
        System.out.println(tasks.get(id).name);
    }

    public void getEpicById(int id){
        if(!epics.containsKey(id)){
            System.out.println("Epic с id - "+id+" не найдено");
            return;
        }
        System.out.println(epics.get(id).name);
    }

    public void getSubtaskById(int id){
        if(!subtasks.containsKey(id)){
            System.out.println("Subtask с id - "+id+" не найдено");
            return;
        }
        System.out.println(subtasks.get(id).name);
    }

    public void createTask(Task task) {
        id++;
        task.id = id;;
        tasks.put(id, task);
    }

    public void createEpic(Epic epic) {
        id++;
        epic.id = id;
        epics.put(id, epic);
    }

    public void createSubtask(Subtask subtask) {
        id++;
        epics.get(subtask.getEpicId()).getSubtaskId().add(id);
        subtask.id = id;
        subtasks.put(id, subtask);
    }

    public void updateTask(Task task) {
        if(!tasks.containsKey(task.id)) {
            System.out.println("Task с id - "+task.id+" не найдено");
            return;
        }
        tasks.get(task.id).name = task.name;
        tasks.get(task.id).taskDescription = task.taskDescription;
        tasks.get(task.id).status = task.status;
    }

    public void updateEpic(Epic epic) {
        int sumNEW = 0;
        int sumIN_PROGRESS = 0;
        int sumDONE = 0;

        if(!epics.containsKey(epic.id)) {
            System.out.println("Epic с id - "+epic.id+" не найдено");
            return;
        }
        epics.get(epic.id).name = epic.name;
        epics.get(epic.id).taskDescription = epic.taskDescription;

        if (!epics.get(epic.id).getSubtaskId().isEmpty()) {
            for (int idSub : epics.get(epic.id).getSubtaskId()) {
                if(subtasks.get(idSub).status.equals("NEW")) {
                    sumNEW++;
                }else if (subtasks.get(idSub).status.equals("IN_PROGRESS")) {
                    sumIN_PROGRESS++;
                }else {
                    sumDONE++;
                }
            }
            if (sumDONE == epics.get(epic.id).getSubtaskId().size()) {
                epics.get(epic.id).status = "DONE";
            }else if (sumNEW == epics.get(epic.id).getSubtaskId().size()) {
                epics.get(epic.id).status = "NEW";
            }else {
                epics.get(epic.id).status = "IN_PROGRESS";
            }
        } else {
            epics.get(epic.id).status = "NEW";
        }
    }

    public void updateSubtask(Subtask subtask) {
        int sumNEW = 0;
        int sumDONE = 0;

        if(!subtasks.containsKey(subtask.id)) {
            System.out.println("Subtask с id - " + subtask.id + " не найдено");
            return;
        }
        subtasks.get(subtask.id).name = subtask.name;
        subtasks.get(subtask.id).taskDescription = subtask.taskDescription;
        subtasks.get(subtask.id).status = subtask.status;

        for (int idSub : epics.get(subtasks.get(subtask.id).getEpicId()).getSubtaskId()) {
            if (subtasks.get(idSub).status.equals("NEW")) {
                sumNEW++;
            } else if (subtasks.get(idSub).status.equals("DONE")) {
                sumDONE++;
            }
        }

        if (sumDONE == epics.get(subtasks.get(subtask.id).getEpicId()).getSubtaskId().size()) {
            epics.get(subtasks.get(subtask.id).getEpicId()).status = "DONE";
        } else if (sumNEW == epics.get(subtasks.get(subtask.id).getEpicId()).getSubtaskId().size()) {
            epics.get(subtasks.get(subtask.id).getEpicId()).status = "NEW";
        } else {
            epics.get(subtasks.get(subtask.id).getEpicId()).status = "IN_PROGRESS";
        }
    }

    public void deleteTaskById(int id) {
        if(!tasks.containsKey(id)) {
            System.out.println("Task с id - " + id + " не найдено");
            return;
        }
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {

        if(!epics.containsKey(id)) {
            System.out.println("Epic с id - "+id+" не найдено");
            return;
        }
        for (int idSub : epics.get(id).getSubtaskId()) {
            subtasks.remove(idSub);
        }
        epics.remove(id);
    }

    public void deleteSubtaskById(int id) {
        if(!subtasks.containsKey(id)) {
            System.out.println("Subtask с id - "+id+" не найдено");
            return;
        }
        int idSub = epics.get(subtasks.get(id).getEpicId()).getSubtaskId().indexOf(id);
        epics.get(subtasks.get(id).getEpicId()).getSubtaskId().remove(idSub);
        subtasks.remove(id);
    }

    public void getListSubtaskByEpicId(int id) {
        if(!epics.containsKey(id)) {
            System.out.println("Epic с id - "+id+" не найдено");
            return;
        }
        for (int idSub : epics.get(id).getSubtaskId()) {
            System.out.println(subtasks.get(idSub).name);;
        }
    }
}
