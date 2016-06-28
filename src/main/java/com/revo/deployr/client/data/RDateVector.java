/*
 * RDateVector.java
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
package com.revo.deployr.client.data;

import java.util.Date;
import java.util.List;

/**
 * Represents an R vector of date values.
 */
public interface RDateVector extends RData {

    /**
     * Gets the List of Date values for this RData.
     *
     * @return List&lt;Date&gt; value
     * @see RData
     */
    public List<Date> getValue();

    /**
     * Gets the String date format for this RDateVector.
     *
     * @return String date format
     * @see RData
     */
    public String getFormat();

}
