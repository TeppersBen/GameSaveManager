package com.frames.minecraft;

import com.frames.core.SaveFolderFrame;
import com.utils.Settings;

import java.util.ArrayList;

public class MinecraftSaveFolderFrame extends SaveFolderFrame {

    public MinecraftSaveFolderFrame() {
        super();
        refreshContent();
    }

    public void refreshContent() {
        Object[][] data = savesManager.loadSavesContent(Settings.pathToMinecraftSaveFolder);
        worldsBox.getChildren().removeAll(worldTileList);
        worldTileList = new ArrayList<>();
        for (Object[] item : data) {
            MinecraftGameSaveTile tile = new MinecraftGameSaveTile(Settings.pathToMinecraftSaveFolder + item[0].toString(), item, this, false);
            worldTileList.add(tile);
            worldsBox.getChildren().add(tile);
        }

        long sum = 0;
        for (Object[] item : data) {
            sum += (long) item[1];
        }
        totalFolderSize = sum;

        totalFolderSizeLabel.setText("Total size: " + totalFolderSize + " MiB");
    }

}