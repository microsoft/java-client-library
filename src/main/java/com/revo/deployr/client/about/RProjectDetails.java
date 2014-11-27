/*
 * RProjectDetails.java
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

import java.util.Date;
import java.util.List;

/**
 * Managed project details.
 */

public class RProjectDetails {

    public RProjectDetails(String id, String origin, boolean islive, Date modified, List<String> authors) {
        this.id = id;
        this.origin = origin;
        this.islive = islive;
        this.modified = modified;
        this.authors = authors;
    }

    /**
     * Project id.
     */
    public final String id;

    /**
     * Project name.
     */
    public String name;

    /**
     * Project description.
     */
    public String descr;

    /**
     * Project long description.
     */
    public String longdescr;

    /**
     * Project origin.
     */
    public final String origin;

    /**
     * Project shared.
     */
    public boolean shared;

    /**
     * Project islive.
     */
    public final boolean islive;

    /**
     * Project cookie.
     */
    public String cookie;

    /**
     * Project last modified date.
     */
    public final Date modified;

    /**
     * Project authors.
     */
    public final List<String> authors;

}
