import Manager.Manager;
import Tasks.Epic;
import Tasks.Subtask;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        Manager manager = new Manager();

        Epic epic = new Epic("LOOOK","JKJKJ","NEW");

        Subtask subtask = new Subtask("JIJIJ", "jjjij", "NEW", 1);


        manager.createEpic(epic);
        manager.createSubtask(subtask);

        manager.deleteSubtaskById(2);

        System.out.println(manager.getListSubtaskByEpicId(1));
    }
}
