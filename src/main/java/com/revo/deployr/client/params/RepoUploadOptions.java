/*
 * RepoUploadOptions.java
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

import java.util.Map;

/**
 * Repo upload options.
 */
public class RepoUploadOptions extends RepoAccessControlOptions {

    public RepoUploadOptions() {
    }

    /**
     * Repository file name.
     */
    public String filename;

    /**
     * Repository directory name.
     */
    public String directory;

    /**
     * Repository file description.
     */
    public String descr;

    /**
     * Repository file new version on upload.
     */
    public boolean newversion;

    /**
     * Repository file new version msg on upload.
     * Can be used when newversion property is enabled.
     */
    public String newversionmsg;

    /**
     * Repository file (script) inputs.
     */
    public String inputs;

    /**
     * Repository file (script) outputs.
     */
    public String outputs;

}
