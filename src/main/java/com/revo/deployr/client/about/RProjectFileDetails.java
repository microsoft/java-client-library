/*
 * RProjectFileDetails.java
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
 * Managed project directory file details.
 */
public class RProjectFileDetails {

    public RProjectFileDetails(String filename,
                               String descr,
                               String type,
                               long size,
                               URL url) {
        this.filename = filename;
        this.descr = descr;
        this.type = type;
        this.size = size;
        this.url = url;
    }

    /**
     * Project directory file name.
     */
    public final String filename;

    /**
     * Project directory file description.
     */
    public final String descr;

    /**
     * Project directory file mime type.
     */
    public final String type;

    /**
     * Project directory file size.
     */
    public final long size;

    /**
     * Project directory file URL.
     */
    public final URL url;

}
