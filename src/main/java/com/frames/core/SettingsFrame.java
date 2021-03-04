package com.frames.core;

import com.jfoenix.controls.JFXTextField;
import com.utils.Settings;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javax.swing.*;

public class SettingsFrame extends BorderPane {

    private VBox pathsBox;
    private JFXTextField fieldSavesFolderPath;
    private JFXTextField fieldBackupFolderPath;
    private Label labelSavesFolderPath;
    private Label labelBackupFolder;

    public SettingsFrame() {
        initComponents();
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
    }

    protected void appendBackupFolderFieldAction(ActionPerformer action) {
        fieldBackupFolderPath.setOnKeyTyped(e -> action.execute());
    }

    protected void appendSaveFolderFieldAction(ActionPerformer action) {
        fieldSavesFolderPath.setOnKeyTyped(e -> action.execute());
    }

    protected interface ActionPerformer {
        void execute();
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

    protected JFXTextField getFieldSavesFolderPath() {
        return fieldSavesFolderPath;
    }

    protected JFXTextField getFieldBackupFolderPath() {
        return fieldBackupFolderPath;
    }

}
