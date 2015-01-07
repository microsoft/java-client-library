/*
 * ProjectCreateCall.java
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

import com.revo.deployr.client.RDataException;
import com.revo.deployr.client.call.AbstractCall;
import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.core.REndpoints;
import com.revo.deployr.client.params.ProjectCreationOptions;
import com.revo.deployr.client.util.RDataUtil;

import java.util.concurrent.Callable;

/**
 * Provides support for DeployR API call: /r/project/create.
 * <p/>
 * Simply construct an instance of this call and pass it on the
 * execute() method of your {@link com.revo.deployr.client.RClient}.
 */
public class ProjectCreateCall extends AbstractCall implements Callable<RCoreResult> {

    private final String API = REndpoints.RPROJECTCREATE;

    public ProjectCreateCall() throws RDataException {
        this(null, null, null);
    }

    public ProjectCreateCall(String name, String descr) throws RDataException {
        this(name, descr, null);
    }

    public ProjectCreateCall(String name, String descr, ProjectCreationOptions options) throws RDataException {

        if (name != null) {
            httpParams.put("projectname", name);
            httpParams.put("projectdescr", descr);
        }

        if (options != null) {

            httpParams.put("blackbox", Boolean.toString(options.blackbox));

            if (options.rinputs != null) {
                String markup = RDataUtil.toJSON(options.rinputs);
                httpParams.put("inputs", markup);
            }

            if (options.preloadWorkspace != null) {
                httpParams.put("preloadobjectname", options.preloadWorkspace.filename);
                httpParams.put("preloadobjectdirectory", options.preloadWorkspace.directory);
                httpParams.put("preloadobjectauthor", options.preloadWorkspace.author);
                httpParams.put("preloadobjectversion", options.preloadWorkspace.version);
            }

            if (options.preloadDirectory != null) {
                httpParams.put("preloadfilename", options.preloadDirectory.filename);
                httpParams.put("preloadfiledirectory", options.preloadDirectory.directory);
                httpParams.put("preloadfileauthor", options.preloadDirectory.author);
                httpParams.put("preloadfileversion", options.preloadDirectory.version);
            }

            httpParams.put("preloadbydirectory", options.preloadByDirectory);

            if (options.adoptionOptions != null) {
                httpParams.put("adoptworkspace", options.adoptionOptions.adoptWorkspace);
                httpParams.put("adoptdirectory", options.adoptionOptions.adoptDirectory);
                httpParams.put("adoptpackages", options.adoptionOptions.adoptPackages);
            }

            httpParams.put("cluster", options.gridCluster);
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
