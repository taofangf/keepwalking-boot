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

import io.keepwalking.common.sensitive.annotation.Sensitive;
import io.keepwalking.common.util.JacksonUtils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

/**
 * SensitiveSerializerTest
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @since 2024.12
 */
class SensitiveSerializerTest {
    static class AddressCustomSensitiveHandler implements CustomSensitiveHandler {
        @Override
        public String handle(String value) {
            return "11111";
        }
    }

    @Getter
    @Setter
    static class BaseInfo {
        @Sensitive(type = SensitiveType.CUSTOM, handler = AddressCustomSensitiveHandler.class)
        private String address;
    }

    @Getter
    @Setter
    @Builder
    static class UserInfo extends BaseInfo {
        @Sensitive(type = SensitiveType.CHINESE_NAME)
        private String username;

        @Sensitive(type = SensitiveType.PASSWORD)
        private String password;

        @Sensitive(type = SensitiveType.EMAIL)
        private String email;
    }

    @Test
    void tesSensitive() {
        String json = JacksonUtils.toJson(
                UserInfo.builder().username("张三").password("1qaz!QAZ").email("xxxxx@139.com").build());
        System.out.println(json);

        UserInfo userInfo = UserInfo.builder().username("张三").password("1qaz!QAZ").email("xxxxx@139.com").build();
        userInfo.setAddress("湖北省武汉市江汉区");
        System.out.println(JacksonUtils.toJson(userInfo));
    }
}