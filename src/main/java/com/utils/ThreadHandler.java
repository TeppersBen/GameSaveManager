package com.utils;

import javafx.application.Platform;

public class ThreadHandler {

    public static void initFXThread(IThreadAction action) {
        Platform.runLater(action::execute);
    }

    public interface IThreadAction {
        void execute();
    }

}
