package multithreading.matchCountingHomeWork;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Anna on 14.09.2016.
 */
public class MyMatchCounter implements Callable<Integer> {

    public static final int COUNT_CONSUMER_THREADS = 10;
    private File directory;
    private String keyword;
    private AtomicInteger count;

    private BlockingQueue<File> filesBuffer;
    private ExecutorService producerThreadPool;
    private ExecutorService consumerThreadPool;


    public MyMatchCounter(File root, String keyword) {
        this.directory = root;
        this.keyword = keyword;

        count = new AtomicInteger();

        filesBuffer = new LinkedBlockingQueue<>(10);
        producerThreadPool = Executors.newCachedThreadPool();
        consumerThreadPool = Executors.newFixedThreadPool(COUNT_CONSUMER_THREADS);

    }

    @Override
    public Integer call() {

        if (directory == null) {
            return 0;
        }

        //Future producerTask = producerThreadPool.submit(new FindFilesAndAddToQueueThread(directory));

        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < COUNT_CONSUMER_THREADS; i++) {
            consumerThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        File file = filesBuffer.poll();

                        if (search(file)) {
                            count.incrementAndGet();
                            System.out.printf("POSITIVE, file %s, THREAD: %s\n",
                                    file.getName(), Thread.currentThread().getName());
                        } else {
                            System.out.printf("NEGATIVE, file %s, THREAD: %s\n",
                                    file.getName(), Thread.currentThread().getName());
                        }
                    }
                }
            });

        }

/*        while (!producerTask.isDone()) {
            try {
                producerThreadPool.awaitTermination(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        producerThreadPool.shutdown();
        System.out.println("create signal for shutdowns producers, THREAD: "
                + Thread.currentThread().getName());

        while (!filesBuffer.isEmpty()){
        }
        consumerThreadPool.shutdown();
        System.out.println("create signal for shutdowns consumers, THREAD: "
                + Thread.currentThread().getName());


        while (!consumerThreadPool.isTerminated() || !producerThreadPool.isTerminated()) {
            try {
                consumerThreadPool.awaitTermination(500, TimeUnit.MILLISECONDS);
                producerThreadPool.awaitTermination(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return count.intValue();
    }

    public boolean search(File file) {
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