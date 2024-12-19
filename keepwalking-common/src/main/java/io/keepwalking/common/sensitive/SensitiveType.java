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

import cn.hutool.core.util.DesensitizedUtil;

/**
 * 模糊化脱敏类型
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @since 2024.12
 */
public enum SensitiveType {
    /**
     * 全部字符都用*代替，比如：******
     */
    GENERAL {
        @Override
        public String handle(String input) {
            return DesensitizedUtil.password(input);
        }
    },
    /**
     * 【中文姓名】只显示第一个汉字，其他隐藏为2个星号，比如：李**
     */
    CHINESE_NAME {
        @Override
        public String handle(String input) {
            return DesensitizedUtil.chineseName(input);
        }
    },
    /**
     * 密码
     */
    PASSWORD {
        @Override
        public String handle(String input) {
            return DesensitizedUtil.password(input);
        }
    },
    /**
     * 【身份证号】前1位 和后2位
     */
    ID_CARD {
        @Override
        public String handle(String input) {
            return DesensitizedUtil.idCardNum(String.valueOf(input), 1, 2);
        }
    },
    /**
     * 【手机号码】前三位，后4位，其他隐藏，比如135****2210
     */
    PHONE {
        @Override
        public String handle(String input) {
            return DesensitizedUtil.mobilePhone(input);
        }
    },
    /**
     * 【银行卡号脱敏】由于银行卡号长度不定，所以只展示前4位，后面的位数根据卡号决定展示1-4位
     */
    BANK_CARD {
        @Override
        public String handle(String input) {
            return DesensitizedUtil.bankCard(input);
        }
    },
    /**
     * 【电子邮箱】邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示
     */
    EMAIL {
        @Override
        public String handle(String input) {
            return DesensitizedUtil.email(input);
        }
    },
    /**
     * 自定义脱敏策略（由用户自定义实现）,默认实现不做任何处理
     * {@link CustomSensitiveHandler}
     */
    CUSTOM {
        @Override
        public String handle(String input) {
            return input;
        }
    },
    ;

    /**
     * 默认的处理方法
     *
     * @param input 输入字符串
     * @return 脱敏后的结果
     */
    public abstract String handle(String input);
}
