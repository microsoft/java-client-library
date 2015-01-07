/*
 * RUserDefaultRepositoryDirectoryCallsTest.java
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
import com.revo.deployr.client.RRepositoryDirectory;
import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.RUser;
import com.revo.deployr.client.about.RRepositoryDirectoryDetails;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.factory.RClientFactory;
import com.revo.deployr.client.params.RepoAccessControlOptions;
import com.revo.deployr.client.params.RepoUploadOptions;
import org.junit.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class RUserDefaultRepositoryDirectoryCallsTest {

    RClient rClient = null;
    RUser rUser = null;

    public RUserDefaultRepositoryDirectoryCallsTest() {
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
     * Test of listDirectories method, of class RUserRepositoryDirectoryCalls.
     */
    @Test
    public void testUserRepositoryListDirectories() {

        // Test variables.
        String expRepoDirectoryName = null;
        String actualRepoDirectoryName = null;
        int expRepoDirectoryFileCount = 0;
        int actualRepoDirectoryFileCount = 0;
        RRepositoryDirectory repoDirectory = null;
        List<RRepositoryDirectory> listDirectories = null;
        boolean repoDirectoryFound = false;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        expRepoDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            repoDirectory = rUser.createDirectory(expRepoDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory failed: ";
        }

        if (exception == null) {
            try {
                actualRepoDirectoryName = repoDirectory.about().name;
                actualRepoDirectoryFileCount = repoDirectory.about().files.size();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoDirectory.about failed: ";
            }
        }

        if (exception == null) {
            try {
                listDirectories = rUser.listDirectories(true, true, true, true);
                // search for filename
                for (RRepositoryDirectory directory : listDirectories) {
                    if (directory.about().name.equalsIgnoreCase(expRepoDirectoryName)) {
                        repoDirectoryFound = true;
                        break;
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listDirectories failed: ";
            }
        }

        // Test cleanup.
        try {
            if (repoDirectory != null) {
                repoDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "repoDirectory.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertTrue(repoDirectoryFound);
            assertEquals(expRepoDirectoryName, actualRepoDirectoryName);
            assertEquals(expRepoDirectoryFileCount, actualRepoDirectoryFileCount);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of listDirectories method, of class RUserRepositoryDirectoryCalls.
     */
    @Test
    public void testUserRepositoryListDirectoriesAndFiles() {

        // Test variables.
        RRepositoryDirectory repoDirectory = null;
        String expRepoDirectoryName = null;
        String actualRepoDirectoryName = null;
        int expRepoDirectoryFileCount = 0;
        int actualRepoDirectoryFileCount = 0;

        RRepositoryDirectory repoDirectoryArchive = null;
        String expRepoDirectoryArchiveName = null;
        String actualRepoDirectoryArchiveName = null;
        int expRepoDirectoryArchiveFileCount = 0;
        int actualRepoDirectoryArchiveFileCount = 0;

        List<RRepositoryDirectory> listDirectories = null;

        boolean repoDirectoryArchiveFound = false;
        boolean repoDirectoryRestrictedFound = false;
        boolean repoDirectorySharedFound = false;
        boolean repoDirectoryPublicFound = false;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        expRepoDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            repoDirectory = rUser.createDirectory(expRepoDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory failed: ";
        }

        if (exception == null) {
            try {
                actualRepoDirectoryName = repoDirectory.about().name;
                actualRepoDirectoryFileCount = repoDirectory.about().files.size();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoDirectory.about failed: ";
            }
        }

        if (exception == null) {
            try {
                repoDirectoryArchive = repoDirectory.archive(expRepoDirectoryName, null);
                expRepoDirectoryArchiveName = expRepoDirectoryName + ".archive";
                repoDirectory = null;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoDirectory.archive failed: ";
            }
        }

        if (exception == null) {
            try {
                actualRepoDirectoryArchiveName = repoDirectoryArchive.about().name;
                actualRepoDirectoryArchiveFileCount = repoDirectoryArchive.about().files.size();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoDirectoryArchive.about failed: ";
            }
        }

        if (exception == null) {
            try {
                listDirectories = rUser.listDirectories(true, true, true, true);
                // search for filename
                for (RRepositoryDirectory directory : listDirectories) {
                    if (directory.about().name.equalsIgnoreCase(expRepoDirectoryArchiveName)) {
                        repoDirectoryArchiveFound = true;
                    } else if (directory.about().name.equalsIgnoreCase(RRepositoryDirectoryDetails.SYSTEM_RESTRICTED)) {
                        repoDirectoryRestrictedFound = true;
                    } else if (directory.about().name.equalsIgnoreCase(RRepositoryDirectoryDetails.SYSTEM_SHARED)) {
                        repoDirectorySharedFound = true;
                    } else if (directory.about().name.equalsIgnoreCase(RRepositoryDirectoryDetails.SYSTEM_PUBLIC)) {
                        repoDirectoryPublicFound = true;
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listDirectories failed: ";
            }
        }

        // Test cleanup.
        try {
            if (repoDirectory != null) {
                repoDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "repoDirectory.delete failed: ";
        }

        try {
            if (repoDirectoryArchive != null) {
                repoDirectoryArchive.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "repoDirectoryArchive.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertTrue(repoDirectoryArchiveFound);
            assertTrue(repoDirectoryRestrictedFound);
            assertTrue(repoDirectorySharedFound);
            assertTrue(repoDirectoryPublicFound);
            assertEquals(expRepoDirectoryArchiveName, actualRepoDirectoryArchiveName);
            assertEquals(expRepoDirectoryArchiveFileCount, actualRepoDirectoryArchiveFileCount);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of copyDirectory method, of class RUserRepositoryDirectoryCalls.
     */
    @Test
    public void testUserRepositoryCopyDirectoryAll() {

        // Test variables.
        String sourceDirectoryName = null;
        RRepositoryDirectory sourceDirectory = null;
        RRepositoryFile sourceFile = null;
        int expSourceDirectoryFileCount = 1;
        int sourceDirectoryFileCount = 0;
        String destinationDirectoryName = null;
        RRepositoryDirectory destinationDirectory = null;
        int expDestinationDirectoryFileCount = 1;
        int destinationDirectoryFileCount = 0;
        List<RRepositoryDirectory> listDirectories = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        sourceDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            sourceDirectory = rUser.createDirectory(sourceDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory sourceDirectory failed: ";
        }

        destinationDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            destinationDirectory = rUser.createDirectory(destinationDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory destination failed: ";
        }

        if (exception == null) {
            try {
                RepoUploadOptions options = new RepoUploadOptions();
                options.filename = "testUserRepositoryCopyDirectory.R";
                options.directory = sourceDirectory.about().name;
                sourceFile = rUser.writeFile("x <- rnorm(100)", options);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.writeFile failed: ";
            }
        }

        if (exception == null) {
            try {
                rUser.copyDirectory(sourceDirectory.about().name,
                        destinationDirectory.about().name, null);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.copyDirectory failed: ";
            }
        }

        if (exception == null) {
            try {
                listDirectories = rUser.listDirectories(true, true, true, true);
                // search for filename
                for (RRepositoryDirectory directory : listDirectories) {
                    if (directory.about().name.equalsIgnoreCase(sourceDirectoryName)) {
                        sourceDirectoryFileCount = directory.about().files.size();
                    } else if (directory.about().name.equalsIgnoreCase(destinationDirectoryName)) {
                        destinationDirectoryFileCount = directory.about().files.size();
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listDirectories failed: ";
            }
        }

        // Test cleanup.
        try {
            if (sourceFile != null) {
                sourceFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "sourceFile.delete failed: ";
        }
        try {
            if (sourceDirectory != null) {
                sourceDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "sourceDirectory.delete failed: ";
        }

        try {
            if (destinationDirectory != null) {
                destinationDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "destinationDirectory.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertEquals(expSourceDirectoryFileCount, sourceDirectoryFileCount);
            assertEquals(expDestinationDirectoryFileCount, destinationDirectoryFileCount);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }


    /**
     * Test of copyDirectory method, of class RUserRepositoryDirectoryCalls.
     */
    @Test
    public void testUserRepositoryCopyDirectorySome() {

        // Test variables.
        String sourceDirectoryName = null;
        RRepositoryDirectory sourceDirectory = null;
        RRepositoryFile sourceFile = null;
        int expSourceDirectoryFileCount = 1;
        int sourceDirectoryFileCount = 0;
        String destinationDirectoryName = null;
        RRepositoryDirectory destinationDirectory = null;
        int expDestinationDirectoryFileCount = 1;
        int destinationDirectoryFileCount = 0;
        List<RRepositoryDirectory> listDirectories = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        sourceDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            sourceDirectory = rUser.createDirectory(sourceDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory sourceDirectory failed: ";
        }

        destinationDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            destinationDirectory = rUser.createDirectory(destinationDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory destination failed: ";
        }

        if (exception == null) {
            try {
                RepoUploadOptions options = new RepoUploadOptions();
                options.filename = "testUserRepositoryCopyDirectory.R";
                options.directory = sourceDirectory.about().name;
                sourceFile = rUser.writeFile("x <- rnorm(100)", options);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.writeFile failed: ";
            }
        }

        if (exception == null) {
            try {
                rUser.copyDirectory(sourceDirectory.about().name,
                        destinationDirectory.about().name,
                        Arrays.asList(sourceFile));
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.copyDirectory failed: ";
            }
        }

        if (exception == null) {
            try {
                listDirectories = rUser.listDirectories(true, true, true, true);
                // search for filename
                for (RRepositoryDirectory directory : listDirectories) {
                    if (directory.about().name.equalsIgnoreCase(sourceDirectoryName)) {
                        sourceDirectoryFileCount = directory.about().files.size();
                    } else if (directory.about().name.equalsIgnoreCase(destinationDirectoryName)) {
                        destinationDirectoryFileCount = directory.about().files.size();
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listDirectories failed: ";
            }
        }

        // Test cleanup.
        try {
            if (sourceFile != null) {
                sourceFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "sourceFile.delete failed: ";
        }
        try {
            if (sourceDirectory != null) {
                sourceDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "sourceDirectory.delete failed: ";
        }

        try {
            if (destinationDirectory != null) {
                destinationDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "destinationDirectory.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertEquals(expSourceDirectoryFileCount, sourceDirectoryFileCount);
            assertEquals(expDestinationDirectoryFileCount, destinationDirectoryFileCount);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of moveDirectory method, of class RUserRepositoryDirectoryCalls.
     */
    @Test
    public void testUserRepositoryMoveDirectoryAll() {

        // Test variables.
        String sourceDirectoryName = null;
        RRepositoryDirectory sourceDirectory = null;
        RRepositoryFile sourceFile = null;
        int expSourceDirectoryFileCount = 0;
        int sourceDirectoryFileCount = 0;
        String destinationDirectoryName = null;
        RRepositoryDirectory destinationDirectory = null;
        RRepositoryFile destinationFile = null;
        int expDestinationDirectoryFileCount = 1;
        int destinationDirectoryFileCount = 0;
        List<RRepositoryDirectory> listDirectories = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        sourceDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            sourceDirectory = rUser.createDirectory(sourceDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory sourceDirectory failed: ";
        }

        destinationDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            destinationDirectory = rUser.createDirectory(destinationDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory destination failed: ";
        }

        if (exception == null) {
            try {
                RepoUploadOptions options = new RepoUploadOptions();
                options.filename = "testUserRepositoryMoveDirectory.R";
                options.directory = sourceDirectory.about().name;
                sourceFile = rUser.writeFile("x <- rnorm(100)", options);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.writeFile failed: ";
            }
        }

        if (exception == null) {
            try {
                rUser.moveDirectory(sourceDirectory.about().name,
                        destinationDirectory.about().name,
                        null);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.moveDirectory failed: ";
            }
        }

        if (exception == null) {
            try {
                listDirectories = rUser.listDirectories(true, true, true, true);
                // search for filename
                for (RRepositoryDirectory directory : listDirectories) {
                    if (directory.about().name.equalsIgnoreCase(sourceDirectoryName)) {
                        sourceDirectoryFileCount = directory.about().files.size();
                    } else if (directory.about().name.equalsIgnoreCase(destinationDirectoryName)) {
                        destinationDirectoryFileCount = directory.about().files.size();
                        destinationFile = (RRepositoryFile) directory.about().files.get(0);
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listDirectories failed: ";
            }
        }

        // Test cleanup.
        try {
            if (destinationFile != null) {
                destinationFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "sourceFile.delete failed: ";
        }
        try {
            if (sourceDirectory != null) {
                sourceDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "sourceDirectory.delete failed: ";
        }

        try {
            if (destinationDirectory != null) {
                destinationDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "destinationDirectory.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertEquals(expSourceDirectoryFileCount, sourceDirectoryFileCount);
            assertEquals(expDestinationDirectoryFileCount, destinationDirectoryFileCount);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of moveDirectory method, of class RUserRepositoryDirectoryCalls.
     */
    @Test
    public void testUserRepositoryMoveDirectorySome() {

        // Test variables.
        String sourceDirectoryName = null;
        RRepositoryDirectory sourceDirectory = null;
        RRepositoryFile sourceFile = null;
        int expSourceDirectoryFileCount = 0;
        int sourceDirectoryFileCount = 0;
        String destinationDirectoryName = null;
        RRepositoryDirectory destinationDirectory = null;
        RRepositoryFile destinationFile = null;
        int expDestinationDirectoryFileCount = 1;
        int destinationDirectoryFileCount = 0;
        List<RRepositoryDirectory> listDirectories = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        sourceDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            sourceDirectory = rUser.createDirectory(sourceDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory sourceDirectory failed: ";
        }

        destinationDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            destinationDirectory = rUser.createDirectory(destinationDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory destination failed: ";
        }

        if (exception == null) {
            try {
                RepoUploadOptions options = new RepoUploadOptions();
                options.filename = "testUserRepositoryMoveDirectory.R";
                options.directory = sourceDirectory.about().name;
                sourceFile = rUser.writeFile("x <- rnorm(100)", options);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.writeFile failed: ";
            }
        }

        if (exception == null) {
            try {
                rUser.moveDirectory(sourceDirectory.about().name,
                        destinationDirectory.about().name,
                        Arrays.asList(sourceFile));
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.moveDirectory failed: ";
            }
        }

        if (exception == null) {
            try {
                listDirectories = rUser.listDirectories(true, true, true, true);
                // search for filename
                for (RRepositoryDirectory directory : listDirectories) {
                    if (directory.about().name.equalsIgnoreCase(sourceDirectoryName)) {
                        sourceDirectoryFileCount = directory.about().files.size();
                    } else if (directory.about().name.equalsIgnoreCase(destinationDirectoryName)) {
                        destinationDirectoryFileCount = directory.about().files.size();
                        destinationFile = (RRepositoryFile) directory.about().files.get(0);
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listDirectories failed: ";
            }
        }

        // Test cleanup.
        try {
            if (destinationFile != null) {
                destinationFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "destinationFile.delete failed: ";
        }
        try {
            if (sourceDirectory != null) {
                sourceDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "sourceDirectory.delete failed: ";
        }

        try {
            if (destinationDirectory != null) {
                destinationDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "destinationDirectory.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertEquals(expSourceDirectoryFileCount, sourceDirectoryFileCount);
            assertEquals(expDestinationDirectoryFileCount, destinationDirectoryFileCount);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }


    /**
     * Test of rename method, of class RRepositoryDirectory.
     */
    @Test
    public void testRepositoryDirectoryRename() {

        // Test variables.
        String expSourceDirectoryName = null;
        RRepositoryDirectory sourceDirectory = null;
        RRepositoryFile sourceFile = null;
        boolean sourceDirectoryFound = false;
        RRepositoryDirectory destinationDirectory = null;
        RRepositoryFile destinationFile = null;
        boolean destinationDirectoryFound = false;
        String expDestinationDirectoryName = null;
        int expDestinationDirectoryFileCount = 1;
        int destinationDirectoryFileCount = 0;
        List<RRepositoryDirectory> listDirectories = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        expSourceDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            sourceDirectory = rUser.createDirectory(expSourceDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory sourceDirectory failed: ";
        }

        if (exception == null) {
            try {
                RepoUploadOptions options = new RepoUploadOptions();
                options.filename = "testRepositoryDirectoryRename.R";
                options.directory = sourceDirectory.about().name;
                sourceFile = rUser.writeFile("x <- rnorm(100)", options);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.writeFile failed: ";
            }
        }

        expDestinationDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            destinationDirectory = sourceDirectory.rename(expDestinationDirectoryName);
            sourceFile = null;
            sourceDirectory = null;
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "sourceDirectory.rename failed: ";
        }

        if (exception == null) {
            try {
                listDirectories = rUser.listDirectories(true, true, true, true);
                // search for filename
                for (RRepositoryDirectory directory : listDirectories) {
                    if (directory.about().name.equalsIgnoreCase(expSourceDirectoryName)) {
                        sourceDirectoryFound = true;
                    } else if (directory.about().name.equalsIgnoreCase(expDestinationDirectoryName)) {
                        destinationDirectoryFound = true;
                        destinationDirectoryFileCount = directory.about().files.size();
                        destinationFile = (RRepositoryFile) directory.about().files.get(0);
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listDirectories failed: ";
            }
        }

        // Test cleanup.
        try {
            if (sourceFile != null) {
                sourceFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "sourceFile.delete failed: ";
        }
        try {
            if (destinationFile != null) {
                destinationFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "destinationFile.delete failed: ";
        }
        try {
            if (sourceDirectory != null) {
                sourceDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "sourceDirectory.delete failed: ";
        }
        try {
            if (destinationDirectory != null) {
                destinationDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "destinationDirectory.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertFalse(sourceDirectoryFound);
            assertTrue(destinationDirectoryFound);
            assertEquals(expDestinationDirectoryFileCount, destinationDirectoryFileCount);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of archive method, of class RRepositoryDirectory.
     */
    @Test
    public void testRepositoryDirectoryArchiveAll() {

        // Test variables.
        String expSourceDirectoryName = null;
        RRepositoryDirectory sourceDirectory = null;
        RRepositoryFile sourceFile = null;
        boolean sourceDirectoryFound = false;
        String expDestinationDirectoryName = null;
        RRepositoryDirectory archiveDirectory = null;
        boolean archiveDirectoryFound = false;
        String expArchiveDirectoryName = null;
        int archiveDirectoryFileCount = 0;
        int expArchiveDirectoryFileCount = 1;
        RRepositoryFile archiveFile = null;
        List<RRepositoryDirectory> listDirectories = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        expSourceDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            sourceDirectory = rUser.createDirectory(expSourceDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory sourceDirectory failed: ";
        }

        if (exception == null) {
            try {
                RepoUploadOptions options = new RepoUploadOptions();
                options.filename = "testRepositoryDirectoryArchive.R";
                options.directory = sourceDirectory.about().name;
                sourceFile = rUser.writeFile("x <- rnorm(100)", options);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.writeFile failed: ";
            }
        }

        expDestinationDirectoryName = DeployrUtil.getUniqueDirectoryName();
        expArchiveDirectoryName = expDestinationDirectoryName + ".archive";

        try {
            archiveDirectory = sourceDirectory.archive(expDestinationDirectoryName, null);
            sourceFile = null;
            sourceDirectory = null;
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "sourceDirectory.archive failed: ";
        }

        if (exception == null) {
            try {
                listDirectories = rUser.listDirectories(true, true, true, true);
                // search for filename
                for (RRepositoryDirectory directory : listDirectories) {
                    if (directory.about().name.equalsIgnoreCase(expSourceDirectoryName)) {
                        sourceDirectoryFound = true;
                    } else if (directory.about().name.equalsIgnoreCase(expArchiveDirectoryName)) {
                        archiveDirectoryFound = true;
                        archiveDirectoryFileCount = directory.about().files.size();
                        archiveFile = (RRepositoryFile) directory.about().files.get(0);
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listDirectories failed: ";
            }
        }

        // Test cleanup.
        try {
            if (sourceFile != null) {
                sourceFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "sourceFile.delete failed: ";
        }
        try {
            if (archiveFile != null) {
                archiveFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "archiveFile.delete failed: ";
        }
        try {
            if (sourceDirectory != null) {
                sourceDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "sourceDirectory.delete failed: ";
        }
        try {
            if (archiveDirectory != null) {
                archiveDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "archiveDirectory.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertFalse(sourceDirectoryFound);
            assertTrue(archiveDirectoryFound);
            assertEquals(expArchiveDirectoryFileCount, archiveDirectoryFileCount);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of archive method, of class RRepositoryDirectory.
     */
    @Test
    public void testRepositoryDirectoryArchiveSome() {

        // Test variables.
        String expSourceDirectoryName = null;
        RRepositoryDirectory sourceDirectory = null;
        RRepositoryFile sourceFileOne = null;
        RRepositoryFile sourceFileTwo = null;
        boolean sourceDirectoryFound = false;
        String expDestinationDirectoryName = null;
        RRepositoryDirectory archiveDirectory = null;
        boolean archiveDirectoryFound = false;
        String expArchiveDirectoryName = null;
        int sourceDirectoryFileCount = 0;
        int expSourceDirectoryFileCount = 1;
        int archiveDirectoryFileCount = 0;
        int expArchiveDirectoryFileCount = 1;
        RRepositoryFile sourceFile = null;
        RRepositoryFile archiveFile = null;
        List<RRepositoryDirectory> listDirectories = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        expSourceDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            sourceDirectory = rUser.createDirectory(expSourceDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory sourceDirectory failed: ";
        }

        if (exception == null) {
            try {
                RepoUploadOptions options = new RepoUploadOptions();
                options.filename = "testRepositoryDirectoryArchiveOne.R";
                options.directory = sourceDirectory.about().name;
                sourceFileOne = rUser.writeFile("x <- rnorm(100)", options);
                options.filename = "testRepositoryDirectoryArchiveTwo.R";
                sourceFileTwo = rUser.writeFile("x <- rnorm(100)", options);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.writeFile failed: ";
            }
        }

        expDestinationDirectoryName = DeployrUtil.getUniqueDirectoryName();
        expArchiveDirectoryName = expDestinationDirectoryName + ".archive";

        try {
            archiveDirectory = sourceDirectory.archive(expDestinationDirectoryName, Arrays.asList(sourceFileTwo));
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "sourceDirectory.archive failed: ";
        }

        if (exception == null) {
            try {
                listDirectories = rUser.listDirectories(true, true, true, true);
                // search for filename
                for (RRepositoryDirectory directory : listDirectories) {
                    if (directory.about().name.equalsIgnoreCase(expSourceDirectoryName)) {
                        sourceDirectoryFound = true;
                        sourceDirectoryFileCount = directory.about().files.size();
                        sourceFile = (RRepositoryFile) directory.about().files.get(0);
                    } else if (directory.about().name.equalsIgnoreCase(expArchiveDirectoryName)) {
                        archiveDirectoryFound = true;
                        archiveDirectoryFileCount = directory.about().files.size();
                        archiveFile = (RRepositoryFile) directory.about().files.get(0);
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listDirectories failed: ";
            }
        }

        // Test cleanup.
        try {
            if (sourceFile != null) {
                sourceFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "sourceFile.delete failed: ";
        }
        try {
            if (archiveFile != null) {
                archiveFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "archiveFile.delete failed: ";
        }
        try {
            if (sourceDirectory != null) {
                sourceDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "sourceDirectory.delete failed: ";
        }
        try {
            if (archiveDirectory != null) {
                archiveDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "archiveDirectory.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertTrue(sourceDirectoryFound);
            assertTrue(archiveDirectoryFound);
            assertEquals(expSourceDirectoryFileCount, sourceDirectoryFileCount);
            assertEquals(expArchiveDirectoryFileCount, archiveDirectoryFileCount);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of update method, of class RRepositoryDirectory.
     */
    @Test
    public void testRepositoryDirectoryUpdateAll() {

        // Test variables.
        String expSourceDirectoryName = null;
        RRepositoryDirectory sourceDirectory = null;
        RRepositoryFile sourceFileOne = null;
        RRepositoryFile sourceFileTwo = null;
        int privateFileCount = 0;
        int expPrivateFileCount = 0;
        int publicFileCount = 0;
        int expPublicFileCount = 2;
        List<RRepositoryDirectory> listDirectories = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        expSourceDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            sourceDirectory = rUser.createDirectory(expSourceDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory sourceDirectory failed: ";
        }

        if (exception == null) {
            try {
                RepoUploadOptions options = new RepoUploadOptions();
                options.filename = "testRepositoryDirectoryUpdateOne.R";
                options.directory = sourceDirectory.about().name;
                sourceFileOne = rUser.writeFile("x <- rnorm(100)", options);
                options.filename = "testRepositoryDirectoryUpdateTwo.R";
                sourceFileTwo = rUser.writeFile("x <- rnorm(100)", options);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.writeFile failed: ";
            }
        }

        try {
            RepoAccessControlOptions accessControlOptions = new RepoAccessControlOptions();
            accessControlOptions.published = true;
            sourceDirectory.update(accessControlOptions, null);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "sourceDirectory.update failed: ";
        }

        if (exception == null) {
            try {
                listDirectories = rUser.listDirectories(true, true, true, true);
                // search for filename
                for (RRepositoryDirectory directory : listDirectories) {
                    if (directory.about().name.equalsIgnoreCase(expSourceDirectoryName)) {
                        for (RRepositoryFile file : directory.about().files) {
                            if (file.about().access.equals("Private")) {
                                privateFileCount++;
                            } else if (file.about().access.equals("Public")) {
                                publicFileCount++;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listDirectories failed: ";
            }
        }

        // Test cleanup.
        try {
            if (sourceDirectory != null) {
                sourceDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "sourceDirectory.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertEquals(expPrivateFileCount, privateFileCount);
            assertEquals(expPublicFileCount, publicFileCount);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of update method, of class RRepositoryDirectory.
     */
    @Test
    public void testRepositoryDirectoryUpdateSome() {

        // Test variables.
        String expSourceDirectoryName = null;
        RRepositoryDirectory sourceDirectory = null;
        RRepositoryFile sourceFileOne = null;
        RRepositoryFile sourceFileTwo = null;
        int privateFileCount = 0;
        int expPrivateFileCount = 1;
        int publicFileCount = 0;
        int expPublicFileCount = 1;
        List<RRepositoryDirectory> listDirectories = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        expSourceDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            sourceDirectory = rUser.createDirectory(expSourceDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory sourceDirectory failed: ";
        }

        if (exception == null) {
            try {
                RepoUploadOptions options = new RepoUploadOptions();
                options.filename = "testRepositoryDirectoryUpdateOne.R";
                options.directory = sourceDirectory.about().name;
                sourceFileOne = rUser.writeFile("x <- rnorm(100)", options);
                options.filename = "testRepositoryDirectoryUpdateTwo.R";
                sourceFileTwo = rUser.writeFile("x <- rnorm(100)", options);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.writeFile failed: ";
            }
        }

        try {
            RepoAccessControlOptions accessControlOptions = new RepoAccessControlOptions();
            accessControlOptions.published = true;
            sourceDirectory.update(accessControlOptions, Arrays.asList(sourceFileTwo));
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "sourceDirectory.update failed: ";
        }

        if (exception == null) {
            try {
                listDirectories = rUser.listDirectories(true, true, true, true);
                // search for filename
                for (RRepositoryDirectory directory : listDirectories) {
                    if (directory.about().name.equalsIgnoreCase(expSourceDirectoryName)) {
                        for (RRepositoryFile file : directory.about().files) {
                            if (file.about().access.equals("Private")) {
                                privateFileCount++;
                            } else if (file.about().access.equals("Public")) {
                                publicFileCount++;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listDirectories failed: ";
            }
        }

        // Test cleanup.
        try {
            if (sourceDirectory != null) {
                sourceDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "sourceDirectory.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertEquals(expPrivateFileCount, privateFileCount);
            assertEquals(expPublicFileCount, publicFileCount);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of copyFiles method, of class RUserRepositoryFileCalls.
     */
    @Test
    public void testUserRepositoryCopyFiles() {

        // Test variables.
        String sourceDirectoryName = null;
        RRepositoryDirectory sourceDirectory = null;
        RRepositoryFile sourceFile = null;
        int expSourceDirectoryFileCount = 1;
        int sourceDirectoryFileCount = 0;
        String destinationDirectoryName = null;
        RRepositoryDirectory destinationDirectory = null;
        int expDestinationDirectoryFileCount = 1;
        int destinationDirectoryFileCount = 0;
        List<RRepositoryDirectory> listDirectories = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        sourceDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            sourceDirectory = rUser.createDirectory(sourceDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory sourceDirectory failed: ";
        }

        destinationDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            destinationDirectory = rUser.createDirectory(destinationDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory destination failed: ";
        }

        if (exception == null) {
            try {
                RepoUploadOptions options = new RepoUploadOptions();
                options.filename = "testUserRepositoryCopyFiles.R";
                options.directory = sourceDirectory.about().name;
                sourceFile = rUser.writeFile("x <- rnorm(100)", options);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.writeFile failed: ";
            }
        }

        if (exception == null) {
            try {
                rUser.copyFiles(destinationDirectory.about().name,
                        Arrays.asList(sourceFile), null);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.copyFiles failed: ";
            }
        }

        if (exception == null) {
            try {
                listDirectories = rUser.listDirectories(true, true, true, true);
                // search for filename
                for (RRepositoryDirectory directory : listDirectories) {
                    if (directory.about().name.equalsIgnoreCase(sourceDirectoryName)) {
                        sourceDirectoryFileCount = directory.about().files.size();
                    } else if (directory.about().name.equalsIgnoreCase(destinationDirectoryName)) {
                        destinationDirectoryFileCount = directory.about().files.size();
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listDirectories failed: ";
            }
        }

        // Test cleanup.
        try {
            if (sourceFile != null) {
                sourceFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "sourceFile.delete failed: ";
        }
        try {
            if (sourceDirectory != null) {
                sourceDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "sourceDirectory.delete failed: ";
        }

        try {
            if (destinationDirectory != null) {
                destinationDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "destinationDirectory.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertEquals(expSourceDirectoryFileCount, sourceDirectoryFileCount);
            assertEquals(expDestinationDirectoryFileCount, destinationDirectoryFileCount);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of copyFiles method, of class RUserRepositoryFileCalls.
     */
    @Test
    public void testUserRepositoryMoveFiles() {

        // Test variables.
        String sourceDirectoryName = null;
        RRepositoryDirectory sourceDirectory = null;
        RRepositoryFile sourceFileOne = null;
        RRepositoryFile sourceFileTwo = null;
        int expSourceDirectoryFileCount = 0;
        int sourceDirectoryFileCount = 0;
        String destinationDirectoryName = null;
        RRepositoryDirectory destinationDirectory = null;
        int expDestinationDirectoryFileCount = 2;
        int destinationDirectoryFileCount = 0;
        List<RRepositoryDirectory> listDirectories = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        sourceDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            sourceDirectory = rUser.createDirectory(sourceDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory sourceDirectory failed: ";
        }

        destinationDirectoryName = DeployrUtil.getUniqueDirectoryName();

        try {
            destinationDirectory = rUser.createDirectory(destinationDirectoryName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createDirectory destination failed: ";
        }

        if (exception == null) {
            try {
                RepoUploadOptions options = new RepoUploadOptions();
                options.filename = "testUserRepositoryMoveFilesOne.R";
                options.directory = sourceDirectory.about().name;
                sourceFileOne = rUser.writeFile("x <- rnorm(100)", options);
                options.filename = "testUserRepositoryMoveFilesTwo.R";
                sourceFileTwo = rUser.writeFile("x <- rnorm(100)", options);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.writeFile failed: ";
            }
        }

        if (exception == null) {
            try {
                rUser.moveFiles(destinationDirectory.about().name,
                        Arrays.asList(sourceFileOne, sourceFileTwo));
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.moveFiles failed: ";
            }
        }

        if (exception == null) {
            try {
                listDirectories = rUser.listDirectories(true, true, true, true);
                // search for filename
                for (RRepositoryDirectory directory : listDirectories) {
                    if (directory.about().name.equalsIgnoreCase(sourceDirectoryName)) {
                        sourceDirectoryFileCount = directory.about().files.size();
                    } else if (directory.about().name.equalsIgnoreCase(destinationDirectoryName)) {
                        destinationDirectoryFileCount = directory.about().files.size();
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listDirectories failed: ";
            }
        }

        // Test cleanup.
        try {
            if (sourceDirectory != null) {
                sourceDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "sourceDirectory.delete failed: ";
        }

        try {
            if (destinationDirectory != null) {
                destinationDirectory.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "destinationDirectory.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertEquals(expSourceDirectoryFileCount, sourceDirectoryFileCount);
            assertEquals(expDestinationDirectoryFileCount, destinationDirectoryFileCount);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of uploadFile method, of class RUserRepositoryFileCalls.
     */

/*
    @Test
    public void testUserRepositoryUploadFile() {

        // Test variables.
        String actualRepoFileName = "";
        String actualRepoFileDesc = "";
        String expRepoFileName = "";
        String expRepoFileDesc = "";
        File file = new File("/etc/hosts");
        RepoUploadOptions options = null;
        RRepositoryFile repoFile = null;
        URL url = null;
        String urlData = "";

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        //Test.
        expRepoFileName = DeployrUtil.getUniqueFileName("txt");
        expRepoFileDesc = "hosts file";
        options = new RepoUploadOptions();
        options.descr = expRepoFileDesc;
        options.filename = expRepoFileName;

        try {
            repoFile = rUser.uploadFile(new FileInputStream(file), options);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.uploadFile failed: ";
        }

        if (exception == null) {
            try {
                actualRepoFileName = repoFile.about().filename;
                actualRepoFileDesc = repoFile.about().descr;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoFile.about failed: ";
            }
        }

        if (exception == null) {
            try {
                url = repoFile.download();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoFile.download failed: ";
            }
        }

        if (exception == null) {
            urlData = DeployrUtil.getDataFromURL(url);
        }

        // Test cleanup.
        try {
            if (repoFile != null) {
                repoFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "repoFile.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertEquals(expRepoFileName, actualRepoFileName);
            assertEquals(expRepoFileDesc, actualRepoFileDesc);
            assertNotNull(urlData);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }
*/
}
