package com.frames.core;

import com.jfoenix.controls.JFXTabPane;
import javafx.scene.Node;
import javafx.scene.control.Tab;

public class TabFrame extends JFXTabPane {

    public TabFrame(String saveFolder, String gameName) {
        Tab savesTab = new Tab("Save Folder", new SaveFolderFrame(saveFolder, gameName));
        Tab backupTab = new Tab("Backup Folder", new BackupFolderFrame(saveFolder, gameName));
        Tab settingsTab = new Tab("Settings", new SettingsFrame(saveFolder, gameName));

        getTabs().addAll(savesTab, backupTab, settingsTab);
    }

    protected void addTab(String name, Node node) {
        getTabs().add(new Tab(name, node));
    }
}
