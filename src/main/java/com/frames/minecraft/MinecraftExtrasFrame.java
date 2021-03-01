package com.frames.minecraft;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MinecraftExtrasFrame extends BorderPane {
    public MinecraftExtrasFrame() {
        JFXButton button = new JFXButton("Open MCA-Selector");
        button.setTooltip(new Tooltip("Might not work, Requires JavaFX 15 located within the Program Files folder under Java."));
        button.setOnAction(e -> {
            try {
                String executionString = "java --module-path \"C:\\Program Files\\Java\\javafx-sdk-15.0.1\\lib\" --add-modules ALL-MODULE-PATH -jar " + System.getenv("APPDATA") + "\\.minecraft\\saves\\MinecraftJavaSurvival\\" + "mcaselector-1.13.2.jar";
                Runtime.getRuntime().exec(executionString);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        setCenter(button);
    }
}
