package juniter.conf;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CachingConfig {



    @Bean
    public CacheManager cacheManager() {
        var cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                buildSandboxesCache(),
                buildBlockCache())
        );

        return cacheManager;
    }

    private CaffeineCache buildBlockCache() {
        return new CaffeineCache("blocks", Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .expireAfterAccess(3, TimeUnit.MINUTES)
                //.recordStats()
                .maximumSize(1000)
                .ticker(Ticker.systemTicker())
                //.ticker(ticker())
                .build());
    }

    private CaffeineCache buildSandboxesCache() {
        return new CaffeineCache("sandboxes", Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .expireAfterAccess(3, TimeUnit.MINUTES)
                //.recordStats()
                .maximumSize(1000)
                .ticker(Ticker.systemTicker())
                //.ticker(ticker())
                .build());
    }

}