package com.frames.core;

import com.jfoenix.controls.JFXButton;
import com.managers.SavesManager;
import com.utils.Settings;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.FileInputStream;
import java.io.IOException;

public class GameSaveTile extends BorderPane {

    private final String sourcePath;
    private final Object[] data;
    private final boolean isBackup;

    private final SaveFolderFrame parentFrame;

    private String worldName;
    private String worldSize;

    private ImageView worldIconView;

    private JFXButton wipeUnnecessaryChunksButton;
    private JFXButton deleteBackupButton;
    private JFXButton replaceWorldWithLatestBackup;
    private JFXButton checkBackupListButton;
    private JFXButton createBackupButton;

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
        wipeUnnecessaryChunksButton = new JFXButton("Wipe Unnecessary Chunks");
        deleteBackupButton = new JFXButton("Delete Backup");
        replaceWorldWithLatestBackup = new JFXButton("Restore World");
        checkBackupListButton = new JFXButton("Check Backup List");
        createBackupButton = new JFXButton("Create Backup");

        worldName = String.valueOf(data[0]);
        worldSize = String.valueOf(data[1]);

        try {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(sourcePath + "/icon.png");
                Image worldIcon = new Image(inputStream);
                worldIconView = new ImageView(worldIcon);
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                assert inputStream != null;
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        setId("container-tile");
    }

    private void initListeners() {
        wipeUnnecessaryChunksButton.setOnAction(e -> {
            parentFrame.setActionPerformedText(new SavesManager().purgeUnnecessaryChunks(String.valueOf(data[0])));
            parentFrame.refreshContent();
        });

        deleteBackupButton.setOnAction(e -> {
            parentFrame.setActionPerformedText(new SavesManager().purgeWorldFolder(String.valueOf(data[0])));
            parentFrame.refreshContent();
        });

        replaceWorldWithLatestBackup.setOnAction(e -> {
            parentFrame.setActionPerformedText(new SavesManager().replaceWorldWithBackup(getWorldName()));
            parentFrame.refreshContent();
        });

        checkBackupListButton.setOnAction(e -> {
            Object[][] data = new SavesManager().loadSavesContent(Settings.pathToMinecraftBackupFolder);
            for (Object[] item : data) {
                System.out.println(item[0] + " - " + item[1] + "MiB");
            }
        });

        createBackupButton.setOnAction(e -> parentFrame.setActionPerformedText(new SavesManager().createBackup(worldName)));
    }

    private void layoutComponents() {
        BorderPane detailsTile = new BorderPane();
        BorderPane detailsTileInformation = new BorderPane();

        detailsTileInformation.setLeft(new Label(worldName));
        detailsTileInformation.setRight(new Label(worldSize + " MiB"));
        detailsTile.setPadding(new Insets(5));

        HBox detailsTileButtonBox = new HBox();
        detailsTileButtonBox.getChildren().addAll(
                wipeUnnecessaryChunksButton,
                checkBackupListButton,
                createBackupButton
        );
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

}
