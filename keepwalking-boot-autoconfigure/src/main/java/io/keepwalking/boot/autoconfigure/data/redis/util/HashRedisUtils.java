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

/**
 * HashRedisUtils
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @since 2025.01
 */
public class HashRedisUtils extends RedisUtils {
    /**
     * 向哈希表中存放键值对。
     *
     * @param key 键
     * @param hashKey 哈希键
     * @param value 值
     * @param <T> 值的类型
     */
    public static <T> void putToHash(String key, String hashKey, T value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 获取哈希表中的值。
     *
     * @param key 键
     * @param hashKey 哈希键
     * @param clazz 值的类型
     * @param <T> 值的类型
     * @return 值
     */
    public static <T> T getFromHash(String key, String hashKey, Class<T> clazz) {
        Object value = redisTemplate.opsForHash().get(key, hashKey);
        return clazz.cast(value);
    }

    /**
     * 获取哈希表中的所有键值对。
     *
     * @param key 键
     * @return 哈希表的键值对
     */
    public static Map<Object, Object> getAllFromHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 删除哈希表中的键值对。
     *
     * @param key 键
     * @param hashKeys 哈希键
     */
    public static void deleteFromHash(String key, String... hashKeys) {
        redisTemplate.opsForHash().delete(key, (Object[]) hashKeys);
    }

    /**
     * 判断哈希表中是否存在指定的哈希键。
     *
     * @param key 键
     * @param hashKey 哈希键
     * @return 是否存在
     */
    public static boolean hashKeyExists(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }
}
