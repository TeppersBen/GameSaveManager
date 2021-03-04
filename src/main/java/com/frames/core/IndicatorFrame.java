package com.frames.core;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;

public class IndicatorFrame extends BorderPane {

    public IndicatorFrame() {
        ProgressIndicator pi = new ProgressIndicator();
        setCenter(pi);
        setId("indicatorFrame");
    }

}
