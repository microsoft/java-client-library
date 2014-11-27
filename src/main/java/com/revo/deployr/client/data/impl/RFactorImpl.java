/*
 * RFactorImpl.java
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

import com.revo.deployr.client.data.RFactor;

import java.util.List;

public class RFactorImpl implements RFactor {

    private String name;
    private List value;
    private List levels;
    private List labels;
    private String rclass = "factor";
    private boolean ordered;
    private String type = "factor";

    public RFactorImpl(String name, List value, boolean ordered) {
        this(name, value, null, null, ordered, "factor");
    }

    public RFactorImpl(String name, List value, boolean ordered, String rclass) {
        this(name, value, null, null, ordered, rclass);
    }

    public RFactorImpl(String name, List value, List levels, List labels, boolean ordered) {
        this(name, value, levels, labels, ordered, "factor");
    }

    public RFactorImpl(String name, List value, List levels, List labels, boolean ordered, String rclass) {
        this.name = name;
        this.rclass = rclass;
        this.value = value;
        this.levels = levels;
        this.labels = labels;
        this.ordered = ordered;
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

    public List getValue() {
        return value;
    }

    public List getLevels() {
        return levels;
    }

    public List getLabels() {
        return labels;
    }

    public boolean isOrdered() {
        return ordered;
    }
}
