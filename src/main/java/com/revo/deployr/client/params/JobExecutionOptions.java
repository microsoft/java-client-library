/*
 * JobExecutionOptions.java
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
package com.revo.deployr.client.params;

import java.util.List;
import com.revo.deployr.client.data.RData;

/**
 * Job execution options.
 */
public class JobExecutionOptions extends ProjectExecutionOptions {

    public static final String LOW_PRIORITY = "low";
    public static final String MEDIUM_PRIORITY = "medium";
    public static final String HIGH_PRIORITY = "high";

    public JobExecutionOptions() {
    }

    /**
     * Job scheduling options specify the start time and
     * optionally the repeat interval and count for a
     * scheduled job.
     */
    public JobSchedulingOptions schedulingOptions;

    /**
     * Set a scheduling priority for your job. Default 
     * scheduling priority defaults to LOW_PRIORITY.
     */
    public String priority = LOW_PRIORITY;

    /**
     * Enable noproject option if project persistence following
     * job execution is not required. Typically used when 
     * ProjectStorageOptions have already been specified.
     */
    public boolean noproject;

    /**
     * Identifies the DeployR grid cluster where the caller would
     * like the job (R session) to execute. If there are no slots
     * available on any of the grid nodes within the cluster indicated
     * then the server will attempt to execute the job on a slot
     * on an available grid node that supports MIXED-operations. If no
     * slot meeting these criteria is found, the job will be queued
     * until a suitable slot becomes available. This feature is optional
     * and available on DeployR Enterprise only.
     */
    public String gridCluster;

}
