package com.frames.minecraft;

import com.frames.core.SaveFolderFrame;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.managers.IOManager;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

public class MinecraftChunksFrame extends BorderPane {

    private final VBox anvilListBox;
    private final Map<String, Map<String, String>> anvilMap;
    private final VBox oldState;
    private final String path;

    public MinecraftChunksFrame(SaveFolderFrame parentFrame, String path) {
        setId("container");

        this.path = path;
        oldState = parentFrame.getWorldsBox();

        anvilListBox = new VBox();
        anvilListBox.setSpacing(5);

        anvilMap = IOManager.deSerializeHashMap(path);

        assert anvilMap != null;
        anvilMap.forEach((k, v) ->
                v.forEach((kk, vv) ->
                        anvilListBox.getChildren().add(
                                new ChunkTile().build(k, kk, vv)
                        )
                )
        );

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
        buttonAppendRow.setOnAction(e -> anvilListBox.getChildren().add(new ChunkTile()));
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

    private class ChunkTile extends BorderPane {

        private final JFXTextField fieldDIM;
        private final JFXTextField fieldMCAFile;
        private final JFXTextField fieldDescription;

        public ChunkTile() {
            BorderPane innerPane = new BorderPane();

            fieldDIM = new JFXTextField();
            fieldDIM.setPromptText("DIM 0");
            fieldDIM.setTooltip(new Tooltip("DIM -1 = Nether Dimension | DIM 0 = Overworld Dimension | DIM 1 = End Dimension"));

            fieldMCAFile = new JFXTextField();
            fieldMCAFile.setPromptText("r.0.0.mca");

            fieldDescription = new JFXTextField();
            fieldDescription.setPromptText("[OPTIONAL]: Describe what this anvil file represents");

            fieldMCAFile.focusedProperty().addListener(e -> notifyMapping(fieldDIM, fieldMCAFile, fieldDescription));
            fieldDescription.focusedProperty().addListener(e -> notifyMapping(fieldDIM, fieldMCAFile, fieldDescription));

            innerPane.setLeft(fieldMCAFile);
            innerPane.setCenter(fieldDescription);

            setLeft(fieldDIM);
            setCenter(innerPane);
            setId("container-tile");

            JFXButton buttonRemove = new JFXButton("X");
            buttonRemove.setOnAction(e -> {
                anvilListBox.getChildren().remove(this);
                if (isValidMCAInput(fieldMCAFile.getText()) && isValidDimension(fieldDIM.getText())) {
                    anvilMap.get(fieldDIM.getText()).remove(fieldMCAFile.getText());
                    IOManager.serializeHashMap(path, anvilMap);
                }
            });
            innerPane.setRight(buttonRemove);
        }

        public ChunkTile build(String dim, String mca, String desc) {
            fieldDIM.setText(dim);
            fieldMCAFile.setText(mca);
            fieldDescription.setText(desc);
            return this;
        }
    }

    private void notifyMapping(JFXTextField fieldDimension, JFXTextField fieldMCAFile, JFXTextField fieldDescription) {
        if (isValidMCAInput(fieldMCAFile.getText()) && isValidDimension(fieldDimension.getText())) {
            anvilMap.get(fieldDimension.getText()).put(fieldMCAFile.getText(), fieldDescription.getText());
            IOManager.serializeHashMap(path, anvilMap);
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

    private boolean isValidDimension(String userInput) {
        String[] segments = userInput.split(" ");
        if (segments.length == 2) {
            return segments[0].equalsIgnoreCase("DIM")
                    && IOManager.isNumeric(segments[1])
                    && Integer.parseInt(segments[1]) >= -1
                    && Integer.parseInt(segments[1]) <= 1;
        }
        return false;
    }
}