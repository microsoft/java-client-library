/*
 * DeployrUtil.java
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
package com.revo.deployr;

import com.revo.deployr.client.*;
import com.revo.deployr.client.data.RData;
import com.revo.deployr.client.data.RString;
import com.revo.deployr.client.factory.RDataFactory;
import com.revo.deployr.client.params.*;
import org.junit.Ignore;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Ignore
public class DeployrUtil {

    public static final String SAMPLE_CODE = "demo(graphics); png('sampleArtifact.png'); plot(rnorm(10));";
    public static final String DEFAULT_PORT = "7400";
    // Prefix used to denote file or object created for the
    // purpose of later loading from repository on preloadfile*
    // or on preloadobject* parameters.
    public static final String PLO_PREFIX = "PLO";

    public static String encodeString(String s) {
        StringBuffer sb = new StringBuffer();
        if (s != null) {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c <= 32) {
                    continue;
                }
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String getUniqueDirectoryName() {
        return new StringBuilder().append("Directory-")
                .append(UUID.randomUUID()).toString();
    }

    public static String getUniqueFileName(String extension) {
        return new StringBuilder().append("test-")
                .append(UUID.randomUUID())
                .append(".").append(extension).toString();
    }

    public static String getUniqueJobName() {
        return new StringBuilder().append("job")
                .append(UUID.randomUUID())
                .append(".").toString();
    }

    public static String getUniqueRName(String extension) {
        String name = new StringBuilder().append("R_")
                .append(UUID.randomUUID()).toString();
        name = name.replaceAll("-", "_");
        return name;
    }

    public static String getDataFromURL(URL url) {
        try {
            InputStream is = url.openConnection().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuffer buff = new StringBuffer("");
            String line;
            while ((line = reader.readLine()) != null) {
                buff.append(line);
            }
            reader.close();
            is.close();
            return buff.toString();
        } catch (IOException ex) {
            Logger.getLogger(DeployrUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String getDataFromStream(InputStream is) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuffer buff = new StringBuffer("");
            String line;
            while ((line = reader.readLine()) != null) {
                buff.append(line);
            }
            reader.close();
            is.close();
            return buff.toString();
        } catch (IOException ex) {
            Logger.getLogger(DeployrUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static RProject createPersistentProject(RUser rUser, String projectName, String projectDesc) {
        RProject rProject = null;

        try {
            rProject = rUser.createProject(projectName, projectDesc);
        } catch (Exception ex) {
            rProject = null;
        }

        // Tests using createProjectForExecutionOptions MUST assert
        // rProject returned on call is NOT NULL.
        return rProject;
    }

    public static RProject createTemporaryProject(RUser rUser) {

        RProject rProject = null;

        try {
            rProject = rUser.createProject();
        } catch (Exception ex) {
            rProject = null;
        }

        // Tests using createProjectForExecutionOptions MUST assert
        // rProject returned on call is NOT NULL.
        return rProject;
    }
    
    /*
     * createCreationOptionsProject
     *
     * Caller is responsible for releasing RProject and List<RRepositoryFile>
     * returned on this method. The releaseRepositoryArtifacts
     * method is provided to simplify the latter task.
     */

    public static Map<String, Object> createCreationOptionsProject(RUser rUser,
                                                                   int testDataDepth) {

        RProject rProject = null;
        RProjectFile projectFile = null;
        RRepositoryFile storedObject = null;
        DirectoryUploadOptions uploadOptions = null;
        List<RRepositoryFile> rProjectPreloadArtifacts = new ArrayList<RRepositoryFile>();

        try {
            rProject = rUser.createProject("ExecutionOptionsProject-Unit-Test",
                    "Created by unit test helper.");

            String pid = rProject.about().id.replaceAll("-", "");

            int depth = 0;
            while (depth < testDataDepth) {

                String objectName = pid + depth;

                // Create object in workspace to use on storageOptions.objects.
                rProject.executeCode(objectName + " <- rnorm(100);");
                // Create object in workspace to store in repository for later use on preloadobject*.
                rProject.executeCode(PLO_PREFIX + objectName + " <- " + objectName);

                // Create file in directory to use on storageOptions.files.
                String fileName = pid + depth;
                rProject.executeCode("save(" + objectName + ", file='" + fileName + ".txt', ascii=TRUE)");

                // Create binary object file in repository to use on preloadobject*.
                storedObject = rProject.storeObject(PLO_PREFIX + objectName,
                        "Unit Test ExecutionOptionsProject stored object.",
                        false, null, false, true);

                rProjectPreloadArtifacts.add(storedObject);

                depth++;
            }

            rProject.close();

        } catch (Exception ex) {
            try {
                releaseRepositoryArtifacts(rProjectPreloadArtifacts);
            } catch (Exception paex) {
            }
            if (storedObject != null) {
                try {
                    storedObject.delete();
                } catch (Exception soex) {
                }
            }
            try {
                rProject.delete();
            } catch (Exception pdex) {
            }
            rProject = null;
        }

        // Tests using createExecutionOptionsProject MUST assert
        // rProject returned on call is NOT NULL.
        Map map = new HashMap<String, Object>();
        map.put("rProject", rProject);
        map.put("rProjectPreloadArtifacts", rProjectPreloadArtifacts);
        return map;
    }

    /*
     * createCreationOptions
     *
     * Method returns a fully-populated ProjectCreationOptions that can
     * be used to test the majority of parameters supported on the new 7.0
     * standard execution model.
     *
     */
    public static ProjectCreationOptions createCreationOptions(RProject rProject,
                                                               int testDataDepth) {

        ProjectCreationOptions createOptions = new ProjectCreationOptions();
        ProjectPreloadOptions workspaceOptions = new ProjectPreloadOptions();
        ProjectPreloadOptions directoryOptions = new ProjectPreloadOptions();
        ProjectAdoptionOptions adoptionOptions = new ProjectAdoptionOptions();

        String pid = rProject.about().id;
        String pidSimplified = pid.replaceAll("-", "");

        List<RData> rInputsList = new ArrayList<RData>();

        int depth = 0;
        while (depth < testDataDepth) {

            // createOptions.preloadWorkspace
            if (workspaceOptions.author != null) {
                workspaceOptions.author = workspaceOptions.author + "," + "testuser";
                workspaceOptions.filename = workspaceOptions.filename + "," + PLO_PREFIX + pidSimplified + depth + ".rData";
            } else {
                workspaceOptions.author = "testuser";
                workspaceOptions.filename = PLO_PREFIX + pidSimplified + depth + ".rData";
            }

            // createOptions.preloadDirectory
            if (directoryOptions.author != null) {
                directoryOptions.author = directoryOptions.author + "," + "testuser";
                directoryOptions.filename = directoryOptions.filename + "," + PLO_PREFIX + pidSimplified + depth + ".rData";
            } else {
                directoryOptions.author = "testuser";
                directoryOptions.filename = PLO_PREFIX + pidSimplified + depth + ".rData";
            }

            String objectName = "inputOutputTestObject" + depth;

            // createOptions.rinputs
            rInputsList.add(RDataFactory.createNumeric(objectName, (double) depth));

            depth++;
        }

        createOptions.rinputs = rInputsList;

        adoptionOptions.adoptWorkspace = pid;
        adoptionOptions.adoptDirectory = pid;
        adoptionOptions.adoptPackages = pid;

        createOptions.preloadWorkspace = workspaceOptions;
        createOptions.preloadDirectory = directoryOptions;
        createOptions.adoptionOptions = adoptionOptions;


        return createOptions;
    }

    /*
     * createExecutionOptionsProject
     *
     * Caller is responsible for releasing RProject and List<RRepositoryFile>
     * returned on this method. The releaseRepositoryArtifacts
     * method is provided to simplify the latter task.
     */
    public static Map<String, Object> createExecutionOptionsProject(RUser rUser,
                                                                    int testDataDepth) {

        RProject rProject = null;
        RRepositoryFile storedObject = null;
        List<RRepositoryFile> rProjectPreloadArtifacts = new ArrayList<RRepositoryFile>();

        try {
            rProject = rUser.createProject("ExecutionOptionsProject-Unit-Test",
                    "Created by unit test helper.");

            String pid = rProject.about().id.replaceAll("-", "");

            int depth = 0;
            while (depth < testDataDepth) {

                String objectName = pid + depth;

                // Create object in workspace to use on storageOptions.objects.
                rProject.executeCode(objectName + " <- rnorm(100);");
                // Create object in workspace to store in repository for later use on preloadobject*.
                rProject.executeCode(PLO_PREFIX + objectName + " <- " + objectName);

                // Create file in directory to use on storageOptions.files.
                String fileName = pid + depth;
                rProject.executeCode("save(" + objectName + ", file='" + fileName + ".txt', ascii=TRUE)");

                // Create binary object file in repository to use on preloadobject*.
                storedObject = rProject.storeObject(PLO_PREFIX + objectName,
                        "Unit Test ExecutionOptionsProject stored object.",
                        false, null, false, true);

                rProjectPreloadArtifacts.add(storedObject);

                depth++;
            }

            rProject.close();

        } catch (Exception ex) {
            try {
                releaseRepositoryArtifacts(rProjectPreloadArtifacts);
            } catch (Exception paex) {
            }
            if (storedObject != null) {
                try {
                    storedObject.delete();
                } catch (Exception soex) {
                }
            }
            try {
                rProject.delete();
            } catch (Exception pdex) {
            }
            rProject = null;
        }

        // Tests using createExecutionOptionsProject MUST assert
        // rProject returned on call is NOT NULL.
        Map map = new HashMap<String, Object>();
        map.put("rProject", rProject);
        map.put("rProjectPreloadArtifacts", rProjectPreloadArtifacts);
        return map;
    }

    /*
     * createExecutionOptions
     *
     * Method returns a fully-populated ProjectExecutionOptions that can
     * be used to test the majority of parameters supported on the new 7.0
     * standard execution model.
     *
     * Caller is responsible for releasing any repository-files resulting
     * from the execOptions.storageOptions. The releaseRepositoryArtifacts
     * method is provided to simplify this task.
     */
    public static AnonymousProjectExecutionOptions createExecutionOptions(RProject rProject,
                                                                          int testDataDepth) {

        AnonymousProjectExecutionOptions execOptions =
                new AnonymousProjectExecutionOptions();
        ProjectPreloadOptions workspaceOptions = new ProjectPreloadOptions();
        ProjectPreloadOptions directoryOptions = new ProjectPreloadOptions();
        ProjectAdoptionOptions adoptionOptions = new ProjectAdoptionOptions();
        ProjectStorageOptions storageOptions = new ProjectStorageOptions();

        String pid = rProject.about().id;
        String pidSimplified = pid.replaceAll("-", "");

        List<RData> rInputsList = new ArrayList<RData>();
        List<String> rOutputsList = new ArrayList<String>();

        int depth = 0;
        while (depth < testDataDepth) {

            // execOptions.preloadWorkspace
            // Repo object existence depends on createExecutionOptionsProject.
            if (workspaceOptions.author != null) {
                workspaceOptions.author = workspaceOptions.author + "," + "testuser";
                workspaceOptions.filename = workspaceOptions.filename + "," + PLO_PREFIX + pidSimplified + depth + ".rData";
            } else {
                workspaceOptions.author = "testuser";
                workspaceOptions.filename = PLO_PREFIX + pidSimplified + depth + ".rData";
            }

            // execOptions.preloadDirectory
            // Repo file existence depends on createExecutionOptionsProject.
            if (directoryOptions.author != null) {
                directoryOptions.author = directoryOptions.author + "," + "testuser";
                directoryOptions.filename = directoryOptions.filename + "," + PLO_PREFIX + pidSimplified + depth + ".rData";
            } else {
                directoryOptions.author = "testuser";
                directoryOptions.filename = PLO_PREFIX + pidSimplified + depth + ".rData";
            }

            // execOptions.storageOptions
            // Any stored here needs to be handled by test cleanup.
            if (storageOptions.files != null) {
                storageOptions.files = storageOptions.files + "," + pidSimplified + depth + ".txt";
                storageOptions.objects = storageOptions.objects + "," + pidSimplified + depth;
            } else {
                storageOptions.files = pidSimplified + depth + ".txt";
                storageOptions.objects = pidSimplified + depth;
            }

            String objectName = "inputOutputTestObject" + depth;

            // execOptions.rinputs
            rInputsList.add(RDataFactory.createNumeric(objectName, (double) depth));

            // execOptions.routputs
            rOutputsList.add(objectName);

            depth++;
        }

        execOptions.rinputs = rInputsList;
        execOptions.routputs = rOutputsList;

        adoptionOptions.adoptWorkspace = pid;
        adoptionOptions.adoptDirectory = pid;
        adoptionOptions.adoptPackages = pid;

        storageOptions.workspace = pidSimplified + "Workspace.rData";

        execOptions.preloadWorkspace = workspaceOptions;
        execOptions.preloadDirectory = directoryOptions;
        execOptions.adoptionOptions = adoptionOptions;
        execOptions.storageOptions = storageOptions;

        return execOptions;
    }

    /*
     * createJobExecutionOptions
     *
     * Method returns a fully-populated JobExecutionOptions that can
     * be used to test the majority of parameters supported on the new 7.0
     * standard execution model.
     *
     * Caller is responsible for releasing any repository-files resulting
     * from the jobExecutionOptions.storageOptions. The releaseRepositoryArtifacts
     * method is provided to simplify this task.
     */
    public static JobExecutionOptions createJobExecutionOptions(RProject rProject,
                                                                String priority,
                                                                boolean storeNoProject,
                                                                int testDataDepth) {

        JobExecutionOptions jobExecutionOptions = new JobExecutionOptions();
        ProjectPreloadOptions workspaceOptions = new ProjectPreloadOptions();
        ProjectPreloadOptions directoryOptions = new ProjectPreloadOptions();
        ProjectAdoptionOptions adoptionOptions = new ProjectAdoptionOptions();
        ProjectStorageOptions storageOptions = new ProjectStorageOptions();

        String pid = rProject.about().id;
        String pidSimplified = pid.replaceAll("-", "");

        List<RData> rInputsList = new ArrayList<RData>();
        List<String> rOutputsList = new ArrayList<String>();

        int depth = 0;
        while (depth < testDataDepth) {

            // jobExecutionOptions.preloadWorkspace
            // Repo object existence depends on createJobExecutionOptions.
            if (workspaceOptions.author != null) {
                workspaceOptions.author = workspaceOptions.author + "," + "testuser";
                workspaceOptions.filename = workspaceOptions.filename + "," + PLO_PREFIX + pidSimplified + depth + ".rData";
            } else {
                workspaceOptions.author = "testuser";
                workspaceOptions.filename = PLO_PREFIX + pidSimplified + depth + ".rData";
            }

            // jobExecutionOptions.preloadDirectory
            // Repo file existence depends on createJobExecutionOptions.
            if (directoryOptions.author != null) {
                directoryOptions.author = directoryOptions.author + "," + "testuser";
                directoryOptions.filename = directoryOptions.filename + "," + PLO_PREFIX + pidSimplified + depth + ".rData";
            } else {
                directoryOptions.author = "testuser";
                directoryOptions.filename = PLO_PREFIX + pidSimplified + depth + ".rData";
            }


            String objectName = "inputOutputTestObject" + depth;

            // jobExecutionOptions.rinputs
            rInputsList.add(RDataFactory.createNumeric(objectName, (double) depth));

            // jobExecutionOptions.routputs
            rOutputsList.add(objectName);

            depth++;
        }

        jobExecutionOptions.priority = priority;
        jobExecutionOptions.noproject = storeNoProject;

        jobExecutionOptions.rinputs = rInputsList;
        jobExecutionOptions.routputs = rOutputsList;

        adoptionOptions.adoptWorkspace = pid;
        adoptionOptions.adoptDirectory = pid;
        adoptionOptions.adoptPackages = pid;

        jobExecutionOptions.preloadWorkspace = workspaceOptions;
        jobExecutionOptions.preloadDirectory = directoryOptions;
        jobExecutionOptions.adoptionOptions = adoptionOptions;
        jobExecutionOptions.storageOptions = storageOptions;

        return jobExecutionOptions;
    }

    /*
     * verifyRepositoryArtifacts
     *
     * Method parses ProjectStorageOptions to determine names of repository
     * artifacts that should exist following an execution using these
     * options. It then verifies that the set of repository artifacts
     * passed on the second argument match the expected repository artifacts.
     */
    public static void verifyRepositoryArtifacts(ProjectStorageOptions storageOptions,
                                                 List<RRepositoryFile> repoArtifacts)
            throws Exception {

        List<String> expectedNames = new ArrayList<String>();

        if (storageOptions != null) {

            if (storageOptions.files != null) {
                String[] files = storageOptions.files.split(",");
                for (String file : files) {
                    expectedNames.add(file.trim());
                }
            }
            if (storageOptions.objects != null) {
                String[] objects = storageOptions.objects.split(",");
                for (String object : objects) {
                    expectedNames.add(object.trim() + ".rData");
                }
            }
            if (storageOptions.workspace != null) {
                expectedNames.add(storageOptions.workspace.trim());
            }
        }

        verifyRepositoryArtifacts(expectedNames, repoArtifacts);
    }

    /*
     * verifyRepositoryArtifacts
     *
     * Method parses ProjectStorageOptions to determine names of repository
     * artifacts that should exist following an execution using these
     * options. It then verifies that the set of repository artifacts
     * passed on the second argument match the expected repository artifacts.
     */
    public static void verifyRepositoryArtifacts(ProjectCreationOptions createOptions,
                                                 List<RRepositoryFile> repoArtifacts)
            throws Exception {

        List<String> expectedNames = new ArrayList<String>();

        if (createOptions != null) {

            if (createOptions.preloadDirectory != null) {
                String[] files = createOptions.preloadDirectory.filename.split(",");
                for (String file : files) {
                    expectedNames.add(file.trim());
                }
            }

            if (createOptions.preloadWorkspace != null) {
                String[] objects = createOptions.preloadWorkspace.filename.split(",");
                for (String object : objects) {
                    expectedNames.add(object.trim());
                }
            }
        }

        verifyRepositoryArtifacts(expectedNames, repoArtifacts);
    }

    /*
     * verifyRepositoryArtifacts
     *
     * Method verifies that the set of expected repository artifact names passed
     * on the first argument match the actual repository artifacts passed on
     * the second argument.
     */
    public static void verifyRepositoryArtifacts(List<String> expectedNames,
                                                 List<RRepositoryFile> repoArtifacts)
            throws Exception {

        if (expectedNames != null && repoArtifacts != null) {

            HashSet<String> artifactNames = new HashSet<String>();

            for (RRepositoryFile repoFile : repoArtifacts) {
                try {
                    artifactNames.add(repoFile.about().filename);
                } catch (Exception ex) {
                    throw new Exception("repoFile.about failed.");
                }
            }

            for (String expectedName : expectedNames) {
                if (!artifactNames.contains(expectedName)) {
                    throw new Exception("Repository artifact " + expectedName + " not found.");
                }
            }
        }
    }

    /*
     * releaseRepositoryArtifacts
     *
     * Method attempts to delete each of the Repository files passed
     * on the first argument. If an exeception occurs on any deletion
     * that failure is reported by throwing an Exception.
     */
    public static void releaseRepositoryArtifacts(List<RRepositoryFile> repoArtifacts)
            throws Exception {

        Exception failure = null;

        if (repoArtifacts != null) {
            for (RRepositoryFile repoFile : repoArtifacts) {
                String filename = repoFile.about().filename;
                try {
                    repoFile.delete();
                } catch (Exception ex) {
                    // Capture failure but allow release loop to continue.
                    failure = new Exception("repoFile.delete failed: " + filename);
                }
            }
        }

        if (failure != null)
            throw failure;
    }

    /*
     * createTemporaryRScript
     *
     * Method returns an instance of new RRepositoryFile representing an
     * R script in the repository. Tests can use this method to create
     * a temporary R script to use when testing R script execution, therefore
     * removing any external dependencies.
     *
     * The scripts returned by this method consistently generate:
     * Execution results: 11
     * Execution artifacts: 1
     * These numbers can be asserted by tests using this method.
     */
    public static RRepositoryFile createTemporaryRScript(RUser rUser,
                                                         boolean published) throws Exception {

        RepoUploadOptions uploadOptions = new RepoUploadOptions();
        uploadOptions.filename = getUniqueFileName("R");
        uploadOptions.descr = uploadOptions.filename + " description.";
        uploadOptions.published = published;

        RRepositoryFile repositoryFile = null;

        try {
            repositoryFile = rUser.writeFile(SAMPLE_CODE, uploadOptions);
        } catch (Exception ex) {
            throw new Exception("rUser.writeFile failed: " + ex.getMessage());
        }

        return repositoryFile;
    }

    /*
     * createTemporaryRScriptOnDisk
     *
     * Method returns a full path to an RScript. Tests can use this method to create
     * a temporary R script to use when testing R script execution, therefore
     * removing any external dependencies.
     *
     * The scripts returned by this method consistently generate:
     * Execution results: 11
     * Execution artifacts: 1
     * These numbers can be asserted by tests using this method.
     */
    public static String createTemporaryRScriptOnDisk(RProject rProject) throws Exception {

        DirectoryUploadOptions uploadOptions = new DirectoryUploadOptions();
        ProjectExecutionOptions options = new ProjectExecutionOptions();
        RProjectExecution pathResult = null;
        RString rPathObject = null;
        String scriptPath = "";
        ArrayList rList = new ArrayList();
        String code = "path<-getwd()";
        uploadOptions.filename = getUniqueFileName("R");
        uploadOptions.descr = uploadOptions.filename + " description.";

        RProjectFile projectFile = null;

        try {
            projectFile = rProject.writeFile(SAMPLE_CODE, uploadOptions);
        } catch (Exception ex) {
            throw new Exception("rProject.writeFile failed: " + ex.getMessage());
        }

        // get path
        try {
            rList.add("path");
            options.routputs = rList;

            pathResult = rProject.executeCode(code, options);
            rPathObject = (RString) pathResult.about().workspaceObjects.get(0);
            scriptPath = rPathObject.getValue();
        } catch (Exception ex) {
            throw new Exception("rProject.executeCode failed: " + ex.getMessage());
        }

        return scriptPath + File.separator + uploadOptions.filename;
    }

}
