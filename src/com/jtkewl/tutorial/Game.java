package com.jtkewl.tutorial;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.text.NumberFormat;
import java.util.LinkedList;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;

    public static int WIDTH = 1280;
    public static int HEIGHT = WIDTH / 16 * 9;
    public static final int SCALE = 1;
    public static final String NAME = "Game";

    private JFrame frame;

    public boolean running = false;
    public int tickCount = 0;
    private boolean didTots = false;
    public Character player;
    public WorldTerrain[] world = new WorldTerrain[4];
    public LinkedList Enemies = new LinkedList();
    StandardEnemy[] tumpa;
    public StatusBar health;
    public StatusBar energy;
    public Controller input = new Controller();
    private NumberFormat form = NumberFormat.getNumberInstance();
    public boolean pausing = false;
    long newTime = 0;

    public Listener listen;

    public Game() {
        setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        frame = new JFrame(NAME);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(this, BorderLayout.CENTER);
        frame.pack();

        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public synchronized void start() {
        running = true;
        WorldTerrain set2 = new WorldTerrain(0, 800, 750, 400);
        WorldTerrain set1 = new WorldTerrain(0, 400, 300, 1080);
        WorldTerrain set3 = new WorldTerrain(750, 400, 300, 1080);
        WorldTerrain set4 = new WorldTerrain(600, 400, 50, 300);
        world[0] = set3;
        world[1] = set2;
        world[2] = set1;
        world[3] = set4;
        StandardEnemy en1 = new StandardEnemy(-1000, 2500, 26, 40, false, 200);
        Enemies.add(en1);
        tumpa = new StandardEnemy[Enemies.size()];
        Enemies.toArray(tumpa);
        form.setMaximumFractionDigits(8);
        form.setMinimumFractionDigits(8);
        player = new Character(WIDTH / 2, HEIGHT / 2, 26, 40, true, 200);
        listen = new Listener(this);
        health = new StatusBar(5, HEIGHT - 100, (player.getHealth() / 10), (int) (WIDTH * .25), 25, "/theBar.png");
        energy = new StatusBar(5, HEIGHT - 75, 10, (int) (WIDTH * .25), 25, "/theEnd.png");
        new Thread(this).start();
    }

    public synchronized void stop() {
        running = false;
        System.exit(0);
        //frame.removeAll();
    }

    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 100000000D / 10D;       //double nsPerTick = 1000000000D / 60D;

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = false;

            while (delta >= 1) {
                if (pausing) {
                    pauseMenu();
                    render();
                } else {
                    ticks++;
                    frames++;
                    tick();
                    delta--;
                    shouldRender = true;
                }
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (shouldRender) {
                frames++;
                render();
            }

            if (System.currentTimeMillis() - lastTimer > +1000) {
                lastTimer += 1000;
                frames = 0;
                ticks = 0;
            }
        }
    }

    int red = 255, gre = 0, blu = 0;

    //------------------------------------------------------------------------------------------------------------------||
    //------------------------------------------------------------------------------------------------------------------||
    //                      TICK FUNCTION                                                                               ||
    //------------------------------------------------------------------------------------------------------------------||
    //------------------------------------------------------------------------------------------------------------------||
    public void tick() {
        tickCount++;

        changeColour();

        if (WIDTH != frame.getWidth() || HEIGHT != frame.getHeight()) {
            WIDTH = frame.getWidth();
            HEIGHT = frame.getHeight();

            health.update(5, HEIGHT - 100, (int) (WIDTH * .25), 25);
            energy.update(5, HEIGHT - 70, (int) (WIDTH * .25), 25);
        }

        boolean gReady = true;
        boolean hitWall = false;
        boolean callToWall = false;
        boolean rightWall = false;
        for (WorldTerrain aWorld : world) {
            aWorld.hitBox.updateTopBlock(player.getgForce());
            worldCollision(player, aWorld);

            if (player.gravBox.box.intersects(aWorld.hitBox.top)) {
                gReady = false;
                callToWall = true;
            }

            if (player.hitBox.box.intersects(aWorld.hitBox.right)) {
                hitWall = true;
                rightWall = true;
            }
            if (player.hitBox.box.intersects(aWorld.hitBox.left)) {
                hitWall = true;
                rightWall = false;
            }
        }
        if (!gReady) {
            player.setGravity(false);
            if (player.mover.stat.equals("jumped")) player.Animate.idle();
        } else
            player.setGravity(true);

        player.setGrounded(callToWall);
        if (callToWall) {
            newTime = System.currentTimeMillis();
        }

        if (hitWall && (System.currentTimeMillis() - newTime) >= 300 && !rightWall && (listen.right.Pressed() || listen.space.Pressed())) {
            player.setGMax(-2);
        } else if (hitWall && (System.currentTimeMillis() - newTime) >= 300 && rightWall && (listen.left.Pressed() || listen.space.Pressed())) {
            player.setGMax(-2);
        } else {
            player.setGMax(-1);
        }

        input.Control();

        player.update();
        for(StandardEnemy anEnemy : tumpa){
            anEnemy.update(xOffset / 2, yOffset / 2);
        }
    }
    //------------------------------------------------------------------------------------------------------------------||
    //                                  END OF TICK
    //------------------------------------------------------------------------------------------------------------------||

    /*private int leftX;
    private int rightX;*/
    private long camlonging;

    public String NOTIFICATION = "";

    /*public boolean gravityChecker(Character person, WorldTerrain floor) {
        Rectangle prime = person.hitBox.box;
        Rectangle gPrime = person.gravBox.box;
        Terrain sect = floor.hitBox;
        if (prime.intersects(sect.top) || gPrime.intersects(sect.top)) {
            return (true);
        } else
            return (false);
    }*/

    public void worldCollision(Character person, WorldTerrain floor) {
        Rectangle prime = person.hitBox.box;
        Rectangle gPrime = person.gravBox.box;
        Rectangle tPrime = person.topBox.box;
        Terrain sect = floor.hitBox;

        int leftX = (sect.x) - person.hitBox.xOffset - (int) prime.getWidth();
        int rightX = (sect.x + (int) sect.box.getWidth()) - person.hitBox.xOffset;

        if (prime.intersects(sect.bottom) && tPrime.intersects(sect.bottom)) {
            person.setPos(person.x, sect.y + (int) sect.box.getHeight() - person.hitBox.yOffset);
            person.setY_SPEED(0);
        }
        if (prime.intersects(sect.left) && !tPrime.intersects(sect.bottom) && !gPrime.intersects(sect.top)) {
            person.setPos(leftX, person.y);
            if (listen.right.Pressed() || listen.rightSpace.Pressed())
                person.setX_SPEED(0);
            camX = false;
            camlonging = System.currentTimeMillis();
            xOffset = 0;
            yOffset = 0;
        } else {
            if (System.currentTimeMillis() - camlonging >= 100) {
                camX = true;
            }
        }
        if (prime.intersects(sect.top) || gPrime.intersects(sect.top)) {
            person.setPos(person.x, (sect.y) - person.hitBox.yOffset - (int) prime.getHeight() - 1);
            person.setY_SPEED(0);
            person.setGrounded(true);
            person.resetGravity();
        }
        if (prime.intersects(sect.right) && !tPrime.intersects(sect.bottom) && !gPrime.intersects(sect.top)) {
            person.setPos(rightX, person.y);
            if (listen.left.Pressed())
                person.setX_SPEED(0);
            camX = false;
            camlonging = System.currentTimeMillis();
            xOffset = 0;
            yOffset = 0;
        } else {
            if (System.currentTimeMillis() - camlonging >= 50) {
                camX = true;
            }
        }
    }

    public boolean camX = true;

    public void Camera() {
        originX = WIDTH/2;
        originY = HEIGHT/2;
        if (camX) {
            if (player.centerX > originX+ 1 || player.centerX < originX - 1) {
                xOffset = originX - player.centerX;
                player.x += xOffset;
                xTots += xOffset;
                if (didTots) {
                    xTots = 0;
                    didTots = false;
                }
            } else
                xOffset = 0;
        }
        if (player.centerY > originY+1 || player.centerY < originY-1) {
            yOffset = originY - player.centerY;
            yTots -= yOffset;
            if (didTots) {
                yTots = 0;
                didTots = false;
            }
        } else
            yOffset = 0;
    }

    private int originX;
    private int originY;
    private int xOffset = WIDTH / 2;
    private int yOffset;
    private int xTots = xOffset;
    private int yTots = yOffset;

    BufferStrategy bs;

    Graphics g;

    public void render() {

        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();

        if (pausing) {
            g.setColor(new Color(100, 100, 100, 100));
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.RED);
            for (int i = 0; i < menuPoints.size(); i++) {
                g.drawString(menuPoints.get(i).toString(), 50, (HEIGHT / 2) + (i * 15) - (menuPoints.size() / 2 * 15));
            }
            g.fillRect(40, (HEIGHT / 2) + (item * 15) - 27 - (menuPoints.size() / 2 * 15), 8, 15);
        } else {
            Camera();

            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, WIDTH * WIDTH, HEIGHT * HEIGHT);

            for (WorldTerrain bWorld : world) bWorld.update(xOffset, yOffset);
            for (WorldTerrain aWorld : world) {
                aWorld.drawTerrain((Graphics2D) g);
                aWorld.hitBox.drawit((Graphics2D) g, Color.ORANGE);
            }
            player.Animate.Make((Graphics2D) g, xOffset, yOffset);
            player.hitBox.drawit(g, Color.GREEN);
            player.gravBox.drawit(g, Color.RED);
            player.topBox.drawit(g, Color.RED);

            g.setColor(Color.green);
            g.setColor(Color.red);
            health.drawIt((Graphics2D) g);
            energy.drawIt((Graphics2D) g);
            g.setColor(Color.MAGENTA);
            g.fillRect(originX - 3, originY - 3, 6, 6);

            g.setColor(new Color(50, 50, 50, 230));
            g.fillRect(40, 30, 200, 200);
            g.setColor(new Color(red, gre, blu));
            g.drawRect(40, 30, 200, 200);
            g.drawRect(41, 31, 198, 198);
            g.drawRect(42, 32, 196, 196);
            g.drawRect(43, 33, 194, 194);

            g.setColor(Color.red);
            String[] Maintenant = player.toStringArray();
            for (int j = 0; j < Maintenant.length; j++)
                g.drawString(Maintenant[j], 50, 50 + (15 * j));
            g.drawString("SpacePressed? " + listen.space.Pressed(), 50, 50 + (15 * Maintenant.length));
            g.drawString(" SpaceDowned? " + listen.space.Downed(), 50, 65 + (15 * Maintenant.length));
            g.drawString(NOTIFICATION, 50, 95 + (15 * Maintenant.length));

            String[] ControlStatus = this.input.toStringArray();
            for (int j = 0; j < ControlStatus.length; j++)
                g.drawString(ControlStatus[j], 50, 110 + (15 * Maintenant.length) + (15 * j));

            for (StandardEnemy anEnemy : tumpa) {
                anEnemy.Animate.Make((Graphics2D) g, 0, 0);
                anEnemy.hitBox.drawit(g, Color.RED);
                anEnemy.drawLineTo(g);
                g.drawString("" + anEnemy.x + ", " + anEnemy.y, 600, 50);
            }
        }
        g.dispose();
        bs.show();
    }

    public void changeColour() {
        if (red >= 255) {
            red = 255;
            gre++;
            blu--;
        }
        if (gre >= 255) {
            gre = 255;
            blu++;
            red--;
        }
        if (blu >= 255) {
            blu = 255;
            red++;
            gre--;
        }
        if (red <= 0) red = 0;
        if (gre <= 0) gre = 0;
        if (blu <= 0) blu = 0;
    }

    long pauseWait = 0;
    int item = 1;
    int maxItem = 3;
    int minItem = 1;
    String menuName = "Testing";
    LinkedList menuPoints = new LinkedList();
    LinkedList SelectFuncts = new LinkedList();

    public void pauseMenu() {
        input.menuControl();

        //if(menuPoints != null) {
        switch (menuName) {
            case "Home":
                menuPoints.clear();
                SelectFuncts.clear();
                /*if(!same.equals("Home")
                    item = 0;*/
                menuPoints.add(0, "Resume");
                SelectFuncts.add(0, 1);
                menuPoints.add(1, "Options");
                SelectFuncts.add(1, 3);
                menuPoints.add(2, "Quit");
                SelectFuncts.add(2, 0);
                maxItem = 3;
                minItem = 1;
                break;
            case "Options":
                menuPoints.clear();
                SelectFuncts.clear();
                menuPoints.add(0, "Debug mode");
                SelectFuncts.add(0, 4);
                menuPoints.add(1, "Back");
                SelectFuncts.add(1, 2);
                maxItem = 2;
                minItem = 1;
                break;
            case "Testing":
                menuPoints.clear();
                SelectFuncts.clear();
                menuPoints.add(0, "Chu 1");
                SelectFuncts.add(0, 2);
                menuPoints.add(1, "Hey 2");
                SelectFuncts.add(1, 2);
                menuPoints.add(2, "Chu-Hey 3");
                SelectFuncts.add(2, 2);
                menuPoints.add(3, "Chu 4");
                SelectFuncts.add(3, 2);
                menuPoints.add(4, "Hey 5");
                SelectFuncts.add(4, 2);
                menuPoints.add(5, "Chu-Hey 6");
                SelectFuncts.add(5, 2);
                menuPoints.add(6, "Chu 7");
                SelectFuncts.add(6, 2);
                menuPoints.add(7, "Hey 8");
                SelectFuncts.add(7, 2);
                menuPoints.add(8, "Chu-Hey 9");
                SelectFuncts.add(8, 2);
                maxItem = 9;
                minItem = 1;
                break;
        }
        //}

        if (item < minItem)
            item = maxItem;
        if (item > maxItem)
            item = minItem;

        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    //---------------------------------------------------------------------------------------------
    //      PAUSE MENU FUNCTIONS
    //          0. QUIT
    //          1. RESUME
    //          2. BACK
    //          3. OPTIONS
    //          4. TOGGLE DEBUG
    //---------------------------------------------------------------------------------------------

    public void Selection() {
        int choice = (int) SelectFuncts.get(item - 1);
        System.out.println(choice);
        switch (choice) {
            case 0:
                stop();
                break;
            case 1:
                pausing = false;
                break;
            case 2:
                menuName = "Home";
                break;
            case 3:
                menuName = "Options";
                break;
            case 4:
                break;
        }
    }

    public static void main(String[] args) {
        new Game().start();
    }


    public class Controller {
        boolean active;

        public Controller() {
            this.active = true;
        }

        public boolean rls = false, rl = false, rs = false, ls = false, r = false, l = false, s = false;

        public void Control() {
            if (listen.pause.Downed()) {
                if (System.currentTimeMillis() - pauseWait >= 500) {
                    pauseWait = System.currentTimeMillis();
                    pausing = true;
                    g.dispose();
                }
            }
            if (listen.right.Pressed() && listen.left.Pressed() && listen.space.Pressed()) {
                rls = true;
            } else
                rls = false;
            if (listen.right.Pressed() && listen.left.Pressed() && !rls) {
                rl = true;
                if (!player.mover.stat.equals("Rainbows")) player.Animate.rainbows();
            } else
                rl = false;
            if (listen.right.Pressed() && listen.space.Pressed() && !rls) {
                rs = true;
                if (player.isGrounded()) {
                    player.setY_SPEED(player.Y_MAX);
                    if (listen.space.Downed())
                        player.setGrounded(false);
                }
                player.setDirection(true);
                player.setX_SPEED(player.X_MAX);
                player.goX();
                if (!player.mover.stat.equals("jumping")) player.Animate.jumped();
            } else
                rs = false;
            if (listen.left.Pressed() && listen.space.Pressed() && !rls) {
                ls = true;
                NOTIFICATION = "Left Space pressed!";
                if (player.isGrounded()) {
                    player.setY_SPEED(player.Y_MAX);
                    if (listen.space.Downed())
                        player.setGrounded(false);
                }
                if (!player.mover.stat.equals("jumping")) player.Animate.jumped();
                player.setDirection(false);
                player.setX_SPEED(player.X_MAX);
                player.goX();
            } else
                ls = false;
            if (listen.right.Pressed() && !rls && !rl && !rs) {
                r = true;
                player.setDirection(true);
                player.setX_SPEED(player.X_MAX);
                player.goX();
                if (!player.mover.stat.equals("runningRight")) {
                    player.Animate.runnedRight();
                }
            } else
                r = false;
            if (listen.left.Pressed() && !rls && !rl && !ls) {
                l = true;
                player.setDirection(false);
                player.setX_SPEED(player.X_MAX);
                player.goX();
                if (!player.mover.stat.equals("runningLeft")) {
                    player.Animate.runnedLeft();
                }
            } else
                l = false;
            if (listen.space.Pressed() && !rls && !rs && !ls) {
                s = true;
                if (player.isGrounded()) {
                    player.setY_SPEED(player.Y_MAX);
                    if (listen.space.Downed())
                        player.setGrounded(false);
                }
                if (!player.mover.stat.equals("jumping")) player.Animate.jumped();
            } else
                s = false;
            if (listen.reset.Pressed()) {
                player.Animate.reset();
            }
            if (!r && !l && !rs && !ls) {
                player.setX_SPEED(0);
            }
            if (!listen.getAll.Pressed() && (player.mover.stat.equals("done") || player.mover.stat.equals("runningRight") || player.mover.stat.equals("runningLeft"))) {
                player.Animate.idle();
            }
            listen.update();
        }

        private boolean menSwitchU = true;
        private boolean menSwitchD = true;

        public void menuControl() {
            if (listen.pause.Downed()) {
                if (System.currentTimeMillis() - pauseWait >= 500) {
                    pauseWait = System.currentTimeMillis();
                    pausing = false;
                }
            }
            if (listen.select.Downed()) {
                Selection();
            }
            if (listen.right.Pressed()) {
                r = true;
            } else
                r = false;
            if (listen.left.Pressed()) {
                l = true;
            } else
                l = false;
            if (listen.up.Downed() && menSwitchU) {
                item--;
                menSwitchU = false;
            }
            if (!listen.up.Downed())
                menSwitchU = true;
            if (listen.down.Downed() && menSwitchD) {
                item++;
                menSwitchD = false;
            }
            if (!listen.down.Downed())
                menSwitchD = true;
            if (listen.space.Pressed()) {
                s = true;
            } else
                s = false;
            if (listen.reset.Pressed()) {
            }
            listen.update();
        }

        public String[] toStringArray() {
            return new String[]{
                    "r - " + r + "    l - " + l + "    s - " + s,
                    "rl - " + rl + "   rs - " + rs + "   ls - " + ls,
                    "rls - " + rls
            };
        }
    }
}
