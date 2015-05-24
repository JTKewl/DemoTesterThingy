package com.jtkewl.tutorial;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Created by Justin on 4/11/2015.
 */
public class WorldTerrain {

    public int x;
    public int y;
    public int width = 50;
    public int height = 50;
    public int xOffset;
    public int yOffset;
    public int totX;
    public int totY;
    public Terrain hitBox;
    private BufferedImage image;
    private BufferedImage picty;
    private int[] pixels;

    public WorldTerrain(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.totX = this.x;
        this.totY = this.y;
        this.xOffset = 0;
        this.yOffset = 0;
        this.width = width;
        this.height = height;

        try {
            image = ImageIO.read(this.getClass().getResourceAsStream("/grounddeux.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        picty = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) picty.getRaster().getDataBuffer()).getData();

        setTerrain();

        hitBox = new Terrain(this.totX, this.totY, this.width, this.height, this);
        hitBox.update();
    }
    public WorldTerrain(int x, int y, int width, int height, String imgName) {
        this.x = x;
        this.y = y;
        this.totX = this.x;
        this.totY = this.y;
        this.xOffset = 0;
        this.yOffset = 0;
        this.width = width;
        this.height = height;

        try {
            image = ImageIO.read(this.getClass().getResourceAsStream("/" + imgName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        picty = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) picty.getRaster().getDataBuffer()).getData();

        setTerrain();

        hitBox = new Terrain(this.totX, this.totY, this.width, this.height, this);
        hitBox.update();
    }

    public void setTerrain() {
        int j = 0;
        int i = 0;
        int offset = 0;
        for (int k = 1; k < pixels.length; k++) {
            pixels[k] = image.getRGB(j, i);
            j++;
            if (j >= image.getWidth(null)) {
                j = 0;
            }
            if (k >= width + offset) {
                offset = k;
                i++;
                j = 0;
            }
            if (i >= image.getHeight(null)) {
                i = 0;
                j = 0;
            }
        }
    }
    public void update(int xOffset, int yOffset)
    {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.totX += this.xOffset;
        this.totY += this.yOffset;
        hitBox.update();
    }

    public void drawTerrain(Graphics2D g) {
        g.drawImage(picty, totX, totY, width, height, null);
        //hitBox.drawit(g, Color.YELLOW);
    }

}
