/*
 * RCoreResultImpl.java
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

import com.revo.deployr.client.core.RCoreResult;
import com.revo.deployr.client.data.RData;
import com.revo.deployr.client.util.RDataUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

// import org.codehaus.jackson.map.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory; 

public class RCoreResultImpl implements RCoreResult {

    private Log log = LogFactory.getLog(RCoreResult.class);

    private boolean success; 
    private String call;
    private String httpcookie;
    private Map<String, String> identity;
    private Map<String, Integer> limits;
    private Map project;
    private List<Map> projects;
    private Map repoFile;
    private List<Map> repoFiles;
    private List<Map> repoScripts;
    private Map repoDirectory;
    private List<Map> repoDirectories;
    private List<String> repoShellConsoleOutput;
    private Map job;
    private List<Map> jobs;

    private Map execution;
    private String console;
    private List<String> warnings;
    private List<Map> results;
    private List<Map> artifacts;
    private Map directoryFile;
    private List<Map> directoryFiles;

    private List<Map> history;

    private List<Map> packages;

    private List<RData> robjects = new ArrayList<RData>();
    private int statusCode;
    private String statusMsg;
    private String error;
    private int errorCode;

    public boolean isSuccess() {
	return success;
    } 

    public String getCall() {
	return call;
    }

    public String getCookie() {
	return httpcookie;
    }

    public Map<String, String> getIdentity() {
	return identity;
    }

    public Map<String, Integer> getLimits() {
	return limits;
    }

    public List<Map> getProjects() {
	return projects;
    }

    public Map getProject() {
	return project;
    }

    public Map getRepoFile() {
	return repoFile;
    }

    public List<Map> getRepoFiles() {
	return repoFiles;
    }

    public Map getRepoDirectory() {
	return repoDirectory;
    }

    public List<Map> getRepoDirectories() {
	return repoDirectories;
    }


    public List<Map> getRepoScripts() {
	return repoScripts;
    }

    public List<String> getRepoShellConsoleOutput() {
        return repoShellConsoleOutput;
    }

    public Map getJob() {
	return job;
    }

    public List<Map> getJobs() {
	return jobs;
    }

    public String getConsole() {
	return console;
    }

    public List<String> getWarnings() {
	return warnings;
    }

    public List<Map> getResults() {
	return results;
    }

    public List<Map> getArtifacts() {
	return artifacts;
    }

    public Map getDirectoryFile() {
	return directoryFile;
    }

    public List<Map> getDirectoryFiles() {
	return directoryFiles;
    }

    public Map getExecution() {
	return execution;
    }

    public List<Map> getHistory() {
	return history;
    }

    public List<Map> getPackages() {
	return packages;
    }

    public List<RData> getRObjects() {
	return robjects;
    }

    public String getError() {
	return error;
    }

    public int getErrorCode() {
	return errorCode;
    }

    public int getStatusCode() {
	return statusCode;
    }

    public String getStatusMsg() {
	return statusMsg;
    }

    public void parseMarkup(String markup, String call, int statusCode, String statusMsg) {

        this.call = call;
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;

        log.debug("RCoreResultImpl: parseMarkup, markup=" + markup + ", statusCode=" + statusCode + " statusMsg=" + statusMsg);

        if(statusCode == 200) {

            ObjectMapper mapper = new ObjectMapper();

            Map markupMap = null;

            try {

                markupMap = (Map) mapper.readValue(markup, Map.class);

            } catch(UnsupportedEncodingException ueex) {
                log.warn("RCoreResultImpl: parseMarkup, unsupported encoding exception=", ueex);
            } catch(IOException ioex) {
                log.warn("RCoreResultImpl: parseMarkup, io exception=", ioex);
            } 

            // Extract RCoreResult data from the Phoenix API JSON response markup.
            if(markupMap != null) {

                // Top-level JSON deployr object.
                Map deployrMap = (Map) markupMap.get("deployr"); 

                // Property: response object.
                Map responseMap = (Map) deployrMap.get("response");
                log.debug("RCoreResult: responseMap=" + responseMap);

                // Properties: success, call, httpcookie, session, error, hisotry.
                success = (Boolean) responseMap.get("success");
                call = (String) responseMap.get("call");
                httpcookie = (String) responseMap.get("httpcookie");
                error = (String) responseMap.get("error");
            if(responseMap.get("errorCode") != null) {
                    errorCode = (Integer) responseMap.get("errorCode");
            }

                log.debug("RCoreResult: success=" + success + " call=" + call);
                log.debug("RCoreResult: httpcookie=" + httpcookie);
                log.debug("RCoreResult: error=" + error + " errorCode=" + errorCode);

                // Property: User Identity.
                identity = (Map) responseMap.get("user");
                log.debug("RCoreResult: identity=" + identity);

                // Property: User Limits.
                limits = (Map) responseMap.get("limits");
                log.debug("RCoreResult: limits=" + limits);

                // Property: Project.
                project = (Map) responseMap.get("project");
                log.debug("RCoreResult: project=" + project);

                // Property: Projects.
                projects = (List<Map>) responseMap.get("projects");
                log.debug("RCoreResult: projects=" + projects);

                // Property: Project Execution.
                execution = (Map) responseMap.get("execution");
                log.debug("RCoreResult: execution=" + execution);

            if(execution != null) {

                console = (String) execution.get("console");
                    log.debug("RCoreResult: console=" + console);

                warnings = (List<String>) execution.get("warnings");
                    log.debug("RCoreResult: warnings=" + warnings);

                results = (List<Map>) execution.get("results");
                    log.debug("RCoreResult: results=" + results);

                artifacts = (List<Map>) execution.get("artifacts");
                    log.debug("RCoreResult: artifacts=" + artifacts);

                    // Property: Project History.
                    history = (List<Map>) execution.get("history");
                    log.debug("RCoreResult: history=" + history);
            }

            Map directory = (Map) responseMap.get("directory");
            if(directory != null) {
                directoryFile = (Map) directory.get("file");
                    log.debug("RCoreResult: directory file=" + directoryFile);
                directoryFiles = (List<Map>) directory.get("files");
                    log.debug("RCoreResult: directory files=" + directoryFiles);
            }

            // Property: Project History.
            packages = (List<Map>) responseMap.get("packages");
            log.debug("RCoreResult: packages=" + packages);

            // Property: Workspace Objects.
            Map workspace = (Map) responseMap.get("workspace");
            if(workspace != null) {

                List<Map> robjectsList = (List<Map>) workspace.get("objects");
                    log.debug("RCoreResult: plural robjectsList=" + robjectsList);
                    if(robjectsList != null) {
                    for(Map robjectMap : robjectsList) {
                    String name = (String) robjectMap.get("name");
                        RData rData = RDataUtil.fromJSON(name, robjectMap);
                        robjects.add(rData);
                    }
                        log.debug("RCoreResult: plural robjects=" + robjects);
                    }

                if(robjectsList == null) {
                    Map robjectMap = (Map) workspace.get("object");
                        log.debug("RCoreResult: singular robjectMap=" + robjectMap);
                    if(robjectMap != null) {	
                        String name = (String) robjectMap.get("name");
                        RData rData = RDataUtil.fromJSON(name, robjectMap);
                        robjects.add(rData);
                            log.debug("RCoreResult: singular robjects=" + robjects);
                    }
                }
            }

            // Property: Repository.
            Map repository = (Map) responseMap.get("repository");
                log.debug("RCoreResult: repository=" + repository);

            if(repository != null) {

                // Property: Repository file.
                    repoFile = (Map) repository.get("file");
                    log.debug("RCoreResult: repoFile=" + repoFile);

                    // Property: Repository files.
                    repoFiles = (List<Map>) repository.get("files");
                    log.debug("RCoreResult: repoFiles=" + repoFiles);

                    // Property: Repository scripts.
                    repoScripts = (List<Map>) repository.get("scripts");
                    log.debug("RCoreResult: repoScripts=" + repoScripts);

                    // Property: Repository directory.
                    repoDirectory = (Map) repository.get("directory");
                    log.debug("RCoreResult: repoDirectory=" + repoDirectory);

                    // Property: Repository directories.
                    Map directories = (Map) repository.get("directories");
                    if(directories != null) {
                        repoDirectories = (List<Map>) directories.get("user");
                        List<Map> systemDirectories = (List<Map>) directories.get("system");
                        if(systemDirectories != null)
                            repoDirectories.addAll(systemDirectories);
                    }
                    log.debug("RCoreResult: repoDirectories=" + repoDirectories);

                    Map shellMap = (Map) repository.get("shell");
                    if(shellMap != null) {
                        repoShellConsoleOutput = (List<String>) shellMap.get("console");
                    }
            }

            // Property: Job.
            job = (Map) responseMap.get("job");
            log.debug("RCoreResult: job=" + job);

                // Property: Jobs.
                jobs = (List<Map>) responseMap.get("jobs");
                log.debug("RCoreResult: jobs=" + jobs);

            }

        }

    }

}
