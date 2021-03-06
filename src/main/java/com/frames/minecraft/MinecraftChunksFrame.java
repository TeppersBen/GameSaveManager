package com.frames.minecraft;

import com.frames.core.SaveFolderFrame;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.managers.IOManager;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MinecraftChunksFrame extends BorderPane {

    private final VBox anvilListBox;
    private Map<String, String> anvilMap;
    private final VBox oldState;

    public MinecraftChunksFrame(SaveFolderFrame parentFrame, String path) {
        setId("container");

        oldState = parentFrame.getWorldsBox();

        anvilMap = new HashMap<>();

        anvilListBox = new VBox();
        anvilListBox.setSpacing(5);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setId("container");
        scrollPane.viewportBoundsProperty().addListener((observableValue, bounds, t1) -> {
            Node content = scrollPane.getContent();
            scrollPane.setFitToWidth(content.prefWidth(-1) < t1.getWidth());
            scrollPane.setFitToHeight(content.prefHeight(-1) < t1.getHeight());
        });
        scrollPane.setContent(anvilListBox);
        setCenter(scrollPane);

        JFXButton buttonAppendRow = new JFXButton("+");
        buttonAppendRow.setOnAction(e -> anvilListBox.getChildren().add(appendRow()));
        JFXButton buttonCloseWindow = new JFXButton("<-");
        buttonCloseWindow.setOnAction(e -> {
            parentFrame.getScrollPane().setContent(oldState);
            parentFrame.getBottomPane().setVisible(true);
        });
        Label labelWorldName = new Label(new File(String.valueOf(Paths.get(path))).getName() + " Priority Chunk List");
        labelWorldName.setId("container-title");

        BorderPane topPane = new BorderPane();
        topPane.setLeft(buttonCloseWindow);
        topPane.setCenter(labelWorldName);
        topPane.setRight(buttonAppendRow);
        setTop(topPane);
    }

    private BorderPane appendRow() {
        BorderPane borderPane = new BorderPane();
        borderPane.setId("container-tile");

        JFXTextField fieldMCAFile = new JFXTextField();
        fieldMCAFile.setPromptText("r.0.0.mca");

        JFXTextField fieldDescription = new JFXTextField();
        fieldDescription.setPromptText("[OPTIONAL]: Describe what this anvil file represents");

        fieldMCAFile.focusedProperty().addListener(e -> notifyMapping(fieldMCAFile, fieldDescription));
        fieldDescription.focusedProperty().addListener(e -> notifyMapping(fieldMCAFile, fieldDescription));

        JFXButton buttonRemove = new JFXButton("X");
        buttonRemove.setOnAction(e -> anvilListBox.getChildren().remove(borderPane));

        borderPane.setLeft(fieldMCAFile);
        borderPane.setCenter(fieldDescription);
        borderPane.setRight(buttonRemove);

        return borderPane;
    }

    private void notifyMapping(JFXTextField fieldMCAFile, JFXTextField fieldDescription) {
        if (isValidMCAInput(fieldMCAFile.getText())) {
            anvilMap.put(fieldMCAFile.getText(), fieldDescription.getText());
        }
    }

    private boolean isValidMCAInput(String userInput) {
        String[] segments = userInput.split("\\.");
        if (segments.length == 4) {
            return segments[0].equalsIgnoreCase("r")
                    && IOManager.isNumeric(segments[1])
                    && IOManager.isNumeric(segments[2])
                    && segments[3].equalsIgnoreCase("mca");
        }
        return false;
    }
}