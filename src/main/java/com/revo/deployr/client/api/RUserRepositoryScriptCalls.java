/*
 * RUserRepositoryScriptCalls.java
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
     * List scripts in user repository.
     *
     * @return List<RRepositoryFile>
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RRepositoryFile> listScripts()
            throws RClientException, RSecurityException;

    /**
     * List scripts including those archived by the caller or those shared or published
     * by other users in the repository. The list of shared scripts will include any
     * restricted scripts that the caller can access.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RRepositoryFile> listScripts(boolean archived,
                                             boolean shared,
                                             boolean published)
            throws RClientException, RSecurityException;

    /**
     * List versions of named script in user repository-managed directory.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RRepositoryFile> listScripts(String filename,
                                             String directory)
            throws RClientException, RSecurityException;

}
