package com.spotifyxp.updater.ui;

import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.threading.DefThread;
import com.spotifyxp.updater.Updater;
import com.spotifyxp.updater.UpdaterValues;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class UpdaterUI extends JFrame {
    private JPanel contents;
    private JButton updateButton;
    private JProgressBar progress;
    private JLabel progresslabel;
    private JButton cancelButton;
    private JTextPane changelog;
    private String downloadUrl;
    private String appLocation;
    private boolean allowClosure = false;

    public UpdaterUI() {
        setLocation(ContentPanel.frame.getLocation());
        setContentPane(contents);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle(UpdaterValues.language.translate("updater.availableui.title"));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(allowClosure) System.exit(0);
            }
        });

        updateButton.setText(UpdaterValues.language.translate("updater.availableui.updateButton"));
        updateButton.addActionListener(e -> {
            allowClosure = false;
            updateButton.setEnabled(false);
            cancelButton.setEnabled(false);
            DefThread downloadThread = new DefThread(() -> {
                download();
                cancelButton.setEnabled(true);
                cancelButton.setText(UpdaterValues.language.translate("updater.availableui.cancelButtonDone"));
                allowClosure = true;
            });
            downloadThread.start();
        });

        cancelButton.setText(UpdaterValues.language.translate("updater.availableui.cancelButton"));
        cancelButton.addActionListener(e -> System.exit(0));

        progresslabel.setText(UpdaterValues.language.translate("updater.availableui.progresslabel"));

        changelog.setEditable(false);
        changelog.setContentType("text/html");
        changelog.setText(new Updater().getChangelogForNewest());}

    private void download() {
        try {
            int dotState = 0;
            progresslabel.setText(UpdaterValues.language.translate("updater.availableui.updating"));
            progress.setMaximum(100000);
            URL url = new URL(downloadUrl);
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            long completeFileSize = httpConnection.getContentLength();
            BufferedInputStream in = new BufferedInputStream(httpConnection.getInputStream());
            FileOutputStream fos = new FileOutputStream(new File(appLocation, "SpotifyXP.jar"));
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;
                final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize)) * 100000d);
                progress.setValue(currentProgress);
                switch (dotState) {
                    case 0:
                        progresslabel.setText(UpdaterValues.language.translate("updater.availableui.updating") + ".");
                        dotState++;
                        break;
                    case 1:
                        progresslabel.setText(UpdaterValues.language.translate("updater.availableui.updating") + "..");
                        dotState++;
                        break;
                    case 2:
                        progresslabel.setText(UpdaterValues.language.translate("updater.availableui.updating")+ "...");
                        dotState = 0;
                        break;
                }
                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
        } catch (SocketTimeoutException socketTimeoutException) {
            download();
        } catch (Exception exception) {
            progress.setForeground(Color.red);
            progresslabel.setText(UpdaterValues.language.translate("updater.availableui.failed").replace("%s", exception.getMessage()));
        }
        progresslabel.setText(UpdaterValues.language.translate("updater.availableui.done"));
    }

    public void open(String[] args) {
        this.appLocation = args[0];
        this.downloadUrl = args[1];
        pack();
        setVisible(true);
    }

    private void createUIComponents() {
        contents = new JPanel();
    }
}
