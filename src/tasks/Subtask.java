package tasks;

import java.time.LocalDateTime;

public class Subtask extends Task {
    protected int epicId;



    public Subtask(int id, String name, String taskDescription, TaskStatus status, int epicId, LocalDateTime startTime, int duration) {
        super(id, name, taskDescription, status, startTime, duration);
        this.epicId = epicId;
    }


    public Subtask(String nameTask, String taskDescription, TaskStatus status, int epicId, LocalDateTime startTime, int duration) {
        super(nameTask, taskDescription, status, startTime, duration);
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
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s%n", getId(),TypeTasks.SUBTASK, getName(), getStatus(),
                getTaskDescription(), getEpicId(), getStartTime(), getDuration());
    }
}
