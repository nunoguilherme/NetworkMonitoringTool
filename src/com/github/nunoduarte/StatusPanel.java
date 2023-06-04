package com.github.nunoduarte;

import javax.swing.JPanel;
import java.awt.Color;

public class StatusPanel extends JPanel {

    public StatusPanel() {
        this.setBackground(Color.RED);
    }

    public void setStatus(boolean status) {
        this.setBackground(status ? Color.GREEN : Color.RED);
    }
}

