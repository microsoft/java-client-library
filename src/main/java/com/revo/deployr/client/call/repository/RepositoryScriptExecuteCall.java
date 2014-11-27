/*
 * RepositoryScriptExecuteCall.java
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

import com.revo.deployr.client.RDataException;
import com.revo.deployr.client.call.StandardExecutionModelCall;
import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.core.REndpoints;
import com.revo.deployr.client.params.AnonymousProjectExecutionOptions;

import java.util.concurrent.Callable;

/**
 * Provides support for DeployR API call: /r/repository/script/execute.
 * <p/>
 * Simply construct an instance of this call and pass it on the
 * execute() method of your {@link com.revo.deployr.client.RClient}.
 */
public class RepositoryScriptExecuteCall extends StandardExecutionModelCall
        implements Callable<RCoreResult> {

    private final String API = REndpoints.RREPOSITORYSCRIPTEXECUTE;

    public RepositoryScriptExecuteCall(String scriptName,
                                       String scriptDirectory,
                                       String scriptAuthor,
                                       String scriptVersion,
                                       String externalSource,
                                       AnonymousProjectExecutionOptions options)
            throws RDataException {

        super(options);

        httpParams.put("filename", scriptName);
        httpParams.put("directory", scriptDirectory);
        httpParams.put("author", scriptAuthor);
        httpParams.put("version", scriptVersion);
        httpParams.put("externalsource", externalSource);

        if (options != null) {
            // HTTP Blackbox Project Support.
            httpParams.put("blackbox", Boolean.toString(options.blackbox));
            httpParams.put("recycle", Boolean.toString(options.recycle));
        }
    }

    /**
     * Internal use only, to execute call use RClient.execute().
     */
    public RCoreResult call() {

        return makePostRequest(API);
    }

}
