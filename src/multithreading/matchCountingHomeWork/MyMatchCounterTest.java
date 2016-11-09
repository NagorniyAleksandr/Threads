package multithreading.matchCountingHomeWork;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by Anna on 14.09.2016.
 */
public class MyMatchCounterTest {

    public static void main(String[] args) {
/*        Scanner in = new Scanner(System.in);
        System.out.print("Enter base directory:");
        String directory = in.nextLine();
        System.out.print("Enter keyword:");
        String keyword = in.nextLine();*/

        long starTime = System.currentTimeMillis();

        String directory = "D:\\Java\\ACO15\\src\\ua\\";
        String keyword = "java";

        FutureTask ticket = new FutureTask(new MyMatchCounter(new File(directory), keyword));

        Thread thread = new Thread(ticket);
        thread.start();

        try {
            System.out.println(ticket.get() + " matching files.");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Time: " + (endTime - starTime));

    }
}
