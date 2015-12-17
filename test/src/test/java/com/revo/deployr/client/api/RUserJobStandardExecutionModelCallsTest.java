/*
 * RUserJobStandardExecutionModelCallsTest.java
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
import com.revo.deployr.client.about.RJobDetails;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.factory.RClientFactory;
import com.revo.deployr.client.params.JobExecutionOptions;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.*;

public class RUserJobStandardExecutionModelCallsTest {

    RClient rClient = null;
    RUser rUser = null;
    RProject rProject = null;
    List<RRepositoryFile> rProjectPreloadArtifacts = new ArrayList<RRepositoryFile>();
    JobExecutionOptions jobExecutionOptions = null;
    RJobDetails jobDetails = null;
    String url;

    // Test Data Depth (TDD)
    // The higher the number set for TDD the greater the complexity,
    // call count and data movement when executing the test.
    int TESTDATADEPTH = Integer.getInteger("test.data.depth", 1);
    // DEPTH * (.txt + .rData) + Workspace.rData
    int TESTREPOARTIFACTSIZE = TESTDATADEPTH * 2 + 1;

    int EXECUTIONRESULTSIZE = 11;
    int EXECUTIONARTIFACTSIZE = 1;

    public RUserJobStandardExecutionModelCallsTest() {
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
     * Test of RUser.submitJobCode.
     */
    @Test
    public void testRUserSubmitJobCode() {

        // Test variables.
        RJob rJob = null;
        RProject projectOnJob = null;
        String jobName = "testRUserSubmitJobCode";
        String jobDescr = "testRUserSubmitJobCode description";

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
        jobExecutionOptions = DeployrUtil.createJobExecutionOptions(rProject,
                JobExecutionOptions.LOW_PRIORITY, false, TESTDATADEPTH);

        // Test.
        try {

            rJob = rUser.submitJobCode(jobName, jobDescr, DeployrUtil.SAMPLE_CODE, jobExecutionOptions);

            // wait for job to complete
            int t = 240;  // 8 minutes
            while (t-- != 0) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    exception = ex;
                    exceptionMsg = "Thread.sleep failed: ";
                    break;
                }
                try {
                    jobDetails = rJob.query();
                } catch (Exception ex) {
                    exception = ex;
                    exceptionMsg = "job.query failed: ";
                    break;
                }

                if (jobDetails.status.equalsIgnoreCase(RJob.COMPLETED)) {
                    break;
                }
            }

            if (jobDetails == null || jobDetails.project == null) {
                if (jobDetails == null)
                    exception = new Exception("job.query failed to return jobDetails.");
                else if (jobDetails.project == null)
                    exception = new Exception("no project found on jobDetails, status=" + jobDetails.status);
                exceptionMsg = "Job failed: ";
            }

            if (exception == null) {
                try {
                    projectOnJob = rUser.getProject(jobDetails.project);
                } catch (Exception ex) {
                    exception = ex;
                    exceptionMsg = "rUser.getProject(jobDetails.project) failed: ";
                }
            }

        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.submitJobCode failed: ";
        }

        // Test cleanup.
        try {
            DeployrUtil.releaseRepositoryArtifacts(rProjectPreloadArtifacts);
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "releaseRepositoryArtifacts.rProjectPreloadArtifacts failed: ";
        }
        if (projectOnJob != null) {
            try {
                projectOnJob.close();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "projectOnJob.close failed: ";
            }
            try {
                projectOnJob.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "projectOnJob.delete failed: ";
            }
        }
        if (rJob != null) {
            try {
                rJob.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rJob.delete failed: ";
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
            assertNotNull(jobDetails);
            assertEquals(RJob.COMPLETED, jobDetails.status);
            assertThat(jobDetails.timeStart, is(not(0L)));
            assertThat(jobDetails.timeCode, is(not(0L)));
            assertThat(jobDetails.timeTotal, is(not(0L)));
            assertNotNull(jobDetails.project);
            assertNotNull(projectOnJob);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }

    }

    /**
     * Test of RUser.submitJobScript.
     */
    @Test
    public void testRUserSubmitJobScript() {

        // Test variables.
        RJob rJob = null;
        RProject projectOnJob = null;
        RRepositoryFile repositoryFile = null;
        String jobName = "testRUserSubmitJobScript";
        String jobDescr = "testRUserSubmitJobScript description";

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
        jobExecutionOptions = DeployrUtil.createJobExecutionOptions(rProject,
                JobExecutionOptions.LOW_PRIORITY, false, TESTDATADEPTH);

        // Test.
        try {

            repositoryFile = DeployrUtil.createTemporaryRScript(rUser, false);
            rJob = rUser.submitJobScript(jobName, jobDescr,
                    repositoryFile.about().filename,
                    repositoryFile.about().directory,
                    repositoryFile.about().author,
                    null,
                    jobExecutionOptions);

            // wait for job to complete
            int t = 240;  // 8 minutes
            while (t-- != 0) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    exception = ex;
                    exceptionMsg = "Thread.sleep failed: ";
                    break;
                }
                try {
                    jobDetails = rJob.query();
                } catch (Exception ex) {
                    exception = ex;
                    exceptionMsg = "job.query failed: ";
                    break;
                }

                if (jobDetails.status.equalsIgnoreCase(RJob.COMPLETED)) {
                    break;
                }
            }

            if (jobDetails == null || jobDetails.project == null) {
                if (jobDetails == null)
                    exception = new Exception("job.query failed to return jobDetails.");
                else if (jobDetails.project == null)
                    exception = new Exception("no project found on jobDetails, status=" + jobDetails.status);
                exceptionMsg = "Job failed: ";
            }

            if (exception == null) {
                try {
                    projectOnJob = rUser.getProject(jobDetails.project);
                } catch (Exception ex) {
                    exception = ex;
                    exceptionMsg = "rUser.getProject(jobDetails.project) failed: ";
                }
            }

        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.submitJobCode failed: ";
        }

        // Test cleanup.
        try {
            DeployrUtil.releaseRepositoryArtifacts(rProjectPreloadArtifacts);
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "releaseRepositoryArtifacts.rProjectPreloadArtifacts failed: ";
        }
        if (repositoryFile != null) {
            try {
                repositoryFile.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "repositoryFile.delete failed: ";
            }
        }
        if (projectOnJob != null) {
            try {
                projectOnJob.close();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "projectOnJob.close failed: ";
            }
            try {
                projectOnJob.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "projectOnJob.delete failed: ";
            }
        }
        if (rJob != null) {
            try {
                rJob.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rJob.delete failed: ";
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
            assertNotNull(jobDetails);
            assertEquals(RJob.COMPLETED, jobDetails.status);
            assertThat(jobDetails.timeStart, is(not(0L)));
            assertThat(jobDetails.timeCode, is(not(0L)));
            assertThat(jobDetails.timeTotal, is(not(0L)));
            assertNotNull(jobDetails.project);
            assertNotNull(projectOnJob);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }

    }

    /**
     * Test of RUser.submitJobExternal.
     */
    @Test
    public void testRUserSubmitJobExternal() {

        // Test variables.
        RJob rJob = null;
        RProject projectOnJob = null;
        RRepositoryFile repositoryFile = null;
        String jobName = "testRUserSubmitJobExternal";
        String jobDescr = "testRUserSubmitJobExternal description";

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
        jobExecutionOptions = DeployrUtil.createJobExecutionOptions(rProject,
                JobExecutionOptions.LOW_PRIORITY, false, TESTDATADEPTH);

        // Test.
        try {

            repositoryFile = DeployrUtil.createTemporaryRScript(rUser, true);
            rJob = rUser.submitJobExternal(jobName, jobDescr,
                    repositoryFile.about().url.toString(),
                    jobExecutionOptions);

            // wait for job to complete
            int t = 240;  // 8 minutes
            while (t-- != 0) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    exception = ex;
                    exceptionMsg = "Thread.sleep failed: ";
                    break;
                }
                try {
                    jobDetails = rJob.query();
                } catch (Exception ex) {
                    exception = ex;
                    exceptionMsg = "job.query failed: ";
                    break;
                }

                if (jobDetails.status.equalsIgnoreCase(RJob.COMPLETED)) {
                    break;
                }
            }

            if (jobDetails == null || jobDetails.project == null) {
                if (jobDetails == null)
                    exception = new Exception("job.query failed to return jobDetails.");
                else if (jobDetails.project == null)
                    exception = new Exception("no project found on jobDetails, status=" + jobDetails.status);
                exceptionMsg = "Job failed: ";
            }

            if (exception == null) {
                try {
                    projectOnJob = rUser.getProject(jobDetails.project);
                } catch (Exception ex) {
                    exception = ex;
                    exceptionMsg = "rUser.getProject(jobDetails.project) failed: ";
                }
            }

        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.submitJobCode failed: ";
        }

        // Test cleanup.
        try {
            DeployrUtil.releaseRepositoryArtifacts(rProjectPreloadArtifacts);
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "releaseRepositoryArtifacts.rProjectPreloadArtifacts failed: ";
        }
        if (repositoryFile != null) {
            try {
                repositoryFile.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "repositoryFile.delete failed: ";
            }
        }
        if (projectOnJob != null) {
            try {
                projectOnJob.close();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "projectOnJob.close failed: ";
            }
            try {
                projectOnJob.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "projectOnJob.delete failed: ";
            }
        }
        if (rJob != null) {
            try {
                rJob.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rJob.delete failed: ";
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
            assertNotNull(jobDetails);
            assertEquals(RJob.COMPLETED, jobDetails.status);
            assertThat(jobDetails.timeStart, is(not(0L)));
            assertThat(jobDetails.timeCode, is(not(0L)));
            assertThat(jobDetails.timeTotal, is(not(0L)));
            assertNotNull(jobDetails.project);
            assertNotNull(projectOnJob);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }

    }

    /**
     * Test of RUser.submitJobStoreNoProject.
     */
    @Test
    public void testRUserSubmitJobStoreNoProject() {

        // Test variables.
        RJob rJob = null;
        RRepositoryFile repositoryFile = null;
        String jobName = "testRUserSubmitJobStoreNoProject";
        String jobDescr = "testRUserSubmitJobStoreNoProject description";

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
        jobExecutionOptions = DeployrUtil.createJobExecutionOptions(rProject,
                JobExecutionOptions.LOW_PRIORITY, true, TESTDATADEPTH);

        // Test.
        try {

            repositoryFile = DeployrUtil.createTemporaryRScript(rUser, true);
            rJob = rUser.submitJobExternal(jobName, jobDescr,
                    repositoryFile.about().url.toString(),
                    jobExecutionOptions);

            // wait for job to complete
            int t = 240;  // 8 minutes
            while (t-- != 0) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    exception = ex;
                    exceptionMsg = "Thread.sleep failed: ";
                    break;
                }
                try {
                    jobDetails = rJob.query();
                } catch (Exception ex) {
                    exception = ex;
                    exceptionMsg = "job.query failed: ";
                    break;
                }

                if (jobDetails.status.equalsIgnoreCase(RJob.COMPLETED)) {
                    break;
                }
            }

            if (jobDetails == null) {
                exception = new Exception("job.query failed to return jobDetails.");
                exceptionMsg = "Job failed: ";
            }

        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.submitJobCode failed: ";
        }

        // Test cleanup.
        try {
            DeployrUtil.releaseRepositoryArtifacts(rProjectPreloadArtifacts);
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "releaseRepositoryArtifacts.rProjectPreloadArtifacts failed: ";
        }
        if (repositoryFile != null) {
            try {
                repositoryFile.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "repositoryFile.delete failed: ";
            }
        }
        if (rJob != null) {
            try {
                rJob.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "rJob.delete failed: ";
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
            assertNotNull(jobDetails);
            assertEquals(RJob.COMPLETED, jobDetails.status);
            assertThat(jobDetails.timeStart, is(not(0L)));
            assertThat(jobDetails.timeCode, is(not(0L)));
            assertThat(jobDetails.timeTotal, is(not(0L)));
            assertNull(jobDetails.project);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }

    }

}
