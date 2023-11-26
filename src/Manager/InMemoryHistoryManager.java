package Manager;
import Tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node> mapNode = new HashMap<>();
    private Node head;
    private Node tail;

    public void LinkLast (Task task) {
        Node newNode = new Node(task, tail, null);
        removeNode(task.getId());
        if (mapNode.isEmpty()){
            head = newNode;
            tail = newNode;
            mapNode.put(task.getId(), newNode);
        } else {
            mapNode.put(task.getId(), newNode);
            tail = newNode;
            mapNode.get(task.getId()).prev.next = newNode;
            if (head.next == null){
                head.next = newNode;
            }
        }
    }

    public ArrayList<Task> getTask() {
        ArrayList<Task> taskList = new ArrayList<>();
        boolean isNode = !mapNode.isEmpty();
        Node hubNode = null;
        while (isNode){
            if (taskList.isEmpty()) {
                taskList.add(head.task);
                hubNode = head.next;
            }else {
                if (hubNode.next == null) {
                    taskList.add(hubNode.task);
                    break;
                }
                taskList.add(hubNode.task);
                hubNode = hubNode.next;
            }
        }
        return taskList;
    }

    public void removeNode(int id) {

        Node node = mapNode.remove(id);
        if (node == null) {
            return;
        } else if(node.prev == null && node.next == null) {
            head = null;
            tail = null;
        } else if(node.prev == null) {
            head = node.next;
            node.next.prev = null;
        } else if (node.next == null){
            tail = node.prev;
            node.prev.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    @Override
    public void addTaskOfListHistory(Task task) {
        LinkLast(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> copyHistory =  getTask();
        return copyHistory;
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }
}
