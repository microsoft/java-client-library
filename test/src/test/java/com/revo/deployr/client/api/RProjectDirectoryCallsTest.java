/*
 * RProjectDirectoryCallsTest.java
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
import com.revo.deployr.client.*;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.factory.RClientFactory;
import com.revo.deployr.client.params.DirectoryUploadOptions;
import com.revo.deployr.client.params.RepoUploadOptions;
import org.junit.*;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RProjectDirectoryCallsTest {

    RClient rClient = null;
    RUser rUser = null;
    RProject rProject = null;

    public RProjectDirectoryCallsTest() {
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
            rUser = rClient.login(rAuthentication);
            rProject = DeployrUtil.createTemporaryProject(rUser);
            assert (rProject != null);
        } catch (Exception ex) {
            if (rProject != null) {
                try {
                    rProject.close();
                } catch (Exception exp) {
                }
            }
            if (rClient != null) {
                rClient.release();
            }
            fail("setUp: " + ex);
        }
    }

    @After
    public void tearDown() {
        if (rProject != null) {
            try {
                rProject.close();
            } catch (Exception exp) {
            }
        }

        if (rClient != null) {
            rClient.release();
        }
    }

    /**
     * Test of listFiles method, of class RProject.
     */
    @Test
    public void testProjectDirectoryListFiles() {

        // Test variables.
        int expSize = 0;
        List listFiles = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {
            listFiles = rProject.listFiles();
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.listFiles failed: ";
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(expSize, listFiles.size());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of uploadFile method, of class RProjectDirectoryCalls.
     */
    @Test
    public void testProjectDirectoryUploadFile() {

        // Test variables.
        DirectoryUploadOptions options = null;
        RProjectFile actualProjectFile = null;
        String actualProjectFileName = "";
        String actualProjectFileDesc = "";
        String expProjectFileName = "";
        String expProjectFileDesc = "";
        String urlData = "";
        URL url = null;
        File file = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        expProjectFileName = DeployrUtil.getUniqueFileName("txt");
        expProjectFileDesc = "host-file";
        file = new File("/etc/hosts");
        options = new DirectoryUploadOptions();
        options.descr = expProjectFileDesc;
        options.filename = expProjectFileName;
        options.overwrite = true;
        try {
            actualProjectFile = rProject.uploadFile(new FileInputStream(file), options);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.uploadFile failed: ";
        }

        if (exception == null) {
            try {
                actualProjectFileName = actualProjectFile.about().filename;
                actualProjectFileDesc = actualProjectFile.about().descr;
                url = actualProjectFile.download();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "actualProjectFile.about failed: ";
            }
        }

        if (exception == null) {
            try {
                url = actualProjectFile.download();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "actualProjectFile.about failed: ";
            }
        }

        if (exception == null) {
            urlData = DeployrUtil.getDataFromURL(url);
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(expProjectFileName, actualProjectFileName);
            assertEquals(expProjectFileDesc, actualProjectFileDesc);
            assertNotNull(urlData);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }


    }

    /**
     * Test of transferFile method, of class RProjectDirectoryCalls.
     */
    @Test
    public void testProjectDirectoryTransferFile() {

        // Test variables.
        DirectoryUploadOptions options = null;
        RProjectFile actualProjectFile = null;
        String actualProjectFileName = "";
        String actualProjectFileDesc = "";
        String expProjectFileName = "";
        String expProjectFileDesc = "";
        String urlData = "";
        File file = null;
        RProjectFile upLoadFile = null;
        URL urlUpLoadFile = null;
        String urlUploadData = "";
        URL urlTransferFile = null;
        String urlTransferData = "";

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        file = new File("/etc/hosts");
        expProjectFileName = DeployrUtil.getUniqueFileName("txt");
        expProjectFileDesc = "host-file";
        options = new DirectoryUploadOptions();
        options.descr = expProjectFileDesc;
        options.filename = expProjectFileName;
        options.overwrite = true;

        try {
            upLoadFile = rProject.uploadFile(new FileInputStream(file), options);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.uploadFile failed: ";
        }

        if (exception == null) {
            try {
                urlUpLoadFile = upLoadFile.download();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "upLoadFile.about failed: ";
            }
        }

        if (exception == null) {
            try {
                actualProjectFile = rProject.transferFile(urlUpLoadFile, options);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.transferFile failed: ";
            }
        }

        if (exception == null) {
            try {
                actualProjectFileName = actualProjectFile.about().filename;
                actualProjectFileDesc = actualProjectFile.about().descr;
                urlTransferFile = actualProjectFile.download();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "actualProjectFile.about failed: ";
            }
        }

        if (exception == null) {
            urlUploadData = DeployrUtil.getDataFromURL(urlUpLoadFile);
            urlTransferData = DeployrUtil.getDataFromURL(urlTransferFile);
        }

        if (exception == null) {
            assertEquals(expProjectFileName, actualProjectFileName);
            assertEquals(expProjectFileDesc, actualProjectFileDesc);
            assertEquals(DeployrUtil.encodeString(urlUploadData), DeployrUtil.encodeString(urlTransferData));
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

    }

    /**
     * Test of writeFile method, of class RProject.
     */
    @Test
    public void testProjectDirectoryWriteFile() {

        // Test variables.
        String expContents = "this is a test";
        String actualProjectFileName = "";
        String actualProjectFileDesc = "";
        String expProjectFileName = "";
        String expProjectFileDesc = "";
        DirectoryUploadOptions options = null;
        RProjectFile actualProjectFile = null;
        URL url = null;
        String urlData = "";

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        options = new DirectoryUploadOptions();
        expProjectFileName = DeployrUtil.getUniqueFileName("txt");
        expProjectFileDesc = "junit test testWriteFile";
        options.descr = expProjectFileDesc;
        options.filename = expProjectFileName;
        options.overwrite = true;
        try {
            actualProjectFile = rProject.writeFile(expContents, options);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.writeFile failed: ";
        }

        if (exception == null) {
            try {
                actualProjectFileName = actualProjectFile.about().filename;
                actualProjectFileDesc = actualProjectFile.about().descr;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "actualProjectFile.about failed: ";
            }
        }

        if (exception == null) {
            try {
                url = actualProjectFile.download();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "actualProjectFile.download failed: ";
            }
        }

        if (exception == null) {
            urlData = DeployrUtil.getDataFromURL(url);
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(expProjectFileName, actualProjectFileName);
            assertEquals(expProjectFileDesc, actualProjectFileDesc);
            assertEquals(DeployrUtil.encodeString(expContents), DeployrUtil.encodeString(urlData));
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of loadFile method, of class RProjectDirectoryCalls.
     */
    @Test
    public void testProjectDirectoryLoadFile() {

        // Test variables.
        String text = "this is a test";
        String actualProjectFileName = "";
        String actualProjectFileDesc = "";
        String expProjectFileName = "";
        String expProjectFileDesc = "";
        RepoUploadOptions uploadOptions = null;
        RRepositoryFile repoFile = null;
        RProjectFile actualProjectFile = null;
        URL url = null;
        String urlData = "";
        long actualSize = 0;
        long expSize = 0;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        expProjectFileName = DeployrUtil.getUniqueFileName("txt");
        expProjectFileDesc = "test repo file";
        uploadOptions = new RepoUploadOptions();
        uploadOptions.descr = expProjectFileDesc;
        uploadOptions.filename = expProjectFileName;
        uploadOptions.newversion = true;

        try {
            repoFile = rUser.writeFile(text, uploadOptions);
            expSize = repoFile.about().size;
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.writeFile failed: ";
        }

        if (exception == null) {
            try {
                actualProjectFile = rProject.loadFile(repoFile);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.loadFile failed: ";
            }
        }

        if (exception == null) {
            try {
                actualProjectFileName = actualProjectFile.about().filename;
                actualProjectFileDesc = actualProjectFile.about().descr;
                url = actualProjectFile.download();
                actualSize = actualProjectFile.about().size;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "actualProjectFile.about failed: ";
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
            assertEquals(expProjectFileName, actualProjectFileName);
            assertEquals(expProjectFileDesc, actualProjectFileDesc);
            assertEquals(expSize, actualSize);
            assertEquals(DeployrUtil.encodeString(text), DeployrUtil.encodeString(urlData));
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of downloadFiles method, of class RProjectDirectoryCalls.
     */
    @Test
    public void testProjectDirectoryDownloadFiles() {

        // Test variables.
        String expectedResults = "PK";
        String text = "this is a test";
        String actualProjectFileName = "";
        String actualProjectFileDesc = "";
        String expProjectFileName = "";
        String expProjectFileDesc = "";
        RProjectFile actualProjectFile = null;
        DirectoryUploadOptions options = null;
        URL url = null;
        String urlData = "";

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        expProjectFileName = DeployrUtil.getUniqueFileName("txt");
        expProjectFileDesc = "junit test testDownloadFiles";
        options = new DirectoryUploadOptions();
        options.descr = expProjectFileDesc;
        options.filename = expProjectFileName;
        options.overwrite = true;

        try {
            actualProjectFile = rProject.writeFile(text, options);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.writeFile failed: ";
        }

        if (exception == null) {
            try {
                actualProjectFileName = actualProjectFile.about().filename;
                actualProjectFileDesc = actualProjectFile.about().descr;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "results.about failed: ";
            }
        }

        if (exception == null) {
            try {
                url = rProject.downloadFiles();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.downloadFiles failed: ";
            }
        }

        if (exception == null) {
            urlData = DeployrUtil.getDataFromURL(url);
        }

        if (exception == null) {
            // Test assertions.
            assertEquals(expProjectFileName, actualProjectFileName);
            assertEquals(expProjectFileDesc, actualProjectFileDesc);
            assertEquals(expectedResults, urlData.substring(0, 2));
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of downloadFiles method, of class RProjectDirectoryCalls.
     */
    @Test
    public void testProjectDirectoryDownloadFilesAsList() {

        // Test variables.
        List<String> listFiles = null;
        DirectoryUploadOptions options = null;
        String expectedResults = "PK";
        String text = "this is a line";
        URL url = null;
        String urlData = "";
        RProjectFile projectFile = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        listFiles = new ArrayList();
        options = new DirectoryUploadOptions();
        options.descr = "junit test test DownloadFiles-List";
        options.filename = DeployrUtil.getUniqueFileName("txt");
        listFiles.add(options.filename);
        options.overwrite = true;
        listFiles.add(options.filename);

        try {
            projectFile = rProject.writeFile(text, options);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.writeFile failed: ";
        }


        if (exception == null) {
            options = new DirectoryUploadOptions();
            options.descr = "junit test test DownloadFiles-List 2";
            options.filename = DeployrUtil.getUniqueFileName("txt");
            options.overwrite = true;
            listFiles.add(options.filename);

            try {
                projectFile = rProject.writeFile(text, options);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.writeFile failed: ";
            }
        }

        if (exception == null) {
            try {
                url = rProject.downloadFiles(listFiles);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.downloadFiles failed: ";
            }
        }

        if (exception == null) {
            urlData = DeployrUtil.getDataFromURL(url);
        }

        if (exception == null) {
            // Test assertions.
            assertEquals(expectedResults, urlData.substring(0, 2));
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }
}
