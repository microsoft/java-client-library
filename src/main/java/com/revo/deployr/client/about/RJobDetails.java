/*
 * RJobDetails.java
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
package com.revo.deployr.client.about;

/**
 * Managed job details.
 */

public class RJobDetails {

    public RJobDetails(String id, String name, String descr, String status, String statusMsg, long schedstart, int schedrepeat, long schedinterval, int onrepeat, String project, long timeStart, long timeCode, long timeTotal, String tag) {
        this.id = id;
        this.name = name;
        this.descr = descr;
        this.status = status;
        this.statusMsg = statusMsg;
        this.schedstart = schedstart;
        this.schedrepeat = schedrepeat;
        this.schedinterval = schedinterval;
        this.onrepeat = onrepeat;
        this.project = project;
        this.timeStart = timeStart;
        this.timeCode = timeCode;
        this.timeTotal = timeTotal;
        this.tag = tag;
    }

    /**
     * Job id.
     */
    public final String id;

    /**
     * Job name.
     */
    public final String name;

    /**
     * Job description.
     */
    public final String descr;

    /**
     * Job status.
     */
    public final String status;

    /**
     * Job statusMsg.
     */
    public final String statusMsg;

    /**
     * Job scheduled start time (UTC).
     */
    public final long schedstart;

    /**
     * Job scheduled repeat count.
     */
    public final int schedrepeat;

    /**
     * Job scheduled repeat interval.
     */
    public final long schedinterval;

    /**
     * Job on repeat.
     */
    public final int onrepeat;

    /**
     * Job generated project identifier.
     */
    public final String project;

    /**
     * Start time (millis) for job. Can be used in conjunction with timeCode
     * and timeTotal to profile job runtime characteristics.
     */
    public final long timeStart;

    /**
     * Code execution time (millis) for job. Measures the time taken to
     * execute the R code for the job.
     * <p/>
     * Can be used in conjunction with timeStart and timeTotal to profile
     * job runtime characteristics.
     */
    public final long timeCode;

    /**
     * Total time (millis) for job. Measures the time taken to prepare
     * R workspace and directory for job, execute code for the job and then
     * persist job artifacts as a persistent project.
     * <p/>
     * Can be used in conjunction with timeStart and timeCode to profile
     * job runtime characteristics.
     */
    public final long timeTotal;

    /**
     * Job execution tag.
     */
    public final String tag;

}
