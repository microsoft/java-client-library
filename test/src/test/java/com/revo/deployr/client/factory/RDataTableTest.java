/*
 * RDataTableTest.java
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

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

public class RDataTableTest {

    public RDataTableTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of RDataFactory.createDataTable.
     */
    @Test
    public void testDataFactoryCreateDataTableFromNumericVector() throws Exception {

        // Test variables.
        RDataTable table = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {
            List vecVals = new ArrayList();
            for(int i=0; i<4; i++) {
                vecVals.add(i);
            }

            RNumericVector rVector =
                RDataFactory.createNumericVector("test", vecVals);

            table =
                RDataFactory.createDataTable(rVector);

        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "RDataFactory.createDataTable failed: ";
        }

        if (exception == null) {
            // Test asserts.
            assertTrue(table.asVector("test") instanceof RNumericVector);
            assertTrue(table.asDataFrame("test") instanceof RDataFrame);
            assertEquals(4, table.getRowCount());
            assertEquals(1, table.getColumnCount());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of RDataFactory.createDataTable.
     */
    @Test
    public void testDataFactoryCreateDataTableFromStringVector() throws Exception {

        // Test variables.
        RDataTable table = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {
            List vecVals = new ArrayList();
            for(int i=0; i<4; i++) {
                vecVals.add(Integer.toString(i));
            }

            RStringVector rVector =
                RDataFactory.createStringVector("test", vecVals);

            table =
                RDataFactory.createDataTable(rVector);

        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "RDataFactory.createDataTable failed: ";
        }

        if (exception == null) {
            // Test asserts.
            assertTrue(table.asVector("test") instanceof RStringVector);
            assertTrue(table.asDataFrame("test") instanceof RDataFrame);
            assertEquals(4, table.getRowCount());
            assertEquals(1, table.getColumnCount());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of RDataFactory.createDataTable.
     */
    @Test
    public void testDataFactoryCreateDataTableFromBooleanVector() throws Exception {

        // Test variables.
        RDataTable table = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {
            List vecVals = new ArrayList();
            for(int i=0; i<4; i++) {
                vecVals.add(true);
            }

            RBooleanVector rVector =
                RDataFactory.createBooleanVector("test", vecVals);

            table =
                RDataFactory.createDataTable(rVector);

        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "RDataFactory.createDataTable failed: ";
        }

        if (exception == null) {
            // Test asserts.
            assertTrue(table.asVector("test") instanceof RBooleanVector);
            assertTrue(table.asDataFrame("test") instanceof RDataFrame);
            assertEquals(4, table.getRowCount());
            assertEquals(1, table.getColumnCount());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of RDataFactory.createDataTable.
     */
    @Test
    public void testDataFactoryCreateDataTableFromDateVector() throws Exception {

        // Test variables.
        RDataTable table = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {

            List<Date> rdvval = new ArrayList<Date>();
            rdvval.add(new Date());
            rdvval.add(new Date());
            RDateVector rVector =
                RDataFactory.createDateVector("rdv", rdvval, "yyyy-MM-dd");

            table =
                RDataFactory.createDataTable(rVector);

        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "RDataFactory.createDataTable failed: ";
        }

        if (exception == null) {
            // Test asserts.
            assertTrue(table.asVector("test") instanceof RDateVector);
            assertTrue(table.asDataFrame("test") instanceof RDataFrame);
            assertEquals(2, table.getRowCount());
            assertEquals(1, table.getColumnCount());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of RDataFactory.createDataTable.
     */
    @Test
    public void testDataFactoryCreateDataTableFromNumericMatrix() throws Exception {

        // Test variables.
        RDataTable table = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {
            List matVals = new ArrayList();
            List matColA = new ArrayList();
            List matColB = new ArrayList();
            for(int i=0; i<4; i++) {
                matColA.add(i);
                matColB.add(i*10);
            }
            matVals.add(matColA);
            matVals.add(matColB);

            RNumericMatrix rMatrix =
                RDataFactory.createNumericMatrix("test", matVals);

            table =
                RDataFactory.createDataTable(rMatrix);

        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "RDataFactory.createDataTable failed: ";
        }

        if (exception == null) {
            // Test asserts.
            assertTrue(table.asMatrix("test") instanceof RNumericMatrix);
            assertTrue(table.asDataFrame("test") instanceof RDataFrame);
            assertEquals(4, table.getRowCount());
            assertEquals(2, table.getColumnCount());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of RDataFactory.createDataTable.
     */
    @Test
    public void testDataFactoryCreateDataTableFromStringMatrix() throws Exception {

        // Test variables.
        RDataTable table = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {
            List matVals = new ArrayList();
            List matColA = new ArrayList();
            List matColB = new ArrayList();
            for(int i=0; i<4; i++) {
                matColA.add(Integer.toString(i));
                matColB.add(Integer.toString(i*10));
            }
            matVals.add(matColA);
            matVals.add(matColB);

            RStringMatrix rMatrix =
                RDataFactory.createStringMatrix("test", matVals);

            table =
                RDataFactory.createDataTable(rMatrix);

        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "RDataFactory.createDataTable failed: ";
        }

        if (exception == null) {
            // Test asserts.
            assertTrue(table.asMatrix("test") instanceof RStringMatrix);
            assertTrue(table.asDataFrame("test") instanceof RDataFrame);
            assertEquals(4, table.getRowCount());
            assertEquals(2, table.getColumnCount());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of RDataFactory.createDataTable.
     */
    @Test
    public void testDataFactoryCreateDataTableFromBooleanMatrix() throws Exception {

        // Test variables.
        RDataTable table = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {
            List matVals = new ArrayList();
            List matColA = new ArrayList();
            List matColB = new ArrayList();
            for(int i=0; i<4; i++) {
                matColA.add(false);
                matColB.add(true);
            }
            matVals.add(matColA);
            matVals.add(matColB);

            RBooleanMatrix rMatrix =
                RDataFactory.createBooleanMatrix("test", matVals);

            table =
                RDataFactory.createDataTable(rMatrix);

        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "RDataFactory.createDataTable failed: ";
        }

        if (exception == null) {
            // Test asserts.
            assertTrue(table.asMatrix("test") instanceof RBooleanMatrix);
            assertTrue(table.asDataFrame("test") instanceof RDataFrame);
            assertEquals(4, table.getRowCount());
            assertEquals(2, table.getColumnCount());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of RDataFactory.createDataTable.
     */
    @Test
    public void testDataFactoryCreateDataTableFromDataFrame() throws Exception {

        // Test variables.
        RDataTable table = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {

            List<Double> rnvval = new ArrayList();
            rnvval.add(new Double(1.0));
            rnvval.add(new Double(2.0));
            RNumericVector rnv  = RDataFactory.createNumericVector("rnv", rnvval);
            List<Date> rdvval = new ArrayList<Date>();
            rdvval.add(new Date());
            rdvval.add(new Date());
            RDateVector rdv =
                RDataFactory.createDateVector("rdv", rdvval, "yyyy-MM-dd");
            List<RData> dfv = new ArrayList<RData>();
            dfv.add(rnv);
            dfv.add(rdv);
            RDataFrame df = RDataFactory.createDataFrame("numdf", dfv);

            table =
                RDataFactory.createDataTable(df);

        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "RDataFactory.createDataTable failed: ";
        }

        if (exception == null) {
            // Test asserts.
            assertTrue(table.asDataFrame("test") instanceof RDataFrame);
            assertEquals(2, table.getRowCount());
            assertEquals(2, table.getColumnCount());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of RDataFactory.createDataTable.
     */
    @Test
    public void testDataFactoryCreateDataTableCSVNoHeader() throws Exception {

        // Test variables.
        RDataTable table = null;
        String delimiter = ",";
        boolean hasHeader = false;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {
            InputStream is =
                new FileInputStream(new File("assets/sample.csv"));
            table =
                RDataFactory.createDataTable(is, delimiter, hasHeader);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "RDataFactory.createDataTable failed: ";
        }

        if (exception == null) {
            // Test asserts.
            assertTrue(table.asMatrix("test") instanceof RNumericMatrix);
            assertTrue(table.asDataFrame("test") instanceof RDataFrame);
            assertEquals(4, table.getColumnCount());
            assertEquals(5, table.getRowCount());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of RDataFactory.createDataTable.
     */
    @Test
    public void testDataFactoryCreateDataTableDelimiterWithHeader() throws Exception {

        // Test variables.
        RDataTable table = null;
        String delimiter = "\\s+";
        boolean hasHeader = true;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.
        try {
            InputStream is =
                new FileInputStream(new File("assets/delimiter.dat"));
            table =
                RDataFactory.createDataTable(is, delimiter, hasHeader);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "RDataFactory.createDataTable failed: ";
        }

        if (exception == null) {
            // Test asserts.
            assertTrue(table.asMatrix("test") instanceof RStringMatrix);
            assertTrue(table.asDataFrame("test") instanceof RDataFrame);
            assertEquals(5, table.getRowCount());
            assertEquals(4, table.getColumnCount());
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

}
