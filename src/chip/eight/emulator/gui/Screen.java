package chip.eight.emulator.gui;

import javax.swing.*;
import java.awt.*;

public class Screen extends JPanel {
    private static final Color PIXEL_BORDER_COLOR = new Color(43, 43, 43);

    private int screenWidth;
    private int screenHeight;
    private int[] data;
    private int scale;
    private Color setColor;
    private Color unsetColor;

    public Screen(int screenWidth, int screenHeight, int scale) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.data = new int[screenWidth * screenHeight];
        this.scale = scale;

        // default pixel colors
        this.setColor = Color.WHITE;
        this.unsetColor = Color.BLACK;
    }

    public void clearScreen() {
        for(int i = 0; i < data.length; i++) {
            data[i] = 0x0;
        }
    }

    public int readPixel(int pos) {
        return data[pos];
    }

    public void writePixel(int pos, int pixel) {
        data[pos] = pixel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int pos, xPos, yPos, width, height;
        for(int i = 0; i < screenHeight; i++) {
            for(int j = 0; j < screenWidth; j++) {
                pos = (i * screenWidth) + j; xPos = j * scale; yPos = i * scale;
                width = scale; height = scale;

                g.setColor(data[pos] == 0x1 ? setColor : unsetColor);
                g.fillRect(xPos, yPos, width, height);

                g.setColor(PIXEL_BORDER_COLOR);
                g.drawRect(xPos, yPos, width, height);
            }
        }
    }
}
