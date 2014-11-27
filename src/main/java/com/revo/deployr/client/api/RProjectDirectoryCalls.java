/*
 * RProjectDirectoryCalls.java
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
import com.revo.deployr.client.RProjectFile;
import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.RSecurityException;
import com.revo.deployr.client.params.DirectoryUploadOptions;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Defines directory related interfaces for DeployR-managed project.
 */
public interface RProjectDirectoryCalls {

    /**
     * List files in project directory.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RProjectFile> listFiles() throws RClientException, RSecurityException;

    /**
     * Upload file to project directory.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RProjectFile uploadFile(InputStream fileStream, DirectoryUploadOptions options) throws RClientException, RSecurityException;

    /**
     * Transfer file to project directory.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RProjectFile transferFile(URL url, DirectoryUploadOptions options) throws RClientException, RSecurityException;

    /**
     * Write file to project directory.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RProjectFile writeFile(String text, DirectoryUploadOptions options) throws RClientException, RSecurityException;

    /**
     * Load file in user repository into project directory.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RProjectFile loadFile(RRepositoryFile file) throws RClientException, RSecurityException;

    /**
     * Download all files from project directory.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public URL downloadFiles() throws RClientException, RSecurityException;

    /**
     * Download files from project directory.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public URL downloadFiles(List<String> files) throws RClientException, RSecurityException;

}
