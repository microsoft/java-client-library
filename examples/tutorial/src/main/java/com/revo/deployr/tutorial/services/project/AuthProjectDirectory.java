/*
 * AuthProjectDirectory.java
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
package com.revo.deployr.tutorial.services.project;

import com.revo.deployr.client.RClient;
import com.revo.deployr.client.RProject;
import com.revo.deployr.client.RProjectFile;
import com.revo.deployr.client.RUser;
import com.revo.deployr.client.auth.RAuthentication;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.factory.RClientFactory;
import com.revo.deployr.client.params.DirectoryUploadOptions;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.List;

public class AuthProjectDirectory {

    private static Logger log = Logger.getLogger(AuthProjectDirectory.class);

    public static void main(String args[]) throws Exception {

        RClient rClient = null;

        try {

            /*
             * Determine DeployR server endpoint.
             */
            String endpoint = System.getProperty("endpoint");
            log.info("Using endpoint=" + endpoint);

            /*
             * Establish RClient connection to DeployR server.
             *
             * An RClient connection is the mandatory starting
             * point for any application using the client library.
             */
            rClient = RClientFactory.createClient(endpoint);

            log.info("Established anonymous " +
                    "connection, rClient=" + rClient);

            /*
             * Build a basic authentication token.
             */
            RAuthentication rAuth =
                    new RBasicAuthentication(System.getProperty("username"),
                            System.getProperty("password"));

            /*
             * Establish an authenticated handle with the DeployR
             * server, rUser.
             */
            RUser rUser = rClient.login(rAuth);
            log.info("Established authenticated " +
                    "connection, rUser=" + rUser);

            /*
             * Create a temporary project (R session).
             *
             * Optionally:
             * ProjectCreationOptions options =
             * new ProjectCreationOptions();
             *
             * Populate options as needed, then:
             *
             * rProject = rUser.createProject(options);
             */
            RProject rProject = rUser.createProject();

            log.info("Created temporary " +
                    "R session, rProject=" + rProject);

            /*
             * Create a file in the R session's working directory
             * by writing text to a named file.
             */
            DirectoryUploadOptions options = new DirectoryUploadOptions();
            options.filename = "hello.txt";
            String fileContent = "Hello World!";
            RProjectFile rProjectFile =
                    rProject.writeFile(fileContent, options);

            /*
             * Download working directory file content using 
             * standard Java InputStream.
             */
            InputStream fileStream = null;
            try {
                fileStream = rProjectFile.download();
            } finally {
                if(fileStream != null)
                    fileStream.close();
            }

            /*
             * Retrieve a list of files in the R session's
             * working directory.
             */
            List<RProjectFile> files = rProject.listFiles();

            for (RProjectFile file : files) {
                log.info("Working directory, " +
                        "found file name=" +
                        file.about().filename + ", type=" +
                        file.about().type + ", size=" +
                        file.about().size);

                try {
                    file.delete();
                    log.info("Working directory, " +
                            "deleted file name=" +
                            file.about().filename + ", type=" +
                            file.about().type + ", size=" +
                            file.about().size);
                } catch (Exception dex) {
                    log.warn("File=" + file +
                            ", deletion ex=" + dex);
                }
            }

        } catch (Exception ex) {
            log.warn("Runtime exception=" + ex);
        } finally {
            try {
                if (rClient != null) {
                    /*
                     * Release rClient connection before application exits.
                     */
                    rClient.release();
                }
            } catch (Exception fex) {
            }
        }
    }

}
