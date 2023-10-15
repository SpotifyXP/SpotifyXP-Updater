package com.spotifyxp.updater;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class UpdaterUI extends JPanel {
    JProgressBar progressbar;
    JLabel progresslabel;
    JButton closebutton;

    public UpdaterUI() {
        setBounds(100, 100, 450, 135);
        setLayout(null);
        progressbar = new JProgressBar();
        progressbar.setBounds(6, 36, 438, 20);
        add(progressbar);

        progresslabel = new JLabel("Progress");
        progresslabel.setBounds(6, 6, 252, 16);
        add(progresslabel);

        closebutton = new JButton("Close");
        closebutton.setBounds(6, 72, 438, 29);
        closebutton.setEnabled(false);
        add(closebutton);

        closebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    void startDownload(String[] args) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(args[0]).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(args[1])) {
            progressbar.setMaximum(in.available());
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                progressbar.setValue(bytesRead);
            }
        } catch (IOException e) {
        }
        closebutton.setEnabled(true);
    }

    public void open(String[] args) {
        JFrame frame = new JFrame("SpotifyXP - Updater");
        frame.getContentPane().add(this);
        frame.setVisible(true);
        frame.pack();
        startDownload(args);
    }
}
