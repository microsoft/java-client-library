/*
 * RDataFactory.java
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

import com.revo.deployr.client.data.*;
import com.revo.deployr.client.RDataException;
import com.revo.deployr.client.data.impl.*;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.List;

/**
 * Factory to simplify the creation of RData in client applications.
 *
 * @see RData
 */
public class RDataFactory {

    private static Log log = LogFactory.getLog(RDataFactory.class);

    public static RBoolean createBoolean(String name, boolean value) {
        return new RBooleanImpl(name, value);
    }

    public static RNumeric createNumeric(String name, double value) {
        return new RNumericImpl(name, value);
    }

    public static RString createString(String name, String value) {
        return new RStringImpl(name, value);
    }

    public static RBooleanVector createBooleanVector(String name,
                                            List<Boolean> value) {
        return new RBooleanVectorImpl(name, value);
    }

    public static RNumericVector createNumericVector(String name,
                                            List<Double> value) {
        return new RNumericVectorImpl(name, value);
    }

    public static RStringVector createStringVector(String name,
                                            List<String> value) {
        return new RStringVectorImpl(name, value);
    }

    public static RDateVector createDateVector(String name,
                                List<Date> value, String format) {
        return new RDateVectorImpl(name, value, format);
    }

    public static RBooleanMatrix createBooleanMatrix(String name,
                                        List<List<Boolean>> value) {
        return new RBooleanMatrixImpl(name, value);
    }

    public static RNumericMatrix createNumericMatrix(String name,
                                        List<List<Double>> value) {
        return new RNumericMatrixImpl(name, value);
    }

    public static RStringMatrix createStringMatrix(String name,
                                        List<List<String>> value) {
        return new RStringMatrixImpl(name, value);
    }

    public static RList createList(String name, List<RData> value) {
        return new RListImpl(name, value);
    }

    public static RDataFrame createDataFrame(String name,
                                            List<RData> value) {
        return new RDataFrameImpl(name, value);
    }

    public static RFactor createFactor(String name,
                                    List value, boolean ordered) {

        return new RFactorImpl(name, value, ordered);
    }

    public static RFactor createFactor(String name, List value,
                        List levels, List labels, boolean ordered) {
        return new RFactorImpl(name, value, levels, labels, ordered);
    }

    public static RDate createDate(String name, Date value) {
        return new RDateImpl(name, value);
    }

    public static RDate createDate(String name, Date value, String format) {
        return new RDateImpl(name, value, format);
    }

    /**
     * Create an RDataTable using the data provided on the
     * call to intialize the table data.
     */
    public static RDataTable createDataTable(List<List> data) {
        return new RDataTableImpl(data);
    }

    /**
     * Create an RDataTable using the data found within the RData
     * instance  provided on the call to intialize the table data.
     * <p/>
     * The following RData concrete types are all accepted on this
     * call:
     * <p><ul>
     * <li>RDataFrame
     * <li>RNumericMatrix
     * <li>RStringMatrix
     * <li>RBooleanMatrix
     * <li>RNumericVector
     * <li>RStringVector
     * <li>RBooleanVector
     * <li>RDateVector
     * <ul>
     * <p>
     * All other RData types are rejected with an RDataException.
     * <p>
     */
    public static RDataTable createDataTable(RData rData)
                                            throws RDataException {
        return new RDataTableImpl(rData);
    }

    /**
     * Create an RDataTable using the data found in the file 
     * represented by the InputStream parameter.
     * <p/>
     * Set a value for the delimiter paramter that matches the
     * data delimiter in the file being ingested.
     * <p/>
     * Enable the hasHeader parameter value if the first line in the
     * file holds header values, as distinct to data values.
     */
    public static RDataTable createDataTable(InputStream is,
                      String delimiter,
                      boolean hasHeader) throws RDataException {
        return new RDataTableImpl(is, delimiter, hasHeader);
    }

    /**
     * Create an RDataTable using the data found in the file 
     * represented by the InputStream parameter.
     * <p/>
     * Set a value for the delimiter paramter that matches the
     * data delimiter in the file being ingested.
     * <p/>
     * Enable the hasHeader parameter value if the first line in the
     * file holds header values, as distinct to data values.
     * <p/>
     * Enable the nullMissingData parameter value if you want the
     * RDataTable to inject null values into the table data where
     * asymmetrical data is found in the file.
     */
    public static RDataTable createDataTable(InputStream is,
                      String delimiter,
                      boolean hasHeader,
                      boolean nullMissingData) throws RDataException {
        return new RDataTableImpl(is, delimiter, hasHeader, nullMissingData);
    }

}
