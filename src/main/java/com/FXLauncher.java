package com;

import com.frames.core.Frame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXLauncher extends Application {

    public static void startUp(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("GameSaveManager");
        stage.setResizable(false);
        Scene scene = new Scene(new Frame(), 1021, 550);
        scene.getStylesheets().add("stylesheet.css");
        stage.setScene(scene);
        stage.show();
    }
}
