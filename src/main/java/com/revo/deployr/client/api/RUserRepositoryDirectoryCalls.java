/*
 * RUserRepositoryDirectoryCalls.java
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
import com.revo.deployr.client.RRepositoryDirectory;
import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.RSecurityException;
import com.revo.deployr.client.params.RepoUploadOptions;

import java.io.InputStream;
import java.util.List;

/**
 * Defines repository-managed directory related interfaces for
 * DeployR-managed user.
 */
public interface RUserRepositoryDirectoryCalls {

    /**
     * List directories in the user's default repository.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions
     *                            not met on call.
     */
    public List<RRepositoryDirectory> listDirectories()
            throws RClientException, RSecurityException;

    /**
     * List directories in the user's default repository.
     * If the userfiles parameter is enabled, then files in the user's
     * directories are included in the response.
     * If the archived parameter is enabled, then files in the user's
     * archive directories are included in the response.
     * If the shared parameter is enabled, then files shared by other
     * users will be included in the response.
     * If the published parameter is enabled, then files shared by other
     * users will be included in the response.
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
     * List directories in the user's default repository using filters to
     * constrain the files in the response markup.
     * If categoryFilter is specified only files matching the Category
     * indicated will be included in the response.
     * If directoryFilter is specified then only files found in the
     * directory indicated will be included in the response.
     * If both categoryFilter and directoryFilter are specified then
     * only files matching the Category within the directory indicated
     * will be included in the response.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RRepositoryDirectory> listDirectories(RRepositoryFile.Category categoryFilter,
                                           String directoryFilter)
            throws RClientException, RSecurityException;

    /**
     * List directories in the user's external repository.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions
     *                            not met on call.
     */
    public List<RRepositoryDirectory> listExternalDirectories()
                            throws RClientException, RSecurityException;

    /**
     * List directories in the user's external repository.
     * If the userfiles parameter is enabled, then files in the user's
     * external directories are included in the response.
     * If the shared parameter is enabled, then external files shared
     * by other users will be included in the response.
     * If the published parameter is enabled, then external files published
     * by other users will be included in the response.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions
     *                            not met on call.
     */
    public List<RRepositoryDirectory> listExternalDirectories(boolean userfiles,
                                                              boolean shared,
                                                              boolean published)
                                throws RClientException, RSecurityException;

    /**
     * List directories in the user's external repository using filters to
     * constrain the files in the response markup.
     * If categoryFilter is specified only files matching the Category
     * indicated will be included in the response.
     * If directoryFilter is specified then only files found in the
     * external directory indicated will be included in the response.
     * If both categoryFilter and directoryFilter are specified then
     * only files matching the Category within the external directory
     * indicated will be included in the response.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RRepositoryDirectory> listExternalDirectories(RRepositoryFile.Category categoryFilter,
                                           String directoryFilter)
            throws RClientException, RSecurityException;

    /**
     * Creates a new custom user directory in the default repository.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RRepositoryDirectory createDirectory(String directory)
            throws RClientException, RSecurityException;

    /**
     * Copies one or more repository-managed files from a source
     * user directory to a destination user directory.
     * <p>
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
     * <p>
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
     * <p>
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
