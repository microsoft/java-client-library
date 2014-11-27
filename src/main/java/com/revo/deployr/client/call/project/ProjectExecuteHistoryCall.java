/*
 * ProjectExecuteHistoryCall.java
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
package com.revo.deployr.client.call.project;

import com.revo.deployr.client.call.AbstractCall;
import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.core.REndpoints;
import com.revo.deployr.client.params.ProjectHistoryOptions;

import java.util.concurrent.Callable;

/**
 * Provides support for DeployR API call: /r/project/execute/history.
 * <p/>
 * Simply construct an instance of this call and pass it on the
 * execute() method of your {@link com.revo.deployr.client.RClient}.
 */
public class ProjectExecuteHistoryCall extends AbstractCall
        implements Callable<RCoreResult> {

    private final String API = REndpoints.RPROJECTEXECUTEHISTORY;

    public ProjectExecuteHistoryCall(String project, ProjectHistoryOptions options) {
        httpParams.put("project", project);

        if (options != null) {
            httpParams.put("filtertag", options.tagFilter);
            if (options.depthFilter < 1 || options.depthFilter > 500)
                options.depthFilter = 250;
            httpParams.put("filterdepth", Integer.toString(options.depthFilter));
            httpParams.put("reversed", Boolean.toString(options.reversed));
        } else {
            httpParams.put("filterdepth", "250");
        }
        httpParams.put("format", "json");
    }

    /**
     * Internal use only, to execute call use RClient.execute().
     */
    public RCoreResult call() {

        return makePostRequest(API);
    }

}
