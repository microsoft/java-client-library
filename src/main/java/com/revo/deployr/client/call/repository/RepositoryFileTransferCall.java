/*
 * RepositoryFileTransferCall.java
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
package com.revo.deployr.client.call.repository;

import com.revo.deployr.client.call.AbstractCall;
import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.core.REndpoints;
import com.revo.deployr.client.params.RepoUploadOptions;

import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Provides support for DeployR API call: /r/repository/file/transfer.
 * <p/>
 * Simply construct an instance of this call and pass it on the
 * execute() method of your {@link com.revo.deployr.client.RClient}.
 */
public class RepositoryFileTransferCall extends AbstractCall
        implements Callable<RCoreResult> {

    private final String API = REndpoints.RREPOSITORYFILETRANSFER;

    public RepositoryFileTransferCall(URL url, RepoUploadOptions options) {
        httpParams.put("filename", options.filename);
        httpParams.put("directory", options.directory);
        httpParams.put("descr", options.descr);
        httpParams.put("url", url.toString());
        httpParams.put("newversion", Boolean.toString(options.newversion));
        httpParams.put("newversionmsg", options.newversionmsg);
        httpParams.put("restricted", options.restricted);
        httpParams.put("shared", Boolean.toString(options.shared));
        httpParams.put("published", Boolean.toString(options.published));
        if (options.inputs != null)
            httpParams.put("inputs", options.inputs);
        if (options.outputs != null)
            httpParams.put("outputs", options.outputs);
        httpParams.put("format", "json");
    }

    /**
     * Internal use only, to execute call use RClient.execute().
     */
    public RCoreResult call() {

        return makePostRequest(API);
    }

}
