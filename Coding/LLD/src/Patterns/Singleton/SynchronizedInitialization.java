package Patterns.Singleton;

public class SynchronizedInitialization {
    private static SynchronizedInitialization instance;

    private SynchronizedInitialization() {}

    public static synchronized SynchronizedInitialization getInstance() {
        if (instance == null) {
            instance = new SynchronizedInitialization();
        }
        return instance;
    }
}
