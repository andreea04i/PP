import java.util.*;
import java.util.concurrent.*;

public class Pipeline {

    private static final int[] V = {5, 2, 9, 1, 3};
    private static final int ALPHA = 3;

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<int[]> queue1 = new ArrayBlockingQueue<>(1);
        BlockingQueue<int[]> queue2 = new ArrayBlockingQueue<>(1);

        Thread t1 = new Thread(() -> {
            int[] result = Arrays.stream(V)
                    .map(x -> x * ALPHA)
                    .toArray();
            try {
                queue1.put(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                int[] data = queue1.take();
                Arrays.sort(data);
                queue2.put(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        Thread t3 = new Thread(() -> {
            try {
                int[] data = queue2.take();
                System.out.println("Rezultat final: " + Arrays.toString(data));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
    }
}
