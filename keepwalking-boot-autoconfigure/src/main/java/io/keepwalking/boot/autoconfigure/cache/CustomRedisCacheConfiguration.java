/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.keepwalking.boot.autoconfigure.cache;

import java.time.Duration;
import java.util.List;

import io.keepwalking.boot.autoconfigure.data.redis.RedisCacheProperties;
import io.keepwalking.boot.autoconfigure.data.redis.RedisCacheProperties.RedisCacheConfig;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * CustomRedisCacheConfiguration
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @see org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
 * @see org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
 * @since 2025.01
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisConnectionFactory.class)
@EnableConfigurationProperties({CacheProperties.class, RedisCacheProperties.class})
public class CustomRedisCacheConfiguration {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory,
            CacheProperties cacheProperties, RedisCacheProperties redisCacheProperties,
            RedisSerializer<Object> valueSerializer) {
        RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory);
        if (cacheProperties.getRedis().isEnableStatistics()) {
            builder.enableStatistics();
        }
        builder.cacheDefaults(createDefaultCacheConfiguration(cacheProperties, valueSerializer));
        List<RedisCacheConfig> caches = redisCacheProperties.getCaches();
        if (!caches.isEmpty()) {
            caches.stream()
                    .filter(v -> v.getCacheName() != null)
                    .forEach(v -> builder.withCacheConfiguration(v.getCacheName(),
                            createCacheConfiguration(cacheProperties, v.getTimeToLive(), valueSerializer)));
        }
        return builder.build();
    }

    private RedisCacheConfiguration createDefaultCacheConfiguration(CacheProperties cacheProperties,
            RedisSerializer<Object> valueSerializer) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(SerializationPair.fromSerializer(valueSerializer));
        Redis redisProperties = cacheProperties.getRedis();
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }

    private RedisCacheConfiguration createCacheConfiguration(CacheProperties cacheProperties, Duration timeToLive,
            RedisSerializer<Object> valueSerializer) {
        RedisCacheConfiguration config = createDefaultCacheConfiguration(cacheProperties, valueSerializer);
        if (timeToLive != null) {
            config = config.entryTtl(timeToLive);
        }
        return config;
    }
}
