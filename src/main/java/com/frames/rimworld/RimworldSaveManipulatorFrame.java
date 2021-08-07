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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class RimworldSaveManipulatorFrame extends BorderPane {

    private final SavesManager savesManager;
    private Label labelSelectedSave;
    private final RimworldSaveManipulator rimworldSaveManipulator;
    private final String savesLocation;
    private String selectedItem;

    public RimworldSaveManipulatorFrame(String savesLocation) {
        this.savesLocation = savesLocation;
        savesManager = new SavesManager();
        rimworldSaveManipulator = new RimworldSaveManipulator();
        ListView<Object> validSaves = createListView(savesLocation);
        BorderPane manipulations = createManipulationsPane();
        setLeft(validSaves);
        setCenter(manipulations);

        setId("container");
        manipulations.setId("container-tile");
        validSaves.setId("container-tile");
        labelSelectedSave.setId("container-title");
    }

    public List<ManipulationTile> getManipulatorList() {
        List<ManipulationTile> tiles = new LinkedList<>();
        tiles.add(new ManipulationTile("Finish All Buildings", rimworldSaveManipulator::finishBuildings));
        tiles.add(new ManipulationTile("Repair Broken Electronics", rimworldSaveManipulator::repairBrokenDownDevices));
        tiles.add(new ManipulationTile("Cure Colonists", rimworldSaveManipulator::pawnsCureAllColonists));
        tiles.add(new ManipulationTile("All Pawns 999 Honor", rimworldSaveManipulator::pawnsGiveAllLoyalists999Honor));
        tiles.add(new ManipulationTile("Max Out All Item Stacks", rimworldSaveManipulator::itemMaxOutStackCount));
        tiles.add(new ManipulationTile("Fully Grow All Crops", rimworldSaveManipulator::plantForcedMaxGrowth));
        tiles.add(new ManipulationTile("Clean Whole Area", rimworldSaveManipulator::cleanWholeArea));
        tiles.add(new ManipulationTile("Force Happy Pawns", rimworldSaveManipulator::forceVeryHappyColonists));
        return tiles;
    }

    private ListView<Object> createListView(String savesLocation) {
        ListView<Object> saves = new ListView<>();
        Object[][] data = savesManager.loadSavesContent(savesLocation);
        for (Object[] item : data) {
            if ((String.valueOf(item[0]).indexOf(".rws") + ".rws".length() == String.valueOf(item[0]).length())) {
                saves.getItems().addAll(item[0]);
            }
        }
        labelSelectedSave = new Label();
        labelSelectedSave.setText("Selected Save: NONE");
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
        resizeManipulationTiles(tiles);
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

    private void resizeManipulationTiles(List<ManipulationTile> tiles) {
        AtomicReference<Integer> width = new AtomicReference<>(0);
        tiles.forEach(e -> {
            if (width.get() < e.getTitle().length()) {
                width.set(e.getTitle().length());
            }
        });
        int endWidth = width.get() * 9;
        System.out.println("End Result: " + endWidth);
        tiles.forEach(e -> e.setWidth(endWidth));
    }

    private static class ManipulationTile extends FlowPane {
        private final JFXToggleButton button;
        private final ActionPerformer action;
        private final String title;
        public ManipulationTile(String title, ActionPerformer action) {
            this.title = title;
            this.action = action;
            button = new JFXToggleButton();
            getChildren().addAll(button, new Label(title));
            setPrefWidth(80);
            setMaxWidth(80);
        }
        public JFXToggleButton getToggleButton() {
            return button;
        }
        public ActionPerformer getAction() {
            return action;
        }
        public String getTitle() {
            return title;
        }
        public void setWidth(int width) {
            setPrefWidth(width);
            setMaxWidth(width);
        }
    }
}
