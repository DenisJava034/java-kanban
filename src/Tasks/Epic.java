package Tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
  protected ArrayList <Integer> subtaskId = new ArrayList<>();

    public Epic(int id, String name, String taskDescription, TaskStatus status) {
        super(id, name, taskDescription, status);
    }

    public Epic(String name, String taskDescription, TaskStatus status) {
        super(name, taskDescription, status);
    }

    public ArrayList<Integer> getSubtaskId() {
        ArrayList<Integer> IdSubtask = subtaskId;
        return IdSubtask;
    }

    public void setSubtaskId(ArrayList<Integer> subtaskId) {
        this.subtaskId = subtaskId;
    }

    @Override
    public String toString() {
        return "Tasks.Epic{" +
                "id=" + id +
                ", nameTask='" + name + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
