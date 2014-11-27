/*
 * AuthProjectExecuteCode.java
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

import com.revo.deployr.client.*;
import com.revo.deployr.client.auth.RAuthentication;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.data.RData;
import com.revo.deployr.client.factory.RClientFactory;
import org.apache.log4j.Logger;

import java.util.List;

public class AuthProjectExecuteCode {

    private static Logger log = Logger.getLogger(AuthProjectExecuteCode.class);

    public static void main(String args[]) throws Exception {

        RClient rClient = null;

        try {

            /*
             * Determine DeployR server endpoint.
             */
            String endpoint = System.getProperty("endpoint");
            log.info("AuthProjectExecuteCode: using endpoint=" + endpoint);

            /*
             * Establish RClient connection to DeployR server.
             *
             * An RClient connection is the mandatory starting
             * point for any application using the client library.
             */
            rClient = RClientFactory.createClient(endpoint);

            log.info("AuthProjectExecuteCode: established anonymous " +
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
            log.info("AuthProjectExecuteCode: established authenticated " +
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

            log.info("AuthProjectExecuteCode: created temporary " +
                    "R session, rProject=" + rProject);

            /*
             * Execute an analytics Web service based on an arbitrary
             * block of R code.
             *
             * Optionally:
             * ProjectExecutionOptions options =
             * new ProjectExecutionOptions();
             *
             * Populate options as needed, then:
             *
             * exec =
             * rProject.executeCode(rCode, options);
             */
            String rCode = "demo(graphics)";
            RProjectExecution exec = rProject.executeCode(rCode);

            log.info("AuthProjectExecuteCode: R code " +
                    "execution completed, rProjectExecution=" + exec);

            /*
             * Retrieve script execution results.
             */
            String console = exec.about().console;
            List<RProjectResult> plots = exec.about().results;
            List<RProjectFile> files = exec.about().artifacts;
            List<RData> objects = exec.about().workspaceObjects;


        } catch (Exception ex) {
            log.warn("AuthProjectExecuteCode: ex=" + ex);
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
