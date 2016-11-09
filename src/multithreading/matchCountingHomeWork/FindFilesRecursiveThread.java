package multithreading.matchCountingHomeWork;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * Created by aleksandrnagorniy on 09.11.16.
 */
public class FindFilesRecursiveThread implements Runnable {

    private File root;
    private BlockingQueue<File> taskQueue;
    private ExecutorService producerThreadPool;

    public FindFilesRecursiveThread(File root, BlockingQueue<File> outputTaskQueue, ExecutorService producerThreadPoolFactory) {
        this.root = root;
        this.taskQueue = outputTaskQueue;
        this.producerThreadPool = producerThreadPoolFactory;
    }

    @Override
    public void run() {
        if (root == null ||
                root.listFiles() == null ||
                root.listFiles().length == 0) {
            return;
        }

        File[] files = root.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("NEW task for PRODUCER, Thread: " + Thread.currentThread().getName());
                producerThreadPool.submit(new FindFilesRecursiveThread(file, taskQueue, producerThreadPool));
            } else {
                try {
                    taskQueue.put(file);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
