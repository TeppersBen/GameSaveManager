package com.utils;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

public class ThreadHandler {

    public static void initFXThread(IThreadAction action) {
        Platform.runLater(action::execute);
    }

    public interface IThreadAction {
        void execute();
    }

}
