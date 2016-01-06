/*
 * RepositoryFileUpdateCall.java
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

import com.revo.deployr.client.about.RRepositoryFileDetails;
import com.revo.deployr.client.call.AbstractCall;
import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.core.REndpoints;

import java.util.concurrent.Callable;

/**
 * Provides support for DeployR API call: /r/repository/file/update.
 * <p/>
 * Simply construct an instance of this call and pass it on the
 * execute() method of your {@link com.revo.deployr.client.RClient}.
 */
public class RepositoryFileUpdateCall extends AbstractCall
        implements Callable<RCoreResult> {

    private final String API = REndpoints.RREPOSITORYFILEUPDATE;

    public RepositoryFileUpdateCall(RRepositoryFileDetails repoFile,
                                    String restricted, boolean shared,
                                    boolean published, String descr,
                                    String inputs, String outputs) {

        httpParams.put("filename", repoFile.filename);
        httpParams.put("directory", repoFile.directory);
        httpParams.put("restricted", restricted);
        httpParams.put("shared", Boolean.toString(shared));
        httpParams.put("published", Boolean.toString(published));
        httpParams.put("descr", descr);
        httpParams.put("inputs", inputs);
        httpParams.put("outputs", outputs);
        httpParams.put("format", "json");
    }

    /**
     * Internal use only, to execute call use RClient.execute().
     */
    public RCoreResult call() {

        return makePostRequest(API);
    }

}
