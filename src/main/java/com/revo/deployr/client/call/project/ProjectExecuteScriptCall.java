/*
 * ProjectExecuteScriptCall.java
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

import com.revo.deployr.client.RDataException;
import com.revo.deployr.client.call.StandardExecutionModelCall;
import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.core.REndpoints;
import com.revo.deployr.client.params.ProjectExecutionOptions;

import java.util.concurrent.Callable;

/**
 * Provides support for DeployR API call: /r/project/execute/script.
 * <p/>
 * Simply construct an instance of this call and pass it on the
 * execute() method of your {@link com.revo.deployr.client.RClient}.
 */
public class ProjectExecuteScriptCall extends StandardExecutionModelCall
        implements Callable<RCoreResult> {

    private final String API = REndpoints.RPROJECTEXECUTESCRIPT;

    public ProjectExecuteScriptCall(String project,
                                    String scriptName,
                                    String scriptDirectory,
                                    String scriptAuthor,
                                    String scriptVersion,
                                    String externalSource,
                                    ProjectExecutionOptions options) throws RDataException {

        super(options);

        httpParams.put("project", project);
        httpParams.put("filename", scriptName);
        httpParams.put("directory", scriptDirectory);
        httpParams.put("author", scriptAuthor);
        httpParams.put("version", scriptVersion);
        httpParams.put("externalsource", externalSource);
    }

    /**
     * Internal use only, to execute call use RClient.execute().
     */
    public RCoreResult call() {

        return makePostRequest(API);
    }

}
