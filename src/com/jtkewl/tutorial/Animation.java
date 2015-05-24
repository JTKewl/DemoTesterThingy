package com.jtkewl.tutorial;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class Animation {

    public int x;
    public int y;
    public String stat;

    private int numFrames;
    private int lastFrame;
    private int frameWidth;
    private int frameHeight;
    private int currentFrame;
    private int FrameOffset;
    private int FrameEnd;
    private int ySetting;
    private int xSetting;

    private long startTime;
    private long stopTime;
    private long delayTime;
    private long beginDelay;
    private long Created;

    private boolean repeat;
    private boolean running;
    private boolean isSet = false;
    private boolean hold;
    private boolean Direction = true;

    private BufferedImage picture;

    public Animation(BufferedImage image, int initX, int initY, int width, int height, int xSetter, int ySetter, int frames, long changeDelay, long startDelay, boolean doesRepeat) {
        this.picture = image;
        this.x = initX;
        this.y = initY;
        this.frameWidth = width;
        this.frameHeight = height;
        this.numFrames = frames;
        this.currentFrame = 0;
        this.delayTime = changeDelay;
        this.beginDelay = startDelay;
        this.repeat = doesRepeat;
        this.ySetting = ySetter;
        this.xSetting = xSetter;

        this.Created = System.currentTimeMillis();
        this.startTime = Created + beginDelay;
        this.stopTime = startTime + delayTime;

        this.FrameOffset = xSetting;
        this.FrameEnd = FrameOffset + width;

        this.running = true;
        this.isSet = true;
        this.stat = "idle";
    }
    public Animation(BufferedImage image, int initX, int initY, int width, int height, int xSetter, int ySetter, int frames, long changeDelay, long startDelay, boolean doesRepeat, boolean doesHold) {
        this.picture = image;
        this.x = initX;
        this.y = initY;
        this.frameWidth = width;
        this.frameHeight = height;
        this.numFrames = frames;
        this.currentFrame = 0;
        this.delayTime = changeDelay;
        this.beginDelay = startDelay;
        this.repeat = doesRepeat;
        this.hold = doesHold;
        this.ySetting = ySetter;
        this.xSetting = xSetter;

        this.Created = System.currentTimeMillis();
        this.startTime = Created + beginDelay;
        this.stopTime = startTime + delayTime;

        this.FrameOffset = xSetting;
        this.FrameEnd = FrameOffset + width;

        this.running = true;
        this.isSet = true;
        this.stat = "idle";
    }

    public void Update() {
        if (stopTime <= System.currentTimeMillis() && running) {
            currentFrame++;
            if (currentFrame >= numFrames) {
                if (repeat)
                    running = true;
                else if(hold){
                    running = false;
                }
                else {
                    running = false;
                    if(!stat.equals("idle")) stat = "done";
                }
                currentFrame = lastFrame;

            }

            FrameOffset = xSetting + (Math.abs(frameWidth * currentFrame));
            FrameEnd = FrameOffset + frameWidth;
            startTime = System.currentTimeMillis();
            stopTime = startTime + delayTime;
        }
    }

    public void Make(Graphics2D screen, int xOffset, int yOffset) {
        Update();

        this.x += xOffset;
        this.y += yOffset;
        if (Created + beginDelay <= System.currentTimeMillis() && Direction)
            screen.drawImage(picture, x, y, x + frameWidth, y + frameHeight, FrameOffset, ySetting, FrameEnd, frameHeight + ySetting, null);
        if (Created + beginDelay <= System.currentTimeMillis() && !Direction)
            screen.drawImage(picture, x + frameWidth + 10, y, x + 10, y + frameHeight, FrameOffset, ySetting, FrameEnd, frameHeight + ySetting, null);
    }

    public void changeFrame(int width, int height, int frames, int laster, int xSetter, int ySetter, long changeDelay/*, long startDelay*/, String newStat, boolean doesRepeat) {
        frameWidth = width;
        frameHeight = height;
        numFrames = frames;
        currentFrame = 0;
        lastFrame = laster;
        delayTime = changeDelay;
        //beginDelay = startDelay;
        repeat = doesRepeat;
        hold = false;
        ySetting = ySetter;
        xSetting = xSetter;
        stat = newStat;

        startTime = Created + beginDelay;
        stopTime = startTime + delayTime;

        running = true;
    }

    public void changeFrame(int width, int height, int frames, int laster, int xSetter, int ySetter, long changeDelay/*, long startDelay*/, String newStat, boolean doesRepeat, boolean doesHold) {
        frameWidth = width;
        frameHeight = height;
        numFrames = frames;
        currentFrame = 0;
        lastFrame = laster;
        delayTime = changeDelay;
        //beginDelay = startDelay;
        repeat = doesRepeat;
        hold = doesHold;
        ySetting = ySetter;
        xSetting = xSetter;
        stat = newStat;

        startTime = Created + beginDelay;
        stopTime = startTime + delayTime;

        running = true;
    }

    public void cutIt() {
        running = false;
        currentFrame = 0;
        stat = "done";
    }
    public void reset()
    {
        currentFrame = 0;
        stat = "running";
        running = true;
    }
    public void setDirection(boolean setter)
    {
        Direction = setter;
    }

    public void setPos(int newX, int newY) {
        x = newX;
        y = newY;
    }

    public int framesLeft()
    {
        return (this.numFrames - this.currentFrame);
    }

    public boolean getSet() {
        return (isSet);
    }

    public boolean getRunning() {
        return (running);
    }
}
