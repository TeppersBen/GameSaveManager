package com.frames.rimworld;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import com.managers.IOManager;
import com.managers.SavesManager;
import com.utils.ActionPerformer;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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

    public List<ManipulationTile> getManipulatorList() {
        List<ManipulationTile> tiles = new LinkedList<>();
        tiles.add(new ManipulationTile("Repair Broken Electronics", () -> rimworldSaveManipulator.repairBrokenDownDevices()));
        tiles.add(new ManipulationTile("Cure Colonists", () -> rimworldSaveManipulator.pawnsCureAllColonists()));
        return tiles;
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
            labelSelectedSave.setText("Selected Save: " + selectedItem);
        });
        return saves;
    }

    private BorderPane createManipulationsPane() {
        BorderPane parent = new BorderPane();
        FlowPane pane = new FlowPane();
        JFXButton buttonApplyManipulations = new JFXButton("Apply");
        List<ManipulationTile> tiles = getManipulatorList();
        pane.getChildren().addAll(tiles);
        buttonApplyManipulations.setOnAction(e -> {
            if (!selectedItem.isEmpty()) {
                rimworldSaveManipulator.attachFileContent(IOManager.readFileContent(savesLocation + "\\" + selectedItem));
                AtomicBoolean anythingToggled = new AtomicBoolean(false);
                tiles.stream().filter(b -> b.getToggleButton().isSelected()).forEach(c -> {
                    anythingToggled.set(true);
                    c.getAction().execute();
                });
                if (anythingToggled.get()) {
                    IOManager.writeToFile(savesLocation + "\\" + selectedItem, rimworldSaveManipulator.getContent());
                }
            }
        });
        parent.setTop(labelSelectedSave);
        parent.setCenter(pane);
        parent.setBottom(buttonApplyManipulations);
        return parent;
    }

    private class ManipulationTile extends FlowPane {
        private JFXToggleButton button;
        private ActionPerformer action;
        private String title;
        public ManipulationTile(String title, ActionPerformer action) {
            this.title = title;
            this.action = action;
            button = new JFXToggleButton();
            getChildren().addAll(new Label(title), button);
        }
        public JFXToggleButton getToggleButton() {
            return button;
        }
        public ActionPerformer getAction() {
            return action;
        }
        public String getButtonTitle() {
            return title;
        }
    }
}
