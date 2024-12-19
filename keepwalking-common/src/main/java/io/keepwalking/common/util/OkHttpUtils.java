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

package io.keepwalking.common.util;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.keepwalking.common.exception.HttpRequestException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OkHttpUtils
 *
 * @author <a href="mailto:taofangf@gmail.com">Fangtao<a/>
 * @since 2024.11
 */
@Slf4j
public class OkHttpUtils {

    /**
     * 默认连接超时时间（秒）。如果没有配置此值，默认为 10 秒。
     */
    private static final long DEFAULT_CONNECT_TIMEOUT = Long.getLong("http.connect.timeout", 10L);

    /**
     * 默认读取超时时间（秒）。如果没有配置此值，默认为 30 秒。
     */
    private static final long DEFAULT_READ_TIMEOUT = Long.getLong("http.read.timeout", 30L);

    /**
     * 默认写入超时时间（秒）。如果没有配置此值，默认为 30 秒。
     */
    private static final long DEFAULT_WRITE_TIMEOUT = Long.getLong("http.write.timeout", 30L);

    /**
     * 默认最大空闲连接数。决定了连接池中最大可存储的空闲连接数。如果没有配置此值，默认为 10。
     */
    private static final int MAX_IDLE_CONNECTIONS = Integer.getInteger("http.max.idle.connections", 10);

    /**
     * 默认连接保持时间（分钟）。决定连接池中连接的最大保持时间。如果没有配置此值，默认为 5 分钟。
     */
    private static final long KEEP_ALIVE_DURATION = Long.getLong("http.keep.alive.duration", 5L);

    /**
     * 默认的 OkHttpClient 实例。用于执行所有的 HTTP 请求。
     * 在调用初始化方法后，该变量将持有一个 OkHttpClient 的实例。
     */
    @Getter
    private static OkHttpClient client;

    /**
     * 媒体类型常量：用于表示 JSON 格式的请求/响应体，默认字符集为 UTF-8。
     */
    public static final String APPLICATION_JSON = "application/json;charset=utf-8";

    /**
     * 媒体类型常量：用于表示表单 URL 编码格式的请求体，默认字符集为 UTF-8。
     */
    public static final String FORM_URLENCODED = "application/x-www-form-urlencoded;charset=utf-8";

    /**
     * 媒体类型常量：用于表示表单数据格式的请求体，通常用于文件上传。
     */
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    /**
     * 媒体类型常量：用于表示纯文本格式的请求体，默认字符集为 UTF-8。
     */
    public static final String TEXT_PLAIN = "text/plain;charset=utf-8";

    static {
        initialize(DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, DEFAULT_WRITE_TIMEOUT, MAX_IDLE_CONNECTIONS,
                KEEP_ALIVE_DURATION);
    }

    /**
     * 初始化 OkHttpClient 实例。
     *
     * @param connectTimeout 连接超时时间（秒）
     * @param readTimeout 读取超时时间（秒）
     * @param writeTimeout 写入超时时间（秒）
     * @param maxIdleConnections 最大空闲连接数
     * @param keepAliveDuration 连接保持时间（分钟）
     */
    public static void initialize(long connectTimeout, long readTimeout, long writeTimeout, int maxIdleConnections,
            long keepAliveDuration) {
        ConnectionPool connectionPool = new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.MINUTES);

        client = new OkHttpClient.Builder().connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .connectionPool(connectionPool)
                .build();
        log.info("OkHttpClient initialized with timeouts: {}s (connect), {}s (read), {}s (write), "
                        + "connection pool: max idle connections = {}, keep alive duration = {} minutes", connectTimeout,
                readTimeout, writeTimeout, maxIdleConnections, keepAliveDuration);
    }

    /**
     * 构建请求体。
     *
     * @param body 请求体内容
     * @param mediaType 请求体的媒体类型
     * @return 构建好的请求体
     */
    private static RequestBody buildRequestBody(String body, String mediaType) {
        MediaType type = MediaType.get(mediaType != null && !mediaType.isEmpty() ? mediaType : APPLICATION_JSON);
        return body == null || body.isEmpty() ? RequestBody.create("", type) : RequestBody.create(body, type);
    }

    /**
     * 构建 HTTP 请求。
     *
     * @param url 请求的 URL
     * @param queryParams 查询参数
     * @param body 请求体内容
     * @param mediaType 请求体的媒体类型
     * @param headers 请求头
     * @param method 请求方法（如 GET、POST 等）
     * @return 构建好的 Request 对象
     */
    private static Request buildRequest(String url, Map<String, String> queryParams, String body, String mediaType,
            Map<String, String> headers, String method) {
        // 将查询参数添加到 URL 中
        if (queryParams != null && !queryParams.isEmpty()) {
            HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
            url = urlBuilder.build().toString();
        }

        Request.Builder requestBuilder = new Request.Builder().url(url);

        // 添加请求头
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // 设置请求体
        RequestBody requestBody = buildRequestBody(body, mediaType);
        switch (method.toUpperCase()) {
            case "GET":
                requestBuilder.get();
                break;
            case "POST":
                requestBuilder.post(requestBody);
                break;
            case "PUT":
                requestBuilder.put(requestBody);
                break;
            case "DELETE":
                requestBuilder.delete(requestBody);
                break;
            default:
                log.error("Invalid HTTP method: {}", method);
                throw new IllegalArgumentException("Invalid HTTP method: " + method);
        }

        return requestBuilder.build();
    }

    /**
     * 执行 HTTP 请求并处理响应。
     *
     * @param url 请求 URL
     * @param queryParams 查询参数
     * @param body 请求体内容
     * @param mediaType 请求体的媒体类型
     * @param headers 请求头
     * @param method 请求方法（如 GET、POST 等）
     * @param handler 响应体处理器
     * @param <T> 响应体类型
     * @return 处理后的响应体
     * @throws HttpRequestException 如果请求失败，抛出异常
     */
    public static <T> T executeRequest(String url, Map<String, String> queryParams, String body, String mediaType,
            Map<String, String> headers, String method, ResponseHandler<T> handler) throws HttpRequestException {
        log.info("Executing request to {} with method {}", url, method);
        Request request = buildRequest(url, queryParams, body, mediaType, headers, method);

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                log.info("Request to {} succeeded with status code {}", url, response.code());
                return handler.handle(response);
            }
            else {
                log.error("Request to {} failed with status code {}", url, response.code());
                throw new HttpRequestException("Request failed with code: " + response.code());
            }
        }
        catch (IOException e) {
            if (e instanceof SocketTimeoutException) {
                log.error("Request to {} timed out", url);
                throw new HttpRequestException("Request timed out", e);
            }
            else {
                log.error("Request to {} failed due to IOException", url, e);
                throw new HttpRequestException("Request failed due to IOException", e);
            }
        }
    }

    /**
     * 发送 HTTP 请求并返回响应体（String 类型）
     *
     * @param url 请求 URL
     * @param queryParams 查询参数
     * @param body 请求体
     * @param mediaType 媒体类型
     * @param headers 请求头
     * @param method 请求方法（如 GET、POST 等）
     * @return HTTP 请求并返回响应体
     * @throws HttpRequestException 异常
     */
    private static String sendRequest(String url, Map<String, String> queryParams, String body, String mediaType,
            Map<String, String> headers, String method) throws HttpRequestException {
        return executeRequest(url, queryParams, body, mediaType, headers, method,
                response -> response.body() != null ? response.body().string() : null);
    }

    /**
     * 发送 GET 请求（简化版）
     *
     * @param url 请求 URL
     * @param queryParams 查询参数
     * @return GET 请求返回的响应体
     * @throws HttpRequestException 异常
     */
    public static String get(String url, Map<String, String> queryParams) throws HttpRequestException {
        return sendRequest(url, queryParams, null, APPLICATION_JSON, null, "GET");
    }

    /**
     * 发送 GET 请求
     *
     * @param url 请求 URL
     * @param queryParams 查询参数
     * @param headers 请求头
     * @return GET 请求返回的响应体
     * @throws HttpRequestException 异常
     */
    public static String get(String url, Map<String, String> queryParams, Map<String, String> headers)
            throws HttpRequestException {
        return sendRequest(url, queryParams, null, APPLICATION_JSON, headers, "GET");
    }

    /**
     * 发送 POST 请求（简化版）
     *
     * @param url 请求 URL
     * @param body 请求体
     * @return POST 请求返回的响应体
     * @throws HttpRequestException 异常
     */
    public static String post(String url, String body) throws HttpRequestException {
        return post(url, body, APPLICATION_JSON);
    }

    /**
     * 发送 POST 请求（简化版）
     *
     * @param url 请求 URL
     * @param body 请求体
     * @param mediaType 媒体类型
     * @return POST 请求返回的响应体
     * @throws HttpRequestException 异常
     */
    public static String post(String url, String body, String mediaType) throws HttpRequestException {
        return sendRequest(url, null, body, mediaType, null, "POST");
    }

    /**
     * 发送 POST 请求
     *
     * @param url 请求 URL
     * @param queryParams 查询参数
     * @param body 请求体
     * @param mediaType 媒体类型
     * @param headers 请求头
     * @return POST 请求返回的响应体
     * @throws HttpRequestException 异常
     */
    public static String post(String url, Map<String, String> queryParams, String body, String mediaType,
            Map<String, String> headers) throws HttpRequestException {
        return sendRequest(url, queryParams, body, mediaType, headers, "POST");
    }

    /**
     * 执行异步 HTTP 请求
     */
    public static void executeRequestAsync(String url, Map<String, String> queryParams, String body, String mediaType,
            Map<String, String> headers, String method, Callback callback) {
        log.info("Executing asynchronous request to {} with method {}", url, method);
        Request request = buildRequest(url, queryParams, body, mediaType, headers, method);
        client.newCall(request).enqueue(callback);
    }

    /**
     * 响应体处理接口
     */
    public interface ResponseHandler<T> {
        /**
         * 自定义返回处理
         *
         * @param response HTTP 返回
         * @return 响应处理
         * @throws IOException 异常
         */
        T handle(Response response) throws IOException;
    }
}



