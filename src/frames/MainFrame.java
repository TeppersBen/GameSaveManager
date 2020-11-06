package frames;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Saves Folder Manager", new SavesManagerFrame());
        tabs.addTab("Settings", new JLabel("Not Implemented Yet!"));
        tabs.addTab("Extra", new ExtrasFrame());
        setLocationRelativeTo(null);
        add(tabs);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("MinecraftJavaSurvival World Manager");
        pack();
        setVisible(true);
    }

}
