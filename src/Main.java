import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(1000);
        for (int i = 0; i < 1000; i++) {
            executor.execute(new RobotThread());
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        int maxFreq = Collections.max(sizeToFreq.values());
        System.out.println("Самое частое количество повторений " + maxFreq);
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (entry.getValue() == maxFreq) {
                System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
            } else {
                System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
            }
        }
    }

    static class RobotThread implements Runnable {
        @Override
        public void run() {
            String route = generateRoute("RLRFR", 100);
            int count = countLetter(route, 'R');
            synchronized (sizeToFreq) {
                sizeToFreq.put(count, sizeToFreq.getOrDefault(count, 0) + 1);
            }
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int countLetter(String str, char letter) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == letter) {
                count++;
            }
        }
        return count;
    }
}