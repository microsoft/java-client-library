/*
 * RSecurityException.java
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

/**
 * Exception thrown when DeployR security conditions not met.
 */
public class RSecurityException extends Exception {

    /**
     * Denotes unauthenciated call.
     */
    public static int AUTHENTICATION = 401;
    /**
     * Denotes unauthorized call.
     */
    public static int AUTHORIZATION = 403;
    /**
     * Denotes concurrency conflict on call.
     */
    public static int CONFLICT = 409;

    public int errorCode;

    public RSecurityException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
