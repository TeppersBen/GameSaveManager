package com.frames.sims4;

import com.frames.core.GameSaveTile;
import com.frames.core.SaveFolderFrame;
import com.jfoenix.controls.JFXButton;
import com.managers.SavesManager;

public class Sims4GameSaveTile extends GameSaveTile {

    public Sims4GameSaveTile(String path, Object[] data, SaveFolderFrame parentFrame, boolean isBackup) {
        super(path, data, parentFrame, isBackup);
        if (!isBackup) {
            addButton(new JFXButton("Wipe Default Checkpoints"), () -> parentFrame.setActionPerformedText(new SavesManager().wipeSims4Checkpoints(path, data[0].toString())));
        }
    }
}
