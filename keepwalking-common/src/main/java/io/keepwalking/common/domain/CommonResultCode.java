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

package io.keepwalking.common.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用返回码
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @since 2024.11
 */
@Getter
@AllArgsConstructor
public enum CommonResultCode implements ResultCode {
    /**
     * 操作成功
     */
    SUCCESS("000000", "Operation successful"),
    /**
     * 操作失败
     */
    FAILURE("100000", "Operation failed"),
    /**
     * 系统异常
     */
    SYSTEM_EXCEPTION("999999", "System exception"),
    ;

    /**
     * 返回码
     */
    private final String code;

    /**
     * 返回信息
     */
    private final String msg;
}
