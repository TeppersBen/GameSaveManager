package com.frames.satisfactory;

import com.frames.core.BackupFolderFrame;
import com.utils.Settings;

public class SatisfactoryBackupFolderFrame extends BackupFolderFrame {
    public SatisfactoryBackupFolderFrame() {
        super(Settings.pathToSatisfactorySaveFolder, Settings.pathToSatisfactoryBackupFolder);
    }
}
