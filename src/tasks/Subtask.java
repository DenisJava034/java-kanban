package tasks;

public class Subtask extends Task {
    protected int epicId;

    public Subtask(int id, String name, String taskDescription, TaskStatus status, int epicId) {
        super(id, name, taskDescription, status);
        this.epicId = epicId;
    }


    public Subtask(String nameTask, String taskDescription, TaskStatus status, int epicId) {
        super(nameTask, taskDescription, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s%n", getId(),TypeTasks.SUBTASK, getName(), getStatus(), getTaskDescription(), getEpicId());

    }
}
