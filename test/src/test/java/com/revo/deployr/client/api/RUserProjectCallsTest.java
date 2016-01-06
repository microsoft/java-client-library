/*
 * RUserProjectCallsTest.java
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
import com.revo.deployr.client.*;
import com.revo.deployr.client.about.RProjectDetails;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.data.RData;
import com.revo.deployr.client.factory.RClientFactory;
import com.revo.deployr.client.params.ProjectCreationOptions;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RUserProjectCallsTest {

    RClient rClient = null;
    RUser rUser = null;
    RProject rProject = null;
    RBasicAuthentication rAuthentication = null;
    List<RRepositoryFile> rProjectPreloadArtifacts = new ArrayList<RRepositoryFile>();
    ProjectCreationOptions creationOptions = null;
    // Test Data Depth (TDD)
    // The higher the number set for TDD the greater the complexity,
    // call count and data movement when executing the test.
    int TESTDATADEPTH = Integer.getInteger("test.data.depth", 2);
    // DEPTH * (.txt + .rData) + Workspace.rData
    int TESTREPOARTIFACTSIZE = TESTDATADEPTH * 2 + 1;

    //   int EXECUTIONRESULTSIZE = 11;
    //   int EXECUTIONARTIFACTSIZE = 1;
    public RUserProjectCallsTest() {
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
            rAuthentication = new RBasicAuthentication("testuser", System.getProperty("password.testuser"));
            rUser = rClient.login(rAuthentication);
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
     * Test of autosaveProjects method, of class RUserProjectCalls.
     */
    @Test
    public void testUserProjectAutosaveProjectsFalse() {

        // Test variables.
        String code = "x<-5";
        boolean autoSave = false;
        RProject rProject = null;
        RProject getProject = null;
        RProjectExecution projectExecution = null;
        List<RProjectExecution> listProjectExecution = null;
        List<RProjectExecution> listGetProjectExecution = null;
        String pid = "";

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        try {
            rUser.autosaveProjects(autoSave);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.autosaveProjects failed: ";
        }

        if (exception == null) {
            rProject = DeployrUtil.createPersistentProject(rUser, "test autosave", "test autosave desc");
            pid = rProject.about().id;
            assertNotNull(rProject);
        }

        if (exception == null) {
            try {
                projectExecution = rProject.executeCode(code);
                listProjectExecution = rProject.getHistory();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.executeCode failed: ";
            }
        }

        if (exception == null) {
            try {
                rProject.close();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.close failed: ";
            }
        }

        if (exception == null) {
            try {
                getProject = rUser.getProject(pid);
                listGetProjectExecution = getProject.getHistory();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.getProject failed: ";
            }
        }

        // Test cleanup.
        if (rProject != null) {
            try {
                rProject.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.delete failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertEquals(1, listProjectExecution.size());
            assertEquals(0, listGetProjectExecution.size());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }

    }

    /**
     * Test of autosaveProjects method, of class RUserProjectCalls.
     */
    @Test
    public void testUserProjectAutosaveProjectsTrue() {

        // Test variables.
        String code = "x<-5";
        boolean autoSave = true;
        RProject rProject = null;
        RProject getProject = null;
        RProjectExecution projectExecution = null;
        List<RProjectExecution> listProjectExecution = null;
        List<RProjectExecution> listGetProjectExecution = null;
        String pid = "";
        String consoleExecution = "";
        String getConsoleExecution = "";

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        try {
            rUser.autosaveProjects(autoSave);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.autosaveProjects failed: ";
        }

        if (exception == null) {
            rProject = DeployrUtil.createPersistentProject(rUser, "test autosave", "test autosave desc");
            pid = rProject.about().id;
            assertNotNull(rProject);
        }

        if (exception == null) {
            try {
                projectExecution = rProject.executeCode(code);
                listProjectExecution = rProject.getHistory();
                consoleExecution = listProjectExecution.get(0).about().console;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.executeCode failed: ";
            }
        }

        if (exception == null) {
            try {
                rProject.close();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.close failed: ";
            }
        }

        if (exception == null) {
            try {
                getProject = rUser.getProject(pid);
                listGetProjectExecution = getProject.getHistory();
                getConsoleExecution = listGetProjectExecution.get(0).about().console;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.getProject failed: ";
            }
        }

        // Test cleanup.
        if (rProject != null) {
            try {
                rProject.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.delete failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertEquals(1, listProjectExecution.size());
            assertEquals(1, listGetProjectExecution.size());
            assertEquals(consoleExecution, getConsoleExecution);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }

    }

    /**
     * Test of createProject method, of class RUserProjectCalls.
     */
    @Test
    public void testUserProjectCreateTemporaryProject() {

        // Test variables.
        String projID = "PROJECT";
        RProject rProject = null;
        RProjectDetails projectDetails = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            rProject = rUser.createProject();
            projectDetails = rProject.about();
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createProject failed: ";
        }

        // Test cleanup.
        if (rProject != null) {
            try {
                rProject.close();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.close failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertNotNull(projectDetails);
            assertEquals(projID, projectDetails.id.substring(0, projID.length()));
            assertFalse(projectDetails.shared);
            assertTrue(projectDetails.islive);
            assertNull(projectDetails.name);
            assertNull(projectDetails.descr);
            assertNull(projectDetails.longdescr);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of createProjectPool method, of class RUserProjectCalls.
     */
    @Test
    public void testUserProjectCreateTemporaryProjectPool() {

        // Test variables.
        String projID = "PROJECT";
        RProjectDetails projectDetails = null;
        ProjectCreationOptions creationOptions = null;
        int poolSize = 3;
        List<RProject> listTemporaryProjects = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            listTemporaryProjects = rUser.createProjectPool(poolSize, creationOptions);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createProjectPool failed: ";
        }

        // Test cleanup.
        if (listTemporaryProjects.size() > 0) {
            try {
                for (RProject project : listTemporaryProjects) {
                    project.close();
                }
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.close failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertTrue(listTemporaryProjects.size() > 0);
            for (RProject project : listTemporaryProjects) {
                projectDetails = project.about();
                assertEquals(projID, projectDetails.id.substring(0, projID.length()));
                assertFalse(projectDetails.shared);
                assertTrue(projectDetails.islive);
                assertNull(projectDetails.name);
                assertNull(projectDetails.descr);
                assertNull(projectDetails.longdescr);
            }
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of createProject method, of class RUserProjectCalls.
     */
    @Test
    public void testUserProjectCreatePersistentProject() {

        // Test variables.
        String projID = "PROJECT";
        String name = "create persistent project";
        String descr = "create persistent project descr";
        RProject rProject = null;
        RProjectDetails projectDetails = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            rProject = rUser.createProject(name, descr);
            projectDetails = rProject.about();
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.createProject failed: ";
        }

        // Test cleanup.
        if (rProject != null) {
            try {
                rProject.close();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.close failed: ";
            }
        }

        if (rProject != null) {
            try {
                rProject.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.delete failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertNotNull(projectDetails);
            assertEquals(projID, projectDetails.id.substring(0, projID.length()));
            assertFalse(projectDetails.shared);
            assertTrue(projectDetails.islive);
            assertEquals(name, projectDetails.name);
            assertEquals(descr, projectDetails.descr);
            assertNull(projectDetails.longdescr);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }

    }

    /**
     * Test of createProject method, of class RUserProjectCalls.
     */
    @Test
    public void testCreateProjectAdoptOptionsCreateOptions() {

        // Test variables.
        RProject temporaryProject = null;
        RRepositoryFile repositoryFile = null;
        List<RRepositoryFile> repoArtifacts = null;
        List<RData> routputs = null;
        String console = null;
        List<RProjectFile> listFiles = null;
        RProjectDetails projectDetails = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test prep.
        Map<String, Object> map = DeployrUtil.createCreationOptionsProject(rUser, TESTDATADEPTH);
        rProject = (RProject) map.get("rProject");
        rProjectPreloadArtifacts = (List<RRepositoryFile>) map.get("rProjectPreloadArtifacts");
        assertNotNull(rProject);
        creationOptions = DeployrUtil.createCreationOptions(rProject, TESTDATADEPTH);

        // Test.
        try {
            temporaryProject = rUser.createProject(creationOptions);
            projectDetails = rProject.about();
            routputs = temporaryProject.listObjects();
            listFiles = temporaryProject.listFiles();

            DeployrUtil.verifyRepositoryArtifacts(creationOptions, rProjectPreloadArtifacts);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createProject failed: ";
        }

        // Test cleanup.
        try {
            DeployrUtil.releaseRepositoryArtifacts(rProjectPreloadArtifacts);
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "releaseRepositoryArtifacts.rProjectPreloadArtifacts failed: ";
        }

        if (temporaryProject != null) {
            try {
                temporaryProject.close();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "temporaryProject.close failed: ";
            }
        }

        if (rProject != null) {
            try {
                rProject.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.delete failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertNotNull(routputs);
            assertEquals(TESTDATADEPTH * 3, routputs.size());
            assertNotNull(listFiles);
            assertEquals(TESTDATADEPTH + 2, listFiles.size());
            assertNotNull(projectDetails);
            assertFalse(projectDetails.shared);
            assertTrue(projectDetails.islive);
            assertNull(projectDetails.longdescr);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of createProjectPools method, of class RUserProjectCalls.
     */
    @Test
    public void testCreateProjectPoolAdoptOptionsCreateOptions() {

        // Test variables.
        RRepositoryFile repositoryFile = null;
        RProject rProject = null;
        List<List<RData>> listRoutputs = new ArrayList();
        List<RProjectDetails> listProjectDetails = new ArrayList();
        List<List<RProjectFile>> listFiles = new ArrayList();
        ;
        List<RProject> listTemporaryProjects = null;
        List<String> fileNames = new ArrayList();
        int poolSize = 3;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test prep.
        Map<String, Object> map = DeployrUtil.createCreationOptionsProject(rUser, TESTDATADEPTH);
        rProject = (RProject) map.get("rProject");
        rProjectPreloadArtifacts = (List<RRepositoryFile>) map.get("rProjectPreloadArtifacts");
        assertNotNull(rProject);
        creationOptions = DeployrUtil.createCreationOptions(rProject, TESTDATADEPTH);

        // Test.
        try {
            listTemporaryProjects = rUser.createProjectPool(poolSize, creationOptions);

            for (RProject temporaryProject : listTemporaryProjects) {
                listRoutputs.add(temporaryProject.listObjects());
                listFiles.add(temporaryProject.listFiles());
                listProjectDetails.add(temporaryProject.about());
            }

            DeployrUtil.verifyRepositoryArtifacts(creationOptions, rProjectPreloadArtifacts);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createProject failed: ";
        }

        // Test cleanup.
        try {
            DeployrUtil.releaseRepositoryArtifacts(rProjectPreloadArtifacts);
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "releaseRepositoryArtifacts.rProjectPreloadArtifacts failed: ";
        }

        if (listTemporaryProjects.size() > 0) {
            try {
                for (RProject temporaryProject : listTemporaryProjects) {
                    temporaryProject.close();
                }
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "temporaryProject.close failed: ";
            }
        }

        if (rProject != null) {
            try {
                rProject.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.delete failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            for (List<RProjectFile> list : listFiles) {
                assertNotNull(list);
                assertEquals(TESTDATADEPTH + 2, list.size());
            }
            for (List<RData> routputs : listRoutputs) {
                assertNotNull(routputs);
                assertEquals(TESTDATADEPTH * 3, routputs.size());
            }
            for (RProjectDetails projectDetails : listProjectDetails) {
                assertNotNull(projectDetails);
                assertFalse(projectDetails.shared);
                assertTrue(projectDetails.islive);
                assertNull(projectDetails.longdescr);
            }
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of getProject method, of class RUserProjectCalls.
     */
    @Test
    public void testGetProject() {

        // Test variables.
        String name = "junit-test-project-testGetProject";
        String descr = "junit test project testGetProject";
        RProject rProject = null;
        RProject getProject = null;
        String pid = "";

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            rProject = rUser.createProject(name, descr);
            pid = rProject.about().id;
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createProject failed: ";
        }

        if (exception == null) {
            try {
                rProject.close();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.close failed: ";
            }
        }

        if (exception == null) {
            try {
                getProject = rUser.getProject(pid);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.getProject failed: ";
            }
        }

        // Test cleanup.
        if (rProject != null) {
            try {
                rProject.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.delete failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertNotNull(rProject);
            assertEquals(name, rProject.about().name);
            assertEquals(descr, rProject.about().descr);

            assertNotNull(getProject);
            assertEquals(rProject.about().name, getProject.about().name);
            assertEquals(rProject.about().descr, getProject.about().descr);
            assertEquals(rProject.about().shared, getProject.about().shared);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of listProjects method, of class RUserProjectCalls.
     */
    @Test
    public void testListProjectsNoArgs() {

        // Test variables.
        String name = "junit-test-project-testListProjects_0args";
        String descr = "junit test project testListProjects_0args";
        RProject rProject = null;
        String pid = "";
        List<RProject> listProjects = null;
        boolean projectFound = false;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";
        try {
            rProject = rUser.createProject(name, descr);
            pid = rProject.about().id;
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createProject failed: ";
        }

        if (exception == null) {
            try {
                listProjects = rUser.listProjects();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listProjects failed: ";
            }
        }

        if (exception == null) {
            for (RProject project : listProjects) {
                if (project.about().id.equals(pid)) {
                    projectFound = true;
                }
            }
        }


        // Test cleanup.
        if (rProject != null) {
            try {
                rProject.close();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.close failed: ";
            }
        }

        if (rProject != null) {
            try {
                rProject.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.delete failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertNotNull(rProject);
            assertEquals(name, rProject.about().name);
            assertEquals(descr, rProject.about().descr);
            assertTrue(projectFound);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }

    }

    /**
     * Test of listProjects method, of class RUserProjectCalls.
     */
    @Test
    public void testListProjectsWithArgs() throws Exception {

        // Test variables.
        String name = "junit-test-project-testListProjects_0args";
        String descr = "junit test project testListProjects_0args";
        RProject rProject1 = null;
        RProject rProject2 = null;
        String pid1 = "";
        String pid2 = "";
        List<RProject> listProjects = null;
        boolean sortByLastModified = true;
        boolean showPublicProjects = false;
        RProjectDetails projectDetails = null;
        int poneIndex = -1;
        int ptwoIndex = -1;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        // create 2 projects to test sortByLastModified
        try {
            rProject1 = rUser.createProject(name, descr);
            pid1 = rProject1.about().id;
            // save this project
            projectDetails = rProject1.about();
            projectDetails.longdescr = "long descr one";
            rProject1.save(projectDetails);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createProject one failed: ";
        }

        try {
            Thread.sleep(1000);
            rProject2 = rUser.createProject(name, descr);
            pid2 = rProject2.about().id;
            // save this project
            projectDetails = rProject2.about();
            projectDetails.longdescr = "long descr two";
            rProject2.save(projectDetails);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.createProject two failed: ";
        }

        if (exception == null) {
            try {
                listProjects = rUser.listProjects(sortByLastModified, showPublicProjects);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listProjects failed: ";
            }
        }

        // loop thru project list. pid2 should precede pid1
        if (exception == null) {
            for (int i = 0; i < listProjects.size(); i++) {
                RProject project = listProjects.get(i);
                if (project.about().id.equals(pid1)) {
                    poneIndex = i;
                } else if (project.about().id.equals(pid2)) {
                    ptwoIndex = i;
                }
            }
        }

        // Test cleanup.
        if (rProject1 != null) {
            try {
                rProject1.close();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.close failed: ";
            }
        }

        if (rProject1 != null) {
            try {
                rProject1.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.delete failed: ";
            }
        }

        if (rProject2 != null) {
            try {
                rProject2.close();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.close failed: ";
            }
        }

        if (rProject2 != null) {
            try {
                rProject2.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.delete failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            // Assert project one was found.
            assert (poneIndex != -1);
            // Assert project two was found.
            assert (ptwoIndex != -1);
            // Assert project two was found ahead of project one in the list.
            assert (ptwoIndex < poneIndex);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }
}
