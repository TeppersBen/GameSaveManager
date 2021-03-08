package com.frames.minecraft;

import com.frames.core.GameSaveTile;
import com.frames.core.SaveFolderFrame;
import com.jfoenix.controls.JFXButton;
import com.managers.SavesManager;

public class MinecraftGameSaveTile extends GameSaveTile {

    public MinecraftGameSaveTile(String path, Object[] data, SaveFolderFrame parentFrame, boolean isBackup) {
        super(path, data, parentFrame, isBackup);
        if (!isBackup) {
            addButton(new JFXButton("Chunk List"), () -> {
                parentFrame.getScrollPane().setContent(new MinecraftChunksFrame(parentFrame, path));
                parentFrame.setCenter(parentFrame.getScrollPane());
                parentFrame.getBottomPane().setVisible(false);
            });
            addButton(new JFXButton("Wipe Unnecessary Chunks"), () -> {
                parentFrame.setActionPerformedText(new SavesManager().purgeUnnecessaryChunks(String.valueOf(data[0])));
                parentFrame.refreshContent();
            });
        }
    }
}