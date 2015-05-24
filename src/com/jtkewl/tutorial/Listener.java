package com.jtkewl.tutorial;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Listener implements KeyListener {

    public Listener(Game game) {
        game.addKeyListener(this);
        this.game = game;
    }

    private Game game;

    public class Key {
        private boolean isPressed = false;
        private boolean isDowned = false;
        public long downStart;
        public boolean firstDown = false;

        public boolean Pressed() {
            return (isPressed);
        }

        public boolean ftDowned() {
            return (isDowned);
        }

        public void toggle(boolean toDo) {
            isPressed = toDo;
        }

        public void reToggle(boolean toDo) {
            isDowned = toDo;
        }
    }

    public Key space = new Key();
    public Key pause = new Key();
    public Key select = new Key();
    public Key up = new Key();
    public Key down = new Key();
    public Key left = new Key();
    public Key right = new Key();
    public Key reset = new Key();
    public Key getAll = new Key();
    public Key rightSpace = new Key();
    public Key[] EVERY = new Key[]{space, pause, select, up, down, left, right, reset, getAll, rightSpace};

    public void keyPressed(KeyEvent e) {
        toggleKey(e.getKeyCode(), true);

        Key hit = null;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                hit = space;
                break;
            case KeyEvent.VK_ESCAPE:
                hit = pause;
                break;
            case KeyEvent.VK_ENTER:
                hit = select;
                break;
            case KeyEvent.VK_W:
                hit = up;
                break;
            case KeyEvent.VK_S:
                hit = down;
                break;
            case KeyEvent.VK_A:
                hit = left;
                break;
            case KeyEvent.VK_D:
                hit = right;
                break;
            case KeyEvent.VK_Q:
                hit = reset;
                break;
        }

        if (hit != null) {
            if (!hit.firstDown) {
                hit.firstDown = true;
                hit.reToggle(true);
            }
        }
    }

    public void update() {
        for (int i = 0; i < EVERY.length; i++) {
            if (System.currentTimeMillis() - EVERY[i].downStart >= 10) {
                EVERY[i].reToggle(false);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        toggleKey(e.getKeyCode(), false);
        Key hit = null;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                hit = space;
                break;
            case KeyEvent.VK_ESCAPE:
                hit = pause;
                break;
            case KeyEvent.VK_ENTER:
                hit = select;
                break;
            case KeyEvent.VK_W:
                hit = up;
                break;
            case KeyEvent.VK_S:
                hit = down;
                break;
            case KeyEvent.VK_A:
                hit = left;
                break;
            case KeyEvent.VK_D:
                hit = right;
                break;
            case KeyEvent.VK_Q:
                hit = reset;
                break;
        }
        if (hit != null) {
            hit.firstDown = false;
            hit.reToggle(false);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void toggleKey(int keyCode, boolean isPressed) {
        if (keyCode == KeyEvent.VK_SPACE) {
            space.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_ESCAPE) {
            pause.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_ENTER) {
            select.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_W) {
            up.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_S) {
            down.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_A) {
            left.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_D) {
            right.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_Q) {
            reset.toggle(isPressed);
        }

        getAll.toggle(isPressed);
    }

    public void reToggleKey(int keyCode) {
        if (keyCode == KeyEvent.VK_SPACE) {
            space.reToggle(true);
            space.downStart = System.currentTimeMillis();
        }
        if (keyCode == KeyEvent.VK_ESCAPE) {
            pause.reToggle(true);
            pause.downStart = System.currentTimeMillis();
        }
        if (keyCode == KeyEvent.VK_ENTER) {
            select.reToggle(true);
            select.downStart = System.currentTimeMillis();
        }
        if (keyCode == KeyEvent.VK_W) {
            up.reToggle(true);
            up.downStart = System.currentTimeMillis();
        }
        if (keyCode == KeyEvent.VK_S) {
            down.reToggle(true);
            down.downStart = System.currentTimeMillis();
        }
        if (keyCode == KeyEvent.VK_A) {
            left.reToggle(true);
            left.downStart = System.currentTimeMillis();
        }
        if (keyCode == KeyEvent.VK_D) {
            right.reToggle(true);
            right.downStart = System.currentTimeMillis();
        }
        if (keyCode == KeyEvent.VK_Q) {
            reset.reToggle(true);
            reset.downStart = System.currentTimeMillis();
        }
    }
}
