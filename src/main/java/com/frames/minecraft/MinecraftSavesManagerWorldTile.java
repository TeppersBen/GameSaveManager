package com.frames.minecraft;

import com.jfoenix.controls.JFXButton;
import com.managers.SavesManager;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.FileInputStream;
import java.io.IOException;

public class MinecraftSavesManagerWorldTile extends BorderPane {

    private final String sourcePath;
    private final Object[] data;
    private final boolean isPrimaryWorld;

    private final MinecraftSavesManagerFrame parentFrame;

    private String worldName;
    private String worldSize;

    private ImageView worldIconView;

    private JFXButton wipeUnnecessaryChunksButton;
    private JFXButton deleteBackupButton;
    private JFXButton replaceWorldWithLatestBackup;

    public MinecraftSavesManagerWorldTile(String path, Object[] data, MinecraftSavesManagerFrame parentFrame) {
        sourcePath = path;
        this.data = data;
        isPrimaryWorld = String.valueOf(data[0]).equalsIgnoreCase("MinecraftJavaSurvival");
        this.parentFrame = parentFrame;

        initComponents();
        initListeners();
        layoutComponents();
    }

    private void initComponents() {
        wipeUnnecessaryChunksButton = new JFXButton("Wipe Unnecessary Chunks");
        deleteBackupButton = new JFXButton("Delete Backup");
        replaceWorldWithLatestBackup = new JFXButton("Restore World");

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
            parentFrame.setActionPerformedText(new SavesManager().replaceWorldWithBackup());
            parentFrame.refreshContent();
        });
    }

    private void layoutComponents() {
        BorderPane detailsTile = new BorderPane();
        BorderPane detailsTileInformation = new BorderPane();

        detailsTileInformation.setLeft(new Label(worldName));
        detailsTileInformation.setRight(new Label(worldSize + " MiB"));
        detailsTile.setPadding(new Insets(5));

        HBox detailsTileButtonBox = new HBox();
        detailsTileButtonBox.getChildren().add(wipeUnnecessaryChunksButton);
        if (!isPrimaryWorld) {
            detailsTileButtonBox.getChildren().add(deleteBackupButton);
        } else {
            detailsTileButtonBox.getChildren().add(replaceWorldWithLatestBackup);
        }

        detailsTile.setTop(detailsTileInformation);
        detailsTile.setBottom(detailsTileButtonBox);

        setLeft(worldIconView);
        setCenter(detailsTile);
    }
}
