package Tasks;

public class Task {
    protected int id;
    protected String name;
    protected String taskDescription;
    protected TaskStatus status;

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

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s%n", getId(),TypeTasks.TASK, getName(), getStatus(), getTaskDescription());
    }

    public enum TapeTasks {
    }
}
