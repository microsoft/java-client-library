/*
 * RClientStandardExecutionModelCallsTest.java
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
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.data.RData;
import com.revo.deployr.client.factory.RClientFactory;
import com.revo.deployr.client.params.AnonymousProjectExecutionOptions;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

public class RClientStandardExecutionModelCallsTest {

    RClient anonymousRClient = null;
    RClient authenticatedRClient = null;
    RUser rUser = null;
    RProject rProject = null;
    List<RRepositoryFile> rProjectPreloadArtifacts = new ArrayList<RRepositoryFile>();
    RRepositoryFile repositoryFile = null;
    AnonymousProjectExecutionOptions executionOptions = null;
    String url;

    // Test Data Depth (TDD)
    // The higher the number set for TDD the greater the complexity,
    // call count and data movement when executing the test.
    int TESTDATADEPTH = Integer.getInteger("test.data.depth", 1);
    // DEPTH * (.txt + .rData) + Workspace.rData
    int TESTREPOARTIFACTSIZE = TESTDATADEPTH * 2 + 1;

    int EXECUTIONRESULTSIZE = 11;
    int EXECUTIONARTIFACTSIZE = 1;
    int HTTPBLACKBOXEXECUTIONARTIFACTSIZE = 0;

    public RClientStandardExecutionModelCallsTest() {
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
            anonymousRClient =RClientFactory.createClient(url, allowSelfSigned);
            authenticatedRClient =RClientFactory.createClient(url, allowSelfSigned);
            RBasicAuthentication rAuthentication = new RBasicAuthentication("testuser", System.getProperty("password.testuser"));
            rUser = authenticatedRClient.login(rAuthentication);

            // Test prep.
            Map<String, Object> map = DeployrUtil.createExecutionOptionsProject(rUser, TESTDATADEPTH);
            rProject = (RProject) map.get("rProject");
            rProjectPreloadArtifacts = (List<RRepositoryFile>) map.get("rProjectPreloadArtifacts");
            assertNotNull(rProject);
            executionOptions = DeployrUtil.createExecutionOptions(rProject, TESTDATADEPTH);

            repositoryFile = DeployrUtil.createTemporaryRScript(rUser, true);

        } catch (Exception ex) {
            if (repositoryFile != null) {
                try {
                    repositoryFile.delete();
                } catch (Exception fex) {
                }
            }
            if (rProject != null) {
                try {
                    rProject.delete();
                } catch (Exception pex) {
                }
            }
            if (anonymousRClient != null) {
                anonymousRClient.release();
            }
            if (authenticatedRClient != null) {
                authenticatedRClient.release();
            }
            fail("setUp: " + ex);
        }
    }

    @After
    public void tearDown() {

        try {
            if (repositoryFile != null) {
                try {
                    repositoryFile.delete();
                } catch (Exception fex) {
                }
            }
            if (anonymousRClient != null) {
                try {
                    anonymousRClient.release();
                } catch (Exception ex) {
                }
            }

        } catch (Exception ex) {
        } finally {
            if (authenticatedRClient != null) {
                try {
                    authenticatedRClient.release();
                } catch (Exception cex) {
                }
            }
        }

    }

    /**
     * Test RClient.executeScript as anonymous user.
     */
    @Test
    public void testRClientExecuteScriptAnonymous() {

        // Test variables.
        RScriptExecution scriptExecution = null;
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

        // Test.
        try {
            // Ensure execution occurs on HTTP Blackbox Project so anonymous user
            // can load public repository files on pre-execution.
            executionOptions.blackbox = true;
            scriptExecution = anonymousRClient.executeScript(repositoryFile.about().filename,
                    repositoryFile.about().directory,
                    repositoryFile.about().author,
                    null, executionOptions);
            console = scriptExecution.about().console;
            routputs = scriptExecution.about().workspaceObjects;
            execResults = scriptExecution.about().results;
            execArtifacts = scriptExecution.about().artifacts;
            repoArtifacts = scriptExecution.about().repositoryFiles;

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
        if (rProject != null) {
            try {
                rProject.delete();
                rProject = null;
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.delete failed: ";
            }
        }
        if (repositoryFile != null) {
            try {
                repositoryFile.delete();
                repositoryFile = null;
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "repositoryFile.delete failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertNotNull(scriptExecution);
            assertNotNull(console);
            assertNotNull(routputs);
            assertEquals(TESTDATADEPTH, routputs.size());
            assertNotNull(execResults);
            assertEquals(EXECUTIONRESULTSIZE, execResults.size());
            assertNotNull(execArtifacts);
            assertEquals(HTTPBLACKBOXEXECUTIONARTIFACTSIZE, execArtifacts.size());
            // ExecutionOptions.storageOptions are not supported
            // on an anonymous execution so test expects
            // zero repository artifacts.
            assertNotNull(repoArtifacts);
            assertEquals(0, repoArtifacts.size());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test RClient.executeScript as authenticated user.
     */
    @Test
    public void testRClientExecuteScriptAuthenticated() {

        // Test variables.
        RScriptExecution scriptExecution = null;
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
        //

        // Test.
        try {
            scriptExecution = authenticatedRClient.executeScript(repositoryFile.about().filename,
                    repositoryFile.about().directory,
                    repositoryFile.about().author,
                    null, executionOptions);
            console = scriptExecution.about().console;
            routputs = scriptExecution.about().workspaceObjects;
            execResults = scriptExecution.about().results;
            execArtifacts = scriptExecution.about().artifacts;
            repoArtifacts = scriptExecution.about().repositoryFiles;

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
        if (repositoryFile != null) {
            try {
                repositoryFile.delete();
                repositoryFile = null;
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "repositoryFile.delete failed: ";
            }
        }
        if (rProject != null) {
            try {
                rProject.delete();
                rProject = null;
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.delete failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertNotNull(scriptExecution);
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

    /**
     * Test RClient.executeScript of external script as authenticated user.
     */
    @Test
    public void testRClientExecuteScriptExternal() {

        // Test variables.
        RScriptExecution scriptExecution = null;
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
        //

        // Test.
        try {
            scriptExecution = authenticatedRClient.executeExternal(repositoryFile.about().url.toString(),
                    executionOptions);
            console = scriptExecution.about().console;
            routputs = scriptExecution.about().workspaceObjects;
            execResults = scriptExecution.about().results;
            execArtifacts = scriptExecution.about().artifacts;
            repoArtifacts = scriptExecution.about().repositoryFiles;

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
        if (repositoryFile != null) {
            try {
                repositoryFile.delete();
                repositoryFile = null;
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "repositoryFile.delete failed: ";
            }
        }
        if (rProject != null) {
            try {
                rProject.delete();
                rProject = null;
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rProject.delete failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertNotNull(scriptExecution);
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
