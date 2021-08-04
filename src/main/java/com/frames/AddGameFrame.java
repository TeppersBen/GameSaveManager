package com.frames;

import com.frames.core.Frame;
import com.frames.factorio.FactorioMainFrame;
import com.frames.minecraft.MinecraftMainFrame;
import com.frames.rimworld.RimworldMainFrame;
import com.frames.satisfactory.SatisfactoryMainFrame;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

public class AddGameFrame extends FlowPane {

    private Frame frame;

    public AddGameFrame(Frame frame) {
        this.frame = frame;
        getChildren().addAll(
                createGameTile(new MinecraftMainFrame(), "Minecraft", "/icons/gameSections/minecraft.jpg"),
                createGameTile(new FactorioMainFrame(), "Factorio", "/icons/gameSections/factorio.png"),
                createGameTile(new SatisfactoryMainFrame(), "Satisfactory", "/icons/gameSections/satisfactory.png"),
                createGameTile(new RimworldMainFrame(), "Rimworld", "/icons/gameSections/Rimworld.png")
        );
    }

    private Node createGameTile(Node node, String title, String iconPath) {
        FlowPane pane = new FlowPane(Orientation.VERTICAL);
        pane.setColumnHalignment(HPos.CENTER);

        JFXButton buttonAddGame = new JFXButton("Add Game");
        JFXButton buttonRemoveGame = new JFXButton("Remove Game");

        ImageView imageView = new ImageView(new Image(getClass().getResource(iconPath).toString()));
        double imageWidth = 40;
        imageView.setFitHeight(imageWidth);
        imageView.setFitWidth(imageWidth);

        buttonAddGame.setOnAction(e -> frame.addGame(node, title, iconPath));
        buttonRemoveGame.setOnAction(e -> frame.removeGame(node));

        pane.getChildren().addAll(
                imageView,
                new Label(title),
                buttonAddGame,
                buttonRemoveGame
        );
        return pane;
    }

}
