package com.spotifyxp.updater;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.injector.InjectorInterface;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.threading.DefThread;
import com.spotifyxp.updater.panels.FeedbackPanel;
import com.spotifyxp.updater.ui.UpdaterUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Initiator implements InjectorInterface {
    public static void main(String[] args) {
        UpdaterValues.language = new libLanguage();
        UpdaterValues.language.setLanguageFolder("spxpuLang");
        UpdaterValues.language.setNoAutoFindLanguage(PublicValues.config.getString(ConfigValues.language.name));

        new UpdaterUI().open(args);
    }

    @Override
    public String getIdentifier() {
        return "SpotifyXPUpdater";
    }

    @Override
    public String getVersion() {
        return "v1.0";
    }

    @Override
    public String getAuthor() {
        return "Werwolf2303";
    }

    @Override
    public void init() {
        Events.subscribe(SpotifyXPEvents.onFrameReady.getName(), new Runnable() {
            @Override
            public void run() {
                JMenuBar bar = ContentPanel.bar;
                JMenu file = bar.getMenu(0);
                JMenuItem checkUpdate = new JMenuItem(UpdaterValues.language.translate("updater.menubar.title"));
                file.add(checkUpdate, file.getItemCount() - 1);

                checkUpdate.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        onClickedUpdater();
                    }
                });

                new FeedbackPanel();
            }
        });
    }

    public void onClickedUpdater() {
        Updater.UpdateInfo info = new Updater().updateAvailable();
        DefThread thread = new DefThread(() -> {
            if (info.updateAvailable) {
                String version = info.version;
                FeedbackPanel.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.available") + version);
            } else {
                if (new Updater().isNightly()) {
                    FeedbackPanel.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.nightly"));
                    FeedbackPanel.feedbackupdaterdownloadbutton.setVisible(false);
                } else {
                    FeedbackPanel.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.notavailable"));
                }
            }
            if(ContentPanel.legacyswitch.isEnabled()) {
                ContentPanel.legacyswitch.setSelectedIndex(ContentPanel.legacyswitch.getTabCount());
            }
        });
        thread.start();
    }
}
