/*
 * RDataUtil.java
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
package com.revo.deployr.client.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revo.deployr.client.RDataException;
import com.revo.deployr.client.data.*;
import com.revo.deployr.client.data.impl.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// import org.codehaus.jackson.map.ObjectMapper;

/**
 * RDataUtil provides support for the following:
 * <p/>
 * <br/><br/>
 * 1. Builds RData from JSON.
 * <br/>
 * 2. Builds JSON from RData.
 * <br/>
 * 3. Builds JSON from List<RData>.
 */
public class RDataUtil {

    private static Log log = LogFactory.getLog(RDataUtil.class);

    public static RData fromJSON(String name, Map json) {

        RData rData = null;

        String type = (json.get("type") != null) ? (String) json.get("type") : "primitive";

        log.debug("fromJSON: name=" + name + " type=" + type + " json=" + json);

        if (type.equalsIgnoreCase("primitive")) {
            rData = buildPrimitiveFromJSON(name, json);
        } else if (type.equalsIgnoreCase("vector")) {
            rData = buildVectorFromJSON(name, json);
        } else if (type.equalsIgnoreCase("matrix")) {
            rData = buildMatrixFromJSON(name, json);
        } else if (type.equalsIgnoreCase("list")) {
            rData = buildListFromJSON(name, json);
        } else if (type.equalsIgnoreCase("dataframe")) {
            rData = buildDataFrameFromJSON(name, json);
        } else if (type.equalsIgnoreCase("factor")) {
            rData = buildFactorFromJSON(name, json);
        } else if (type.equalsIgnoreCase("date")) {
            rData = buildDateFromJSON(name, json);
        } else {
            // Support RData without a corresponding
            // Phoenix encoding as can be
            // returned on a RSession listObjects call.
            rData = buildSummaryFromJSON(name, json);
        }

        return rData;
    }

    public static String toJSON(RData rData) throws RDataException {

        Map<String, Object> pMap = new HashMap<String, Object>();
        Map<String, Object> pValues = new HashMap<String, Object>();
        pValues.put("type", rData.getType());
        pValues.put("rclass", rData.getRclass());
        pValues.put("value", getValueForJSON(rData));

	/*
     * RData special cases needs additional properties in markup.
	 */
        if (rData instanceof RDate) {
            RDate rDate = (RDate) rData;
            pValues.put("format", rDate.getFormat());
        } else if (rData instanceof RDateVector) {
            RDateVector rDateVector = (RDateVector) rData;
            pValues.put("format", rDateVector.getFormat());
        } else if (rData instanceof RFactor) {
            RFactor pFactor = (RFactor) rData;
            if (pFactor.getLevels() != null)
                pValues.put("levels", pFactor.getLevels());
            if (pFactor.getLabels() != null)
                pValues.put("labels", pFactor.getLabels());
            pValues.put("ordered", pFactor.isOrdered());
        }

        pMap.put(rData.getName(), pValues);

        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();

        String json = null;

        try {
            mapper.writeValue(writer, pMap);
            json = writer.toString();
        } catch (Exception ex) {
            log.warn("toJSON: exception writing RData, name=" + rData.getName() + " type=" + rData.getType() + " to JSON.", ex);
            throw new RDataException("RData can be not be parsed into JSON, ex=" + ex.getMessage());
        }

        log.debug("toJSON: returning=" + json);
        return json;
    }

    public static String toJSON(List<RData> data) throws RDataException {

        Map<String, Object> pMap = new HashMap<String, Object>();

        for (RData rData : data) {

            Map<String, Object> pValues = new HashMap<String, Object>();
            pValues.put("type", rData.getType());
            pValues.put("rclass", rData.getRclass());
            pValues.put("value", getValueForJSON(rData));

	    /*
	     * RData special cases needs additional properties in markup.
	     */
            if (rData instanceof RDate) {
                RDate rDate = (RDate) rData;
                pValues.put("format", rDate.getFormat());
            } else if (rData instanceof RDateVector) {
                RDateVector rDateVector = (RDateVector) rData;
                pValues.put("format", rDateVector.getFormat());
            } else if (rData instanceof RFactor) {
                RFactor pFactor = (RFactor) rData;
                if (pFactor.getLevels() != null)
                    pValues.put("levels", pFactor.getLevels());
                if (pFactor.getLabels() != null)
                    pValues.put("labels", pFactor.getLabels());
                pValues.put("ordered", pFactor.isOrdered());
            }

            pMap.put(rData.getName(), pValues);
        }

        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();

        String json = null;

        try {
            mapper.writeValue(writer, pMap);
            json = writer.toString();
        } catch (Exception ex) {
            log.warn("toJSON: exception writing List<RData> to JSON.", ex);
            throw new RDataException("RData list can be not be parsed into JSON, ex=" + ex.getMessage());
        }

        log.debug("toJSON: List<RData> returning=" + json);
        return json;
    }

    private static RData buildPrimitiveFromJSON(String name, Map json) {

        RData rData = null;

        String type = (String) json.get("type");
        String rclass = (String) json.get("rclass");

        log.debug("buildPrimitiveFromJSON: name=" + name + " type=" + type + " rclass=" + rclass + " json=" + json);

        if (json.containsKey("value")) {

            Object value = json.get("value");

            log.debug("buildPrimitiveFromJSON: name=" + name + " has value=" + value);

            if (value instanceof Integer) {
                rData = new RNumericImpl(name, ((Integer) value).doubleValue());
            } else if (value instanceof Double) {
                rData = new RNumericImpl(name, (Double) value);
            } else if (value instanceof String) {
                rData = new RStringImpl(name, (String) value);
            } else if (value instanceof Boolean) {
                rData = new RBooleanImpl(name, (Boolean) value);
            } else {
                // Indeterminate type, skip here to treat as RDataNA.
            }

        } else {

            rData = new RDataImpl(name, type, rclass);
        }

        if (rData == null) {
            rData = new RDataNAImpl(name, "vector");
        }

        log.debug("buildPrimitiveFromJSON: name=" + name + " returning=" + rData);
        return rData;
    }


    private static RData buildVectorFromJSON(String name, Map json) {

        RData rData = null;

        String type = (String) json.get("type");
        String rclass = (String) json.get("rclass");
        String format = (String) json.get("format");

        log.debug("buildVectorFromJSON: name=" + name + " type=" + type + " rclass=" + rclass + " format=" + format + " json=" + json);

        if (format != null) {

            if (json.containsKey("value")) {

                List value = (List) json.get("value");
                log.debug("buildVectorFromJSON: name=" + name + " format=" + format + " has value=" + value);
                Iterator iter = value.iterator();

                List<Date> dates = new ArrayList<Date>();

                SimpleDateFormat sdf = new SimpleDateFormat(format);

                while (iter.hasNext()) {

                    Object found = iter.next();
                    if (found instanceof String) {

                        try {
                            Date date = sdf.parse((String) found);
                            dates.add(date);
                        } catch (Exception ex) {
                            log.debug("buildVectorFromJSON: date sdf.parse format=${format} adding NULL because ex=${ex}");
                            ex.printStackTrace();
                            dates.add(null);
                        }
                    }
                }

                rData = new RDateVectorImpl(name, dates, format, rclass);
            }

        } else if (json.containsKey("value")) {

            List value = (List) json.get("value");

            log.debug("buildVectorFromJSON: name=" + name + " has value=" + value);

            Iterator iter = value.iterator();

            // Loop to determine the "type" of context in the JSON vector.
            while (iter.hasNext()) {

                Object found = iter.next();

                if (found instanceof Integer) {
                    rData = new RNumericVectorImpl(name, value);
                } else if (found instanceof Double) {
                    rData = new RNumericVectorImpl(name, value);
                } else if (found instanceof String) {
                    rData = new RStringVectorImpl(name, value);
                } else if (found instanceof Boolean) {
                    rData = new RBooleanVectorImpl(name, value);
                } else {
                    // Indeterminate type, skip here allowing other value in vector to determine type.
                }

                if (rData != null)
                    break;
            }

        } else {

            rData = new RDataImpl(name, type, rclass);
        }

        if (rData == null) {
            rData = new RDataNAImpl(name, "vector");
        }

        log.debug("buildVectorFromJSON: name=" + name + " returning=" + rData);
        return rData;
    }

    private static RData buildMatrixFromJSON(String name, Map json) {

        RData rData = null;

        String type = (String) json.get("type");
        String rclass = (String) json.get("rclass");

        log.debug("buildMatrixFromJSON: name=" + name + " type=" + type + " rclass=" + rclass + " json=" + json);

        if (json.containsKey("value")) {

            List value = (List) json.get("value");

            log.debug("buildMatrixFromJSON: name=" + name + " value=" + value);

            Iterator rows = value.iterator();

            // Loop to determine the "type" of context in the JSON matrix.
            while (rows.hasNext()) {

                List rowList = (List) rows.next();

                Iterator rowIterator = rowList.iterator();

                while (rowIterator.hasNext()) {

                    Object entry = rowIterator.next();

                    if (entry instanceof Integer) {
                        rData = new RNumericMatrixImpl(name, value);
                    } else if (entry instanceof Double) {
                        rData = new RNumericMatrixImpl(name, value);
                    } else if (entry instanceof String) {
                        rData = new RStringMatrixImpl(name, value);
                    } else if (entry instanceof Boolean) {
                        rData = new RBooleanMatrixImpl(name, value);
                    } else {
                        // Indeterminate type, skip here allowing other value in matrix to determine type.
                    }

                    if (rData != null)
                        break;
                }

                if (rData != null)
                    break;
            }

        } else {

            rData = new RDataImpl(name, type, rclass);
        }

        if (rData == null) {
            rData = new RDataNAImpl(name, "matrix");
        }

        log.debug("buildMatrixFromJSON: name=" + name + " returning=" + rData);
        return rData;
    }

    private static RData buildListFromJSON(String name, Map json) {

        RData rData = null;

        String type = (String) json.get("type");
        String rclass = (String) json.get("rclass");

        log.debug("buildListFromJSON: name=" + name + " type=" + type + " rclass=" + rclass + " json=" + json);

        if (json.containsKey("value")) {

            List<Map> values = (List<Map>) json.get("value");

            List<RData> listData = new ArrayList();

            for (Map entry : values) {

                // Recursively build RList from JSON.
                String entryName = (String) entry.get("name");
                RData dataItem = fromJSON(entryName, entry);
                listData.add(dataItem);
            }

            rData = new RListImpl(name, listData);

        } else {

            rData = new RDataImpl(name, type, rclass);
        }

        log.debug("buildListFromJSON: name=" + name + " returning=" + rData);
        return rData;
    }

    private static RData buildDataFrameFromJSON(String name, Map json) {

        RData rData = null;

        String type = (String) json.get("type");
        String rclass = (String) json.get("rclass");

        log.debug("buildDataFrameFromJSON: name=" + name + " type=" + type + " rclass=" + rclass + " json=" + json);

        if (json.containsKey("value")) {

            List<Map> values = (List<Map>) json.get("value");

            List<RData> frameData = new ArrayList();

            for (Map entry : values) {

                // Recursively build RDataFrame from JSON.
                String entryName = (String) entry.get("name");
                RData dataItem = fromJSON(entryName, entry);
                frameData.add(dataItem);
            }

            rData = new RDataFrameImpl(name, frameData);

        } else {

            rData = new RDataImpl(name, type, rclass);
        }

        log.debug("buildDataFrameFromJSON: name=" + name + " returning=" + rData);
        return rData;
    }

    private static RData buildFactorFromJSON(String name, Map json) {

        RData rData = null;

        String type = (String) json.get("type");
        String rclass = (String) json.get("rclass");

        log.debug("buildFactorFromJSON: name=" + name + " type=" + type + " rclass=" + rclass + " json=" + json);

        if (json.containsKey("value")) {

            List value = (List) json.get("value");
            List levels = (List) json.get("levels");
            List labels = (List) json.get("labels");
            boolean ordered = (Boolean) json.get("ordered");
            rData = new RFactorImpl(name, value, levels, labels, ordered, rclass);

        } else {

            rData = new RDataImpl(name, type, rclass);
        }

        log.debug("buildFactorFromJSON: name=" + name + " returning=" + rData);
        return rData;
    }

    private static RData buildDateFromJSON(String name, Map json) {

        RData rData = null;

        String type = (String) json.get("type");
        String rclass = (String) json.get("rclass");

        log.debug("buildDateFromJSON: name=" + name + " type=" + type + " rclass=" + rclass + " json=" + json);

        if (json.containsKey("value")) {

            String value = (String) json.get("value");
            String format = (String) json.get("format");

            SimpleDateFormat sdf = new SimpleDateFormat(format);

            try {
                Date date = sdf.parse(value);
                rData = new RDateImpl(name, date, format, rclass);
            } catch (ParseException pex) {
                rData = new RDataImpl(name, type, rclass);
                log.warn("buildDateFromJSON: bad date results in NA, RDataImpl=" + rData);
            }

        } else {

            rData = new RDataImpl(name, type, rclass);
        }

        log.debug("buildDateFromJSON: name=" + name + " returning=" + rData);
        return rData;
    }

    private static RData buildSummaryFromJSON(String name, Map json) {

        RData rData = null;

        String type = (String) json.get("type");
        String rclass = (String) json.get("rclass");
        rData = new RDataImpl(name, type, rclass);
        log.debug("buildSummaryFromJSON: name=" + name + " returning=" + rData);
        return rData;
    }

    /**
     * Facilitate the building of valid Phoenix JSON.
     */
    private static Object getValueForJSON(RData rData) {

        if (rData instanceof RNumeric) {
            return ((RNumeric) rData).getValue();
        } else if (rData instanceof RString) {
            return ((RString) rData).getValue();
        } else if (rData instanceof RBoolean) {
            return ((RBoolean) rData).getValue();
        } else if (rData instanceof RNumericVector) {
            return ((RNumericVector) rData).getValue();
        } else if (rData instanceof RStringVector) {
            return ((RStringVector) rData).getValue();
        } else if (rData instanceof RDateVector) {
            RDateVector rDateVector = (RDateVector) rData;
            SimpleDateFormat sdf = new SimpleDateFormat(rDateVector.getFormat());
            List<Date> dates = rDateVector.getValue();
            List<String> dateValues = new ArrayList<String>();
            Iterator iter = dates.iterator();
            while (iter.hasNext()) {
                Date date = (Date) iter.next();
                String dateString = null;
                try {
                    dateString = sdf.format(date);
                } catch (Exception ex) {
                }
                dateValues.add(dateString);
            }
            return dateValues;
        } else if (rData instanceof RBooleanVector) {
            return ((RBooleanVector) rData).getValue();
        } else if (rData instanceof RNumericMatrix) {
            return ((RNumericMatrix) rData).getValue();
        } else if (rData instanceof RStringMatrix) {
            return ((RStringMatrix) rData).getValue();
        } else if (rData instanceof RBooleanMatrix) {
            return ((RBooleanMatrix) rData).getValue();
        } else if (rData instanceof RList) {
            RListImpl pList = (RListImpl) rData;
            List<Map> pListItems = new ArrayList<Map>();
            for (RData pChild : pList.getValue()) {
                pListItems.add(mapPDataValues(pChild, true));
            }
            return pListItems;
        } else if (rData instanceof RDataFrame) {
            RDataFrame pFrame = (RDataFrame) rData;
            List<Map> pFrameItems = new ArrayList<Map>();
            for (RData pChild : pFrame.getValue()) {
                pFrameItems.add(mapPDataValues(pChild, true));
            }
            return pFrameItems;
        } else if (rData instanceof RFactor) {
            return ((RFactor) rData).getValue();
        } else if (rData instanceof RDate) {
            RDate rDate = (RDate) rData;
            String format = rDate.getFormat();
            Date date = rDate.getValue();
            SimpleDateFormat sdf = new SimpleDateFormat(rDate.getFormat());
            String dateString = null;
            try {
                dateString = sdf.format(date);
            } catch (Exception ex) {
            }
            return dateString;
        } else
            return null;
    }

    /**
     * Facilitate the building of valid Phoenix JSON.
     */
    private static Map mapPDataValues(RData rData) {
        return mapPDataValues(rData, false);
    }

    private static Map mapPDataValues(RData rData, boolean mapForOrderedCollection) {
        Map<String, Object> pValues = new HashMap<String, Object>();
        if (mapForOrderedCollection)
            pValues.put("name", rData.getName());
        pValues.put("type", rData.getType());
        pValues.put("rclass", rData.getRclass());
        pValues.put("value", getValueForJSON(rData));

        if (rData instanceof RDate) {
            RDate rDate = (RDate) rData;
            pValues.put("format", rDate.getFormat());
        } else if (rData instanceof RDateVector) {
            RDateVector rDateVector = (RDateVector) rData;
            pValues.put("format", rDateVector.getFormat());
        } else if (rData instanceof RFactor) {
            RFactor pFactor = (RFactor) rData;
            if (pFactor.getLevels() != null)
                pValues.put("levels", pFactor.getLevels());
            if (pFactor.getLabels() != null)
                pValues.put("labels", pFactor.getLabels());
            pValues.put("ordered", pFactor.isOrdered());
        }

        return pValues;
    }
}
