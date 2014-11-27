/*
 * RClientTest.java
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revo.deployr.client;

import com.revo.deployr.DeployrUtil;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.factory.RClientFactory;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author nriesland
 */
public class RClientTest {

    RClient rClient = null;
    String url;

    public RClientTest() {
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
            String url = System.getProperty("url.property");
            if (url == null) {
                url = "localhost:" + DeployrUtil.DEFAULT_PORT;
            }
            rClient = RClientFactory.createClient("http://" + url + "/deployr");
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
     * Test of login method, of class RClient.
     */
    @Test
    public void testRClientLogin_RAuthentication() {

        // Test variables.
        String userName = "testuser";
        RUser rUser = null;
        RBasicAuthentication pAuthentication = null;
        RProject rProject = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        pAuthentication = new RBasicAuthentication(userName, "changeme");
        try {
            rUser = rClient.login(pAuthentication);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rClient.login failed: ";
        }

        // Test user is logged in
        if (exception == null) {
            try {
                rProject = rUser.createProject();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.createProject failed: ";
            }
        }

        // Test cleanup.
        if (rProject != null) {
            try {
                rProject.close();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.close failed: ";
            }
        }

        if (rUser != null) {
            try {
                rClient.logout(rUser);
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rClient.logout failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertEquals(userName, rUser.about().username);
            assertNotNull(rProject);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of login method, of class RClient.
     */
    @Test
    public void testRClientLogin_RAuthentication_disableautosave() throws Exception {

        // Test variables.
        RBasicAuthentication pAuthentication = new RBasicAuthentication("testuser", "changeme");
        boolean disableAutosave = true;
        String code = "x<-5";
        RUser rUser = null;
        boolean autoSave = false;
        RProject rProject = null;
        RProject getProject = null;
        RProjectExecution projectExecution = null;
        List<RProjectExecution> listProjectExecution = null;
        List<RProjectExecution> listGetProjectExecution = null;
        String pid = "";

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        try {
            rUser = rClient.login(pAuthentication, disableAutosave);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rClient.login failed: ";
        }

        if (exception == null) {
            rProject = DeployrUtil.createPersistentProject(rUser, "test autosave", "test autosave desc");
            pid = rProject.about().id;
            assertNotNull(rProject);
        }

        if (exception == null) {
            try {
                projectExecution = rProject.executeCode(code);
                listProjectExecution = rProject.getHistory();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.executeCode failed: ";
            }
        }

        if (exception == null) {
            try {
                rProject.close();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.close failed: ";
            }
        }

        if (exception == null) {
            try {
                getProject = rUser.getProject(pid);
                listGetProjectExecution = getProject.getHistory();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.getProject failed: ";
            }
        }

        // Test cleanup.
        if (rProject != null) {
            try {
                rProject.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.delete failed: ";
            }
        }

        if (rUser != null) {
            try {
                rClient.logout(rUser);
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rClient.logout failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertEquals(1, listProjectExecution.size());
            assertEquals(0, listGetProjectExecution.size());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of logout method, of class RClient.
     */
    @Test
    public void testLogout() {

        // Test variables.
        RBasicAuthentication pAuthentication = new RBasicAuthentication("testuser", "changeme");
        String userName = "testuser";
        RUser rUser = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        try {
            // Test.
            rUser = rClient.login(pAuthentication);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rClient.login failed: ";
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
            assertEquals(userName, rUser.about().username);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }
}
