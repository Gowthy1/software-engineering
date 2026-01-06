package Patterns.Singleton;

public class LazyInitialization {
    private static LazyInitialization instance;

    private LazyInitialization() {
    }

    public static LazyInitialization getInstance() {
        if (instance == null) {
            System.out.println("Creating instance... ");
            instance = new LazyInitialization();
        }
        return instance;
    }
}
