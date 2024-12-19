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

package io.keepwalking.common.exception;

import io.keepwalking.common.domain.ResultCode;

import lombok.Getter;

/**
 * 自定义全局业务异常
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @since 2024.11
 */
@Getter
public class BusinessException extends RuntimeException {
    /**
     * 异常编码
     */
    private final String code;

    /**
     * 异常信息
     */
    private final String msg;

    public BusinessException(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMsg());
    }

    public BusinessException(ResultCode resultCode, String param) {
        this(resultCode.getCode(), resultCode.format(param));
    }

    public BusinessException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
