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

/**
 * 返回码
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @since 2024.11
 */
public interface ResultCode {
    /**
     * 获取返回码
     *
     * @return 返回码
     */
    String getCode();

    /**
     * 获取返回信息
     *
     * @return 返回信息
     */
    String getMsg();

    /**
     * 动态替换错误消息中的占位符
     *
     * @param params 动态参数
     * @return 格式化后的消息
     */
    default String format(Object... params) {
        return String.format(getMsg(), params);
    }
}