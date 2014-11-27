/*
 * RDate.java
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
package com.revo.deployr.client.data;

import java.util.Date;

/**
 * Represents an R date object.
 */
public interface RDate extends RData {

    /**
     * Gets the Date value for this RData.
     *
     * @return Date value
     * @see RData
     */
    public Date getValue();

    /**
     * Gets the String date format for this RDate.
     *
     * @return String date format
     * @see RData
     */
    public String getFormat();
}
