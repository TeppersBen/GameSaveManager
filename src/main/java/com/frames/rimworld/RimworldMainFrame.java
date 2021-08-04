package com.frames.rimworld;

import com.frames.core.TabFrame;
import com.frames.minecraft.MinecraftAnvilSelectorFrame;
import com.frames.minecraft.MinecraftBackupFolderFrame;
import com.frames.minecraft.MinecraftSaveFolderFrame;
import com.frames.minecraft.MinecraftSettingsFrame;

public class RimworldMainFrame extends TabFrame {

    public RimworldMainFrame() {
        super(new MinecraftSaveFolderFrame(), new MinecraftBackupFolderFrame(), new MinecraftSettingsFrame());
    }
}