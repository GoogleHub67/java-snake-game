import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JPanel implements ActionListener {

    private final int TILE_SIZE = 25;
    private final int GRID_SIZE = 20; // 20x20 grid
    private final int BOARD_SIZE = TILE_SIZE * GRID_SIZE; // 500x500 pixels

    // Game state variables
    private final ArrayList<Point> snake = new ArrayList<>();
    private Point apple;
    private char direction = 'R'; // U, D, L, R
    private boolean running = false;
    private int score = 0;
    private Timer timer;

    public Game() {
        this.setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        snake.clear();
        // Start snake with 3 segments in the middle
        snake.add(new Point(5, 10));
        snake.add(new Point(4, 10));
        snake.add(new Point(3, 10));
        
        direction = 'R';
        score = 0;
        spawnApple();
        running = true;

        // Game loop ticks every 130 milliseconds
        if (timer != null) timer.stop();
        timer = new Timer(130, this);
        timer.start();
    }

    public void spawnApple() {
        Random random = new Random();
        int x, y;
        boolean onSnake;
        do {
            onSnake = false;
            x = random.nextInt(GRID_SIZE);
            y = random.nextInt(GRID_SIZE);
            // Ensure apple doesn't spawn on the snake's body
            for (Point p : snake) {
                if (p.x == x && p.y == y) {
                    onSnake = true;
                    break;
                }
            }
        } while (onSnake);
        apple = new Point(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // Draw Apple
            g.setColor(Color.RED);
            g.fillOval(apple.x * TILE_SIZE, apple.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            // Draw Snake
            for (int i = 0; i < snake.size(); i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN); // Head
                } else {
                    g.setColor(new Color(45, 180, 0)); // Body
                }
                Point p = snake.get(i);
                g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1);
            }

            // Draw Score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 15, 30);
        } else {
            gameOver(g);
        }
    }

    public void move() {
        // Create new head based on current direction
        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);

        switch (direction) {
            case 'U' -> newHead.y--;
            case 'D' -> newHead.y++;
            case 'L' -> newHead.x--;
            case 'R' -> newHead.x++;
        }

        // Insert new head at the front
        snake.add(0, newHead);

        // Check if snake ate the apple
        if (newHead.x == apple.x && newHead.y == apple.y) {
            score++;
            spawnApple();
        } else {
            // Remove the tail segment if it didn't grow
            snake.remove(snake.size() - 1);
        }
    }

    public void checkCollisions() {
        Point head = snake.get(0);

        // Check wall collisions
        if (head.x < 0 || head.x >= GRID_SIZE || head.y < 0 || head.y >= GRID_SIZE) {
            running = false;
        }

        // Check self collisions
        for (int i = 1; i < snake.size(); i++) {
            if (head.x == snake.get(i).x && head.y == snake.get(i).y) {
                running = false;
                break;
            }
        }

        if (!running) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollisions();
        }
        repaint();
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (BOARD_SIZE - metrics.stringWidth("Game Over")) / 2, BOARD_SIZE / 2 - 20);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Final Score: " + score, (BOARD_SIZE - metrics2.stringWidth("Final Score: " + score)) / 2, BOARD_SIZE / 2 + 20);
        g.drawString("Press SPACE to Restart", (BOARD_SIZE - metrics2.stringWidth("Press SPACE to Restart")) / 2, BOARD_SIZE / 2 + 60);
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') direction = 'D';
                    break;
                case KeyEvent.VK_SPACE:
                    if (!running) startGame();
                    break;
            }
        }
    }

    // Main entry point bootstrapping the Swing window
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pure Java Snake Game");
        Game gamePanel = new Game();
        
        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center window on screen
        frame.setVisible(true);
    }
}
