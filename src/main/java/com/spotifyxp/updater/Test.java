package com.spotifyxp.updater;

import com.spotifyxp.PublicValues;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.updater.ui.UpdaterUI;

import java.io.File;

public class Test {
    public static void main(String[] args) {
        UpdaterValues.language = new libLanguage();
        UpdaterValues.language.setLanguageFolder("spxpuLang");
        UpdaterValues.language.setNoAutoFindLanguage("en");

        PublicValues.appLocation = new File("/Users/werwolf2303/Documents/GitHub/SpotifyXP/data").getAbsolutePath();
        new UpdaterUI().open(new String[]{PublicValues.appLocation, "https://ash-speed.hetzner.com/100MB.bin"});
    }
}
