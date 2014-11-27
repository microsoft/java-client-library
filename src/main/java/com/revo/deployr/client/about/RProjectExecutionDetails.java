/*
 * RProjectExecutionDetails.java
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

import com.revo.deployr.client.RProjectFile;
import com.revo.deployr.client.RProjectResult;
import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.data.RData;

import java.util.List;

/**
 * Project execution details.
 */
public class RProjectExecutionDetails {

    public RProjectExecutionDetails(String id, String code, long timeStart, long timeCode, long timeTotal, String tag, String console, String error, int errorCode, List<String> warnings, boolean interrupted, List<RProjectResult> results, List<RProjectFile> artifacts, List<RData> workspaceObjects, List<RRepositoryFile> repositoryFiles) {

        this.id = id;
        this.code = code;
        this.timeStart = timeStart;
        this.timeCode = timeCode;
        this.timeTotal = timeTotal;
        this.tag = tag;
        this.console = console;
        this.error = error;
        this.errorCode = errorCode;
        this.warnings = warnings;
        this.interrupted = interrupted;
        this.results = results;
        this.artifacts = artifacts;
        this.workspaceObjects = workspaceObjects;
        this.repositoryFiles = repositoryFiles;
    }

    /**
     * Project execution id.
     */
    public final String id;

    /**
     * Code on R execution.
     */
    public final String code;

    /**
     * Start time (millis) for execution. Can be used in conjunction with
     * timeCode and timeTotal to profile execution runtime characteristics.
     */
    public final long timeStart;

    /**
     * Code execution time (millis) for execution. Measures the time taken to
     * execute the R code for execution.
     * <p/>
     * Can be used in conjunction with timeStart and timeTotal to profile
     * execution runtime characteristics.
     */
    public final long timeCode;

    /**
     * Total time (millis) for execution. Measures the time taken to prepare
     * R workspace and directory for execution, execute R code and then
     * persist execution artifacts to on project.
     * <p/>
     * Can be used in conjunction with timeStart and timeCode to profile
     * execution runtime characteristics.
     */
    public final long timeTotal;

    /**
     * Tag associated with R execution.
     */
    public final String tag;

    /**
     * Console output resulting from R execution.
     */
    public final String console;

    /**
     * Error message resulting from failed R execution.
     */
    public String error;

    /**
     * Error code resulting from failed R execution.
     */
    public int errorCode;

    /**
     * Warning messages resulting from R execution.
     */
    public List<String> warnings;

    /**
     * Interrupted status on R execution.
     */
    public boolean interrupted;

    /**
     * Generated artifacts on R execution.
     */
    public List<RProjectFile> artifacts;

    /**
     * Generated results on R execution.
     */
    public List<RProjectResult> results;

    /**
     * RData encoded workspace objects on R execution.
     */
    public final List<RData> workspaceObjects;

    /**
     * RRepositoryFile references on R execution.
     */
    public final List<RRepositoryFile> repositoryFiles;

}
