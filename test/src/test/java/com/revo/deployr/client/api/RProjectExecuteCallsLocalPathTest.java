/*
 * RProjectExecuteCallsLocalPathTest.java
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
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.data.RData;
import com.revo.deployr.client.factory.RClientFactory;
import com.revo.deployr.client.params.ProjectExecutionOptions;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RProjectExecuteCallsLocalPathTest {

    RClient rClient = null;
    RUser rUser = null;
    RProject rProject = null;
    List<RRepositoryFile> rProjectPreloadArtifacts = new ArrayList<RRepositoryFile>();
    ProjectExecutionOptions executionOptions = null;
    String url;

    // Test Data Depth (TDD)
    // The higher the number set for TDD the greater the complexity,
    // call count and data movement when executing the test.
    int TESTDATADEPTH = Integer.getInteger("test.data.depth", 1);
    // DEPTH * (.txt + .rData) + Workspace.rData
    int TESTREPOARTIFACTSIZE = TESTDATADEPTH * 2 + 1;

    int EXECUTIONRESULTSIZE = 11;
    int EXECUTIONARTIFACTSIZE = 1;

    public RProjectExecuteCallsLocalPathTest() {
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
     * Test of RProject.executeExternal.
     */
    @Test
    public void testRProjectExecuteExternalPath() {

        // Test variables.
        String scriptPath = "";
        RProject temporaryProject = null;
        RProjectExecution projectExecution = null;
        List<RProjectResult> execResults = null;
        List<RProjectFile> execArtifacts = null;
        List<RRepositoryFile> repoArtifacts = null;
        List<RData> routputs = null;
        String console = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test prep.
        Map<String, Object> map = DeployrUtil.createExecutionOptionsProject(rUser, TESTDATADEPTH);
        rProject = (RProject) map.get("rProject");
        rProjectPreloadArtifacts = (List<RRepositoryFile>) map.get("rProjectPreloadArtifacts");
        assertNotNull(rProject);
        executionOptions = DeployrUtil.createExecutionOptions(rProject, TESTDATADEPTH);

        // Test.
        try {
            temporaryProject = rUser.createProject();
            scriptPath = DeployrUtil.createTemporaryRScriptOnDisk(temporaryProject);
            projectExecution = temporaryProject.executeExternal(scriptPath, executionOptions);
            console = projectExecution.about().console;
            routputs = projectExecution.about().workspaceObjects;
            execResults = projectExecution.about().results;
            execArtifacts = projectExecution.about().artifacts;
            repoArtifacts = projectExecution.about().repositoryFiles;

            DeployrUtil.verifyRepositoryArtifacts(executionOptions.storageOptions,
                    repoArtifacts);

        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.executeScript failed: ";
        }

        // Test cleanup.
        try {
            DeployrUtil.releaseRepositoryArtifacts(rProjectPreloadArtifacts);
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "releaseRepositoryArtifacts.rProjectPreloadArtifacts failed: ";
        }
        try {
            DeployrUtil.releaseRepositoryArtifacts(repoArtifacts);
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "releaseRepositoryArtifacts.repoArtifacts failed: ";
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
            assertNotNull(projectExecution);
            assertNotNull(console);
            assertNotNull(routputs);
            assertEquals(TESTDATADEPTH, routputs.size());
            assertNotNull(execResults);
            assertEquals(EXECUTIONRESULTSIZE, execResults.size());
            assertNotNull(execArtifacts);
            assertEquals(EXECUTIONARTIFACTSIZE, execArtifacts.size());
            assertNotNull(repoArtifacts);
            assertEquals(TESTREPOARTIFACTSIZE, repoArtifacts.size());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }

    }

}
