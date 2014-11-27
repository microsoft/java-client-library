/*
 * RepoAccessControlOptions.java
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
 * Repo upload options.
 */
public class RepoAccessControlOptions {

    public RepoAccessControlOptions() {
    }

    /**
     * Repository file to be restricted to comma-separated
     * list of Roles on upload.
     *
     */
    public String restricted;

    /**
     * Repository file to be shared on upload.
     */
    public boolean shared;

    /**
     * Repository file to be published on upload.
     */
    public boolean published;

}
