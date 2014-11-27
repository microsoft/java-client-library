/*
 * RProjectPackageCalls.java
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
import com.revo.deployr.client.RProjectPackage;
import com.revo.deployr.client.RSecurityException;

import java.util.List;

/**
 * Defines package related interfaces for DeployR-managed project.
 */
public interface RProjectPackageCalls {

    /**
     * List R packages on project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RProjectPackage> listPackages(boolean installed) throws RClientException, RSecurityException;

    /**
     * Attach R packages to project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RProjectPackage> attachPackage(String packageName, String repo) throws RClientException, RSecurityException;

    /**
     * Attach R packages to project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RProjectPackage> attachPackage(List<String> packageNames, String repo) throws RClientException, RSecurityException;

    /**
     * Detach R packages from project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RProjectPackage> detachPackage(String packageName) throws RClientException, RSecurityException;

    /**
     * Detach R packages from project.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RProjectPackage> detachPackage(List<String> packageNames) throws RClientException, RSecurityException;

}
