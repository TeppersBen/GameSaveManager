package com;

import com.frames.MainFrame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXLauncher extends Application {

    public static void startUp(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("MinecraftJavaSurvival World Manager");
        //stage.setResizable(false);
        Scene scene = new Scene(new MainFrame(), 1021, 550);
        stage.setScene(scene);
        stage.show();
    }
}
