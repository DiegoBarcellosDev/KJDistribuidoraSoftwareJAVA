
package br.com.senac.kjdistribuidorasoftware.gui;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;


public class BackgroundPanel extends JPanel{
    
    private Image backgroundImage;

    public BackgroundPanel(Image image) {
        this.backgroundImage = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
