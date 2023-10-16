import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList <Integer> subtaskId = new ArrayList<>();

    public Epic(int id, String name, String taskDescription, String status) {
        super(id, name, taskDescription, status);
    }

    public Epic(String nameTask, String taskDescription, String status) {
        super(nameTask, taskDescription, status);
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(ArrayList<Integer> subtaskId) {
        this.subtaskId = subtaskId;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", nameTask='" + name + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
