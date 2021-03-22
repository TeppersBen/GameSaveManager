package com.frames.core;

import com.frames.minecraft.MinecraftMainFrame;
import com.jfoenix.controls.JFXTabPane;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;

public class Frame extends JFXTabPane {

    private final double tabWidth = 90.0;

    public Frame() {
        setMinWidth(tabWidth);
        setMaxWidth(tabWidth);
        setTabMinHeight(tabWidth);
        setTabMaxHeight(tabWidth);
        setRotateGraphic(true);
        setSide(Side.LEFT);

        createCell(new MinecraftMainFrame(), "Minecraft", "/icons/gameSections/minecraft.jpg");
        createCell("Satisfactory");
        createCell("Sims 4");
        createCell("Valheim");
        createCell("Factorio");
        createCell("Terraria");
    }

    private void createCell(String title) {
        createCell(new Label("Coming Soon ..."), title, "/icons/gameSections/placeholder.png");
    }

    private void createCell(Node node, String title, String iconPath) {
        Tab tab = new Tab("", node);
        ImageView imageView = new ImageView(new Image(getClass().getResource(iconPath).toString()));
        double imageWidth = 40;
        imageView.setFitHeight(imageWidth);
        imageView.setFitWidth(imageWidth);

        Label label = new Label(title);
        label.setMaxWidth(tabWidth - 20);
        label.setPadding(new Insets(5,0,0,0));
        label.setStyle("-fx-text-fill: black; -fx-font-size: 8pt; -fx-font-weight: normal;");
        label.setTextAlignment(TextAlignment.CENTER);

        BorderPane tabPane = new BorderPane();
        tabPane.setRotate(90.0);
        tabPane.setMaxWidth(tabWidth);
        tabPane.setCenter(imageView);
        tabPane.setBottom(label);

        tab.setText("");
        tab.setGraphic(tabPane);

        getTabs().add(tab);
    }
}
