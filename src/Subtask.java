public class Subtask extends Task {
    private int epicId;

    public Subtask(int id, String name, String taskDescription, String status) {
        super(id, name, taskDescription, status);
    }

    public Subtask(String nameTask, String taskDescription, String status, int epicId) {
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
        return "Subtask{" +
                "id=" + id +
                ", nameTask='" + name + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
