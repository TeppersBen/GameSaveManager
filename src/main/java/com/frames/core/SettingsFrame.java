package com.frames.core;

import com.jfoenix.controls.JFXTextField;
import com.managers.PropertiesManager;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class SettingsFrame extends BorderPane {

    private VBox pathsBox;
    private JFXTextField fieldSavesFolderPath;
    private JFXTextField fieldBackupFolderPath;
    private Label labelSavesFolderPath;
    private Label labelBackupFolder;

    private final String saveFolderProperty;
    private final String backupFolderProperty;

    public SettingsFrame(String saveFolderProperty, String backupFolderProperty) {
        this.saveFolderProperty = saveFolderProperty;
        this.backupFolderProperty = backupFolderProperty;
        initComponents();
        layoutComponents();
        initListeners();
    }

    private void initListeners() {
        fieldBackupFolderPath.focusedProperty().addListener(e -> PropertiesManager.saveProperty(backupFolderProperty, fieldBackupFolderPath.getText()));
        fieldSavesFolderPath.focusedProperty().addListener(e -> PropertiesManager.saveProperty(saveFolderProperty, fieldSavesFolderPath.getText()));
    }

    private void initComponents() {
        pathsBox = new VBox();
        pathsBox.setSpacing(10);
        fieldSavesFolderPath = new JFXTextField();
        fieldSavesFolderPath.setText(PropertiesManager.getProperty(saveFolderProperty));
        fieldBackupFolderPath = new JFXTextField();
        fieldBackupFolderPath.setText(PropertiesManager.getProperty(backupFolderProperty));
        labelSavesFolderPath = new Label("Saves Folder:");
        labelBackupFolder = new Label("Backup Folder:");

        setId("container");
        pathsBox.setId("container-tile");
        labelBackupFolder.setId("container-title");
        labelSavesFolderPath.setId("container-title");
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
