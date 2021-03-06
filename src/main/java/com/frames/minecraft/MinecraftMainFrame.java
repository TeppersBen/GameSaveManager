package com.frames.minecraft;

import com.frames.core.TabFrame;

public class MinecraftMainFrame extends TabFrame {


    public MinecraftMainFrame() {
        super(new MinecraftSaveFolderFrame(), new MinecraftBackupFolderFrame(), new MinecraftSettingsFrame());
        addTab("Minecraft Anvil Selector", new MinecraftAnvilSelectorFrame());
    }
}