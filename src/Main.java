import manager.Managers;
import manager.TaskManager;

public class Main {

    public static void main(String[] args){
        System.out.println("Поехали!");

        TaskManager manager = Managers.getDefault();
    }
}
