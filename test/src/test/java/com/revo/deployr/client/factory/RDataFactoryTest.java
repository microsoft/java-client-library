/*
 * RDataFactoryTest.java
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
package com.revo.deployr.client.factory;

import com.revo.deployr.DeployrUtil;
import com.revo.deployr.client.RClient;
import com.revo.deployr.client.RProject;
import com.revo.deployr.client.RUser;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.data.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

public class RDataFactoryTest {

    RClient rClient = null;
    RUser rUser = null;
    RProject rProject = null;
    double delta = .1;

    public RDataFactoryTest() {
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
     * Test of createBoolean method, of class RDataFactory.
     */
    @Test
    public void testDataFactoryCreateBoolean() {

        // Test variables.
        List<RData> inputs = null;
        RBoolean expBoolean = null;
        List listObjects = null;
        RData listObject = null;
        RBoolean actualRData = null;
        String rObject = "b1";
        boolean value = false;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        expBoolean = RDataFactory.createBoolean(rObject, value);

        inputs = new ArrayList();
        inputs.add(expBoolean);
        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }
        if (exception == null) {
            try {
                actualRData = (RBoolean) rProject.getObject(rObject);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
                listObject = (RData) listObjects.get(0);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(listObject.getRclass(), actualRData.getRclass());
            assertEquals(listObject.getName(), actualRData.getName());
            assertEquals(listObject.getRclass(), actualRData.getRclass());

            assertEquals(expBoolean.getName(), listObject.getName());
            assertEquals(expBoolean.getRclass(), listObject.getRclass());
            assertEquals(expBoolean.getType(), listObject.getType());

            assertEquals(expBoolean.getValue(), actualRData.getValue());
            assertEquals(expBoolean.getName(), actualRData.getName());
            assertEquals(expBoolean.getType(), actualRData.getType());
            assertEquals(expBoolean.getRclass(), actualRData.getRclass());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of createNumeric method, of class RDataFactory.
     */
    @Test
    public void testDataFactoryCreateNumeric() throws Exception {

        // Test variables.
        List<RData> inputs = null;
        RNumeric expNumeric = null;
        List listObjects = null;
        RData listObject = null;
        RNumeric actualRData = null;
        String rObject = "d1";
        double value = 10.0;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        expNumeric = RDataFactory.createNumeric(rObject, value);

        inputs = new ArrayList();
        inputs.add(expNumeric);
        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }
        if (exception == null) {
            try {
                actualRData = (RNumeric) rProject.getObject(rObject);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
                listObject = (RData) listObjects.get(0);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(listObject.getRclass(), actualRData.getRclass());
            assertEquals(listObject.getName(), actualRData.getName());
            assertEquals(listObject.getRclass(), actualRData.getRclass());

            assertEquals(expNumeric.getName(), listObject.getName());
            assertEquals(expNumeric.getRclass(), listObject.getRclass());
            assertEquals(expNumeric.getType(), listObject.getType());

            assertEquals(expNumeric.getValue(), actualRData.getValue(), delta);
            assertEquals(expNumeric.getName(), actualRData.getName());
            assertEquals(expNumeric.getType(), actualRData.getType());
            assertEquals(expNumeric.getRclass(), actualRData.getRclass());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of createString method, of class RDataFactory.
     */
    @Test
    public void testDataFactoryCreateString() throws Exception {

        // Test variables.
        List<RData> inputs = null;
        RString expString = null;
        List listObjects = null;
        RData listObject = null;
        RString actualRData = null;
        String rObject = "s1";
        String value = "a string";

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        expString = RDataFactory.createString(rObject, value);

        inputs = new ArrayList();
        inputs.add(expString);
        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }
        if (exception == null) {
            try {
                actualRData = (RString) rProject.getObject(rObject);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
                listObject = (RData) listObjects.get(0);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(listObject.getRclass(), actualRData.getRclass());
            assertEquals(listObject.getName(), actualRData.getName());
            assertEquals(listObject.getRclass(), actualRData.getRclass());

            assertEquals(expString.getName(), listObject.getName());
            assertEquals(expString.getRclass(), listObject.getRclass());
            assertEquals(expString.getType(), listObject.getType());

            assertEquals(expString.getValue(), actualRData.getValue());
            assertEquals(expString.getName(), actualRData.getName());
            assertEquals(expString.getType(), actualRData.getType());
            assertEquals(expString.getRclass(), actualRData.getRclass());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of createBooleanVector method, of class RDataFactory.
     */
    @Test
    public void testDataFactoryCreateBooleanVector() throws Exception {

        // Test variables.
        List<RData> inputs = null;
        RBooleanVector expBooleanVector = null;
        List listObjects = null;
        RData listObject = null;
        RBooleanVector actualRData = null;
        String rObject = "bv";
        List<Boolean> value = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        value = new ArrayList();
        value.add(true);
        value.add(false);
        expBooleanVector = RDataFactory.createBooleanVector(rObject, value);

        inputs = new ArrayList();
        inputs.add(expBooleanVector);
        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }
        if (exception == null) {
            try {
                actualRData = (RBooleanVector) rProject.getObject(rObject);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
                listObject = (RData) listObjects.get(0);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(listObject.getType(), actualRData.getType());
            assertEquals(listObject.getName(), actualRData.getName());
            assertEquals(listObject.getRclass(), actualRData.getRclass());

            assertEquals(expBooleanVector.getName(), listObject.getName());
            assertEquals(expBooleanVector.getRclass(), listObject.getRclass());
            assertEquals(expBooleanVector.getType(), listObject.getType());

            assertEquals(expBooleanVector.getValue(), actualRData.getValue());
            assertEquals(expBooleanVector.getName(), actualRData.getName());
            assertEquals(expBooleanVector.getType(), actualRData.getType());
            assertEquals(expBooleanVector.getRclass(), actualRData.getRclass());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of createNumericVector method, of class RDataFactory.
     */
    @Test
    public void testDataFactoryCreateNumericVector() throws Exception {

        // Test variables.
        List<RData> inputs = null;
        RNumericVector expNumericVector = null;
        List listObjects = null;
        RData listObject = null;
        RNumericVector actualRData = null;
        String rObject = "dv";
        List<Double> value = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        value = new ArrayList();
        value.add(10.0);
        value.add(20.0);
        expNumericVector = RDataFactory.createNumericVector(rObject, value);

        inputs = new ArrayList();
        inputs.add(expNumericVector);
        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }
        if (exception == null) {
            try {
                actualRData = (RNumericVector) rProject.getObject(rObject);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
                listObject = (RData) listObjects.get(0);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(listObject.getType(), actualRData.getType());
            assertEquals(listObject.getName(), actualRData.getName());
            assertEquals(listObject.getRclass(), actualRData.getRclass());

            assertEquals(expNumericVector.getName(), listObject.getName());
            assertEquals(expNumericVector.getRclass(), listObject.getRclass());
            assertEquals(expNumericVector.getType(), listObject.getType());

            assertEquals(expNumericVector.getValue(), actualRData.getValue());
            assertEquals(expNumericVector.getName(), actualRData.getName());
            assertEquals(expNumericVector.getType(), actualRData.getType());
            assertEquals(expNumericVector.getRclass(), actualRData.getRclass());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of createStringVector method, of class RDataFactory.
     */
    @Test
    public void testDataFactoryCreateStringVector() throws Exception {

        // Test variables.
        List<RData> inputs = null;
        RStringVector expStringVector = null;
        List listObjects = null;
        RData listObject = null;
        RStringVector actualRData = null;
        String rObject = "dv";
        List<String> value = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        value = new ArrayList();
        value.add("line one");
        value.add("line two");
        expStringVector = RDataFactory.createStringVector(rObject, value);

        inputs = new ArrayList();
        inputs.add(expStringVector);
        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }
        if (exception == null) {
            try {
                actualRData = (RStringVector) rProject.getObject(rObject);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
                listObject = (RData) listObjects.get(0);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(listObject.getType(), actualRData.getType());
            assertEquals(listObject.getName(), actualRData.getName());
            assertEquals(listObject.getRclass(), actualRData.getRclass());

            assertEquals(expStringVector.getName(), listObject.getName());
            assertEquals(expStringVector.getRclass(), listObject.getRclass());
            assertEquals(expStringVector.getType(), listObject.getType());

            assertEquals(expStringVector.getValue(), actualRData.getValue());
            assertEquals(expStringVector.getName(), actualRData.getName());
            assertEquals(expStringVector.getType(), actualRData.getType());
            assertEquals(expStringVector.getRclass(), actualRData.getRclass());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of createDateVector method, of class RDataFactory.
     */
    @Test
    public void testDataFactoryCreateDateVectorDate() throws Exception {

        // Test variables.
        List<RData> inputs = null;
        RDateVector expDateVector = null;
        List listObjects = null;
        RData listObject = null;
        RDateVector actualRData = null;
        String rObject = "dv";
        List<Date> value = null;
        String format = "yyyy-MM-dd";

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        value = new ArrayList();
        value.add(new GregorianCalendar(2011, 1, 1).getTime());
        value.add(new GregorianCalendar(2011, 1, 2).getTime());
        expDateVector = RDataFactory.createDateVector(rObject, value, format);

        inputs = new ArrayList();
        inputs.add(expDateVector);
        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }
        if (exception == null) {
            try {
                actualRData = (RDateVector) rProject.getObject(rObject);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
                listObject = (RData) listObjects.get(0);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(listObject.getType(), actualRData.getType());
            assertEquals(listObject.getName(), actualRData.getName());
            assertEquals(listObject.getRclass(), actualRData.getRclass());

            assertEquals(expDateVector.getName(), listObject.getName());
            assertEquals(expDateVector.getRclass(), listObject.getRclass());
            assertEquals(expDateVector.getType(), listObject.getType());

            assertEquals(expDateVector.getValue(), actualRData.getValue());
            assertEquals(expDateVector.getName(), actualRData.getName());
            assertEquals(expDateVector.getType(), actualRData.getType());
            assertEquals(expDateVector.getRclass(), actualRData.getRclass());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of createDateVector method, of class RDataFactory.
     */
    @Test
    public void testDataFactoryCreateDateVectorPOSIXct() throws Exception {

        // Test variables.
        List<RData> inputs = null;
        RDateVector expDateVector = null;
        List listObjects = null;
        RData listObject = null;
        RDateVector actualRData = null;
        String rObject = "dv";
        List<Date> value = null;
        String format = "yyyy-MM-dd HH:mm:ss z";

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        value = new ArrayList();
        value.add(new GregorianCalendar(2011, 1, 1, 10, 30, 30).getTime());
        value.add(new GregorianCalendar(2011, 1, 2, 10, 30, 30).getTime());
        expDateVector = RDataFactory.createDateVector(rObject, value, format);

        inputs = new ArrayList();
        inputs.add(expDateVector);
        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }
        if (exception == null) {
            try {
                actualRData = (RDateVector) rProject.getObject(rObject);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
                listObject = (RData) listObjects.get(0);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(listObject.getType(), actualRData.getType());
            assertEquals(listObject.getName(), actualRData.getName());
            assertEquals(listObject.getRclass(), actualRData.getRclass());

            assertEquals(expDateVector.getName(), listObject.getName());
            assertEquals(expDateVector.getRclass(), listObject.getRclass());
            assertEquals(expDateVector.getType(), listObject.getType());

            assertEquals(expDateVector.getValue(), actualRData.getValue());
            assertEquals(expDateVector.getName(), actualRData.getName());
            assertEquals(expDateVector.getType(), actualRData.getType());
            assertEquals(expDateVector.getRclass(), actualRData.getRclass());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }


    }

    /**
     * Test of createBooleanMatrix method, of class RDataFactory.
     */
    @Test
    public void testDataFactoryCreateBooleanMatrix() throws Exception {

        // Test variables.
        List<RData> inputs = null;
        RBooleanMatrix expBooleanMatrix = null;
        List listObjects = null;
        RData listObject = null;
        RBooleanMatrix actualRData = null;
        String rObject = "bm";
        List<List<Boolean>> value = null;
        List<Boolean> bList = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        value = new ArrayList();
        bList = new ArrayList();
        bList.add(new Boolean(true));
        bList.add(new Boolean(true));
        value.add(bList);

        expBooleanMatrix = RDataFactory.createBooleanMatrix(rObject, value);

        inputs = new ArrayList();
        inputs.add(expBooleanMatrix);
        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }
        if (exception == null) {
            try {
                actualRData = (RBooleanMatrix) rProject.getObject(rObject);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
                listObject = (RData) listObjects.get(0);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(listObject.getRclass(), actualRData.getRclass());
            assertEquals(listObject.getName(), actualRData.getName());
            assertEquals(listObject.getType(), actualRData.getType());

            assertEquals(expBooleanMatrix.getName(), listObject.getName());
            assertEquals(expBooleanMatrix.getRclass(), listObject.getRclass());
            assertEquals(expBooleanMatrix.getType(), listObject.getType());

            assertEquals(expBooleanMatrix.getValue(), actualRData.getValue());
            assertEquals(expBooleanMatrix.getName(), actualRData.getName());
            assertEquals(expBooleanMatrix.getType(), actualRData.getType());
            assertEquals(expBooleanMatrix.getRclass(), actualRData.getRclass());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of createNumericMatrix method, of class RDataFactory.
     */
    @Test
    public void testDataFactoryCreateNumericMatrix() throws Exception {

        // Test variables.
        List<RData> inputs = null;
        RNumericMatrix expNumericMatrix = null;
        List listObjects = null;
        RData listObject = null;
        RNumericMatrix actualRData = null;
        String rObject = "dm";
        List<List<Double>> value = null;
        List<Double> dList = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        value = new ArrayList();
        dList = new ArrayList();
        dList.add(new Double(1.0));
        dList.add(new Double(2.0));
        value.add(dList);

        expNumericMatrix = RDataFactory.createNumericMatrix(rObject, value);

        inputs = new ArrayList();
        inputs.add(expNumericMatrix);
        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }
        if (exception == null) {
            try {
                actualRData = (RNumericMatrix) rProject.getObject(rObject);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
                listObject = (RData) listObjects.get(0);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(listObject.getType(), actualRData.getType());
            assertEquals(listObject.getName(), actualRData.getName());
            assertEquals(listObject.getRclass(), actualRData.getRclass());

            assertEquals(expNumericMatrix.getName(), listObject.getName());
            assertEquals(expNumericMatrix.getRclass(), listObject.getRclass());
            assertEquals(expNumericMatrix.getType(), listObject.getType());

            assertEquals(expNumericMatrix.getValue(), actualRData.getValue());
            assertEquals(expNumericMatrix.getName(), actualRData.getName());
            assertEquals(expNumericMatrix.getType(), actualRData.getType());
            assertEquals(expNumericMatrix.getRclass(), actualRData.getRclass());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of createStringMatrix method, of class RDataFactory.
     */
    @Test
    public void testDataFactoryCreateStringMatrix() throws Exception {

        // Test variables.
        List<RData> inputs = null;
        RStringMatrix expStringMatrix = null;
        List listObjects = null;
        RData listObject = null;
        RStringMatrix actualRData = null;
        String rObject = "sm";
        List<List<String>> value = null;
        List<String> sList = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        value = new ArrayList();
        sList = new ArrayList();
        sList.add("string 1");
        sList.add("string 2");
        value.add(sList);

        expStringMatrix = RDataFactory.createStringMatrix(rObject, value);

        inputs = new ArrayList();
        inputs.add(expStringMatrix);
        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }
        if (exception == null) {
            try {
                actualRData = (RStringMatrix) rProject.getObject(rObject);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
                listObject = (RData) listObjects.get(0);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(listObject.getType(), actualRData.getType());
            assertEquals(listObject.getName(), actualRData.getName());
            assertEquals(listObject.getRclass(), actualRData.getRclass());

            assertEquals(expStringMatrix.getName(), listObject.getName());
            assertEquals(expStringMatrix.getRclass(), listObject.getRclass());
            assertEquals(expStringMatrix.getType(), listObject.getType());

            assertEquals(expStringMatrix.getValue(), actualRData.getValue());
            assertEquals(expStringMatrix.getName(), actualRData.getName());
            assertEquals(expStringMatrix.getType(), actualRData.getType());
            assertEquals(expStringMatrix.getRclass(), actualRData.getRclass());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of createList method, of class RDataFactory.
     */
    @Test
    public void testDataFactoryCreateList() {

        // Test variables.
        List<RData> inputs = null;
        RList expList = null;
        List listObjects = null;
        RData listObject = null;
        RList actualRData = null;
        String rObject = "rl";
        List<RData> value;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        RNumeric d1 = RDataFactory.createNumeric("d1", 2.0);
        RNumeric d2 = RDataFactory.createNumeric("d2", 3.0);
        value = new ArrayList();
        value.add(d1);
        value.add(d2);
        expList = RDataFactory.createList(rObject, value);

        inputs = new ArrayList();
        inputs.add(expList);
        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }
        if (exception == null) {
            try {
                actualRData = (RList) rProject.getObject(rObject);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
                listObject = (RData) listObjects.get(0);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        if (exception == null) {
            // Test asserts.

            assertEquals(listObject.getName(), actualRData.getName());
            assertEquals(listObject.getRclass(), actualRData.getRclass());
            assertEquals(listObject.getType(), actualRData.getType());

            assertEquals(expList.getName(), listObject.getName());
            assertEquals(expList.getRclass(), listObject.getRclass());
            assertEquals(expList.getType(), listObject.getType());

            assertEquals(((RNumeric) actualRData.getValue().get(0)).getValue(),
                    ((RNumeric) expList.getValue().get(0)).getValue(), delta);
            assertEquals(((RNumeric) actualRData.getValue().get(1)).getValue(),
                    ((RNumeric) expList.getValue().get(1)).getValue(), delta);
            assertEquals(expList.getName(), actualRData.getName());
            assertEquals(expList.getType(), actualRData.getType());
            assertEquals(expList.getRclass(), actualRData.getRclass());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of createDataFrame method, of class RDataFactory.
     */
    @Test
    public void testDataFactoryCreateDataFrame() {

        // Test variables.
        List<RData> inputs = null;
        RDataFrame expList = null;
        List listObjects = null;
        RData listObject = null;
        RDataFrame actualRData = null;
        String rObject = "df";
        List<RData> value = null;
        List<Double> d1 = null;
        List<Double> d2 = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        value = new ArrayList();
        d1 = new ArrayList();
        d1.add(new Double(1.0));
        d1.add(new Double(2.0));
        RNumericVector dv1 = RDataFactory.createNumericVector("d1", d1);

        d2 = new ArrayList();
        d2.add(new Double(1.0));
        d2.add(new Double(2.0));
        RNumericVector dv2 = RDataFactory.createNumericVector("d2", d2);

        value.add(dv1);
        value.add(dv2);
        expList = RDataFactory.createDataFrame(rObject, value);

        inputs = new ArrayList();
        inputs.add(expList);
        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }
        if (exception == null) {
            try {
                actualRData = (RDataFrame) rProject.getObject(rObject);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
                listObject = (RData) listObjects.get(0);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(listObject.getRclass(), actualRData.getRclass());
            assertEquals(listObject.getName(), actualRData.getName());
            assertEquals(listObject.getType(), actualRData.getType());

            assertEquals(expList.getName(), listObject.getName());
            assertEquals(expList.getRclass(), listObject.getRclass());
            assertEquals(expList.getType(), listObject.getType());

            assertEquals(((RNumericVector) expList.getValue().get(0)).getValue().get(0).doubleValue(),
                    ((RNumericVector) actualRData.getValue().get(0)).getValue().get(0).doubleValue(), delta);
            assertEquals(((RNumericVector) expList.getValue().get(0)).getValue().get(1).doubleValue(),
                    ((RNumericVector) actualRData.getValue().get(0)).getValue().get(1).doubleValue(), delta);
            assertEquals(((RNumericVector) expList.getValue().get(1)).getValue().get(0).doubleValue(),
                    ((RNumericVector) actualRData.getValue().get(1)).getValue().get(0).doubleValue(), delta);
            assertEquals(((RNumericVector) expList.getValue().get(1)).getValue().get(1).doubleValue(),
                    ((RNumericVector) actualRData.getValue().get(1)).getValue().get(1).doubleValue(), delta);

            assertEquals(expList.getName(), actualRData.getName());
            assertEquals(expList.getType(), actualRData.getType());
            assertEquals(expList.getRclass(), actualRData.getRclass());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of createDataFrame method, of class RDataFactory.
     */
    @Test
    public void testDataFactoryCreateDataFrameWithDateVectors() {

        // Test variables.
        List<RData> inputs = null;
        RDataFrame expList = null;
        List listObjects = null;
        RData listObject = null;
        RDataFrame actualRData = null;
        String rObject = "df";
        List<RData> value = null;
        List<Double> d1 = null;
        List<Double> d2 = null;

        String dateFormat = "yyyy-MM-dd";
        RDateVector rdv = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        value = new ArrayList();
        d1 = new ArrayList();
        d1.add(new Double(1.0));
        d1.add(new Double(2.0));
        RNumericVector dv1 = RDataFactory.createNumericVector("d1", d1);

        d2 = new ArrayList();
        d2.add(new Double(1.0));
        d2.add(new Double(2.0));
        RNumericVector dv2 = RDataFactory.createNumericVector("d2", d2);

        rdv = RDataFactory.createDateVector("DateTestVector", new ArrayList(), dateFormat);
        rdv.getValue().add(new Date());
        rdv.getValue().add(new Date());

        value.add(dv1);
        value.add(dv2);
        value.add(rdv);
        expList = RDataFactory.createDataFrame(rObject, value);

        inputs = new ArrayList();
        inputs.add(expList);
        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }

        if (exception == null) {
            try {
                actualRData = (RDataFrame) rProject.getObject("df");
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
                listObject = (RData) listObjects.get(0);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(listObject.getRclass(), actualRData.getRclass());
            assertEquals(listObject.getName(), actualRData.getName());
            assertEquals(listObject.getType(), actualRData.getType());

            assertEquals(expList.getName(), listObject.getName());
            assertEquals(expList.getRclass(), listObject.getRclass());
            assertEquals(expList.getType(), listObject.getType());

            assertEquals(((RNumericVector) expList.getValue().get(0)).getValue().get(0).doubleValue(),
                    ((RNumericVector) actualRData.getValue().get(0)).getValue().get(0).doubleValue(), delta);
            assertEquals(((RNumericVector) expList.getValue().get(0)).getValue().get(1).doubleValue(),
                    ((RNumericVector) actualRData.getValue().get(0)).getValue().get(1).doubleValue(), delta);
            assertEquals(((RNumericVector) expList.getValue().get(1)).getValue().get(0).doubleValue(),
                    ((RNumericVector) actualRData.getValue().get(1)).getValue().get(0).doubleValue(), delta);
            assertEquals(((RNumericVector) expList.getValue().get(1)).getValue().get(1).doubleValue(),
                    ((RNumericVector) actualRData.getValue().get(1)).getValue().get(1).doubleValue(), delta);

            assertTrue(actualRData.getValue().get(2) instanceof RDateVector);

            Object temp = ((RDateVector) expList.getValue().get(2)).getRclass();

            assertEquals(((RDateVector) expList.getValue().get(2)).getValue().get(0).getClass(),
                    ((RDateVector) actualRData.getValue().get(2)).getValue().get(0).getClass());

            assertEquals("Date",
                    ((RDateVector) actualRData.getValue().get(2)).getRclass());
            assertEquals("vector",
                    ((RDateVector) actualRData.getValue().get(2)).getType());

            assertEquals(expList.getName(), actualRData.getName());
            assertEquals(expList.getType(), actualRData.getType());
            assertEquals(expList.getRclass(), actualRData.getRclass());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of createFactor method, of class RDataFactory.
     */
    @Test
    public void testDataFactoryCreateFactorStringList() {

        // Test variables.
        String name = "fact";
        boolean ordered = false;
        String rclass = "factor";
        RFactor actualRFactor = null;
        RFactor expResult = null;
        List rObjects = null;
        RData rObject = null;
        List<RData> inputs = null;
        List value = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        inputs = new ArrayList();
        value = new ArrayList();
        value.add("a");
        value.add("b");
        value.add("d");
        value.add("e");
        expResult = RDataFactory.createFactor(name, value, ordered);
        inputs.add(expResult);

        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }

        if (exception == null) {
            try {
                rObjects = rProject.listObjects();
                rObject = (RData) rObjects.get(0);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        if (exception == null) {
            try {
                actualRFactor = (RFactor) rProject.getObject(name);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(rObject.getRclass(), actualRFactor.getRclass());
            assertEquals(rObject.getName(), actualRFactor.getName());
            assertEquals(rObject.getType(), actualRFactor.getType());

            assertEquals(expResult.getName(), rObject.getName());
            assertEquals(expResult.getRclass(), rObject.getRclass());
            assertEquals(expResult.getType(), rObject.getType());

            assertEquals(expResult.getValue(), actualRFactor.getValue());
            assertEquals(expResult.getName(), actualRFactor.getName());
            assertEquals(expResult.getType(), actualRFactor.getType());
            assertEquals(expResult.getRclass(), actualRFactor.getRclass());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

    }

    /**
     * Test of createFactor method, of class RDataFactory.
     */
    @Test
    public void testDataFactoryCreateFactor5args() {

        // Test variables.
        String name = "fact";
        List labels = null;
        List levels = null;
        List value = null;
        List valueToTest = null;
        RFactor expResult = null;
        RData rObject = null;
        RFactor actualRData = null;
        List<RData> inputs = null;
        List rObjects = null;
        boolean ordered = false;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        levels = new ArrayList();
        levels.add("1");
        levels.add("2");
        levels.add("3");

        labels = new ArrayList();
        labels.add("one");
        labels.add("two");
        labels.add("three");

        value = new ArrayList();
        value.add("1");
        value.add("3");
        value.add("2");
        value.add("2");
        value.add("3");
        value.add("2");

        valueToTest = new ArrayList();
        valueToTest.add("one");
        valueToTest.add("three");
        valueToTest.add("two");
        valueToTest.add("two");
        valueToTest.add("three");
        valueToTest.add("two");

        expResult = RDataFactory.createFactor(name, value, levels, labels, ordered);
        inputs = new ArrayList();
        inputs.add(expResult);

        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }

        if (exception == null) {
            try {
                actualRData = (RFactor) rProject.getObject(name);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            try {
                rObjects = rProject.listObjects();
                rObject = (RData) rObjects.get(0);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(rObject.getRclass(), actualRData.getRclass());
            assertEquals(rObject.getName(), actualRData.getName());
            assertEquals(rObject.getType(), actualRData.getType());

            assertEquals(expResult.getName(), rObject.getName());
            assertEquals(expResult.getRclass(), rObject.getRclass());
            assertEquals(expResult.getType(), rObject.getType());

            assertEquals(valueToTest, actualRData.getValue());
            assertEquals(labels, actualRData.getLevels());
            assertEquals(expResult.getName(), actualRData.getName());
            assertEquals(expResult.getRclass(), actualRData.getRclass());
            assertEquals(expResult.getType(), actualRData.getType());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of createDate method, of class RDataFactory.
     */
    @Test
    public void testDataFactoryCreateDateStringDate() {

        // Test variables.
        String name = "d";
        String rclass = "Date";
        String format = "yyyy-MM-dd";
        Date value = null;
        RDate expResult = null;
        RDate actualRData = null;
        List<RData> inputs = null;
        List listObjects = null;
        RData rObject = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        value = new GregorianCalendar(2010, 3, 2).getTime();
        expResult = RDataFactory.createDate(name, value, format);
        inputs = new ArrayList();
        inputs.add(expResult);

        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }

        if (exception == null) {
            try {
                actualRData = (RDate) rProject.getObject(name);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
                rObject = (RData) listObjects.get(0);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(rObject.getRclass(), actualRData.getRclass());
            assertEquals(rObject.getName(), actualRData.getName());
            assertEquals(rObject.getRclass(), actualRData.getRclass());

            assertEquals(expResult.getName(), rObject.getName());
            assertEquals(expResult.getRclass(), rObject.getRclass());
            assertEquals(expResult.getType(), rObject.getType());

            assertEquals(expResult.getValue(), actualRData.getValue());
            assertEquals(expResult.getName(), actualRData.getName());
            assertEquals(expResult.getType(), actualRData.getType());
            assertEquals(expResult.getRclass(), actualRData.getRclass());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }


    }

    /**
     * Test of createDate method, of class RDataFactory.
     */
    @Test
    public void testDataFactoryCreateDate4args() throws Exception {

        // Test variables.
        String name = "d";
        Date value = null;
        RDate expResult = null;
        RDate actualRData = null;
        List<RData> inputs = null;
        List listObjects = null;
        RData rObject = null;
        String rClass = "POSIXct";
        String format = "yyyy-MM-dd HH:mm:ss z";

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        value = new GregorianCalendar(2010, 3, 2, 10, 30, 30).getTime();
        expResult = RDataFactory.createDate(name, value, format);
        inputs = new ArrayList();
        inputs.add(expResult);
        try {
            rProject.pushObject(inputs);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rProject.pushObject failed: ";
        }

        if (exception == null) {
            try {
                actualRData = (RDate) rProject.getObject(name);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.getObject failed: ";
            }
        }

        if (exception == null) {
            try {
                listObjects = rProject.listObjects();
                rObject = (RData) listObjects.get(0);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rProject.listObjects failed: ";
            }
        }

        if (exception == null) {
            // Test asserts.
            assertEquals(rObject.getRclass(), actualRData.getRclass());
            assertEquals(rObject.getName(), actualRData.getName());
            assertEquals(rObject.getRclass(), actualRData.getRclass());

            assertEquals(expResult.getName(), rObject.getName());
            assertEquals(rClass, rObject.getRclass());
            assertEquals(expResult.getType(), rObject.getType());

            assertEquals(expResult.getValue(), actualRData.getValue());
            assertEquals(expResult.getName(), actualRData.getName());
            assertEquals(expResult.getType(), actualRData.getType());
            assertEquals(expResult.getRclass(), actualRData.getRclass());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }
}
