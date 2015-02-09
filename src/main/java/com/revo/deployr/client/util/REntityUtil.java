/*
 * REntityUtil.java
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
package com.revo.deployr.client.util;

import com.revo.deployr.client.RProjectFile;
import com.revo.deployr.client.RProjectResult;
import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.about.*;
import com.revo.deployr.client.core.impl.RLiveContext;
import com.revo.deployr.client.core.impl.RProjectFileImpl;
import com.revo.deployr.client.core.impl.RProjectResultImpl;
import com.revo.deployr.client.core.impl.RRepositoryFileImpl;
import com.revo.deployr.client.data.RData;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class REntityUtil {

    public static RUserDetails getUserDetails(Map<String, String> identity, Map<String, Integer> limits) {

        String username = (String) identity.get("username");
        String displayname = (String) identity.get("displayname");
        String cookie = (String) identity.get("usercookie");

        int maxConcurrent = limits.get("maxConcurrentLiveProjectCount").intValue();
        int maxIdle = limits.get("maxIdleLiveProjectTimeout").intValue();
        int maxFile = limits.get("maxFileUploadSize").intValue();
        RUserLimitDetails limitDetails =
                new RUserLimitDetails(maxConcurrent, maxIdle, maxFile);

        RUserDetails userDetails = new RUserDetails(username, displayname, cookie, limitDetails);

        return userDetails;
    }

    public static RProjectDetails getProjectDetails(Map project) {

        String id = (String) project.get("project");
        String origin = (String) project.get("origin");
        boolean islive = false;
        if (project.get("live") != null) {
            islive = (Boolean) project.get("live");
        }
        Date modified = null;
        try {
            modified = new Date((Long) project.get("lastmodified"));
        } catch (Exception lex) {
        }
        List<String> authors = (List<String>) project.get("authors");

        RProjectDetails projectDetails = new RProjectDetails(id, origin, islive, modified, authors);
        projectDetails.name = (String) project.get("name");
        projectDetails.descr = (String) project.get("descr");
        projectDetails.longdescr = (String) project.get("longdescr");
        projectDetails.shared = false;
        if (project.get("shared") != null) {
            projectDetails.shared = (Boolean) project.get("shared");
        }
        projectDetails.cookie = (String) project.get("projectcookie");

        return projectDetails;
    }

    public static RRepositoryFileDetails getRepositoryFileDetails(Map repoFile) {

        String filename = (String) repoFile.get("filename");
        String directory = (String) repoFile.get("directory");
        String author = (String) repoFile.get("author");
        String version = (String) repoFile.get("version");
        String latestby = (String) repoFile.get("latestby");
        String descr = (String) repoFile.get("descr");
        String type = (String) repoFile.get("type");
        String urlString = (String) repoFile.get("url");

        String tags = (String) repoFile.get("tags");
        RRepositoryFile.Category category = null;
        String categoryName = (String) repoFile.get("category");
        if(categoryName != null) {
            category = RRepositoryFile.Category.fromString(categoryName);
        }
        String md5 = (String) repoFile.get("md5");
        Long lastModified = (Long) repoFile.get("lastModified");
        Date lastModifiedDate = null;
        try {
            lastModifiedDate = new Date(lastModified);
        } catch (Exception dex) {
        }

        URL url = null;
        try {
            url = new URL(urlString);
        } catch (Exception ex) {
        }
        long size = 0L;
        Object sizeObj = repoFile.get("length");
        if(sizeObj instanceof Long)
            size = (Long) sizeObj;
        else
        if(sizeObj instanceof Integer)
            size = ((Integer) sizeObj).longValue();
        String restricted = (String) repoFile.get("restricted");
        boolean shared = false;
        if (repoFile.get("shared") != null) {
            shared = (Boolean) repoFile.get("shared");
        }
        boolean published = false;
        if (repoFile.get("published") != null) {
            published = (Boolean) repoFile.get("published");
        }
        String access = (String) repoFile.get("access");

        List<String> authors = (List<String>) repoFile.get("authors");

        String inputs = (String) repoFile.get("inputs");
        String outputs = (String) repoFile.get("outputs");

        RRepositoryFileDetails fileDetails =
                new RRepositoryFileDetails(filename, directory, author, version, latestby,
                        descr, type, size, url, access,
                        restricted, shared, published, authors, inputs, outputs,
                        tags, category, md5, lastModifiedDate);

        return fileDetails;
    }

    public static RRepositoryDirectoryDetails getRepositoryDirectoryDetails(Map directory,
                                                                            RLiveContext liveContext) {

        String name = (String) directory.get("directory");
        boolean systemDirectory = (name.equals(RRepositoryDirectoryDetails.SYSTEM_RESTRICTED) ||
                name.equals(RRepositoryDirectoryDetails.SYSTEM_SHARED) ||
                name.equals(RRepositoryDirectoryDetails.SYSTEM_PUBLIC)) ? true : false;
        List<Map> files = (List<Map>) directory.get("files");

        List<RRepositoryFile> repoList = new ArrayList<RRepositoryFile>();

        if (files != null) {

            for (Map repoFileMap : files) {
                RRepositoryFileDetails details = REntityUtil.getRepositoryFileDetails(repoFileMap);
                RRepositoryFile repoFile = new RRepositoryFileImpl(details, liveContext);
                repoList.add(repoFile);
            }
        }

        RRepositoryDirectoryDetails dirDetails =
                new RRepositoryDirectoryDetails(name, systemDirectory, repoList);

        return dirDetails;
    }

    public static RJobDetails getJobDetails(Map job) {

        String id = (String) job.get("job");
        String name = (String) job.get("name");
        String descr = (String) job.get("descr");
        String status = (String) job.get("status");
        String statusMsg = (String) job.get("statusMsg");
        String tag = (String) job.get("tag");

        long schedstart = 0;
        Object schedstartObj = job.get("schedstart");
        if (schedstartObj instanceof Integer)
            schedstart = ((Integer) schedstartObj).longValue();
        else if (schedstartObj instanceof Long)
            schedstart = (Long) schedstartObj;

        long schedinterval = 0;
        Object schedintervalObj = job.get("schedinterval");
        if (schedintervalObj instanceof Integer)
            schedinterval = ((Integer) schedintervalObj).longValue();
        else if (schedintervalObj instanceof Long)
            schedinterval = (Long) schedintervalObj;

        int schedrepeat = (Integer) job.get("schedrepeat");
        int onrepeat = (Integer) job.get("onrepeat");

        String project = (String) job.get("project");

        long timeStart = 0L;
        try {
            timeStart = Long.parseLong(String.valueOf(job.get("timeStart")));
        } catch (NumberFormatException nfex) {
        }
        long timeCode = 0L;
        try {
            timeCode = Long.parseLong(String.valueOf(job.get("timeCode")));
        } catch (NumberFormatException nfex) {
        }
        long timeTotal = 0L;
        try {
            timeTotal = Long.parseLong(String.valueOf(job.get("timeTotal")));
        } catch (NumberFormatException nfex) {
        }

        RJobDetails jobDetails =
                new RJobDetails(id, name, descr, status, statusMsg, schedstart,
                        schedrepeat, schedinterval, onrepeat, project,
                        timeStart, timeCode, timeTotal, tag);

        return jobDetails;
    }

    public static RProjectExecutionDetails getProjectExecutionDetails(RProjectDetails project, Map executionMap, List<RData> robjects, List<Map> repofiles, String error, int errorCode, RLiveContext liveContext) {

        String execution = (String) executionMap.get("execution");
        String code = (String) executionMap.get("code");
        long timeStart = 0L;
        try {
            timeStart = Long.parseLong(String.valueOf(executionMap.get("timeStart")));
        } catch (NumberFormatException nfex) {
        }
        long timeCode = 0L;
        try {
            timeCode = Long.parseLong(String.valueOf(executionMap.get("timeCode")));
        } catch (NumberFormatException nfex) {
        }
        long timeTotal = 0L;
        try {
            timeTotal = Long.parseLong(String.valueOf(executionMap.get("timeTotal")));
        } catch (NumberFormatException nfex) {
        }
        String tag = (String) executionMap.get("tag");

        String console = (String) executionMap.get("console");
        List<String> warnings = (List<String>) executionMap.get("warnings");

        boolean interrupted = false;
        if (executionMap.get("interrupted") != null) {
            interrupted = (Boolean) executionMap.get("interrupted");
        }

        List<Map> results = (List<Map>) executionMap.get("results");
        List<RProjectResult> resultList = new ArrayList<RProjectResult>();

        if (results != null) {

            for (Map resultMap : results) {
                RProjectResultDetails details = REntityUtil.getProjectResultDetails(resultMap);
                RProjectResult projectResult =
                        new RProjectResultImpl(project, details, liveContext);
                resultList.add(projectResult);
            }
        }

        List<Map> artifacts = (List<Map>) executionMap.get("artifacts");

        List<RProjectFile> artifactList = new ArrayList<RProjectFile>();

        if (artifacts != null) {

            for (Map artifactMap : artifacts) {
                RProjectFileDetails details = REntityUtil.getProjectFileDetails(artifactMap);
                RProjectFile projectFile =
                        new RProjectFileImpl(project, details, liveContext);
                artifactList.add(projectFile);
            }
        }

        List<RRepositoryFile> repoList = new ArrayList<RRepositoryFile>();

        if (repofiles != null) {

            for (Map repoFileMap : repofiles) {
                RRepositoryFileDetails details = REntityUtil.getRepositoryFileDetails(repoFileMap);
                RRepositoryFile repoFile = new RRepositoryFileImpl(details, liveContext);
                repoList.add(repoFile);
            }
        }

        RProjectExecutionDetails execDetails =
                new RProjectExecutionDetails(execution, code,
                        timeStart, timeCode, timeTotal, tag, console, error, errorCode,
                        warnings, interrupted, resultList, artifactList, robjects, repoList);

        return execDetails;
    }

    public static RProjectFileDetails getProjectFileDetails(Map fileMap) {

        String filename = (String) fileMap.get("filename");
        String descr = (String) fileMap.get("descr");
        String type = (String) fileMap.get("type");
        long size = 0L;
        Object sizeObj = fileMap.get("length");
        if(sizeObj instanceof Long)
            size = (Long) sizeObj;
        else
        if(sizeObj instanceof Integer)
            size = ((Integer) sizeObj).longValue();
        String urlString = (String) fileMap.get("url");
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (Exception ex) {
        }

        RProjectFileDetails fileDetails =
                new RProjectFileDetails(filename, descr, type, size, url);

        return fileDetails;
    }

    public static RProjectResultDetails getProjectResultDetails(Map resultMap) {

        String execution = (String) resultMap.get("execution");
        String filename = (String) resultMap.get("filename");
        String type = (String) resultMap.get("type");
        long size = 0L;
        Object sizeObj = resultMap.get("length");
        if(sizeObj instanceof Long)
            size = (Long) sizeObj;
        else
        if(sizeObj instanceof Integer)
            size = ((Integer) sizeObj).longValue();
        String urlString = (String) resultMap.get("url");
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (Exception ex) {
        }

        RProjectResultDetails resultDetails =
                new RProjectResultDetails(execution, filename, type, size, url);

        return resultDetails;
    }

    public static RProjectPackageDetails getProjectPackageDetails(Map packageMap) {

        String name = (String) packageMap.get("name");
        String descr = (String) packageMap.get("descr");
        String repo = (String) packageMap.get("repo");
        String version = (String) packageMap.get("version");
        String status = (String) packageMap.get("status");
        boolean attached = false;
        if (packageMap.get("attached") != null) {
            attached = (Boolean) packageMap.get("attached");
        }

        RProjectPackageDetails packageDetails =
                new RProjectPackageDetails(name, descr, repo, version, status, attached);

        return packageDetails;
    }

}
