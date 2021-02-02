package com.frames;

import com.jfoenix.controls.JFXButton;
import com.managers.SavesManager;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.IOException;

public class SavesManagerWorldTile extends BorderPane {

    private final String sourcePath;
    private final Object[] data;
    private final boolean isPrimaryWorld;

    private SavesManagerFrame parentFrame;

    private String worldName;
    private String worldSize;

    private Image worldIcon;
    private ImageView worldIconView;

    private JFXButton wipeUnnecessaryChunksButton;
    private JFXButton deleteBackupButton;
    private JFXButton replaceWorldWithLatestBackup;

    public SavesManagerWorldTile(String path, Object[] data, SavesManagerFrame parentFrame) {
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
                worldIcon = new Image(inputStream);
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
            new SavesManager().purgeUnnecessaryChunks(String.valueOf(data[0]));
            parentFrame.refreshContent();
        });

        deleteBackupButton.setOnAction(e -> {
            new SavesManager().purgeWorldFolder(String.valueOf(data[0]));
            parentFrame.refreshContent();
        });

        replaceWorldWithLatestBackup.setOnAction(e -> {
            new SavesManager().replaceWorldWithBackup();
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
