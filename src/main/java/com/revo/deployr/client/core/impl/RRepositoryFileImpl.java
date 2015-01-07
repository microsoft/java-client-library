/*
 * RRepositoryFileImpl.java
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

import com.revo.deployr.client.call.repository.RepositoryFileListCall;
import com.revo.deployr.client.call.repository.RepositoryFileGrantCall;
import com.revo.deployr.client.call.repository.RepositoryFileRevertCall;
import com.revo.deployr.client.call.repository.RepositoryFileUpdateCall;
import com.revo.deployr.client.call.repository.RepositoryFileDeleteCall;

import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.about.RRepositoryFileDetails;

import com.revo.deployr.client.core.REndpoints;
import com.revo.deployr.client.util.REntityUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.net.*;
import org.apache.http.client.utils.URIBuilder;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory; 

/**
 * DeployR repository file.
 */
public class RRepositoryFileImpl implements RRepositoryFile {

    private Log log = LogFactory.getLog(RRepositoryFile.class);

    RRepositoryFileDetails about;
    RLiveContext liveContext;

    public RRepositoryFileImpl(RRepositoryFileDetails about, RLiveContext liveContext) {
	this.about = about;
	this.liveContext = liveContext;
    }

    public RRepositoryFileDetails about() {
	return about;
    }

    public List<RRepositoryFile> versions()
                    throws RClientException, RSecurityException {

        RCall rCall = new RepositoryFileListCall(false, false, false,
                                                 about.filename,
                                                 about.directory,
                                                 false);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        List<Map> repoFiles = rResult.getRepoFiles();
        log.debug("versions: repoFiles=" + repoFiles);

        List<RRepositoryFile> versionList = new ArrayList<RRepositoryFile>();

        for(Map repoFileMap : repoFiles) {

            RRepositoryFileDetails details = REntityUtil.getRepositoryFileDetails(repoFileMap);
            RRepositoryFile file = new RRepositoryFileImpl(details, liveContext);
            versionList.add(file);	
        }

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("versions: repoFiles, success=" + success + " error=" + error + " errorCode=" + errorCode);

        return versionList;
    }

    /*
     * RRepositoryFile Interfaces.
     */

    public RRepositoryFile grant(String newauthor, String revokeauthor)
                        throws RClientException, RSecurityException {

        RCall rCall = new RepositoryFileGrantCall(about, newauthor, revokeauthor);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map repoFileMap = rResult.getRepoFile();
        log.debug("grant: rResult.getRepoFile=" + repoFileMap);
        RRepositoryFileDetails details = REntityUtil.getRepositoryFileDetails(repoFileMap);

        RRepositoryFile repoFile = new RRepositoryFileImpl(details, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("grant: repoFile, success=" + success + " error=" + error + " errorCode=" + errorCode);

        return repoFile;
    }

    public RRepositoryFile revert(RRepositoryFile fileVersion, String descr, String restricted, boolean shared, boolean published)
                    throws RClientException, RSecurityException {

        RCall rCall = new RepositoryFileRevertCall(fileVersion.about(), descr, restricted, shared, published);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map repoFileMap = rResult.getRepoFile();
        log.debug("revert: rResult.getRepoFile=" + repoFileMap);
        RRepositoryFileDetails details = REntityUtil.getRepositoryFileDetails(repoFileMap);

        RRepositoryFile repoFile = new RRepositoryFileImpl(details, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("revert: repoFile, success=" + success + " error=" + error + " errorCode=" + errorCode);

        return repoFile;
    }

    public RRepositoryFile update(String restricted, boolean shared, boolean published, String descr)
                    throws RClientException, RSecurityException {
        return update(restricted, shared, published, descr, null, null);
    }

    public RRepositoryFile update(String restricted, boolean shared, boolean published, String descr, String inputs, String outputs)
                    throws RClientException, RSecurityException {

        RCall rCall = new RepositoryFileUpdateCall(about, restricted, shared, published, descr, inputs, outputs);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map repoFileMap = rResult.getRepoFile();
        log.debug("update: rResult.getRepoFile=" + repoFileMap);
        RRepositoryFileDetails details = REntityUtil.getRepositoryFileDetails(repoFileMap);

        RRepositoryFile repoFile = new RRepositoryFileImpl(details, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("update: repoFile, success=" + success + " error=" + error + " errorCode=" + errorCode);

        return repoFile;
    }


    public URL diff() throws RClientException, RSecurityException {

        URL diffURL;

        if(this.about.version == null) {
            throw new RClientException("Repository file diff can only be requested on a version of a file, not on the latest.");
        }

        try {

            String urlPath = liveContext.serverurl + REndpoints.RREPOSITORYFILEDIFF;
            urlPath = urlPath + ";jsessionid=" + liveContext.httpcookie;

            URIBuilder builder = new URIBuilder(urlPath);
            builder.addParameter("filename", this.about.filename);
            builder.addParameter("directory", this.about.directory);
            builder.addParameter("author", this.about.latestby);
            builder.addParameter("version", this.about.version);

            diffURL = builder.build().toURL();

        } catch(Exception uex) {
            throw new RClientException("Diff url: ex=" + uex.getMessage());
        }
        return diffURL;
    }

    public URL download()
                    throws RClientException, RSecurityException {

        URL downloadURL = null;
        try {

            String urlPath = liveContext.serverurl + REndpoints.RREPOSITORYFILEDOWNLOAD;
            urlPath = urlPath + ";jsessionid=" + liveContext.httpcookie;

            URIBuilder builder = new URIBuilder(urlPath);
            builder.addParameter("filename", this.about.filename);
            builder.addParameter("directory", this.about.directory);
            builder.addParameter("author", this.about.author);
            builder.addParameter("version", this.about.version);

            downloadURL = builder.build().toURL();
        } catch(Exception uex) {
            throw new RClientException("Download url: " + downloadURL + ", ex=" + uex.getMessage());
        }
        return downloadURL;
    }

    public void delete()
                    throws RClientException, RSecurityException {

        RCall rCall = new RepositoryFileDeleteCall(about);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("delete: repoFile, success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

}
