package com.qsx.parking.utils;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 可重复读取的HTTP请求包装类 | 通过缓存请求体内容，使其可以被多次读取
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
public class RepeatableHttpServletRequest extends HttpServletRequestWrapper {

    /**
     * 请求体内容的字节数组
     */
    private final byte[] body;

    /**
     * 构造函数
     * 读取原始请求的内容并保存
     * @param request 原始HTTP请求
     */
    public RepeatableHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        body = request.getInputStream().readAllBytes();
    }

    /**
     * 获取请求体输入流
     * 每次调用都返回包含完整请求体内容的新输入流
     */
    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStream() {
            private int lastIndexRetrieved = -1;
            private ReadListener readListener = null;

            @Override
            public boolean isFinished() {
                return lastIndexRetrieved == body.length - 1;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                this.readListener = readListener;
                if (!isFinished()) {
                    try {
                        readListener.onDataAvailable();
                    } catch (IOException e) {
                        readListener.onError(e);
                    }
                } else {
                    try {
                        readListener.onAllDataRead();
                    } catch (IOException e) {
                        readListener.onError(e);
                    }
                }
            }

            @Override
            public int read() {
                int i;
                if (!isFinished()) {
                    i = body[lastIndexRetrieved + 1];
                    lastIndexRetrieved++;
                    if (lastIndexRetrieved == body.length - 1) {
                        try {
                            if (readListener != null) {
                                readListener.onAllDataRead();
                            }
                        } catch (IOException e) {
                            if (readListener != null) {
                                readListener.onError(e);
                            }
                        }
                    }
                    return i;
                } else {
                    return -1;
                }
            }
        };
    }

    /**
     * 获取请求体Reader
     * 每次调用都返回包含完整请求体内容的新Reader
     */
    @Override
    public BufferedReader getReader() {
        ByteArrayInputStream is = new ByteArrayInputStream(body);
        return new BufferedReader(new InputStreamReader(is));
    }
}
