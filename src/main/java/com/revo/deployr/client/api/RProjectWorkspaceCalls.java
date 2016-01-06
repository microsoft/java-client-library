/*
 * RProjectWorkspaceCalls.java
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
import com.revo.deployr.client.data.RData;
import com.revo.deployr.client.params.ProjectWorkspaceOptions;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Defines workspace related interfaces for DeployR managed project.
 */
public interface RProjectWorkspaceCalls {

    /**
     * List objects in project workspace.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RData> listObjects() throws RClientException, RSecurityException;

    /**
     * List objects in project workspace.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RData> listObjects(ProjectWorkspaceOptions options) throws RClientException, RSecurityException;

    /**
     * Retrieve encoded objects from project workspace.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RData getObject(String objectName) throws RClientException, RSecurityException;

    /**
     * Retrieve encoded objects from project workspace.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RData getObject(String objectName, boolean encodeDataFramePrimitiveAsVector) throws RClientException, RSecurityException;

    /**
     * Retrieve encoded objects from project workspace.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RData> getObjects(List<String> objectNames) throws RClientException, RSecurityException;

    /**
     * Retrieve encoded objects from project workspace.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RData> getObjects(List<String> objectNames, boolean encodeDataFramePrimitiveAsVector) throws RClientException, RSecurityException;

    /**
     * Upload object to project workspace.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void uploadObject(String name, InputStream fileStream) throws RClientException, RSecurityException;

    /**
     * Transfer object to project workspace.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void transferObject(String name, URL url) throws RClientException, RSecurityException;

    /**
     * Push object to project workspace.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void pushObject(RData object) throws RClientException, RSecurityException, RDataException;

    /**
     * Push objects to project workspace.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void pushObject(List<RData> objects) throws RClientException, RSecurityException, RDataException;

    /**
     * Save object from project workspace to project directory.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RProjectFile saveObject(String name, String descr, boolean versioning) throws RClientException, RSecurityException;

    /**
     * Store object from project workspace to user repository.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RRepositoryFile storeObject(String name, String descr, boolean versioning, String restricted, boolean shared, boolean published) throws RClientException, RSecurityException;

    /**
     * Load user repository object into project workspace.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void loadObject(RRepositoryFile file) throws RClientException, RSecurityException;

    /**
     * Delete objects in project workspace.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void deleteObject(String objectName) throws RClientException, RSecurityException;

    /**
     * Delete objects in project workspace.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void deleteObject(List<String> objectNames) throws RClientException, RSecurityException;

}
