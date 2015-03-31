/*
 * RProject.java
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

import com.revo.deployr.client.about.RProjectDetails;
import com.revo.deployr.client.api.RProjectDirectoryCalls;
import com.revo.deployr.client.api.RProjectExecuteCalls;
import com.revo.deployr.client.api.RProjectPackageCalls;
import com.revo.deployr.client.api.RProjectWorkspaceCalls;
import com.revo.deployr.client.params.ProjectCloseOptions;
import com.revo.deployr.client.params.ProjectDropOptions;

import java.io.InputStream;

/**
 * Represents a DeployR managed project.
 */
public interface RProject extends
        RProjectExecuteCalls, RProjectWorkspaceCalls, RProjectDirectoryCalls, RProjectPackageCalls {

    /**
     * About project.
     *
     * @see RProjectDetails
     */
    public RProjectDetails about();

    /**
     * Ping project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public boolean ping() throws RClientException, RSecurityException;

    /**
     * Update project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RProjectDetails
     */
    public RProjectDetails update(RProjectDetails about) throws RClientException, RSecurityException;

    /**
     * Grant project authorship to another user.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RProjectDetails
     */
    public RProjectDetails grant(String username) throws RClientException, RSecurityException;

    /**
     * Save project.
     *
     * @return RProject
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RProjectDetails
     */
    public RProjectDetails save() throws RClientException, RSecurityException;

    /**
     * Save project.
     *
     * @return RProject
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RProjectDetails
     */
    public RProjectDetails save(RProjectDetails details) throws RClientException, RSecurityException;

    /**
     * Save project.
     *
     * @return RProject
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RProjectDetails
     */
    public RProjectDetails save(RProjectDetails details, ProjectDropOptions dropOptions) throws RClientException, RSecurityException;

    /**
     * Save-as project.
     *
     * @return RProject
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RProject
     */
    public RProject saveAs(RProjectDetails details) throws RClientException, RSecurityException;

    /**
     * Save-as project.
     *
     * @return RProject
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RProject
     */
    public RProject saveAs(RProjectDetails details, ProjectDropOptions dropOptions) throws RClientException, RSecurityException;

    /**
     * Recycle R session on project.
     * <p/>
     * Recycles the R session associated with the project by deleting all
     * R objects from the workspace and all files from the working directory.
     * <p/>
     * Recommended for temporary and blackbox projects. Recycle persistent projects
     * with caution as this operation can not be reversed.
     *
     * @return RProject
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RProjectDetails
     */
    public RProjectDetails recycle()
            throws RClientException, RSecurityException;

    /**
     * Recycle R session on project.
     * <p/>
     * Recycles the R session associated with the project by deleting all
     * R objects from the workspace and all files from the working directory.
     * <p/>
     * Workspace objects and/or directory files can be preserved on a recycle
     * by enabling the respective parameter.
     * <p/>
     * Recycling a project is a convenient and efficient alternative to
     * starting over by closing an existing project and then creating a new project.
     * <p/>
     * Recommended for temporary and blackbox projects. Recycle persistent projects
     * with caution as this operation can not be reversed.
     *
     * @return RProject
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RProjectDetails
     */
    public RProjectDetails recycle(boolean preserveWorkspace,
                                   boolean preserveDirectory)
            throws RClientException, RSecurityException;

    /**
     * Close project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void close() throws RClientException, RSecurityException;

    /**
     * Close project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see ProjectCloseOptions
     */
    public void close(ProjectCloseOptions options) throws RClientException, RSecurityException;

    /**
     * Delete project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public void delete() throws RClientException, RSecurityException;

    /**
     * Export project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public InputStream export() throws RClientException, RSecurityException;

}
