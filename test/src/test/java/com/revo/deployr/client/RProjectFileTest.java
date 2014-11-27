/*
 * RProjectFileTest.java
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
import com.revo.deployr.client.about.RProjectFileDetails;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.factory.RClientFactory;
import com.revo.deployr.client.params.DirectoryUploadOptions;
import com.revo.deployr.client.params.RepoUploadOptions;
import org.junit.*;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author nriesland
 */
public class RProjectFileTest {

    RClient rClient = null;
    RUser rUser = null;
    RProject rProject = null;
    String connectUrl;
    RProjectFile rProjectFile = null;
    String fileName = null;
    String fileDesc = "junit test RProjectFileTest";
    String fileContents = "this is a test";

    public RProjectFileTest() {
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
            connectUrl = System.getProperty("url.property");
            if (connectUrl == null) {
                connectUrl = "localhost:" + DeployrUtil.DEFAULT_PORT;
            }
            rClient = RClientFactory.createClient("http://" + connectUrl + "/deployr");
            RBasicAuthentication rAuthentication = new RBasicAuthentication("testuser", "changeme");

            rUser = rClient.login(rAuthentication);
            rProject = DeployrUtil.createTemporaryProject(rUser);
            assert (rProject != null);

            fileName = DeployrUtil.getUniqueFileName("txt");

            DirectoryUploadOptions options = new DirectoryUploadOptions();
            options.descr = fileDesc;
            options.filename = fileName;
            options.overwrite = true;
            String expResult = fileName;
            rProjectFile = rProject.writeFile(fileContents, options);
            assertNotNull(rProjectFile);

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
     * Test of about method, of class RProjectFile.
     */
    @Test
    public void testProjectFileAbout() {

        // Test variables.
        RProjectFileDetails fileDetails = null;
        String expFileName = fileName;
        String expDescr = fileDesc;
        URL resultURL = null;
        String urlData;

        // Test.
        try {
            fileDetails = rProjectFile.about();
        } catch (Exception ex) {
            fail("rProjectFile.about failed: " + ex.getMessage());
        }
        try {
            resultURL = rProjectFile.download();
        } catch (Exception ex) {
            fail("rProjectFile.download failed: " + ex.getMessage());
        }

        // Test asserts.
        assertEquals(expFileName, fileDetails.filename);
        assertEquals(expDescr, fileDetails.descr);

        assertNotNull(resultURL);

        urlData = DeployrUtil.getDataFromURL(resultURL);

        assertNotNull(urlData);
        assertEquals(DeployrUtil.encodeString(fileContents), DeployrUtil.encodeString(urlData));
    }

    /**
     * Test of update method, of class RProjectFile.
     */
    @Test
    public void testProjectFileUpdate() {

        // Test variables.
        RProjectFile projectFile = null;
        String expDescr = fileDesc + "_new";
        String expFileName = fileName + ".new";
        String actualFileName = null;
        String actualDesc = null;
        boolean overwrite = false;

        // Test.
        try {
            projectFile = rProjectFile.update(expFileName, expDescr, overwrite);
            actualFileName = projectFile.about().filename;
            actualDesc = projectFile.about().descr;
        } catch (Exception ex) {
            fail("rProjectFile.update failed: " + ex.getMessage());
        }

        // Test asserts.
        assertEquals(expFileName, actualFileName);
        assertEquals(expDescr, actualDesc);
    }

    /**
     * Test of update method, of class RProjectFile.
     */
    @Test
    public void testProjectFileUpdateOverwrite() throws Exception {

        // Test variables.
        RProjectFile projectFile = null;
        String expDescr = fileDesc + "_new";
        String expFileName = fileName + ".new";
        String actualFileName = null;
        String actualDesc = null;
        boolean overwrite = true;

        // Test.
        try {
            projectFile = rProjectFile.update(expFileName, expDescr, overwrite);
            actualFileName = projectFile.about().filename;
            actualDesc = projectFile.about().descr;
        } catch (Exception ex) {
            fail("rProjectFile.update failed: " + ex.getMessage());
        }

        // Test asserts.
        assertEquals(expFileName, actualFileName);
        assertEquals(expDescr, actualDesc);
    }

    /**
     * Test of store method, of class RProjectFile.
     */
    @Test
    public void testProjectFileStore() {

        // Test variables.
        RepoUploadOptions options = null;
        RRepositoryFile repoFile = null;
        String expFilename = fileName;
        String expDesc = fileDesc;
        String expAuthor = "testuser";
        String urlData = null;
        String actualFileName = null;
        String actualDesc = null;
        String actualAuthor = null;
        URL url = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test prep.
        options = new RepoUploadOptions();
        options.descr = fileDesc;
        options.filename = fileName;
        options.published = true;
        options.newversion = false;

        //Test.
        try {
            repoFile = rProjectFile.store(options);
            actualFileName = repoFile.about().filename;
            actualDesc = repoFile.about().descr;
            actualAuthor = repoFile.about().author;
            url = repoFile.download();
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProjectFile.store failed: ";
        }

        urlData = DeployrUtil.getDataFromURL(url);

        // Test cleanup.
        try {
            if (repoFile != null) {
                repoFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "repoFile.delete failed: ";
        }

        // Test asserts.
        if (exception == null) {
            assertEquals(expFilename, actualFileName);
            assertEquals(expDesc, actualDesc);
            assertEquals(expAuthor, actualAuthor);

            assertNotNull(urlData);

            assertEquals(DeployrUtil.encodeString(fileContents), DeployrUtil.encodeString(urlData));
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }

    }

    /**
     * Test of delete method, of class RProjectFile.
     */
    @Test
    public void testProjectFileDelete() {

        // Test variables.
        List<RProjectFile> projectFiles = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        //Test.
        try {
            rProjectFile.delete();
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProjectFile.store failed: ";
        }

        if (exception == null) {
            try {
                projectFiles = rProject.listFiles();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listFiles failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assert (projectFiles.size() == 0);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of download method, of class RProjectFile.
     */
    @Test
    public void testProjectFileDownload() {

        // Test variables.
        String urlData = null;
        URL url = null;

        //Test.
        try {
            url = rProjectFile.download();
        } catch (Exception ex) {
            fail("rProjectFile.download failed: " + ex.getMessage());
        }

        // Test asserts.
        assertNotNull(url);
        urlData = DeployrUtil.getDataFromURL(url);
        assertNotNull(urlData);
        assertEquals(DeployrUtil.encodeString(fileContents), DeployrUtil.encodeString(urlData));
    }
}
