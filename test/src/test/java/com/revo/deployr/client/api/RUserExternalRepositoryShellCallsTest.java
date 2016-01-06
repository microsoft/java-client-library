/*
 * RUserExternalRepositoryShellCallsTest.java
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
package com.revo.deployr.client.api;

import com.revo.deployr.DeployrUtil;
import com.revo.deployr.client.RClient;
import com.revo.deployr.client.RUser;
import com.revo.deployr.client.RScriptExecution;
import com.revo.deployr.client.RClientException;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.factory.RClientFactory;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class RUserExternalRepositoryShellCallsTest {
    RClient rClient = null;
    RUser rUser = null;

    public RUserExternalRepositoryShellCallsTest() {
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
            RBasicAuthentication rAuthentication = new RBasicAuthentication("testuser", "changeme");
            String expResultName = "testuser";
            rUser = rClient.login(rAuthentication);
            assertEquals(expResultName, rUser.about().username);

        } catch (Exception ex) {
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
     * Test RClient.executeShell using external repository
     * shell script.
     */
    @Test
    public void testClientExternalRepositoryExecuteShell() {

        // Test variables.
        RRepositoryFile shell = null;
        List<String> consoleOutput = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            /*
             * The echo.[sh,bat] shell script lives by default in
             * the following directory on disk within the external
             * repository:
             * external/repository/public/admin which
             * maps to the following directory value:
             * external:public:admin
             */
            shell = rUser.fetchFile("echo.sh",
                                "external:public:admin",
                                "admin",
                                null);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.fetchFile failed: ";
        }

        if(exception == null) {

            try {
                /*
                 * Test DeployR Linux/OSX Server.
                 */
                consoleOutput =
                    rClient.executeShell(shell.about().filename,
                                         shell.about().directory,
                                         shell.about().author,
                                         null,
                                         "Hello World!");
            } catch(Exception ex) {
                try {
                    /*
                     * Test DeployR Windows Server.
                     */
                    consoleOutput =
                    rClient.executeShell("echo.bat",
                                         shell.about().directory,
                                         shell.about().author,
                                         null,
                                         "Hello World!");

                } catch(Exception batex) {
                    exception = ex;
                    exceptionMsg = "rClient.executeShell failed: ";
                }
            }

        }

        // Test cleanup.
        if (exception == null) {
            // Test assertions.
            assertNotNull(shell);
            assertNotNull(consoleOutput);
            assertEquals(consoleOutput.size(), 2);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test RClient.executeShell using external repository
     * shell script.
     */
    @Test
    public void testClientExternalRepositoryExecuteShellBadFilename() {

        // Test variables.
        RRepositoryFile shell = null;
        List<String> consoleOutput = null;
        RClientException clientEx = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            /*
             * The echo.sh shell script lives by default in the
             * following directory on disk within the external
             * repository:
             * external/repository/public/admin which
             * maps to the following directory value:
             * external:public:admin
             */
            shell = rUser.fetchFile("echo.sh",
                                "external:public:admin",
                                "admin",
                                null);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.fetchFile failed: ";
        }

        if(exception == null) {

            try {
                consoleOutput =
                    rClient.executeShell("does-not-exist.sh",
                                         shell.about().directory,
                                         shell.about().author,
                                         null,
                                         "Hello World!");
            } catch(RClientException ex) {
                clientEx = ex;
            } catch(Exception ex) {
                exception = ex;
                exceptionMsg = "rClient.executeShell unexpected ex, failed: ";
            }

        }

        // Test cleanup.
        if (exception == null) {
            // Test assertions.
            assertNotNull(shell);
            assertNotNull(clientEx);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test RClient.executeShell using external repository
     * shell script.
     */
    @Test
    public void testClientExternalRepositoryExecuteShellBadDirectory() {

        // Test variables.
        RRepositoryFile shell = null;
        List<String> consoleOutput = null;
        RClientException clientEx = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            /*
             * The echo.sh shell script lives by default in the
             * following directory on disk within the external
             * repository:
             * external/repository/public/admin which
             * maps to the following directory value:
             * external:public:admin
             */
            shell = rUser.fetchFile("echo.sh",
                                "external:public:admin",
                                "admin",
                                null);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.fetchFile failed: ";
        }

        if(exception == null) {

            try {
                consoleOutput =
                    rClient.executeShell(shell.about().filename,
                                         "external:public:dir-not-found",
                                         shell.about().author,
                                         null,
                                         "Hello World!");
            } catch(RClientException ex) {
                clientEx = ex;
            } catch(Exception ex) {
                exception = ex;
                exceptionMsg = "rClient.executeShell unexpected ex, failed: ";
            }

        }

        // Test cleanup.
        if (exception == null) {
            // Test assertions.
            assertNotNull(shell);
            assertNotNull(clientEx);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test RClient.executeShell using external repository
     * shell script.
     */
    @Test
    public void testClientExternalRepositoryExecuteShellBadAuthor() {

        // Test variables.
        RRepositoryFile shell = null;
        List<String> consoleOutput = null;
        RClientException clientEx = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            /*
             * The echo.sh script lives by default in the
             * following directory on disk within the external
             * repository:
             * external/repository/public/admin which
             * maps to the following directory value:
             * external:public:admin
             */
            shell = rUser.fetchFile("echo.sh",
                                "external:public:admin",
                                "admin",
                                null);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.fetchFile failed: ";
        }

        if(exception == null) {

            try {
                consoleOutput =
                    rClient.executeShell(shell.about().filename,
                                         shell.about().directory,
                                         "author-not-found",
                                         null,
                                         "Hello World!");
            } catch(RClientException ex) {
                clientEx = ex;
            } catch(Exception ex) {
                exception = ex;
                exceptionMsg = "rClient.executeShell unexpected ex, failed: ";
            }

        }

        // Test cleanup.
        if (exception == null) {
            // Test assertions.
            assertNotNull(shell);
            assertNotNull(clientEx);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

}
