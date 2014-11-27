/*
 * RProjectExecution.java
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

import com.revo.deployr.client.about.RProjectExecutionDetails;

import java.net.URL;
import java.util.List;

/**
 * Represents a DeployR project execution.
 */
public interface RProjectExecution {

    /**
     * About project execution.
     *
     * @see RProjectExecutionDetails
     */
    public RProjectExecutionDetails about();

    /**
     * Flush execution.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void flush() throws RClientException, RSecurityException;

    /**
     * List execution results.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RProjectResult> listResults() throws RClientException, RSecurityException;

    /**
     * Download execution results.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public URL downloadResults() throws RClientException, RSecurityException;

    /**
     * Delete execution results.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void deleteResults() throws RClientException, RSecurityException;


}
