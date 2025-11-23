package com.halin.taifooddesktop;

import com.halin.taifooddesktop.gui.MainApp;
import javax.swing.SwingUtilities;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
    }
}