package com.company.Game;

import com.company.Game.GameObjects.Apple;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import static com.company.Game.Settings.*;

public class GameField extends JPanel implements ActionListener {
    private final Apple apple;

    private Image snakeDotImage;

    private int[] snakeX = new int[ALL_DOTS];
    private int[] snakeY = new int[ALL_DOTS];

    private int snakeSize;

    private Timer timer;

    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean isInGame = true;

    public GameField() {
        apple = new Apple(new ImageIcon(SNAKE_DOT_IMAGE).getImage());

        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new FieldKeyListener());

        loadImages();
        initGame();
    }

    public void initGame() {
        snakeSize = 3;

        for (int i = 0; i < snakeSize; i++) {
            snakeX[i] = 48 - i * DOT_SIZE;
            snakeY[i] = 48;
        }

        timer = new Timer(250, this);
        timer.start();

        createApple();
    }

    public void createApple() {
        appleX = new Random().nextInt(WINDOW_SIZE / DOT_SIZE) * DOT_SIZE;
        appleY = new Random().nextInt(WINDOW_SIZE / DOT_SIZE) * DOT_SIZE;
    }

    public void loadImages() {
        snakeDotImage = new ImageIcon(SNAKE_DOT_IMAGE).getImage();
    }

    @Override
    protected void paintComponent(Graphics gr) {
        super.paintComponent(gr);

        if (isInGame) {
            gr.drawImage(appleImage, appleX, appleY, this);

            for (int i = 0; i < snakeSize; i++) {
                gr.drawImage(snakeDotImage, snakeX[i], snakeY[i], this);
            }
        } else {
            String str = "Game Over";
            //Font font = new Font("Arial", 14, Font.BOLD);
            gr.setColor(Color.WHITE);
            //gr.setFont(font);
            gr.drawString(str, 120, WINDOW_SIZE / 2);
        }
    }

    public void move() {
        for (int i = snakeSize; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        if (left) {
            snakeX[0] -= DOT_SIZE;
        } else if (right) {
            snakeX[0] += DOT_SIZE;
        } else if (up) {
            snakeY[0] -= DOT_SIZE;
        } else if (down) {
            snakeY[0] += DOT_SIZE;
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (isInGame) {
            checkApple();
            checkCollisions();
            move();
        }

        repaint();
    }

    public void checkApple() {
        if (snakeX[0] == appleX && snakeY[0] == appleY) {
            snakeSize++;
            createApple();
        }

    }

    public void checkCollisions() {
        for (int i = snakeSize; i > 0; i--) {
            if (i > 4 && snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                isInGame = false;
            }
        }

        if (snakeX[0] > WINDOW_SIZE) {
            isInGame = false;
        }
        if (snakeX[0] < 0) {
            isInGame = false;
        }
        if (snakeY[0] > WINDOW_SIZE) {
            isInGame = false;
        }
        if (snakeY[0] < 0) {
            isInGame = false;
        }
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent ev) {
            super.keyPressed(ev);
            int key = ev.getKeyCode();

            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            } else if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            } else if (key == KeyEvent.VK_UP && !down) {
                up = true;
                left = false;
                right = false;
            } else if (key == KeyEvent.VK_DOWN && !up) {
                down = true;
                left = false;
                right = false;
            }
        }
    }
}
