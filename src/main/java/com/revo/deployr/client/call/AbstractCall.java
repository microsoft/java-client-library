/*
 * AbstractCall.java
 *
 * Copyright (C) 2010-2014 by Revolution Analytics Inc.
 *
 * This program is licensed to you under the terms of Version 2.0 of the
 * Apache License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * Apache License 2.0 (http://www.apache.org/licenses/LICENSE-2.0) for more details.
 *
 */
package com.revo.deployr.client.call;

import com.revo.deployr.client.core.RCoreResponse;
import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.core.RExecutionException;
import com.revo.deployr.client.core.RInterruptedException;
import com.revo.deployr.client.core.impl.RCoreResultImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Provides abstract base class for PCall implementations.
 */
public abstract class AbstractCall implements RCall, RCoreResponse {

    private Log log = LogFactory.getLog(AbstractCall.class);

    protected HttpClient httpClient;
    protected String serverUrl;
    protected Future future;
    protected HttpUriRequest httpUriRequest;


    protected Map<String, String> httpParams = new HashMap();

    /**
     * Method, internal use only.
     */
    public void setClient(HttpClient httpClient, String serverUrl) {
        this.httpClient = httpClient;
        this.serverUrl = serverUrl;
    }

    /**
     * Method, internal use only.
     */
    public void setFuture(Future future) {
        this.future = future;
    }

    /*
     * RCall Implementation - Interface Marker Only, No Implementation.
     */

    /*
     * RCoreResponse Implementation
     */

    public RCoreResult get() throws RInterruptedException,
            RExecutionException {

        RCoreResult pResult = null;

        try {
            pResult = (RCoreResult) future.get();
        } catch (InterruptedException iex) {
            log.warn("Interrupted exception on call, httpUriRequest=" + httpUriRequest);
            try {
                httpUriRequest.abort();
                log.warn("Interrupted exception, call aborted.");
            } catch (UnsupportedOperationException uoe) {
                log.warn("Interrupted, unsupported operation exception.", uoe);
            }
            if (httpUriRequest.isAborted())
                throw new RInterruptedException("InterruptedException, call aborted.");
            else
                throw new RInterruptedException("InterruptedException, call not aborted.");
        } catch (ExecutionException eex) {
            throw new RExecutionException("ExecutionException, call aborted.", eex);
        }

        return pResult;
    }


    public boolean cancel() {
        return future.cancel(true);
    }

    public boolean isCancelled() {
        return future.isCancelled();
    }

    public boolean isCompleted() {
        return future.isDone();
    }

    /*
     * Protected implementation making HTTP Request.
     */

    protected RCoreResult makePostRequest(String API) {

        try {

            HttpPost httpPost = new HttpPost(serverUrl + API);

            List<NameValuePair> postParams = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : httpParams.entrySet()) {
                postParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParams, "UTF-8");
            httpPost.setEntity(entity);

            return makeRequest(httpPost, API);

        } catch (UnsupportedEncodingException uex) {
            log.warn("AbstractCall: makePostRequest unsupported encoding exception.", uex);
            return null;
        }
    }

    protected RCoreResult makeGetRequest(String API) {

        List<NameValuePair> getParams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : httpParams.entrySet()) {
            getParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        String encodedParams = URLEncodedUtils.format(getParams, "UTF-8");
        HttpGet httpGet = new HttpGet(serverUrl + API + "?" + encodedParams);

        return makeRequest(httpGet, API);
    }

    protected RCoreResult makeRequest(HttpUriRequest httpUriRequest, String API) {

        this.httpUriRequest = httpUriRequest;

        RCoreResultImpl pResult = null;

        try {

            HttpResponse response = httpClient.execute(this.httpUriRequest);
            StatusLine statusLine = response.getStatusLine();

            HttpEntity responseEntity = response.getEntity();
            String markup = EntityUtils.toString(responseEntity);

            pResult = new RCoreResultImpl();

            try {
                pResult.parseMarkup(markup, API, statusLine.getStatusCode(), statusLine.getReasonPhrase());
            } catch (Throwable tex) {
                log.warn("AbstractCall: makeRequest pResult.parseMarkup throwable=" + tex);
                throw tex;
            }

        } catch (UnsupportedEncodingException ueex) {
            log.warn("AbstractCall: makeRequest unsupported encoding exception=" + ueex);
        } catch (IOException ioex) {
            log.warn("AbstractCall: makeRequest io exception=" + ioex);
        } catch (Exception ex) {
            log.warn("AbstractCall: makeRequest exception=" + ex);
        } finally {
            log.debug("AbstractCall: makeRequest pResult=" + pResult);
        }

        return pResult;
    }

}
