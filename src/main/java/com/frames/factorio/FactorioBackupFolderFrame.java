package com.frames.factorio;

import com.frames.core.BackupFolderFrame;
import com.utils.Settings;

public class FactorioBackupFolderFrame extends BackupFolderFrame {
    public FactorioBackupFolderFrame() {
        super(Settings.pathToFactorioBackupFolder);
    }
}
