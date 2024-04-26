package com.spotifyxp.updater.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.updater.Updater;
import com.spotifyxp.utils.ConnectionUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class FeedbackPanel {
    public static JButton feedbackupdaterdownloadbutton;
    public static JPanel feedbackupdatespanel;
    public static JTextField feedbackupdaterversionfield;

    public FeedbackPanel() {
        feedbackupdatespanel = new JPanel();
        feedbackupdatespanel.setBorder(new TitledBorder(null, PublicValues.language.translate("ui.updater.border"), TitledBorder.LEADING, TitledBorder.TOP, null, PublicValues.globalFontColor));
        feedbackupdatespanel.setBounds(10, 59, 566, 249);
        ContentPanel.feedbackpanel.add(feedbackupdatespanel);
        feedbackupdatespanel.setLayout(null);
        feedbackupdaterversionfield = new JTextField();
        feedbackupdaterversionfield.setBounds(10, 85, 230, 20);
        feedbackupdaterversionfield.setForeground(PublicValues.globalFontColor);
        feedbackupdatespanel.add(feedbackupdaterversionfield);
        feedbackupdaterversionfield.setColumns(10);
        feedbackupdaterdownloadbutton = new JButton(PublicValues.language.translate("ui.updater.downloadnewest"));
        feedbackupdaterdownloadbutton.setBounds(10, 149, 230, 23);
        feedbackupdatespanel.add(feedbackupdaterdownloadbutton);
        feedbackupdaterdownloadbutton.setForeground(PublicValues.globalFontColor);
        feedbackupdaterversionfield.setEditable(false);
        feedbackupdaterdownloadbutton.addActionListener(e -> new Updater().invoke());
    }
}
