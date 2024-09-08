package src.main;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("SSHN by CC35A");
        window.setLayout(new BorderLayout());

        MapPanel mapPanel = new MapPanel();
        window.add(mapPanel, BorderLayout.CENTER);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        mapPanel.startMapThread();
    }
}
