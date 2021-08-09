package com.frames.core;

import com.frames.factorio.FactorioMainFrame;
import com.frames.minecraft.MinecraftMainFrame;
import com.frames.rimworld.RimworldMainFrame;
import com.frames.satisfactory.SatisfactoryMainFrame;
import com.frames.sims4.Sims4MainFrame;
import com.frames.valheim.ValheimMainFrame;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.managers.IOManager;
import com.managers.PropertiesManager;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class SettingsFrame extends BorderPane {

    private final Frame frame;

    public SettingsFrame(Frame frame) {
        this.frame = frame;
        setId("container");
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.getChildren().addAll(
                createGameTile(new MinecraftMainFrame(), "Minecraft", "/icons/gameSections/minecraft.jpg"),
                createGameTile(new FactorioMainFrame(), "Factorio", "/icons/gameSections/factorio.png"),
                createGameTile(new SatisfactoryMainFrame(), "Satisfactory", "/icons/gameSections/satisfactory.png"),
                createGameTile(new RimworldMainFrame(), "Rimworld", "/icons/gameSections/Rimworld.png"),
                createGameTile(new Sims4MainFrame(), "Sims4", "/icons/gameSections/Sims4.png"),
                createGameTile(new ValheimMainFrame(), "Valheim", "/icons/gameSections/Valheim.png")
        );

        JFXButton buttonWipeData = new JFXButton("Wipe Application Data");
        buttonWipeData.setId("button-permanently-remove");
        buttonWipeData.setOnAction(e -> IOManager.wipeApplicationData());

        JFXButton buttonOpenApplicationFolder = new JFXButton("Open Application Folder");
        buttonOpenApplicationFolder.setOnAction(e -> IOManager.openApplicationFolder());

        FlowPane buttonPane = new FlowPane();
        buttonPane.getChildren().addAll(buttonOpenApplicationFolder, buttonWipeData);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setId("container");
        scrollPane.setContent(vbox);
        setCenter(scrollPane);
        scrollPane.viewportBoundsProperty().addListener((observableValue, bounds, t1) -> {
            Node content = scrollPane.getContent();
            scrollPane.setFitToWidth(content.prefWidth(-1) < t1.getWidth());
            scrollPane.setFitToHeight(content.prefHeight(-1) < t1.getHeight());
        });

        setBottom(buttonPane);
    }

    private Node createGameTile(Node node, String title, String iconPath) {
        BorderPane pane = new BorderPane();
        BorderPane sidePane = new BorderPane();
        Label labelTitle = new Label(title);
        JFXTextField saveLocation = new JFXTextField();
        pane.setId("container-tile");
        pane.setPrefHeight(100);

        FlowPane sectionImage = new FlowPane(Orientation.VERTICAL);
        sectionImage.setColumnHalignment(HPos.CENTER);
        sectionImage.setAlignment(Pos.CENTER);
        sectionImage.setMinWidth(100);

        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(iconPath)).toString()));
        double imageWidth = 40;
        imageView.setFitHeight(imageWidth);
        imageView.setFitWidth(imageWidth);

        JFXButton button = new JFXButton("");
        button.setPrefWidth(50);
        if (PropertiesManager.getProperty("gameTabs").contains(title)) {
            button.setId("button-remove");
            button.setText("-");
        } else {
            button.setId("button-add");
            button.setText("+");
        }
        button.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        button.setOnAction(e -> {
            if (PropertiesManager.getProperty("gameTabs").contains(title)) {
                button.setId("button-add");
                button.setText("+");
                frame.removeGame(node);
            } else {
                button.setId("button-remove");
                button.setText("-");
                frame.addGame(node, title, iconPath);
            }
        });

        saveLocation.focusedProperty().addListener(e -> PropertiesManager.saveProperty("pathTo"+title+"SaveFolder", saveLocation.getText()));
        saveLocation.setText(PropertiesManager.getProperty("pathTo"+title+"SaveFolder"));

        sectionImage.getChildren().addAll(imageView, labelTitle);

        sidePane.setLeft(sectionImage);
        sidePane.setCenter(button);
        pane.setLeft(sidePane);
        pane.setCenter(saveLocation);

        return pane;
    }
}
