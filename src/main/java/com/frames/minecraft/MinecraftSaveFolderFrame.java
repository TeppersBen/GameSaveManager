package com.frames.minecraft;

import com.frames.core.SaveFolderFrame;
import com.utils.Settings;

public class MinecraftSaveFolderFrame extends SaveFolderFrame {

    public MinecraftSaveFolderFrame() {
        super(Settings.pathToMinecraftSaveFolder);
    }
}