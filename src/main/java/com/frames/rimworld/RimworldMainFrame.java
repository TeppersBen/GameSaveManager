package com.frames.rimworld;

import com.frames.core.TabFrame;
import com.managers.PropertiesManager;

public class RimworldMainFrame extends TabFrame {

    public RimworldMainFrame() {
        super("pathToRimworldSaveFolder", "Rimworld");
        addTab("Save File Manipulator", new RimworldSaveManipulatorFrame(PropertiesManager.getProperty("pathToRimworldSaveFolder")));
    }
}