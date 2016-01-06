/*
 * RCoreResponse.java
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
package com.revo.deployr.client.core;

/**
 * Represents a handle to a response on a DeployR API call.
 */
public interface RCoreResponse {

    /**
     * Test to determine if DeployR API call has completed.
     * <p/>
     * Testing in this manner facilitates asynchronous response handling.
     *
     * @return true is call has completed, false otherwise.
     */
    public boolean isCompleted();

    /**
     * Attempt to cancel the execution of a DeployR API call.
     * <p/>
     * This attempt will fail if the call has already completed, already
     * been cancelled, or could not be cancelled for some other reason.
     *
     * @return false is the call could not be cancelled, true otherwise.
     */
    public boolean cancel();

    /**
     * Test to determine if DeployR API call has been cancelled.
     * <p/>
     * A cancelled call will always see null returned on a get().
     *
     * @return true if the call was cancelled before it managed to complete.
     */
    public boolean isCancelled();

    /**
     * Retrieves the result of a completed DeployR API call.
     * <p/>
     * This call will block until the call completes or is cancelled.
     *
     * @return RCoreResult
     */
    public RCoreResult get() throws RInterruptedException, RExecutionException;

}
