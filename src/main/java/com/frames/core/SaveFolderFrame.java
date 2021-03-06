package com.frames.core;

import com.jfoenix.controls.JFXButton;
import com.managers.SavesManager;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public abstract class SaveFolderFrame extends BorderPane {

    protected VBox worldsBox;
    protected ScrollPane scrollPane;
    protected List<GameSaveTile> worldTileList;
    protected SavesManager savesManager;
    protected JFXButton refreshContentButton;
    protected Label totalFolderSizeLabel;
    protected Label actionPerformedLabel;
    protected long totalFolderSize;
    protected IndicatorFrame indicatorFrame;
    protected BorderPane bottomPane;

    public SaveFolderFrame() {
        initComponents();
        initListeners();
        layoutComponents();

        refreshContent();
    }

    private void layoutComponents() {
        bottomPane = new BorderPane();
        bottomPane.setLeft(totalFolderSizeLabel);
        bottomPane.setCenter(refreshContentButton);
        bottomPane.setRight(actionPerformedLabel);

        scrollPane.setContent(worldsBox);

        setCenter(scrollPane);
        setBottom(bottomPane);
    }

    private void initListeners() {
        refreshContentButton.setOnAction(e->refreshContent());
        scrollPane.viewportBoundsProperty().addListener((observableValue, bounds, t1) -> {
            Node content = scrollPane.getContent();
            scrollPane.setFitToWidth(content.prefWidth(-1) < t1.getWidth());
            scrollPane.setFitToHeight(content.prefHeight(-1) < t1.getHeight());
        });
    }

    private void initComponents() {
        scrollPane = new ScrollPane();
        savesManager = new SavesManager();
        worldsBox = new VBox();
        worldsBox.setSpacing(5);
        totalFolderSizeLabel = new Label("Total size: " + totalFolderSize + " MiB");
        actionPerformedLabel = new Label();
        refreshContentButton = new JFXButton("Refresh");
        worldTileList = new ArrayList<>();
        indicatorFrame = new IndicatorFrame();

        setId("container");
        scrollPane.setId("container");
    }

    protected void activateIndicator() {
        //TODO :: System that activates the Inidicator - IndicatorFrame
    }

    protected void deactivateIndicator() {
        //TODO :: System that deactivates the Inidicator - IndicatorFrame
    }

    public void setActionPerformedText(String text) {
        actionPerformedLabel.setText(text);
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public VBox getWorldsBox() {
        return worldsBox;
    }

    public BorderPane getBottomPane() {
        return bottomPane;
    }

    public abstract void refreshContent();

    protected void setErrLabel(String filePath) {
        Label errLabel = new Label("Folder is empty!:\n"+filePath);
        errLabel.setTextAlignment(TextAlignment.CENTER);
        setCenter(errLabel);
    }

}
