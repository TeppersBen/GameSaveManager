package com.frames.core;

import com.jfoenix.controls.JFXButton;
import com.managers.SavesManager;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public abstract class SaveFolderFrame extends BorderPane {

    protected VBox worldsBox;
    protected List<GameSaveTile> worldTileList;
    protected SavesManager savesManager;
    protected JFXButton refreshContentButton;
    protected Label totalFolderSizeLabel;
    protected Label actionPerformedLabel;
    protected long totalFolderSize;

    public SaveFolderFrame() {
        initComponents();
        initListeners();
        layoutComponents();

        refreshContent();
    }

    private void layoutComponents() {
        BorderPane bottomPane = new BorderPane();
        bottomPane.setLeft(totalFolderSizeLabel);
        bottomPane.setCenter(refreshContentButton);
        bottomPane.setRight(actionPerformedLabel);

        setCenter(worldsBox);
        setBottom(bottomPane);
    }

    private void initListeners() {
        refreshContentButton.setOnAction(e->refreshContent());
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

    public void setActionPerformedText(String text) {
        actionPerformedLabel.setText(text);
    }

    public abstract void refreshContent();

}
