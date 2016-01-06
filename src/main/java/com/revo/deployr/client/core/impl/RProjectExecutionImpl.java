/*
 * RProjectExecutionImpl.java
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
package com.revo.deployr.client.core.impl;

import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.core.RClientExecutor;
import com.revo.deployr.client.core.REndpoints;
import com.revo.deployr.client.call.RCall;

import com.revo.deployr.client.RProjectExecution;
import com.revo.deployr.client.RProjectResult;
import com.revo.deployr.client.about.RProjectDetails;
import com.revo.deployr.client.about.RProjectExecutionDetails;
import com.revo.deployr.client.about.RProjectResultDetails;

import com.revo.deployr.client.call.project.ProjectExecuteFlushCall;
import com.revo.deployr.client.call.project.ProjectExecuteResultListCall;
import com.revo.deployr.client.call.project.ProjectExecuteResultDeleteCall;

import com.revo.deployr.client.util.REntityUtil;

import com.revo.deployr.client.RClientException;
import com.revo.deployr.client.RSecurityException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.net.URL;
import java.net.MalformedURLException;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory; 

/**
 * Represents a DeployR project execution.
 */
public class RProjectExecutionImpl implements RProjectExecution {

    private Log log = LogFactory.getLog(RProjectExecution.class);

    RProjectDetails project;
    RProjectExecutionDetails about;
    RLiveContext liveContext;

    public RProjectExecutionImpl(RProjectDetails project, RProjectExecutionDetails about, RLiveContext liveContext) {
	this.project = project;
	this.about = about;
	this.liveContext = liveContext;
    }

    public RProjectExecutionDetails about() {
	return this.about;
    }

    public void flush()
	throws RClientException, RSecurityException {

	RCall rCall = new ProjectExecuteFlushCall(this.project.id, this.about.id);
	RCoreResult rResult = liveContext.executor.processCall(rCall);

	boolean success = rResult.isSuccess();
	String error = rResult.getError();
	int errorCode = rResult.getErrorCode();

	log.debug("flush: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

    public List<RProjectResult> listResults()
	throws RClientException, RSecurityException {

	RCall rCall = new ProjectExecuteResultListCall(this.project.id, this.about.id);
	RCoreResult rResult = liveContext.executor.processCall(rCall);

        List<Map> results = rResult.getResults();

	List<RProjectResult> resultList = new ArrayList<RProjectResult>();

	for(Map resultMap : results) {
	    RProjectResultDetails resultDetails = REntityUtil.getProjectResultDetails(resultMap);
	    RProjectResult result = new RProjectResultImpl(this.project, resultDetails, liveContext);
	    resultList.add(result);	
	}

	// Update results on RProjectExecutionDetails.
	this.about.results = resultList;

	boolean success = rResult.isSuccess();
	String error = rResult.getError();
	int errorCode = rResult.getErrorCode();

	log.debug("listResults: success=" + success + " error=" + error + " errorCode=" + errorCode);
	return resultList;
    }

    public URL downloadResults()
	throws RClientException, RSecurityException {

	String urlPath = this.liveContext.serverurl + REndpoints.RPROJECTEXECUTERESULTDOWNLOAD;
	String urlQuery = urlPath + "/" + this.project.id + "/" + this.about.id + ";jsessionid=" + this.liveContext.httpcookie;
	log.debug("downloadResults: url=" + urlQuery);
	URL downloadURL = null;
	try {
	    downloadURL = new URL(urlQuery);
	} catch(MalformedURLException mex) {
	    throw new RClientException("Download project execution results url malformed, ex=" + mex.getMessage());
	}
	return downloadURL;
    }

    public void deleteResults()
	throws RClientException, RSecurityException {

	RCall rCall = new ProjectExecuteResultDeleteCall(this.project.id, this.about.id, null);
	RCoreResult rResult = liveContext.executor.processCall(rCall);

	boolean success = rResult.isSuccess();
	String error = rResult.getError();
	int errorCode = rResult.getErrorCode();

	log.debug("deleteResults: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

}
