/*
 * RNumericMatrixImpl.java
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
package com.revo.deployr.client.data.impl;

import com.revo.deployr.client.data.RNumericMatrix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RNumericMatrixImpl implements RNumericMatrix {

    private String name;
    private List<List<Double>> value;
    private String type = "matrix";
    private String rclass = "matrix";

    public RNumericMatrixImpl(String name, List<List<Double>> value) {
        this.name = name;

        List<List<Double>> cleansed = null;

        if (value != null) {
            cleansed = new ArrayList();

            Iterator iter = value.iterator();

            while (iter.hasNext()) {

                List<Double> cleansedInner = new ArrayList<Double>();

                List<Double> innerList = (List<Double>) iter.next();
                Iterator innerIter = innerList.iterator();

                while (innerIter.hasNext()) {

                    Object item = innerIter.next();
                    if (item instanceof Integer) {
                        cleansedInner.add((Double) ((Integer) item).doubleValue());
                    } else {
                        cleansedInner.add((Double) item);
                    }
                }

                cleansed.add(cleansedInner);
            }
        }

        this.value = cleansed;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getRclass() {
        return rclass;
    }

    public List<List<Double>> getValue() {
        return value;
    }

}
