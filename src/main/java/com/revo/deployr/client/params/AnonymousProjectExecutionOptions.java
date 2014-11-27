/*
 * AnonymousProjectExecutionOptions.java
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
package com.revo.deployr.client.params;

import java.util.List;
import com.revo.deployr.client.data.RData;

/**
 * Anonymous project execution options.
 */
public class AnonymousProjectExecutionOptions extends ProjectExecutionOptions {

    public AnonymousProjectExecutionOptions() {
    }

    /**
     * When true, the execution will occur on the HTTP blackbox project
     * for the current HTTP session.
     *
     * A HTTP blackbox projects is a stateful R session
     * available to an anonymous user. A HTTP blackbox project is
     * useful when an application needs to maintain the same R session
     * for the duration of an anonymous user's HTTP session. 
     */
    public boolean blackbox;

    /**
     * When true, the R session associated with the
     * HTTP blackbox project on the current HTTP session
     * is recycled before the execution takes place.
     *
     * Recycling an R session deletes all R objects from
     * the workspace and all files from the working directory.
     * The ability to recycle a HTTP blackbox project gives
     * an anonymous user control over the R session lifecycle.
     */
    public boolean recycle;

}
