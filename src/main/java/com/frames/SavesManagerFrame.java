package com.frames;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.managers.SavesManager;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class SavesManagerFrame extends BorderPane {

    private SavesManager savesManager;
    private JFXListView<SavesManagerWorldTile> listViewWorlds;
    private JFXListView<JFXButton> listViewOptions;
    private Object[][] data;
    private long totalFolderSize;

    private JFXButton createBackupButton;
    private JFXButton refreshContentButton;
    private JFXButton purgeOldBackupsButton;

    private Label totalFolderSizeLabel;

    public SavesManagerFrame() {
        initComponents();
        refreshContent();
        initListeners();
        layoutComponents();
    }

    public void refreshContent() {
        data = savesManager.loadSavesContent();
        listViewWorlds.getItems().removeAll(listViewWorlds.getItems());
        for (Object[] item : data) {
            listViewWorlds.getItems().add(new SavesManagerWorldTile(savesManager.getMinecraftSavesFolder() + item[0].toString(), item, this));
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

        listViewOptions.getItems().addAll(
                refreshContentButton,
                createBackupButton,
                purgeOldBackupsButton
        );
    }

    private void initListeners() {
        createBackupButton.setOnAction(e -> {
            savesManager.createBackup();
            refreshContent();
        });

        refreshContentButton.setOnAction(e -> {
            refreshContent();
        });

        purgeOldBackupsButton.setOnAction(e -> {
            savesManager.purgeOldBackups();
            refreshContent();
        });
    }

    private void layoutComponents() {
        listViewWorlds.setExpanded(true);
        listViewWorlds.setDepth(1);
        listViewWorlds.setVerticalGap(3.0);

        listViewOptions.setExpanded(true);
        listViewOptions.setDepth(1);
        listViewOptions.setVerticalGap(3.0);

        setCenter(listViewWorlds);
        setRight(listViewOptions);
        setBottom(totalFolderSizeLabel);
    }

}