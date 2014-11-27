/*
 * RUserRepositoryDirectoryCalls.java
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
import com.revo.deployr.client.RRepositoryDirectory;
import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.RSecurityException;
import com.revo.deployr.client.params.RepoUploadOptions;

import java.io.InputStream;
import java.util.List;

/**
 * tori
 * Defines repository-managed directory related interfaces for
 * DeployR-managed user.
 */
public interface RUserRepositoryDirectoryCalls {

    /**
     * List repository-managed directories.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions
     *                            not met on call.
     */
    public List<RRepositoryDirectory> listDirectories()
            throws RClientException, RSecurityException;

    /**
     * List repository-managed directories.
     * If the archived parameter is enabled, then files in the user archive
     * directories are included in the response.
     * If the shared parameter is enabled, then files in the system shared
     * directory is included in the response.
     * If the published parameter is enabled, then files in the system
     * published directory is included in the response.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions
     *                            not met on call.
     */
    public List<RRepositoryDirectory> listDirectories(boolean userfiles,
                                                      boolean archived,
                                                      boolean shared,
                                                      boolean published)
            throws RClientException, RSecurityException;

    /**
     * Creates a new repository-managed custom user directory.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RRepositoryDirectory createDirectory(String directory)
            throws RClientException, RSecurityException;

    /**
     * Copies one or more repository-managed files from a source
     * user directory to a destination user directory.
     * <p/>
     * If the files parameter is null, all files in the source
     * directory will be copied to the destination directory.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void copyDirectory(String sourceDirectory,
                              String destinationDirectory,
                              List<RRepositoryFile> files)
            throws RClientException, RSecurityException;

    /**
     * Moves one or more repository-managed files from a source
     * user directory to a destination user directory.
     * <p/>
     * If the files parameter is null, all files in the source
     * directory will be moved to the destination directory.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void moveDirectory(String sourceDirectory,
                              String destinationDirectory,
                              List<RRepositoryFile> files)
            throws RClientException, RSecurityException;

    /**
     * Uploads a set of files in a single zip archive into an existing
     * repository-managed user directory. The files are extracted from
     * the zip archive and placed file-by-file into the directory.
     * <p/>
     * The options.filename property is ignored on this call and
     * can be left blank.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void uploadDirectory(InputStream zipStream,
                                RepoUploadOptions options)
            throws RClientException, RSecurityException;

}
