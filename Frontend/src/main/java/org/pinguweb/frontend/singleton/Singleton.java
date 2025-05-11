package org.pinguweb.frontend.singleton;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Getter
@Setter
public final class Singleton<T> {

    private static final ConcurrentMap<Class<?>, Singleton<?>> INSTANCES =
            new ConcurrentHashMap<>();

    private T value;

    private Singleton() { }

    @SuppressWarnings("unchecked")
    public static <T> Singleton<T> getInstance(Class<T> clazz) {
        // computeIfAbsent es atómico y seguro en concurrencia
        return (Singleton<T>) INSTANCES.computeIfAbsent(
                clazz,
                key -> new Singleton<T>()
        );
    }
}