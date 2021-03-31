package com.frames.factorio;

import com.frames.core.SettingsFrame;
import com.utils.Settings;

public class FactorioSettingsFrame extends SettingsFrame {
    public FactorioSettingsFrame() {
        super(Settings.pathToFactorioSaveFolder, Settings.pathToFactorioBackupFolder);
    }
}
