package com.frames.core;

import com.frames.factorio.FactorioMainFrame;
import com.frames.minecraft.MinecraftMainFrame;
import com.frames.rimworld.RimworldMainFrame;
import com.frames.satisfactory.SatisfactoryMainFrame;
import com.jfoenix.controls.JFXTabPane;
import com.managers.PropertiesManager;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;

import java.util.Objects;

public class Frame extends JFXTabPane {

    private final double tabWidth = 90.0;

    public Frame() {
        setMinWidth(tabWidth);
        setMaxWidth(tabWidth);
        setTabMinHeight(tabWidth);
        setTabMaxHeight(tabWidth);
        setRotateGraphic(true);
        setSide(Side.LEFT);

        String gameTabs = PropertiesManager.getProperty("gameTabs");
        for (String tab : gameTabs.split(",")) {
            switch (tab) {
                case "MinecraftMainFrame":
                    addGame(new MinecraftMainFrame(), "Minecraft", "/icons/gameSections/minecraft.jpg");
                    break;
                case "FactorioMainFrame":
                    addGame(new FactorioMainFrame(), "Factorio", "/icons/gameSections/factorio.png");
                    break;
                case "SatisfactoryMainFrame":
                    addGame(new SatisfactoryMainFrame(), "Satisfactory", "/icons/gameSections/satisfactory.png");
                    break;
                case "RimworldMainFrame":
                    addGame(new RimworldMainFrame(), "Rimworld", "/icons/gameSections/Rimworld.png");
                    break;
                case "SettingsFrame":
                    addGame(new SettingsFrame(this), "Settings", "/icons/gears.png");
                    break;
            }
        }
    }

    public void addGame(Node node, String title, String iconPath) {
        boolean alreadyPersists = false;
        for (Tab tab : getTabs()) {
            if (node.getClass().getSimpleName().equalsIgnoreCase(tab.getContent().getClass().getSimpleName())) {
                alreadyPersists = true;
            }
        }
        if (!alreadyPersists) {
            createCell(node, title, iconPath);
            StringBuilder tabs = new StringBuilder();
            tabs.append("SettingsFrame");
            for (Tab tab : getTabs()) {
                tabs.append(",").append(tab.getContent().getClass().getSimpleName()).append(",");
            }
            PropertiesManager.saveProperty("gameTabs", tabs.substring(0, tabs.length()-1));
        }
    }

    public void removeGame(Node node) {
        Tab removableTab = null;
        for (int i = 0; i < getTabs().size(); i++) {
            if (getTabs().get(i).getContent().getClass().getSimpleName().equalsIgnoreCase(node.getClass().getSimpleName())) {
                removableTab = getTabs().get(i);
            }
        }
        getTabs().remove(removableTab);

        StringBuilder tabs = new StringBuilder();
        for (Tab tab : getTabs()) {
            tabs.append(tab.getContent().getClass().getSimpleName()).append(",");
        }
        PropertiesManager.saveProperty("gameTabs", tabs.substring(0, tabs.length()-1));
    }

    private void createCell(Node node, String title, String iconPath) {
        Tab tab = new Tab("", node);
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(iconPath)).toString()));
        double imageWidth = 32;
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
