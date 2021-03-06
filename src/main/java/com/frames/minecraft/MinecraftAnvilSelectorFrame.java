package com.frames.minecraft;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.managers.IOManager;
import com.managers.PropertiesManager;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MinecraftAnvilSelectorFrame extends VBox {
    public MinecraftAnvilSelectorFrame() {
        setId("container");
        setSpacing(5);

        VBox pathsBox = new VBox();
        pathsBox.setId("container-tile");
        BorderPane pathPane = new BorderPane();
        Label lblTitle = new Label("JavaFX SDK Library Folder");
        lblTitle.setId("container-title");
        pathPane.setTop(lblTitle);
        JFXTextField javaFXLibraryPath = new JFXTextField();
        javaFXLibraryPath.setPromptText("%programfiles%\\Java\\javafx-sdk-15.0.1\\lib");
        javaFXLibraryPath.setText(PropertiesManager.getProperty("javaFXSDKLibraryFolder"));
        javaFXLibraryPath.focusedProperty().addListener(e -> PropertiesManager.saveProperty("javaFXSDKLibraryFolder", javaFXLibraryPath.getText()));
        pathPane.setCenter(javaFXLibraryPath);
        pathsBox.getChildren().add(pathPane);

        BorderPane buttonPane = new BorderPane();
        buttonPane.setId("container-tile");
        JFXButton button = new JFXButton("Open MCA-Selector");
        button.setOnAction(e -> {
            try {
                String executionString = "java --module-path \"" + IOManager.convertSystemEnvironmentPathToAbsolutePath(javaFXLibraryPath.getText()) + "\" --add-modules ALL-MODULE-PATH -jar " + getClass().getResource("/mcaselector-1.14.2.jar").getPath().substring(1);
                Runtime.getRuntime().exec(executionString);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        buttonPane.setCenter(button);

        getChildren().addAll(
                generateInformationTile("Description", "An external tool to export or delete selected chunks and regions from a world save of Minecraft Java Edition"),
                generateInformationTile("Requirements",
                        "\t- 64bit JRE 8+, you can get it from https://www.java.com/en/download/\n" +
                        "\t- JavaFX SDK, which can be downloaded from https://gluonhq.com/products/javafx/\n" +
                        "\t- A Minecraft Java Edition installation\n" +
                        "\t- At least 6 GB of RAM. If lower, more RAM has to manually be assigned to the JVM using the -Xmx argument. Assigning 4 GB is recommended.\n"
                ),
                pathsBox,
                buttonPane
        );
    }

    private BorderPane generateInformationTile(String title, String message) {
        BorderPane informationPane = new BorderPane();
        informationPane.setId("container-tile");
        Label lblTitle = new Label(title);
        lblTitle.setId("container-title");
        informationPane.setTop(lblTitle);
        informationPane.setLeft(new Label(message));
        return informationPane;
    }
}