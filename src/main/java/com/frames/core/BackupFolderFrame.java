package com.frames.core;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public abstract class BackupFolderFrame extends SaveFolderFrame {

    protected void addBackupSection(GameSaveTile tile) {
        BorderPane backupSection = new BorderPane();
        Label sectionTitle = new Label(
                tile.getWorldName().substring(
                        0,
                        tile.getWorldName().split("\\(")
                                [tile.getWorldName().split("\\(").length-2].length()-3
                )
        );
        backupSection.setTop(sectionTitle);
        backupSection.setCenter(tile);

        worldsBox.getChildren().add(backupSection);
    }
}
