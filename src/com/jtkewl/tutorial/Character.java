package com.jtkewl.tutorial;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Character {

    public Animation mover;
    public Animate Animate;
    private BufferedImage image;
    public boolean gravity;
    private int gForce;
    private double gAccel = 0;
    private long gStartTime;
    private boolean isGrounded;
    public boolean isIdle;
    public int x;
    public int y;
    public int centerX;
    public int centerY;
    public int Y_SPEED = 0;
    public int Y_MAX = 2;
    public int Y_CURRENT = Y_MAX;
    public int X_SPEED = 0;
    public int X_MAX = 4;
    public int X_CURRENT = X_SPEED;
    private boolean facingRight = true;
    public Collider hitBox;
    public Collider gravBox;
    public Collider topBox;
    private int gMax = 0;
    private int health;
    private boolean gravitating = false;
    private boolean Xgo = false;


    public Character(int x, int y, int width, int height, boolean gravity, int health) {
        this.x = x;
        this.y = y;
        centerX = x + 20;
        centerY = y + 20;
        this.health = health;
        this.Y_CURRENT = 0;
        gForce = 0;
        gAccel = 0.001;
        this.gravity = gravity;
        hitBox = new Collider(width, height, 15, 20, this);
        gravBox = new Collider(width + 2, 10, hitBox.xOffset, hitBox.yOffset + height - 8, this);
        topBox = new Collider(width + 2, 10, hitBox.xOffset, hitBox.yOffset, this);
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream("/hidemega.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mover = new Animation(image, x, y, 40, 40, 18, 66, 6, 75, 0, false);
        isIdle = true;
        Animate = new Animate();
        gStartTime = System.currentTimeMillis();
    }

    public void reset() {
        x = 0;
        y = 0;
        gForce = 0;
        gAccel = 0;
        isIdle = true;
        gStartTime = System.currentTimeMillis();
    }

    public void update() {

        if (!this.isGrounded()) {
            if (!gravitating)
                gStartTime = System.currentTimeMillis();
            gravitate();
            if(Y_SPEED <= (gMax))
                Y_SPEED = (gMax);
            moveY();
            gravitating = true;
        } else {
            Y_SPEED = 0;
            Y_CURRENT = 0;
            gravitating = false;
        }
        if (this.Xgo) {
            moveX();
            this.Xgo = false;
        }
        if(isGrounded && mover.stat.equals("jumping"))
            Animate.idle();
        mover.x = this.x;
        mover.y = this.y;
        centerX = this.x + hitBox.xOffset + 10;
        centerY = this.y + hitBox.yOffset + 20;
        hitBox.update();
        gravBox.update();
        topBox.update();
    }

    public void setPos(int newX, int newY) {
        x = newX;
        y = newY;
    }

    public void resetGravity() {
        gStartTime = System.currentTimeMillis();
    }

    public void moveX() {
        if (facingRight)
            setPos(x + X_SPEED, y);
        else
            setPos(x - X_SPEED, y);
    }

    public void moveY() {
        if(Y_SPEED < gMax)
            Y_SPEED = gMax;
        setPos(x, y - Y_SPEED);
    }

    public void goX() {
        this.Xgo=true;
    }

        //TEST COMMENT :3

    public void gravitate() {
        long deltaT = System.currentTimeMillis() - gStartTime;
        double temp = .001 * deltaT;
        Y_SPEED = (int)(Y_CURRENT - temp);
        if(Y_SPEED < gMax)
            Y_SPEED = gMax;
    }

    public void setX_SPEED(int speed) {
        X_SPEED = speed;
    }

    public void setY_SPEED(int speed) {
        /*if (speed == 0)
            Y_CURRENT = 0;
        else if (isGrounded)
            Y_CURRENT = speed + 6;
        else
            Y_CURRENT = speed;*/
        Y_CURRENT = speed;
    }

    public void setY_MAX(int speed) {
        Y_MAX = speed;
    }

    private int iter = 0;

    /*public void yChanger() {
        Y_SPEED -= iter;
        iter++;
        System.out.println(iter);
        if (Y_SPEED <= 0) {
            Y_SPEED = 0;
        }
    }

    public void setG(double grav) {
        gAccel = grav;
    }

    public void setPureG(int grav) {
        gForce = grav;
    }

    public void gravitas() {
        gForce = 0.02931;
        Y_SPEED -= gForce;

        if (Y_SPEED <= (-1*gMax)) {
            Y_SPEED = (-1*gMax);
            gAccel = 0;
            //System.out.println("HA!!!");
        }
    }

    public void gravitate() {
        iter++;
        gForce = (int) (gAccel);
        if (gAccel >= gMax) {
            gForce = gMax;
        }
        this.y -= (gAccel * iter);
        this.Y_CURRENT = Y_SPEED;
    }*/

    public String[] toStringArray() {
        return (new String[]
                {
                        "X: " + this.x + "      Y: " + this.y,
                        "CX: " + this.centerX + "       Y: " + this.centerY,
                        "X_SPEED: " + this.X_SPEED + "      Y_SPEED: " + this.Y_SPEED,
                        "Y_CURRENT: " + this.Y_CURRENT + "      Y_MAX: " + this.Y_MAX,
                        "Grounded? " + this.isGrounded() + "        Really? " + this.isGrounded,
                        "Frame: " + this.mover.stat + "Time Left: " + mover.framesLeft()
                });
    }

    public void setGravity(boolean gravitate) {
        this.gravity = gravitate;
    }

    public void setGrounded(boolean ground) {
        isGrounded = ground;
    }

    public boolean isGrounded() {
        return (isGrounded);
    }

    public void setGMax(int num) {
        gMax = num;
    }

    public int getgForce() {
        return (gForce);
    }

    public int getHealth() {
        return (health);
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setDirection(boolean right) {
        if (facingRight != right) {
            //flipper();
            facingRight = right;
        }
    }

    /*public void flipper() {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -image.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);
    }*/

    public class Animate {
        public Animate() {
            spawn();
        }

        public void spawn() {
            int width = 40;
            int height = 58;
            int numFrames = 8;
            int lastFrame = 7;
            long delayTime = 75;
            int xSetter = 0;
            int ySetter = 116;
            String stat = "spawn";

            isIdle = false;

            mover.changeFrame(width, height, numFrames, lastFrame, xSetter, ySetter, delayTime, stat, false);
        }

        public void idle() {
            int width = 40;
            int height = 58;
            int numFrames = 6;
            int lastFrame = 0;
            long delayTime = 75;
            int xSetter = 0;
            int ySetter = 0;
            String stat = "idle";

            isIdle = true;

            mover.changeFrame(width, height, numFrames, lastFrame, xSetter, ySetter, delayTime, stat, false);
        }

        public void runnedRight() {
            int width = 53;
            int height = 58;
            int numFrames = 10;
            int lastFrame = 0;
            long delayTime = 60;
            int xSetter = 0;
            int ySetter = 58;
            mover.setDirection(true);
            String stat = "runningRight";

            isIdle = false;

            mover.changeFrame(width, height, numFrames, lastFrame, xSetter, ySetter, delayTime, stat, false);
        }

        public void runnedLeft() {
            int width = 53;
            int height = 58;
            int numFrames = 10;
            int lastFrame = 0;
            long delayTime = 60;
            int xSetter = 0;
            int ySetter = 58;
            mover.setDirection(false);
            String stat = "runningLeft";

            isIdle = false;

            mover.changeFrame(width, height, numFrames, lastFrame, xSetter, ySetter, delayTime, stat, false);
        }

        public void jumped() {
            int width = 40;
            int height = 58;
            int numFrames = 7;
            int lastFrame = 7;
            long delayTime = 60;
            int xSetter = 0;
            int ySetter = 174;
            String stat = "jumping";

            isIdle = false;

            mover.changeFrame(width, height, numFrames, lastFrame, xSetter, ySetter, delayTime, stat, false, true);
        }

        public void rainbows() {
            int width = 51;
            int height = 58;
            int numFrames = 8;
            int lastFrame = 7;
            long delayTime = 60;
            int xSetter = 0;
            int ySetter = 233;
            String stat = "Rainbows";

            isIdle = false;

            mover.changeFrame(width, height, numFrames, lastFrame, xSetter, ySetter, delayTime, stat, true, true);
        }

        public void stopAnimation() {
            mover.cutIt();
        }

        public void reset() {
            mover.reset();
        }


        public void Make(Graphics2D g2d, int xOffset, int yOffset) {
            g2d.setColor(Color.CYAN);
            g2d.fillOval(centerX - 2, centerY - 2, 4, 4);
            mover.Make(g2d, xOffset, yOffset);
        }
    }
}
