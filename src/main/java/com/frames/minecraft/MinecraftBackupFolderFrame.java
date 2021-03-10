package com.frames.minecraft;

import com.frames.core.BackupFolderFrame;
import com.utils.Settings;

public class MinecraftBackupFolderFrame extends BackupFolderFrame {

    @Override
    public void refreshContent() {
        activateIndicator();
        Object[][] data = savesManager.loadSavesContent(Settings.pathToMinecraftBackupFolder);
        if (data != null) {
            wipeAllSections();
            String previousName = "/";
            for (Object[] item : data) {
                MinecraftGameSaveTile tile = new MinecraftGameSaveTile(Settings.pathToMinecraftBackupFolder + item[0].toString(), item, this, true);
                if (previousName.equalsIgnoreCase("/") || !previousName.equalsIgnoreCase(determineSectionName(tile))) {
                    addBackupSection(tile);
                    previousName = determineSectionName(tile);
                }
                appendBackupTileToSection(tile);
            }

            long sum = 0;
            for (Object[] item : data) {
                sum += (long) item[1];
            }
            totalFolderSize = sum;

            totalFolderSizeLabel.setText("Total size: " + totalFolderSize + " MiB");
            deactivateIndicator();
            setCenter(worldsBox);
        } else {
            setErrLabel(Settings.pathToMinecraftBackupFolder);
        }
    }
}