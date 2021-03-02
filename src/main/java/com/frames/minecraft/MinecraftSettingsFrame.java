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

        fieldSavesFolderPath.setText(Settings.DEFAULT_MINECRAFT_SAVE_FOLDER);
        fieldBackupFolderPath.setText(Settings.DEFAULT_MINECRAFT_BACKUP_FOLDER);
    }

    private void initListeners() {
        //TODO :: Create a system where as the user changes the location of saves or backups, the manager knows about it.
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
