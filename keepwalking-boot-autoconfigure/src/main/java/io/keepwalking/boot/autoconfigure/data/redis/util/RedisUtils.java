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

package io.keepwalking.boot.autoconfigure.data.redis.util;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * RedisUtils
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @see io.keepwalking.boot.autoconfigure.data.redis.CustomRedisAutoConfiguration
 * @see io.keepwalking.boot.autoconfigure.data.redis.RedisUtilsAutoConfiguration
 * @since 2025.01
 */
public abstract class RedisUtils {
    protected static RedisTemplate<String, Object> redisTemplate;

    protected static StringRedisTemplate stringRedisTemplate;

    /**
     * 初始化 RedisUtils。该方法在 Spring Boot 自动配置时会调用。
     * {@link org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration}
     *
     * @param redisTemplate RedisTemplate实例，用于处理对象类型的Redis操作
     * @param stringRedisTemplate StringRedisTemplate实例，用于处理字符串类型的Redis操作
     */
    public static void init(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        if (redisTemplate == null || stringRedisTemplate == null) {
            throw new IllegalArgumentException("RedisTemplate or StringRedisTemplate cannot be null");
        }
        RedisUtils.redisTemplate = redisTemplate;
        RedisUtils.stringRedisTemplate = stringRedisTemplate;
    }

    /* ---------------------- String 类型操作 -------------------------- */

    /**
     * 设置字符串键值对。
     *
     * @param key 键
     * @param value 值
     */
    public static void setString(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取字符串值。
     *
     * @param key 键
     * @return 值
     */
    public static String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 设置字符串键值对并指定过期时间。
     *
     * @param key 键
     * @param value 值
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     */
    public static void setStringAndExpire(String key, String value, long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 原子递增。
     *
     * @param key 键
     * @param delta 增量（正数递增，负数递减）
     * @return 自增后的值
     */
    public static Long increment(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 批量设置字符串键值对。
     *
     * @param keyValueMap 键值对集合
     */
    public static void batchSetString(Map<String, String> keyValueMap) {
        stringRedisTemplate.opsForValue().multiSet(keyValueMap);
    }

    /* ---------------------- 通用 Key-Value 操作 ---------------------- */

    /**
     * 设置键值对。
     *
     * @param key 键
     * @param value 值
     * @param <T> 值的类型
     */
    public static <T> void set(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置键值对并指定过期时间。
     *
     * @param key 键
     * @param value 值
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @param <T> 值的类型
     */
    public static <T> void setAndExpire(String key, T value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 获取键对应的值。
     *
     * @param key 键
     * @param clazz 值的类型
     * @param <T> 值的类型
     * @return 值
     */
    public static <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        return clazz.cast(value);
    }

    /**
     * 获取键对应的值，返回 Optional 包装。
     *
     * @param key 键
     * @param clazz 值的类型
     * @param <T> 值的类型
     * @return Optional 包装的值
     */
    public static <T> Optional<T> getOptional(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(clazz.cast(value));
    }

    /**
     * 批量设置键值对。
     *
     * @param keyValueMap 键值对集合
     */
    public static void batchSet(Map<String, Object> keyValueMap) {
        redisTemplate.opsForValue().multiSet(keyValueMap);
    }

    /**
     * 删除键值对。
     *
     * @param key 键
     */
    public static void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 判断键是否存在。
     *
     * @param key 键
     * @return 如果键存在，返回 true，否则返回 false
     */
    public static boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 设置键的过期时间。
     *
     * @param key 键
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @return 是否设置成功
     */
    public static boolean setExpire(String key, long timeout, TimeUnit timeUnit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, timeUnit));
    }

    /**
     * 获取键的剩余过期时间。
     *
     * @param key 键
     * @return 剩余时间（秒），如果键不存在或未设置过期时间，返回 null
     */
    public static Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }
}
