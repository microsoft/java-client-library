/*
 * RScriptExecutionImpl.java
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

import com.revo.deployr.client.about.RProjectDetails;
import com.revo.deployr.client.about.RProjectExecutionDetails;

import com.revo.deployr.client.RScriptExecution;

import com.revo.deployr.client.RClientException;
import com.revo.deployr.client.RSecurityException;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory; 

/**
 * Represents a DeployR script execution.
 */
public class RScriptExecutionImpl implements RScriptExecution {

    private Log log = LogFactory.getLog(RScriptExecution.class);

    RProjectDetails project;
    RProjectExecutionDetails about;
    RLiveContext liveContext;

    public RScriptExecutionImpl(RProjectDetails project, RProjectExecutionDetails about, RLiveContext liveContext) {
	this.project = project;
	this.about = about;
	this.liveContext = liveContext;
    }

    public RProjectExecutionDetails about() {
	return this.about;
    }

}
