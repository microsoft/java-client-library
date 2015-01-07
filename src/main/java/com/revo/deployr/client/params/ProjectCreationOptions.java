/*
 * ProjectCreationOptions.java
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

import java.util.List;
import com.revo.deployr.client.data.RData;

/**
 * Project creation options. Can be used
 * to pre-initialize data in the workspace
 * and working directory for the new project.
 */
public class ProjectCreationOptions {

    public ProjectCreationOptions() {
    }

    /**
     * Enable to create a blackbox project.
     * Blackbox projects are a special type of temporary project
     * that limit API access on the underlying R session.
     */
    public boolean blackbox;

    /**
     * List of DeployR-encoded R objects to be added to the
     * workspace of the new R session.
     */
    public List<RData> rinputs;

    /**
     * Allows the loading of one
     * or more named binary R objects from the repository into the
     * workspace of the new R session.
     */
    public ProjectPreloadOptions preloadWorkspace;

    /**
     * Allows the loading
     * of one or more named files from the repository into the
     * working directory of the new R session.
     */
    public ProjectPreloadOptions preloadDirectory;

    /**
     * Allows the loading of all files
     * found in one or more repository-managed directories
     * into the working directory of the current R sesssion
     * prior to execution.
     *
     * When loading the contents of more than one directory,
     * use a comma-separated list of directory names.
     *
     * Pre-execution option.
     */
    public String preloadByDirectory;

    /**
     * Allows the pre-loading
     * of a pre-existing project workspace, project working directory,
     * project history and/or project package dependencies
     * into the current R session.
     */
    public ProjectAdoptionOptions adoptionOptions;

    /**
     * Identifies the DeployR grid cluster where the caller would
     * like the project (R session) to execute. If there are no slots
     * available on any of the grid nodes within the cluster indicated
     * then the server will attempt to execute the project on a slot
     * on an available grid node that supports MIXED-operations. If no
     * slot meeting these criteria is found, the call will return an
     * {@link com.revo.deployr.client.RGridException}. This feature
     * is optional and available on DeployR Enterprise only.
     */
    public String gridCluster;

}
