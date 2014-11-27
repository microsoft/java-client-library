/*
 * ProjectWorkspaceGetCall.java
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

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Provides support for DeployR API call: /r/project/workspace/get.
 * <p/>
 * Simply construct an instance of this call and pass it on the
 * execute() method of your {@link com.revo.deployr.client.RClient}.
 */
public class ProjectWorkspaceGetCall extends AbstractCall
        implements Callable<RCoreResult> {

    private final String API = REndpoints.RPROJECTWORKSPACEGET;

    public ProjectWorkspaceGetCall(String project, List<String> objects,
                                   boolean encodeDataFramePrimitiveAsVector) {
        httpParams.put("project", project);

        String objectNames = null;
        if (objects != null) {
            for (String object : objects) {
                if (objectNames != null) {
                    objectNames = objectNames + "," + object;
                } else {
                    objectNames = object;
                }
            }
        }
        httpParams.put("name", objectNames);
        httpParams.put("encodeDataFramePrimitiveAsVector", Boolean.toString(encodeDataFramePrimitiveAsVector));

        httpParams.put("format", "json");
    }

    /**
     * Internal use only, to execute call use RClient.execute().
     */
    public RCoreResult call() {

        return makeGetRequest(API);
    }

}
