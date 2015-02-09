/*
 * ProjectExecutionOptions.java
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
 * Project pre-and-post execution options.
 */
public class ProjectExecutionOptions {

    public ProjectExecutionOptions() {
    }

    /**
     * List of DeployR-encoded R objects to be added to the
     * workspace of the current R session prior to the execution.
     *
     * Pre-execution option.
     */
    public List<RData> rinputs;

    /**
     * Comma-seperated list of primitive R object names and values,
     * to be added to the workspace of the current R session prior
     * to the execution.
     *
     * eg. csvrinputs=name,George,age,45
     *
     * Pre-execution option.
     */
    public String csvrinputs;

    /**
     * Allows the loading of one
     * or more named binary R objects from the repository into the
     * workspace of the current R session prior to execution.
     *
     * Pre-execution option.
     */
    public ProjectPreloadOptions preloadWorkspace;

    /**
     * Allows the loading
     * of one or more named files from the repository into the
     * working directory of the current R session prior to execution.
     *
     * Pre-execution option.
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
     * into the current R session prior to an execution.
     *
     * Pre-execution option.
     */
    public ProjectAdoptionOptions adoptionOptions;

    /**
     * Set R graphics device to use on execution: "png" or "svg".
     * The default R graphics device is "png".
     *
     * On-execution option.
     */
    public String graphicsDevice;

    /**
     * Set the width of the R graphics device on execution.
     *
     * On-execution option.
     */
    public int graphicsWidth;

    /**
     * Set the height of the R graphics device on execution.
     *
     * On-execution option.
     */
    public int graphicsHeight;

    /**
     * When enabled R console output events corresponding to
     * the current execution will be sent on the event stream.
     *
     * On-execution option.
     */
    public boolean enableConsoleEvents;

    /**
     * When enabled R commands are not echoed to the console
     * output and will not appear in response markup or
     * on the event stream.
     *
     * On-execution option.
     */
    public boolean echooff;

    /**
     * When enabled all R console output is suppressed and will
     * not appear in response markup. This control has no 
     * impact on console output on the event stream.
     *
     * On-execution option.
     */
    public boolean consoleoff;

    /**
     * List of workspace objects to be retrieved from the
     * workspace of the current R session following the execution
     * and returned as DeployR-encoded R objects.
     *
     * Post-execution option.
     */
    public List<String> routputs;

    /**
     * Workspace data.frame object encoding preference when
     * retrieving R objects from the current R session following an execution.
     * This option works in conjunction with the robjects property on this class.
     * The default DeployR-encoding is to encode primatives inside data.frame
     * objects as primitives, not as vectors.
     *
     * Post-execution option.
     */
    public boolean encodeDataFramePrimitiveAsVector;

    /**
     * Optional custom value to denote NAN values in
     * DeployR-encoded objects in the response markup.
     * Default is null.
     *
     * Post-execution option.
     */
    public String nan;

    /**
     * Optional custom value to denote INFINITY values in
     * DeployR-encoded objects in the response markup.
     * Default is 0x7ff0000000000000L.
     *
     * Post-execution option.
     */
    public String infinity;

    /**
     * Repository storage options allow the storage of
     * one-or-more workspace objects, the entire workspace
     * and/or one-or-more working directory files from the
     * current R session into the repository following the execution.
     *
     * Storage options are only available to AUTHENTICATED
     * users on the call, ANONYMOUS users can not store data
     * to the repository.
     *
     * Post-execution option.
     */
    public ProjectStorageOptions storageOptions;

    /**
     * Optional tag to be associated with the execution that
     * can be used later when viewing the execution history.
     *
     * Post-execution option.
     */
    public String tag;

    /**
     * When enabled meta-data about the execution is not
     * recorded in the DeployR database and the execution
     * does not appear in the RProject execution history.
     */
    public boolean phantom;
}
