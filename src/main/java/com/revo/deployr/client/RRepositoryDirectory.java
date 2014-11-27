/*
 * RRepositoryDirectory.java
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

import com.revo.deployr.client.about.RRepositoryDirectoryDetails;
import com.revo.deployr.client.params.RepoAccessControlOptions;
import com.revo.deployr.client.params.RepoUploadOptions;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Represents a DeployR repository-managed directory.
 */
public interface RRepositoryDirectory {

    public static final String ROOT = "root";

    /**
     * About managed repository directory.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RRepositoryDirectoryDetails
     */
    public RRepositoryDirectoryDetails about() throws RClientException, RSecurityException;

    /**
     * Update access-controls on files found in repository-managed user directory.
     * <p/>
     * If the files parameter is null, all files in the directory are updated.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void update(RepoAccessControlOptions options,
                       List<RRepositoryFile> files)
            throws RClientException, RSecurityException;

    /**
     * Upload files in a zip archive to repository-managed user directory.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void upload(InputStream fileStream, RepoUploadOptions options)
            throws RClientException, RSecurityException;

    /**
     * Archive files found in repository-managed user directory.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RUser
     */
    public RRepositoryDirectory archive(String archiveDirectoryName,
                                        List<RRepositoryFile> files)
            throws RClientException, RSecurityException;

    /**
     * Rename repository-managed user directory.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RRepositoryDirectory rename(String newDirectoryName)
            throws RClientException, RSecurityException;

    /**
     * Download zip archive of files found in repository-managed user directory.
     * <p/>
     * If the files parameter is null, all files in the directory are downloaded.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public URL download(List<RRepositoryFile> files) throws RClientException, RSecurityException;

    /**
     * Delete repository-managed user directory.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RRepositoryDirectoryDetails
     */
    public void delete() throws RClientException, RSecurityException;


}
