/*
 * RepositoryDirectoryUpdateCall.java
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

import com.revo.deployr.client.about.RRepositoryDirectoryDetails;
import com.revo.deployr.client.call.AbstractCall;
import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.core.REndpoints;
import com.revo.deployr.client.params.RepoAccessControlOptions;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Provides support for DeployR API call: /r/repository/directory/update.
 * <p/>
 * Simply construct an instance of this call and pass it on the
 * execute() method of your {@link com.revo.deployr.client.RClient}.
 */
public class RepositoryDirectoryUpdateCall extends AbstractCall
        implements Callable<RCoreResult> {

    private final String API = REndpoints.RREPOSITORYDIRECTORYUPDATE;

    public RepositoryDirectoryUpdateCall(RRepositoryDirectoryDetails details,
                                         RepoAccessControlOptions options,
                                         List<String> files) {

        httpParams.put("directory", details.name);

        String fileNames = null;
        if (files != null) {
            for (String fileName : files) {
                if (fileNames != null) {
                    fileNames = fileNames + "," + fileName;
                } else {
                    fileNames = fileName;
                }
            }
        }
        httpParams.put("filename", fileNames);

        if (options != null) {
            httpParams.put("restricted", options.restricted);
            httpParams.put("shared", Boolean.toString(options.shared));
            httpParams.put("published", Boolean.toString(options.published));
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
