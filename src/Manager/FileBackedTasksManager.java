package Manager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Tasks.TaskStatus;

import java.io.*;
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
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (history.size() <= 1) {
            return fileBackedTasksManager;
        }

        if (!(history.get(history.size() - 2).isBlank())) {
            stringDelete = 1;
            isHistory = false;
        }

        for (int i = 1; i <= history.size() - stringDelete; i++) {
            Task newTack = fromString(history.get(i));

            if (fileBackedTasksManager.id < newTack.getId()) {
                fileBackedTasksManager.id = newTack.getId();
            }

            if (newTack instanceof Epic) {
                fileBackedTasksManager.epics.put(newTack.getId(), (Epic) newTack);

            } else if (newTack instanceof Subtask) {
                fileBackedTasksManager.subtasks.put(newTack.getId(), (Subtask) newTack);
                ArrayList<Integer> num = fileBackedTasksManager.epics.get(((Subtask) newTack).getEpicId())
                        .getSubtaskId();
                num.add(newTack.getId());
                fileBackedTasksManager.epics.get(((Subtask) newTack).getEpicId()).setSubtaskId(num);

            } else {
                fileBackedTasksManager.tasks.put(newTack.getId(), newTack);
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
            Task task = new Task(Integer.parseInt(str[0]), str[2], str[4], TaskStatus.valueOf(str[3]));
            return task;
        } else if (str[1].equals("EPIC")) {
            Epic epic = new Epic(Integer.parseInt(str[0]), str[2], str[4], TaskStatus.valueOf(str[3]));
            return epic;
        } else {
            Subtask subtask = new Subtask(Integer.parseInt(str[0]), str[2], str[4], TaskStatus.valueOf(str[3]),
                    Integer.parseInt(str[5]));
            return subtask;
        }
    }

    public void save() {
        cleanFile(file);

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

    private void cleanFile(File file) {
        try (Writer fileClean = new FileWriter(file, false)) {
            fileClean.write("id,type,name,status,description,epic\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString(Task task) {
        return task.toString();
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> idInt = new ArrayList<>();
        String[] str = value.split(",");

        for (int i = 0; i < str.length; i++) {
            idInt.add(Integer.parseInt(str[i]));
        }
        return idInt;
    }

    static String historyToString(HistoryManager manager) {
        StringBuilder builder = new StringBuilder();

        for (Task task : manager.getHistory()) {
            builder.append(task.getId() + ",");
        }

        return builder.toString();
    }

    @Override
    public ArrayList<Task> getListOfTasks() {
        return super.getListOfTasks();
    }

    @Override
    public ArrayList<Epic> getListOfEpics() {
        return super.getListOfEpics();
    }

    @Override
    public ArrayList<Subtask> getListOfSubTasks() {
        return super.getListOfSubTasks();

    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        save();
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
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public ArrayList<Subtask> getListSubtaskByEpicId(int id) {
        return super.getListSubtaskByEpicId(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    protected void updateStatusEpic(int epicId) {
        super.updateStatusEpic(epicId);
        save();
    }
}
