package com.frames.core;

import com.jfoenix.controls.JFXTabPane;
import javafx.scene.Node;
import javafx.scene.control.Tab;

public class TabFrame extends JFXTabPane {

    public TabFrame(SaveFolderFrame saveFolderFrame, BackupFolderFrame backupFolderFrame, SettingsFrame settingsFrame) {
        Tab savesTab = new Tab("Save Folder", saveFolderFrame);
        Tab backupTab = new Tab("Backup Folder", backupFolderFrame);
        Tab settingsTab = new Tab("Settings", settingsFrame);

        getTabs().addAll(savesTab, backupTab, settingsTab);
    }

    protected void addTab(String name, Node node) {
        getTabs().add(new Tab(name, node));
    }
}
