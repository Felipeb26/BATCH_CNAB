package com.batsworks.batch.config.cache;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.ExpiryPolicy;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;
import java.util.function.Supplier;


@Configuration
public class EhConfiguration {

    @Bean
    public CacheManager cacheManager() {
        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager = provider.getCacheManager();

        javax.cache.configuration.Configuration<Object, Object> cacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfiguration());
        cacheManager.createCache("EntityCache", cacheConfiguration);
        return cacheManager;
    }

    private CacheConfiguration<Object, Object> cacheConfiguration() {
        return CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                        ResourcePoolsBuilder.heap(5).offheap(5, MemoryUnit.MB))
                .withExpiry(new ExpiryPolicy<>() {
                    @Override
                    public Duration getExpiryForCreation(Object key, Object value) {
                        return Duration.ofSeconds(300);
                    }

                    @Override
                    public Duration getExpiryForAccess(Object key, Supplier<?> value) {
                        return Duration.ofSeconds(300);
                    }

                    @Override
                    public Duration getExpiryForUpdate(Object key, Supplier<?> oldValue, Object newValue) {
                        return Duration.ofSeconds(300);
                    }
                }).build();
    }


}
