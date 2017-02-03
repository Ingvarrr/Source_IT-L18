import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by vig on 12/2/16.
 */

// создание пула для выполнение заданий 10 заданий под каждый новый поток
public class Task implements Runnable {

    Task targetToDestroy = null;
    volatile boolean flag = false;

    public Task(Task targetToDestroy) {
        this.targetToDestroy = targetToDestroy;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("This is " +
                    Thread.currentThread().getName());
            safeSleep(1000);
            if (i == 2 && targetToDestroy != null) {
                System.out.println(this
                        + " interrupting " + targetToDestroy);
                targetToDestroy.flag = true;
            }
            if (this.flag) {
                System.out.println("Interrupted "
                        + Thread.currentThread().getName());
                break;
            }
        }
    }

    private static void safeSleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() +
                    e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExecutorService pool
                = Executors.newFixedThreadPool(10);
        safeSleep(20000);
        for(int i = 0; i<Integer.MAX_VALUE; i++)
            new Thread(new Task(null)).start();

        pool.shutdown();
    }
}

//

/*public class Task implements Runnable {

    Task targetToDestroy = null;
    volatile boolean flag = false;

    public Task(Task targetToDestroy) {
        this.targetToDestroy = targetToDestroy;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1; i++) {
            System.out.println("This is " +
                    Thread.currentThread().getName());
            if (i == 2 && targetToDestroy != null) {
                System.out.println(this
                        + " interrupting " + targetToDestroy);
                targetToDestroy.flag = true;
            }
            if (this.flag) {
                System.out.println("Interrupted "
                        + Thread.currentThread().getName());
                break;
            }
        }
    }

    private static void safeSleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() +
                    e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExecutorService pool
                = Executors.newFixedThreadPool(10);
        for(int i = 0; i<Integer.MAX_VALUE; i++)
        pool.submit(new Task(null));
        pool.shutdown();
    }
}*/

//yield - используют для ассинхронных потоков