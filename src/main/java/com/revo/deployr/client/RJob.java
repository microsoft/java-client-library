/*
 * RJob.java
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
package com.revo.deployr.client;

import com.revo.deployr.client.about.RJobDetails;

/**
 * Represents a DeployR managed job.
 */
public interface RJob {

    /**
     * About job.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RJobDetails
     */
    public RJobDetails about() throws RClientException, RSecurityException;


    /**
     * Query job.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RJobDetails
     */
    public RJobDetails query() throws RClientException, RSecurityException;

    /**
     * Cancel job.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RJobDetails
     */
    public RJobDetails cancel() throws RClientException, RSecurityException;

    /**
     * Delete job.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void delete() throws RClientException, RSecurityException;

    public static final String SCHEDULED = "Scheduled";
    public static final String QUEUED = "Queued";
    public static final String RUNNING = "Running";
    public static final String COMPLETED = "Completed";
    public static final String CANCELLED = "Cancelled";
    public static final String INTERRUPTED = "Interrupted";
    public static final String FAILED = "Failed";
    public static final String ABORTED = "Aborted";

}
