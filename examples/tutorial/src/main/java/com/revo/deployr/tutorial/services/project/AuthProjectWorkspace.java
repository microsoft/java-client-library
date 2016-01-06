/*
 * AuthProjectWorkspace.java
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
import com.revo.deployr.client.RProjectExecution;
import com.revo.deployr.client.RUser;
import com.revo.deployr.client.auth.RAuthentication;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.data.RBoolean;
import com.revo.deployr.client.data.RData;
import com.revo.deployr.client.data.RNumericVector;
import com.revo.deployr.client.factory.RClientFactory;
import com.revo.deployr.client.factory.RDataFactory;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class AuthProjectWorkspace {

    private static Logger log = Logger.getLogger(AuthProjectWorkspace.class);

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
             * Execute a block of R code to create an object
             * in the R session's workspace.
             */
            String rCode = "x <- T";
            RProjectExecution exec = rProject.executeCode(rCode);

            /*
             * Retrieve the object "x" from the R session's workspace.
             */
            RData encodedX = rProject.getObject("x");

            log.info("Retrieved object x " +
                    "from workspace, encodedX=" + encodedX);

            if (encodedX instanceof RBoolean) {
                RBoolean xBool = (RBoolean) encodedX;
                Boolean xValue = xBool.getValue();
            }

            /*
             * Create R object data in the R sesssion's workspace
             * by pushing DeployR-encoded data from the client application.
             *
             * - Prepare sample R object vector data.
             * - Use RDataFactory to encode the sample R object vector data.
             * - Push encoded R object into the workspace.
             */
            List<Double> vectorValues =
                    Arrays.asList(10.0, 11.1, 12.2, 13.3, 14.4);
            RData encodedY =
                    RDataFactory.createNumericVector("y", vectorValues);
            rProject.pushObject(encodedY);

            /*
             * Retrieve the DeployR-encoding of the R object
             * from the R session's workspace.
             */
            encodedY = rProject.getObject("y");

            log.info("Retrieved object y " +
                    "from workspace, encodedY=" + encodedY);

            if (encodedY instanceof RNumericVector) {
                RNumericVector numVector = (RNumericVector) encodedY;
                List<Double> numVectorValues = numVector.getValue();
            }

            /*
             * Retrieve a list of R objects in the R session's workspace.
             *
             * Optionally:
             * ProjectWorkspaceOptions options =
             * new ProjectWorkspaceOptions();
             *
             * Populate options as needed, then:
             *
             * objs = rProject.listObjects(options);
             *
             */
            List<RData> objs = rProject.listObjects();


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
