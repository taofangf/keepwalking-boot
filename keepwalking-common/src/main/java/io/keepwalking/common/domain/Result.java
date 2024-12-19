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

import io.keepwalking.common.exception.BusinessException;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 通用返回类
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @since 2024.11
 */
@Getter
@Setter
@ToString
public class Result<T> {
    /**
     * 返回码
     */
    private String code;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 链路ID
     */
    private String traceId;

    public Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.traceId = "N/A";
    }

    /**
     * 通用返回构造
     *
     * @param code 返回码
     * @param msg 返回信息
     * @param data 返回数据
     * @param <T> 返回数据类型
     * @return 返回信息
     */
    public static <T> Result<T> of(String code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    /**
     * 通用返回构造
     *
     * @param resultCode {@link ResultCode}
     * @param data 返回数据
     * @param <T> 返回数据类型
     * @return 返回信息
     */
    public static <T> Result<T> of(ResultCode resultCode, T data) {
        return of(resultCode.getCode(), resultCode.getMsg(), data);
    }

    /**
     * 成功时的返回
     *
     * @return 成功时的返回
     */
    public static Result<Void> ofSuccess() {
        return of(CommonResultCode.SUCCESS, null);
    }

    /**
     * 成功时的返回
     *
     * @param data 返回数据
     * @param <T> 返回数据类型
     * @return 成功时的返回
     */
    public static <T> Result<T> ofSuccess(T data) {
        return of(CommonResultCode.SUCCESS, data);
    }

    /**
     * 失败时的返回
     *
     * @param msg 返回信息
     * @return 失败时的返回
     */
    public static <T> Result<T> ofFailure(String msg) {
        return of(CommonResultCode.FAILURE.getCode(), msg, null);
    }

    /**
     * 失败时的返回
     *
     * @param resultCode {@link ResultCode}
     * @return 返回信息
     */
    public static <T> Result<T> ofFailure(ResultCode resultCode) {
        return of(resultCode.getCode(), resultCode.getMsg(), null);
    }

    /**
     * 失败时的返回
     *
     * @param data 返回数据
     * @param <T> 返回数据类型
     * @return 失败时的返回
     */
    public static <T> Result<T> ofFailure(T data) {
        return of(CommonResultCode.FAILURE, data);
    }

    /**
     * 失败时的返回
     *
     * @param msg 返回信息
     * @param data 返回数据
     * @param <T> 返回数据类型
     * @return 失败时的返回
     */
    public static <T> Result<T> ofFailure(String msg, T data) {
        return of(CommonResultCode.FAILURE.getCode(), msg, data);
    }

    /**
     * 异常时的返回
     *
     * @param ex 异常对象
     * @param <T> 异常对象
     * @return 异常时的返回
     */
    public static <T extends BusinessException> Result<T> ofException(T ex) {
        return of(ex.getCode(), ex.getMsg(), null);
    }
}
