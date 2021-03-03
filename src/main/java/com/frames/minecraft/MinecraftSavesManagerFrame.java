package com.frames.minecraft;

import com.jfoenix.controls.JFXButton;
import com.managers.SavesManager;
import com.utils.Settings;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class MinecraftSavesManagerFrame extends BorderPane {

    private SavesManager savesManager;
    private VBox worldsBox;
    private long totalFolderSize;

    private List<MinecraftSavesManagerWorldTile> worldTileList;

    private JFXButton refreshContentButton;

    private Label totalFolderSizeLabel;
    private Label actionPerformedLabel;

    public MinecraftSavesManagerFrame() {
        initComponents();
        initListeners();
        layoutComponents();
        refreshContent();
    }

    public void refreshContent() {
        Object[][] data = savesManager.loadSavesContent(Settings.pathToMinecraftSaveFolder);
        worldsBox.getChildren().removeAll(worldTileList);
        worldTileList = new ArrayList<>();
        for (Object[] item : data) {
            MinecraftSavesManagerWorldTile tile = new MinecraftSavesManagerWorldTile(Settings.pathToMinecraftSaveFolder + item[0].toString(), item, this, false);
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

    private void initComponents() {
        savesManager = new SavesManager();
        worldsBox = new VBox();
        totalFolderSizeLabel = new Label("Total size: " + totalFolderSize + " MiB");
        actionPerformedLabel = new Label();
        refreshContentButton = new JFXButton("Refresh");
        worldTileList = new ArrayList<>();

        setId("container");
    }

    private void initListeners() {
        refreshContentButton.setOnAction(e->refreshContent());
    }

    private void layoutComponents() {
        setCenter(worldsBox);

        BorderPane bottomPane = new BorderPane();
        bottomPane.setLeft(totalFolderSizeLabel);
        bottomPane.setCenter(refreshContentButton);
        bottomPane.setRight(actionPerformedLabel);
        setBottom(bottomPane);
    }

    public void setActionPerformedText(String text) {
        actionPerformedLabel.setText(text);
    }
}