/*
 * RUserExternalRepositoryDirectoryCallsTest.java
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
import com.revo.deployr.client.RClientException;
import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.RRepositoryDirectory;
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

public class RUserExternalRepositoryDirectoryCallsTest {

    RClient rClient = null;
    RUser rUser = null;

    public RUserExternalRepositoryDirectoryCallsTest() {
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
     * Test RUserRepositoryFileCalls.listExternalDirectories().
     */
    @Test
    public void testUserRepositorylistExternalDirectories() {

        // Test variables.
        List<RRepositoryDirectory> listExternalDirectories = null;
        int totalUserFiles = 0;
        int totalSystemFiles = 0;
        int userFilesNotOwnedByCaller = 0;
        int systemFilesOwnedByCaller = 0;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            listExternalDirectories = rUser.listExternalDirectories();
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.listExternalDirectories failed: ";
        }

        try {
            // Determine if all files found in user directories are 
            // owned by the caller and if all files found in system
            // directories are owned by users other than the caller.
            for(RRepositoryDirectory dir : listExternalDirectories) {

                if(dir.about().systemDirectory) { // system directory
                    totalSystemFiles += dir.about().files.size();
                    for (RRepositoryFile file : dir.about().files) {
                        if(file.about().authors.contains("testuser")) {
                            systemFilesOwnedByCaller += 1;
                        }
                    }
                } else { // user directory
                    totalUserFiles += dir.about().files.size();
                    for (RRepositoryFile file : dir.about().files) {
                        if(!file.about().authors.contains("testuser")) {
                            userFilesNotOwnedByCaller += 1;
                        }
                    }
                }

            }
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "file.about().authors.contains failed: ";
        }

        // Test cleanup.
        if (exception == null) {
            // Test assertions.
            assertEquals(totalUserFiles, 0);
            assertEquals(totalSystemFiles, 0);
            assertEquals(userFilesNotOwnedByCaller, 0);
            assertEquals(systemFilesOwnedByCaller, 0);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test RUserRepositoryFileCalls.listExternalDirectories(true, false, false).
     */
    @Test
    public void testUserRepositorylistExternalDirectoriesUserFiles() {

        // Test variables.
        List<RRepositoryDirectory> listExternalDirectories = null;
        int totalUserFiles = 0;
        int totalSystemFiles = 0;
        int userFilesNotOwnedByCaller = 0;
        int systemFilesOwnedByCaller = 0;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            listExternalDirectories =
                rUser.listExternalDirectories(true, false, false);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.listExternalDirectories failed: ";
        }

        try {
            // Determine if all files found in user directories are 
            // owned by the caller and if all files found in system
            // directories are owned by users other than the caller.
            for(RRepositoryDirectory dir : listExternalDirectories) {

                if(dir.about().systemDirectory) { // system directory
                    totalSystemFiles += dir.about().files.size();
                    for (RRepositoryFile file : dir.about().files) {
                        if(file.about().authors.contains("testuser")) {
                            systemFilesOwnedByCaller += 1;
                        }
                    }
                } else { // user directory
                    totalUserFiles += dir.about().files.size();
                    for (RRepositoryFile file : dir.about().files) {
                        if(!file.about().authors.contains("testuser")) {
                            userFilesNotOwnedByCaller += 1;
                        }
                    }
                }
                
            }
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "file.about().authors.contains failed: ";
        }

        // Test cleanup.
        if (exception == null) {
            // Test assertions.
            assertEquals(totalSystemFiles, 0);
            assertEquals(userFilesNotOwnedByCaller, 0);
            assertEquals(systemFilesOwnedByCaller, 0);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test RUserRepositoryFileCalls.listExternalDirectories(false, true, false).
     */
    @Test
    public void testUserRepositorylistExternalDirectoriesSharedFiles() {

        // Test variables.
        List<RRepositoryDirectory> listExternalDirectories = null;
        int totalUserFiles = 0;
        int totalSystemFiles = 0;
        int userFilesNotOwnedByCaller = 0;
        int systemFilesOwnedByCaller = 0;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            listExternalDirectories =
                rUser.listExternalDirectories(false, true, false);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.listExternalDirectories failed: ";
        }

        try {
            // Determine if all files found in user directories are 
            // owned by the caller and if all files found in system
            // directories are owned by users other than the caller.
            for(RRepositoryDirectory dir : listExternalDirectories) {

                if(dir.about().systemDirectory) { // system directory
                    totalSystemFiles += dir.about().files.size();
                    for (RRepositoryFile file : dir.about().files) {
                        if(file.about().authors.contains("testuser")) {
                            systemFilesOwnedByCaller += 1;
                        }
                    }
                } else { // user directory
                    totalUserFiles += dir.about().files.size();
                    for (RRepositoryFile file : dir.about().files) {
                        if(!file.about().authors.contains("testuser")) {
                            userFilesNotOwnedByCaller += 1;
                        }
                    }
                }
                
            }
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "file.about().authors.contains failed: ";
        }

        // Test cleanup.
        if (exception == null) {
            // Test assertions.
            assertEquals(totalUserFiles, 0);
            assertEquals(userFilesNotOwnedByCaller, 0);
            assertEquals(systemFilesOwnedByCaller, 0);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test RUserRepositoryFileCalls.listExternalDirectories(false, true, false).
     */
    @Test
    public void testUserRepositorylistExternalDirectoriesPublicFiles() {

        // Test variables.
        List<RRepositoryDirectory> listExternalDirectories = null;
        int totalUserFiles = 0;
        int totalSystemFiles = 0;
        int userFilesNotOwnedByCaller = 0;
        int systemFilesOwnedByCaller = 0;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            listExternalDirectories =
                rUser.listExternalDirectories(false, false, true);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.listExternalDirectories failed: ";
        }

        try {
            // Determine if all files found in user directories are 
            // owned by the caller and if all files found in system
            // directories are owned by users other than the caller.
            for(RRepositoryDirectory dir : listExternalDirectories) {

                if(dir.about().systemDirectory) { // system directory
                    totalSystemFiles += dir.about().files.size();
                    for (RRepositoryFile file : dir.about().files) {
                        if(file.about().authors.contains("testuser")) {
                            systemFilesOwnedByCaller += 1;
                        }
                    }
                } else { // user directory
                    totalUserFiles += dir.about().files.size();
                    for (RRepositoryFile file : dir.about().files) {
                        if(!file.about().authors.contains("testuser")) {
                            userFilesNotOwnedByCaller += 1;
                        }
                    }
                }
                
            }
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "file.about().authors.contains failed: ";
        }

        // Test cleanup.
        if (exception == null) {
            // Test assertions.
            assertEquals(totalUserFiles, 0);
            assertEquals(userFilesNotOwnedByCaller, 0);
            assertEquals(systemFilesOwnedByCaller, 0);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test RUserRepositoryFileCalls.listFiles(categoryFilter, directoryFilter).
     */
    @Test
    public void testUserRepositoryListExternalDirectoriesGoodFilters() {

        // Test variables.
        List<RRepositoryDirectory> listDirectories = null;
        List<RRepositoryFile> listFilesExampleDirectory = null;
        List<RRepositoryFile> listFilesExampleScripts = null;
        List<RRepositoryFile> listFilesExampleBinary = null;
        String exampleFraudScoreDirectory = "external:root:example-fraud-score";
        int fraudExampleTotalFileCount = 2;
        int fraudExampleScriptFileCount = 1;
        boolean fraudExampleScriptsAreScripts = false;
        int fraudExampleBinaryFileCount = 1;
        boolean fraudExampleBinaryAreBinary = false;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.

        try {
            listDirectories =
                rUser.listExternalDirectories((RRepositoryFile.Category) null,
                                        exampleFraudScoreDirectory);
            for(RRepositoryDirectory dir : listDirectories) {
                if(dir.about().name.equals(exampleFraudScoreDirectory)) {
                    listFilesExampleDirectory = dir.about().files;
                }
            }
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.listExternalDirectories(null, directory) failed: ";
        }

        if(exception == null) {

            try {

                listDirectories =
                    rUser.listExternalDirectories(RRepositoryFile.Category.RSCRIPT,
                                            exampleFraudScoreDirectory);
                for(RRepositoryDirectory dir : listDirectories) {
                    if(dir.about().name.equals(exampleFraudScoreDirectory)) {
                        listFilesExampleScripts = dir.about().files;
                        break;
                    }
                }

                for(RRepositoryFile scriptFile : listFilesExampleScripts) {
                    if(scriptFile.about().category !=
                                        RRepositoryFile.Category.RSCRIPT) {
                        fraudExampleScriptsAreScripts = false;
                        break;
                    } else {
                        fraudExampleScriptsAreScripts = true;
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listExternalDirectories(RSCRIPT, directory) failed: ";
            }
        }

        if(exception == null) {

            try {

                listDirectories =
                    rUser.listExternalDirectories(RRepositoryFile.Category.RBINARY,
                                            exampleFraudScoreDirectory);
                for(RRepositoryDirectory dir : listDirectories) {
                    if(dir.about().name.equals(exampleFraudScoreDirectory)) {
                        listFilesExampleBinary = dir.about().files;
                        break;
                    }
                }

                for(RRepositoryFile binFile : listFilesExampleBinary) {
                    if(binFile.about().category !=
                                        RRepositoryFile.Category.RBINARY) {
                        fraudExampleBinaryAreBinary = false;
                        break;
                    } else {
                        fraudExampleBinaryAreBinary = true;
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listExternalDirectories(RBINARY, directory) failed: ";
            }
        }

        if (exception == null) {
            // Test assertions.
            assertEquals(fraudExampleTotalFileCount, listFilesExampleDirectory.size());
            assertEquals(fraudExampleScriptFileCount, listFilesExampleScripts.size());
            assertTrue(fraudExampleScriptsAreScripts);
            assertEquals(fraudExampleBinaryFileCount, listFilesExampleBinary.size());
            assertTrue(fraudExampleBinaryAreBinary);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test RUserRepositoryDirectoryCalls.listDirectories(categoryFilter, directoryFilter).
     */
    @Test
    public void testUserRepositoryListExternalDirectoriesBadFilters() {

        // Test variables.
        List<RRepositoryDirectory> listDirectories = null;
        RClientException clientEx = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            listDirectories =
                rUser.listExternalDirectories((RRepositoryFile.Category) null,
                                        "external:public:dir-not-found");
        } catch (RClientException cex) {
            clientEx = cex;
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.listExternalDirectories(null, dir-not-found) failed: ";
        }

        if (exception == null) {
            // Test assertions.
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
