/*
 * RUserJobCalls.java
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
package com.revo.deployr.client.api;

import com.revo.deployr.client.RClientException;
import com.revo.deployr.client.RDataException;
import com.revo.deployr.client.RJob;
import com.revo.deployr.client.RSecurityException;
import com.revo.deployr.client.params.JobExecutionOptions;

import java.util.List;

/**
 * Defines job related interfaces for DeployR-managed user.
 */
public interface RUserJobCalls {

    /**
     * List jobs.
     *
     * @return List<RJob>
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public List<RJob> listJobs()
            throws RClientException, RSecurityException;

    /**
     * Query an existing job by job identifier.
     *
     * @return RJob
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RJob queryJob(String job)
            throws RClientException, RSecurityException;

    /**
     * Submit job based on block of R code.
     *
     * @return RJob
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RJob submitJobCode(String jobName, String jobDescr, String code)
            throws RClientException, RSecurityException, RDataException;

    /**
     * Submit job based on block of R code.
     *
     * @return RJob
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RJob submitJobCode(String jobName,
                              String jobDescr,
                              String code,
                              JobExecutionOptions options)
            throws RClientException, RSecurityException, RDataException;

    /**
     * Submit a single repository-managed script or a chain of
     * repository-managed scripts (found in the root directory)
     * to execute as a job.
     * <p/>
     * To submit a chain of repository-managed scripts on this call provide a comma-separated
     * list of values on the scriptName, scriptAuthor and optionally scriptVersion parameters.
     * Chained execution executes each of the scripts identified on the call in a sequential
     * fashion on the R session, with execution occuring in the order specified on the parameter list.
     *
     * @return RJob
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @deprecated As of release 7.1, use submitJobScript method that
     * supports scriptDirectory parameter. This deprecated call assumes
     * each script is found in the root directory.
     */
    @Deprecated
    public RJob submitJobScript(String jobName,
                                String jobDescr,
                                String scriptName,
                                String scriptAuthor,
                                String scriptVersion)
            throws RClientException, RSecurityException, RDataException;

    /**
     * Submit a single repository-managed script or a chain of
     * repository-managed scripts to execute as a job.
     * <p/>
     * To submit a chain of repository-managed scripts on this call provide a comma-separated
     * list of values on the scriptName, scriptAuthor and optionally scriptVersion parameters.
     * Chained execution executes each of the scripts identified on the call in a sequential
     * fashion on the R session, with execution occuring in the order specified on the parameter list.
     *
     * @return RJob
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     * @deprecated As of release 7.1, use submitJobScript method that
     * supports scriptDirectory parameter. This deprecated call assumes
     * each script is found in the root directory.
     */
    @Deprecated
    public RJob submitJobScript(String jobName,
                                String jobDescr,
                                String scriptName,
                                String scriptAuthor,
                                String scriptVersion,
                                JobExecutionOptions options)
            throws RClientException, RSecurityException, RDataException;

    /**
     * Submit a single repository-managed script or a chain of repository-managed scripts
     * to execute as a job.
     * <p/>
     * To submit a chain of repository-managed scripts on this call provide a comma-separated
     * list of values on the scriptName, scriptAuthor and optionally scriptVersion parameters.
     * Chained execution executes each of the scripts identified on the call in a sequential
     * fashion on the R session, with execution occuring in the order specified on the parameter list.
     *
     * @return RJob
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RJob submitJobScript(String jobName,
                                String jobDescr,
                                String scriptName,
                                String scriptDirectory,
                                String scriptAuthor,
                                String scriptVersion)
            throws RClientException, RSecurityException, RDataException;

    /**
     * Submit a single repository-managed script or a chain of repository-managed scripts
     * to execute as a job.
     * <p/>
     * To submit a chain of repository-managed scripts on this call provide a comma-separated
     * list of values on the scriptName, scriptAuthor and optionally scriptVersion parameters.
     * Chained execution executes each of the scripts identified on the call in a sequential
     * fashion on the R session, with execution occuring in the order specified on the parameter list.
     *
     * @return RJob
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RJob submitJobScript(String jobName,
                                String jobDescr,
                                String scriptName,
                                String scriptDirectory,
                                String scriptAuthor,
                                String scriptVersion,
                                JobExecutionOptions options)
            throws RClientException, RSecurityException, RDataException;

    /**
     * Submit a single script found on a URL/path or a chain of scripts found on a set of URLs/paths
     * on the current project.
     * <p/>
     * To submit a chain of repository-managed scripts on this call provide a comma-separated
     * list of values on the externalSource parameter.
     * <p/>
     * POWER_USER privileges are required for this call.
     *
     * @return RJob
     * @throws RClientException   if RClient fails to complete call.
     * @throws RSecurityException if DeployR server security conditions not met on call.
     */
    public RJob submitJobExternal(String jobName, String jobDescr, String externalSource, JobExecutionOptions options)
            throws RClientException, RSecurityException, RDataException;

}
