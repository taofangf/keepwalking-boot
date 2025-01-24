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

import java.util.Optional;

import io.keepwalking.boot.autoconfigure.condition.ConditionOnEnabledWebSecurity;

import static io.keepwalking.boot.autoconfigure.Constants.PROPERTY_NAME_ENABLED;
import static io.keepwalking.boot.autoconfigure.Constants.WEB_SECURITY_CORS_PROPERTY_NAME_PREFIX;
import static io.keepwalking.boot.autoconfigure.Constants.WEB_SECURITY_HTTP_HEADER_PROPERTY_NAME_PREFIX;
import static io.keepwalking.boot.autoconfigure.Constants.WEB_SECURITY_RATE_LIMIT_PROPERTY_NAME_PREFIX;
import static io.keepwalking.boot.autoconfigure.Constants.WEB_SECURITY_REMOTE_ADDR_PROPERTY_NAME_PREFIX;
import static io.keepwalking.boot.autoconfigure.Constants.WEB_SECURITY_REMOTE_IP_PROPERTY_NAME_PREFIX;
import static io.keepwalking.boot.autoconfigure.security.FilterNameConstants.CORS_FILTER;
import static io.keepwalking.boot.autoconfigure.security.FilterNameConstants.HTTP_HEAD_SECURITY_FILTER;
import static io.keepwalking.boot.autoconfigure.security.FilterNameConstants.RATE_LIMIT_FILTER;
import static io.keepwalking.boot.autoconfigure.security.FilterNameConstants.REMOTE_ADDR_FILTER;
import static io.keepwalking.boot.autoconfigure.security.FilterNameConstants.REMOTE_IP_FILTER;
import static org.apache.catalina.filters.CorsFilter.PARAM_CORS_ALLOWED_HEADERS;
import static org.apache.catalina.filters.CorsFilter.PARAM_CORS_ALLOWED_METHODS;
import static org.apache.catalina.filters.CorsFilter.PARAM_CORS_ALLOWED_ORIGINS;
import static org.apache.catalina.filters.CorsFilter.PARAM_CORS_EXPOSED_HEADERS;
import static org.apache.catalina.filters.CorsFilter.PARAM_CORS_PREFLIGHT_MAXAGE;
import static org.apache.catalina.filters.CorsFilter.PARAM_CORS_REQUEST_DECORATE;
import static org.apache.catalina.filters.CorsFilter.PARAM_CORS_SUPPORT_CREDENTIALS;
import static org.apache.catalina.filters.RateLimitFilter.PARAM_BUCKET_DURATION;
import static org.apache.catalina.filters.RateLimitFilter.PARAM_BUCKET_REQUESTS;
import static org.apache.catalina.filters.RateLimitFilter.PARAM_ENFORCE;
import static org.apache.catalina.filters.RateLimitFilter.PARAM_STATUS_CODE;
import static org.apache.catalina.filters.RateLimitFilter.PARAM_STATUS_MESSAGE;

import org.apache.catalina.filters.CorsFilter;
import org.apache.catalina.filters.HttpHeaderSecurityFilter;
import org.apache.catalina.filters.RateLimitFilter;
import org.apache.catalina.filters.RemoteAddrFilter;
import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * ServletWebSecurityAutoConfiguration
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @see <a href="https://tomcat.apache.org/tomcat-9.0-doc/config/filter.html">Apache Tomcat filter.html</a>
 * @since 2024.07
 */
@AutoConfiguration
@ConditionOnEnabledWebSecurity
@ConditionalOnWebApplication(type = Type.SERVLET)
@EnableConfigurationProperties({WebSecurityProperties.class, FilterOrderProperties.class})
public class ServletWebSecurityAutoConfiguration {

    /**
     * WebSecurityProperties
     */
    private final WebSecurityProperties properties;

    /**
     * FilterOrderProperties
     */
    private final FilterOrderProperties orderProperties;

    public ServletWebSecurityAutoConfiguration(WebSecurityProperties properties,
            FilterOrderProperties orderProperties) {
        this.properties = properties;
        this.orderProperties = orderProperties;
    }

    /**
     * RemoteIpFilter
     *
     * @return FilterRegistrationBean<RemoteIpFilter>
     * @see <a href="https://tomcat.apache.org/tomcat-9.0-doc/config/filter.html#Remote_IP_Filter">RemoteIpFilter</a>
     * @see RemoteIpFilter
     */
    @Bean
    @ConditionalOnClass(RemoteIpFilter.class)
    @ConditionalOnMissingFilterBean(RemoteIpFilter.class)
    @ConditionalOnProperty(prefix = WEB_SECURITY_REMOTE_IP_PROPERTY_NAME_PREFIX, name = PROPERTY_NAME_ENABLED, matchIfMissing = true)
    public FilterRegistrationBean<RemoteIpFilter> remoteIpFilter() {
        FilterRegistrationBean<RemoteIpFilter> registrationBean = new FilterRegistrationBean<>();
        Optional.ofNullable(properties.getRemoteIp().getInternalProxies())
                .ifPresent(v -> registrationBean.addInitParameter("internalProxies", v));
        Optional.ofNullable(properties.getRemoteIp().getProtocolHeader())
                .ifPresent(v -> registrationBean.addInitParameter("protocolHeader", v));
        Optional.ofNullable(properties.getRemoteIp().getProtocolHeaderHttpsValue())
                .ifPresent(v -> registrationBean.addInitParameter("protocolHeaderHttpsValue", v));
        Optional.ofNullable(properties.getRemoteIp().getHostHeader())
                .ifPresent(v -> registrationBean.addInitParameter("hostHeader", v));
        Optional.ofNullable(properties.getRemoteIp().getPortHeader())
                .ifPresent(v -> registrationBean.addInitParameter("portHeader", v));
        Optional.ofNullable(properties.getRemoteIp().getChangeLocalName())
                .ifPresent(v -> registrationBean.addInitParameter("changeLocalName", v));
        Optional.ofNullable(properties.getRemoteIp().getChangeLocalPort())
                .ifPresent(v -> registrationBean.addInitParameter("changeLocalPort", v));
        Optional.ofNullable(properties.getRemoteIp().getProxiesHeader())
                .ifPresent(v -> registrationBean.addInitParameter("proxiesHeader", v));
        Optional.ofNullable(properties.getRemoteIp().getRemoteIpHeader())
                .ifPresent(v -> registrationBean.addInitParameter("remoteIpHeader", v));
        Optional.ofNullable(properties.getRemoteIp().getTrustedProxies())
                .ifPresent(v -> registrationBean.addInitParameter("trustedProxies", v));
        Optional.ofNullable(properties.getRemoteIp().getHttpServerPort())
                .ifPresent(v -> registrationBean.addInitParameter("httpServerPort", v));
        Optional.ofNullable(properties.getRemoteIp().getHttpsServerPort())
                .ifPresent(v -> registrationBean.addInitParameter("httpsServerPort", v));
        Optional.ofNullable(properties.getRemoteIp().getEnableLookups())
                .ifPresent(v -> registrationBean.addInitParameter("enableLookups", v));

        registrationBean.setFilter(new RemoteIpFilter());
        registrationBean.setName(REMOTE_IP_FILTER);
        registrationBean.setOrder(
                orderProperties.getOrder().getOrDefault(REMOTE_IP_FILTER, properties.getOrder() + 10));
        return registrationBean;
    }

    /**
     * RemoteAddrFilter
     *
     * @return FilterRegistrationBean<RemoteAddrFilter>
     * @see <a href="https://tomcat.apache.org/tomcat-9.0-doc/config/filter.html#Remote_Address_Filter">RemoteAddrFilter</a>
     * @see RemoteIpFilter
     */
    @Bean
    @ConditionalOnClass(RemoteAddrFilter.class)
    @ConditionalOnMissingFilterBean(RemoteAddrFilter.class)
    @ConditionalOnProperty(prefix = WEB_SECURITY_REMOTE_ADDR_PROPERTY_NAME_PREFIX, name = PROPERTY_NAME_ENABLED, matchIfMissing = true)
    public FilterRegistrationBean<RemoteAddrFilter> remoteAddrFilter() {
        FilterRegistrationBean<RemoteAddrFilter> registrationBean = new FilterRegistrationBean<>();
        RemoteAddrFilter remoteAddrFilter = new RemoteAddrFilter();
        Optional.ofNullable(properties.getRemoteAddr().getAllow()).ifPresent(remoteAddrFilter::setAllow);
        Optional.ofNullable(properties.getRemoteAddr().getDeny()).ifPresent(remoteAddrFilter::setDeny);
        Optional.ofNullable(properties.getRemoteAddr().getDenyStatus())
                .ifPresent(v -> remoteAddrFilter.setDenyStatus(Integer.parseInt(v)));

        registrationBean.setFilter(remoteAddrFilter);
        registrationBean.setName(REMOTE_ADDR_FILTER);
        registrationBean.setOrder(
                orderProperties.getOrder().getOrDefault(REMOTE_ADDR_FILTER, properties.getOrder() + 20));
        return registrationBean;
    }

    /**
     * CorsFilter
     *
     * @return FilterRegistrationBean<CorsFilter>
     * @see <a href="https://tomcat.apache.org/tomcat-9.0-doc/config/filter.html#CORS_Filter">CorsFilter</a>
     * @see CorsFilter
     * @see org.springframework.web.filter.CorsFilter
     */
    @Bean
    @ConditionalOnClass(CorsFilter.class)
    @ConditionalOnMissingFilterBean(CorsFilter.class)
    @ConditionalOnProperty(prefix = WEB_SECURITY_CORS_PROPERTY_NAME_PREFIX, name = PROPERTY_NAME_ENABLED)
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
        Optional.ofNullable(properties.getCors().getAllowedOrigins())
                .ifPresent(v -> registrationBean.addInitParameter(PARAM_CORS_ALLOWED_ORIGINS, v));
        Optional.ofNullable(properties.getCors().getAllowedMethods())
                .ifPresent(v -> registrationBean.addInitParameter(PARAM_CORS_ALLOWED_METHODS, v));
        Optional.ofNullable(properties.getCors().getAllowedHeaders())
                .ifPresent(v -> registrationBean.addInitParameter(PARAM_CORS_ALLOWED_HEADERS, v));
        Optional.ofNullable(properties.getCors().getExposedHeaders())
                .ifPresent(v -> registrationBean.addInitParameter(PARAM_CORS_EXPOSED_HEADERS, v));
        Optional.ofNullable(properties.getCors().getPreflightMaxAge())
                .ifPresent(v -> registrationBean.addInitParameter(PARAM_CORS_PREFLIGHT_MAXAGE, v));
        Optional.ofNullable(properties.getCors().getSupportCredentials())
                .ifPresent(v -> registrationBean.addInitParameter(PARAM_CORS_SUPPORT_CREDENTIALS, v));
        Optional.ofNullable(properties.getCors().getRequestDecorate())
                .ifPresent(v -> registrationBean.addInitParameter(PARAM_CORS_REQUEST_DECORATE, v));

        registrationBean.setFilter(new CorsFilter());
        registrationBean.setName(CORS_FILTER);
        registrationBean.setOrder(orderProperties.getOrder().getOrDefault(CORS_FILTER, properties.getOrder() + 30));
        return registrationBean;
    }

    /**
     * RateLimitFilter
     *
     * @return FilterRegistrationBean<RateLimitFilter>
     * @see <a href="https://tomcat.apache.org/tomcat-9.0-doc/config/filter.html#Rate_Limit_Filter">RateLimitFilter</a>
     * @see RateLimitFilter
     */
    @Bean
    @ConditionalOnClass(RateLimitFilter.class)
    @ConditionalOnMissingFilterBean(RateLimitFilter.class)
    @ConditionalOnProperty(prefix = WEB_SECURITY_RATE_LIMIT_PROPERTY_NAME_PREFIX, name = PROPERTY_NAME_ENABLED, matchIfMissing = true)
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilter() {
        FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.addInitParameter(PARAM_BUCKET_DURATION, properties.getRateLimit().getBucketDuration());
        registrationBean.addInitParameter(PARAM_BUCKET_REQUESTS, properties.getRateLimit().getBucketRequests());
        registrationBean.addInitParameter(PARAM_ENFORCE, String.valueOf(properties.getRateLimit().isEnabled()));
        registrationBean.addInitParameter(PARAM_STATUS_CODE, properties.getRateLimit().getStatusCode());
        registrationBean.addInitParameter(PARAM_STATUS_MESSAGE, properties.getRateLimit().getStatusMessage());

        registrationBean.setFilter(new RateLimitFilter());
        registrationBean.setName(RATE_LIMIT_FILTER);
        registrationBean.setOrder(
                orderProperties.getOrder().getOrDefault(RATE_LIMIT_FILTER, properties.getOrder() + 40));

        return registrationBean;
    }

    /**
     * HttpHeaderSecurityFilter
     *
     * @return FilterRegistrationBean<HttpHeaderSecurityFilter>
     * @see <a href="https://tomcat.apache.org/tomcat-9.0-doc/config/filter.html#HTTP_Header_Security_Filter">HttpHeaderSecurityFilter</a>
     * @see HttpHeaderSecurityFilter
     */
    @Bean
    @ConditionalOnClass(HttpHeaderSecurityFilter.class)
    @ConditionalOnMissingFilterBean(HttpHeaderSecurityFilter.class)
    @ConditionalOnProperty(prefix = WEB_SECURITY_HTTP_HEADER_PROPERTY_NAME_PREFIX, name = PROPERTY_NAME_ENABLED, matchIfMissing = true)
    public FilterRegistrationBean<HttpHeaderSecurityFilter> httpHeadSecurityFilter() {
        FilterRegistrationBean<HttpHeaderSecurityFilter> registrationBean = new FilterRegistrationBean<>();
        HttpHeaderSecurityFilter headerSecurityFilter = new HttpHeaderSecurityFilter();
        headerSecurityFilter.setHstsEnabled(properties.getHttpHeader().isHstsEnabled());
        headerSecurityFilter.setHstsMaxAgeSeconds(properties.getHttpHeader().getHstsMaxAgeSeconds());
        headerSecurityFilter.setHstsIncludeSubDomains(properties.getHttpHeader().isHstsIncludeSubDomains());
        headerSecurityFilter.setHstsPreload(properties.getHttpHeader().isHstsPreload());
        headerSecurityFilter.setAntiClickJackingEnabled(properties.getHttpHeader().isAntiClickJackingEnabled());
        headerSecurityFilter.setAntiClickJackingOption(properties.getHttpHeader().getAntiClickJackingOption());
        Optional.ofNullable(properties.getHttpHeader().getAntiClickJackingUri())
                .ifPresent(headerSecurityFilter::setAntiClickJackingUri);
        headerSecurityFilter.setBlockContentTypeSniffingEnabled(
                properties.getHttpHeader().isBlockContentTypeSniffingEnabled());
        headerSecurityFilter.setXssProtectionEnabled(properties.getHttpHeader().isXssProtectionEnabled());

        registrationBean.setFilter(headerSecurityFilter);
        registrationBean.setName(HTTP_HEAD_SECURITY_FILTER);
        registrationBean.setOrder(
                orderProperties.getOrder().getOrDefault(HTTP_HEAD_SECURITY_FILTER, properties.getOrder() + 40));
        return registrationBean;
    }
}
