package com.frames.minecraft;

import com.frames.core.GameSaveTile;
import com.frames.core.SaveFolderFrame;
import com.jfoenix.controls.JFXButton;
import com.managers.SavesManager;

public class MinecraftSavesManagerWorldTile extends GameSaveTile {

    public MinecraftSavesManagerWorldTile(String path, Object[] data, SaveFolderFrame parentFrame, boolean isBackup) {
        super(path, data, parentFrame, isBackup);
        addButton(new JFXButton("Wipe Unnecessary Chunks"), () -> {
            parentFrame.setActionPerformedText(new SavesManager().purgeUnnecessaryChunks(String.valueOf(data[0])));
            parentFrame.refreshContent();
        });
    }



}
