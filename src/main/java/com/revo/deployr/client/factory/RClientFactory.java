/*
 * RClientFactory.java
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
package com.revo.deployr.client.factory;

import com.revo.deployr.client.RClient;
import com.revo.deployr.client.RClientException;
import com.revo.deployr.client.RSecurityException;
import com.revo.deployr.client.core.impl.RClientImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Create connection at the specified DeployR URL.
 */
public class RClientFactory {

    private static Log log = LogFactory.getLog(RClientFactory.class);

    /**
     * Create connection at the specified DeployR URL.
     * <p>
     * Concurrent call limit defaults to 200.
     * </p>
     *
     * @param deployrUrl url address of DeployR Server
     * @return {@link com.revo.deployr.client.RClient}
     */
    public static RClient createClient(String deployrUrl)
            throws RClientException, RSecurityException {
        log.debug("RClientFactory: createClient, deployrUrl=" + deployrUrl);
        return new RClientImpl(deployrUrl);
    }

    /**
     * Create connection at the specified DeployR URL.
     * <p>
     * Concurrent call limit defaults to 200.
     * </p>
     *
     * @param deployrUrl          url address of DeployR Server
     * @param concurrentCallLimit beyond which DeployR API calls are queued for execution
     * @return {@link com.revo.deployr.client.RClient}
     */
    public static RClient createClient(String deployrUrl, int concurrentCallLimit)
            throws RClientException, RSecurityException {

        log.debug("RClientFactory: createClient, deployrUrl=" +
                deployrUrl + " concurrentCallLimit=" + concurrentCallLimit);
        return new RClientImpl(deployrUrl, concurrentCallLimit);
    }

    /**
     * Create connection at the specified DeployR URL.
     *
     * @param deployrUrl          url address of DeployR Server
     * @param allowSelfSignedSSLCert when enabled, HTTPS connections using self-signed SSL certs are permitted
     * @return {@link com.revo.deployr.client.RClient}
     */
    public static RClient createClient(String deployrUrl,
                                       boolean allowSelfSignedSSLCert)
            throws RClientException, RSecurityException {

        log.debug("RClientFactory: createClient, deployrUrl=" + deployrUrl +
            ", allowSelfSignedSSLCert=" + allowSelfSignedSSLCert);
        return new RClientImpl(deployrUrl,
                               200,
                               allowSelfSignedSSLCert);
    }

    /**
     * Create connection at the specified DeployR URL.
     *
     * @param deployrUrl          url address of DeployR Server
     * @param concurrentCallLimit beyond which DeployR API calls are queued for execution
     * @param allowSelfSignedSSLCert when enabled, HTTPS connections using self-signed SSL certs are permitted
     * @return {@link com.revo.deployr.client.RClient}
     */
    public static RClient createClient(String deployrUrl,
                                       int concurrentCallLimit,
                                       boolean allowSelfSignedSSLCert)
            throws RClientException, RSecurityException {

        log.debug("RClientFactory: createClient, deployrUrl=" + deployrUrl +
            ", concurrentCallLimit=" + concurrentCallLimit +
            ", allowSelfSignedSSLCert=" + allowSelfSignedSSLCert);
        return new RClientImpl(deployrUrl,
                               concurrentCallLimit,
                               allowSelfSignedSSLCert);
    }

}
