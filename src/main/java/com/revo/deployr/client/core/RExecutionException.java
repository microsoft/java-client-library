/*
 * RExecutionException.java
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
package com.revo.deployr.client.core;

import java.util.concurrent.ExecutionException;

/**
 * Exception thrown when attempting to retrieve the RResult of a DeployR
 * API call that was aborted as a result of a runtime Exception.
 * <p/>
 * This exception can be inspected using the Throwable.getCause() method.
 */
public class RExecutionException extends ExecutionException {

    public RExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
