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

package io.keepwalking.autoconfigure.security.web.servlet;

import io.keepwalking.autoconfigure.BaseConfig;

import static io.keepwalking.autoconfigure.Constants.WEB_SECURITY_PROPERTY_NAME_PREFIX;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * WebSecurityProperties
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @see <a href="https://tomcat.apache.org/tomcat-9.0-doc/config/filter.html">Apache Tomcat filter.html</a>
 * @since 2024.07
 */
@Getter
@Setter
@ConfigurationProperties(prefix = WEB_SECURITY_PROPERTY_NAME_PREFIX)
public class WebSecurityProperties extends BaseConfig {

    /**
     * web security base order
     */
    private Integer order = Integer.MIN_VALUE;

    /**
     * remoteIp
     */
    private RemoteIp remoteIp = new RemoteIp();

    /**
     * remoteAddr
     */
    private RemoteAddr remoteAddr = new RemoteAddr();

    /**
     * cors
     */
    private Cors cors = new Cors();

    /**
     * rateLimit
     */
    private RateLimit rateLimit = new RateLimit();

    /**
     * httpHeader
     */
    private HttpHeaderSecurity httpHeader = new HttpHeaderSecurity();

    /**
     * RemoteIpFilter config
     * <pre>
     *     {@code
     *     <filter>
     *        <filter-name>RemoteIpFilter</filter-name>
     *        <filter-class>org.apache.catalina.filters.RemoteIpFilter</filter-class>
     *        <init-param>
     *          <param-name>allowedInternalProxies</param-name>
     *          <param-value>192\.168\.0\.10|192\.168\.0\.11</param-value>
     *        </init-param>
     *        <init-param>
     *          <param-name>remoteIpHeader</param-name>
     *          <param-value>x-forwarded-for</param-value>
     *        </init-param>
     *        <init-param>
     *          <param-name>remoteIpProxiesHeader</param-name>
     *          <param-value>x-forwarded-by</param-value>
     *        </init-param>
     *        <init-param>
     *          <param-name>trustedProxies</param-name>
     *          <param-value>proxy1|proxy2</param-value>
     *        </init-param>
     *      </filter>
     *     }
     * </pre>
     *
     * @see org.apache.catalina.filters.RemoteIpFilter
     * @see <a href="https://tomcat.apache.org/tomcat-9.0-doc/config/filter.html#Remote_IP_Filter">Remote_IP_Filter</a>
     */
    @Getter
    @Setter
    static class RemoteIp extends BaseConfig {
        /**
         * Regular expression (using java.util.regex) that a proxy's IP address must match to be considered an internal proxy.
         * Internal proxies that appear in the remoteIpHeader will be trusted and will not appear in the proxiesHeader value.
         * If not specified the default value of 10\.\d{1,3}\.\d{1,3}\.\d{1,3}|192\.168\.\d{1,3}\.\d{1,3}|169\.254\.\d{1,3}\.\d{1,3}|127\.\d{1,3}\.\d{1,3}\.\d{1,3}|100\.6[4-9]{1}\.\d{1,3}\.\d{1,3}|100\.[7-9]{1}\d{1}\.\d{1,3}\.\d{1,3}|100\.1[0-1]{1}\d{1}\.\d{1,3}\.\d{1,3}|100\.12[0-7]{1}\.\d{1,3}\.\d{1,3}|172\.1[6-9]{1}\.\d{1,3}\.\d{1,3}|172\.2[0-9]{1}\.\d{1,3}\.\d{1,3}|172\.3[0-1]{1}\.\d{1,3}\.\d{1,3}|0:0:0:0:0:0:0:1 will be used.
         */
        private String internalProxies;

        /**
         * Name of the HTTP Header read by this valve that holds the protocol used by the client to connect to the proxy.
         * If not specified, the default of X-Forwarded-Proto is used.
         */
        private String protocolHeader;

        /**
         * Value of the protocolHeader to indicate that it is an HTTPS request.
         * If not specified, the default of https is used.
         */
        private String protocolHeaderHttpsValue;

        /**
         * Name of the HTTP Header read by this valve that holds the host used by the client to connect to the proxy.
         * If not specified, the default of null is used.
         */
        private String hostHeader;

        /**
         * Name of the HTTP Header read by this valve that holds the port used by the client to connect to the proxy.
         * If not specified, the default of null is used.
         */
        private String portHeader;

        /**
         * If true, the value returned by ServletRequest.getLocalName() and ServletRequest.getServerName() is modified by the this filter.
         * If not specified, the default of false is used.
         */
        private String changeLocalName;

        /**
         * If true, the value returned by ServletRequest.getLocalPort() and ServletRequest.getServerPort() is modified by the this filter.
         * If not specified, the default of false is used.
         */
        private String changeLocalPort;

        /**
         * Name of the HTTP header created by this valve to hold the list of proxies that have been processed in the incoming remoteIpHeader.
         * If not specified, the default of X-Forwarded-By is used.
         */
        private String proxiesHeader;

        /**
         * Name of the HTTP Header read by this valve that holds the list of traversed IP addresses starting from the requesting client.
         * If not specified, the default of X-Forwarded-For is used.
         */
        private String remoteIpHeader;

        /**
         * Regular expression (using java.util.regex) that a proxy's IP address must match to be considered an trusted proxy.
         * Trusted proxies that appear in the remoteIpHeader will be trusted and will appear in the proxiesHeader value.
         * If not specified, no proxies will be trusted.
         */
        private String trustedProxies;

        /**
         * Value returned by ServletRequest.getServerPort() when the protocolHeader indicates http protocol and no portHeader is present.
         * If not specified, the default of 80 is used.
         */
        private String httpServerPort;

        /**
         * Value returned by ServletRequest.getServerPort() when the protocolHeader indicates https protocol and no portHeader is present.
         * If not specified, the default of 443 is used.
         */
        private String httpsServerPort;

        /**
         * Should a DNS lookup be performed to provide a host name when calling ServletRequest#getRemoteHost().
         * If not specified, the default of false is used.
         */
        private String enableLookups;
    }

    /**
     * RemoteAddrFilter Config
     * <pre>
     *     {@code
     *     <filter>
     *       <filter-name>Remote Address Filter</filter-name>
     *       <filter-class>org.apache.catalina.filters.RemoteAddrFilter</filter-class>
     *       <init-param>
     *         <param-name>allow</param-name>
     *         <param-value>127\.\d+\.\d+\.\d+|::1|0:0:0:0:0:0:0:1</param-value>
     *       </init-param>
     *     </filter>
     *     }
     * </pre>
     *
     * @see org.apache.catalina.filters.RemoteAddrFilter
     * @see <a href="https://tomcat.apache.org/tomcat-9.0-doc/config/filter.html#Remote_Address_Filter">Remote_Address_Filter</a>
     */
    @Getter
    @Setter
    static class RemoteAddr extends BaseConfig {
        /**
         * A regular expression (using java.util.regex) that the remote client's IP address is compared to.
         * If this attribute is specified, the remote address MUST match for this request to be accepted.
         * If this attribute is not specified, all requests will be accepted UNLESS the remote address matches a deny pattern.
         */
        private String allow;

        /**
         * A regular expression (using java.util.regex) that the remote client's IP address is compared to.
         * If this attribute is specified, the remote address MUST NOT match for this request to be accepted.
         * If this attribute is not specified, request acceptance is governed solely by the accept attribute.
         */
        private String deny;

        /**
         * HTTP response status code that is used when rejecting denied request.
         * The default value is 403. For example, it can be set to the value 404.
         */
        private String denyStatus;
    }

    /**
     * CorsFilter Config
     *
     * @see <a href="https://tomcat.apache.org/tomcat-9.0-doc/config/filter.html#CORS_Filter">CORS_Filter</a>
     * @see org.apache.catalina.filters.CorsFilter
     */
    @Getter
    @Setter
    static class Cors extends BaseConfig {
        /**
         * A list of origins that are allowed to access the resource.
         * A * can be specified to enable access to resource from any origin. Otherwise, an allow list of comma separated origins can be provided. Eg: https://www.w3.org, https://www.apache.org.
         * Defaults: The empty String. (No origin is allowed to access the resource).
         */
        private String allowedOrigins;

        /**
         * A comma separated list of HTTP methods that can be used to access the resource, using cross-origin requests.
         * These are the methods which will also be included as part of Access-Control-Allow-Methods header in pre-flight response. Eg: GET, POST.
         * Defaults: GET, POST, HEAD, OPTIONS
         */
        private String allowedMethods;

        /**
         * A comma separated list of request headers that can be used when making an actual request.
         * These headers will also be returned as part of Access-Control-Allow-Headers header in a pre-flight response. Eg: Origin,Accept.
         * Defaults: Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers
         */
        private String allowedHeaders;

        /**
         * A comma separated list of headers other than simple response headers that browsers are allowed to access.
         * These are the headers which will also be included as part of Access-Control-Expose-Headers header in the pre-flight response. Eg: X-CUSTOM-HEADER-PING,X-CUSTOM-HEADER-PONG.
         * Default: None. Non-simple headers are not exposed by default.
         */
        private String exposedHeaders;

        /**
         * The amount of seconds, browser is allowed to cache the result of the pre-flight request.
         * This will be included as part of Access-Control-Max-Age header in the pre-flight response. A negative value will prevent CORS Filter from adding this response header to pre-flight response.
         * Defaults: 1800
         */
        private String preflightMaxAge;

        /**
         * A flag that indicates whether the resource supports user credentials.
         * This flag is exposed as part of Access-Control-Allow-Credentials header in a pre-flight response. It helps browser determine whether or not an actual request can be made using credentials.
         * Defaults: false
         */
        private String supportCredentials;

        /**
         * A flag to control if CORS specific attributes should be added to HttpServletRequest object or not.
         * Defaults: true
         */
        private String requestDecorate;
    }

    /**
     * RateLimitFilter Config
     *
     * @see org.apache.catalina.filters.RateLimitFilter
     * @see <a href="https://tomcat.apache.org/tomcat-9.0-doc/config/filter.html#Rate_Limit_Filter">Rate_Limit_Filter</a>
     */
    @Getter
    @Setter
    static class RateLimit extends BaseConfig {
        /**
         * The number of seconds in a time bucket. Default is 60.
         */
        private String bucketDuration = "60";

        /**
         * The number of requests that are allowed in a time bucket. Default is 300.
         */
        private String bucketRequests = "300";

        /**
         * The status code to return when a request is dropped. Default is 429.
         */
        private String statusCode = "429";

        /**
         * The status message to return when a request is dropped. Default is "Too many requests".
         */
        private String statusMessage = "Too many requests";
    }

    /**
     * HttpHeaderSecurityFilter Config
     *
     * @see <a href="https://tomcat.apache.org/tomcat-9.0-doc/config/filter.html#HTTP_Header_Security_Filter">HTTP_Header_Security_Filter</a>
     * @see org.apache.catalina.filters.HttpHeaderSecurityFilter
     */
    @Getter
    @Setter
    static class HttpHeaderSecurity extends BaseConfig {
        /**
         * Will an HTTP Strict Transport Security (HSTS) header (Strict-Transport-Security) be set on the response for secure requests. Any HSTS header already present will be replaced.
         * See RFC 6797 for further details of HSTS.
         * If not specified, the default value of true will be used.
         *
         * @see <a href="https://datatracker.ietf.org/doc/html/rfc6797">RFC 6797</a>
         */
        private boolean hstsEnabled = true;

        /**
         * The max age value that should be used in the HSTS header. Negative values will be treated as zero.
         * If not specified, the default value of 0 will be used.
         */
        private int hstsMaxAgeSeconds = 0;

        /**
         * Should the includeSubDomains parameter be included in the HSTS header.
         * If not specified, the default value of false will be used.
         */
        private boolean hstsIncludeSubDomains = false;

        /**
         * Should the preload parameter be included in the HSTS header.
         * If not specified, the default value of false will be used.
         *
         * @see <a href="https://hstspreload.org">hstsPreload</a> for important information about this parameter.
         */
        private boolean hstsPreload = false;

        /**
         * Should the anti click-jacking header (X-Frame-Options) be set on the response.
         * Any anti click-jacking header already present will be replaced.
         * If not specified, the default value of true will be used.
         */
        private boolean antiClickJackingEnabled = true;

        /**
         * What value should be used for the anticlick-jacking header? Must be one of DENY, SAMEORIGIN, ALLOW-FROM (case-insensitive).
         * If not specified, the default value of DENY will be used.
         */
        private String antiClickJackingOption = "DENY";

        /**
         * If ALLOW-FROM is used for antiClickJackingOption, what URI should be allowed?
         * If not specified, the default value of an empty string will be used.
         */
        private String antiClickJackingUri;

        /**
         * Should the header that blocks content type sniffing (X-Content-Type-Options) be set on every response. If already present, the header will be replaced.
         * If not specified, the default value of true will be used.
         */
        private boolean blockContentTypeSniffingEnabled = true;

        /**
         * <pre>
         *     <b>Note: This setting is deprecated as support for the HTTP header has been removed from all major browsers. The setting has been removed in Tomcat 11.0.x onwards.</b>
         * </pre>
         * Should the header that enables the browser's cross-site scripting filter protection (X-XSS-Protection: 1; mode=block) be set on every response. If already present, the header will be replaced.
         * If not specified, the default value of false will be used.
         */
        @Deprecated
        private boolean xssProtectionEnabled = false;
    }
}
