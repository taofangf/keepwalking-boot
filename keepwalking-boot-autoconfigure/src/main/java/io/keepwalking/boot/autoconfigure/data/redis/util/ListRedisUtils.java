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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ListRedisUtils
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @since 2025.01
 */
public class ListRedisUtils extends RedisUtils {
    /**
     * 向列表右侧添加元素。
     *
     * @param key 键
     * @param value 值
     * @param <T> 值的类型
     */
    public static <T> void pushToList(String key, T value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 从列表左侧弹出元素。
     *
     * @param key 键
     * @param clazz 值的类型
     * @param <T> 值的类型
     * @return 弹出的元素
     */
    public static <T> T popFromList(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForList().leftPop(key);
        return clazz.cast(value);
    }

    /**
     * 获取列表指定范围的元素。
     *
     * @param key 键
     * @param start 起始索引
     * @param end 结束索引
     * @param clazz 值的类型
     * @param <T> 值的类型
     * @return 元素列表
     */
    public static <T> List<T> getRangeFromList(String key, long start, long end, Class<T> clazz) {
        List<Object> values = redisTemplate.opsForList().range(key, start, end);
        List<T> result = new ArrayList<>();
        if (values != null) {
            for (Object value : values) {
                result.add(clazz.cast(value));
            }
        }
        return result;
    }

    /**
     * 批量向列表右侧添加元素。
     *
     * @param key 键
     * @param values 值列表
     * @param <T> 值的类型
     */
    public static <T> void batchPushToList(String key, List<T> values) {
        redisTemplate.opsForList().rightPushAll(key, values.toArray());
    }

    /**
     * 获取列表长度。
     *
     * @param key 键
     * @return 列表长度
     */
    public static Long getListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /* ---------------------- 高效批量操作 --------------------------- */

    /**
     * 批量删除键。
     *
     * @param keys 键集合
     */
    public static void batchDelete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 批量获取值。
     *
     * @param keys 键集合
     * @param clazz 值的类型
     * @param <T> 值的类型
     * @return 键值对 Map
     */
    public static <T> Map<String, T> batchGet(Collection<String> keys, Class<T> clazz) {
        List<Object> values = redisTemplate.opsForValue().multiGet(keys);
        Map<String, T> result = new HashMap<>();
        Iterator<String> keyIterator = keys.iterator();
        if (values != null) {
            for (Object value : values) {
                if (keyIterator.hasNext()) {
                    String key = keyIterator.next();
                    result.put(key, clazz.cast(value));
                }
            }
        }
        return result;
    }

    /**
     * 批量设置键值对并指定统一的过期时间。
     *
     * @param keyValueMap 键值对集合
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     */
    public static void batchSetAndExpire(Map<String, Object> keyValueMap, long timeout, TimeUnit timeUnit) {
        keyValueMap.forEach((key, value) -> redisTemplate.opsForValue().set(key, value, timeout, timeUnit));
    }
}
