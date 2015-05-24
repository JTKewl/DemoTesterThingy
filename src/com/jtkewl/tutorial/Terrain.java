package com.jtkewl.tutorial;

import java.awt.*;

/**
 * Created by Justin on 4/11/2015.
 */
public class Terrain {
    public int x;
    public int y;
    public Rectangle box;
    private int width;
    private int height;
    public Rectangle bottom;
    public Rectangle top;
    public Rectangle left;
    public Rectangle right;
    private int testGurth = 20;
    WorldTerrain parent;

    public Terrain(int x, int y, int width, int height, WorldTerrain boss) {
        this.width = width;
        this.height = height;
        this.parent = boss;
        this.x = x;
        this.y = y;

        this.bottom = new Rectangle();
        this.top = new Rectangle();
        this.left = new Rectangle();
        this.right = new Rectangle();
        box = new Rectangle(this.x, this.y, this.width, this.height);
    }

    public void update() {
        x = parent.totX;
        y = parent.totY;
        width = parent.width;
        height = parent.height;
        box.setLocation(x, y);
        updateFaces();
    }

    private void updateFaces() {
        bottom.setBounds(x, y + height - testGurth, width, testGurth);
        top.setBounds(x, y - 1, width, testGurth);
        left.setBounds(x - 1, y, testGurth, height);
        right.setBounds(x + width - testGurth + 1, y, testGurth, height);
    }

    public void updateTopBlock(int accel)
    {
        top.setBounds(x, y - 1, width,  testGurth + accel);
    }

    public void drawit(Graphics2D page, Color color) {
        page.setColor(color);
        page.drawRect(x, y, width, height);
        page.setColor(Color.ORANGE);
        page.drawRect(box.x, box.y, box.width, box.height);
        //page.drawRect(bottom.x, bottom.y, bottom.width, bottom.height);
        page.drawRect(top.x, top.y, top.width, top.height);
        //page.drawRect(left.x, left.y, left.width, left.height);
        //page.drawRect(right.x, right.y, right.width, right.height);
    }
}
