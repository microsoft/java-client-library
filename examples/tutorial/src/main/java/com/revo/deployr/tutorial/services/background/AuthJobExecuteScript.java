/*
 * AuthJobExecuteScript.java
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
import com.revo.deployr.client.RProject;
import com.revo.deployr.client.RUser;
import com.revo.deployr.client.about.RJobDetails;
import com.revo.deployr.client.auth.RAuthentication;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.factory.RClientFactory;
import com.revo.deployr.client.params.JobExecutionOptions;
import org.apache.log4j.Logger;

public class AuthJobExecuteScript {

    private static Logger log = Logger.getLogger(AuthJobExecuteScript.class);

    public static void main(String args[]) throws Exception {

        RClient rClient = null;
        RJob rJob = null;
        RProject rProject = null;

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
             * Submit a background job to execute a repository-managed
             * script: /testuser/root/Histogram of Auto Sales.R.
             */
            JobExecutionOptions options = new JobExecutionOptions();
            options.priority = JobExecutionOptions.HIGH_PRIORITY;

            rJob = rUser.submitJobScript("Background Script Execution",
                    "Background script execution.",
                    "Histogram of Auto Sales",
                    "root", "testuser", null,
                    options);

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
                         * execution. Based on the JobExecutionOptions
                         * for this example the results are found
                         * within an RProject generated on the
                         * successful completion of the job.
                         */
                        rProject = rUser.getProject(details.project);

                        log.info("Retrieved background " +
                                "job result on project, rProject=" + rProject);

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
                rProject.delete();
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
