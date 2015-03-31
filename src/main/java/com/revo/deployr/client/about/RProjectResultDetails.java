/*
 * RProjectResultDetails.java
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

import java.net.URL;

/**
 * Managed project execution result details.
 */
public class RProjectResultDetails {

    public RProjectResultDetails(String execution,
                                 String filename,
                                 String type,
                                 long size,
                                 URL url) {
        this.execution = execution;
        this.filename = filename;
        this.type = type;
        this.size = size;
        this.url = url;
    }

    /**
     * Project execution result file name.
     */
    public final String filename;

    /**
     * Project execution result file mime type.
     */
    public final String type;

    /**
     * Project execution result file size.
     */
    public final long size;

    /**
     * Project execution result file url.
     */
    public final URL url;

    /**
     * Project execution identifier.
     */
    public final String execution;

}
