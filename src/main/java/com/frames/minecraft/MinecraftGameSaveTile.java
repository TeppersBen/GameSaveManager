package com.frames.minecraft;

import com.frames.core.GameSaveTile;
import com.frames.core.SaveFolderFrame;
import com.jfoenix.controls.JFXButton;
import com.managers.SavesManager;
import com.utils.Settings;

public class MinecraftGameSaveTile extends GameSaveTile {

    public MinecraftGameSaveTile(String path, Object[] data, SaveFolderFrame parentFrame, boolean isBackup) {
        super(path, data, parentFrame, isBackup);
        addButton(new JFXButton("Wipe Unnecessary Chunks"), () -> {
            parentFrame.setActionPerformedText(new SavesManager().purgeUnnecessaryChunks(String.valueOf(data[0])));
            parentFrame.refreshContent();
        });
        if (!isBackup) {
            addButton(new JFXButton("Remove World"), () -> {
                parentFrame.setActionPerformedText(new SavesManager().purgeWorldFolder(Settings.pathToMinecraftSaveFolder, getWorldName()));
                parentFrame.refreshContent();
            });
        }
    }
}