/*
 * AnonProjectExecuteScriptInOut.java
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
import com.revo.deployr.client.data.RNumericVector;
import com.revo.deployr.client.factory.RClientFactory;
import com.revo.deployr.client.factory.RDataFactory;
import com.revo.deployr.client.params.AnonymousProjectExecutionOptions;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class AnonProjectExecuteScriptInOut {

    private static Logger log = Logger.getLogger(AnonProjectExecuteScriptInOut.class);

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
             * /testuser/root/DeployR - Hello World.R
             *
             * Create the AnonymousProjectExecutionOptions object·
             * to specify inputs and output to the script.
             * The R object that is an input to the script is:
             * 'input_randomNum'.
             * The R object that we want to retrieve after script execution is:
             * 'x'.
             */
            AnonymousProjectExecutionOptions options =
                    new AnonymousProjectExecutionOptions();
            RData randNum = RDataFactory.createNumeric("input_randomNum", 100);
            options.rinputs = Arrays.asList(randNum);
            options.routputs = Arrays.asList("x");
            RScriptExecution exec =
                    rClient.executeScript("DeployR - Hello World.R",
                            "root", "testuser", null, options);

            log.info("Public " +
                    "repository-managed script execution completed, " +
                    "rScriptExecution=" + exec);

            /*
             * Retrieve script execution results.
             */
            String console = exec.about().console;
            List<RProjectResult> plots = exec.about().results;
            List<RProjectFile> files = exec.about().artifacts;
            List<RData> objects = exec.about().workspaceObjects;

            RNumericVector xVec = (RNumericVector) objects.get(0);

            log.info("Public " +
                    "repository-managed script execution completed, " +
                    "retrieved encoded R object=" + xVec);


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
