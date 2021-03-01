package com.frames.minecraft;

import com.jfoenix.controls.JFXTabPane;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

public class MinecraftMainFrame extends JFXTabPane {

    public MinecraftMainFrame() {
        Tab savesTab = new Tab("Saves Folder Manager", new SavesManagerFrame());
        Tab settingsTab = new Tab("Settings", new Label("Not Implemented Yet!"));
        Tab extraTab = new Tab("Extra", new MinecraftExtrasFrame());

        getTabs().addAll(savesTab, settingsTab, extraTab);
    }

}