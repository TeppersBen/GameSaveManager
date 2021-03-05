package com.frames.minecraft;

import com.frames.core.SettingsFrame;
import com.utils.Settings;

public class MinecraftSettingsFrame extends SettingsFrame {

    public MinecraftSettingsFrame() {
        super("pathToMinecraftSaveFolder", "pathToMinecraftBackupFolder");
        getFieldSavesFolderPath().setText(Settings.pathToMinecraftSaveFolder);
        getFieldBackupFolderPath().setText(Settings.pathToMinecraftBackupFolder);

        appendSaveFolderFieldAction(() -> {
            Settings.pathToMinecraftSaveFolder = getFieldSavesFolderPath().getText();

        });
        appendBackupFolderFieldAction(() -> {
            Settings.pathToMinecraftBackupFolder = getFieldBackupFolderPath().getText();
        });
    }
}
