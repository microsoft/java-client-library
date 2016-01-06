/*
 * RRepositoryDirectoryDetails.java
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

import com.revo.deployr.client.RRepositoryFile;

import java.util.List;

/**
 * Managed repository directory details.
 */
public class RRepositoryDirectoryDetails {

    public final static String SYSTEM_RESTRICTED = "Restricted";
    public final static String SYSTEM_SHARED = "Shared";
    public final static String SYSTEM_PUBLIC = "Public";

    public RRepositoryDirectoryDetails(String name,
                                       boolean systemDirectory,
                                       List<RRepositoryFile> files) {
        this.name = name;
        this.systemDirectory = systemDirectory;
        this.files = files;
    }

    /**
     * Repository directory name.
     */
    public final String name;

    /**
     * Repository directory type: system or user.
     */
    public final boolean systemDirectory;

    /**
     * Repository directory files.
     */
    public final List<RRepositoryFile> files;

}
