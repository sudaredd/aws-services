package app.aws.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class CacheTest {

    @Test
    public void testCache() throws InterruptedException {
        BiConsumer<String, String> bs = (k,v)-> System.out.println("key :"+k + " ,val:"+v);
        Cache<String, String> cache = new Cache(bs, 2);
        cache.put("one","1");
        cache.put("two","2");
        cache.put("three","3");
        Assertions.assertTrue(!cache.isEmpty());
        System.out.println(cache);
        TimeUnit.SECONDS.sleep(10);
        System.out.println(cache);
        Assertions.assertTrue(cache.isEmpty());


    }
}
