import Patterns.Singleton.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
//        EagerInitialization eagerInitialization = EagerInitialization.getInstance();
//        multithreadLazyInitialization();
        testMultithreadedSingleton(EagerInitialization.class.getName(), EagerInitialization::getInstance);
        testMultithreadedSingleton(LazyInitialization.class.getName(), LazyInitialization::getInstance);
        testMultithreadedSingleton(SynchronizedInitialization.class.getName(), SynchronizedInitialization::getInstance);
        testMultithreadedSingleton(OptimizedSynchronizedInitialization.class.getName(), () -> OptimizedSynchronizedInitialization.getInstance());
    }

    private static <T> void testMultithreadedSingleton(String singletonName, Supplier<T> getInstance) {
        final int threadCount = 5;
        final List<T> instances = Collections.synchronizedList(new ArrayList<>());
        final CountDownLatch latch = new CountDownLatch(threadCount);

        try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {

            System.out.println("\n***********************************************");
            System.out.println("Testing multithreaded Singleton (" + singletonName + ") with " + threadCount + " threads...");
            System.out.println("==========================================");

            for (int i = 0; i < threadCount; i++) {
                final int threadId = i + 1;
                executor.submit(() -> {
                    try {
                        T instance = getInstance.get();
                        instances.add(instance);
                        System.out.println("Thread " + threadId + " got instance: " + instance.hashCode());
                    } finally {
                        latch.countDown();
                    }
                });
            }

            try {
                // Wait for all threads to complete
                latch.await(5, TimeUnit.SECONDS);
                executor.shutdown();

                System.out.println("==========================================");
                System.out.println("Total instances created: " + instances.size());

                // Check if all instances are the same
                T firstInstance = instances.get(0);
                boolean allSame = instances.stream()
                        .allMatch(instance -> instance == firstInstance);

                if (allSame) {
                    System.out.println("✓ SUCCESS: All threads got the same instance (Singleton pattern works)");
                    System.out.println("Instance hash code: " + firstInstance.hashCode());
                } else {
                    System.out.println("✗ FAILURE: Multiple instances were created (Singleton pattern broken!)");
                    System.out.println("Unique instances found: " + instances.stream().distinct().count());
                    System.out.println("Instance hash codes:");
                    instances.stream()
                            .distinct()
                            .forEach(instance -> System.out.println("  - " + instance.hashCode()));
                }
                System.out.println("***********************************************\n");
            } catch (InterruptedException e) {
                System.err.println("Test interrupted: " + e.getMessage());
                executor.shutdownNow();
            }
        }
    }


    private static void multithreadLazyInitialization() {
        final int threadCount = 10;
        final List<LazyInitialization> instances = Collections.synchronizedList(new ArrayList<>());
        final CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        System.out.println("Testing multithreaded Singleton with " + threadCount + " threads...");
        System.out.println("==========================================");

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i + 1;
            executor.submit(() -> {
                try {
                    LazyInitialization instance = LazyInitialization.getInstance();
                    instances.add(instance);
                    System.out.println("Thread " + threadId + " got instance: " + instance.hashCode());
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            // Wait for all threads to complete
            latch.await(5, TimeUnit.SECONDS);
            executor.shutdown();

            System.out.println("==========================================");
            System.out.println("Total instances created: " + instances.size());

            // Check if all instances are the same
            LazyInitialization firstInstance = instances.get(0);
            boolean allSame = instances.stream()
                    .allMatch(instance -> instance == firstInstance);

            if (allSame) {
                System.out.println("✓ SUCCESS: All threads got the same instance (Singleton pattern works)");
                System.out.println("Instance hash code: " + firstInstance.hashCode());
            } else {
                System.out.println("✗ FAILURE: Multiple instances were created (Singleton pattern broken!)");
                System.out.println("Unique instances found: " + instances.stream().distinct().count());
                System.out.println("Instance hash codes:");
                instances.stream()
                        .distinct()
                        .forEach(instance -> System.out.println("  - " + instance.hashCode()));
            }
        } catch (InterruptedException e) {
            System.err.println("Test interrupted: " + e.getMessage());
            executor.shutdownNow();
        }
    }
}