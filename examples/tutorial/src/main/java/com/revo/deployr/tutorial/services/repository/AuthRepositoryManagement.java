/*
 * AuthRepositoryManagement.java
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
package com.revo.deployr.tutorial.services.repository;

import com.revo.deployr.client.RClient;
import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.RUser;
import com.revo.deployr.client.auth.RAuthentication;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.factory.RClientFactory;
import com.revo.deployr.client.params.RepoUploadOptions;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.List;

public class AuthRepositoryManagement {

    private static Logger log = Logger.getLogger(AuthRepositoryManagement.class);

    public static void main(String args[]) throws Exception {

        RClient rClient = null;
        RRepositoryFile rRepositoryFile = null;

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
             * Create a file in the authenticated user's private
             * repository and set shared access on the file so
             * other authenticated users can access the file.
             */
            RepoUploadOptions options = new RepoUploadOptions();
            options.filename = "hello.txt";
            options.shared = true;
            String fileContent = "Hello World!";
            rRepositoryFile = rUser.writeFile(fileContent, options);

            /*
             * Download working directory file content using 
             * standard Java URL.
             */
            InputStream downStream = rRepositoryFile.download();
            downStream.close();

            /*
             * Retrieve a list of files in the authenticated user's
             * private repository.
             */
            List<RRepositoryFile> files = rUser.listFiles();

            for (RRepositoryFile file : files) {
                log.info("Private repository, " +
                        "found file name=" +
                        file.about().filename + ", directory=" +
                        file.about().directory + ", access=" +
                        file.about().access);
            }

        } catch (Exception ex) {

            log.warn("Runtime exception=" + ex);

        } finally {

            try {
                rRepositoryFile.delete();
            } catch (Exception rex) {
            }

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
