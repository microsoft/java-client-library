/*
 * RProjectTest.java
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
package com.revo.deployr.client;

import com.revo.deployr.DeployrUtil;
import com.revo.deployr.client.about.RProjectDetails;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.factory.RClientFactory;
import com.revo.deployr.client.params.ProjectExecutionOptions;
import com.revo.deployr.client.params.ProjectDropOptions;
import org.junit.*;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static org.junit.Assert.*;

public class RProjectTest {

    RClient rClient = null;
    RUser rUser = null;
    RProject rProject = null;
    String projectName = "R Project Test";
    String projectDesc = "Created By R Project Test";

    public RProjectTest() {
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
            rProject = DeployrUtil.createPersistentProject(rUser, projectName, projectDesc);
            assert (rProject != null);
        } catch (Exception ex) {
            if (rProject != null) {
                try {
                    rProject.close();
                } catch (Exception exp) {
                }
                try {
                    rProject.delete();
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
            try {
                rProject.delete();
            } catch (Exception exp) {
            }
        }

        if (rClient != null) {
            rClient.release();
        }
    }

    /**
     * Test of about method, of class RProject.
     */
    @Test
    public void testProjectTestAbout() {

        // Test variables.
        RProjectDetails expDetails = null;
        RProjectDetails projectDetails = null;

        // Test.
        expDetails = new RProjectDetails("", "", false, null, null);
        expDetails.name = projectName;
        expDetails.descr = projectDesc;
        projectDetails = rProject.about();

        // Test asserts.          
        assertEquals(expDetails.name, projectDetails.name);
        assertEquals(expDetails.descr, projectDetails.descr);
    }

    /**
     * Test of ping method, of class RProject.
     */
    @Test
    public void testProjectTestPing() {

        // Test variables.
        boolean expPing = false;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {
            expPing = rProject.ping();
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.ping failed: ";
        }

        // Test asserts.
        if (exception == null) {
            assertTrue(expPing);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of update method, of class RProject.
     */
    @Test
    public void testProjectTestUpdate() {

        // Test variables.
        RProjectDetails projectDetails = null;
        RProjectDetails aboutDetails = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        aboutDetails = rProject.about();
        aboutDetails.name = projectName + " new name";
        aboutDetails.descr = projectDesc + " new desc";
        aboutDetails.longdescr = projectDesc + " long descr";
        aboutDetails.shared = true;
        try {
            projectDetails = rProject.update(aboutDetails);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.update failed: ";
        }

        // Test asserts.
        if (exception == null) {
            assertNotNull(projectDetails);
            assertEquals(aboutDetails.descr, projectDetails.descr);
            assertEquals(aboutDetails.longdescr, projectDetails.longdescr);
            assertEquals(aboutDetails.name, projectDetails.name);
            assertEquals(aboutDetails.origin, projectDetails.origin);
            assertTrue(projectDetails.shared);
            assertTrue(projectDetails.islive);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of save method, of class RProject.
     */
    @Test
    public void testProjectTestSave() {

        // Test variables.
        String newProjectName = "Project: Autosave";
        RProject rTempProject = null;
        RProjectDetails savedProjectDetails = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        rTempProject = DeployrUtil.createTemporaryProject(rUser);

        try {
            savedProjectDetails = rTempProject.save();
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rTempProject.save failed: ";
        }

        // Test cleanup.
        try {
            if (rTempProject != null) {
                rTempProject.close();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "rTempProject.close failed: ";
        }

        try {
            if (rTempProject != null) {
                rTempProject.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "rTempProject.delete failed: ";
        }


        // Test asserts.
        if (exception == null) {
            assertNotNull(savedProjectDetails);
            assertNull(savedProjectDetails.descr);
            assertNull(savedProjectDetails.longdescr);
            assertEquals(newProjectName, savedProjectDetails.name.substring(0, 17));
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of save method, of class RProject.
     */
    @Test
    public void testProjectTestSaveRProjectDetails() {

        // Test variables.
        String newProjectName = "New Project Name";
        RProject rTempProject = null;
        RProjectDetails savedProjectDetails = null;
        RProjectDetails aboutProjectDetails = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        rTempProject = DeployrUtil.createTemporaryProject(rUser);

        aboutProjectDetails = rProject.about();
        aboutProjectDetails.name = newProjectName;
        aboutProjectDetails.descr = projectDesc;
        aboutProjectDetails.shared = true;

        try {
            savedProjectDetails = rTempProject.save(aboutProjectDetails);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rTempProject.save failed: ";
        }

        // Test cleanup.
        try {
            if (rTempProject != null) {
                rTempProject.close();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "rTempProject.close failed: ";
        }

        try {
            if (rTempProject != null) {
                rTempProject.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "rTempProject.delete failed: ";
        }

        // Test asserts.
        if (exception == null) {
            assertNotNull(savedProjectDetails);
            assertNull(savedProjectDetails.longdescr);
            assertEquals(newProjectName, savedProjectDetails.name);
            assertEquals(projectDesc, savedProjectDetails.descr);
            assertTrue(savedProjectDetails.shared);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of save method, of class RProject.
     */
    @Test
    public void testProjectTestSaveRProjectDetailsDropOptionsTrue() {

        // Test variables.
        int historySize = 0;
        String code = "x_testSave<-5\nx_testSave";
        String codeExpResult = "\n> x_testSave<-5\n>x_testSave[1] 5\n";
        String newProjectName = "New Project Name";
        RProject rTempProject = null;
        RProjectDetails savedProjectDetails = null;
        RProjectDetails aboutProjectDetails = null;
        ProjectDropOptions dropOptions = null;
        List<RProjectExecution> listProjectExecution = null;
        RProjectExecution codeResult = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        rTempProject = DeployrUtil.createTemporaryProject(rUser);
        try {
            codeResult = rTempProject.executeCode(code);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rTempProject.executeCode failed: ";
        }

        if (exception == null) {
            aboutProjectDetails = rProject.about();
            aboutProjectDetails.name = newProjectName;
            aboutProjectDetails.descr = projectDesc;
            aboutProjectDetails.shared = true;

            dropOptions = new ProjectDropOptions();
            dropOptions.dropDirectory = true;
            dropOptions.dropHistory = true;
            dropOptions.dropWorkspace = true;

            try {
                savedProjectDetails = rTempProject.save(aboutProjectDetails, dropOptions);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rTempProject.save failed: ";
            }
        }

        if (exception == null) {
            try {
                listProjectExecution = rTempProject.getHistory();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rTempProject.getHistory failed: ";
            }
        }

        // Test cleanup.
        try {
            if (rTempProject != null) {
                rTempProject.close();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "rTempProject.close failed: ";
        }

        try {
            if (rTempProject != null) {
                rTempProject.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "rTempProject.delete failed: ";
        }

        // Test asserts.
        if (exception == null) {
            assertNotNull(savedProjectDetails);
            assertNull(savedProjectDetails.longdescr);
            assertEquals(newProjectName, savedProjectDetails.name);
            assertEquals(projectDesc, savedProjectDetails.descr);
            assertTrue(savedProjectDetails.shared);
            assertEquals(listProjectExecution.size(), 0);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of phantom property of class ProjectExecutionOptions.
     */
    @Test
    public void testProjectPhantomExecution() {

        // Test variables.
        String code = "x<-1";
        RProject rTempProject = null;
        RProjectExecution codeResult = null;
        List<RProjectExecution> projectHistoryList = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        rTempProject = DeployrUtil.createTemporaryProject(rUser);
        try {
            ProjectExecutionOptions options =
                new ProjectExecutionOptions();
            options.phantom = true;
            codeResult = rTempProject.executeCode(code, options);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rTempProject.executeCode failed: ";
        }

        if (exception == null) {
            try {
                projectHistoryList = rTempProject.getHistory();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rTempProject.getHistory failed: ";
            }
        }

        // Test cleanup.
        try {
            if (rTempProject != null) {
                rTempProject.close();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "rTempProject.close failed: ";
        }

        // Test asserts.
        if (exception == null) {
            assertEquals(projectHistoryList.size(), 0);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of saveAs method, of class RProject.
     */
    @Test
    public void testProjectTestSaveAsRProjectDetails() {

        // Test variables.
        String saveAsProjectName = "New Project Name save as";
        String saveAsProjectDesc = "New Project Name save as desc";
        RProject saveAsProject = null;
        RProjectDetails saveAsProjectDetails = null;
        RProjectDetails aboutProjectDetails = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        aboutProjectDetails = rProject.about();
        aboutProjectDetails.name = saveAsProjectName;
        aboutProjectDetails.descr = saveAsProjectDesc;
        aboutProjectDetails.shared = true;

        try {
            saveAsProject = rProject.saveAs(aboutProjectDetails);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.saveAs failed: ";
        }

        if (exception == null) {
            saveAsProjectDetails = saveAsProject.about();
        }

        // Test cleanup.
        try {
            if (saveAsProject != null) {
                saveAsProject.close();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "saveAsProject.close failed: ";
        }

        try {
            if (saveAsProject != null) {
                saveAsProject.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "saveAsProject.delete failed: ";
        }

        // Test asserts.
        if (exception == null) {
            assertNotNull(saveAsProjectDetails);
            assertNull(saveAsProjectDetails.longdescr);
            assertEquals(saveAsProjectName, saveAsProjectDetails.name);
            assertEquals(saveAsProjectDesc, saveAsProjectDetails.descr);
            assertTrue(saveAsProjectDetails.shared);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of saveAs method, of class RProject.
     */
    @Test
    public void testProjectTestSaveAsRProjectDetailsDropOptionsFalse() {

        // Test variables.
        int historySize = 1;
        String code = "x<-5";
        String saveAsProjectName = "New Project Name save as";
        String saveAsProjectDesc = "New Project Name save as desc";
        RProject saveAsProject = null;
        RProjectDetails saveAsProjectDetails = null;
        RProjectDetails aboutProjectDetails = null;
        ProjectDropOptions dropOptions = null;
        List<RProjectExecution> projectExecution = null;
        RProjectExecution codeResult = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        aboutProjectDetails = rProject.about();
        aboutProjectDetails.name = saveAsProjectName;
        aboutProjectDetails.descr = saveAsProjectDesc;
        aboutProjectDetails.shared = true;

        dropOptions = new ProjectDropOptions();
        dropOptions.dropDirectory = false;
        dropOptions.dropHistory = false;
        dropOptions.dropWorkspace = false;
        try {
            codeResult = rProject.executeCode(code);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.executeCode failed: ";
        }

        if (exception == null) {
            try {
                saveAsProject = rProject.saveAs(aboutProjectDetails);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.saveAs failed: ";
            }
        }

        if (exception == null) {
            try {
                projectExecution = saveAsProject.getHistory();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "saveAsProject.getHistory failed: ";
            }
        }

        if (exception == null) {
            saveAsProjectDetails = saveAsProject.about();

        }

        // Test cleanup.
        try {
            if (saveAsProject != null) {
                saveAsProject.close();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "saveAsProject.close failed: ";
        }

        try {
            if (saveAsProject != null) {
                saveAsProject.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "saveAsProject.delete failed: ";
        }

        // Test asserts.
        if (exception == null) {
            assertNotNull(saveAsProjectDetails);
            assertNull(saveAsProjectDetails.longdescr);
            assertEquals(saveAsProjectName, saveAsProjectDetails.name);
            assertEquals(saveAsProjectDesc, saveAsProjectDetails.descr);
            assertTrue(saveAsProjectDetails.shared);
            assertEquals(projectExecution.size(), historySize);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of export method, of class RProject.
     */
    @Test
    public void testProjectTestImportExport() {

        // Test variables.
        int historySize = 1;
        String code = "x_testExport<-5\nplot(x_testExport)";
        String importFileName = null;
        String importDescr = "import project from file";
        RProjectExecution projectExecution = null;
        List<RProjectExecution> listProjectExecution = null;
        List<RProjectExecution> listImportProjectExecution = null;
        URL url = null;
        RProject importProject = null;
        File importFile = null;
        URLConnection urlConnection = null;
        DataInputStream dis = null;
        FileOutputStream fos = null;
        String importProjectName = "";

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            projectExecution = rProject.executeCode(code);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.executeCode failed: ";
        }

        if (exception == null) {
            try {
                listProjectExecution = rProject.getHistory();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getHistory failed: ";
            }
        }

        if (exception == null) {
            try {
                url = rProject.export();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.export failed: ";
            }
        }

        if (exception == null) {
            try {
                urlConnection = url.openConnection();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "url.openConnection failed: ";
            }
        }

        if (exception == null) {
            try {
                dis = new DataInputStream(urlConnection.getInputStream());
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "urlConnection.getInputStream failed: ";
            }
        }

        if (exception == null) {
            importFileName = DeployrUtil.getUniqueFileName("zip");
            try {
                fos = new FileOutputStream(importFileName);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "new FileOutputStream failed: ";
            }
        }

        if (exception == null) {
            int numRead = -1;
            int len = 8192;
            byte[] raw = new byte[len];
            try {
                while ((numRead = dis.read(raw, 0, len)) != -1) {
                    fos.write(raw, 0, numRead);
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "dis.read or fos.write failed: ";
            }
        }

        if (exception == null) {
            try {
                fos.close();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "fos.close failed: ";
            }
        }

        if (exception == null) {
            try {
                dis.close();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "dis.close failed: ";
            }
        }


        if (exception == null) {
            importProjectName = projectName + " (Import)";

            importFile = new File(importFileName);
            try {
                importProject = rUser.importProject(new FileInputStream(importFile), importDescr);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.importProject failed: ";
            }
            try {
                listImportProjectExecution = importProject.getHistory();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "importProject.getHistory failed: ";
            }
        }

        // Test cleanup.
        try {
            if (importProject != null) {
                importProject.close();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "importProject.close failed: ";
        }

        try {
            if (importProject != null) {
                importProject.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "importProject.delete failed: ";
        }

        // Test asserts.
        if (exception == null) {
            assertEquals(importProjectName, importProject.about().name);
            assertEquals(importDescr, importProject.about().descr);
            assertEquals(listProjectExecution.size(), listImportProjectExecution.size());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }
}
