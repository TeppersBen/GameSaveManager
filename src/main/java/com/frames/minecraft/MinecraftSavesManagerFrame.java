package com.frames.minecraft;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.managers.SavesManager;
import com.utils.ThreadHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class MinecraftSavesManagerFrame extends BorderPane {

    private SavesManager savesManager;
    private JFXListView<MinecraftSavesManagerWorldTile> listViewWorlds;
    private JFXListView<JFXButton> listViewOptions;
    private long totalFolderSize;

    private JFXButton createBackupButton;
    private JFXButton refreshContentButton;
    private JFXButton purgeOldBackupsButton;

    private Label totalFolderSizeLabel;
    private Label actionPerformedLabel;

    public MinecraftSavesManagerFrame() {
        initComponents();
        refreshContent();
        initListeners();
        layoutComponents();
    }

    public void refreshContent() {
        Object[][] data = savesManager.loadSavesContent();
        listViewWorlds.getItems().removeAll(listViewWorlds.getItems());
        for (Object[] item : data) {
            listViewWorlds.getItems().add(new MinecraftSavesManagerWorldTile(savesManager.getMinecraftSavesFolder() + item[0].toString(), item, this));
        }

        long sum = 0;
        for (Object[] item : data) {
            sum += (long) item[1];
        }
        totalFolderSize = sum;

        totalFolderSizeLabel.setText("Total size: " + totalFolderSize + " MiB");
    }

    private void initComponents() {
        savesManager = new SavesManager();
        listViewOptions = new JFXListView<>();
        listViewWorlds = new JFXListView<>();
        createBackupButton = new JFXButton("Create Backup");
        refreshContentButton = new JFXButton("Refresh Table");
        totalFolderSizeLabel = new Label("Total size: " + totalFolderSize + " MiB");
        purgeOldBackupsButton = new JFXButton("Purge Old Backups");
        actionPerformedLabel = new Label();
    }

    private void initListeners() {
        createBackupButton.setOnAction(e -> ThreadHandler.initFXThread(() -> {
            setActionPerformedText(savesManager.createBackup());
            refreshContent();
        }));

        refreshContentButton.setOnAction(e -> {
            setActionPerformedText("Refreshed table");
            refreshContent();
        });

        purgeOldBackupsButton.setOnAction(e -> {
            setActionPerformedText(savesManager.purgeOldBackups());
            refreshContent();
        });
    }

    private void layoutComponents() {
        listViewOptions.getItems().addAll(
                refreshContentButton,
                createBackupButton,
                purgeOldBackupsButton
        );

        setValuesForListViews(listViewWorlds);
        setValuesForListViews(listViewOptions);

        setCenter(listViewWorlds);
        setRight(listViewOptions);

        BorderPane bottomPane = new BorderPane();
        bottomPane.setLeft(totalFolderSizeLabel);
        bottomPane.setRight(actionPerformedLabel);
        setBottom(bottomPane);
    }

    private void setValuesForListViews(JFXListView<?> listview) {
        listview.setExpanded(true);
        listview.setDepth(1);
        listview.setVerticalGap(3.0);
        listview.setPadding(new Insets(10));
    }

    public void setActionPerformedText(String text) {
        actionPerformedLabel.setText(text);
    }

}