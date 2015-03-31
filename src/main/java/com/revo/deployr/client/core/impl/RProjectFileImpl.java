/*
 * RProjectFileImpl.java
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

import com.revo.deployr.client.call.project.ProjectDirectoryUpdateCall;
import com.revo.deployr.client.call.project.ProjectDirectoryStoreCall;
import com.revo.deployr.client.call.project.ProjectDirectoryDeleteCall;

import com.revo.deployr.client.RProjectFile;
import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.about.RProjectDetails;
import com.revo.deployr.client.about.RProjectFileDetails;
import com.revo.deployr.client.about.RRepositoryFileDetails;

import com.revo.deployr.client.params.RepoUploadOptions;

import com.revo.deployr.client.core.REndpoints;
import com.revo.deployr.client.util.REntityUtil;

import org.apache.http.client.utils.URIBuilder;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.InputStream;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory; 

/**
 * DeployR project directory file.
 */
public class RProjectFileImpl implements RProjectFile {

    private Log log = LogFactory.getLog(RProjectFile.class);

    RProjectDetails project;
    RProjectFileDetails about;
    RLiveContext liveContext;

    public RProjectFileImpl(RProjectDetails project, RProjectFileDetails about, RLiveContext liveContext) {
	this.project = project;
	this.about = about;
	this.liveContext = liveContext;
    }

    public RProjectFileDetails about() {
	return about;
    }

    public RProjectFile update(String rename, String descr, boolean overwrite)
	throws RClientException, RSecurityException {

	RCall rCall = new ProjectDirectoryUpdateCall(this.project.id, this.about.filename, rename, descr, overwrite);
	RCoreResult rResult = liveContext.executor.processCall(rCall);

	Map dirFileMap = rResult.getDirectoryFile();
	log.debug("updateDirectoryFile: rResult.getDirectoryFile=" + dirFileMap);
	RProjectFileDetails details = REntityUtil.getProjectFileDetails(dirFileMap);
	RProjectFile projectFile = new RProjectFileImpl(this.project, details, liveContext);

	boolean success = rResult.isSuccess();
	String error = rResult.getError();
	int errorCode = rResult.getErrorCode();

	log.debug("updateDirectoryFile: projectFile, success=" + success + " error=" + error + " errorCode=" + errorCode);

	return projectFile;
    }

    public RRepositoryFile store(RepoUploadOptions options)
	throws RClientException, RSecurityException {

	if(options.filename != this.about.filename) {
	    options.filename = this.about.filename;
	}

	RCall rCall = new ProjectDirectoryStoreCall(this.project.id, options);
	RCoreResult rResult = liveContext.executor.processCall(rCall);

	Map repoFileMap = rResult.getRepoFile();
	log.debug("uploadFile: rResult.getRepoFile=" + repoFileMap);
	RRepositoryFileDetails details = REntityUtil.getRepositoryFileDetails(repoFileMap);

	RRepositoryFile repoFile = new RRepositoryFileImpl(details, liveContext);

	boolean success = rResult.isSuccess();
	String error = rResult.getError();
	int errorCode = rResult.getErrorCode();

	log.debug("store: repoFile, success=" + success + " error=" + error + " errorCode=" + errorCode);

	return repoFile;
    }

    public void delete()
	throws RClientException, RSecurityException {

	RCall rCall = new ProjectDirectoryDeleteCall(this.project.id, this.about.filename);
	RCoreResult rResult = liveContext.executor.processCall(rCall);

	boolean success = rResult.isSuccess();
	String error = rResult.getError();
	int errorCode = rResult.getErrorCode();

	log.debug("delete: projectFile, success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

    public InputStream download()
		throws RClientException, RSecurityException {

		try {

	        String urlBase = liveContext.serverurl +
	        			REndpoints.RPROJECTDIRECTORYDOWNLOAD;
	        String urlPath = urlBase + ";jsessionid=" + liveContext.httpcookie;
	        URIBuilder builder = new URIBuilder(urlPath);
	        builder.addParameter("project", this.project.id);
	        builder.addParameter("filename", this.about.filename);
			return liveContext.executor.download(builder);
        } catch(Exception dex) {
            throw new RClientException("Download failed: "  + dex.getMessage());
        }
    }

}
