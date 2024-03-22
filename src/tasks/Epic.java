package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskId = new ArrayList<>();

    protected LocalDateTime endTime;

    public Epic(int id, String name, String taskDescription, TaskStatus status) {
        super(id, name, taskDescription, status);
    }

    public Epic(String name, String taskDescription, TaskStatus status) { // Для создания
        super(name, taskDescription, status);
    }

    public Epic(int id, String name, String taskDescription, TaskStatus status, LocalDateTime startTime, int duration) {
        super(id, name, taskDescription, status, startTime, duration);
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(ArrayList<Integer> subtaskId) {
        this.subtaskId = subtaskId;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s%n", getId(), TypeTasks.EPIC, getName(), getStatus(), getTaskDescription(),
                getStartTime(), getDuration());
    }
}
