/*
 * RProjectExecuteCalls.java
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
package com.revo.deployr.client.api;

import com.revo.deployr.client.*;
import com.revo.deployr.client.params.ProjectExecutionOptions;

import java.io.InputStream;
import java.util.List;

/**
 * Defines execution related interfaces for DeployR-managed project.
 */
public interface RProjectExecuteCalls {

    /**
     * Execute code on project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RProjectExecution executeCode(String code)
            throws RClientException,
            RSecurityException,
            RDataException,
            RGridException;

    /**
     * Execute code on project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RProjectExecution executeCode(String code,
                                         ProjectExecutionOptions options)
            throws RClientException,
            RSecurityException,
            RDataException,
            RGridException;

    /**
     * Execute a single repository-managed script or a chain of
     * repository-managed scripts on the current project.
     * <p/>
     * To execute a chain of repository-managed scripts on this call provide a comma-separated
     * list of values on the scriptName, scriptAuthor and optionally scriptVersion parameters.
     * Chained execution executes each of the scripts identified on the call in a sequential
     * fashion on the R session, with execution occuring in the order specified on the parameter list.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @deprecated As of release 7.1, use executeScript method that
     * supports scriptDirectory parameter. This deprecated call assumes
     * each script is found in the root directory.
     */
    public RProjectExecution executeScript(String scriptName,
                                           String scriptAuthor,
                                           String scriptVersion)
            throws RClientException,
            RSecurityException,
            RDataException,
            RGridException;

    /**
     * Execute a single repository-managed script or a chain of
     * repository-managed scripts on the current project.
     * <p/>
     * To execute a chain of repository-managed scripts on this call provide a comma-separated
     * list of values on the scriptName, scriptAuthor and optionally scriptVersion parameters.
     * Chained execution executes each of the scripts identified on the call in a sequential
     * fashion on the R session, with execution occuring in the order specified on the parameter list.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @deprecated As of release 7.1, use executeScript method that
     * supports scriptDirectory parameter. This deprecated call assumes
     * each script is found in the root directory.
     */
    public RProjectExecution executeScript(String scriptName,
                                           String scriptAuthor,
                                           String scriptVersion,
                                           ProjectExecutionOptions options)
            throws RClientException,
            RSecurityException,
            RDataException,
            RGridException;


    /**
     * Execute a single repository-managed script or a chain of repository-managed scripts
     * on the current project.
     * <p/>
     * To execute a chain of repository-managed scripts on this call provide a comma-separated
     * list of values on the scriptName, scriptAuthor and optionally scriptVersion parameters.
     * Chained execution executes each of the scripts identified on the call in a sequential
     * fashion on the R session, with execution occuring in the order specified on the parameter list.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RProjectExecution executeScript(String scriptName,
                                           String scriptDirectory,
                                           String scriptAuthor,
                                           String scriptVersion)
            throws RClientException,
            RSecurityException,
            RDataException,
            RGridException;

    /**
     * Execute a single repository-managed script or a chain of repository-managed scripts
     * on the current project.
     * <p/>
     * To execute a chain of repository-managed scripts on this call provide a comma-separated
     * list of values on the scriptName, scriptAuthor and optionally scriptVersion parameters.
     * Chained execution executes each of the scripts identified on the call in a sequential
     * fashion on the R session, with execution occuring in the order specified on the parameter list.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RProjectExecution executeScript(String scriptName,
                                           String scriptDirectory,
                                           String scriptAuthor,
                                           String scriptVersion,
                                           ProjectExecutionOptions options)
            throws RClientException,
            RSecurityException,
            RDataException,
            RGridException;

    /**
     * Execute a single script found on a URL/path or a chain of scripts found on a set of URLs/paths
     * on the current project.
     * <p/>
     * To execute a chain of repository-managed scripts on this call provide a comma-separated
     * list of values on the externalSource parameter.
     * Chained execution executes each of the scripts identified on the call in a sequential
     * fashion on the R session, with execution occuring in the order specified on the parameter list.
     * <p/>
     * POWER_USER privileges are required for this call.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RProjectExecution executeExternal(String externalSource,
                                             ProjectExecutionOptions options)
            throws RClientException,
            RSecurityException,
            RDataException,
            RGridException;

    /**
     * Retrieve console output on latest execution on project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public String getConsole() throws RClientException, RSecurityException;

    /**
     * Interrupt execution on project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */

    public void interruptExecution() throws RClientException, RSecurityException;

    /**
     * Retrieve execution history on project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RProjectExecution> getHistory() throws RClientException, RSecurityException;

    /**
     * Flush execution history on project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void flushHistory() throws RClientException, RSecurityException;

    /**
     * Retreive execution result list on project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RProjectResult> listResults() throws RClientException, RSecurityException;

    /**
     * Delete execution results on project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void deleteResults() throws RClientException, RSecurityException;

    /**
     * Download execution results on project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public InputStream downloadResults() throws RClientException, RSecurityException;

}
