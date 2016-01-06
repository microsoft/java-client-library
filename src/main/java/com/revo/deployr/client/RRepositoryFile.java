/*
 * RRepositoryFile.java
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
package com.revo.deployr.client;

import com.revo.deployr.client.about.RRepositoryFileDetails;

import java.net.URL;
import java.io.InputStream;
import java.util.List;

/**
 * Represents a DeployR managed repository file.
 */
public interface RRepositoryFile {

    /**
     * About managed repository file.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RRepositoryFileDetails
     */
    public RRepositoryFileDetails about() throws RClientException, RSecurityException;

    /**
     * Retrieve versions of this managed repository file.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RRepositoryFileDetails
     */
    public List<RRepositoryFile> versions() throws RClientException, RSecurityException;

    /**
     * Grant or revoke repository file to/from another user.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RRepositoryFileDetails
     */
    public RRepositoryFile grant(String newauthor, String revokeauthor) throws RClientException, RSecurityException;

    /**
     * Revert repository file to an earlier version.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RRepositoryFileDetails
     */
    public RRepositoryFile revert(RRepositoryFile fileVersion, String descr, String restricted, boolean shared, boolean published) throws RClientException, RSecurityException;

    /**
     * Update repository file.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RRepositoryFileDetails
     */
    public RRepositoryFile update(String restricted, boolean shared, boolean published, String descr) throws RClientException, RSecurityException;


    /**
     * Update repository file.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RRepositoryFileDetails
     */
    public RRepositoryFile update(String restricted, boolean shared, boolean published, String descr, String inputs, String outputs) throws RClientException, RSecurityException;

    /**
     * Diff managed repository file version against the latest version.
     * Available only for text based files in the repository. Generates
     * a HTML encoded response diff.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RRepositoryFileDetails
     */
    public URL diff() throws RClientException, RSecurityException;

    /**
     * Download managed repository file.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RRepositoryFileDetails
     */
    public InputStream download() throws RClientException, RSecurityException;

    /**
     * Delete managed repository file.
     *
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @see RRepositoryFileDetails
     */
    public void delete() throws RClientException, RSecurityException;

    /**
     * Categories of repository-managed file.
     */
    public enum Category {

        DATAFILE("data"),
        GRAPHICSPLOT("plot"),
        OTHER("file"),
        PDFFILE("pdf"),
        RBINARY("R"),
        RSCRIPT("script"),
        SHELLSCRIPT("shell"),
        TEXTFILE("text");

        private Category(String name) {
            this.name = name;
        }

        private final String name;
        
        public String toString() {
            return name;
        }

        public static Category fromString(String name) {
            Category match = Category.OTHER;
            for(Category c : Category.values()) {
                if(c.toString().equalsIgnoreCase(name)) {
                    match = c;
                    break;
                }
            }
            return match;
        }
    }

}
