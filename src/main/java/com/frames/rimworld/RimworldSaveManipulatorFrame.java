package com.frames.rimworld;

import com.jfoenix.controls.JFXButton;
import com.managers.IOManager;
import com.managers.SavesManager;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class RimworldSaveManipulatorFrame extends BorderPane {

    private SavesManager savesManager;
    private Label labelSelectedSave;
    private RimworldSaveManipulator rimworldSaveManipulator;
    private String savesLocation;
    private String selectedItem;

    public RimworldSaveManipulatorFrame(String savesLocation) {
        this.savesLocation = savesLocation;
        savesManager = new SavesManager();
        rimworldSaveManipulator = new RimworldSaveManipulator();
        setLeft(createListView(savesLocation));
        setCenter(createManipulationsPane());
    }

    private ListView<Object> createListView(String savesLocation) {
        ListView<Object> saves = new ListView<>();
        Object[][] data = savesManager.loadSavesContent(savesLocation);
        for (Object[] item : data) {
            saves.getItems().addAll(item[0]);
        }
        labelSelectedSave = new Label();
        saves.getSelectionModel().selectedItemProperty().addListener(e -> {
            selectedItem = String.valueOf(saves.getSelectionModel().getSelectedItem());
            labelSelectedSave.setText(selectedItem);
            rimworldSaveManipulator.attachFileContent(IOManager.readFileContent(savesLocation + "\\" + selectedItem));
        });
        return saves;
    }

    private FlowPane createManipulationsPane() {
        FlowPane pane = new FlowPane();
        JFXButton buttonRepairBrokenElectronicDevices = new JFXButton("Repair Broken Devices");
        buttonRepairBrokenElectronicDevices.setOnAction(e -> {
            rimworldSaveManipulator.repairBrokenDownDevices();
            IOManager.writeToFile(savesLocation + "\\" + selectedItem, rimworldSaveManipulator.getContent());
        });
        pane.getChildren().addAll(
                buttonRepairBrokenElectronicDevices
        );
        return pane;
    }

}
