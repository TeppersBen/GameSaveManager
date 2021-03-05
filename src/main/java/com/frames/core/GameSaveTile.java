package com.frames.core;

import com.jfoenix.controls.JFXButton;
import com.managers.SavesManager;
import com.utils.ActionPerformer;
import com.utils.Settings;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import javax.print.attribute.URISyntax;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GameSaveTile extends BorderPane {

    private final String sourcePath;
    private final Object[] data;
    private final boolean isBackup;

    private final SaveFolderFrame parentFrame;

    private String worldName;
    private String worldSize;

    private ImageView worldIconView;

    private JFXButton deleteBackupButton;
    private JFXButton replaceWorldWithLatestBackup;
    private JFXButton createBackupButton;

    HBox detailsTileButtonBox;

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
        deleteBackupButton = new JFXButton("Delete Backup");
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

    private void initListeners() {
        deleteBackupButton.setOnAction(e -> {
            parentFrame.setActionPerformedText(new SavesManager().purgeWorldFolder(Settings.pathToMinecraftBackupFolder, String.valueOf(data[0])));
            parentFrame.refreshContent();
        });

        replaceWorldWithLatestBackup.setOnAction(e -> {
            parentFrame.setActionPerformedText(new SavesManager().replaceWorldWithBackup(getWorldName()));
            parentFrame.refreshContent();
        });

        if (isBackup) {
            createBackupButton.setOnAction(e -> parentFrame.setActionPerformedText("Function coming soon ...")); //TODO :: Recover world function for the backups list.
        } else {
            createBackupButton.setOnAction(e -> parentFrame.setActionPerformedText(new SavesManager().createBackup(worldName)));
        }

    }

    private void layoutComponents() {
        BorderPane detailsTile = new BorderPane();
        BorderPane detailsTileInformation = new BorderPane();

        detailsTileInformation.setLeft(new Label(worldName));
        detailsTileInformation.setRight(new Label(worldSize + " MiB"));
        detailsTile.setPadding(new Insets(5));

        detailsTileButtonBox.getChildren().add(createBackupButton);
        if (isBackup) {
            detailsTileButtonBox.getChildren().add(deleteBackupButton);
        } else {
            detailsTileButtonBox.getChildren().add(replaceWorldWithLatestBackup);
        }

        detailsTile.setTop(detailsTileInformation);
        detailsTile.setBottom(detailsTileButtonBox);

        setLeft(worldIconView);
        setCenter(detailsTile);

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
