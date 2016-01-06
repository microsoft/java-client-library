/*
 * RUserDefaultRepositoryFileCallsTest.java
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
package com.revo.deployr.client.api;

import com.revo.deployr.DeployrUtil;
import com.revo.deployr.client.RClient;
import com.revo.deployr.client.RClientException;
import com.revo.deployr.client.RRepositoryFile;
import com.revo.deployr.client.RUser;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.factory.RClientFactory;
import com.revo.deployr.client.params.RepoUploadOptions;
import org.junit.*;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;

public class RUserDefaultRepositoryFileCallsTest {

    RClient rClient = null;
    RUser rUser = null;

    public RUserDefaultRepositoryFileCallsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        try {
            String url = System.getProperty("connection.protocol") +
                            System.getProperty("connection.endpoint");
            if (url == null) {
                fail("setUp: connection.[protocol|endpoint] null.");
            }
            boolean allowSelfSigned = 
                Boolean.valueOf(System.getProperty("allow.SelfSignedSSLCert"));
            rClient =RClientFactory.createClient(url, allowSelfSigned);
            RBasicAuthentication rAuthentication = new RBasicAuthentication("testuser", System.getProperty("password.testuser"));
            String expResultName = "testuser";
            rUser = rClient.login(rAuthentication);
            assertNotNull(rUser);

        } catch (Exception ex) {

            if (rClient != null) {
                rClient.release();
            }
            fail("setUp: " + ex);
        }
    }

    @After
    public void tearDown() {
        if (rClient != null) {
            rClient.release();
        }
    }

    /**
     * Test of about method, of class RRepositoryFile.
     */
    @Test
    public void testUserRepositoryAboutFile() {

        // Test variables.
        String text = "this is a line of text";
        String actualRepoFileName = "";
        String actualRepoFileDesc = "";
        String expRepoFileName = "";
        String expRepoFileDesc = "";
        RepoUploadOptions options = null;
        RRepositoryFile repoFile = null;
        InputStream downStream = null;
        String urlData = "";

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        expRepoFileName = DeployrUtil.getUniqueFileName("txt");
        expRepoFileDesc = "Repository File About";
        options = new RepoUploadOptions();
        options.descr = expRepoFileDesc;
        options.filename = expRepoFileName;

        try {
            repoFile = rUser.writeFile(text, options);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.writeFile failed: ";
        }

        if (exception == null) {
            try {
                actualRepoFileName = repoFile.about().filename;
                actualRepoFileDesc = repoFile.about().descr;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoFile.about failed: ";
            }
        }

        if (exception == null) {
            try {
                downStream = repoFile.download();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoFile.download failed: ";
            }
        }

        if (exception == null) {
            urlData = DeployrUtil.getDataFromStream(downStream);
        }

        // Test cleanup.
        try {
            if (repoFile != null) {
                repoFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "repoFile.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertEquals(expRepoFileName, actualRepoFileName);
            assertEquals(expRepoFileDesc, actualRepoFileDesc);
            assertEquals(DeployrUtil.encodeString(text),
                            DeployrUtil.encodeString(urlData));
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of listFiles method, of class RUserRepositoryFileCalls.
     */
    @Test
    public void testUserRepositoryListFiles() {

        // Test variables.
        String text = "this is a line of text";
        String actualRepoFileName = "";
        String actualRepoFileDesc = "";
        String expRepoFileName = "";
        String expRepoFileDesc = "";
        RepoUploadOptions options = null;
        RRepositoryFile repoFile = null;
        InputStream downStream = null;
        String urlData = "";
        List<RRepositoryFile> listFiles = null;
        boolean repoFileFound = false;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        expRepoFileName = DeployrUtil.getUniqueFileName("txt");
        expRepoFileDesc = "Repository File List";
        options = new RepoUploadOptions();
        options.descr = expRepoFileDesc;
        options.filename = expRepoFileName;

        try {
            repoFile = rUser.writeFile(text, options);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.writeFile failed: ";
        }

        if (exception == null) {
            try {
                actualRepoFileName = repoFile.about().filename;
                actualRepoFileDesc = repoFile.about().descr;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoFile.about failed: ";
            }
        }

        if (exception == null) {
            try {
                downStream = repoFile.download();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoFile.download failed: ";
            }
        }

        if (exception == null) {
            try {
                listFiles = rUser.listFiles();
                // search for filename
                for (RRepositoryFile file : listFiles) {
                    if (file.about().filename.equalsIgnoreCase(expRepoFileName)) {
                        repoFileFound = true;
                        break;
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listFiles failed: ";
            }
        }

        if (exception == null) {
            urlData = DeployrUtil.getDataFromStream(downStream);
        }

        // Test cleanup.
        try {
            if (repoFile != null) {
                repoFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "repoFile.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertTrue(listFiles.size() > 0);
            assertTrue(repoFileFound);
            assertEquals(expRepoFileName, actualRepoFileName);
            assertEquals(expRepoFileDesc, actualRepoFileDesc);
            assertEquals(DeployrUtil.encodeString(text), DeployrUtil.encodeString(urlData));
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test RUserRepositoryFileCalls.listFiles(categoryFilter, directoryFilter).
     */
    @Test
    public void testUserRepositoryListFilesGoodFilters() {

        // Test variables.
        List<RRepositoryFile> listFilesExampleDirectory = null;
        List<RRepositoryFile> listFilesExampleScripts = null;
        List<RRepositoryFile> listFilesExampleBinary = null;
        String exampleFraudScoreDirectory = "example-fraud-score";
        int fraudExampleTotalFileCount = 2;
        int fraudExampleScriptFileCount = 1;
        boolean fraudExampleScriptsAreScripts = false;
        int fraudExampleBinaryFileCount = 1;
        boolean fraudExampleBinaryAreBinary = false;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            listFilesExampleDirectory =
                rUser.listFiles((RRepositoryFile.Category) null,
                                        exampleFraudScoreDirectory);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.listFiles(null, directory) failed: ";
        }

        if(exception == null) {

            try {
                listFilesExampleScripts =
                    rUser.listFiles(RRepositoryFile.Category.RSCRIPT,
                                        exampleFraudScoreDirectory);

                for(RRepositoryFile scriptFile : listFilesExampleScripts) {
                    if(scriptFile.about().category !=
                                        RRepositoryFile.Category.RSCRIPT) {
                        fraudExampleScriptsAreScripts = false;
                        break;
                    } else {
                        fraudExampleScriptsAreScripts = true;
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listFiles(RSCRIPT, directory) failed: ";
            }
        }

        if(exception == null) {

            try {
                listFilesExampleBinary =
                    rUser.listFiles(RRepositoryFile.Category.RBINARY,
                                        exampleFraudScoreDirectory);

                for(RRepositoryFile binFile : listFilesExampleBinary) {
                    if(binFile.about().category !=
                                        RRepositoryFile.Category.RBINARY) {
                        fraudExampleBinaryAreBinary = false;
                        break;
                    } else {
                        fraudExampleBinaryAreBinary = true;
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listFiles(RSCRIPT, directory) failed: ";
            }
        }

        if (exception == null) {
            // Test assertions.
            assertEquals(fraudExampleTotalFileCount, listFilesExampleDirectory.size());
            assertEquals(fraudExampleScriptFileCount, listFilesExampleScripts.size());
            assertTrue(fraudExampleScriptsAreScripts);
            assertEquals(fraudExampleBinaryFileCount, listFilesExampleBinary.size());
            assertTrue(fraudExampleBinaryAreBinary);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test RUserRepositoryFileCalls.listFiles(categoryFilter, directoryFilter).
     */
    @Test
    public void testUserRepositoryListFilesBadFilters() {

        // Test variables.
        List<RRepositoryFile> listFiles = null;
        RClientException clientEx = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        try {
            listFiles =
                rUser.listFiles((RRepositoryFile.Category) null,
                                        "dir-not-found");
        } catch (RClientException cex) {
            clientEx = cex;
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.listFiles(null, dir-not-found) failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertNotNull(clientEx);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of listFiles method, of class RUserRepositoryFileCalls.
     */
    @Test
    public void testUserRepositoryListFilesShowPublicFiles() {

        // Test variables.
        String expRepoFileName = "Histogram of Auto Sales.R";
        List<RRepositoryFile> listFiles = null;
        String expAuthor = "testuser";
        String actualAuthor = "";
        boolean repoFileFound = false;
        boolean archived = false;
        boolean shared = false;
        boolean published = true;
        boolean isPublished = false;
        RUser rUserManager = null;
        RBasicAuthentication rAuthentication = null;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";

        // Test.

        // sign-out as testuser
        String expResultName = "testuser";
        try {
            rClient.logout(rUser);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rClient.logout failed: ";
        }

        // sign-in as admin.  Try 5 times
        if (exception == null) {
            rAuthentication = new RBasicAuthentication("admin", System.getProperty("password.admin"));
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    exception = ex;
                    exceptionMsg = "Thread.sleep failed: ";
                }
                if (exception == null) {
                    try {
                        rUserManager = rClient.login(rAuthentication);
                    } catch (Exception ex) {
                        if (i < 5) {
                            continue;
                        }
                        exception = ex;
                        exceptionMsg = "rClient.login failed: ";
                    }
                }
            }
        }

        if (exception == null) {
            try {
                listFiles = rUserManager.listFiles(archived, shared, published);
                // search for filename
                for (RRepositoryFile file : listFiles) {
                    if (file.about().filename.equalsIgnoreCase(expRepoFileName)) {
                        repoFileFound = true;
                        actualAuthor = file.about().author;
                        isPublished = file.about().published;
                        break;
                    }
                }
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.listFiles failed: ";
            }
        }

        if (exception == null) {
            // Test assertions.
            assertTrue(repoFileFound);
            assertTrue(isPublished);
            assertEquals(expAuthor, actualAuthor);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }
    }

    /**
     * Test of uploadFile method, of class RUserRepositoryFileCalls.
     */
    @Test
    public void testUserRepositoryUploadFile() {

        // Test variables.
        String actualRepoFileName = "";
        String actualRepoFileDesc = "";
        String expRepoFileName = "";
        String expRepoFileDesc = "";
        File file = new File("/etc/hosts");
        RepoUploadOptions options = null;
        RRepositoryFile repoFile = null;
        InputStream downStream = null;
        String urlData = "";

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        //Test.
        expRepoFileName = DeployrUtil.getUniqueFileName("txt");
        expRepoFileDesc = "hosts file";
        options = new RepoUploadOptions();
        options.descr = expRepoFileDesc;
        options.filename = expRepoFileName;

        try {
            repoFile = rUser.uploadFile(new FileInputStream(file), options);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.uploadFile failed: ";
        }

        if (exception == null) {
            try {
                actualRepoFileName = repoFile.about().filename;
                actualRepoFileDesc = repoFile.about().descr;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoFile.about failed: ";
            }
        }

        if (exception == null) {
            try {
                downStream = repoFile.download();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoFile.download failed: ";
            }
        }

        if (exception == null) {
            urlData = DeployrUtil.getDataFromStream(downStream);
        }

        // Test cleanup.
        try {
            if (repoFile != null) {
                repoFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "repoFile.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertEquals(expRepoFileName, actualRepoFileName);
            assertEquals(expRepoFileDesc, actualRepoFileDesc);
            assertNotNull(urlData);
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of writeFile method, of class RUserRepositoryFileCalls.
     */
    @Test
    public void testUserRepositoryWriteFile() {

        // Test variables.
        String text = "this is a line of text";
        String actualRepoFileName = "";
        String actualRepoFileDesc = "";
        String expRepoFileName = "";
        String expRepoFileDesc = "";
        RepoUploadOptions options = null;
        RRepositoryFile repoFile = null;
        InputStream downStream = null;
        String urlData = "";

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        expRepoFileName = DeployrUtil.getUniqueFileName("txt");
        expRepoFileDesc = "Repository Write File";
        options = new RepoUploadOptions();
        options.descr = expRepoFileDesc;
        options.filename = expRepoFileName;

        try {
            repoFile = rUser.writeFile(text, options);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.writeFile failed: ";
        }

        if (exception == null) {
            try {
                actualRepoFileName = repoFile.about().filename;
                actualRepoFileDesc = repoFile.about().descr;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoFile.about failed: ";
            }
        }

        if (exception == null) {
            try {
                downStream = repoFile.download();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoFile.download failed: ";
            }
        }

        if (exception == null) {
            urlData = DeployrUtil.getDataFromStream(downStream);
        }

        // Test cleanup.
        try {
            if (repoFile != null) {
                repoFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "repoFile.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertEquals(expRepoFileName, actualRepoFileName);
            assertEquals(expRepoFileDesc, actualRepoFileDesc);
            assertEquals(DeployrUtil.encodeString(text), DeployrUtil.encodeString(urlData));
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of writeFile method, of class RUserRepositoryFileCalls.
     */
    @Test
    public void testUserRepositoryDownloadFile() {

        // Test variables.
        String text = "this is a line of text";
        String actualRepoFileName = "";
        String actualRepoFileDesc = "";
        String expRepoFileName = "";
        String expRepoFileDesc = "";
        RepoUploadOptions options = null;
        RRepositoryFile repoFile = null;
        InputStream downStream = null;
        String urlData = "";

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        expRepoFileName = DeployrUtil.getUniqueFileName("txt");
        expRepoFileDesc = "User Repository Download File";
        options = new RepoUploadOptions();
        options.descr = expRepoFileDesc;
        options.filename = expRepoFileName;

        try {
            repoFile = rUser.writeFile(text, options);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.writeFile failed: ";
        }

        if (exception == null) {
            try {
                actualRepoFileName = repoFile.about().filename;
                actualRepoFileDesc = repoFile.about().descr;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoFile.about failed: ";
            }
        }

        if (exception == null) {
            try {
                downStream = repoFile.download();
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoFile.download failed: ";
            }
        }

        if (exception == null) {
            urlData = DeployrUtil.getDataFromStream(downStream);
        }

        // Test cleanup.
        try {
            if (repoFile != null) {
                repoFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "repoFile.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertEquals(expRepoFileName, actualRepoFileName);
            assertEquals(expRepoFileDesc, actualRepoFileDesc);
            assertEquals(DeployrUtil.encodeString(text), DeployrUtil.encodeString(urlData));
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }

    /**
     * Test of transferFile method, of class RUserRepositoryFileCalls.
     */
    @Test
    public void testUserRepositoryTransferFile() {

        // Test variables.
        String text = "this is a line of text";
        String actualRepoFileName = "";
        String actualRepoFileDesc = "";
        String actualTransFileName = "";
        String actualTransFileDesc = "";
        String expRepoFileName = "";
        String expRepoFileDesc = "";
        String expTransFileName = "";
        String expTransFileDesc = "";
        RepoUploadOptions options = null;
        RRepositoryFile repoFile = null;
        String urlData = "";
        String urlTransData = "";
        URL repoURL = null;
        URL repoTransURL = null;
        RRepositoryFile repoTransFile = null;
        long repoFileSize = 0;
        long repoTransFileSize = 0;

        // Test error handling.
        Exception exception = null;
        String exceptionMsg = "";
        Exception cleanupException = null;
        String cleanupExceptionMsg = "";

        // Test.
        expRepoFileName = DeployrUtil.getUniqueFileName("txt");
        expRepoFileDesc = "User Repository Download File";
        options = new RepoUploadOptions();
        options.descr = expRepoFileDesc;
        options.filename = expRepoFileName;

        try {
            repoFile = rUser.writeFile(text, options);
        } catch (Exception ex) {
            exception = ex;
            exceptionMsg = "rUser.writeFile failed: ";
        }

        if (exception == null) {
            try {
                actualRepoFileName = repoFile.about().filename;
                actualRepoFileDesc = repoFile.about().descr;
                repoFileSize = repoFile.about().size;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoFile.about failed: ";
            }
        }

        if (exception == null) {
            try {
                repoURL = repoFile.about().url;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoFile.download failed: ";
            }
        }

        if (exception == null) {
            expTransFileName = DeployrUtil.getUniqueFileName("txt");
            expTransFileDesc = "User Repository Transfer File";
            options = new RepoUploadOptions();
            options.descr = expTransFileDesc;
            options.filename = expTransFileName;
            try {
                repoTransFile = rUser.transferFile(repoURL, options);
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "rUser.transferFile failed: ";
            }
        }

        if (exception == null) {
            try {
                actualTransFileName = repoTransFile.about().filename;
                actualTransFileDesc = repoTransFile.about().descr;
                repoTransFileSize = repoTransFile.about().size;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoTransFile.about failed: ";
            }
        }

        if (exception == null) {
            try {
                repoTransURL = repoTransFile.about().url;
            } catch (Exception ex) {
                exception = ex;
                exceptionMsg = "repoTransFile.download failed: ";
            }
        }

        // Test cleanup.
        try {
            if (repoFile != null) {
                repoFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "repoFile.delete failed: ";
        }

        try {
            if (repoTransFile != null) {
                repoTransFile.delete();
            }
        } catch (Exception ex) {
            cleanupException = ex;
            cleanupExceptionMsg = "repoTransFile.delete failed: ";
        }

        if (exception == null) {
            // Test assertions.
            assertEquals(expRepoFileName, actualRepoFileName);
            assertEquals(expRepoFileDesc, actualRepoFileDesc);
            assertEquals(repoTransFileSize, repoFileSize);
            assertEquals(DeployrUtil.encodeString(urlTransData),
                            DeployrUtil.encodeString(urlData));
        } else {
            fail(exceptionMsg + exception.getMessage());
        }

        // Test cleanup errors.
        if (cleanupException != null) {
            fail(cleanupExceptionMsg + cleanupException.getMessage());
        }
    }
}
