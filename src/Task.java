public class Task {
    protected int id;
    protected String name;
    protected String taskDescription;
    protected String status;

    public Task(int id, String name, String taskDescription, String status) {
        this.id = id;
        this.name = name;
        this.taskDescription = taskDescription;
        this.status = status;
    }

    public Task(String nameTask, String taskDescription, String status) {
        this.name = nameTask;
        this.taskDescription = taskDescription;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameTask() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", nameTask='" + name + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
