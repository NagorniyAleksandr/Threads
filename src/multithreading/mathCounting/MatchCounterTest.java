package multithreading.mathCounting;

import java.io.File;
import java.util.Scanner;

/**
 * Created by Anna on 14.09.2016.
 */
public class MatchCounterTest {

    public static void main(String[] args) {
/*        Scanner in = new Scanner(System.in);
        System.out.print("Enter base directory:");
        String directory = in.nextLine();
        System.out.print("Enter keyword:");
        String keyword = in.nextLine();*/

        long starTime = System.currentTimeMillis();

        String directory = "D:\\Java\\ACO15\\src\\ua\\";
        String keyword = "java";
        
        MatchCounter matchCounter = new MatchCounter(new File(directory), keyword);
        System.out.println(matchCounter.find() + " matching files.");

        long endTime = System.currentTimeMillis();

        System.out.println("Time: " + (endTime - starTime));

    }
}
