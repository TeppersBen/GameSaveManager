package com.frames.minecraft;

import com.frames.core.BackupFolderFrame;
import com.utils.Settings;

import java.util.ArrayList;

public class MinecraftBackupFolderFrame extends BackupFolderFrame {

    @Override
    public void refreshContent() {
        Object[][] data = savesManager.loadSavesContent(Settings.pathToMinecraftBackupFolder);
        worldsBox.getChildren().removeAll(worldTileList);
        worldTileList = new ArrayList<>();
        String previousName = "/";
        for (Object[] item : data) {
            MinecraftGameSaveTile tile = new MinecraftGameSaveTile(Settings.pathToMinecraftBackupFolder + item[0].toString(), item, this, true);
            if (previousName.equalsIgnoreCase("/") || !previousName.equalsIgnoreCase(determineSectionName(tile))) {
                addBackupSection(tile);
                previousName = determineSectionName(tile);
            }
            worldTileList.add(tile);
            appendBackupTileToSection(tile);
        }

        long sum = 0;
        for (Object[] item : data) {
            sum += (long) item[1];
        }
        totalFolderSize = sum;

        totalFolderSizeLabel.setText("Total size: " + totalFolderSize + " MiB");
    }
}
