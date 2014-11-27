/*
 * RProjectFile.java
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
package com.revo.deployr.client;

import com.revo.deployr.client.about.RProjectFileDetails;
import com.revo.deployr.client.params.RepoUploadOptions;

import java.net.URL;

/**
 * Represents a DeployR project directory file.
 */
public interface RProjectFile {

    /**
     * About project directory file.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RProjectFileDetails
     */
    public RProjectFileDetails about() throws RClientException, RSecurityException;

    /**
     * Update project directory file.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RProjectFileDetails
     */
    public RProjectFile update(String name, String descr, boolean overwrite) throws RClientException, RSecurityException;

    /**
     * Store project directory file in repository.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RProjectFileDetails
     */
    public RRepositoryFile store(RepoUploadOptions options) throws RClientException, RSecurityException;

    /**
     * Delete project directory file.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RProjectFileDetails
     */
    public void delete() throws RClientException, RSecurityException;

    /**
     * Download project directory file.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RProjectFileDetails
     */
    public URL download() throws RClientException, RSecurityException;

}
