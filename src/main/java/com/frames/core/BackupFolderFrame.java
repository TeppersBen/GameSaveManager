package com.frames.core;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public abstract class BackupFolderFrame extends SaveFolderFrame {

    protected Map<String, Section> sections;

    protected void addBackupSection(GameSaveTile tile) {
        Section section = new Section(tile);
        if (sections == null) {
            sections = new HashMap<>();
        }
        sections.put(section.getSectionName(), section);
        worldsBox.getChildren().add(section);
    }

    protected void appendBackupTileToSection(GameSaveTile tile) {
        Section section = sections.get(determineSectionName(tile));
        section.getBackupsBox().getChildren().add(tile);
    }

    protected String determineSectionName(GameSaveTile tile) {
        return tile.getWorldName().substring(
                0,
                tile.getWorldName().split("\\(")
                        [tile.getWorldName().split("\\(").length-2].length()-3
        );
    }

    private static class Section extends BorderPane {
        private final VBox backups;
        private final String sectionName;
        public Section(GameSaveTile tile) {
            backups = new VBox();
            sectionName = tile.getWorldName().substring(
                    0,
                    tile.getWorldName().split("\\(")
                            [tile.getWorldName().split("\\(").length-2].length()-3
            );
            Label sectionTitle = new Label(sectionName);
            setTop(sectionTitle);
            setCenter(backups);
        }
        public VBox getBackupsBox() { return backups; }
        public String getSectionName() { return sectionName; }
    }
}
