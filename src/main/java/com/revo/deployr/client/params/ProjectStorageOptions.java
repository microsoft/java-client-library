/*
 * ProjectStorageOptions.java
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
package com.revo.deployr.client.params;

import java.util.Map;

/**
 * Project post-execution repository storage options.
 */
public class ProjectStorageOptions {

    public ProjectStorageOptions() {
    }

    /**
     * Comma-separated list of working directory files to be stored
     * in the repository following an execution.
     */
    public String files;

    /**
     * Comma-separated list of workspace objects to be stored in the
     * repository following an execution.
     */
    public String objects;

    /**
     * Specify a filename and the contents of the entire workspace will
     * be saved as filename.rData in the repository.
     */
    public String workspace;

    /**
     * Specify a directory into which each of the stored files, objects
     * and/or workspace will be saved in the repository.
     */
    public String directory;

    /**
     * Enable to create new version of files being stored in the 
     * repository following an execution. Default behavior is to overwrite
     * files that already exist in the repository.
     */
    public boolean newversion;

    /**
     * Enable to assign public access on stored files in the
     * repository following an execution. By default private access is
     * assigned to files being stored in the repository.
     */
    public boolean published;

}
