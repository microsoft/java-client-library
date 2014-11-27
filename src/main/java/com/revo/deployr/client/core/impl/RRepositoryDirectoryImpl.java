/*
 * RRepositoryDirectoryImpl.java
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
package com.revo.deployr.client.core.impl;

import com.revo.deployr.client.RClientException;
import com.revo.deployr.client.RSecurityException;
import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.core.RClientExecutor;
import com.revo.deployr.client.call.RCall;

import com.revo.deployr.client.call.repository.RepositoryDirectoryListCall;
import com.revo.deployr.client.call.repository.RepositoryDirectoryArchiveCall;
import com.revo.deployr.client.call.repository.RepositoryDirectoryRenameCall;
import com.revo.deployr.client.call.repository.RepositoryDirectoryUpdateCall;
import com.revo.deployr.client.call.repository.RepositoryDirectoryDeleteCall;

import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.RRepositoryDirectory;
import com.revo.deployr.client.about.RRepositoryDirectoryDetails;

import com.revo.deployr.client.params.RepoAccessControlOptions;
import com.revo.deployr.client.params.RepoUploadOptions;

import com.revo.deployr.client.core.REndpoints;
import com.revo.deployr.client.util.REntityUtil;

import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.net.*;
import org.apache.http.client.utils.URIBuilder;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory; 

/**
 * DeployR repository directory.
 */
public class RRepositoryDirectoryImpl implements RRepositoryDirectory {

    private Log log = LogFactory.getLog(RRepositoryDirectory.class);

    RRepositoryDirectoryDetails about;
    RLiveContext liveContext;

    public RRepositoryDirectoryImpl(RRepositoryDirectoryDetails about,
                                    RLiveContext liveContext) {
        this.about = about;
        this.liveContext = liveContext;
    }

    public RRepositoryDirectoryDetails about()
                throws RClientException, RSecurityException {
        return about;
    }

    public void update(RepoAccessControlOptions options,
                       List<RRepositoryFile> files)
                    throws RClientException, RSecurityException {

        List<String> fileNames = null;
        if(files != null) {
            fileNames = new ArrayList<String>();
            for(RRepositoryFile repoFile : files) {
                fileNames.add(repoFile.about().filename);
            }
        }

        RCall rCall = new RepositoryDirectoryUpdateCall(about, options, fileNames);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("update: repoDirectory, success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

    public void upload(InputStream fileStream, RepoUploadOptions options)
                                throws RClientException, RSecurityException {

    }

    public RRepositoryDirectory archive(String archiveDirectoryName,
                                        List<RRepositoryFile> files)
                    throws RClientException, RSecurityException {

        List<String> fileNames = null;
        if(files != null) {
            fileNames = new ArrayList<String>();
            for(RRepositoryFile repoFile : files) {
                fileNames.add(repoFile.about().filename);
            }
        }

        RCall rCall = new RepositoryDirectoryArchiveCall(about, archiveDirectoryName, fileNames);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map repoDirectoryMap = (Map) rResult.getRepoDirectory();
        log.debug("archive: rResult.getRepoDirectory=" + repoDirectoryMap);
        RRepositoryDirectoryDetails details = REntityUtil.getRepositoryDirectoryDetails(repoDirectoryMap, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("archive: repoDirectory, success=" + success + " error=" + error + " errorCode=" + errorCode);
        return new RRepositoryDirectoryImpl(details, liveContext);
    }

    public RRepositoryDirectory rename(String newDirectoryName)
                    throws RClientException, RSecurityException {

        RCall rCall = new RepositoryDirectoryRenameCall(about, newDirectoryName);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map repoDirectoryMap = (Map) rResult.getRepoDirectory();
        log.debug("rename: rResult.getRepoDirectory=" + repoDirectoryMap);
        RRepositoryDirectoryDetails details = REntityUtil.getRepositoryDirectoryDetails(repoDirectoryMap, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("rename: repoDirectory, success=" + success + " error=" + error + " errorCode=" + errorCode);

        return new RRepositoryDirectoryImpl(details, liveContext);
    }

    public URL download(List<RRepositoryFile> files)
                    throws RClientException, RSecurityException {

        String fileNames = null;
        if(files != null) {
            for(RRepositoryFile repoFile : files) {
                if(fileNames != null) {
                    fileNames = fileNames + "," + repoFile.about().filename;
                } else {
                    fileNames = repoFile.about().filename;
                }
            }
        }

        return null;
    }

    public void delete()
        throws RClientException, RSecurityException {

        RCall rCall = new RepositoryDirectoryDeleteCall(about);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("delete: repoDirectory, success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

}
