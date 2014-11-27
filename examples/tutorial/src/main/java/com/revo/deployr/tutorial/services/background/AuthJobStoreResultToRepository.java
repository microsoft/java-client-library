/*
 * AuthJobStoreResultToRepository.java
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
package com.revo.deployr.tutorial.services.background;

import com.revo.deployr.client.RClient;
import com.revo.deployr.client.RJob;
import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.RUser;
import com.revo.deployr.client.about.RJobDetails;
import com.revo.deployr.client.auth.RAuthentication;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.factory.RClientFactory;
import com.revo.deployr.client.params.JobExecutionOptions;
import com.revo.deployr.client.params.ProjectStorageOptions;
import org.apache.log4j.Logger;

public class AuthJobStoreResultToRepository {

    private static Logger log = Logger.getLogger(AuthJobStoreResultToRepository.class);

    public static void main(String args[]) throws Exception {

        RClient rClient = null;
        RJob rJob = null;
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
             * Submit a background job to execute a block of R code
             * that will generate a vector object. We will then use
             * JobExecutionOtpions to request the following behavior:
             *
             * 1. Execute the job at low (default) priority.
             * 2. Skip the persistence of results to a project.
             * 3. Request the persistence of a named vector object
             * as a binary R object file to the repository.
             */

            String rCode = "tutorialVector <- rnorm(100)";

            JobExecutionOptions options = new JobExecutionOptions();
            options.priority = JobExecutionOptions.LOW_PRIORITY;
            options.noproject = true;
            /*
             * Use ProjectStorageOptions to identify the name of one
             * or more workspace objects for storage to the repository.
             *
             * In this case, the named object matches the name of the
             * object created the rCode being executed on the job. We
             * request that the binary R object file be stored into
             * the root directory in the repository.
             */
            options.storageOptions = new ProjectStorageOptions();
            options.storageOptions.objects = "tutorialVector";
            options.storageOptions.directory = "root";

            rJob = rUser.submitJobCode("Background Code Execution",
                    "Demonstrate storing job results to repository.",
                    rCode, options);

            log.info("Submitted background job " +
                    "for execution, rJob=" + rJob);

            try {

                boolean jobPending = true;

                while (jobPending) {

                    RJobDetails details = rJob.query();

                    if (details.status.equals(RJob.COMPLETED)) {

                        jobPending = false;

                        /*
                         * Retrieve the results of the background job
                         * execution. Given the custom JobExecutionOptions
                         * specified for this job (noproject=true) there will
                         * be no RProject to hold the results.
                         *
                         * However, the custom JobExecutionOptions specified
                         * for this job requested the storage of the 
                         * "tutorialVector" vectory object as a binary R
                         * object file (.rData) in the repository.
                         *
                         * The following code demonstrates how we can
                         * retrieve that result from the repository:
                         */
                        rRepositoryFile =
                                rUser.fetchFile("tutorialVector.rData",
                                        "root",
                                        System.getProperty("username"),
                                        null);

                        log.info("Retrieved background " +
                                "job result from repository, rRepositoryFile=" + rRepositoryFile);

                    } else {
                        Thread.currentThread().sleep(500);
                    }
                }

            } catch (Exception wex) {
                log.warn("Waiting for background job " +
                        "execution to complete, ex=" + wex);
            }

        } catch (Exception ex) {

            log.warn("Runtime exception=" + ex);

        } finally {

            try {
                rJob.delete();
            } catch (Exception pex) {
            }

            try {
                rRepositoryFile.delete();
            } catch (Exception pex) {
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
