/*
 * RProjectImpl.java
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

import com.revo.deployr.client.RClientException;
import com.revo.deployr.client.RDataException;
import com.revo.deployr.client.RSecurityException;
import com.revo.deployr.client.RGridException;
import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.core.RClientExecutor;
import com.revo.deployr.client.core.REndpoints;
import com.revo.deployr.client.call.RCall;

import com.revo.deployr.client.call.user.AboutCall;
import com.revo.deployr.client.call.project.ProjectRecycleCall;
import com.revo.deployr.client.call.project.ProjectPingCall;
import com.revo.deployr.client.call.project.ProjectAboutUpdateCall;
import com.revo.deployr.client.call.project.ProjectGrantCall;
import com.revo.deployr.client.call.project.ProjectSaveCall;
import com.revo.deployr.client.call.project.ProjectSaveAsCall;
import com.revo.deployr.client.call.project.ProjectCloseCall;
import com.revo.deployr.client.call.project.ProjectDeleteCall;
import com.revo.deployr.client.call.project.ProjectExecuteConsoleCall;
import com.revo.deployr.client.call.project.ProjectExecuteCodeCall;
import com.revo.deployr.client.call.project.ProjectExecuteScriptCall;
import com.revo.deployr.client.call.project.ProjectExecuteInterruptCall;
import com.revo.deployr.client.call.project.ProjectExecuteHistoryCall;
import com.revo.deployr.client.call.project.ProjectExecuteFlushCall;

import com.revo.deployr.client.call.project.ProjectExecuteResultListCall;
import com.revo.deployr.client.call.project.ProjectExecuteResultDeleteCall;

import com.revo.deployr.client.call.project.ProjectWorkspaceListCall;
import com.revo.deployr.client.call.project.ProjectWorkspaceGetCall;
import com.revo.deployr.client.call.project.ProjectWorkspaceUploadCall;
import com.revo.deployr.client.call.project.ProjectWorkspaceTransferCall;
import com.revo.deployr.client.call.project.ProjectWorkspacePushCall;
import com.revo.deployr.client.call.project.ProjectWorkspaceSaveCall;
import com.revo.deployr.client.call.project.ProjectWorkspaceStoreCall;
import com.revo.deployr.client.call.project.ProjectWorkspaceLoadCall;
import com.revo.deployr.client.call.project.ProjectWorkspaceDeleteCall;

import com.revo.deployr.client.call.project.ProjectDirectoryListCall;
import com.revo.deployr.client.call.project.ProjectDirectoryUploadCall;
import com.revo.deployr.client.call.project.ProjectDirectoryTransferCall;
import com.revo.deployr.client.call.project.ProjectDirectoryWriteCall;
import com.revo.deployr.client.call.project.ProjectDirectoryLoadCall;
import com.revo.deployr.client.call.project.ProjectPackageListCall;
import com.revo.deployr.client.call.project.ProjectPackageAttachCall;
import com.revo.deployr.client.call.project.ProjectPackageDetachCall;

import com.revo.deployr.client.RProject;
import com.revo.deployr.client.RProjectPackage;

import com.revo.deployr.client.RProjectExecution;
import com.revo.deployr.client.RProjectResult;
import com.revo.deployr.client.RProjectFile;
import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.RRepositoryDirectory;

import com.revo.deployr.client.core.impl.RProjectExecutionImpl;
import com.revo.deployr.client.core.impl.RProjectResultImpl;

import com.revo.deployr.client.data.RData;

import com.revo.deployr.client.about.RProjectDetails;
import com.revo.deployr.client.about.RProjectExecutionDetails;
import com.revo.deployr.client.about.RProjectResultDetails;
import com.revo.deployr.client.about.RProjectFileDetails;
import com.revo.deployr.client.about.RProjectPackageDetails;
import com.revo.deployr.client.about.RRepositoryFileDetails;

import com.revo.deployr.client.params.ProjectCloseOptions;
import com.revo.deployr.client.params.ProjectDropOptions;
import com.revo.deployr.client.params.ProjectExecutionOptions;
import com.revo.deployr.client.params.ProjectHistoryOptions;
import com.revo.deployr.client.params.ProjectWorkspaceOptions;
import com.revo.deployr.client.params.DirectoryUploadOptions;

import com.revo.deployr.client.util.REntityUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.io.File;
import java.io.InputStream;
import java.net.*;
import org.apache.http.client.utils.URIBuilder;


import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory; 

/**
 * DeployR Project.
 */
public class RProjectImpl implements RProject {

    private Log log = LogFactory.getLog(RProject.class);

    public RProjectDetails about;
    RLiveContext liveContext;

    public RProjectImpl(RProjectDetails about, RLiveContext liveContext) {
        this.about = about;
        this.liveContext = liveContext;
    }

    public RProjectDetails about() {
        return about;
    }

    /*
     * RProject Interfaces.
     */

    public boolean ping()
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectPingCall(about.id);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map project = rResult.getProject();
        boolean live = (Boolean) project.get("live");

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("ping: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return live;
    }

    public RProjectDetails update(RProjectDetails details)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectAboutUpdateCall(details);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map project = rResult.getProject();

        this.about = REntityUtil.getProjectDetails(project);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("update: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return this.about;
    }

    public RProjectDetails grant(String username)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectGrantCall(about.id, username);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map project = rResult.getProject();

        this.about = REntityUtil.getProjectDetails(project);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("grant: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return this.about;
    }

    public RProjectDetails save()
        throws RClientException, RSecurityException {

        return save(this.about);
    }


    public RProjectDetails save(RProjectDetails details)
        throws RClientException, RSecurityException {

        return save(details, null);
    }

    public RProjectDetails save(RProjectDetails details, ProjectDropOptions dropOptions)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectSaveCall(details, dropOptions);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map project = rResult.getProject();
        log.debug("save: rResult returns project=" + project);

        log.debug("save: rResult new about details have been updated.");

        this.about = REntityUtil.getProjectDetails(project);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("save: rResult has been parsed.");

        log.debug("save: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return this.about;
    }

    public RProject saveAs(RProjectDetails details)
        throws RClientException, RSecurityException {

        return saveAs(details, null);
    }

    public RProject saveAs(RProjectDetails details, ProjectDropOptions dropOptions)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectSaveCall(details, dropOptions);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map project = rResult.getProject();

        RProjectDetails saveasDetails = REntityUtil.getProjectDetails(project);
        RProject rProject = new RProjectImpl(saveasDetails, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("saveAs: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return rProject;
    }

    public RProjectDetails recycle()
        throws RClientException, RSecurityException {

        return recycle(false, false);
    }


    public RProjectDetails recycle(boolean preserveWorkspace,
                                   boolean preserveDirectory)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectRecycleCall(about.id,
                                             preserveWorkspace,
                                             preserveDirectory);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map project = rResult.getProject();
        log.debug("recycle: rResult returns project=" + project);

        this.about = REntityUtil.getProjectDetails(project);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("recycle: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return this.about;
    }

    public void close()
        throws RClientException, RSecurityException {

        close(null);
    }

    public void close(ProjectCloseOptions options)
        throws RClientException, RSecurityException {


        RCall rCall = new ProjectCloseCall(this.about, options);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("close: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

    public void delete()
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectDeleteCall(this.about);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("delete: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

    public InputStream export()
        throws RClientException, RSecurityException {

        try {
          String urlBase = this.liveContext.serverurl +
                                  REndpoints.RPROJECTEXPORT;
          String urlPath = urlBase + "/" + this.about.id +
              ";jsessionid=" + this.liveContext.httpcookie;
          URIBuilder builder = new URIBuilder(urlPath);
          builder.addParameter("project", this.about.id);
          return liveContext.executor.download(builder);
        } catch(Exception ex) {
            throw new RClientException("Export failed: " +
                                         ex.getMessage());
        }
    }

    /*
     * RProjectExecuteCalls Interfaces.
     */

    public RProjectExecution executeCode(String code)
        throws RClientException,
               RSecurityException,
               RDataException,
               RGridException {

        RCall rCall = new ProjectExecuteCodeCall(this.about.id, code, null);
        RCoreResult rResult = liveContext.executor.processCallOnGrid(rCall);

        Map execution = (Map) rResult.getExecution();
        List<RData> robjects = rResult.getRObjects();
        List<Map> repofiles = rResult.getRepoFiles();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        RProjectExecutionDetails about =
            REntityUtil.getProjectExecutionDetails(this.about, execution, robjects, repofiles, error, errorCode, liveContext);

        RProjectExecution rExecution =
            new RProjectExecutionImpl(this.about, about, liveContext);

        boolean success = rResult.isSuccess();

        log.debug("executeCode: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return rExecution;
    }

    public RProjectExecution executeCode(String code,
                                         ProjectExecutionOptions options)
        throws RClientException,
               RSecurityException,
               RDataException,
               RGridException {

        RCall rCall = new ProjectExecuteCodeCall(this.about.id, code, options);
        RCoreResult rResult = liveContext.executor.processCallOnGrid(rCall);

        Map execution = (Map) rResult.getExecution();
        List<RData> robjects = rResult.getRObjects();
        List<Map> repofiles = rResult.getRepoFiles();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        RProjectExecutionDetails about =
            REntityUtil.getProjectExecutionDetails(this.about,
                                                   execution,
                                                   robjects,
                                                   repofiles,
                                                   error,
                                                   errorCode,
                                                   liveContext);

        RProjectExecution rExecution =
            new RProjectExecutionImpl(this.about, about, liveContext);

        boolean success = rResult.isSuccess();

        log.debug("executeCode: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return rExecution;
    }

    public RProjectExecution executeScript(RRepositoryFile script)
        throws RClientException,
               RSecurityException,
               RDataException,
               RGridException {

        return _executeScript(script.about().filename,
                              script.about().directory,
                              script.about().author,
                              script.about().version, null);
    }

    public RProjectExecution executeScript(String scriptName,
                                           String scriptAuthor,
                                           String scriptVersion)
        throws RClientException,
               RSecurityException,
               RDataException,
               RGridException {

        return _executeScript(scriptName,
                              RRepositoryDirectory.ROOT,
                              scriptAuthor,
                              scriptVersion, null);
    }

    public RProjectExecution executeScript(String scriptName,
                                           String scriptDirectory,
                                           String scriptAuthor,
                                           String scriptVersion)
        throws RClientException,
               RSecurityException,
               RDataException,
               RGridException {

        return _executeScript(scriptName,
                              scriptDirectory,
                              scriptAuthor,
                              scriptVersion, null);
    }

    public RProjectExecution executeScript(RRepositoryFile script,
                                           ProjectExecutionOptions options)
        throws RClientException,
               RSecurityException,
               RDataException,
               RGridException {

        return _executeScript(script.about().filename,
                              script.about().directory,
                              script.about().author,
                              script.about().version, options);
    }

    public RProjectExecution executeScript(String scriptName,
                                           String scriptAuthor,
                                           String scriptVersion,
                                           ProjectExecutionOptions options)
        throws RClientException,
               RSecurityException,
               RDataException,
               RGridException {

        return _executeScript(scriptName,
                              RRepositoryDirectory.ROOT,
                              scriptAuthor,
                              scriptVersion, options);
    }

    public RProjectExecution executeScript(String scriptName,
                                           String scriptDirectory,
                                           String scriptAuthor,
                                           String scriptVersion,
                                           ProjectExecutionOptions options)
        throws RClientException,
               RSecurityException,
               RDataException,
               RGridException {

        return _executeScript(scriptName,
                              scriptDirectory,
                              scriptAuthor,
                              scriptVersion, options);
    }

    private RProjectExecution _executeScript(String scriptName,
                                             String scriptDirectory,
                                             String scriptAuthor,
                                             String scriptVersion,
                                             ProjectExecutionOptions options)
        throws RClientException,
               RSecurityException,
               RDataException,
               RGridException {

        RCall rCall = new ProjectExecuteScriptCall(this.about.id,
                                                   scriptName,
                                                   scriptDirectory,
                                                   scriptAuthor,
                                                   scriptVersion,
                                                   null,
                                                   options);

        RCoreResult rResult = liveContext.executor.processCallOnGrid(rCall);

        Map execution = (Map) rResult.getExecution();
        List<RData> robjects = rResult.getRObjects();
        List<Map> repofiles = rResult.getRepoFiles();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();
        RProjectExecutionDetails about =
            REntityUtil.getProjectExecutionDetails(this.about,
                                                   execution,
                                                   robjects,
                                                   repofiles,
                                                   error,
                                                   errorCode,
                                                   liveContext);
        RProjectExecution rExecution =
            new RProjectExecutionImpl(this.about, about, liveContext);

        boolean success = rResult.isSuccess();

        log.debug("executeScript: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return rExecution;
    }

    public RProjectExecution executeExternal(String externalSource,
                                             ProjectExecutionOptions options)
        throws RClientException,
               RSecurityException,
               RDataException,
               RGridException {

        RCall rCall = new ProjectExecuteScriptCall(this.about.id,
                                                   null, null, null, null,
                                                   externalSource, options);
        RCoreResult rResult = liveContext.executor.processCallOnGrid(rCall);

        Map execution = (Map) rResult.getExecution();
        List<RData> robjects = rResult.getRObjects();
        List<Map> repofiles = rResult.getRepoFiles();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();
        RProjectExecutionDetails about =
            REntityUtil.getProjectExecutionDetails(this.about,
                                                   execution,
                                                   robjects,
                                                   repofiles,
                                                   error,
                                                   errorCode,
                                                   liveContext);
        RProjectExecution rExecution =
            new RProjectExecutionImpl(this.about, about, liveContext);

        boolean success = rResult.isSuccess();
        log.debug("executeScript: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return rExecution;
    }

    public String getConsole()
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectExecuteConsoleCall(about.id);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        String console = rResult.getConsole();

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("getConsole: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return console;
    }

    public void interruptExecution()
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectExecuteInterruptCall(about.id);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("interruptExecution: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

    public List<RProjectExecution> getHistory()
        throws RClientException, RSecurityException {

        return getHistory(null);
    }

    public List<RProjectExecution> getHistory(ProjectHistoryOptions options)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectExecuteHistoryCall(this.about.id, options);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        List<RProjectExecution> history = new ArrayList<RProjectExecution>();

        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();
        List<Map> historyList = (List<Map>) rResult.getHistory();

        for(Map executionMap : historyList) {

            String histError = (String) executionMap.get("error");
            int histErrorCode = 0;
            if(executionMap.get("errorCode") != null) {
            histErrorCode = (Integer) executionMap.get("errorCode");
            }

            RProjectExecutionDetails about =
                REntityUtil.getProjectExecutionDetails(this.about,
                                                       executionMap,
                                                       null, null,
                                                       histError,
                                                       histErrorCode,
                                                       liveContext);
            RProjectExecution rExecution =
            new RProjectExecutionImpl(this.about, about, liveContext);

            history.add(rExecution);
        }

        boolean success = rResult.isSuccess();

        log.debug("executeCode: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return history;
    }

    public void flushHistory()
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectExecuteFlushCall(this.about.id, null);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("flushHistory: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

    public List<RProjectResult> listResults()
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectExecuteResultListCall(this.about.id, null);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

            List<Map> results = rResult.getResults();

        List<RProjectResult> resultList = new ArrayList<RProjectResult>();

        for(Map resultMap : results) {
            RProjectResultDetails resultDetails = REntityUtil.getProjectResultDetails(resultMap);
            RProjectResult result = new RProjectResultImpl(this.about, resultDetails, liveContext);
            resultList.add(result);	
        }

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("listResults: success=" + success + " error=" + error + " errorCode=" + errorCode);
        return resultList;
    }

    public void deleteResults()
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectExecuteResultDeleteCall(this.about.id, null, null);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("deleteResults: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

    public InputStream downloadResults()
        throws RClientException, RSecurityException {

      try {

        String urlPath = liveContext.serverurl +
                  REndpoints.RPROJECTEXECUTERESULTDOWNLOAD;
        urlPath = urlPath + ";jsessionid=" + liveContext.httpcookie;
        URIBuilder builder = new URIBuilder(urlPath);
        builder.addParameter("project", this.about.id);
        return liveContext.executor.download(builder);
      } catch(Exception ex) {
          throw new RClientException("Download failed: " +
                                          ex.getMessage());
      }
    }


    /*
     * RProjectWorkspaceCalls Interfaces.
     */

    public List<RData> listObjects()
        throws RClientException, RSecurityException {

        return listObjects(null);
    }

    public List<RData> listObjects(ProjectWorkspaceOptions options)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectWorkspaceListCall(this.about.id, options);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        List<RData> robjects = rResult.getRObjects();
        String error = rResult.getError();
        boolean success = rResult.isSuccess();
        int errorCode = rResult.getErrorCode();

        log.debug("listObjects: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return robjects;
    }

    public RData getObject(String objectName)
        throws RClientException, RSecurityException {

        return getObject(objectName, false);
    }

    public RData getObject(String objectName, boolean encodeDataFramePrimitiveAsVector)
        throws RClientException, RSecurityException {

        List<String> objectNames = new ArrayList<String>();
        objectNames.add(objectName);
        List<RData> objects = getObjects(objectNames, encodeDataFramePrimitiveAsVector);
        return (objects.size() > 0) ? objects.get(0) : null;
    }

    public List<RData> getObjects(List<String> objectNames)
        throws RClientException, RSecurityException {

        return getObjects(objectNames, false);
    }

    public List<RData> getObjects(List<String> objectNames, boolean encodeDataFramePrimitiveAsVector)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectWorkspaceGetCall(this.about.id, objectNames, encodeDataFramePrimitiveAsVector);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        List<RData> robjects = rResult.getRObjects();
        String error = rResult.getError();
        boolean success = rResult.isSuccess();
        int errorCode = rResult.getErrorCode();

        log.debug("getObject: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return robjects;
    }

    public void uploadObject(String name, InputStream fileStream)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectWorkspaceUploadCall(this.about.id, name, fileStream);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("uploadObject: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

    public void transferObject(String name, URL url)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectWorkspaceTransferCall(this.about.id, name, url.toString());
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("transferObject: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

    public void pushObject(RData object)
        throws RClientException, RSecurityException, RDataException {

        List<RData> objects = Arrays.asList(object);
        pushObject(objects);
    }

    public void pushObject(List<RData> objects)
        throws RClientException, RSecurityException, RDataException {

        RCall rCall = new ProjectWorkspacePushCall(this.about.id, objects);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("pushObject: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

    public RProjectFile saveObject(String name, String descr, boolean versioning)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectWorkspaceSaveCall(this.about.id, name, descr, versioning);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map dirFileMap = rResult.getDirectoryFile();
        log.debug("saveObject: rResult.getDirectoryFile=" + dirFileMap);
        RProjectFileDetails details = REntityUtil.getProjectFileDetails(dirFileMap);
        RProjectFile projectFile = new RProjectFileImpl(this.about, details, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("saveObject: projectFile, success=" + success + " error=" + error + " errorCode=" + errorCode);

        return projectFile;
    }

    public RRepositoryFile storeObject(String name, String descr, boolean versioning,
                                       String restricted, boolean shared, boolean published)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectWorkspaceStoreCall(this.about.id, name, descr, versioning,
                                                    restricted, shared, published);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        Map repoFileMap = rResult.getRepoFile();
        log.debug("storeObject: rResult.getRepoFile=" + repoFileMap);
        RRepositoryFileDetails details = REntityUtil.getRepositoryFileDetails(repoFileMap, liveContext);

        RRepositoryFile repoFile = new RRepositoryFileImpl(details, liveContext);

        log.debug("storeObject: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return repoFile;
    }

    public void loadObject(RRepositoryFile repoFile)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectWorkspaceLoadCall(this.about.id,
                                                   repoFile.about().filename,
                                                   repoFile.about().directory,
                                                   repoFile.about().author,
                                                   repoFile.about().version);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("loadObject: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

    public void deleteObject(String objectName)
        throws RClientException, RSecurityException {

        List<String> objectNames = new ArrayList<String>();
        objectNames.add(objectName);
        deleteObject(objectNames);
    }

    public void deleteObject(List<String> objectNames)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectWorkspaceDeleteCall(this.about.id, objectNames);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("deleteObject: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }


    /*
     * RProjectDirectoryCalls Interfaces.
     */

    public List<RProjectFile> listFiles()
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectDirectoryListCall(this.about.id);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

            List<Map> files = rResult.getDirectoryFiles();

        List<RProjectFile> fileList = new ArrayList<RProjectFile>();

        for(Map fileMap : files) {
            RProjectFileDetails fileDetails = REntityUtil.getProjectFileDetails(fileMap);
            RProjectFile file = new RProjectFileImpl(this.about, fileDetails, liveContext);
            fileList.add(file);	
        }

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("listFiles: success=" + success + " error=" + error + " errorCode=" + errorCode);
        return fileList;
    }

    public RProjectFile uploadFile(InputStream fileStream, DirectoryUploadOptions options)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectDirectoryUploadCall(this.about.id, fileStream, options);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map dirFileMap = rResult.getDirectoryFile();
        log.debug("uploadFile: rResult.getDirectoryFile=" + dirFileMap);
        RProjectFileDetails details = REntityUtil.getProjectFileDetails(dirFileMap);
        RProjectFile projectFile = new RProjectFileImpl(this.about, details, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("uploadFile: projectFile, success=" + success + " error=" + error + " errorCode=" + errorCode);

        return projectFile;

    }

    public RProjectFile transferFile(URL url, DirectoryUploadOptions options)
	throws RClientException, RSecurityException {

        RCall rCall = new ProjectDirectoryTransferCall(this.about.id, url, options);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map dirFileMap = rResult.getDirectoryFile();
        log.debug("transferFile: rResult.getDirectoryFile=" + dirFileMap);
        RProjectFileDetails details = REntityUtil.getProjectFileDetails(dirFileMap);
        RProjectFile projectFile = new RProjectFileImpl(this.about, details, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("transferFile: projectFile, success=" + success + " error=" + error + " errorCode=" + errorCode);

        return projectFile;
    }

    public RProjectFile writeFile(String text, DirectoryUploadOptions options)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectDirectoryWriteCall(this.about.id, text, options);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map dirFileMap = rResult.getDirectoryFile();
        log.debug("writeFile: rResult.getDirectoryFile=" + dirFileMap);
        RProjectFileDetails details = REntityUtil.getProjectFileDetails(dirFileMap);
        RProjectFile projectFile = new RProjectFileImpl(this.about, details, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("writeFile: projectFile, success=" + success + " error=" + error + " errorCode=" + errorCode);

        return projectFile;
    }

    public RProjectFile loadFile(RRepositoryFile repoFile)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectDirectoryLoadCall(this.about.id,
                                                   repoFile.about().filename,
                                                   repoFile.about().directory,
                                                   repoFile.about().author,
                                                   repoFile.about().version);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map dirFileMap = rResult.getDirectoryFile();
        log.debug("loadFile: rResult.getDirectoryFile=" + dirFileMap);
        RProjectFileDetails details = REntityUtil.getProjectFileDetails(dirFileMap);
        RProjectFile projectFile = new RProjectFileImpl(this.about, details, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("loadFile: projectFile, success=" + success + " error=" + error + " errorCode=" + errorCode);

        return projectFile;
    }

    public InputStream downloadFiles()
        throws RClientException, RSecurityException {
          return downloadFiles(null);
    }

    public InputStream downloadFiles(List<String> files)
        throws RClientException, RSecurityException {

        try {

          String fileNames = null;
          if(files != null) {
              for(String fileName : files) {
                if(fileNames != null) {
                    fileNames = fileNames + "," + fileName;
                } else {
                    fileNames = fileName;
                }
              }
          }

          String urlPath = liveContext.serverurl +
                    REndpoints.RPROJECTDIRECTORYDOWNLOAD;
          urlPath = urlPath + ";jsessionid=" + liveContext.httpcookie;

          URIBuilder builder = new URIBuilder(urlPath);
          builder.addParameter("project", this.about.id);
          if(fileNames != null) {
            builder.addParameter("filename", fileNames);
          }
          return liveContext.executor.download(builder);
        } catch(Exception ex) {
            throw new RClientException("Download failed: " +
                                            ex.getMessage());
        }
    }

    /*
     * RProjectPackageCalls Interfaces.
     */

    public List<RProjectPackage> listPackages(boolean installed)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectPackageListCall(this.about.id, installed);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        List<Map> packageList = rResult.getPackages();
        log.debug("listPackages: rResult.getPackages=" + packageList);

        List<RProjectPackage> packages = new ArrayList<RProjectPackage>();

        for(Map pkgMap : packageList) {

            RProjectPackageDetails details = REntityUtil.getProjectPackageDetails(pkgMap);
            RProjectPackage projectPackage = new RProjectPackageImpl(this.about, details, liveContext);
            packages.add(projectPackage);
        }

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("listPackages: projectFile, success=" + success + " error=" + error + " errorCode=" + errorCode);

        return packages;
    }

    public List<RProjectPackage> attachPackage(String packageName, String repo)
        throws RClientException, RSecurityException {

        List<String> packageNames = new ArrayList<String>();
        packageNames.add(packageName);
        return attachPackage(packageNames, repo);
    }

    public List<RProjectPackage> attachPackage(List<String> packageNames, String repo)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectPackageAttachCall(this.about.id, packageNames, repo);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        List<Map> packageList = rResult.getPackages();
        log.debug("attachPackage: rResult.getPackages=" + packageList);

        List<RProjectPackage> packages = new ArrayList<RProjectPackage>();

        for(Map pkgMap : packageList) {

            RProjectPackageDetails details = REntityUtil.getProjectPackageDetails(pkgMap);
            RProjectPackage projectPackage = new RProjectPackageImpl(this.about, details, liveContext);
            packages.add(projectPackage);
        }

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("attachPackage: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return packages;
    }

    public List<RProjectPackage> detachPackage(String packageName)
        throws RClientException, RSecurityException {

        List<String> packageNames = new ArrayList<String>();
        packageNames.add(packageName);
        return detachPackage(packageNames);
        }

        public List<RProjectPackage> detachPackage(List<String> packageNames)
        throws RClientException, RSecurityException {

        RCall rCall = new ProjectPackageDetachCall(this.about.id, packageNames);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        List<Map> packageList = rResult.getPackages();
        log.debug("detachPackage: rResult.getPackages=" + packageList);

        List<RProjectPackage> packages = new ArrayList<RProjectPackage>();

        for(Map pkgMap : packageList) {

            RProjectPackageDetails details = REntityUtil.getProjectPackageDetails(pkgMap);
            RProjectPackage projectPackage = new RProjectPackageImpl(this.about, details, liveContext);
            packages.add(projectPackage);
        }

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("detachPackages: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return packages;
    }

}
