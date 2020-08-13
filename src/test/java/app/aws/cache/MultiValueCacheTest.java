package app.aws.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MultiValueCacheTest {

    @Test
    public void testMultiValue() throws InterruptedException {
       MultiValueCache<String, String> ml = MultiValueCache.MultiValueCacheBuilder.aMultiValueCache()
                .withCachingTime(2)
                .withMaxSizePerKey(5)
                .withConsumer((a,b)-> System.out.println("a:"+a + " ,b:"+b))
                .build();

       ml.put("one","1");
       ml.put("one","2");
       ml.put("one","3");

        System.out.println(ml);
        Assertions.assertTrue(!ml.isEmpty());

        SECONDS.sleep(10);
        System.out.println(ml);
        Assertions.assertTrue(ml.isEmpty());
    }
}
