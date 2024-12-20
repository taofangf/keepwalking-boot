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

package io.keepwalking.common.sensitive;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import io.keepwalking.common.sensitive.annotation.Sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;

/**
 * SensitiveSerializer
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @see Sensitive
 * @since 2024.12
 */
@Slf4j
public class SensitiveSerializer extends StdSerializer<Object> implements ContextualSerializer {

    /**
     * 脱敏类型
     * {@link SensitiveType}
     */
    private final SensitiveType type;

    /**
     * 指定的脱敏处理类
     */
    private final Class<? extends CustomSensitiveHandler> handler;

    /**
     * 处理器实例缓存
     */
    private static final ConcurrentHashMap<Class<? extends CustomSensitiveHandler>, CustomSensitiveHandler>
            HANDLER_CACHE = new ConcurrentHashMap<>();

    public SensitiveSerializer() {
        super(Object.class);
        this.type = null;
        this.handler = null;
    }

    protected SensitiveSerializer(SensitiveType type, Class<? extends CustomSensitiveHandler> handler) {
        super(Object.class);
        this.type = type;
        this.handler = handler;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        if (property != null) {
            Sensitive sensitive = property.getAnnotation(Sensitive.class);
            if (sensitive != null && sensitive.enabled()) {
                return new SensitiveSerializer(sensitive.type(), sensitive.handler());
            }
        }
        return this;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        String input = value.toString();
        try {
            String result = handleSensitive(input);
            gen.writeString(result);
        }
        catch (Exception e) {
            log.error("Sensitive serialization failed for value: {} with handler: {}", input, handler, e);
            gen.writeString(input);
        }
    }

    /**
     * 执行脱敏处理逻辑
     *
     * @param input 原始输入
     * @return 脱敏后的结果
     */
    private String handleSensitive(String input) {
        if (type == null) {
            return input;
        }
        if (SensitiveType.CUSTOM == type && handler != null) {
            return HANDLER_CACHE.computeIfAbsent(handler, v -> {
                try {
                    return v.getDeclaredConstructor().newInstance();
                }
                catch (Exception e) {
                    log.error("Failed to instantiate handler: {}", v, e);
                    throw new IllegalStateException("Failed to instantiate handler", e);
                }
            }).handle(input);
        }
        return type.handle(input);
    }
}
