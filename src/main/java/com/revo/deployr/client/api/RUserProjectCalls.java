/*
 * RUserProjectCalls.java
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

import com.revo.deployr.client.*;
import com.revo.deployr.client.params.ProjectCreationOptions;

import java.io.InputStream;
import java.util.List;

/**
 * Defines project related interfaces for DeployR-managed user.
 */
public interface RUserProjectCalls {

    /**
     * Enabled/disable project auto-save semantics for duration of user session.
     *
     * @param save true to enable auto-save of projects on close or logout.
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void autosaveProjects(boolean save) throws RClientException, RSecurityException;

    /**
     * Releases all server-wide live project grid resources associated with
     * this user.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void releaseProjects() throws RClientException, RSecurityException;

    /**
     * Create a temporary project.
     *
     * @return RProject
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RProject createProject()
            throws RClientException, RSecurityException, RDataException, RGridException;

    /**
     * Create a temporary project.
     *
     * @return RProject
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RProject createProject(ProjectCreationOptions options)
            throws RClientException, RSecurityException, RDataException, RGridException;

    /**
     * Create a pool of temporary projects.
     *
     * @return List<RProject>
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RProject> createProjectPool(int poolSize, ProjectCreationOptions options)
            throws RClientException, RSecurityException, RDataException, RGridException;

    /**
     * Create a named persistent project.
     *
     * @return RProject
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RProject createProject(String name, String descr)
            throws RClientException, RSecurityException, RDataException, RGridException;

    /**
     * Create a named persistent project based on adoptions from existing projects.
     *
     * @return RProject
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RProject createProject(String name, String descr, ProjectCreationOptions adoptions)
            throws RClientException, RSecurityException, RDataException, RGridException;

    /**
     * Retrieve project reference.
     *
     * @return RProject
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RProject getProject(String pid)
            throws RClientException, RSecurityException;

    /**
     * List projects.
     *
     * @return RProject
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RProject> listProjects()
            throws RClientException, RSecurityException;

    /**
     * List projects.
     *
     * @param sortByLastModified true is projects to be sorted by last modified date.
     * @param showPublicProjects true is projects list to include public projects.
     * @return RProject
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RProject> listProjects(boolean sortByLastModified, boolean showPublicProjects)
            throws RClientException, RSecurityException;

    /**
     * Import project.
     *
     * @return RProject
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RProject importProject(InputStream fileStream, String descr)
            throws RClientException, RSecurityException;

}
