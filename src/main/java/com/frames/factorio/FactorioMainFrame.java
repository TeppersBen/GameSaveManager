package com.frames.factorio;

import com.frames.core.TabFrame;

public class FactorioMainFrame extends TabFrame {
    public FactorioMainFrame() {
        super(new FactorioSaveFolderFrame(), new FactorioBackupFolderFrame(), new FactorioSettingsFrame());
    }
}
