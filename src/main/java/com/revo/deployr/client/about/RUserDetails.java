/*
 * RUserDetails.java
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
package com.revo.deployr.client.about;

/**
 * Managed user details.
 */

public class RUserDetails {

    public RUserDetails(String username, String displayname, String cookie, RUserLimitDetails limits) {

        this.username = username;
        this.displayname = displayname;
        this.cookie = cookie;
        this.limits = limits;
    }

    /**
     * Username for currently authenticated user.
     */
    public final String username;

    /**
     * Displayname for currently authenticated user.
     */
    public final String displayname;

    /**
     * Custom cookie for currently authenticated user.
     */
    public final String cookie;

    /**
     * About limits for currently authenticated user.
     */
    public final RUserLimitDetails limits;
}
