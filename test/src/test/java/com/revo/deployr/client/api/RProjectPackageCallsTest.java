/*
 * RProjectPackageCallsTest.java
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
import com.revo.deployr.client.RProject;
import com.revo.deployr.client.RProjectPackage;
import com.revo.deployr.client.RUser;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.factory.RClientFactory;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RProjectPackageCallsTest {

    RClient rClient = null;
    RUser rUser = null;
    RProject rProject = null;

    public RProjectPackageCallsTest() {
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
     * Test of listPackages method, of class RProjectPackageCalls.
     */
    @Test
    public void testListPackages() {

        // Test variables.
        boolean installed = false;
        String expResult = "ok";
        List<RProjectPackage> listPackages = null;
        boolean passed = true;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {
            listPackages = rProject.listPackages(installed);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.listPackages failed: ";
        }

        if (exception == null) {
            for (RProjectPackage details : listPackages) {
                try {
                    if (!details.about().status.equalsIgnoreCase(expResult)) {
                        passed = false;
                        break;
                    }
                } catch (Exception ex) {
                    exception = ex;
                    exceptionMsg = "details.about failed: ";
                    break;
                }
            }
        }

        // Test assertions.
        if (exception == null) {
            assertTrue(passed);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of attachPackage method, of class RProjectPackageCalls.
     */
    @Test
    public void testAttachPackageAsString() {

        // Test variables.
        String packageName = "ada";
        String repo = "http://cran.us.r-project.org";
        String expStatus = "success";
        String actualStatus = "";
        List<RProjectPackage> listPackages = null;
        List<RProjectPackage> detachList = null;
        RProjectPackage details = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {
            listPackages = rProject.attachPackage(packageName, repo);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.attachPackage failed: ";
        }

        if (exception == null) {
            details = listPackages.get(0);
            try {
                actualStatus = details.about().status;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "details.about failed: ";
            }
        }

        // Test assertions.
        if (exception == null) {
            assertEquals(expStatus, actualStatus);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of attachPackage method, of class RProjectPackageCalls.
     */
    @Test
    public void testAttachPackageAsList() {

        // Test variables
        List<String> packageNames = null;
        List<RProjectPackage> listPackages = null;
        RProjectPackage details = null;
        String expStatus = "success";
        String actualStatus = "";
        String repo = "http://cran.us.r-project.org";
        String expResult = "success";
        String packageName = "ada";

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        packageNames = new ArrayList();
        packageNames.add(packageName);

        try {
            listPackages = rProject.attachPackage(packageNames, repo);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.attachPackage failed: ";
        }

        if (exception == null) {
            details = listPackages.get(0);
            try {
                actualStatus = details.about().status;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "details.about failed: ";
            }
        }

        // Test assertions.
        if (exception == null) {
            assertEquals(expStatus, actualStatus);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of detachPackage method, of class RProjectPackageCalls.
     */
    @Test
    public void testDetachPackageString() {

        // Test variables.
        String packageName = "ada";
        String repo = "http://cran.us.r-project.org";
        String expStatus = "success";
        String attachDetailStatus = "";
        String detachDetailStatus = "";
        List<RProjectPackage> listPackages = null;
        List<RProjectPackage> detachList = null;
        RProjectPackage attachDetails = null;
        RProjectPackage detachDetails = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {
            listPackages = rProject.attachPackage(packageName, repo);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.attachPackage failed: ";
        }

        if (exception == null) {
            try {
                detachList = rProject.detachPackage(packageName);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.detachPackage failed: ";
            }
        }

        if (exception == null) {
            attachDetails = listPackages.get(0);
            detachDetails = detachList.get(0);
            try {
                attachDetailStatus = attachDetails.about().status;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "attachDetails.about failed: ";
            }

            if (exception == null) {
                try {
                    detachDetailStatus = detachDetails.about().status;
                } catch (Exception ex) {
                    exception = ex;
                    exceptionMsg = "detachDetails.about failed: ";
                }
            }
        }

        // Test asserts.
        if (exception == null) {
            assertEquals(expStatus, attachDetailStatus);
            assertEquals(expStatus, detachDetailStatus);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of detachPackage method, of class RProjectPackageCalls.
     */
    @Test
    public void testDetachPackageList() {

        // Test variables.
        String packageName = "ada";
        String repo = "http://cran.us.r-project.org";
        List<String> packageNames = null;
        String expStatus = "success";
        String attachDetailStatus = "";
        String detachDetailStatus = "";
        List<RProjectPackage> listPackages = null;
        List<RProjectPackage> detachList = null;
        RProjectPackage attachDetails = null;
        RProjectPackage detachDetails = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        packageNames = new ArrayList();
        packageNames.add(packageName);

        try {
            listPackages = rProject.attachPackage(packageNames, repo);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.attachPackage failed: ";
        }

        if (exception == null) {
            try {
                detachList = rProject.detachPackage(packageNames);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.detachPackage failed: ";
            }
        }

        if (exception == null) {
            attachDetails = listPackages.get(0);
            detachDetails = detachList.get(0);
            try {
                attachDetailStatus = attachDetails.about().status;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "attachDetails.about failed: ";
            }

            if (exception == null) {
                try {
                    detachDetailStatus = detachDetails.about().status;
                } catch (Exception ex) {
                    exception = ex;
                    exceptionMsg = "detachDetails.about failed: ";
                }
            }
        }

        // Test asserts.
        if (exception == null) {
            assertEquals(expStatus, attachDetailStatus);
            assertEquals(expStatus, detachDetailStatus);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }
}
