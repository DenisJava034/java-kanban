package tasks;

import java.time.LocalDateTime;

public class Task {
    protected int id;
    protected String name;
    protected String taskDescription;
    protected TaskStatus status;
    protected LocalDateTime  startTime;
    protected int  duration;


    public Task(int id, String name, String taskDescription, TaskStatus status, LocalDateTime startTime, int duration) {
        this.id = id;
        this.name = name;
        this.taskDescription = taskDescription;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String taskDescription, TaskStatus status, LocalDateTime startTime, int duration) {
        this.name = name;
        this.taskDescription = taskDescription;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, String name, String taskDescription, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.taskDescription = taskDescription;
        this.status = status;
    }

    public Task(String name, String taskDescription, TaskStatus status) {
        this.name = name;
        this.taskDescription = taskDescription;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setNameTask(String nameTask) {
        this.name = nameTask;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s%n", getId(),TypeTasks.TASK, getName(), getStatus(),
                getTaskDescription(), getStartTime(), getDuration());
    }


    public LocalDateTime getEndTime() {
        LocalDateTime andDateTime = null;
        if(startTime != null){
            andDateTime = startTime.plusMinutes(duration);
        }
        return andDateTime;
    }
}
