/*
 * AnonProjectExecuteScript.java
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
package com.revo.deployr.tutorial.services.project;

import com.revo.deployr.client.RClient;
import com.revo.deployr.client.RProjectFile;
import com.revo.deployr.client.RProjectResult;
import com.revo.deployr.client.RScriptExecution;
import com.revo.deployr.client.data.RData;
import com.revo.deployr.client.factory.RClientFactory;
import org.apache.log4j.Logger;

import java.util.List;

public class AnonProjectExecuteScript {

    private static Logger log = Logger.getLogger(AnonProjectExecuteScript.class);

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
             * Execute a public analytics Web service as an anonymous
             * user based on a repository-managed R script:
             * /testuser/root/Histogram of Auto Sales.R.
             *
             * Optionally:
             * AnonymousProjectExecutionOptions options =
             * new AnonymousProjectExecutionOptions();
             *
             * Populate options as needed, then:
             *
             * RScriptExecution exec =
             * rClient.executeScript(filename, directory, author options);
             */
            RScriptExecution exec =
                    rClient.executeScript("Histogram of Auto Sales",
                            "root", "testuser", null,
                            null);

            log.info("Public repository-managed " +
                    "script execution completed, rScriptExecution=" + exec);

            /*
             * Retrieve script execution results.
             */
            String console = exec.about().console;
            List<RProjectResult> plots = exec.about().results;
            List<RProjectFile> files = exec.about().artifacts;
            List<RData> objects = exec.about().workspaceObjects;


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
