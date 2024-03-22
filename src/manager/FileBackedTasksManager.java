package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        int stringDelete = 3;
        boolean isHistory = true;
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        ArrayList<String> history = new ArrayList<>();

        try (Reader fileReader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(fileReader);

            while (br.ready()) {
                String line = br.readLine();
                history.add(line);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка " + e.getMessage());
        }

        if (history.size() <= 1) { // если файл пустой или есть только заголовок, isEmpty() проверяет только пустоту.
            return fileBackedTasksManager;
        }

        if (!history.get(history.size() - 2).isBlank()) {
            stringDelete = 1;
            isHistory = false;
        }

        for (int i = 1; i <= history.size() - stringDelete; i++) {
            Task newTask = fromString(history.get(i));

            if (fileBackedTasksManager.id < newTask.getId()) {
                fileBackedTasksManager.id = newTask.getId();
            }

            if (newTask instanceof Epic) {
                fileBackedTasksManager.epics.put(newTask.getId(), (Epic) newTask);


            } else if (newTask instanceof Subtask) {
                fileBackedTasksManager.subtasks.put(newTask.getId(), (Subtask) newTask);
                ArrayList<Integer> num = fileBackedTasksManager.epics.get(((Subtask) newTask).getEpicId())
                        .getSubtaskId();
                num.add(newTask.getId());
                fileBackedTasksManager.epics.get(((Subtask) newTask).getEpicId()).setSubtaskId(num);

                fileBackedTasksManager.startTimeSet.add((Subtask) newTask);
                fileBackedTasksManager.startTimeEndTimeEpic(((Subtask) newTask).getEpicId());

            } else {
                fileBackedTasksManager.tasks.put(newTask.getId(), newTask);
                fileBackedTasksManager.startTimeSet.add(newTask);
            }
        }
        if (isHistory) {
            List<Integer> idInt = historyFromString(history.get(history.size() - 1));

            for (Integer i : idInt) {
                if (fileBackedTasksManager.tasks.containsKey(i)) {
                    fileBackedTasksManager.historyManager.addTaskOfListHistory(fileBackedTasksManager.tasks.get(i));
                } else if (fileBackedTasksManager.epics.containsKey(i)) {
                    fileBackedTasksManager.historyManager.addTaskOfListHistory(fileBackedTasksManager.epics.get(i));
                } else {
                    fileBackedTasksManager.historyManager.addTaskOfListHistory(fileBackedTasksManager.subtasks.get(i));
                }
            }
        }
        return fileBackedTasksManager;
    }

    public static Task fromString(String value) {
        String[] str = value.split(",");

        if (str[1].equals("TASK")) {
            if (str[5].equals("null")) {
                LocalDateTime startTime = null;
                return new Task(Integer.parseInt(str[0]), str[2], str[4], TaskStatus.valueOf(str[3]),
                        startTime, Integer.parseInt(str[6]));
            } else {
                return new Task(Integer.parseInt(str[0]), str[2], str[4], TaskStatus.valueOf(str[3]),
                        LocalDateTime.parse(str[5]), Integer.parseInt(str[6]));
            }
        }
        if (str[1].equals("EPIC")) {
            if (str[5].equals("null")) {
                LocalDateTime startTime = null;
                return new Epic(Integer.parseInt(str[0]), str[2], str[4], TaskStatus.valueOf(str[3]),
                        startTime, Integer.parseInt(str[6]));
            } else {
                return new Epic(Integer.parseInt(str[0]), str[2], str[4], TaskStatus.valueOf(str[3]),
                        LocalDateTime.parse(str[5]), Integer.parseInt(str[6]));
            }
        }
        if (str[6].equals("null")) {
            LocalDateTime startTime = null;
            return new Subtask(Integer.parseInt(str[0]), str[2], str[4], TaskStatus.valueOf(str[3]),
                    Integer.parseInt(str[5]), startTime, Integer.parseInt(str[7]));
        }
        return new Subtask(Integer.parseInt(str[0]), str[2], str[4], TaskStatus.valueOf(str[3]),
                Integer.parseInt(str[5]), LocalDateTime.parse(str[6]), Integer.parseInt(str[7]));
    }

    private void save() {
        recordHeader(file);

        try (Writer fileWriter = new FileWriter(file, true)) {
            if (!tasks.isEmpty()) {
                for (int id : tasks.keySet()) {
                    fileWriter.write(toString(tasks.get(id)));
                }
            }
            if (!epics.isEmpty()) {
                for (int id : epics.keySet()) {
                    fileWriter.write(toString(epics.get(id)));
                }
            }
            if (!subtasks.isEmpty()) {
                for (int id : subtasks.keySet()) {
                    fileWriter.write(toString(subtasks.get(id)));
                }
            }
            if (!(getHistory().isEmpty())) {
                fileWriter.write(String.format("%n%s", historyToString(historyManager)));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка " + e.getMessage());
        }
    }

    private void recordHeader(File file) {
        try (Writer fileClean = new FileWriter(file, false)) {
            fileClean.write("id,type,name,status,description,epic,start,duration\n");

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка " + e.getMessage());
        }
    }

    private String toString(Task task) {
        return task.toString();
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> idInt = new ArrayList<>();
        String[] str = value.split(",");

        for (int i = 0; i < str.length; i++) {
            idInt.add(Integer.parseInt(str[i]));
        }
        return idInt;
    }

    private static String historyToString(HistoryManager manager) {
        StringBuilder builder = new StringBuilder();

        for (Task task : manager.getHistory()) {
            builder.append(task.getId() + ",");
        }

        return builder.toString();
    }

    @Override
    public Object deleteAllTasks() {
        super.deleteAllTasks();
        save();
        return null;
    }

    @Override
    public Object deleteAllEpic() {
        super.deleteAllEpic();
        save();
        return null;
    }

    @Override
    public Object deleteAllSubtask() {
        super.deleteAllSubtask();
        save();
        return null;
    }

    @Override
    public Task getTaskById(int id) {
        final Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        final Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        final Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Object deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
        return null;
    }

    @Override
    public Object deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
        return null;
    }

    @Override
    public Object deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
        return null;
    }

    @Override
    protected void updateStatusEpic(int epicId) {
        super.updateStatusEpic(epicId);
        save();
    }
}