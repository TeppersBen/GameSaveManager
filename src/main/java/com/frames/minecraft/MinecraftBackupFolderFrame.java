package com.frames.minecraft;

import com.frames.core.BackupFolderFrame;
import com.frames.core.GameSaveTile;
import com.utils.Settings;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;

public class MinecraftBackupFolderFrame extends BackupFolderFrame {

    public MinecraftBackupFolderFrame() {

    }

    @Override
    public void refreshContent() {
        Object[][] data = savesManager.loadSavesContent(Settings.pathToMinecraftBackupFolder);
        worldsBox.getChildren().removeAll(worldTileList);
        worldTileList = new ArrayList<>();
        for (Object[] item : data) {
            MinecraftSavesManagerWorldTile tile = new MinecraftSavesManagerWorldTile(Settings.pathToMinecraftBackupFolder + item[0].toString(), item, this, true);
            worldTileList.add(tile);
            addBackupSection(tile);
        }

        long sum = 0;
        for (Object[] item : data) {
            sum += (long) item[1];
        }
        totalFolderSize = sum;

        totalFolderSizeLabel.setText("Total size: " + totalFolderSize + " MiB");
    }
}
