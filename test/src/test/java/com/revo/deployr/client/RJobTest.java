/*
 * RJobTest.java
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
import com.revo.deployr.client.about.RJobDetails;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.factory.RClientFactory;
import org.junit.*;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

public class RJobTest {

    RClient rClient = null;
    RUser rUser = null;

    public RJobTest() {
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
     * Test of query method, of class RJob.
     */
    @Test
    public void testJobQuery() {

        // Test variables.
        String jobName = DeployrUtil.getUniqueJobName();
        String jobDescr = "testQuery-junit test job";
        String code = "x<-5";
        String actualProjectName = null;
        String expProjectName = jobName + " (Job)";
        RJob job = null;
        RJobDetails jobDetails = null;
        String projId = null;
        RProject project = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            job = rUser.submitJobCode(jobName, jobDescr, code);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.submitJobCode failed: ";
        }

        if (exception == null) {
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
                    jobDetails = job.query();
                } catch (Exception ex) {
                    exception = ex;
                    exceptionMsg = "job.query failed: ";
                    break;
                }

                if (jobDetails.status.equalsIgnoreCase(RJob.COMPLETED)) {
                    break;
                }
            }
        }

        // test for created project
        if (job != null) {
            try {
                jobDetails = job.query();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "job.query failed: ";
            }
            projId = jobDetails.project;
            if (projId != null) {
                try {
                    // get project
                    project = rUser.getProject(projId);
                    actualProjectName = project.about().name;
                } catch (Exception ex) {
                    exception = ex;
                    exceptionMsg = "rUser.getProject failed: ";
                }
            }
        }

        // Test cleanup.
        if (job != null) {
            try {
                job.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "job.delete failed: ";
            }
        }
        if (project != null) {
            try {
                project.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "project.delete failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertNotNull(jobDetails);
            assertEquals(RJob.COMPLETED, jobDetails.status);
            assertNotNull(actualProjectName);
            assertEquals(expProjectName, actualProjectName);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of cancel method, of class RJob.
     */
    @Test
    public void testJobCancelImmediate() {

        // Test variables.
        String jobName = DeployrUtil.getUniqueJobName();
        String jobDescr = "testCancel-junit test job";
        String code = "Sys.sleep(30)";
        RJob job = null;
        String projId = null;
        RProject project = null;
        RJobDetails jobDetails = null;
        RJobDetails cancelJobDetails = null;
        HashSet<String> expectedEndStates = new HashSet(Arrays.asList(RJob.INTERRUPTED, RJob.CANCELLED, RJob.COMPLETED));

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            job = rUser.submitJobCode(jobName, jobDescr, code);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.submitJobCode failed: ";
        }

        if (exception == null) {
            try {
                cancelJobDetails = job.cancel();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "job.cancel failed: ";
            }
        }

        if (exception == null) {
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
                    jobDetails = job.query();
                } catch (Exception ex) {
                    exception = ex;
                    exceptionMsg = "job.query failed: ";
                    break;
                }
                if (expectedEndStates.contains(jobDetails.status)) {
                    break;
                }
            }
        }

        // test for created project
        if (job != null) {
            try {
                jobDetails = job.query();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "job.query failed: ";
            }
            projId = jobDetails.project;
            if (projId != null) {
                try {
                    // get project
                    project = rUser.getProject(projId);
                } catch (Exception ex) {
                    exception = ex;
                    exceptionMsg = "rUser.getProject failed: ";
                }
            }
        }

        // Test cleanup.
        if (job != null) {
            // Cancel to ensure job can be deleted.
            try {
                job.cancel();
            } catch (Exception cex) {
            }
            try {
                job.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "job.delete failed: ";
            }
        }
        if (project != null) {
            try {
                project.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "project.delete failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertNotNull(jobDetails);
            assert (expectedEndStates.contains(jobDetails.status));
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of cancel method, of class RJob.
     */
    @Test
    public void testJobCancelRunning() {

        // Test variables.
        String jobName = DeployrUtil.getUniqueJobName();
        String jobDescr = "testCancel-junit test job";
        String code = "Sys.sleep(60)";
        RJob job = null;
        String projId = null;
        RProject project = null;
        RJobDetails jobDetails = null;
        RJobDetails cancelJobDetails = null;
        HashSet<String> expectedEndStates = new HashSet(Arrays.asList(RJob.INTERRUPTED, RJob.COMPLETED));

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            job = rUser.submitJobCode(jobName, jobDescr, code);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.submitJobCode failed: ";
        }

        // make sure job is running before cancelling
        if (exception == null) {
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
                    jobDetails = job.query();
                } catch (Exception ex) {
                    exception = ex;
                    exceptionMsg = "job.query failed: ";
                    break;
                }
                if (jobDetails.status.equalsIgnoreCase(RJob.RUNNING) || jobDetails.status.equalsIgnoreCase(RJob.COMPLETED)) {
                    break;
                }
            }
        }

        if (exception == null) {
            try {
                cancelJobDetails = job.cancel();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "job.cancel failed: ";
            }
        }

        if (exception == null) {
            if (!jobDetails.status.equalsIgnoreCase(RJob.COMPLETED)) {
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
                        jobDetails = job.query();
                    } catch (Exception ex) {
                        exception = ex;
                        exceptionMsg = "job.query failed: ";
                        break;
                    }
                    if (expectedEndStates.contains(jobDetails.status)) {
                        break;
                    }
                }
            }
        }

        // test for created project
        if (job != null) {
            try {
                jobDetails = job.query();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "job.query failed: ";
            }
            projId = jobDetails.project;
            if (projId != null) {
                try {
                    // get project
                    project = rUser.getProject(projId);
                } catch (Exception ex) {
                    exception = ex;
                    exceptionMsg = "rUser.getProject failed: ";
                }
            }
        }

        // Test cleanup.
        if (job != null) {
            // Cancel to ensure job can be deleted.
            try {
                job.cancel();
            } catch (Exception cex) {
            }
            try {
                job.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "job.delete failed: ";
            }
        }
        if (project != null) {
            try {
                project.delete();
            } catch (Exception ex) {
                cleanupException = ex;
                cleanupExceptionMsg = "project.delete failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertNotNull(jobDetails);
            assert (expectedEndStates.contains(jobDetails.status));
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }
}
