/*
 * StandardExecutionModelCall.java
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
package com.revo.deployr.client.call;

import com.revo.deployr.client.RDataException;
import com.revo.deployr.client.params.ProjectExecutionOptions;
import com.revo.deployr.client.util.RDataUtil;

/**
 * Provides support for the standard execution model parameters on
 * all DeployR Execution APIs.
 */
public abstract class StandardExecutionModelCall extends AbstractCall {

    public StandardExecutionModelCall(ProjectExecutionOptions options) throws RDataException {

        if (options != null) {

            if (options.rinputs != null) {
                String markup = RDataUtil.toJSON(options.rinputs);
                httpParams.put("inputs", markup);
            }
            httpParams.put("csvinputs", options.csvrinputs);

            String objectNames = null;
            if (options.routputs != null) {
                for (String object : options.routputs) {
                    if (objectNames != null) {
                        objectNames = objectNames + "," + object;
                    } else {
                        objectNames = object;
                    }
                }
            }
            httpParams.put("robjects", objectNames);
            httpParams.put("tag", options.tag);
            httpParams.put("enableConsoleEvents", Boolean.toString(options.enableConsoleEvents));
            httpParams.put("echooff", Boolean.toString(options.echooff));
            httpParams.put("consoleoff", Boolean.toString(options.consoleoff));
            httpParams.put("artifactsoff", Boolean.toString(options.artifactsoff));
            httpParams.put("encodeDataFramePrimitiveAsVector", Boolean.toString(options.encodeDataFramePrimitiveAsVector));

            if (options.preloadWorkspace != null) {
                httpParams.put("preloadobjectname", options.preloadWorkspace.filename);
                httpParams.put("preloadobjectdirectory", options.preloadWorkspace.directory);
                httpParams.put("preloadobjectauthor", options.preloadWorkspace.author);
                httpParams.put("preloadobjectversion", options.preloadWorkspace.version);
            }

            if (options.preloadDirectory != null) {
                httpParams.put("preloadfilename", options.preloadDirectory.filename);
                httpParams.put("preloadfiledirectory", options.preloadDirectory.directory);
                httpParams.put("preloadfileauthor", options.preloadDirectory.author);
                httpParams.put("preloadfileversion", options.preloadDirectory.version);
            }

            httpParams.put("preloadbydirectory", options.preloadByDirectory);

            if (options.adoptionOptions != null) {
                httpParams.put("adoptworkspace", options.adoptionOptions.adoptWorkspace);
                httpParams.put("adoptdirectory", options.adoptionOptions.adoptDirectory);
                httpParams.put("adoptpackages", options.adoptionOptions.adoptPackages);
            }

            if (options.storageOptions != null) {
                httpParams.put("storefile", options.storageOptions.files);
                httpParams.put("storeobject", options.storageOptions.objects);
                httpParams.put("storeworkspace", options.storageOptions.workspace);
                httpParams.put("storedirectory", options.storageOptions.directory);
                httpParams.put("storenewversion", Boolean.toString(options.storageOptions.newversion));
                httpParams.put("storepublic", Boolean.toString(options.storageOptions.published));
            }

            httpParams.put("nan", options.nan);
            httpParams.put("infinity", options.infinity);

            httpParams.put("graphics", options.graphicsDevice);
            httpParams.put("graphicswidth", Integer.toString(options.graphicsWidth));
            httpParams.put("graphicsheight", Integer.toString(options.graphicsHeight));

            httpParams.put("phantom", Boolean.toString(options.phantom));

        }
        httpParams.put("format", "json");
    }

}
