package com.frames;

import com.jfoenix.controls.JFXButton;
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

    private String worldName;
    private String worldSize;

    private Image worldIcon;
    private ImageView worldIconView;

    private JFXButton wipeUnnecessaryChunksButton;
    private JFXButton deleteBackupButton;
    private JFXButton replaceWorldWithLatestBackup;

    public SavesManagerWorldTile(String path, Object[] data) {
        sourcePath = path;
        this.data = data;
        isPrimaryWorld = String.valueOf(data[0]).equalsIgnoreCase("MinecraftJavaSurvival");

        try {
            FileInputStream inputStream = new FileInputStream(path + "/icon.png");
            worldIcon = new Image(inputStream);
            worldIconView = new ImageView(worldIcon);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        wipeUnnecessaryChunksButton = new JFXButton("Wipe Unnecessary Chunks");
        if (!isPrimaryWorld) {
            deleteBackupButton = new JFXButton("Delete Backup");
        } else {
            replaceWorldWithLatestBackup = new JFXButton("Restore World");
        }

        worldName = String.valueOf(data[0]);
        worldSize = String.valueOf(data[1]);

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
