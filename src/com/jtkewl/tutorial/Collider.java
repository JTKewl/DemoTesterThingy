package com.jtkewl.tutorial;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Collider {
    public int x;
    public int y;
    public Rectangle box;
    public int xOffset;
    public int yOffset;
    private int width;
    private int height;
    Character parent;

    public Collider(int width, int height, int xOffset, int yOffset, Character boss) {
        this.width = width - 5;
        this.height = height - 5;
        this.parent = boss;
        this.x = parent.x + 5;
        this.y = parent.y + 5;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        box = new Rectangle(this.x, this.y, this.width, this.height);
    }

    public void update() {
        x = parent.x + xOffset;
        y = parent.y + yOffset;
        box.setLocation(this.x, this.y);
    }

    public void drawit(Graphics page, Color color) {
        //page.dispose();
        page.setColor(color);
        page.drawRect(x, y, width, height);
    }
}
