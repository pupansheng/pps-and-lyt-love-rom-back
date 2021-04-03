/*
 * Copyright (c) ACCA Corp.
 * All Rights Reserved.
 */
package com.pps.back.frame.pupansheng.core.http.myrequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.AsyncClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author pupansheng, 2021/3/29
 * @version OPRA v1.0
 */
public abstract class  AbstractAsyncClientHttpRequest implements AsyncClientHttpRequest {

    private final HttpHeaders headers = new HttpHeaders();

    private boolean executed = false;


    @Override
    public final HttpHeaders getHeaders() {
        return (this.executed ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers);
    }

    @Override
    public final OutputStream getBody() throws IOException {
        assertNotExecuted();
        return getBodyInternal(this.headers);
    }

    @Override
    public ListenableFuture<ClientHttpResponse> executeAsync() throws IOException {
        assertNotExecuted();
        ListenableFuture<ClientHttpResponse> result = executeInternal(this.headers);
        this.executed = true;
        return result;
    }

    /**
     * Asserts that this request has not been {@linkplain #executeAsync() executed} yet.
     * @throws IllegalStateException if this request has been executed
     */
    protected void assertNotExecuted() {
        Assert.state(!this.executed, "ClientHttpRequest already executed");
    }


    /**
     * Abstract template method that returns the body.
     * @param headers the HTTP headers
     * @return the body output stream
     */
    protected abstract OutputStream getBodyInternal(HttpHeaders headers) throws IOException;

    /**
     * Abstract template method that writes the given headers and content to the HTTP request.
     * @param headers the HTTP headers
     * @return the response object for the executed request
     */
    protected abstract ListenableFuture<ClientHttpResponse> executeInternal(HttpHeaders headers)
            throws IOException;

}
