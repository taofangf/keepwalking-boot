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

package io.keepwalking.boot.autoconfigure.security.web.servlet;

import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

/**
 * ServletWebSecurityAutoConfigurationTest
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @since 2024.08
 */
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "keepwalking.security.web.enabled=true",
        "keepwalking.security.web.remote-addr.allow=127\\.\\\\d+\\.\\\\d+\\.\\\\d+|::1|0:0:0:0:0:0:0:1",
        "keepwalking.security.web.rate-limit.bucket-duration=1", "keepwalking.security.web.rate-limit.bucket-requests=8"
})
@Import({ServletWebSecurityAutoConfiguration.class, SecurityTestController.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServletWebSecurityAutoConfigurationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void remoteIpFilter() throws Exception {
        mockMvc.perform(post("/test-endpoint").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    void remoteAddrFilter() throws Exception {

    }

    @Test
    void corsFilter() {
    }

    @Test
    void rateLimitFilter() throws Exception {
        for (int i = 0; i < 8; i++) {
            mockMvc.perform(post("/test-endpoint").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        }
        mockMvc.perform(post("/test-endpoint").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isTooManyRequests());
        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        for (int i = 0; i < 8; i++) {
            mockMvc.perform(post("/test-endpoint").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        }
        mockMvc.perform(post("/test-endpoint").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void httpHeadSecurityFilter() {
    }
}