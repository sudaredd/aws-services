package app.aws.cache;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static java.time.temporal.ChronoUnit.SECONDS;

public class MultiValueCache<K, V> {

    private ConcurrentMap<CacheKey, List<V>> cacheMap;
    private ScheduledExecutorService sch;
    private BiConsumer<K, List<V>> consumer;
    private volatile int cachingTime;
    private volatile int maxSizePerKey;

    private final Predicate<Map.Entry<CacheKey<K>, List<V>>> sizePredicate = (e -> e.getValue().size() >= maxSizePerKey);
    private final Predicate<Map.Entry<CacheKey<K>, List<V>>> timePredicate = (e -> SECONDS.between(e.getKey().getTime(), LocalDateTime.now()) > cachingTime);

    private MultiValueCache() {

    }

    public void init() {
        sch.scheduleWithFixedDelay(() -> cleanUp(), 0, 2, TimeUnit.SECONDS);
    }

    private void cleanUp() {

        Predicate<Map.Entry<CacheKey<K>, List<V>>> timeOrSizeFilter = timePredicate.or(sizePredicate);
        try {
            for (Iterator iterator = cacheMap.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<CacheKey<K>, List<V>> entry = (Map.Entry<CacheKey<K>, List<V>>) iterator.next();
                if (timeOrSizeFilter.test(entry)) {
                    consumer.accept(entry.getKey().getK(), entry.getValue());
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public V put(K key, V val) {
        CacheKey cacheKey = new CacheKey(key, LocalDateTime.now());
        cacheMap.computeIfAbsent(cacheKey, v -> new ArrayList<>()).add(val);
        return val;
    }

    public List<V> get(K key) {
        return cacheMap.get(new CacheKey<>(key));
    }

    public boolean isEmpty() {
        return cacheMap.isEmpty();
    }

    public List<V> remove(K key) {
        return cacheMap.remove(new CacheKey<>(key));
    }

    @Override
    public String toString() {
        return "Cache{" +
                "cacheMap=" + cacheMap +
                '}';
    }

    public static final class MultiValueCacheBuilder<K, V> {
        private ConcurrentMap<CacheKey, List<V>> cacheMap = new ConcurrentHashMap<>();
        private ScheduledExecutorService sch = Executors.newScheduledThreadPool(1);
        private BiConsumer<K, V> consumer;
        private int cachingTime = Integer.MAX_VALUE;
        private int maxSizePerKey = Integer.MAX_VALUE;

        private MultiValueCacheBuilder() {

        }

        public static MultiValueCacheBuilder aMultiValueCache() {
            return new MultiValueCacheBuilder();
        }

        public MultiValueCacheBuilder withConsumer(BiConsumer<K, V> consumer) {
            this.consumer = consumer;
            return this;
        }

        public MultiValueCacheBuilder withCachingTime(int cachingTime) {
            this.cachingTime = cachingTime;
            return this;
        }

        public MultiValueCacheBuilder withMaxSizePerKey(int maxSizePerKey) {
            this.maxSizePerKey = maxSizePerKey;
            return this;
        }

        public MultiValueCache build() {
            MultiValueCache multiValueCache = new MultiValueCache();
            multiValueCache.maxSizePerKey = this.maxSizePerKey;
            multiValueCache.consumer = this.consumer;
            multiValueCache.cacheMap = this.cacheMap;
            multiValueCache.cachingTime = this.cachingTime;
            multiValueCache.sch = this.sch;
            multiValueCache.init();
            return multiValueCache;
        }
    }
}
