package com.frames;

import com.jfoenix.controls.JFXTabPane;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

public class MainFrame extends JFXTabPane {

    public MainFrame() {
        Tab savesTab = new Tab("Saves Folder Manager", new SavesManagerFrame());
        Tab settingsTab = new Tab("Settings", new Label("Not Implemented Yet!"));
        Tab extraTab = new Tab("Extra", new ExtrasFrame());

        getTabs().addAll(savesTab, settingsTab, extraTab);
    }

}