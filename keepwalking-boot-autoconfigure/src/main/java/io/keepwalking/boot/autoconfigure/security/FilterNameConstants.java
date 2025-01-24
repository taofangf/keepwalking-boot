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

package io.keepwalking.boot.autoconfigure.security;

/**
 * FilterNameConstants
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @since 2024.08
 */
public interface FilterNameConstants {
    /**
     * remoteIpFilter
     */
    String REMOTE_IP_FILTER = "remoteIpFilter";

    /**
     * remoteAddrFilter
     */
    String REMOTE_ADDR_FILTER = "remoteAddrFilter";

    /**
     * corsFilter
     */
    String CORS_FILTER = "corsFilter";

    /**
     * rateLimitFilter
     */
    String RATE_LIMIT_FILTER = "rateLimitFilter";

    /**
     * httpHeadSecurityFilter
     */
    String HTTP_HEAD_SECURITY_FILTER = "httpHeadSecurityFilter";
}
