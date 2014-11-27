/*
 * RUserLimitDetails.java
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
package com.revo.deployr.client.about;

/**
 * Managed user limit details.
 */

public class RUserLimitDetails {

    public RUserLimitDetails(int maxConcurrentLiveProjectCount,
                             int maxIdleLiveProjectTimeout,
                             int maxFileUploadSize) {

        this.maxConcurrentLiveProjectCount = maxConcurrentLiveProjectCount;
        this.maxIdleLiveProjectTimeout = maxIdleLiveProjectTimeout;
        this.maxFileUploadSize = maxFileUploadSize;
    }

    /**
     * Max concurrent live project limit.
     */
    public final int maxConcurrentLiveProjectCount;

    /**
     * Max idle live project timeout limit.
     */
    public final int maxIdleLiveProjectTimeout;

    /**
     * Max file upload size limit.
     */
    public final int maxFileUploadSize;

}
