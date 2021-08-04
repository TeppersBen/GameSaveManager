package com.frames.core;

import com.jfoenix.controls.JFXTabPane;
import javafx.scene.Node;
import javafx.scene.control.Tab;

public class TabFrame extends JFXTabPane {

    public TabFrame(String saveFolder, String backupFolder) {
        Tab savesTab = new Tab("Save Folder", new SaveFolderFrame(saveFolder, backupFolder));
        Tab backupTab = new Tab("Backup Folder", new BackupFolderFrame(saveFolder, backupFolder));
        Tab settingsTab = new Tab("Settings", new SettingsFrame(saveFolder, backupFolder));

        getTabs().addAll(savesTab, backupTab, settingsTab);
    }

    protected void addTab(String name, Node node) {
        getTabs().add(new Tab(name, node));
    }
}
