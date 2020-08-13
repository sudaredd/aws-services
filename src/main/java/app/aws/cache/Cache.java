package app.aws.cache;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Cache<K, V> {

    private ConcurrentMap<CacheKey, V> cacheMap;
    private ScheduledExecutorService sch;
    private BiConsumer<K, V> consumer;
    private volatile int cachingTime;

    private final Predicate<CacheKey> timePredicate = k -> SECONDS.between(k.getTime(), LocalDateTime.now()) > cachingTime;

    public Cache(BiConsumer<K, V> consumer, int cachingTime) {
        cacheMap = new ConcurrentHashMap<>();
        sch = Executors.newScheduledThreadPool(1);
        this.cachingTime = cachingTime;
        this.consumer = consumer;
        init();
    }

    private void init() {

        sch.scheduleWithFixedDelay(() -> cleanUp(), 0, 2, TimeUnit.SECONDS);
    }

    private void cleanUp() {
        for (Iterator iterator = cacheMap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<CacheKey<K>, V> entry = (Map.Entry<CacheKey<K>, V>) iterator.next();
            if (timePredicate.test(entry.getKey())) {
                consumer.accept(entry.getKey().getK(), entry.getValue());
                iterator.remove();
            }
        }
    }

    public V put(K key, V val) {
        CacheKey cacheKey = new CacheKey(key, LocalDateTime.now());
        return cacheMap.put(cacheKey, val);
    }

    public V get(K key) {
        return cacheMap.get(new CacheKey(key));
    }

    public boolean isEmpty() {
        return cacheMap.isEmpty();
    }

    public V remove(K key) {
        return cacheMap.remove(new CacheKey(key));
    }

    @Override
    public String toString() {
        return "Cache{" +
                "cacheMap=" + cacheMap +
                '}';
    }
}

class CacheKey<K> {

    private K key;
    private LocalDateTime time;

    public CacheKey(K key) {
        this.key = key;
    }

    public CacheKey(K key, LocalDateTime time) {
        this.key = key;
        this.time = time;
    }

    public K getK() {
        return key;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheKey<?> cacheKey = (CacheKey<?>) o;
        return Objects.equals(key, cacheKey.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "CacheKey{" +
                "key=" + key +
                ", time=" + time +
                '}';
    }
}