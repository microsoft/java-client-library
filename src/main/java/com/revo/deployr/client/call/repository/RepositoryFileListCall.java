/*
 * RepositoryFileListCall.java
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

import java.util.concurrent.Callable;

/**
 * Provides support for DeployR API call: /r/repository/file/list.
 * <p/>
 * Simply construct an instance of this call and pass it on the
 * execute() method of your {@link com.revo.deployr.client.RClient}.
 */
public class RepositoryFileListCall extends AbstractCall
        implements Callable<RCoreResult> {

    private final String API = REndpoints.RREPOSITORYFILELIST;

    public RepositoryFileListCall(boolean archived, boolean shared, boolean published,
                                  String filename, String directory,
                                  boolean useExternalRepo,
                                  String categoryFilter) {
        httpParams.put("filename", filename);
        httpParams.put("directory", directory);
        httpParams.put("archived", Boolean.toString(archived));
        httpParams.put("shared", Boolean.toString(shared));
        httpParams.put("published", Boolean.toString(published));
        httpParams.put("external", Boolean.toString(useExternalRepo));
        httpParams.put("categoryFilter", categoryFilter);
        httpParams.put("format", "json");
    }

    /**
     * Internal use only, to execute call use RClient.execute().
     */
    public RCoreResult call() {

        return makeGetRequest(API);
    }

}
