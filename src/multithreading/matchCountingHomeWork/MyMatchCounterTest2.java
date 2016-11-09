package multithreading.matchCountingHomeWork;

import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by aleksandrnagorniy on 09.11.16.
 */
public class MyMatchCounterTest2 {

    public static final int QUEUE_LENGTH = 20;
    private static final int COUNT_CONSUMER_THREADS = 10;

    public static void main(String[] args) {

        long starTime = System.currentTimeMillis();

        String directory = "/Users/aleksandrnagorniy/Documents/Java/IdeaProjects/";
        String keyword = "java";

        AtomicInteger count = new AtomicInteger();

        BlockingQueue<File> filesBuffer = new LinkedBlockingQueue<>(QUEUE_LENGTH);
        ExecutorService producerThreadPool = Executors.newCachedThreadPool();

        //start finding all files
        System.out.println("FIRST task for PRODUCER, Thread: " + Thread.currentThread().getName());
        Future findFilesTask = producerThreadPool.submit(
                new FindFilesRecursiveThread(
                        new File(directory), filesBuffer, producerThreadPool), null);



        ExecutorService consumerThreadPool = Executors.newFixedThreadPool(COUNT_CONSUMER_THREADS);

        //start analyse file content
        while (!findFilesTask.isDone() || !filesBuffer.isEmpty()) {
            System.out.println("NEW task for CONSUMERS, Thread: " + Thread.currentThread().getName());

            Future analyseContentTask = consumerThreadPool.submit(new AnalyseContentOfFilesThread(keyword, filesBuffer));

            try {

                if ((boolean)analyseContentTask.get()) count.incrementAndGet();
                System.out.println("DONE task for CONSUMERS, Thread: " + Thread.currentThread().getName());

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.println("SHUTDOWN producers, THREAD: " + Thread.currentThread().getName());
        producerThreadPool.shutdown();

        System.out.println("SHUTDOWN consumers, THREAD: " + Thread.currentThread().getName());
        consumerThreadPool.shutdown();


        waitForTerminationExecutorServices(producerThreadPool, consumerThreadPool);

        System.out.println(count.intValue() + " matching files.");

        long endTime = System.currentTimeMillis();

        System.out.println("Time: " + (endTime - starTime));



    }

    private static void waitForTerminationExecutorServices(ExecutorService producerThreadPool, ExecutorService consumerThreadPool) {
        while (!consumerThreadPool.isTerminated() || !producerThreadPool.isTerminated()) {
            try {
                consumerThreadPool.awaitTermination(500, TimeUnit.MILLISECONDS);
                producerThreadPool.awaitTermination(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
