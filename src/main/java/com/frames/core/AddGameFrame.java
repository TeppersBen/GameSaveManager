package com.frames.core;

import com.frames.core.Frame;
import com.frames.factorio.FactorioMainFrame;
import com.frames.minecraft.MinecraftMainFrame;
import com.frames.rimworld.RimworldMainFrame;
import com.frames.satisfactory.SatisfactoryMainFrame;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.managers.IOManager;
import com.managers.PropertiesManager;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class AddGameFrame extends VBox {

    private Frame frame;

    public AddGameFrame(Frame frame) {
        this.frame = frame;
        setId("container");
        getChildren().addAll(
                createGameTile(new MinecraftMainFrame(), "Minecraft", "/icons/gameSections/minecraft.jpg"),
                createGameTile(new FactorioMainFrame(), "Factorio", "/icons/gameSections/factorio.png"),
                createGameTile(new SatisfactoryMainFrame(), "Satisfactory", "/icons/gameSections/satisfactory.png"),
                createGameTile(new RimworldMainFrame(), "Rimworld", "/icons/gameSections/Rimworld.png")
        );
    }

    private Node createGameTile(Node node, String title, String iconPath) {
        BorderPane pane = new BorderPane();
        BorderPane sidePane = new BorderPane();
        Label labelTitle = new Label(title);
        JFXTextField saveLocation = new JFXTextField();
        setId("container-tile");

        FlowPane sectionImage = new FlowPane(Orientation.VERTICAL);
        sectionImage.setColumnHalignment(HPos.CENTER);
        sectionImage.setAlignment(Pos.CENTER);
        sectionImage.setMinWidth(100);

        ImageView imageView = new ImageView(new Image(getClass().getResource(iconPath).toString()));
        double imageWidth = 40;
        imageView.setFitHeight(imageWidth);
        imageView.setFitWidth(imageWidth);

        JFXButton button = new JFXButton("");
        button.setPrefWidth(50);
        if (PropertiesManager.getProperty("gameTabs").contains(title)) {
            button.setText("-");
        } else {
            button.setText("+");
        }
        button.setOnAction(e -> {
            if (PropertiesManager.getProperty("gameTabs").contains(title)) {
                button.setText("+");
                frame.removeGame(node);
            } else {
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

        /*createDebugBox(sectionImage, "purple");
        createDebugBox(imageView, "yellow");
        createDebugBox(pane, "blue");
        createDebugBox(labelTitle, "magenta");
        createDebugBox(sidePane, "red");
        createDebugBox(saveLocation, "brown");*/

        return pane;
    }

    private void createDebugBox(Node node, String color) {
        node.setStyle("-fx-border-color:"+color);
    }

}
