package Patterns.Singleton;

/**
 * Double-Checked Locking (DCL) + volatile
 */
public class OptimizedSynchronizedInitialization {
//    private static OptimizedSynchronizedInitialization instance;

    // Multithreading problems:
    // 1. Visibility - storing variables in thread local memory
    // 2. Re-ordering - for cpu/ram optimization
    //
    //  Volatile solves:
    //  ✔ Visibility → changes seen across threads
    //  ✔ Reordering (for that variable) → No instruction reordering for that variable
    //
    //  Volatile does NOT:
    //  ❌ Provide atomicity
    //  ❌ Replace synchronization
    //
    // So now:
    // Thread B can NEVER see a partially constructed object
    //
    // 📌 Staff-level one-liner:
    //  - volatile forces reads/writes to go through main memory, not thread-local caches.
    //  - volatile prevents reordering of object publication.
    private static volatile OptimizedSynchronizedInitialization instance;

    private OptimizedSynchronizedInitialization() {
    }

    public static OptimizedSynchronizedInitialization getInstance() {
        if (instance == null) {
            synchronized (OptimizedSynchronizedInitialization.class) {
                if (instance == null) {
                    instance = new OptimizedSynchronizedInitialization();
                }
            }
        }
        return instance;
    }
}
