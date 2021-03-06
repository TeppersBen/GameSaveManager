package com.frames.core;

import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        section.getTiles().add(tile);
    }

    protected String determineSectionName(GameSaveTile tile) {
        return tile.getWorldName().substring(
                0,
                tile.getWorldName().split("\\(")
                        [tile.getWorldName().split("\\(").length-2].length()-3
        );
    }

    protected void wipeAllSections() {
        if (sections != null && sections.size() > 0) {
            sections.forEach((k,v) -> {
                v.wipeBackupsGUI();
                worldsBox.getChildren().remove(v);
            });
        }
    }

    private static class Section extends TitledPane {
        private final VBox backups;
        private List<GameSaveTile> tiles;
        private final String sectionName;
        public Section(GameSaveTile tile) {
            backups = new VBox();
            backups.setSpacing(5);
            tiles = new ArrayList<>();
            sectionName = tile.getWorldName().substring(
                    0,
                    tile.getWorldName().split("\\(")
                            [tile.getWorldName().split("\\(").length-2].length()-3
            );
            Label sectionTitle = new Label(sectionName);
            setContent(backups);
            setText(sectionTitle.getText());
        }
        public void wipeBackupsGUI() {
            backups.getChildren().removeAll(tiles);
            tiles = new ArrayList<>();
        }
        public List<GameSaveTile> getTiles() { return tiles; }
        public VBox getBackupsBox() { return backups; }
        public String getSectionName() { return sectionName; }
    }
}
