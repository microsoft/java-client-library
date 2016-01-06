/*
 * RepositoryFileCopyCall.java
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

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Provides support for DeployR API call: /r/repository/file/copy.
 * <p/>
 * Simply construct an instance of this call and pass it on the
 * execute() method of your {@link com.revo.deployr.client.RClient}.
 */
public class RepositoryFileCopyCall extends AbstractCall
        implements Callable<RCoreResult> {

    private final String API = REndpoints.RREPOSITORYFILECOPY;

    public RepositoryFileCopyCall(String destination,
                                  List<Map<String, String>> file,
                                  List<String> fileRenames) {

        httpParams.put("destination", destination);

        String fileNames = null;
        String dirNames = null;
        String versionNames = null;
        for (int i = 0; i < file.size(); i++) {
            Map<String, String> fileMap = (Map<String, String>) file.get(i);
            if (fileNames != null) {
                fileNames = fileNames + "," + fileMap.get("filename");
                dirNames = dirNames + "," + fileMap.get("directory");
                if (fileMap.get("version") != null) {
                    versionNames = versionNames + "," + fileMap.get("version");
                }
            } else {
                fileNames = fileMap.get("filename");
                dirNames = fileMap.get("directory");
                if (fileMap.get("version") != null) {
                    versionNames = fileMap.get("version");
                }
            }
        }
        httpParams.put("filename", fileNames);
        httpParams.put("directory", dirNames);
        httpParams.put("version", versionNames);

        if (fileRenames != null) {

            String renameNames = null;
            for (String renameName : fileRenames) {
                if (renameNames != null) {
                    renameNames = renameNames + "," + renameName;
                } else {
                    renameNames = renameName;
                }
            }
            httpParams.put("filerename", renameNames);
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
