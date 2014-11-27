/*
 * RProjectPackageImpl.java
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

import com.revo.deployr.client.RProjectPackage;
import com.revo.deployr.client.about.RProjectDetails;
import com.revo.deployr.client.about.RProjectPackageDetails;

import com.revo.deployr.client.core.REndpoints;
import com.revo.deployr.client.util.REntityUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.net.URL;
import java.net.MalformedURLException;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory; 

/**
 * DeployR project package.
 */
public class RProjectPackageImpl implements RProjectPackage {

    private Log log = LogFactory.getLog(RProjectPackage.class);

    RProjectDetails project;
    RProjectPackageDetails about;
    RLiveContext liveContext;

    public RProjectPackageImpl(RProjectDetails project, RProjectPackageDetails about, RLiveContext liveContext) {
	this.project = project;
	this.about = about;
	this.liveContext = liveContext;
    }

    public RProjectPackageDetails about() {
	return about;
    }

}
