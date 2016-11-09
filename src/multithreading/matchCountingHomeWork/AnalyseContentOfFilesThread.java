package multithreading.matchCountingHomeWork;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/**
 * Created by aleksandrnagorniy on 09.11.16.
 */
public class AnalyseContentOfFilesThread implements Callable<Boolean> {


    private String keyword;
    private BlockingQueue<File> taskQueue;

    public AnalyseContentOfFilesThread(String keyword, BlockingQueue<File> taskQueue) {
        this.keyword = keyword;
        this.taskQueue = taskQueue;
    }

    @Override
    public Boolean call() {
            File file = taskQueue.poll();

            if (isKeywordInFile(file)) {
                System.out.printf("POSITIVE, file %s, THREAD: %s\n",
                        file.getName(), Thread.currentThread().getName());

                return true;
            } else {
                System.out.printf("NEGATIVE, file %s, THREAD: %s\n",
                        file.getName(), Thread.currentThread().getName());

                return false;
            }
    }

    private boolean isKeywordInFile(File file) {
        if (file == null) return false;

        try (Scanner in = new Scanner(file)) {
            boolean found = false;
            while (!found && in.hasNextLine()) {
                String line = in.nextLine();
                if (line.contains(keyword)) {
                    return true;
                }
            }
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
