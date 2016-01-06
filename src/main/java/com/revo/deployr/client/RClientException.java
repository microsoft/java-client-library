/*
 * RClientException.java
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
package com.revo.deployr.client;

/**
 * Exception thrown when RClient fails to complete call.
 */
public class RClientException extends Exception {

    public int errorCode = 400;

    public RClientException(String message) {
        super(message);
    }

    public RClientException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public RClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public RClientException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
