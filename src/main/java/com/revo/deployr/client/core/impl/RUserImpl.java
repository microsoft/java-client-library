/*
 * RUserImpl.java
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
import com.revo.deployr.client.RSecurityException;
import com.revo.deployr.client.RDataException;
import com.revo.deployr.client.RGridException;
import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.core.RClientExecutor;
import com.revo.deployr.client.call.RCall;

import com.revo.deployr.client.call.user.AutosaveCall;
import com.revo.deployr.client.call.project.ProjectReleaseCall;
import com.revo.deployr.client.call.project.ProjectCreateCall;
import com.revo.deployr.client.call.project.ProjectPoolCall;
import com.revo.deployr.client.call.project.ProjectListCall;
import com.revo.deployr.client.call.project.ProjectAboutCall;
import com.revo.deployr.client.call.project.ProjectImportCall;
import com.revo.deployr.client.call.repository.RepositoryFileListCall;
import com.revo.deployr.client.call.repository.RepositoryFileFetchCall;
import com.revo.deployr.client.call.repository.RepositoryFileUploadCall;
import com.revo.deployr.client.call.repository.RepositoryFileWriteCall;
import com.revo.deployr.client.call.repository.RepositoryFileTransferCall;
import com.revo.deployr.client.call.repository.RepositoryFileCopyCall;
import com.revo.deployr.client.call.repository.RepositoryFileMoveCall;
import com.revo.deployr.client.call.repository.RepositoryScriptListCall;
import com.revo.deployr.client.call.repository.RepositoryDirectoryListCall;
import com.revo.deployr.client.call.repository.RepositoryDirectoryCreateCall;
import com.revo.deployr.client.call.repository.RepositoryDirectoryCopyCall;
import com.revo.deployr.client.call.repository.RepositoryDirectoryMoveCall;
import com.revo.deployr.client.call.repository.RepositoryDirectoryUploadCall;
import com.revo.deployr.client.call.job.JobListCall;
import com.revo.deployr.client.call.job.JobQueryCall;
import com.revo.deployr.client.call.job.JobSubmitCall;
import com.revo.deployr.client.call.job.JobScheduleCall;

import com.revo.deployr.client.RUser;
import com.revo.deployr.client.RProject;
import com.revo.deployr.client.RJob;
import com.revo.deployr.client.RProjectFile;
import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.RRepositoryDirectory;
import com.revo.deployr.client.core.impl.RProjectImpl;

import com.revo.deployr.client.about.RUserDetails;
import com.revo.deployr.client.about.RProjectDetails;
import com.revo.deployr.client.about.RRepositoryFileDetails;
import com.revo.deployr.client.about.RRepositoryDirectoryDetails;
import com.revo.deployr.client.about.RJobDetails;

import com.revo.deployr.client.params.ProjectCreationOptions;
import com.revo.deployr.client.params.ProjectPreloadOptions;
import com.revo.deployr.client.params.RepoUploadOptions;
import com.revo.deployr.client.params.JobSchedulingOptions;
import com.revo.deployr.client.params.JobExecutionOptions;

import com.revo.deployr.client.util.REntityUtil;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.ArrayList;
import java.io.File;
import java.io.InputStream;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory; 

/**
 * DeployR User.
 */
public class RUserImpl implements RUser {

    private Log log = LogFactory.getLog(RUser.class);

    public RUserDetails about;
    RLiveContext liveContext;

    public RUserImpl(RUserDetails about, RLiveContext liveContext) {
	this.about = about;
	this.liveContext = liveContext;
    }

    public RUserDetails about() {
	return about;
    }

    /*
     * RUserProjects Interfaces.
     */

    public void autosaveProjects(boolean save)
                throws RClientException, RSecurityException {

        RCall rCall = new AutosaveCall(save);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("autosaveProjects: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

    public void releaseProjects()
                throws RClientException, RSecurityException {

        RCall rCall = new ProjectReleaseCall();
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("releaseProjects: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

    public RProject createProject()
            throws RClientException, RSecurityException, RDataException, RGridException {

        RCall rCall = new ProjectCreateCall();
        RCoreResult rResult = liveContext.executor.processCallOnGrid(rCall);

        Map project = rResult.getProject();
        log.debug("createProject: project=" + project);

        RProjectDetails about = REntityUtil.getProjectDetails(project);
        RProject rProject = new RProjectImpl(about, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("createProject: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return rProject;
    }

    public RProject createProject(ProjectCreationOptions options)
            throws RClientException, RSecurityException, RDataException, RGridException {

        RCall rCall = new ProjectCreateCall(null, null, options);
        RCoreResult rResult = liveContext.executor.processCallOnGrid(rCall);

        Map project = rResult.getProject();
        log.debug("createProject: project=" + project);

        RProjectDetails about = REntityUtil.getProjectDetails(project);
        RProject rProject = new RProjectImpl(about, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("createProject: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return rProject;
    }

    public RProject createProject(String name, String descr)
                throws RClientException, RSecurityException, RDataException, RGridException {

        RCall rCall = new ProjectCreateCall(name, descr);
        RCoreResult rResult = liveContext.executor.processCallOnGrid(rCall);

        Map project = rResult.getProject();
        log.debug("createProject: project=" + project);

        RProjectDetails about = REntityUtil.getProjectDetails(project);
        RProject rProject = new RProjectImpl(about, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();
        
        log.debug("createProject: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return rProject;
    }

    public RProject createProject(String name,
                                  String descr,
                                  ProjectCreationOptions options)
                throws RClientException, RSecurityException, RDataException, RGridException {

        RCall rCall = new ProjectCreateCall(name, descr, options);
        RCoreResult rResult = liveContext.executor.processCallOnGrid(rCall);

        Map project = rResult.getProject();
        log.debug("createProject: project=" + project);

        RProjectDetails about = REntityUtil.getProjectDetails(project);
        RProject rProject = new RProjectImpl(about, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("createProject: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return rProject;
    }

    public List<RProject> createProjectPool(int poolsize,
                                            ProjectCreationOptions options)
                throws RClientException,
                       RSecurityException,
                       RDataException,
                       RGridException {

        List<RProject> projectList = null;
        boolean success = false;
        String error = null;
        int errorCode = 0;

        try {

            RCall rCall = new ProjectPoolCall(poolsize, options);
            log.debug("createProjectPool: about to create pool, size=" + poolsize);
            RCoreResult rResult = null;
            rResult = liveContext.executor.processCallOnGrid(rCall);

            List<Map> projects = (rResult != null) ? rResult.getProjects() : null;
            log.debug("createProjectPool: rResult.getProjects returns=" + projects);

            if(projects == null || (projects != null && projects.size() == 0)) {
                log.debug("createProjectPool: projects=" + projects + ", throwing RGridException.");
                throw new RGridException(rResult.getError(), rResult.getErrorCode());
            } else {
                log.debug("createProjectPool: rResult.getProjects are valid, continue processing.");
            }

            projectList = new ArrayList<RProject>();

            for(Map projectMap : projects) {
                try {
                    RProjectDetails about = REntityUtil.getProjectDetails(projectMap);
                    RProject project = new RProjectImpl(about, liveContext);
                    projectList.add(project);	
                } catch(Exception dex) {
                    log.warn("createProjectPool: exception converting projectMap= " + projectMap + ", into RProjectDetails, ex=" + dex);
                }
            }

        } finally {
            log.debug("createProjectPool: about to return, projectList=" + projectList + ", success=" + success + ", error=" + error + ", errorCode=" + errorCode);
        }

        log.debug("createProjectPool: success=" + success + " error=" + error + " errorCode=" + errorCode);
        return projectList;
    }

    public RProject getProject(String id)
                throws RClientException, RSecurityException {

        RCall rCall = new ProjectAboutCall(id);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map project = rResult.getProject();
        log.debug("getProject: project=" + project);

        RProjectDetails about = REntityUtil.getProjectDetails(project);
        RProject rProject = new RProjectImpl(about, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("getProject: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return rProject;
    }

    public List<RProject> listProjects()
                throws RClientException, RSecurityException {

        return listProjects(false, false);
    }

    public List<RProject> listProjects(boolean sortByLastModified,
                                       boolean showPublicProjects)
                        throws RClientException, RSecurityException {

        RCall rCall = new ProjectListCall(sortByLastModified, showPublicProjects);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        List<Map> projects = rResult.getProjects();
        log.debug("listProjects: projects=" + projects);

        List<RProject> projectList = new ArrayList<RProject>();

        for(Map projectMap : projects) {
            RProjectDetails about = REntityUtil.getProjectDetails(projectMap);
            RProject project = new RProjectImpl(about, liveContext);
            projectList.add(project);	
        }

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("listProjects: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return projectList;
    }

    public RProject importProject(InputStream fileStream,
                                  String descr)
                throws RClientException, RSecurityException {

        RCall rCall = new ProjectImportCall(fileStream, descr);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map project = rResult.getProject();
        log.debug("importProject: project=" + project);

        RProjectDetails about = REntityUtil.getProjectDetails(project);
        RProject rProject = new RProjectImpl(about, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("importProject: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return rProject;
    }

    /*
     * RUserFileRepository Interfaces.
     */

    public List<RRepositoryFile> listFiles()
                throws RClientException, RSecurityException {

        return listFiles(false, false, false);
    }

    public List<RRepositoryFile> listFiles(boolean archived,
                                           boolean shared,
                                           boolean published)
                throws RClientException, RSecurityException {

        return listFiles(archived, shared, published, null, null);
    }

    public List<RRepositoryFile> listFiles(RRepositoryFile.Category categoryFilter,
                                           String directoryFilter)
            throws RClientException, RSecurityException {

        return listRepoFiles(false, false, false,
                             null, directoryFilter,
                             false,
                             categoryFilter);
    }

    public List<RRepositoryFile> listFiles(String filename,
                                           String directory)
                throws RClientException, RSecurityException {
        return listFiles(false, false, false, filename, directory);
    }

    public List<RRepositoryFile> listFiles(boolean archived,
                                          boolean shared, boolean published,
                                          String filename, String directory)
                throws RClientException, RSecurityException {

        return listRepoFiles(archived,
                             shared, published,
                             filename, directory,
                             false, null);
    }

    public List<RRepositoryFile> listExternalFiles()
                        throws RClientException, RSecurityException {
        return listExternalFiles(false, false);
    }

    public List<RRepositoryFile> listExternalFiles(boolean shared,
                                                   boolean published)
                        throws RClientException, RSecurityException {

        return listRepoFiles(false,
                             shared, published,
                             null, null,
                             true, null);
    }

    public List<RRepositoryFile> listExternalFiles(RRepositoryFile.Category categoryFilter,
                                           String directoryFilter)
            throws RClientException, RSecurityException {

        return listRepoFiles(false, false, false,
                             null, directoryFilter,
                             true,
                             categoryFilter);
    }

    private List<RRepositoryFile> listRepoFiles(boolean archived,
                                              boolean shared, boolean published,
                                              String filename, String directory,
                                              boolean useExternalRepo,
                                              RRepositoryFile.Category categoryFilter)
                throws RClientException, RSecurityException {

        RCall rCall = new RepositoryFileListCall(archived,
                                                 shared, published,
                                                 filename, directory,
                                                 useExternalRepo,
             categoryFilter != null ? categoryFilter.toString() : null);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        List<Map> repoFiles = rResult.getRepoFiles();
        log.debug("listFiles: repoFiles=" + repoFiles);

        List<RRepositoryFile> fileList = new ArrayList<RRepositoryFile>();

        for(Map repoFileMap : repoFiles) {

            RRepositoryFileDetails details = REntityUtil.getRepositoryFileDetails(repoFileMap, liveContext);
            RRepositoryFile file = new RRepositoryFileImpl(details, liveContext);
            fileList.add(file);	
        }

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("listFiles: repoFiles, success=" + success + " error=" + error + " errorCode=" + errorCode);

        return fileList;
    }

    public RRepositoryFile fetchFile(String filename,
                                     String directory,
                                     String author,
                                     String version)
                throws RClientException, RSecurityException {

        RCall rCall = new RepositoryFileFetchCall(filename, directory, author, version);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map repoFileMap = rResult.getRepoFile();
        log.debug("fetchFile: rResult.getRepoFile=" + repoFileMap);
        RRepositoryFileDetails details = REntityUtil.getRepositoryFileDetails(repoFileMap, liveContext);
        RRepositoryFile repoFile = new RRepositoryFileImpl(details, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("fetchFile: repoFile, success=" + success + " error=" + error + " errorCode=" + errorCode);

        return repoFile;
    }

    public RRepositoryFile uploadFile(InputStream fileStream,
                                      RepoUploadOptions options)
                throws RClientException, RSecurityException {

        RCall rCall = new RepositoryFileUploadCall(fileStream, options);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map repoFileMap = rResult.getRepoFile();
        log.debug("uploadFile: rResult.getRepoFile=" + repoFileMap);
        RRepositoryFileDetails details = REntityUtil.getRepositoryFileDetails(repoFileMap, liveContext);
        RRepositoryFile repoFile = new RRepositoryFileImpl(details, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("uploadFile: repoFile, success=" + success + " error=" + error + " errorCode=" + errorCode);

        return repoFile;
    }

    public RRepositoryFile writeFile(String text, RepoUploadOptions options)
                throws RClientException, RSecurityException {

        RCall rCall = new RepositoryFileWriteCall(text, options);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map repoFileMap = rResult.getRepoFile();
        log.debug("writeFile: rResult.getRepoFile=" + repoFileMap);
        RRepositoryFileDetails details = REntityUtil.getRepositoryFileDetails(repoFileMap, liveContext);

        RRepositoryFile repoFile = new RRepositoryFileImpl(details, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("writeFile: repoFile, success=" + success + " error=" + error + " errorCode=" + errorCode);

        return repoFile;
    }

    public RRepositoryFile transferFile(URL url, RepoUploadOptions options)
                throws RClientException, RSecurityException {

        RCall rCall = new RepositoryFileTransferCall(url, options);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map repoFileMap = rResult.getRepoFile();
        log.debug("transferFile: rResult.getRepoFile=" + repoFileMap);
        RRepositoryFileDetails details = REntityUtil.getRepositoryFileDetails(repoFileMap, liveContext);

        RRepositoryFile repoFile = new RRepositoryFileImpl(details, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("transferFile: repoFile, success=" + success + " error=" + error + " errorCode=" + errorCode);

        return repoFile;
    }

    public void copyFiles(String destination,
                          List<RRepositoryFile> files,
                          List<String> fileRenames)
                        throws RClientException, RSecurityException {

        List<Map<String,String>> fileMapList = null;
        if(files != null) {
            fileMapList = new ArrayList<Map<String,String>>();
            for(RRepositoryFile repoFile : files) {
                Map<String,String> fileMap = new HashMap<String,String>();
                fileMap.put("filename", repoFile.about().filename);
                fileMap.put("directory", repoFile.about().directory);
                fileMap.put("author", repoFile.about().author);
                fileMap.put("version", repoFile.about().version);
                fileMapList.add(fileMap);
            }
        }

        RCall rCall = new RepositoryFileCopyCall(destination,
                                                 fileMapList,
                                                 fileRenames);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("copyFiles: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

    public void moveFiles(String destination,
                          List<RRepositoryFile> files)
                        throws RClientException, RSecurityException {

        List<Map<String,String>> fileMapList = null;
        if(files != null) {
            fileMapList = new ArrayList<Map<String,String>>();
            for(RRepositoryFile repoFile : files) {
                Map<String,String> fileMap = new HashMap<String,String>();
                fileMap.put("filename", repoFile.about().filename);
                fileMap.put("directory", repoFile.about().directory);
                fileMap.put("author", repoFile.about().author);
                fileMapList.add(fileMap);
            }
        }

        RCall rCall = new RepositoryFileMoveCall(destination,
                                                 fileMapList);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("moveFiles: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

    /*
     * RUserScriptRepository Interfaces.
     */

    public List<RRepositoryFile> listScripts()
        throws RClientException, RSecurityException {

        return listRepoScripts(false, false, false,
                               null, null,
                               false);
    }

    public List<RRepositoryFile> listScripts(boolean archived,
                                             boolean shared,
                                             boolean published)
                    throws RClientException, RSecurityException {

        return listRepoScripts(archived, shared, published,
                               null, null,
                               false);
    }

    public List<RRepositoryFile> listScripts(String filename,
                                             String directory)
	throws RClientException, RSecurityException {

        return listRepoScripts(false, false, false,
                               filename, directory,
                               false);
    }

    public List<RRepositoryFile> listExternalScripts()
        throws RClientException, RSecurityException {

        return listExternalScripts(false, false);
    }

    public List<RRepositoryFile> listExternalScripts(boolean shared,
                                                     boolean published)
                    throws RClientException, RSecurityException {

        return listRepoScripts(false, shared, published,
                               null, null,
                               true);
    }

    private List<RRepositoryFile> listRepoScripts(boolean archived,
                                                  boolean shared,
                                                  boolean published,
                                                  String filename,
                                                  String directory,
                                                  boolean useExternalRepo)
                        throws RClientException, RSecurityException {

        RCall rCall = new RepositoryScriptListCall(archived,
                                                   shared,
                                                   published,
                                                   filename,
                                                   directory,
                                                   useExternalRepo);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        List<Map> repoScripts = rResult.getRepoScripts();
        log.debug("listRepoScripts: repoScripts=" + repoScripts);

        List<RRepositoryFile> scriptList = new ArrayList<RRepositoryFile>();

        for(Map repoScriptMap : repoScripts) {
            RRepositoryFileDetails details =
                REntityUtil.getRepositoryFileDetails(repoScriptMap, liveContext);
            RRepositoryFile script =
                new RRepositoryFileImpl(details, liveContext);
            scriptList.add(script);	
        }

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("listRepoScripts: repoScripts, success=" + success +
                        " error=" + error + " errorCode=" + errorCode);

        return scriptList;
    }

    /*
     * RUserDirectoryRepository Interfaces.
     */

    public List<RRepositoryDirectory> listDirectories()
                    throws RClientException, RSecurityException {

        return listDirectories(false, false, false, false);
    }

   public List<RRepositoryDirectory> listDirectories(boolean userfiles,
                                                      boolean archived,
                                                      boolean shared,
                                                      boolean published)
                            throws RClientException, RSecurityException {

        return listRepoDirectories(userfiles,
                                   archived, shared, published,
                                   false,
                                   null, null);
    }

    public List<RRepositoryDirectory> listDirectories(RRepositoryFile.Category categoryFilter,
                                           String directoryFilter)
            throws RClientException, RSecurityException {

        return listRepoDirectories(false,
                                   false, false, false,
                                   false,
                                   directoryFilter, categoryFilter);
    }

    public List<RRepositoryDirectory> listExternalDirectories()
                            throws RClientException, RSecurityException {

        return listExternalDirectories(false, false, false);
    }

    public List<RRepositoryDirectory> listExternalDirectories(boolean userfiles,
                                                              boolean shared,
                                                              boolean published)
                                throws RClientException, RSecurityException {

        return listRepoDirectories(userfiles,
                                   false, shared, published,
                                   true,
                                   null, null);
    }

    public List<RRepositoryDirectory> listExternalDirectories(RRepositoryFile.Category categoryFilter,
                                           String directoryFilter)
            throws RClientException, RSecurityException {

        return listRepoDirectories(false,
                                   false, false, false,
                                   true,
                                   directoryFilter, categoryFilter);
    }

    private List<RRepositoryDirectory> listRepoDirectories(boolean userfiles,
                                                           boolean archived,
                                                           boolean shared,
                                                           boolean published,
                                                           boolean useExternalRepo,
                                                           String directoryFilter,
                                                           RRepositoryFile.Category categoryFilter)
                            throws RClientException, RSecurityException {

        RCall rCall = new RepositoryDirectoryListCall(userfiles,
                                                      archived,
                                                      shared,
                                                      published,
                                                      useExternalRepo,
                                                      directoryFilter,
            categoryFilter !=  null ? categoryFilter.toString() : null);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        List<Map> repoDirectories = rResult.getRepoDirectories();
        log.debug("listDirectories: repoDirectories=" + repoDirectories);

        List<RRepositoryDirectory> directoryList = new ArrayList<RRepositoryDirectory>();

        for(Map repoDirectoryMap : repoDirectories) {

            RRepositoryDirectoryDetails details =
                REntityUtil.getRepositoryDirectoryDetails(repoDirectoryMap, liveContext);
            RRepositoryDirectory directory =
                new RRepositoryDirectoryImpl(details, liveContext);
            directoryList.add(directory);   
        }

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("listRepoDirectories: repoDirectories, success=" + success +
                            " error=" + error + " errorCode=" + errorCode);

        return directoryList;
    }

    public RRepositoryDirectory createDirectory(String newDirectoryName)
                            throws RClientException, RSecurityException {

        RCall rCall = new RepositoryDirectoryCreateCall(newDirectoryName);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map repoDirectoryMap = rResult.getRepoDirectory();
        log.debug("create: rResult.getRepoDirectory=" + repoDirectoryMap);
        RRepositoryDirectoryDetails details =
            REntityUtil.getRepositoryDirectoryDetails(repoDirectoryMap, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("create: repoDirectory, success=" + success +" error=" + error + " errorCode=" + errorCode);

        return new RRepositoryDirectoryImpl(details, liveContext);
    }

    public void copyDirectory(String source,
                              String destination,
                              List<RRepositoryFile> files)
                        throws RClientException, RSecurityException {

        List<String> fileNames = null;
        if(files != null) {
            fileNames = new ArrayList<String>();
            for(RRepositoryFile repoFile : files) {
                fileNames.add(repoFile.about().filename);
            }
        }

        RCall rCall = new RepositoryDirectoryCopyCall(source,
                                                      destination,
                                                      fileNames);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("copyDirectory: success=" + success + " error=" + error + " errorCode=" + errorCode);

    }

    public void moveDirectory(String source,
                              String destination,
                              List<RRepositoryFile> files)
                        throws RClientException, RSecurityException {

        List<String> fileNames = null;
        if(files != null) {
            fileNames = new ArrayList<String>();
            for(RRepositoryFile repoFile : files) {
                fileNames.add(repoFile.about().filename);
            }
        }

        RCall rCall = new RepositoryDirectoryMoveCall(source,
                                                      destination,
                                                      fileNames);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("moveDirectory: success=" + success + " error=" + error + " errorCode=" + errorCode);

    }

    public void uploadDirectory(InputStream zipStream,
                                RepoUploadOptions options)
                        throws RClientException, RSecurityException {

        RCall rCall = new RepositoryDirectoryUploadCall(zipStream, options);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("uploadDirectory: success=" + success + " error=" + error + " errorCode=" + errorCode);
    }

    /*
     * RUserJobs Interfaces.
     */

    public List<RJob> listJobs()
                throws RClientException, RSecurityException {

        return listJobs(false);
    }


    public List<RJob> listJobs(boolean openOnly)
                throws RClientException, RSecurityException {

        RCall rCall = new JobListCall(openOnly);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        List<Map> jobs = rResult.getJobs();
        log.debug("listJobs: jobs=" + jobs);

        List<RJob> jobList = new ArrayList<RJob>();

        for(Map jobMap : jobs) {
            RJobDetails about = REntityUtil.getJobDetails(jobMap);
            RJob job = new RJobImpl(about, liveContext);
            jobList.add(job);	
        }

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("listJobs: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return jobList;
    }

    public RJob queryJob(String jobId)
                throws RClientException, RSecurityException {

        RCall rCall = new JobQueryCall(jobId);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map job = rResult.getJob();
        log.debug("query: job=" + job);

        RJobDetails jobDetails = REntityUtil.getJobDetails(job);
        RJob rJob = new RJobImpl(jobDetails, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("queryJob: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return rJob;
    }

    public RJob submitJobCode(String name,
                              String descr,
                              String code)
                throws RClientException, RSecurityException, RDataException {

        return submitJobCode(name, descr, code, null);
    }

    public RJob submitJobCode(String name,
                              String descr,
                              String code,
                              JobExecutionOptions options)
                throws RClientException, RSecurityException, RDataException {

        RCall rCall = null;
        if(options != null && options.schedulingOptions != null)
            rCall = new JobScheduleCall(name, descr, code, null, null, null, null, null, options);
        else
            rCall = new JobSubmitCall(name, descr, code, null, null, null, null, null, options);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map job = rResult.getJob();
        log.debug("submitJobCode: job=" + job);

        RJobDetails about = REntityUtil.getJobDetails(job);
        RJob rJob = new RJobImpl(about, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("submitJobCode: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return rJob;
    }

    public RJob submitJobScript(String name,
                                String descr,
                                String scriptName,
                                String scriptAuthor,
                                String scriptVersion)
                throws RClientException, RSecurityException, RDataException {

        return submitJobScript(name,
                               descr,
                               scriptName,
                               RRepositoryDirectory.ROOT,
                               scriptAuthor,
                               scriptVersion,
                               null);
    }


    public RJob submitJobScript(String name,
                                String descr,
                                String scriptName,
                                String scriptAuthor,
                                String scriptVersion,
                                JobExecutionOptions options)
                throws RClientException, RSecurityException, RDataException {

        return submitJobScript(name,
                               descr,
                               scriptName,
                               RRepositoryDirectory.ROOT,
                               scriptAuthor,
                               scriptVersion,
                               options
                               );
    }

    public RJob submitJobScript(String name,
                                String descr,
                                String scriptName,
                                String scriptDirectory,
                                String scriptAuthor,
                                String scriptVersion)
                throws RClientException, RSecurityException, RDataException {

        return submitJobScript(name,
                               descr,
                               scriptName,
                               scriptDirectory,
                               scriptAuthor,
                               scriptVersion,
                               null
                               );
    }


    public RJob submitJobScript(String name,
                                String descr,
                                String scriptName,
                                String scriptDirectory,
                                String scriptAuthor,
                                String scriptVersion,
                                JobExecutionOptions options)
                throws RClientException, RSecurityException, RDataException {

        RCall rCall = null;

        if(options != null && options.schedulingOptions !=  null)
            rCall = new JobScheduleCall(name,
                                        descr,
                                        null,
                                        scriptName,
                                        scriptDirectory,
                                        scriptAuthor,
                                        scriptVersion,
                                        null, options);
        else
            rCall = new JobSubmitCall(name,
                                      descr,
                                      null,
                                      scriptName,
                                      scriptDirectory,
                                      scriptAuthor,
                                      scriptVersion,
                                      null, options);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map job = rResult.getJob();
        log.debug("submitJobScript: job=" + job);

        RJobDetails about = REntityUtil.getJobDetails(job);
        RJob rJob = new RJobImpl(about, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("submitJobScript: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return rJob;
    }

    public RJob submitJobExternal(String name,
                                  String descr,
                                  String externalSource,
                                  JobExecutionOptions options)
                throws RClientException, RSecurityException, RDataException {

        RCall rCall = null;

        if(options != null && options.schedulingOptions !=  null)
            rCall = new JobScheduleCall(name,
                                        descr,
                                        null, null, null, null, null,
                                        externalSource, options);
        else
            rCall = new JobSubmitCall(name,
                                      descr,
                                      null, null, null, null, null,
                                      externalSource, options);
        RCoreResult rResult = liveContext.executor.processCall(rCall);

        Map job = rResult.getJob();
        log.debug("submitJobExternal: job=" + job);

        RJobDetails about = REntityUtil.getJobDetails(job);
        RJob rJob = new RJobImpl(about, liveContext);

        boolean success = rResult.isSuccess();
        String error = rResult.getError();
        int errorCode = rResult.getErrorCode();

        log.debug("submitJobExternal: success=" + success + " error=" + error + " errorCode=" + errorCode);

        return rJob;
    }
}
