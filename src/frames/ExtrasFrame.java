package frames;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ExtrasFrame extends JPanel {
    public ExtrasFrame() {
        super(new BorderLayout());
        JButton button = new JButton("Open MCA-Selector");
        button.setToolTipText("Might not work, Requires JavaFX 15 located within the Program Files folder under Java.");
        button.addActionListener(e -> {
            try {
                String executionString = "java --module-path \"C:\\Program Files\\Java\\javafx-sdk-15.0.1\\lib\" --add-modules ALL-MODULE-PATH -jar " + System.getenv("APPDATA") + "\\.minecraft\\saves\\MinecraftJavaSurvival\\" + "mcaselector-1.13.2.jar";
                Runtime.getRuntime().exec(executionString);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        add(button, BorderLayout.CENTER);
    }
}
