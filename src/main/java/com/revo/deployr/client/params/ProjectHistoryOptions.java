/*
 * ProjectHistoryOptions.java
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
package com.revo.deployr.client.params;

import java.util.Map;

/**
 * Project history options.
 */
public class ProjectHistoryOptions {

    public ProjectHistoryOptions() {
    }

    /**
     * History tag filter.
     */
    public String tagFilter;

    /**
     * History depth filter.
     */
    public int depthFilter;

    /**
     * History reverse chronology.
     */
    public boolean reversed;
}
