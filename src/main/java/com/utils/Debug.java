package com.utils;

import javafx.scene.Node;

@SuppressWarnings("All")
public class Debug {

    public static void createDebugBox(Node node, String color) {
        node.setStyle("-fx-border-color:"+color);
    }

}
