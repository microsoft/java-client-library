/*
 * RUserExternalRepositoryFileCallsTest.java
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
import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.RUser;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.factory.RClientFactory;
import com.revo.deployr.client.params.RepoUploadOptions;
import org.junit.*;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;

public class RUserExternalRepositoryFileCallsTest {

    RClient rClient = null;
    RUser rUser = null;

    public RUserExternalRepositoryFileCallsTest() {
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
            RBasicAuthentication rAuthentication = new RBasicAuthentication("testuser", "changeme");
            String expResultName = "testuser";
            rUser = rClient.login(rAuthentication);
            assertNotNull(rUser);

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
     * Test RUserRepositoryFileCalls.listExternalFiles().
     */
    @Test
    public void testUserRepositoryListExternalFiles() {

        // Test variables.
        List<RRepositoryFile> listExternalFiles = null;
        int filesFoundOwnedByCaller = 0;
        int filesFoundNotOwnedByCaller = 0;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            listExternalFiles = rUser.listExternalFiles();
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.listExternalFiles failed: ";
        }

        try {
            // Determine if files authored by other users 
            // were found on the response. The listExternalFiles()
            // call should only return files authored by the caller.
            for (RRepositoryFile file : listExternalFiles) {
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
            assertEquals(filesFoundOwnedByCaller, listExternalFiles.size());
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
     * Test RUserRepositoryFileCalls.listExternalFiles(true, true).
     */
    @Test
    public void testUserRepositoryListExternalFilesSharedPublic() {

        // Test variables.
        RUser rAdminUser = null;
        List<RRepositoryFile> listExternalFiles = null;
        List<RRepositoryFile> listExternalFilesSharedPublic = null;
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
                // Retrieve list of external files owned by admin.
                listExternalFiles = rAdminUser.listExternalFiles();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rAdminUser.listExternalFiles failed: ";
            }
        }

        if(exception == null) {
            try {
                // Retrieve list of external files owned by admin
                // plus list of external files owned by other users
                // that have been shared or public.
                listExternalFilesSharedPublic =
                    rAdminUser.listExternalFiles(true, true);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rAdminUser.listExternalFiles(true, true) failed: ";
            }
        }

        if(exception == null) {

            try {

                for (RRepositoryFile file : listExternalFilesSharedPublic) {
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
            assertEquals(filesFoundOwnedByCaller, listExternalFiles.size());
            assertEquals(filesFoundNotOwnedByCaller,
                listExternalFilesSharedPublic.size() - listExternalFiles.size());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

}
