/*
 * ProjectImportCall.java
 *
 * Copyright (C) 2010-2016, Microsoft Corporation
 *
 * This program is licensed to you under the terms of Version 2.0 of the
 * Apache License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * Apache License 2.0 (http://www.apache.org/licenses/LICENSE-2.0) for more details.
 *
 */
package com.revo.deployr.client.call.project;

import com.revo.deployr.client.call.AbstractCall;
import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.core.REndpoints;
import com.revo.deployr.client.core.impl.RCoreResultImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Provides support for DeployR API call: /r/project/import.
 * <p/>
 * Simply construct an instance of this call and pass it on the
 * execute() method of your {@link com.revo.deployr.client.RClient}.
 */
public class ProjectImportCall extends AbstractCall
        implements Callable<RCoreResult> {

    private Log log = LogFactory.getLog(ProjectImportCall.class);

    private InputStream fileStream;
    private String descr;

    private final String API = REndpoints.RPROJECTIMPORT;

    public ProjectImportCall(InputStream fileStream, String descr) {
        this.fileStream = fileStream;
        this.descr = descr;
    }

    /**
     * Internal use only, to execute call use RClient.execute().
     */
    public RCoreResult call() {

        RCoreResultImpl pResult = null;

        try {

            HttpPost httpPost = new HttpPost(serverUrl + API);
            super.httpUriRequest = httpPost;

            List<NameValuePair> postParams = new ArrayList<NameValuePair>();
            postParams.add(new BasicNameValuePair("format", "json"));

            MultipartEntity entity =
                    new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            entity.addPart("file", new InputStreamBody(((InputStream) fileStream), "application/zip"));
            entity.addPart("descr", new StringBody(descr, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("format", new StringBody("json", "text/plain", Charset.forName("UTF-8")));

            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            HttpEntity responseEntity = response.getEntity();
            String markup = EntityUtils.toString(responseEntity);

            pResult = new RCoreResultImpl();
            pResult.parseMarkup(markup, API, statusLine.getStatusCode(), statusLine.getReasonPhrase());

        } catch (UnsupportedEncodingException ueex) {
            log.warn("ProjectImportCall: unsupported encoding exception.", ueex);
        } catch (IOException ioex) {
            log.warn("ProjectImportCall: io exception.", ioex);
        }

        return pResult;
    }

}
