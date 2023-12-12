package tasks;

import java.util.ArrayList;

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
        return String.format("%s,%s,%s,%s,%s%n", getId(),TypeTasks.EPIC, getName(), getStatus(), getTaskDescription());
    }
}
