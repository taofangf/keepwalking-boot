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

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 分页结果
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @since 2024.12
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    /**
     * 数据集合
     */
    private List<T> list;

    /**
     * 总数
     */
    private Long total;

    /**
     * 构造分页对象
     *
     * @param list 数据集合
     * @param total 总数
     * @param <T> 数据泛型
     * @return 分页结果
     */
    public static <T> PageResult<T> of(List<T> list, Long total) {
        return new PageResult<T>(list, total);
    }
}
