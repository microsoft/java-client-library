/*
 * RProjectTempClose.java
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

import com.revo.deployr.DeployrUtil;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.factory.RClientFactory;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class RProjectTempClose {

    RClient rClient = null;
    String url;

    public RProjectTempClose() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        try {
            String url = System.getProperty("connection.protocol") +
                            System.getProperty("connection.endpoint");
            if (url == null) {
                fail("setUp: connection.[protocol|endpoint] null.");
            }
            boolean allowSelfSigned = 
                Boolean.valueOf(System.getProperty("allow.SelfSignedSSLCert"));
            rClient =RClientFactory.createClient(url, allowSelfSigned);
        } catch (Exception ex) {
            if (rClient != null) {
                rClient.release();
            }
            fail("setUp: " + ex);
        }
    }

    @After
    public void tearDown() {
        if (rClient != null) {
            rClient.release();
        }
    }

    /**
     * Test automatic shutdown of temporary project
     * following logout.
     */
    @Test
    public void testTemporaryProjectAutoShutdownAfterLogout() {

        // Test variables.
        RBasicAuthentication pAuthentication = null;
        List<RProject> projects = null;
        String expResult = "testuser";
        RUser rUser = null;
        RProject rProject = null;
        String pid = "";
        boolean found = false;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        pAuthentication = new RBasicAuthentication("testuser", System.getProperty("password.testuser"));
        try {
            rUser = rClient.login(pAuthentication);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rClient.login failed: ";
        }

        if (exception == null) {
            try {
                rProject = rUser.createProject();
                pid = rProject.about().id;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.createProject failed: ";
            }
        }

        if (exception == null) {
            try {
                rClient.logout(rUser);
                Thread.sleep(3000);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rClient.logout failed: ";
            }
        }

        if (exception == null) {
            try {
                rUser = rClient.login(pAuthentication);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rClient.login failed: ";
            }
        }

        if (exception == null) {
            try {
                projects = rUser.listProjects();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listProjects failed: ";
            }
        }


        if (exception == null) {
            for (RProject proj : projects) {
                if (proj.about().id.equals(pid)) {
                    found = true;
                }
            }
        }

        if (exception == null) {
            try {
                rClient.logout(rUser);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rClient.logout failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertFalse(found);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }
}
