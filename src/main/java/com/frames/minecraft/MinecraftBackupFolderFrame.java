package com.frames.minecraft;

import com.frames.core.BackupFolderFrame;
import com.utils.Settings;

public class MinecraftBackupFolderFrame extends BackupFolderFrame {

    public MinecraftBackupFolderFrame() {
        super(Settings.pathToMinecraftSaveFolder, Settings.pathToMinecraftBackupFolder);
    }
}