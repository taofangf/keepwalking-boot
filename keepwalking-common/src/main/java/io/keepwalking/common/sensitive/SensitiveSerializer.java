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
    private SensitiveType type;

    /**
     * 指定的脱敏处理类
     */
    private Class<? extends CustomSensitiveHandler> handler;

    protected SensitiveSerializer() {
        super(Object.class);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        if (property != null) {
            Sensitive sensitive = property.getAnnotation(Sensitive.class);
            if (sensitive != null && sensitive.enabled()) {
                this.type = sensitive.type();
                this.handler = sensitive.handler();
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
            String result;
            if (SensitiveType.CUSTOM == type && handler != null) {
                CustomSensitiveHandler customHandler = handler.getConstructor().newInstance();
                result = customHandler.handle(input);
            }
            else {
                result = type.handle(input);
            }
            gen.writeString(result);
        }
        catch (Exception e) {
            log.error("Sensitive serialization failed for value: {} with handler: {}", input, handler, e);
            gen.writeString(input);
        }
    }
}
