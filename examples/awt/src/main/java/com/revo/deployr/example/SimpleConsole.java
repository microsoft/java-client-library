/*
 * SimpleConsole.java
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
package com.revo.deployr.example;

import com.revo.deployr.client.*;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import com.revo.deployr.client.factory.RClientFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;

public class SimpleConsole extends JFrame {

    JTextField id = new JTextField(System.getProperty("username"), 6);
    JPasswordField pw = new JPasswordField(System.getProperty("password"), 6);
    JTextField server = new JTextField(System.getProperty("endpoint"), 12);
    JTextArea inputText = new JTextArea("demo(graphics)");
    JTextArea outputText = new JTextArea();
    JButton exeButton = new JButton("Execute Code");
    JButton exeSelButton = new JButton("Execute Selected Code");
    JButton cc = new JButton("Connect/Authenticate");
    JButton rc = new JButton("Sign Off & Release Client");

    RUser rUser = null;
    RClient rClient = null;
    RProject rProject = null;

    public SimpleConsole() {
        initComponents();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new SimpleConsole().setVisible(true);
            }
        });
    }

    private void initComponents() {
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                if (rClient != null) {
                    try {
                        if (rProject != null) {
                            rProject.close();
                            rProject = null;
                        }
                        rClient.logout(rUser);
                    } catch (RClientException ex) {
                        ex.printStackTrace();
                    } catch (RSecurityException ex) {
                        ex.printStackTrace();
                    }
                    rClient.release();
                }
            }
        });
        setTitle("DeployR Java Client Library Simple Console");

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel subPanel = new JPanel(new BorderLayout());
        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel idLabel = new JLabel("Username: ");
        textPanel.add(idLabel);
        textPanel.add(id);

        JLabel pwLabel = new JLabel("Password: ");
        textPanel.add(pwLabel);
        textPanel.add(pw);

        JLabel sLabel = new JLabel("Server: ");
        textPanel.add(sLabel);
        textPanel.add(server);

        cc.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createClient();
            }
        });
        textPanel.add(cc);
        rc.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                releaseClient();
            }
        });
        rc.setEnabled(false);
        textPanel.add(rc);

        subPanel.add(textPanel, BorderLayout.NORTH);

        JPanel executePanel = new JPanel(new BorderLayout());

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        exeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String code = inputText.getText();
                executeCode(code);
            }
        });
        bp.add(exeButton);
        exeButton.setEnabled(false);

        exeSelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String code = inputText.getSelectedText();
                if (code != null) {
                    executeCode(code);
                }
            }
        });
        bp.add(exeSelButton);
        exeSelButton.setEnabled(false);

        executePanel.add(bp, BorderLayout.NORTH);

        JPanel textAreaExecutePanel = new JPanel(new GridLayout(1, 2));
        JScrollPane isp = new JScrollPane(inputText);
        isp.setBorder(BorderFactory.createTitledBorder("Input Code"));
        textAreaExecutePanel.add(isp);

        JScrollPane osp = new JScrollPane(outputText);
        osp.setBorder(BorderFactory.createTitledBorder("Execute Results"));
        textAreaExecutePanel.add(osp);

        executePanel.add(textAreaExecutePanel, BorderLayout.CENTER);
        subPanel.add(executePanel, BorderLayout.CENTER);

        mainPanel.add(subPanel);
        this.getContentPane().add(mainPanel);
        pack();
        this.setSize(800, 400);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        this.setLocation(screenSize.width / 2 - (frameSize.width / 2),
                screenSize.height / 2 - (frameSize.height / 2));

    }

    private void createClient() {
        try {
            String url = server.getText();
            rClient = RClientFactory.createClient(url);
            RBasicAuthentication pAuthentication = new RBasicAuthentication(id.getText(), new String(pw.getPassword()));
            rUser = rClient.login(pAuthentication);
            if (rUser != null) {
                exeButton.setEnabled(true);
                exeSelButton.setEnabled(true);
                cc.setEnabled(false);
                rc.setEnabled(true);
            }
        } catch (RClientException ex) {
            ex.printStackTrace();
        } catch (RSecurityException ex) {
            ex.printStackTrace();
        }
    }

    private void releaseClient() {
        if (rClient != null) {
            try {
                if (rProject != null) {
                    rProject.close();
                    rProject = null;
                }
                rClient.logout(rUser);
                rUser = null;
            } catch (RClientException ex) {
                ex.printStackTrace();
            } catch (RSecurityException ex) {
                ex.printStackTrace();
            }
            rClient.release();
            rClient = null;
            cc.setEnabled(true);
            rc.setEnabled(false);
            exeButton.setEnabled(false);
            exeSelButton.setEnabled(false);
        }
    }

    private void executeCode(String code) {
        if (rUser != null) {
            try {
                if (rProject == null) {
                    rProject = rUser.createProject();
                }
                RProjectExecution codeResult = rProject.executeCode(code);
                outputText.append(codeResult.about().console + "\n");
                // check for plot file
                ArrayList results = (ArrayList) codeResult.about().results;
                for (int i = 0; i < results.size(); i++) {
                    RProjectResult pr = (RProjectResult) results.get(i);
                    URL url = pr.about().url;
                    if (url != null) {
                        createPlotWindow(url, (i + 1) * 20, (i + 1) * 20);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            outputText.append("Please create client before executing code\n");
        }
    }

    private void createPlotWindow(URL url, int x, int y) {
        ImagePanel panel = new ImagePanel(new ImageIcon(url).getImage());
        JFrame frame = new JFrame();
        frame.setTitle("DeployR Graph");
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocation(x, y);
        frame.setVisible(true);
    }

    class ImagePanel extends JPanel {

        private Image img;

        public ImagePanel(String img) {
            this(new ImageIcon(img).getImage());
        }

        public ImagePanel(Image img) {
            this.img = img;
            Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            setSize(size);
            setLayout(null);
        }

        public void paintComponent(Graphics g) {
            g.drawImage(img, 0, 0, null);
        }
    }
}
