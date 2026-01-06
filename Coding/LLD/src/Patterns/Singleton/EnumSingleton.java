package Patterns.Singleton;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum EnumSingleton {
    INSTANCE;

    // Internal state
    private final Map<String, String> config;

    // Enum constructor (called exactly once by JVM)
    EnumSingleton() {
        Map<String, String> temp = new HashMap<>();
        temp.put("env", "PROD");
        temp.put("timeout", "500");
        temp.put("region", "us-east-1");
        this.config = Collections.synchronizedMap(temp);
    }

    // Read operation
    public String get(String key) {
        return config.get(key);
    }

    // Write operation
    public void put(String key, String value) {
        config.put(key, value);
    }

    // Optional: expose safe snapshot
    public Map<String, String> snapshot() {
        synchronized (config) {
            return Map.copyOf(config);
        }
    }

}
