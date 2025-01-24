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

package io.keepwalking.boot.autoconfigure.data.redis;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RedisCacheProperties
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @see org.springframework.boot.autoconfigure.cache.CacheProperties
 * @see org.springframework.boot.autoconfigure.data.redis.RedisProperties
 * @since 2025.01
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.cache.redis")
public class RedisCacheProperties {
    /**
     * 自定义Redis缓存配置集合
     */
    private List<RedisCacheConfig> caches = new ArrayList<>();

    @Getter
    @Setter
    public static class RedisCacheConfig {
        /**
         * 缓存名称
         * CacheProperties cacheNames中缓存名称如果配置该值会导致禁用动态缓存创建
         */
        private String cacheName;

        /**
         * 缓存过期时间，如果没有指定单位默认为妙
         * </br>
         * s 表示秒/ms 表示毫秒/m 表示分钟/h 表示小时。
         */
        private Duration timeToLive;
    }
}
