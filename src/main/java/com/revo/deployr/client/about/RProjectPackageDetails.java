/*
 * RProjectPackageDetails.java
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
package com.revo.deployr.client.about;

/**
 * Managed project package dependency details.
 */

public class RProjectPackageDetails {

    public RProjectPackageDetails(String name, String descr, String repo, String version, String status, boolean attached) {
        this.name = name;
        this.descr = descr;
        this.repo = repo;
        this.version = version;
        this.status = status;
        this.attached = attached;
    }

    /**
     * Package name.
     */
    public final String name;

    /**
     * Package description.
     */
    public final String descr;

    /**
     * Package repo.
     */
    public final String repo;

    /**
     * Package version.
     */
    public final String version;

    /**
     * Package status.
     */
    public final String status;

    /**
     * Package attached on current project.
     */
    public final boolean attached;

}
