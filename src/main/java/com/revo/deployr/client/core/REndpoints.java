/*
 * REndpoints.java
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
package com.revo.deployr.client.core;


/**
 * Represents DeployR API service endpoints.
 */
public interface REndpoints {

    public static final String RUSERLOGIN = "/r/user/login";
    public static final String RUSERABOUT = "/r/user/about";
    public static final String RUSERAUTOSAVE = "/r/user/autosave";
    public static final String RUSERRELEASE = "/r/user/release";
    public static final String RUSERLOGOUT = "/r/user/logout";

    public static final String RPROJECTLIST = "/r/project/list";
    public static final String RPROJECTCREATE = "/r/project/create";
    public static final String RPROJECTPOOL = "/r/project/pool";
    public static final String RPROJECTRECYCLE = "/r/project/recycle";
    public static final String RPROJECTCLOSE = "/r/project/close";
    public static final String RPROJECTDELETE = "/r/project/delete";
    public static final String RPROJECTPING = "/r/project/ping";
    public static final String RPROJECTGRANT = "/r/project/grant";
    public static final String RPROJECTABOUT = "/r/project/about";
    public static final String RPROJECTABOUTUPDATE = "/r/project/about/update";
    public static final String RPROJECTSAVE = "/r/project/save";
    public static final String RPROJECTSAVEAS = "/r/project/saveas";
    public static final String RPROJECTIMPORT = "/r/project/import";
    public static final String RPROJECTEXPORT = "/r/project/export";

    public static final String RPROJECTEXECUTECONSOLE = "/r/project/execute/console";
    public static final String RPROJECTEXECUTECODE = "/r/project/execute/code";
    public static final String RPROJECTEXECUTESCRIPT = "/r/project/execute/script";
    public static final String RPROJECTEXECUTEINTERRUPT = "/r/project/execute/interrupt";
    public static final String RPROJECTEXECUTEFLUSH = "/r/project/execute/flush";
    public static final String RPROJECTEXECUTEHISTORY = "/r/project/execute/history";

    public static final String RPROJECTEXECUTERESULTLIST = "/r/project/execute/result/list";
    public static final String RPROJECTEXECUTERESULTPRINT = "/r/project/execute/result/print";
    public static final String RPROJECTEXECUTERESULTDOWNLOAD = "/r/project/execute/result/download";
    public static final String RPROJECTEXECUTERESULTDELETE = "/r/project/execute/result/delete";

    public static final String RPROJECTWORKSPACELIST = "/r/project/workspace/list";
    public static final String RPROJECTWORKSPACEGET = "/r/project/workspace/get";
    public static final String RPROJECTWORKSPACEUPLOAD = "/r/project/workspace/upload";
    public static final String RPROJECTWORKSPACETRANSFER = "/r/project/workspace/transfer";
    public static final String RPROJECTWORKSPACEPUSH = "/r/project/workspace/push";
    public static final String RPROJECTWORKSPACESAVE = "/r/project/workspace/save";
    public static final String RPROJECTWORKSPACESTORE = "/r/project/workspace/store";
    public static final String RPROJECTWORKSPACELOAD = "/r/project/workspace/load";
    public static final String RPROJECTWORKSPACEDELETE = "/r/project/workspace/delete";

    public static final String RPROJECTDIRECTORYLIST = "/r/project/directory/list";
    public static final String RPROJECTDIRECTORYUPLOAD = "/r/project/directory/upload";
    public static final String RPROJECTDIRECTORYTRANSFER = "/r/project/directory/transfer";
    public static final String RPROJECTDIRECTORYWRITE = "/r/project/directory/write";
    public static final String RPROJECTDIRECTORYUPDATE = "/r/project/directory/update";
    public static final String RPROJECTDIRECTORYDOWNLOAD = "/r/project/directory/download";
    public static final String RPROJECTDIRECTORYDELETE = "/r/project/directory/delete";
    public static final String RPROJECTDIRECTORYSAVE = "/r/project/directory/save";
    public static final String RPROJECTDIRECTORYSTORE = "/r/project/directory/store";
    public static final String RPROJECTDIRECTORYLOAD = "/r/project/directory/load";

    public static final String RPROJECTPACKAGELIST = "/r/project/package/list";
    public static final String RPROJECTPACKAGEATTACH = "/r/project/package/attach";
    public static final String RPROJECTPACKAGEDETACH = "/r/project/package/detach";

    public static final String RREPOSITORYDIRECTORYLIST = "/r/repository/directory/list";
    public static final String RREPOSITORYDIRECTORYCREATE = "/r/repository/directory/create";
    public static final String RREPOSITORYDIRECTORYCOPY = "/r/repository/directory/copy";
    public static final String RREPOSITORYDIRECTORYMOVE = "/r/repository/directory/move";
    public static final String RREPOSITORYDIRECTORYUPLOAD = "/r/repository/directory/upload";
    public static final String RREPOSITORYDIRECTORYUPDATE = "/r/repository/directory/update";
    public static final String RREPOSITORYDIRECTORYARCHIVE = "/r/repository/directory/archive";
    public static final String RREPOSITORYDIRECTORYRENAME = "/r/repository/directory/rename";
    public static final String RREPOSITORYDIRECTORYDELETE = "/r/repository/directory/delete";
    public static final String RREPOSITORYDIRECTORYDOWNLOAD = "/r/repository/directory/download";

    public static final String RREPOSITORYFILELIST = "/r/repository/file/list";
    public static final String RREPOSITORYFILEFETCH = "/r/repository/file/fetch";
    public static final String RREPOSITORYFILEUPLOAD = "/r/repository/file/upload";
    public static final String RREPOSITORYFILEWRITE = "/r/repository/file/write";
    public static final String RREPOSITORYFILETRANSFER = "/r/repository/file/transfer";
    public static final String RREPOSITORYFILEPUBLISH = "/r/repository/file/publish";
    public static final String RREPOSITORYFILEUPDATE = "/r/repository/file/update";
    public static final String RREPOSITORYFILEGRANT = "/r/repository/file/grant";
    public static final String RREPOSITORYFILEREVERT = "/r/repository/file/revert";
    public static final String RREPOSITORYFILEDOWNLOAD = "/r/repository/file/download";
    public static final String RREPOSITORYFILEDIFF = "/r/repository/file/diff";
    public static final String RREPOSITORYFILEDELETE = "/r/repository/file/delete";
    public static final String RREPOSITORYFILECOPY = "/r/repository/file/copy";
    public static final String RREPOSITORYFILEMOVE = "/r/repository/file/move";

    public static final String RREPOSITORYSCRIPTLIST = "/r/repository/script/list";
    public static final String RREPOSITORYSCRIPTEXECUTE = "/r/repository/script/execute";
    public static final String RREPOSITORYSCRIPTRENDER = "/r/repository/script/render";
    public static final String RREPOSITORYSCRIPTINTERRUPT = "/r/repository/script/interrupt";

    public static final String RREPOSITORYSHELLEXECUTE = "/r/repository/shell/execute";

    public static final String RJOBLIST = "/r/job/list";
    public static final String RJOBSUBMIT = "/r/job/submit";
    public static final String RJOBSCHEDULE = "/r/job/schedule";
    public static final String RJOBQUERY = "/r/job/query";
    public static final String RJOBCANCEL = "/r/job/cancel";
    public static final String RJOBDELETE = "/r/job/delete";

}
