package com.frames.satisfactory;

import com.frames.core.TabFrame;

public class SatisfactoryMainFrame extends TabFrame {
    public SatisfactoryMainFrame() {
        super(new SatisfactorySaveFolderFrame(), new SatisfactoryBackupFolderFrame(), new SatisfactorySettingsFrame());
    }
}
