package com.frames.minecraft;

import com.jfoenix.controls.JFXTextField;
import com.utils.Settings;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MinecraftSettingsFrame extends BorderPane {

    private VBox pathsBox;
    private JFXTextField fieldSavesFolderPath;
    private JFXTextField fieldBackupFolderPath;
    private Label labelSavesFolderPath;
    private Label labelBackupFolder;

    public MinecraftSettingsFrame() {
        initComponents();
        initListeners();
        layoutComponents();
    }

    private void initComponents() {
        pathsBox = new VBox();
        fieldSavesFolderPath = new JFXTextField();
        fieldBackupFolderPath = new JFXTextField();
        labelSavesFolderPath = new Label("Saves Folder:");
        labelBackupFolder = new Label("Backup Folder:");

        setId("container");
        pathsBox.setId("container-tile");
        labelBackupFolder.setId("fieldLabel");
        labelSavesFolderPath.setId("fieldLabel");

        fieldSavesFolderPath.setText(Settings.pathToMinecraftSaveFolder);
        fieldBackupFolderPath.setText(Settings.pathToMinecraftBackupFolder);
    }

    private void initListeners() {
        fieldSavesFolderPath.setOnKeyTyped(e -> Settings.pathToMinecraftSaveFolder = fieldSavesFolderPath.getText());
        fieldBackupFolderPath.setOnKeyTyped(e -> Settings.pathToMinecraftBackupFolder = fieldBackupFolderPath.getText());
    }

    private void layoutComponents() {
        BorderPane savesFolderPanel = new BorderPane();
        savesFolderPanel.setTop(labelSavesFolderPath);
        savesFolderPanel.setCenter(fieldSavesFolderPath);
        BorderPane backupFolderPanel = new BorderPane();
        backupFolderPanel.setTop(labelBackupFolder);
        backupFolderPanel.setCenter(fieldBackupFolderPath);
        pathsBox.getChildren().addAll(savesFolderPanel, backupFolderPanel);
        setTop(pathsBox);
    }
}
