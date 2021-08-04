package com.frames.minecraft;

import com.frames.core.TabFrame;

public class MinecraftMainFrame extends TabFrame {


    public MinecraftMainFrame() {
        super("pathToMinecraftSaveFolder","pathToMinecraftBackupFolder");
        addTab("Minecraft Anvil Selector", new MinecraftAnvilSelectorFrame());
    }
}