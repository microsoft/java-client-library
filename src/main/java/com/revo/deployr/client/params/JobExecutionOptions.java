/*
 * JobExecutionOptions.java
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

}
