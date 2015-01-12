/*
 * RClientImpl.java
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
package com.revo.deployr.client.core.impl;

import com.revo.deployr.client.RClient;
import com.revo.deployr.client.RUser;
import com.revo.deployr.client.core.REndpoints;
import com.revo.deployr.client.call.RCall;
import com.revo.deployr.client.call.AbstractCall;
import com.revo.deployr.client.call.user.LoginCall;
import com.revo.deployr.client.call.user.LogoutCall;
import com.revo.deployr.client.call.user.AboutCall;
import com.revo.deployr.client.call.user.AutosaveCall;
import com.revo.deployr.client.call.project.ProjectCreateCall;
import com.revo.deployr.client.call.repository.RepositoryScriptExecuteCall;
import com.revo.deployr.client.call.repository.RepositoryShellExecuteCall;
import com.revo.deployr.client.call.repository.RepositoryScriptInterruptCall;
import com.revo.deployr.client.RClientException;
import com.revo.deployr.client.RDataException;
import com.revo.deployr.client.RSecurityException;
import com.revo.deployr.client.RGridException;
import com.revo.deployr.client.auth.RAuthentication;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;

import com.revo.deployr.client.core.RCoreResponse;
import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.core.RClientExecutor;

import com.revo.deployr.client.data.RData;
import com.revo.deployr.client.util.RDataUtil;

import com.revo.deployr.client.params.AnonymousProjectExecutionOptions;

import com.revo.deployr.client.RProjectExecution;
import com.revo.deployr.client.RScriptExecution;
import com.revo.deployr.client.RRepositoryDirectory;

import com.revo.deployr.client.about.RUserDetails;
import com.revo.deployr.client.about.RUserLimitDetails;
import com.revo.deployr.client.about.RProjectDetails;
import com.revo.deployr.client.about.RProjectExecutionDetails;

import com.revo.deployr.client.util.REntityUtil;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.utils.URIBuilder;

import java.util.*;
import java.net.*;

import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ExecutorService;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory; 

public class RClientImpl implements RClient, RClientExecutor {

    private Log log = LogFactory.getLog(RClientImpl.class);

    private HttpClient httpClient;
    private ExecutorService eService;
    private String serverurl;
    private String httpcookie;
    private RLiveContext liveContext;

    private ArrayList<Integer> GRID_EXCEPTION_CODES = new ArrayList<Integer>(
            Arrays.asList(910, 911, 912, 913, 914, 915, 916, 917, 918, 919));

    public RClientImpl(String serverurl)
		throws RClientException, RSecurityException {

        this(serverurl, 200);
    } 

    public RClientImpl(String serverurl, int concurrentCallLimit)
			throws RClientException, RSecurityException {

        log.debug("Creating client connection: serverurl=" + serverurl + " concurrentCallLimit=" + concurrentCallLimit);

        this.serverurl = serverurl;

        // Create and initialize HTTP parameters
        HttpParams httpParams = new BasicHttpParams();
        // Set Infinite Connection and Socket Timeouts.
        HttpConnectionParams.setConnectionTimeout(httpParams, 0);
        HttpConnectionParams.setSoTimeout(httpParams, 0);
        ConnManagerParams.setMaxTotalConnections(httpParams, concurrentCallLimit);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(concurrentCallLimit));
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);

        // Create and initialize scheme registry 
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(
                new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        // Create a HttpClient with the ThreadSafeClientConnManager.
        // This connection manager must be used if more than one thread will
        // be using the HttpClient.
        ClientConnectionManager cm =
	    new ThreadSafeClientConnManager(httpParams, schemeRegistry);

        httpClient = new DefaultHttpClient(cm, httpParams);

        // Enable cookie handling by setting cookie policy on HttpClient.
        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);

        log.debug("Created client connection: httpClient=" + httpClient);

        eService = Executors.newCachedThreadPool();

    }

    public RUser login(RAuthentication rAuthentication)
        throws RClientException, RSecurityException {

        return login(rAuthentication, false);
    }

    public RUser login(RAuthentication rAuthentication, boolean disableautosave)
        throws RClientException, RSecurityException {

        RCall rCall = null;

        if(rAuthentication instanceof RBasicAuthentication) {

            RBasicAuthentication basic = (RBasicAuthentication) rAuthentication;	
            rCall = new LoginCall(basic.getUsername(), basic.getPassword(), disableautosave);
        }

        RCoreResult rResult = processCall(rCall);

        Map<String, String> identity = rResult.getIdentity();
        Map<String, Integer> limits = rResult.getLimits();

        this.httpcookie = rResult.getCookie();

        RUserDetails userDetails = REntityUtil.getUserDetails(identity, limits);

        liveContext = new RLiveContext(this, serverurl, httpcookie);
        return new RUserImpl(userDetails, liveContext);
    }

    public void logout(RUser user) throws RClientException, RSecurityException {

        RUserDetails about = (user != null) ? ((RUserImpl) user).about : null;
        RCall rCall = new LogoutCall(about);
        processCall(rCall);
    }

    public RScriptExecution executeScript(String scriptName,
                                          String scriptAuthor,
                                          String scriptVersion)
        throws RClientException,
               RSecurityException,
               RDataException,
               RGridException {
        return executeScript(scriptName,
                             RRepositoryDirectory.ROOT,
                             scriptAuthor,
                             scriptVersion,
                             null);
    }

    public RScriptExecution executeScript(String scriptName,
                                          String scriptAuthor,
                                          String scriptVersion,
                                          AnonymousProjectExecutionOptions executionOptions)
        throws RClientException,
               RSecurityException,
               RDataException,
               RGridException {
        return executeScript(scriptName,
                             RRepositoryDirectory.ROOT,
                             scriptAuthor,
                             scriptVersion,
                             executionOptions);
    }

    public RScriptExecution executeScript(String scriptName,
                                          String scriptDirectory,
                                          String scriptAuthor,
                                          String scriptVersion)
        throws RClientException,
               RSecurityException,
               RDataException,
               RGridException {
        return executeScript(scriptName, scriptDirectory, scriptAuthor, scriptVersion, null);
    }

    public RScriptExecution executeScript(String scriptName,
                                          String scriptDirectory,
                                          String scriptAuthor,
                                          String scriptVersion,
                                          AnonymousProjectExecutionOptions executionOptions)
        throws RClientException,
               RSecurityException,
               RDataException,
               RGridException {

        RCall rCall = new RepositoryScriptExecuteCall(scriptName,
                                                      scriptDirectory,
                                                      scriptAuthor,
                                                      scriptVersion,
                                                      null,
                                                      executionOptions);

        RCoreResult rResult = processCallOnGrid(rCall);

        Map projectMap = rResult.getProject();
        RProjectDetails projectDetails = REntityUtil.getProjectDetails(projectMap);

        Map execution = (Map) rResult.getExecution();
        List<RData> workspaceObjects = rResult.getRObjects();
        List<Map> repofiles = rResult.getRepoFiles();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();
        liveContext = new RLiveContext(this, serverurl, httpcookie);

        RProjectExecutionDetails execDetails =
            REntityUtil.getProjectExecutionDetails(projectDetails, execution, workspaceObjects, repofiles, error, errorCode, liveContext);
        RScriptExecution rExecution =
            new RScriptExecutionImpl(projectDetails, execDetails, liveContext);

        boolean success = rResult.isSuccess();

        log.debug("executeScript: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return rExecution;
    }

    public RScriptExecution executeExternal(String externalSource,
                                            AnonymousProjectExecutionOptions executionOptions)
        throws RClientException,
               RSecurityException,
               RDataException,
               RGridException {

        RCall rCall = new RepositoryScriptExecuteCall(null, null, null, null,
                                                      externalSource, executionOptions);
        RCoreResult rResult = processCallOnGrid(rCall);

        Map projectMap = rResult.getProject();
        RProjectDetails projectDetails = REntityUtil.getProjectDetails(projectMap);

        Map execution = (Map) rResult.getExecution();
        List<RData> workspaceObjects = rResult.getRObjects();
        List<Map> repofiles = rResult.getRepoFiles();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();
        liveContext = new RLiveContext(this, serverurl, httpcookie);

        RProjectExecutionDetails execDetails =
            REntityUtil.getProjectExecutionDetails(projectDetails, execution, workspaceObjects, repofiles, error, errorCode, liveContext);
        RScriptExecution rExecution =
            new RScriptExecutionImpl(projectDetails, execDetails, liveContext);

        boolean success = rResult.isSuccess();

        log.debug("executeExternal: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return rExecution;
    }

    public URL renderScript(String scriptName,
                            String scriptDirectory,
                            String scriptAuthor,
                            String scriptVersion,
                            AnonymousProjectExecutionOptions options)
        throws RClientException,
               RDataException {

        URL renderURL;

        try {

            String urlPath = serverurl + REndpoints.RREPOSITORYSCRIPTRENDER;
            if(httpcookie != null) {
                urlPath += ";jsessionid=" + httpcookie;
            }
            URIBuilder builder = new URIBuilder(urlPath);

            builder.addParameter("filename", scriptName);
            builder.addParameter("directory", scriptDirectory);
            builder.addParameter("author", scriptAuthor);

            if(scriptVersion != null) {
                builder.addParameter("version", scriptVersion);
            }

            if(options != null) {

                builder.addParameter("blackbox", Boolean.toString(options.blackbox));
                builder.addParameter("recycle", Boolean.toString(options.recycle));

                if(options.rinputs != null) {
                    String markup = RDataUtil.toJSON(options.rinputs);
                    builder.addParameter("inputs", markup);
                }

                String objectNames = null;
                if(options.routputs != null) {
                    for(String object : options.routputs) {
                        if(objectNames != null) {
                            objectNames = objectNames + "," + object;
                        } else {
                            objectNames = object;
                        }
                    }
                }
                builder.addParameter("robjects", objectNames);

                builder.addParameter("tag", options.tag);
                builder.addParameter("echooff", Boolean.toString(options.echooff));
                builder.addParameter("consoleoff", Boolean.toString(options.consoleoff));
                builder.addParameter("encodeDataFramePrimitiveAsVector", Boolean.toString(options.encodeDataFramePrimitiveAsVector));

                if(options.preloadWorkspace != null) {
                    builder.addParameter("preloadobjectname", options.preloadWorkspace.filename);
                    builder.addParameter("preloadobjectauthor", options.preloadWorkspace.author);
                    builder.addParameter("preloadobjectversion", options.preloadWorkspace.version);
                }

                if(options.preloadDirectory != null) {
                    builder.addParameter("preloadfilename", options.preloadWorkspace.filename);
                    builder.addParameter("preloadfileauthor", options.preloadWorkspace.author);
                    builder.addParameter("preloadfileversion", options.preloadWorkspace.version);
                }

                if(options.adoptionOptions != null) {
                    builder.addParameter("adoptworkspace", options.adoptionOptions.adoptWorkspace);
                    builder.addParameter("adoptdirectory", options.adoptionOptions.adoptDirectory);
                    builder.addParameter("adoptpackages", options.adoptionOptions.adoptPackages);
                }

                if(options.storageOptions != null) {
                    builder.addParameter("storefile", options.storageOptions.files);
                    builder.addParameter("storeobjct", options.storageOptions.objects);
                    builder.addParameter("storeworkspace", options.storageOptions.workspace);
                    builder.addParameter("storenewversion", Boolean.toString(options.storageOptions.newversion));
                    builder.addParameter("storepublic", Boolean.toString(options.storageOptions.published));
                }

                builder.addParameter("nan", options.nan);
                builder.addParameter("infinity", options.infinity);
                builder.addParameter("graphics", options.graphicsDevice);
                builder.addParameter("graphicswidth", Integer.toString(options.graphicsWidth));
                builder.addParameter("graphicsheight", Integer.toString(options.graphicsHeight));

            }

            renderURL = builder.build().toURL();
        } catch(Exception uex) {
            throw new RClientException("Render url: ex=" + uex.getMessage());
        }
        return renderURL;
    }

    public List<String> executeShell(String shellName,
                                     String shellDirectory,
                                     String shellAuthor,
                                     String shellVersion,
                                     String shellArgs)
            throws RClientException,
            RSecurityException,
            RDataException {

        RCall rCall = new RepositoryShellExecuteCall(shellName,
                                                     shellDirectory,
                                                     shellAuthor,
                                                     shellVersion,
                                                     shellArgs);
        RCoreResult rResult = processCall(rCall);

        List<String> repoShellConsoleOutput =
            rResult.getRepoShellConsoleOutput();

        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();
        boolean success = rResult.isSuccess();

        log.debug("executeShell: success=" + success +
                " error=" + error + " errorCode=" + errorCode);

        return repoShellConsoleOutput;
    }

    public void interruptScript() throws RClientException, RSecurityException {

        RCall rCall = new RepositoryScriptInterruptCall();
        RCoreResult rResult = processCall(rCall);

        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();
        boolean success = rResult.isSuccess();

        log.debug("interruptScript: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

    public void release() {

        try {
            logout(null);
        } catch(Exception ex) {
            // Ignore any exceptions on forced logout operation.
        }

        try {
            eService.shutdown();
        } catch (Exception ex) {
            log.debug("Exception shutting down the executor service.", ex);
        }

        log.debug("RClient eService.isShutdown=" + eService.isShutdown());

        try {
            httpClient.getConnectionManager().shutdown();
        } catch (Exception ex) {
            log.debug("Exception shutting down the HTTP connection manager.", ex);
        }

        log.debug("RClient shutdown completes.");
    }

    // RClientExecutor interface implementation.

    public RCoreResult processCall(RCall rCall)
		throws RClientException, RSecurityException {

        RCoreResponse pResponse = execute(rCall);

        RCoreResult rResult = null;

        try {
            rResult = pResponse.get();

            log.debug("processCall: pCoreResult.statusMsg=" +
                                    rResult.getStatusMsg() +
                                    ", statusCode=" + rResult.getStatusCode());

            if(rResult.getStatusCode() == 200) {
                if(!rResult.isSuccess())
                throw new RClientException(rResult.getError());
            } else
            if(rResult.getStatusCode() == 401) {
                throw new RSecurityException("Unauthenticated call.", 401);
            } else
            if(rResult.getStatusCode() == 403) {
                throw new RSecurityException("Unauthorized call.", 403);
            } else
            if(rResult.getStatusCode() == 409) {
                throw new RSecurityException("Conflict on call.", 409);
            } else {
                throw new RClientException(rResult.getStatusMsg(),
                                        rResult.getStatusCode());
            }

        } catch(RSecurityException psex) {
            throw psex;
        } catch(RClientException pcex) {
            throw pcex;
        } catch(Exception ex) {
            throw new RClientException(ex.getMessage(), ex);
        }

        return rResult;
    }

    public RCoreResult processCallOnGrid(RCall rCall)
		throws RClientException,
               RSecurityException,
               RGridException {

        RCoreResponse pResponse = execute(rCall);

        RCoreResult rResult = null;

        try {
            rResult = pResponse.get();

            log.debug("processCallOnGrid: pCoreResult.statusMsg=" +
                                    rResult.getStatusMsg() +
                                    ", statusCode=" + rResult.getStatusCode());

            if(rResult.getStatusCode() == 200) {

                if(!rResult.isSuccess()) {

                   if(GRID_EXCEPTION_CODES.contains(rResult.getErrorCode())) {
                        throw new RGridException(rResult.getError(),
                                                        rResult.getErrorCode());
                    } else {
                        throw new RClientException(rResult.getError(), rResult.getErrorCode());
                    }
                }

            } else
            if(rResult.getStatusCode() == 401) {
                throw new RSecurityException("Unauthenticated call.", 401);
            } else
            if(rResult.getStatusCode() == 403) {
                throw new RSecurityException("Unauthorized call.", 403);
            } else
            if(rResult.getStatusCode() == 409) {
                throw new RSecurityException("Conflict on call.", 409);
            } else {
                 throw new RClientException(rResult.getError(),
                                        rResult.getErrorCode());
            }

        } catch(RSecurityException secex) {
            throw secex;
        } catch(RClientException cex) {
            if(GRID_EXCEPTION_CODES.contains(rResult.getErrorCode())) {
                throw new RGridException(rResult.getError(),
                                        rResult.getErrorCode());
            } else {
                throw cex;
            }

        } catch(RGridException gex) {
            throw gex;
        } catch(Exception ex) {
            throw new RClientException(ex.getMessage(), ex);
        }

        return rResult;
    }

    // Private method implementations.

    public RCoreResponse execute(RCall call) {

        AbstractCall abstractCall = (AbstractCall) call;
        // Provide httpClient and DeployR server url context to RCall.
        abstractCall.setClient(httpClient, serverurl);
        Callable callable = (Callable) call;
        // Wrap Callable in FutureTask for execution by the Executor Service.
        FutureTask task = new FutureTask(callable);
        abstractCall.setFuture(task);
        eService.submit(task);

        return (RCoreResponse) call;
    }
}
