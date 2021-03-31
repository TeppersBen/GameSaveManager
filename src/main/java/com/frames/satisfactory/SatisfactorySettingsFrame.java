package com.frames.satisfactory;

import com.frames.core.SettingsFrame;
import com.utils.Settings;

public class SatisfactorySettingsFrame extends SettingsFrame {
    public SatisfactorySettingsFrame() {
        super(Settings.pathToSatisfactorySaveFolder, Settings.pathToSatisfactoryBackupFolder);
    }
}
