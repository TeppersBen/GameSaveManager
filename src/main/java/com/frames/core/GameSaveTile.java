package com.frames.core;

import com.jfoenix.controls.JFXButton;
import com.managers.SavesManager;
import com.utils.ActionPerformer;
import com.utils.Settings;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.File;

public class GameSaveTile extends BorderPane {

    private String sourcePath;
    private final Object[] data;
    private final boolean isBackup;

    private final SaveFolderFrame parentFrame;

    private String worldName;
    private String worldSize;

    private ImageView worldIconView;

    private JFXButton deleteWorldFile;
    private JFXButton replaceWorldWithLatestBackup;
    private JFXButton createBackupButton;

    private HBox detailsTileButtonBox;

    public GameSaveTile(String path, Object[] data, SaveFolderFrame parentFrame, boolean isBackup) {
        sourcePath = path;
        this.data = data;
        this.isBackup = isBackup;
        this.parentFrame = parentFrame;

        initComponents();
        initListeners();
        layoutComponents();
    }

    private void initComponents() {
        deleteWorldFile = new JFXButton("X");
        deleteWorldFile.setId("remove-icon");
        replaceWorldWithLatestBackup = new JFXButton("Restore World");

        if (isBackup) {
            createBackupButton = new JFXButton("Recover World");
        } else {
            createBackupButton = new JFXButton("Create Backup");
        }

        detailsTileButtonBox = new HBox();

        worldName = String.valueOf(data[0]);
        worldSize = String.valueOf(data[1]);

        if (new File(sourcePath + "\\icon.png").exists()) {
            worldIconView = new ImageView(new Image(new File("file:" + sourcePath + "\\icon.png").getPath()));
        } else {
            worldIconView = new ImageView(new Image(getClass().getResource("/icons/minecraft/icon.png").toString()));
        }

        setId("container-tile");
    }

    @SuppressWarnings("All")
    private void initListeners() {
        deleteWorldFile.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.NO, ButtonType.YES);
            alert.setTitle(worldName + " removal");
            alert.setContentText("This will clean up " + worldSize + " MiB from your harddrive.");
            alert.setHeaderText("You are about to remove " + worldName + ", are you sure?");
            if (alert.showAndWait().get() == ButtonType.YES) {
                sourcePath = sourcePath.substring(0, sourcePath.length() - String.valueOf(data[0]).length());
                parentFrame.setActionPerformedText(new SavesManager().purgeWorldFolder(sourcePath, String.valueOf(data[0])));
                parentFrame.refreshContent();
            }
        });

        replaceWorldWithLatestBackup.setOnAction(e -> {
            parentFrame.setActionPerformedText(new SavesManager().replaceWorldWithBackup(getWorldName()));
            parentFrame.refreshContent();
        });

        if (isBackup) {
            createBackupButton.setOnAction(e -> parentFrame.setActionPerformedText(new SavesManager().recoverBackup(worldName)));
        } else {
            createBackupButton.setOnAction(e -> parentFrame.setActionPerformedText(new SavesManager().createBackup(worldName)));
        }

    }

    private void layoutComponents() {
        BorderPane detailsTile = new BorderPane();
        BorderPane detailsTileInformation = new BorderPane();
        BorderPane detailsTileRemoval = new BorderPane();

        detailsTileInformation.setLeft(new Label(worldName));
        detailsTileInformation.setRight(new Label(worldSize + " MiB"));
        detailsTile.setPadding(new Insets(5));

        detailsTileButtonBox.getChildren().add(createBackupButton);
        if (!isBackup) {
            detailsTileButtonBox.getChildren().add(replaceWorldWithLatestBackup);
        }

        detailsTile.setTop(detailsTileInformation);
        detailsTile.setBottom(detailsTileButtonBox);

        detailsTileRemoval.setTop(deleteWorldFile);

        setLeft(worldIconView);
        setCenter(detailsTile);
        setRight(detailsTileRemoval);

        setMaxHeight(50);
    }

    public String getWorldName() {
        return worldName;
    }

    protected void addButton(JFXButton button, ActionPerformer action) {
        button.setOnAction(e -> action.execute());
        detailsTileButtonBox.getChildren().add(button);
    }

}
