import Manager.Managers;
import Manager.TaskManager;

public class Main {

    public static void main(String[] args){
        System.out.println("Поехали!");

        TaskManager manager = Managers.getDefault();
    }
}
