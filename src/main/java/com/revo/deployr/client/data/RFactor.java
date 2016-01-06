/*
 * RFactor.java
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

import java.util.List;

/**
 * Represents an R factor object.
 */
public interface RFactor extends RData {

    /**
     * Gets the List value for this RData.
     *
     * @return List value
     * @see RData
     */
    public List getValue();

    /**
     * Gets the List levels for this RData.
     *
     * @return List levels
     * @see RData
     */
    public List getLevels();

    /**
     * Gets the List labels for this RData.
     *
     * @return List labels
     * @see RData
     */
    public List getLabels();

    /**
     * Gets the boolean ordered for this RData.
     *
     * @return boolean value
     * @see RData
     */
    public boolean isOrdered();
}
