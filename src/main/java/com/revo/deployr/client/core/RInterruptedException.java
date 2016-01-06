/*
 * RInterruptedException.java
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
 * Exeception thrown when the thread handling a DeployR API call
 * is interrupted, typically due to the call being cancelled.
 */
public class RInterruptedException extends InterruptedException {

    public RInterruptedException(String message) {
        super(message);
    }
}
