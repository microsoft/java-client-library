/*
 * RProjectResultImpl.java
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

import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.core.RClientExecutor;

import com.revo.deployr.client.RProjectResult;
import com.revo.deployr.client.about.RProjectDetails;
import com.revo.deployr.client.about.RProjectExecutionDetails;
import com.revo.deployr.client.about.RProjectResultDetails;

import com.revo.deployr.client.call.RCall;
import com.revo.deployr.client.call.project.ProjectExecuteResultDeleteCall;
import com.revo.deployr.client.core.REndpoints;

import com.revo.deployr.client.RClientException;
import com.revo.deployr.client.RSecurityException;

import java.io.InputStream;
import org.apache.http.client.utils.URIBuilder;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory; 

/**
 * Represents a DeployR project execution result.
 */
public class RProjectResultImpl implements RProjectResult {

    private Log log = LogFactory.getLog(RProjectResult.class);

    RProjectDetails project;
    RProjectResultDetails about;
    RLiveContext liveContext;

    public RProjectResultImpl(RProjectDetails project,
    						  RProjectResultDetails about,
    						  RLiveContext liveContext) {
		this.project = project;
		this.about = about;
		this.liveContext = liveContext;
    }

    public RProjectResultDetails about() {
		return this.about;
    }

    public InputStream download()
		throws RClientException, RSecurityException {

		try {
	        String urlBase = this.liveContext.serverurl +
	                REndpoints.RPROJECTEXECUTERESULTDOWNLOAD;
	        String urlPath = urlBase + ";jsessionid=" + liveContext.httpcookie;
	        URIBuilder builder = new URIBuilder(urlPath);
	        builder.addParameter("project", this.project.id);
	        builder.addParameter("filename", this.about.filename);
	        return liveContext.executor.download(builder);
		} catch(Exception ex) {
            throw new RClientException("Download failed: " +
            								ex.getMessage());
        }
    }

    public void delete()
		throws RClientException, RSecurityException {

		RCall rCall = new ProjectExecuteResultDeleteCall(this.project.id,
							this.about.execution, this.about.filename);
		RCoreResult rResult = liveContext.executor.processCall(rCall);

		boolean success = rResult.isSuccess();
		String error = rResult.getError();
		int errorCode = rResult.getErrorCode();

		log.debug("delete: success=" + success + " error=" +
									error + " errorCode=" + errorCode);
    }

}
