/*
 * RProjectFileTest.java
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
import com.revo.deployr.client.about.RProjectFileDetails;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.factory.RClientFactory;
import com.revo.deployr.client.params.DirectoryUploadOptions;
import com.revo.deployr.client.params.RepoUploadOptions;
import org.junit.*;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

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
            String url = System.getProperty("connection.protocol") +
                            System.getProperty("connection.endpoint");
            if (url == null) {
                fail("setUp: connection.[protocol|endpoint] null.");
            }
            boolean allowSelfSigned = 
                Boolean.valueOf(System.getProperty("allow.SelfSignedSSLCert"));
            rClient =RClientFactory.createClient(url, allowSelfSigned);
            RBasicAuthentication rAuthentication = new RBasicAuthentication("testuser", System.getProperty("password.testuser"));

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
        InputStream resultStream = null;
        String urlData;

        // Test.
        try {
            fileDetails = rProjectFile.about();
        } catch (Exception ex) {
            fail("rProjectFile.about failed: " + ex.getMessage());
        }
        try {
            resultStream = rProjectFile.download();
        } catch (Exception ex) {
            fail("rProjectFile.download failed: " + ex.getMessage());
        }

        // Test asserts.
        assertEquals(expFileName, fileDetails.filename);
        assertEquals(expDescr, fileDetails.descr);

        assertNotNull(resultStream);

        urlData = DeployrUtil.getDataFromStream(resultStream);

        assertNotNull(urlData);
        assertEquals(DeployrUtil.encodeString(fileContents),
                            DeployrUtil.encodeString(urlData));
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
        InputStream repoStream = null;

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
            repoStream = repoFile.download();
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProjectFile.store failed: ";
        }

        urlData = DeployrUtil.getDataFromStream(repoStream);

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
        InputStream fileStream = null;

        //Test.
        try {
            fileStream = rProjectFile.download();
        } catch (Exception ex) {
            fail("rProjectFile.download failed: " + ex.getMessage());
        }

        // Test asserts.
        assertNotNull(fileStream);
        urlData = DeployrUtil.getDataFromStream(fileStream);
        assertNotNull(urlData);
        assertEquals(DeployrUtil.encodeString(fileContents), DeployrUtil.encodeString(urlData));
    }
}
