package Patterns.Singleton;

/**
 * Static Inner Class (Bill Pugh pattern)
 */
public class StaticInnerClass {
    private StaticInnerClass() {
    }

    private static class SingletonHolder {
        private static final StaticInnerClass INSTANCE = new StaticInnerClass();
    }

    public static StaticInnerClass getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
