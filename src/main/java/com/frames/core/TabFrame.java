package com.frames.core;

import com.jfoenix.controls.JFXTabPane;
import javafx.scene.Node;
import javafx.scene.control.Tab;

public class TabFrame extends JFXTabPane {

    public TabFrame(String saveFolder, String gameName) {
        this(saveFolder, gameName, "");
    }

    public TabFrame(String saveFolder, String gameName, String extension) {
        Tab savesTab = new Tab("Save Folder", new SaveFolderFrame(saveFolder, gameName, extension));
        Tab backupTab = new Tab("Backup Folder", new BackupFolderFrame(saveFolder, gameName));

        getTabs().addAll(savesTab, backupTab);
    }

    protected void addTab(String name, Node node) {
        getTabs().add(new Tab(name, node));
    }
}
