/*
 * RJobImpl.java
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

import com.revo.deployr.client.RClientException;
import com.revo.deployr.client.RSecurityException;
import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.core.RClientExecutor;
import com.revo.deployr.client.call.RCall;

import com.revo.deployr.client.RJob;
import com.revo.deployr.client.about.RJobDetails;
import com.revo.deployr.client.call.job.JobQueryCall;
import com.revo.deployr.client.call.job.JobCancelCall;
import com.revo.deployr.client.call.job.JobDeleteCall;
import com.revo.deployr.client.util.REntityUtil;

import java.util.List;
import java.util.Map;
import java.net.URL;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory; 

/**
 * DeployR managed job.
 */
public class RJobImpl implements RJob {

    private Log log = LogFactory.getLog(RJob.class);

    RJobDetails about;
    RLiveContext liveContext;

    public RJobImpl(RJobDetails about, RLiveContext liveContext) {
	this.about = about;
	this.liveContext = liveContext;
    }

    public RJobDetails about() {
	return about;
    }

    /*
     * RJob Interfaces.
     */

    public RJobDetails query()
	throws RClientException, RSecurityException {

	RCall rCall = new JobQueryCall(this.about.id);
	RCoreResult rResult = liveContext.executor.processCall(rCall);

	Map job = rResult.getJob();
	log.debug("query: job=" + job);

	this.about = REntityUtil.getJobDetails(job);

	boolean success = rResult.isSuccess();
	String error = rResult.getError();
	int errorCode = rResult.getErrorCode();

	log.debug("query: success=" + success + " error=" + error + " errorCode=" + errorCode);

	return this.about;
    }

    /**
     * Cancel job.
     *
     * @throws RClientException if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     *
     * @see RJobDetails
     */
    public RJobDetails cancel()
	throws RClientException, RSecurityException {

	RCall rCall = new JobCancelCall(this.about.id);
	RCoreResult rResult = liveContext.executor.processCall(rCall);

	Map job = rResult.getJob();
	log.debug("cancel: job=" + job);

	this.about = REntityUtil.getJobDetails(job);

	boolean success = rResult.isSuccess();
	String error = rResult.getError();
	int errorCode = rResult.getErrorCode();

	log.debug("cancel: success=" + success + " error=" + error + " errorCode=" + errorCode);

	return this.about;
    }

    /**
     * Delete job.
     *
     * @throws RClientException if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     *
     */
    public void delete()
	throws RClientException, RSecurityException {

	RCall rCall = new JobDeleteCall(this.about.id);
	RCoreResult rResult = liveContext.executor.processCall(rCall);

	boolean success = rResult.isSuccess();
	String error = rResult.getError();
	int errorCode = rResult.getErrorCode();

	log.debug("delete: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

}
