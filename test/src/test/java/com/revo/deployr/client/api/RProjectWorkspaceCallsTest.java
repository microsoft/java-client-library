/*
 * RProjectWorkspaceCallsTest.java
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
package com.revo.deployr.client.api;

import com.revo.deployr.DeployrUtil;
import com.revo.deployr.client.*;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.data.RData;
import com.revo.deployr.client.data.RNumeric;
import com.revo.deployr.client.data.RString;
import com.revo.deployr.client.factory.RClientFactory;
import com.revo.deployr.client.factory.RDataFactory;
import com.revo.deployr.client.params.ProjectWorkspaceOptions;
import org.junit.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author nriesland
 */
public class RProjectWorkspaceCallsTest {

    RClient rClient = null;
    RUser rUser = null;
    RProject rProject = null;

    public RProjectWorkspaceCallsTest() {
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

            // create Temp project with x and y numerics
            rProject = DeployrUtil.createTemporaryProject(rUser);
            assert (rProject != null);
            rProject.executeCode("x<-10;y<-20");
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
     * Test of listObjects method, of class RProjectWorkspaceCalls.
     */
    @Test
    public void testProjectWorkspaceListObjects() {

        // Test variables.
        int rObjectSize = 2;
        RProjectExecution projectExecution = null;
        List<RData> listObjects = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertTrue(rObjectSize == listObjects.size());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of listObjects method, of class RProjectWorkspaceCalls.
     */
    @Test
    public void testProjectWorkspaceListObjects_ProjectWorkspaceOptions() {

        // Test variables.
        int mtcarsSize = 11;
        String code = "data(mtcars)";
        String expType = "vector";
        String expRClass = "numeric";
        String expName = "mtcars";
        RProjectExecution projectExecution = null;
        ProjectWorkspaceOptions options = null;
        List<RData> listRData = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {
            projectExecution = rProject.executeCode(code);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.executeCode failed: ";
        }

        if (exception == null) {
            options = new ProjectWorkspaceOptions();
            options.alternateRoot = "mtcars";

            try {
                listRData = rProject.listObjects(options);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        // Test assertions.
        if (exception == null) {
            assertTrue(listRData.size() == mtcarsSize);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

    }

    /**
     * Test of getObject method, of class RProjectWorkspaceCalls.
     */
    @Test
    public void testProjectWorkspaceGetObjectString() {

        // Test variables.
        String expName = "x";
        double delta = .1;
        double expValue = 10.0;
        String expType = "primitive";
        String expRClass = "numeric";
        RNumeric actualNumeric = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {
            actualNumeric = (RNumeric) rProject.getObject(expName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.getObject failed: ";
        }

        // Test asserts.
        if (exception == null) {
            assertEquals(expName, actualNumeric.getName());
            assertEquals(expType, actualNumeric.getType());
            assertEquals(expRClass, actualNumeric.getRclass());
            assertEquals(expValue, actualNumeric.getValue(), delta);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of getObject method, of class RProjectWorkspaceCalls.
     */
    @Test
    public void testProjectWorkspaceGetObjectAsList() {

        // Test variables.
        int rObjectSize = 2;
        List<String> objectNames = null;
        List<RData> listObjects = null;
        String expNameX = "x";
        String expNameY = "y";
        double delta = .1;
        double expValueX = 10.0;
        double expValueY = 20.0;
        String expType = "primitive";
        String expRClass = "numeric";
        RNumeric actualNumericX = null;
        RNumeric actualNumericY = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        objectNames = new ArrayList();
        objectNames.add(expNameX);
        objectNames.add(expNameY);

        try {
            listObjects = rProject.getObjects(objectNames);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.getObjects failed: ";
        }

        if (exception == null) {
            actualNumericX = (RNumeric) listObjects.get(0);
            actualNumericY = (RNumeric) listObjects.get(1);
        }

        // Test asserts.
        if (exception == null) {
            assertTrue(rObjectSize == listObjects.size());

            assertEquals(expNameX, actualNumericX.getName());
            assertEquals(expType, actualNumericX.getType());
            assertEquals(expRClass, actualNumericX.getRclass());
            assertEquals(expValueX, actualNumericX.getValue(), delta);

            assertEquals(expNameY, actualNumericY.getName());
            assertEquals(expType, actualNumericY.getType());
            assertEquals(expRClass, actualNumericY.getRclass());
            assertEquals(expValueY, actualNumericY.getValue(), delta);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of transferObject method, of class RProjectWorkspaceCalls.
     */
    @Test
    public void testProjectWorkspaceSaveAndTransferObject() {

        // Test variables.
        String expName = "x";
        String descr = "x object saved";
        double delta = .1;
        double expValue = 10.0;
        String expType = "primitive";
        String expRClass = "numeric";
        String workSpaceFileName = "x.rData";
        List<RData> listObjects = null;
        boolean versioning = false;
        RProjectFile projectFile = null;
        RNumeric rNumeric = null;
        URL url = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {
            projectFile = rProject.saveObject(expName, descr, versioning);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.saveObject failed: ";
        }

        if (exception == null) {
            try {
                url = projectFile.download();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "projectFile.about failed: ";
            }
        }

        if (exception == null) {
            try {
                rProject.deleteObject(expName);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.deleteObject failed: ";
            }
        }

        if (exception == null) {
            try {
                rProject.transferObject(workSpaceFileName, url);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.transferObject failed: ";
            }
        }

        if (exception == null) {
            try {
                rNumeric = (RNumeric) rProject.getObject(expName);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertEquals(expName, rNumeric.getName());
            assertEquals(expRClass, rNumeric.getRclass());
            assertEquals(expType, rNumeric.getType());
            assertEquals(expValue, rNumeric.getValue(), delta);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of pushObject method, of class RProjectWorkspaceCalls.
     */
    @Test
    public void testProjectWorkspacePushObject() {

        // Test variables.
        String rObjectName = "xStr";
        String rObjectValue = "this is a string";
        List<RData> inputs = null;
        RString expRString = null;
        RString actualRString = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        //Test.        
        expRString = RDataFactory.createString(rObjectName, rObjectValue);
        inputs = new ArrayList();
        inputs.add(expRString);

        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }

        if (exception == null) {
            try {
                actualRString = (RString) rProject.getObject(rObjectName);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertEquals(expRString.getName(), actualRString.getName());
            assertEquals(expRString.getRclass(), actualRString.getRclass());
            assertEquals(expRString.getType(), actualRString.getType());
            assertEquals(expRString.getValue(), actualRString.getValue());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

    }

    /**
     * Test of storeObject method, of class RProjectWorkspaceCalls.
     */
    @Test
    public void testProjectWorkspaceStoreObject() {

        // Test variables.
        String rObjectName = "";
        String expType = "primitive";
        String expRClass = "numeric";
        RProjectExecution projectExecution = null;
        boolean versioning = false;
        RRepositoryFile repoFile = null;
        RNumeric rNumeric = null;
        String descr = "R object desc";
        URL url = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        //Test.
        rObjectName = DeployrUtil.getUniqueRName("x");
        try {
            projectExecution = rProject.executeCode(rObjectName + "<-10");
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.executeCode failed: ";
        }

        if (exception == null) {
            try {
                repoFile = rProject.storeObject(rObjectName, descr, versioning, null, false, false);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.storeObject failed: ";
            }
        }

        if (exception == null) {
            try {
                url = repoFile.download();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoFile.about failed: ";
            }
        }

        if (exception == null) {
            try {
                rProject.deleteObject(rObjectName);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.deleteObject failed: ";
            }
        }

        if (exception == null) {
            try {
                rProject.transferObject(rObjectName, url);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.transferObject failed: ";
            }
        }

        if (exception == null) {
            try {
                rNumeric = (RNumeric) rProject.getObject(rObjectName);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
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

        // Test asserts.
        if (exception == null) {
            assertEquals(rObjectName, rNumeric.getName());
            assertEquals(expRClass, rNumeric.getRclass());
            assertEquals(expType, rNumeric.getType());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }

    }

    /**
     * Test of loadObject method, of class RProjectWorkspaceCalls.
     */
    @Test
    public void testProjectWorkspaceLoadObject() {

        // Test variables.
        int rWorkspaceSize = 1;
        String expType = "primitive";
        String expRClass = "numeric";
        String expRObjectName = "";
        String rObjectDescr = "x load object";
        boolean versioning = false;
        RRepositoryFile repoFile = null;
        RProjectExecution projectExecution = null;
        RNumeric actualRNumeric = null;
        List<RData> listObjects = null;
        List<String> objectNames = null;
        URL url = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        expRObjectName = DeployrUtil.getUniqueRName("x");

        try {
            projectExecution = rProject.executeCode(expRObjectName + "<-10");
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.executeCode failed: ";
        }

        if (exception == null) {
            try {
                repoFile = rProject.storeObject(expRObjectName, rObjectDescr, versioning, null, false, false);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.storeObject failed: ";
            }
        }

        if (exception == null) {
            // remove all objects from workspace
            objectNames = new ArrayList();
            objectNames.add("x");
            objectNames.add("y");
            objectNames.add(expRObjectName);
            try {
                rProject.deleteObject(objectNames);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.deleteObject failed: ";
            }
        }

        if (exception == null) {
            try {
                rProject.loadObject(repoFile);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.loadObject failed: ";
            }
        }

        if (exception == null) {
            try {
                actualRNumeric = (RNumeric) rProject.getObject(expRObjectName);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
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

        // Test assertions.
        if (exception == null) {
            assertEquals(rWorkspaceSize, listObjects.size());
            assertEquals(expRObjectName, actualRNumeric.getName());
            assertEquals(expRClass, actualRNumeric.getRclass());
            assertEquals(expType, actualRNumeric.getType());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }

    }

    /**
     * Test of deleteObject method, of class RProjectWorkspaceCalls.
     */
    @Test
    public void testProjectWorkspaceDeleteObjectAsString() {

        // Test variables.
        int rWorkspaceSize = 1;
        String rObjectName = "x";
        List<RData> listObjects = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {
            rProject.deleteObject(rObjectName);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.deleteObject failed: ";
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertEquals(rWorkspaceSize, listObjects.size());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of deleteObject method, of class RProjectWorkspaceCalls.
     */
    @Test
    public void testProjectWorkspaceDeleteObjectAsList() {

        // Test variables.
        int rWorkspaceSize = 0;
        String rObjectName = "x";
        List<RData> listObjects = null;
        List<String> objectNames = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        objectNames = new ArrayList();
        objectNames.add("x");
        objectNames.add("y");

        try {
            rProject.deleteObject(objectNames);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.deleteObject failed: ";
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        // Test asserts.
        if (exception == null) {
            assertEquals(rWorkspaceSize, listObjects.size());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }
}
