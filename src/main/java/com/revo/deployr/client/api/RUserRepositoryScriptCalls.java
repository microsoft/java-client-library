/*
 * RUserRepositoryScriptCalls.java
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

import com.revo.deployr.client.RClientException;
import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.RSecurityException;

import java.util.List;

/**
 * Defines the managed script interfaces for DeployR-managed user.
 */
public interface RUserRepositoryScriptCalls {

    /**
     * List scripts in user' default repository.
     *
     * @return List<RRepositoryFile>
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RRepositoryFile> listScripts()
            throws RClientException, RSecurityException;

    /**
     * List scripts in the user's default repository.
     * If the archived parameter is enabled, then archived scripts
     * by the user will be included in the response.
     * If the shared parameter is enabled, then scripts shared by other
     * users will be included in the response. The list of shared scripts
     * will include any restricted scripts that the caller can access.
     * If the published parameter is enabled, then scripts published
     * by other users will be included in the response.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RRepositoryFile> listScripts(boolean archived,
                                             boolean shared,
                                             boolean published)
            throws RClientException, RSecurityException;

    /**
     * List versions of a named script in the user's default repository.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RRepositoryFile> listScripts(String filename,
                                             String directory)
            throws RClientException, RSecurityException;

    /**
     * List scripts in user' external repository.
     *
     * @return List<RRepositoryFile>
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RRepositoryFile> listExternalScripts()
            throws RClientException, RSecurityException;

    /**
     * List scripts in the user's external repository.
     * If the shared parameter is enabled, then external scripts shared by
     * other users will be included in the response.
     * If the published parameter is enabled, then external scripts published
     * by other users will be included in the response.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RRepositoryFile> listExternalScripts(boolean shared,
                                                     boolean published)
            throws RClientException, RSecurityException;
}
