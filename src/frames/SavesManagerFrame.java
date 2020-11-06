package frames;

import managers.SavesManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class SavesManagerFrame extends JPanel {

    private JButton createBackupButton;
    private JButton refreshTableButton;
    private JButton wipeOldBackupsButton;
    private JButton wipeUnnecessaryChunksButton;
    private JButton replaceWorldWithBackup;
    private JLabel totalFolderSize;
    private JLabel output;
    private JPanel tablePanel;
    private JTable table;
    private DefaultTableModel tableModel;

    private SavesManager savesManager;

    private String[] columnNames = { "File Name", "Size" };
    private Object[][] data = { {"", ""} };

    public SavesManagerFrame() {
        super(new BorderLayout());
        initComponents();
        setUpToolTips();
        initListeners();
        layoutComponents();
        refreshTable();
    }

    private void layoutComponents() {
        JPanel p = new JPanel(new FlowLayout());
        p.add(createBackupButton);
        p.add(wipeUnnecessaryChunksButton);
        p.add(wipeOldBackupsButton);

        JPanel p1 = new JPanel(new FlowLayout());
        p1.add(refreshTableButton);
        p1.add(replaceWorldWithBackup);

        JPanel p2 = new JPanel(new BorderLayout());
        p2.add(p, BorderLayout.NORTH);
        p2.add(p1, BorderLayout.CENTER);

        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(table, BorderLayout.CENTER);

        JPanel p3 = new JPanel(new BorderLayout());
        p3.add(totalFolderSize, BorderLayout.WEST);
        p3.add(output, BorderLayout.EAST);

        add(p2, BorderLayout.NORTH);
        add(new JScrollPane(tablePanel), BorderLayout.CENTER);
        add(p3, BorderLayout.SOUTH);
    }

    private void initListeners() {
        refreshTableButton.addActionListener(e -> {
            refreshTable();
            printOutputText("Table refreshed!");
        });
        createBackupButton.addActionListener(e -> {
            printOutputText("Backup Created: " + savesManager.createBackup());
            refreshTable();
        });
        wipeOldBackupsButton.addActionListener(e -> {
            printOutputText(savesManager.purgeOldBackups());
            refreshTable();
        });
        wipeUnnecessaryChunksButton.addActionListener(e -> {
            printOutputText(savesManager.purgeUnnecessaryChunks());
            refreshTable();
        });
        replaceWorldWithBackup.addActionListener(e -> {
            printOutputText(savesManager.replaceWorldWithBackup());
            refreshTable();
        });
    }

    private void setUpToolTips() {
        refreshTableButton.setToolTipText("Syncs with folders/files within the MC-Saves folder.");
        createBackupButton.setToolTipText("Creates a backup of the primary world and refreshes the table.");
        wipeOldBackupsButton.setToolTipText("Delete old backups from the device.");
        wipeUnnecessaryChunksButton.setToolTipText("Delete chunks that not belong to the original package.");
        replaceWorldWithBackup.setToolTipText("Remove current main save and replace it with the latest backup.");
    }

    private void initComponents() {
        createBackupButton = new JButton("Create Backup");
        refreshTableButton = new JButton("Refresh Table");
        wipeOldBackupsButton = new JButton("Purge Old Backups");
        wipeUnnecessaryChunksButton = new JButton("Purge Unnecessary Chunks");
        replaceWorldWithBackup = new JButton("Replace World With Backup");
        totalFolderSize = new JLabel("Total Size: ");
        output = new JLabel("");
        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);
        table.setEnabled(false);
        tablePanel = new JPanel(new BorderLayout());

        savesManager = new SavesManager();
    }

    private void refreshTable() {
        data = savesManager.loadSavesContent();
        long sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += (long) data[i][1];
            data[i][1] = data[i][1] + " MiB";
        }
        totalFolderSize.setText("Total Size: " + sum + " MiB");
        tableModel = new DefaultTableModel(data, columnNames);
        table.setModel(tableModel);
        resizeColumnWidth(table);
    }

    public void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 300)
                width=300;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    private void printOutputText(String message) {
        output.setText(message);
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                output.setText("");
            } catch (InterruptedException e) {
                e.printStackTrace();
                output.setText("THREAD CRASHED!");
            }
        }).start();
    }
}
