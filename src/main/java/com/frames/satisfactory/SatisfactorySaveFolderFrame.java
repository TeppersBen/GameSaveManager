package com.frames.satisfactory;

import com.frames.core.SaveFolderFrame;
import com.utils.Settings;

public class SatisfactorySaveFolderFrame extends SaveFolderFrame {
    public SatisfactorySaveFolderFrame() {
        super(Settings.pathToSatisfactorySaveFolder, Settings.pathToSatisfactoryBackupFolder);
    }
}
