/*
 * RBooleanMatrixImpl.java
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

import com.revo.deployr.client.data.RBooleanMatrix;

import java.util.List;

public class RBooleanMatrixImpl implements RBooleanMatrix {

    private String name;
    private List<List<Boolean>> value;
    private String type = "matrix";
    private String rclass = "matrix";

    public RBooleanMatrixImpl(String name, List<List<Boolean>> value) {
        this.name = name;
        this.value = value;
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

    public List<List<Boolean>> getValue() {
        return value;
    }
}
