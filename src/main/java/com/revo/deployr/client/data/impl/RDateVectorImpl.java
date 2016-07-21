/*
 * RDateVectorImpl.java
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

import com.revo.deployr.client.data.RDateVector;

import java.util.Date;
import java.util.List;

public class RDateVectorImpl implements RDateVector {

    private String name;
    private List<Date> value;
    private String type = "vector";
    private String rclass = "Date";
    private String format = "yyyy-MM-dd";

    public RDateVectorImpl(String name, List<Date> value, String format) {
        this.name = name;
        this.value = value;
        if (format != null) {
            String derivedRClass = "Date";
            if (format.toLowerCase().indexOf("hh") != -1) {
                derivedRClass = "POSIXct";
            }
            this.format = format;
            this.rclass = derivedRClass;
        }
    }

    public RDateVectorImpl(String name, List<Date> value, String format, String rclass) {
        this.name = name;
        this.value = value;
        this.format = format;
        this.rclass = rclass;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getFormat() {
        return format;
    }

    public String getRclass() {
        return rclass;
    }

    public List<Date> getValue() {
        return value;
    }
}
