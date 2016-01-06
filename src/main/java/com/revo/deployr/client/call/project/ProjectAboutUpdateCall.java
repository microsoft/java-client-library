/*
 * ProjectAboutUpdateCall.java
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

import com.revo.deployr.client.about.RProjectDetails;
import com.revo.deployr.client.call.AbstractCall;
import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.core.REndpoints;

import java.util.concurrent.Callable;

/**
 * Provides support for DeployR API call: /r/project/about/update.
 * <p/>
 * Simply construct an instance of this call and pass it on the
 * execute() method of your {@link com.revo.deployr.client.RClient}.
 */
public class ProjectAboutUpdateCall extends AbstractCall
        implements Callable<RCoreResult> {

    private final String API = REndpoints.RPROJECTABOUTUPDATE;

    public ProjectAboutUpdateCall(RProjectDetails details) {
        httpParams.put("project", details.id);
        httpParams.put("name", details.name);
        httpParams.put("descr", details.descr);
        httpParams.put("longdescr", details.longdescr);
        httpParams.put("projectcookie", details.cookie);
        httpParams.put("shared", Boolean.toString(details.shared));
        httpParams.put("format", "json");
    }

    /**
     * Internal use only, to execute call use RClient.execute().
     */
    public RCoreResult call() {

        return makePostRequest(API);
    }

}
