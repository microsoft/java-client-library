/*
 * RepositoryFileUploadCall.java
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
package com.revo.deployr.client.call.repository;

import com.revo.deployr.client.call.AbstractCall;
import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.core.REndpoints;
import com.revo.deployr.client.core.impl.RCoreResultImpl;
import com.revo.deployr.client.params.RepoUploadOptions;
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
 * Provides support for DeployR API call: /r/repository/file/upload.
 * <p/>
 * Simply construct an instance of this call and pass it on the
 * execute() method of your {@link com.revo.deployr.client.RClient}.
 */
public class RepositoryFileUploadCall extends AbstractCall
        implements Callable<RCoreResult> {

    private Log log = LogFactory.getLog(RepositoryFileUploadCall.class);

    private InputStream fileStream;
    private RepoUploadOptions options;

    private final String API = REndpoints.RREPOSITORYFILEUPLOAD;

    public RepositoryFileUploadCall(InputStream fileStream, RepoUploadOptions options) {
        this.fileStream = fileStream;
        this.options = options;
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
            if (options.filename != null)
                entity.addPart("filename", new StringBody(options.filename, "text/plain", Charset.forName("UTF-8")));
            if (options.directory != null)
                entity.addPart("directory", new StringBody(options.directory, "text/plain", Charset.forName("UTF-8")));
            if (options.descr != null)
                entity.addPart("descr", new StringBody(options.descr, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("newversion", new StringBody(Boolean.toString(options.newversion), "text/plain", Charset.forName("UTF-8")));
            if (options.newversionmsg != null)
                entity.addPart("newversionmsg", new StringBody(options.newversionmsg, "text/plain", Charset.forName("UTF-8")));
            if (options.restricted != null)
                entity.addPart("restricted", new StringBody(options.restricted, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("shared", new StringBody(Boolean.toString(options.shared), "text/plain", Charset.forName("UTF-8")));
            entity.addPart("published", new StringBody(Boolean.toString(options.published), "text/plain", Charset.forName("UTF-8")));
            if (options.inputs != null)
                entity.addPart("inputs", new StringBody(options.inputs, "text/plain", Charset.forName("UTF-8")));
            if (options.outputs != null)
                entity.addPart("outputs", new StringBody(options.outputs, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("format", new StringBody("json", "text/plain", Charset.forName("UTF-8")));

            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            HttpEntity responseEntity = response.getEntity();
            String markup = EntityUtils.toString(responseEntity);

            pResult = new RCoreResultImpl();
            pResult.parseMarkup(markup, API, statusLine.getStatusCode(), statusLine.getReasonPhrase());

        } catch (UnsupportedEncodingException ueex) {
            log.warn("RepositoryFileUploadCall: unsupported encoding exception.", ueex);
        } catch (IOException ioex) {
            log.warn("RepositoryFileUploadCall: io exception.", ioex);
        }

        return pResult;
    }

}
