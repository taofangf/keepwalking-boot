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

package io.keepwalking.common.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;

/**
 * JacksonUtils
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @since 2024.11
 */
@Slf4j
public class JacksonUtils {
    private static final ObjectMapper DEFAULT_OBJECT_MAPPER;

    static {
        /*
         * {@code     defaultObjectMapper = new ObjectMapper()
         *         .registerModule(new JavaTimeModule())  // 注册 Java 8 日期时间模块
         *         .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)  // 防止空 Bean 序列化异常
         *         .enable(SerializationFeature.INDENT_OUTPUT)  // 启用缩进输出
         *         .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))  // 设置日期格式
         *         .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)  // 允许忽略未知属性
         *         .configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false)  // 不序列化 null 值的 Map 键值
         *         .configure(SerializationFeature.WRITE_EMPTY_JSON_OBJECTS, false)  // 不序列化空对象
         *         .configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false)  // 不序列化空数组
         *         .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)  // 空字符串作为 null 处理
         *         .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)  // 时间戳按毫秒处理而非纳秒
         *         .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)  // 使用蛇形命名规则（字段名转为下划线）
         *         .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)  // 忽略多余的字段
         *         .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)  // 允许未知枚举值为 null
         *         .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)  // 允许单值数组
         *         .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)  // 使用枚举的 toString 方法序列化
         *         .configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)  // 使用枚举的 toString 方法反序列化
         *         .configure(JsonParser.Feature.ALLOW_COMMENTS, true)  // 允许 JSON 注释
         *         .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true); // 允许非引号字段名称
         * }
         */
        DEFAULT_OBJECT_MAPPER = new ObjectMapper()
                // 注册 Java 8 日期时间模块
                .registerModule(new JavaTimeModule().addSerializer(LocalDateTime.class,
                                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .addDeserializer(LocalDateTime.class,
                                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .addSerializer(LocalDate.class,
                                new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .addDeserializer(LocalDate.class,
                                new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .addSerializer(LocalTime.class,
                                new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")))
                        .addDeserializer(LocalTime.class,
                                new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss"))))
                // 防止空 Bean 序列化异常
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                // 启用缩进输出
                // .enable(SerializationFeature.INDENT_OUTPUT)
                // 设置日期格式
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                // 允许忽略未知属性
                .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JacksonUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 使用默认的 ObjectMapper 将对象转换为 JSON 字符串
     */
    public static String toJson(Object object) {
        return toJson(object, DEFAULT_OBJECT_MAPPER);
    }

    /**
     * 将对象转换为 JSON 字符串（允许传入自定义 ObjectMapper）
     */
    public static String toJson(Object object, ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            log.error("JSON序列化失败: {}", e.getMessage(), e);
            throw new JacksonException("JSON序列化失败", e);
        }
    }

    /**
     * 使用默认的 ObjectMapper 将 JSON 字符串转换为对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return fromJson(json, clazz, DEFAULT_OBJECT_MAPPER);
    }

    /**
     * 将 JSON 字符串转换为对象（允许传入自定义 ObjectMapper）
     */
    public static <T> T fromJson(String json, Class<T> clazz, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(json, clazz);
        }
        catch (IOException e) {
            log.error("JSON映射失败: {}", e.getMessage(), e);
            throw new JacksonException("JSON映射失败", e);
        }
    }

    /**
     * 使用默认的 ObjectMapper 将 JSON 字符串转换为对象（支持复杂泛型）
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        return fromJson(json, typeReference, DEFAULT_OBJECT_MAPPER);
    }

    /**
     * 将 JSON 字符串转换为对象（允许传入自定义 ObjectMapper）
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(json, typeReference);
        }
        catch (JsonProcessingException e) {
            log.error("JSON处理失败: {}", e.getMessage(), e);
            throw new JacksonException("JSON处理失败", e);
        }
    }

    /**
     * 使用默认的 ObjectMapper 将对象转换为 Map
     */
    public static Map<String, Object> toMap(Object object) {
        return toMap(object, DEFAULT_OBJECT_MAPPER);
    }

    /**
     * 将对象转换为 Map（允许传入自定义 ObjectMapper）
     */
    public static Map<String, Object> toMap(Object object, ObjectMapper objectMapper) {
        try {
            return objectMapper.convertValue(object, new TypeReference<>() {});
        }
        catch (IllegalArgumentException e) {
            log.error("对象转换为Map失败: {}", e.getMessage(), e);
            throw new JacksonException("对象转换为Map失败", e);
        }
    }

    /**
     * 使用默认的 ObjectMapper 将 JSON 字符串转换为 List
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        return toList(json, clazz, DEFAULT_OBJECT_MAPPER);
    }

    /**
     * 将 JSON 字符串转换为 List（允许传入自定义 ObjectMapper）
     */
    public static <T> List<T> toList(String json, Class<T> clazz, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        }
        catch (JsonProcessingException e) {
            log.error("JSON转换为List失败: {}", e.getMessage(), e);
            throw new JacksonException("JSON转换为List失败", e);
        }
    }

    /**
     * 使用默认的 ObjectMapper 将 JSON 字符串转换为 Set
     */
    public static <T> Set<T> toSet(String json, Class<T> clazz) {
        return toSet(json, clazz, DEFAULT_OBJECT_MAPPER);
    }

    /**
     * 将 JSON 字符串转换为 Set（允许传入自定义 ObjectMapper）
     */
    public static <T> Set<T> toSet(String json, Class<T> clazz, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(Set.class, clazz));
        }
        catch (JsonProcessingException e) {
            log.error("JSON转换为Set失败: {}", e.getMessage(), e);
            throw new JacksonException("JSON转换为Set失败", e);
        }
    }

    /**
     * 自定义异常类
     */
    public static class JacksonException extends RuntimeException {
        public JacksonException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
