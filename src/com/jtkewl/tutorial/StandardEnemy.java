package com.jtkewl.tutorial;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Justin on 5/7/2015.
 */
public class StandardEnemy {

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
    public int Y_MAX = 6;
    public int Y_CURRENT = Y_MAX;
    public int X_SPEED = 0;
    public int X_MAX = 4;
    public int X_CURRENT = X_SPEED;
    private boolean facingRight = true;
    public EnemyCollider hitBox;
    public EnemyCollider gravBox;
    public EnemyCollider topBox;
    private int gMax = 10;
    private int health;
    private boolean gravitating = false;
    private boolean Xgo = false;
    private int totXoffset = 0;
    private int totYoffset = 0;


    public StandardEnemy(int x, int y, int width, int height, boolean gravity, int health) {
        this.x = x;
        this.y = y;
        centerX = x + 20;
        centerY = y + 20;
        this.health = health;
        this.Y_CURRENT = 0;
        gForce = 0;
        gAccel = 0.001;
        this.gravity = gravity;
        hitBox = new EnemyCollider(width, height, 15, 20, this);
        gravBox = new EnemyCollider(width + 2, 10, hitBox.xOffset, hitBox.yOffset + height - 8, this);
        topBox = new EnemyCollider(width + 2, 10, hitBox.xOffset, hitBox.yOffset, this);
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

    public void update(int xOffset, int yOffset) {
        if(gravity) {
            if (!this.isGrounded()) {
                if (!gravitating)
                    gStartTime = System.currentTimeMillis();
                gravitate();
                if (Y_SPEED <= (-gMax))
                    Y_SPEED = (-gMax);
                moveY();
                gravitating = true;
            } else {
                Y_SPEED = 0;
                Y_CURRENT = 0;
                gravitating = false;
            }
        }
        else
            isGrounded = true;
        if (this.Xgo) {
            moveX();
            this.Xgo = false;
        }
        if(isGrounded && mover.stat.equals("jumping"))
            Animate.idle();
        totXoffset += xOffset;
        totYoffset += yOffset;
        this.x += xOffset;
        this.y += yOffset;
        mover.x = this.x;
        mover.y = this.y;
        centerX = this.x + hitBox.xOffset + 10 + xOffset;
        centerY = this.y + hitBox.yOffset + 20 + yOffset;
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
        setPos(x, y - Y_SPEED);
    }

    public void goX() {
        this.Xgo=true;
    }

    public void drawLineTo(Graphics page)
    {
        page.drawLine(this.x, this.y, 500, 500);
    }

    public void gravitate() {
        long deltaT = System.currentTimeMillis() - gStartTime;
        Y_SPEED = (int)(Y_CURRENT - (.02 * deltaT));
    }

    public void setX_SPEED(int speed) {
        X_SPEED = speed;
    }

    public void setY_SPEED(int speed) {
        Y_CURRENT = speed;
    }

    public void setY_MAX(int speed) {
        Y_MAX = speed;
    }

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
