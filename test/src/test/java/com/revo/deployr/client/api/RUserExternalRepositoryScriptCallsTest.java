/*
 * RUserExternalRepositoryScriptCallsTest.java
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
package com.revo.deployr.client.api;

import com.revo.deployr.DeployrUtil;
import com.revo.deployr.client.RClient;
import com.revo.deployr.client.RUser;
import com.revo.deployr.client.RScriptExecution;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.factory.RClientFactory;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class RUserExternalRepositoryScriptCallsTest {
    RClient rClient = null;
    RUser rUser = null;

    public RUserExternalRepositoryScriptCallsTest() {
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
            RBasicAuthentication rAuthentication = new RBasicAuthentication("testuser", "Secret11");
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
     * Test RUserRepositoryScriptCalls.listExternalScripts().
     */
    @Test
    public void testUserRepositoryListExternalScripts() {

        // Test variables.
        List<RRepositoryFile> listExternalScripts = null;
        int filesFoundOwnedByCaller = 0;
        int filesFoundNotOwnedByCaller = 0;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            listExternalScripts = rUser.listExternalScripts();
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.listExternalScripts failed: ";
        }

        try {
            // Determine if script authored by other users 
            // were found on the response. The listExternalScripts()
            // call should only return scripts authored by the caller.
            for (RRepositoryFile file : listExternalScripts) {
                if (file.about().authors.contains("testuser")) {
                    filesFoundOwnedByCaller += 1;
                } else {
                    filesFoundNotOwnedByCaller += 1;
                }
            }
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "file.about().authors.contains failed: ";
        }

        // Test cleanup.
        if (exception == null) {
            // Test assertions.
            assertEquals(filesFoundOwnedByCaller, listExternalScripts.size());
            assertEquals(filesFoundNotOwnedByCaller, 0);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test RUserRepositoryScriptCalls.listExternalScripts(shared, published).
     */
    @Test
    public void testUserRepositoryListExternalScriptsSharedPublished() {

        // Test variables.
        RUser rAdminUser = null;
        List<RRepositoryFile> listExternalScripts = null;
        List<RRepositoryFile> listExternalScriptsSharedPublished = null;
        int filesFoundOwnedByCaller = 0;
        int filesFoundNotOwnedByCaller= 0;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            rClient.logout(rUser);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rClient.logout failed: ";
        }

        if (exception == null) {
            RBasicAuthentication rAuthentication =
                new RBasicAuthentication("admin", "Secret11");
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    exception = ex;
                    exceptionMsg = "Thread.sleep failed: ";
                }
                if (exception == null) {
                    try {
                        rAdminUser = rClient.login(rAuthentication);
                    } catch (Exception ex) {
                        if (i < 5) {
                            continue;
                        }
                        exception = ex;
                        exceptionMsg = "rClient.login failed: ";
                    }
                }
            }
        }

        if(exception == null) {
            try {
                // Retrieve list of external scripts owned by admin.
                listExternalScripts = rAdminUser.listExternalScripts();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rAdminUser.listExternalScripts failed: ";
            }
        }

        if(exception == null) {
            try {
                // Retrieve list of external scripts owned by admin
                // plus list of external scripts owned by other users
                // that have been shared or published.
                listExternalScriptsSharedPublished =
                    rAdminUser.listExternalScripts(true, true);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rAdminUser.listExternalScripts(true, true) failed: ";
            }
        }

        if(exception == null) {

            try {
                for (RRepositoryFile file : listExternalScriptsSharedPublished) {
                    if (file.about().authors.contains("admin")) {
                        filesFoundOwnedByCaller += 1;
                    } else {
                        filesFoundNotOwnedByCaller += 1;
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "file.about().authors.contains failed: ";
            }
        }

        // Test cleanup.
        if (exception == null) {
            // Test assertions.
            assertEquals(filesFoundOwnedByCaller, listExternalScripts.size());
            assertEquals(filesFoundNotOwnedByCaller,
                listExternalScriptsSharedPublished.size() - listExternalScripts.size());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test RClient.executeScript using external repository script.
     */
    @Test
    public void testClientExternalRepositoryExecuteScript() {

        // Test variables.
        RRepositoryFile script = null;
        RScriptExecution exec = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            /*
             * The Histogram of Autos Sales.R script lives
             * by default in the following directory within
             * the external repository:
             * external/repository/public/testuser which
             * maps to the following directory value:
             * external:public:testuser
             */
            script = rUser.fetchFile("Histogram of Auto Sales.R",
                                     "external:public:testuser",
                                     "testuser",
                                     null);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.fetchFile failed: ";
        }

        if(exception == null) {

            try {
                exec = rClient.executeScript(script.about().filename,
                                             script.about().directory,
                                             script.about().author,
                                             (String) null);
            } catch(Exception ex) {
                exception = ex;
                exceptionMsg = "rClient.executeScript failed: ";
            }

        }

        // Test cleanup.
        if (exception == null) {
            // Test assertions.
            assertNotNull(script);
            assertNotNull(exec);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

}
