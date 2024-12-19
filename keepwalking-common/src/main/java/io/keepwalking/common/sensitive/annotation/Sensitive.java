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

package io.keepwalking.common.sensitive.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.keepwalking.common.sensitive.CustomSensitiveHandler;
import io.keepwalking.common.sensitive.DefaultSensitiveHandler;
import io.keepwalking.common.sensitive.SensitiveSerializer;
import io.keepwalking.common.sensitive.SensitiveType;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 模糊化脱敏注解
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @see SensitiveSerializer
 * @since 2024.12
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveSerializer.class)
public @interface Sensitive {
    /**
     * 脱敏策略类型
     * 默认使用通用脱敏策略，其他类型如手机号、银行卡号等可自定义处理。
     *
     * @return {@link SensitiveType}
     */
    SensitiveType type() default SensitiveType.GENERAL;

    /**
     * 是否启用脱敏，默认为启用（true）。如果不需要脱敏，显式设置为 false。
     *
     * @return true or false
     */
    boolean enabled() default true;

    /**
     * 指定自定义脱敏处理类。如果 type 为 CUSTOM，则必须指定该类。
     *
     * @return 自定义脱敏处理类
     */
    Class<? extends CustomSensitiveHandler> handler() default DefaultSensitiveHandler.class;
}
