/*
 * RNumericVectorImpl.java
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
package com.revo.deployr.client.data.impl;

import com.revo.deployr.client.data.RNumericVector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RNumericVectorImpl implements RNumericVector {

    private String name;
    private List<Double> value;
    private String type = "vector";
    private String rclass = "numeric";

    public RNumericVectorImpl(String name, List<Double> value) {
        this.name = name;

        List<Double> cleansed = null;
        if (value != null) {
            cleansed = new ArrayList<Double>();
            Iterator iter = value.iterator();
            while (iter.hasNext()) {
                Object item = iter.next();
                if (item instanceof Integer) {
                    cleansed.add((Double) ((Integer) item).doubleValue());
                } else {
                    cleansed.add((Double) item);
                }
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

    public List<Double> getValue() {
        return value;
    }
}
