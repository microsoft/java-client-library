/*
 * RClient.java
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
package com.revo.deployr.client;

import com.revo.deployr.client.auth.RAuthentication;
import com.revo.deployr.client.params.AnonymousProjectExecutionOptions;

import java.net.URL;
import java.util.List;

/**
 * Represents a DeployR client connection.
 * Supports top level DeployR API calls and provides access
 * to the DeployR Service Management objects.
 */
public interface RClient {

    /**
     * Authenticate with the DeployR server.
     *
     * @param pAuthentication - an authentication token.
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RUser
     */
    public RUser login(RAuthentication pAuthentication)
            throws RClientException, RSecurityException;

    /**
     * Authenticate with the DeployR server.
     *
     * @param pAuthentication - an authentication token.
     * @param disableautosave - when true, disables persistent project auto-save semantics for duration of user session.
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RUser login(RAuthentication pAuthentication, boolean disableautosave)
            throws RClientException, RSecurityException;


    /**
     * Logout from the DeployR server.
     *
     * @param user a managed user reference
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void logout(RUser user) throws RClientException, RSecurityException;

    /**
     * Execute a single repository-managed script or a chain of
     * repository-managed scripts on an anonymous project.
     * <p/>
     * To execute a chain of repository-managed scripts on this call provide a comma-separated
     * list of values on the scriptName, scriptAuthor and optionally scriptVersion parameters.
     * Chained execution executes each of the scripts identified on the call in a sequential
     * fashion on the R session, with execution occuring in the order specified on the parameter list.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RScriptExecution
     * @deprecated As of release 7.1, use executeScript method that
     * supports scriptDirectory parameter. This deprecated call assumes
     * each script is found in the root directory.
     */
    @Deprecated
    public RScriptExecution executeScript(String scriptName,
                                          String scriptAuthor,
                                          String scriptVersion)
            throws RClientException,
            RSecurityException,
            RDataException,
            RGridException;


    /**
     * Execute a single repository-managed script or a chain of
     * repository-managed scripts on an anonymous project.
     * <p/>
     * To execute a chain of repository-managed scripts on this call provide a comma-separated
     * list of values on the scriptName, scriptAuthor and optionally scriptVersion parameters.
     * Chained execution executes each of the scripts identified on the call in a sequential
     * fashion on the R session, with execution occuring in the order specified on the parameter list.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RScriptExecution
     * @deprecated As of release 7.1, use executeScript method that
     * supports scriptDirectory parameter. This deprecated call assumes
     * each script is found in the root directory.
     */
    @Deprecated
    public RScriptExecution executeScript(String scriptName,
                                          String scriptAuthor,
                                          String scriptVersion,
                                          AnonymousProjectExecutionOptions options)
            throws RClientException,
            RSecurityException,
            RDataException,
            RGridException;

    /**
     * Execute a single repository-managed script or a chain of repository-managed scripts
     * on an anonymous project.
     * <p/>
     * To execute a chain of repository-managed scripts on this call provide a comma-separated
     * list of values on the scriptName, scriptAuthor and optionally scriptVersion parameters.
     * Chained execution executes each of the scripts identified on the call in a sequential
     * fashion on the R session, with execution occuring in the order specified on the parameter list.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RScriptExecution
     */
    public RScriptExecution executeScript(String scriptName,
                                          String scriptDirectory,
                                          String scriptAuthor,
                                          String scriptVersion)
            throws RClientException,
            RSecurityException,
            RDataException,
            RGridException;

    /**
     * Execute a single repository-managed script or a chain of repository-managed scripts
     * on an anonymous project.
     * <p/>
     * To execute a chain of repository-managed scripts on this call provide a comma-separated
     * list of values on the scriptName, scriptAuthor and optionally scriptVersion parameters.
     * Chained execution executes each of the scripts identified on the call in a sequential
     * fashion on the R session, with execution occuring in the order specified on the parameter list.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RScriptExecution
     */
    public RScriptExecution executeScript(String scriptName,
                                          String scriptDirectory,
                                          String scriptAuthor,
                                          String scriptVersion,
                                          AnonymousProjectExecutionOptions options)
            throws RClientException,
            RSecurityException,
            RDataException,
            RGridException;

    /**
     * Execute a single script found on a URL/path or a chain of scripts found on a set of URLs/paths
     * on an anonymous project.
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
     * @see RScriptExecution
     */
    public RScriptExecution executeExternal(String externalSource,
                                            AnonymousProjectExecutionOptions options)
            throws RClientException,
            RSecurityException,
            RDataException,
            RGridException;

    /**
     * Execute a single repository-managed script or a chain of repository-managed scripts
     * on an anonymous project and render the outputs to a HTML page.
     * <p/>
     * To execute a chain of repository-managed scripts on this call provide a comma-separated
     * list of values on the scriptName, scriptAuthor and optionally scriptVersion parameters.
     * Chained execution executes each of the scripts identified on the call in a sequential
     * fashion on the R session, with execution occuring in the order specified on the parameter list.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RScriptExecution
     */
    public URL renderScript(String scriptName,
                            String scriptDirectory,
                            String scriptAuthor,
                            String scriptVersion,
                            AnonymousProjectExecutionOptions options)
            throws RClientException,
            RDataException;

    /**
     * Execute a repository-managed shell script. Execution occurs on the
     * DeployR server. The response captures the line-by-line console output
     * generated by the execution of the shell script on the server.
     * Only shell scripts created by an ADMIN user on the DeployR server
     * can be executed on this call. All attempts to execute shell scripts
     * not created by an ADMIN user will be rejected. Access to shell
     * scripts is also governed by standard repository access controls.
     * <p/>
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<String> executeShell(String shellName,
                                     String shellDirectory,
                                     String shellAuthor,
                                     String shellVersion,
                                     String args)
            throws RClientException,
            RSecurityException,
            RDataException;

    /**
     * Interrupts the current execution on the HTTP blackbox project associated
     * with the current HTTP session.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RScriptExecution
     */
    public void interruptScript() throws RClientException, RSecurityException;

    /**
     * Shutdown the client connection releasing any resources
     * associated with the connection. If needed, the user will
     * be automatically logged out as a result of this call.
     */
    public void release();

}
