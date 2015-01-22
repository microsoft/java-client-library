/*
 * RUserRepositoryFileCalls.java
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
import com.revo.deployr.client.params.RepoUploadOptions;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Defines repository-managed file related interfaces for
 * a DeployR-managed user.
 */
public interface RUserRepositoryFileCalls {

    /**
     * List files in the user's default repository.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RRepositoryFile> listFiles() throws RClientException, RSecurityException;

    /**
     * List files in the user's default repository.
     * If the archived parameter is enabled, then archived files
     * by the user will be included in the response.
     * If the shared parameter is enabled, then files shared by other
     * users will be included in the response. The list of shared files
     * will include any restricted files that the caller can access.
     * If the published parameter is enabled, then files published
     * by other users will be included in the response.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RRepositoryFile> listFiles(boolean archived,
                                           boolean shared,
                                           boolean published)
            throws RClientException, RSecurityException;

    /**
     * List versions of named file in user's default repository.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RRepositoryFile> listFiles(String filename,
                                           String directory)
            throws RClientException, RSecurityException;

    /**
     * List files in the user's default repository using filters to
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
    public List<RRepositoryFile> listFiles(RRepositoryFile.Category categoryFilter,
                                           String directoryFilter)
            throws RClientException, RSecurityException;

   /**
     * List files in the user's external repository.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RRepositoryFile> listExternalFiles()
                        throws RClientException, RSecurityException;

    /**
     * List files in the user's external repository.
     * If the shared parameter is enabled, then external files shared by
     * other users will be included in the response.
     * If the published parameter is enabled, then external files published
     * by other users will be included in the response.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RRepositoryFile> listExternalFiles(boolean shared,
                                                   boolean published)
                        throws RClientException, RSecurityException;

    /**
     * List files in the user's external repository using filters to
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
    public List<RRepositoryFile> listExternalFiles(RRepositoryFile.Category categoryFilter,
                                           String directoryFilter)
            throws RClientException, RSecurityException;
                        
    /**
     * Fetch latest meta-data on repository-managed file.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RRepositoryFile fetchFile(String filename,
                                     String directory,
                                     String author,
                                     String version)
            throws RClientException, RSecurityException;

    /**
     * Upload file to user repository.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RRepositoryFile uploadFile(InputStream fileStream, RepoUploadOptions options)
            throws RClientException, RSecurityException;

    /**
     * Write file to user repository.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RRepositoryFile writeFile(String text, RepoUploadOptions options)
            throws RClientException, RSecurityException;

    /**
     * Transfer file to user repository.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RRepositoryFile transferFile(URL url, RepoUploadOptions options)
            throws RClientException, RSecurityException;

    /**
     * Copy one or more files to a repository-managed directory.
     * The fileRenames parameter is optional. If specified, it
     * can be used to rename each file as it is copied.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void copyFiles(String destinationDirectory,
                          List<RRepositoryFile> files,
                          List<String> fileRenames)
            throws RClientException, RSecurityException;

    /**
     * Move one or more files to a repository-managed directory.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void moveFiles(String destinationDirectory,
                          List<RRepositoryFile> files)
            throws RClientException, RSecurityException;
}
