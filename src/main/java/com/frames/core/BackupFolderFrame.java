package com.frames.core;

import com.frames.minecraft.MinecraftGameSaveTile;
import com.managers.IOManager;
import com.managers.SavesManager;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.util.*;

public class BackupFolderFrame extends SaveFolderFrame {

    protected Map<String, Section> sections;

    public BackupFolderFrame(String saveFolderPropertyName, String gameName) {
        super(saveFolderPropertyName, gameName);
    }

    @Override
    public void refreshContent() {
        activateIndicator();
        String backupFolderLocation = SavesManager.backupCoreFolder + gameName;
        if (backupFolderLocation.isEmpty() || backupFolderLocation.isBlank()) {
            deactivateIndicator();
            return;
        }
        IOManager.createFolderIfNotExists(backupFolderLocation);
        Object[][] data = savesManager.loadSavesContent(backupFolderLocation);
        if (Objects.requireNonNull(data).length != 0) {
            wipeAllSections();
            String previousName = "/";
            for (Object[] item : data) {
                GameSaveTile tile;
                if (backupFolderLocation.contains("minecraft")) {
                    tile = new MinecraftGameSaveTile(backupFolderLocation, item, this, true);
                } else {
                    tile = new GameSaveTile(backupFolderLocation, item, this, true);
                }
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
        } else {
            setErrLabel(gameName);
        }
    }

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
        section.updateSectionName();
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
        public void updateSectionName() {
            setText(sectionName + " (" + getTiles().size() + ")");
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
