package com.jtkewl.tutorial;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

/**
 * Created by Justin on 4/14/2015.
 */
public class StatusBar {
    private int x;
    private int y;
    private double numBars;
    private int realwidth;
    private int realheight;
    private int width;
    private int height;

    private BufferedImage image;
    private BufferedImage picty;
    private int[] pixels;

    public StatusBar(int x, int y,double numSwath, int width, int height, String filePath)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.numBars = numSwath;

        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.realheight = image.getHeight();
        this.realwidth = image.getWidth() * 10;
        this.picty = new BufferedImage(this.realwidth, this.realheight, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) picty.getRaster().getDataBuffer()).getData();

        setPicture();
    }

    public void update(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.realheight = image.getHeight();
        this.realwidth = (int)(image.getWidth() * numBars);
        this.picty = new BufferedImage(this.realwidth, this.realheight, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) picty.getRaster().getDataBuffer()).getData();

        setPicture();
    }

    public void setPicture() {
        int j = 0;
        int i = 0;
        int offset = 0;
        for (int k = 1; k < pixels.length; k++) {
            pixels[k] = image.getRGB(j, i);
            j++;
            if (j >= image.getWidth(null)) {
                j = 0;
            }
            if (k >= realwidth + offset) {
                offset = k;
                i++;
                j = 0;
            }
            if (i >= realheight) {
                i = 0;
                j = 0;
            }
        }
    }

    public void drawIt(Graphics2D g)
    {
        g.drawImage(picty, x, y, width, height, null);
        //g.drawImage(picty, 50, 50, picty.getWidth(), picty.getHeight(), null);
        //g.scale(scalarX, scalarY);
    }
}
