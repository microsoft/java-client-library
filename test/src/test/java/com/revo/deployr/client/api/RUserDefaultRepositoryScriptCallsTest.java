/*
 * RUserDefaultRepositoryScriptCallsTest.java
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
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.factory.RClientFactory;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class RUserDefaultRepositoryScriptCallsTest {
    RClient rClient = null;
    RUser rUser = null;

    public RUserDefaultRepositoryScriptCallsTest() {
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
     * Test RUserRepositoryScriptCalls.listScripts().
     */
    @Test
    public void testUserRepositoryListScripts() {

        // Test variables.
        List<RRepositoryFile> listScripts = null;
        int filesFoundOwnedByCaller = 0;
        int filesFoundNotOwnedByCaller = 0;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            listScripts = rUser.listScripts();
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.listScripts failed: ";
        }

        try {
            // Determine if script authored by other users 
            // were found on the response. The listScripts()
            // call should only return scripts authored by the caller.
            for (RRepositoryFile file : listScripts) {
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
            assertEquals(filesFoundOwnedByCaller, listScripts.size());
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
     * Test RUserRepositoryScriptCalls.listScripts(archived, false, false).
     */
    @Test
    public void testUserRepositoryListScriptsArchived() {

        // Test variables.
        RUser rAdminUser = null;
        List<RRepositoryFile> listScripts = null;
        List<RRepositoryFile> listScriptsAll = null;
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
                new RBasicAuthentication("admin", "changeme");
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
                // Retrieve list of scripts owned by admin.
                listScripts = rAdminUser.listScripts();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rAdminUser.listScripts failed: ";
            }
        }

        if(exception == null) {
            try {
                // Retrieve list of scripts owned by admin including
                // archived scripts. Exclude list of scripts owned by
                // other users that have been shared or published.
                listScriptsAll =
                    rAdminUser.listScripts(true, false, false);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rAdminUser.listScripts(true, false, false) failed: ";
            }
        }

        if(exception == null) {

            try {
                for (RRepositoryFile file : listScriptsAll) {
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
            assertEquals(filesFoundOwnedByCaller, listScriptsAll.size());
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
     * Test RUserRepositoryScriptCalls.listScripts(false, shared, published).
     */
    @Test
    public void testUserRepositoryListScriptsSharedPublished() {

        // Test variables.
        RUser rAdminUser = null;
        List<RRepositoryFile> listScripts = null;
        List<RRepositoryFile> listScriptsAll = null;
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
                new RBasicAuthentication("admin", "changeme");
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
                // Retrieve list of scripts owned by admin.
                listScripts = rAdminUser.listScripts();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rAdminUser.listScripts failed: ";
            }
        }

        if(exception == null) {
            try {
                // Retrieve list of scripts owned by admin excluding
                // archived scripts plus list of scripts owned by
                // other users that have been shared or published.
                listScriptsAll =
                    rAdminUser.listScripts(false, true, true);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rAdminUser.listScripts(false, true, true) failed: ";
            }
        }

        if(exception == null) {

            try {
                for (RRepositoryFile file : listScriptsAll) {
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
            assertEquals(filesFoundOwnedByCaller, listScripts.size());
            assertEquals(filesFoundNotOwnedByCaller,
                listScriptsAll.size() - listScripts.size());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test RClient.executeScript using default repository script.
     */
    @Test
    public void testClientRepositoryExecuteScript() {

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
            script = rUser.fetchFile("Histogram of Auto Sales.R",
                                     "root",
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
                exceptionMsg = "rUser.fetchFile failed: ";
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
