import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;
import java.io.*;

public class EnhancedPlatformer extends JPanel implements ActionListener, KeyListener {
    // Game constants
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PLAYER_SIZE = 40;
    private static final int GRAVITY = 1;
    private static final int JUMP_STRENGTH = 15;
    private static final int MOVE_SPEED = 5;
    
    // Game objects
    private Player player;
    private ArrayList<Platform> platforms;
    private ArrayList<Enemy> enemies;
    private ArrayList<Coin> coins;
    private Goal goal;
    private int currentLevel = 1;
    private int score = 0;
    private boolean gameOver = false;
    private boolean levelComplete = false;
    
    // Images
    private Image playerImage, enemyImage, coinImage, goalImage, platformImage;
    
    // Sound
    private Clip jumpSound, coinSound, winSound, gameOverSound;
    
    public EnhancedPlatformer() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(135, 206, 235)); // Sky blue
        addKeyListener(this);
        setFocusable(true);
        
        // Load images
        loadImages();
        
        // Load sounds
        loadSounds();
        
        // Initialize game objects
        initLevel(currentLevel);
        
        // Game timer
        Timer timer = new Timer(16, this); // ~60 FPS
        timer.start();
    }
    
    private void loadImages() {
        try {
            // Simple colored rectangles - replace with actual images in a real game
            playerImage = createColoredImage(Color.RED, PLAYER_SIZE, PLAYER_SIZE);
            enemyImage = createColoredImage(Color.BLACK, PLAYER_SIZE, PLAYER_SIZE);
            coinImage = createColoredImage(Color.YELLOW, 20, 20);
            goalImage = createColoredImage(Color.MAGENTA, 40, 60);
            platformImage = createColoredImage(new Color(34, 139, 34), 100, 20); // Forest green
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Image createColoredImage(Color color, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        return image;
    }
    
    private void loadSounds() {
        try {
            // Simple beeps - replace with actual sound files in a real game
            jumpSound = AudioSystem.getClip();
            coinSound = AudioSystem.getClip();
            winSound = AudioSystem.getClip();
            gameOverSound = AudioSystem.getClip();
            
            // In a real game, you would load actual sound files:
            // jumpSound = AudioSystem.getClip();
            // jumpSound.open(AudioSystem.getAudioInputStream(new File("jump.wav")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void initLevel(int level) {
        player = new Player(100, 100, PLAYER_SIZE, PLAYER_SIZE);
        platforms = new ArrayList<>();
        enemies = new ArrayList<>();
        coins = new ArrayList<>();
        
        // Common ground
        platforms.add(new Platform(0, HEIGHT - 40, WIDTH, 40, platformImage));
        
        // Level-specific elements
        switch (level) {
            case 1:
                platforms.add(new Platform(200, 450, 100, 20, platformImage));
                platforms.add(new Platform(400, 350, 100, 20, platformImage));
                platforms.add(new Platform(600, 250, 100, 20, platformImage));
                
                enemies.add(new Enemy(300, HEIGHT - 40 - PLAYER_SIZE, PLAYER_SIZE, PLAYER_SIZE, enemyImage, 250, 400));
                enemies.add(new Enemy(500, 250 - PLAYER_SIZE, PLAYER_SIZE, PLAYER_SIZE, enemyImage, 450, 550));
                
                coins.add(new Coin(250, 400, 20, 20, coinImage));
                coins.add(new Coin(450, 300, 20, 20, coinImage));
                coins.add(new Coin(650, 200, 20, 20, coinImage));
                
                goal = new Goal(700, HEIGHT - 40 - 60, 40, 60, goalImage);
                break;
                
            case 2:
                platforms.add(new Platform(150, 500, 80, 20, platformImage));
                platforms.add(new Platform(300, 400, 80, 20, platformImage));
                platforms.add(new Platform(450, 300, 80, 20, platformImage));
                platforms.add(new Platform(600, 400, 80, 20, platformImage));
                
                enemies.add(new Enemy(200, 500 - PLAYER_SIZE, PLAYER_SIZE, PLAYER_SIZE, enemyImage, 150, 250));
                enemies.add(new Enemy(400, 300 - PLAYER_SIZE, PLAYER_SIZE, PLAYER_SIZE, enemyImage, 350, 450));
                enemies.add(new Enemy(550, 400 - PLAYER_SIZE, PLAYER_SIZE, PLAYER_SIZE, enemyImage, 500, 600));
                
                for (int i = 0; i < 8; i++) {
                    coins.add(new Coin(100 + i * 80, HEIGHT - 100, 20, 20, coinImage));
                }
                
                goal = new Goal(700, HEIGHT - 40 - 60, 40, 60, goalImage);
                break;
                
            case 3:
                // More challenging level
                platforms.add(new Platform(100, 500, 60, 20, platformImage));
                platforms.add(new Platform(250, 450, 60, 20, platformImage));
                platforms.add(new Platform(400, 400, 60, 20, platformImage));
                platforms.add(new Platform(550, 350, 60, 20, platformImage));
                platforms.add(new Platform(700, 300, 60, 20, platformImage));
                platforms.add(new Platform(400, 200, 60, 20, platformImage));
                
                enemies.add(new Enemy(150, 500 - PLAYER_SIZE, PLAYER_SIZE, PLAYER_SIZE, enemyImage, 100, 200));
                enemies.add(new Enemy(300, 450 - PLAYER_SIZE, PLAYER_SIZE, PLAYER_SIZE, enemyImage, 250, 350));
                enemies.add(new Enemy(450, 400 - PLAYER_SIZE, PLAYER_SIZE, PLAYER_SIZE, enemyImage, 400, 500));
                enemies.add(new Enemy(600, 350 - PLAYER_SIZE, PLAYER_SIZE, PLAYER_SIZE, enemyImage, 550, 650));
                enemies.add(new Enemy(750, 300 - PLAYER_SIZE, PLAYER_SIZE, PLAYER_SIZE, enemyImage, 700, 800));
                
                for (int i = 0; i < 15; i++) {
                    int x = 50 + i * 50;
                    int y = HEIGHT - 100 - (i % 3) * 50;
                    coins.add(new Coin(x, y, 20, 20, coinImage));
                }
                
                goal = new Goal(400, 140, 40, 60, goalImage);
                break;
        }
        
        levelComplete = false;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw all game objects
        for (Platform platform : platforms) {
            platform.draw(g);
        }
        
        for (Coin coin : coins) {
            coin.draw(g);
        }
        
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
        
        goal.draw(g);
        player.draw(g);
        
        // Draw UI
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 20, 30);
        g.drawString("Level: " + currentLevel, 20, 60);
        
        if (gameOver) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("GAME OVER", WIDTH/2 - 120, HEIGHT/2 - 30);
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString("Final Score: " + score, WIDTH/2 - 80, HEIGHT/2 + 20);
            g.drawString("Press R to Restart", WIDTH/2 - 100, HEIGHT/2 + 60);
        }
        
        if (levelComplete) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("LEVEL COMPLETE!", WIDTH/2 - 180, HEIGHT/2 - 30);
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString("Score: " + score, WIDTH/2 - 50, HEIGHT/2 + 20);
            
            if (currentLevel < 3) {
                g.drawString("Press N for Next Level", WIDTH/2 - 120, HEIGHT/2 + 60);
            } else {
                g.drawString("You've completed all levels!", WIDTH/2 - 180, HEIGHT/2 + 60);
                g.drawString("Press R to Restart", WIDTH/2 - 100, HEIGHT/2 + 100);
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !levelComplete) {
            // Update player
            player.update(platforms);
            
            // Update enemies
            for (Enemy enemy : enemies) {
                enemy.update();
                
                // Check enemy collision with player
                if (player.getBounds().intersects(enemy.getBounds())) {
                    gameOver = true;
                    playSound(gameOverSound);
                }
            }
            
            // Check coin collection
            for (int i = coins.size() - 1; i >= 0; i--) {
                Coin coin = coins.get(i);
                if (player.getBounds().intersects(coin.getBounds())) {
                    coins.remove(i);
                    score += 10;
                    playSound(coinSound);
                }
            }
            
            // Check goal reached
            if (player.getBounds().intersects(goal.getBounds())) {
                levelComplete = true;
                score += 100 * currentLevel;
                playSound(winSound);
            }
        }
        
        repaint();
    }
    
    private void playSound(Clip sound) {
        if (sound != null) {
            sound.setFramePosition(0);
            sound.start();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                // Restart game
                currentLevel = 1;
                score = 0;
                gameOver = false;
                initLevel(currentLevel);
            }
            return;
        }
        
        if (levelComplete) {
            if (e.getKeyCode() == KeyEvent.VK_N && currentLevel < 3) {
                // Next level
                currentLevel++;
                initLevel(currentLevel);
            } else if (e.getKeyCode() == KeyEvent.VK_R) {
                // Restart game
                currentLevel = 1;
                score = 0;
                levelComplete = false;
                initLevel(currentLevel);
            }
            return;
        }
        
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player.setMovingLeft(true);
                break;
            case KeyEvent.VK_RIGHT:
                player.setMovingRight(true);
                break;
            case KeyEvent.VK_SPACE:
                if (player.isOnGround()) {
                    player.jump();
                    playSound(jumpSound);
                }
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player.setMovingLeft(false);
                break;
            case KeyEvent.VK_RIGHT:
                player.setMovingRight(false);
                break;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    // Game object classes
    class GameObject {
        protected int x, y, width, height;
        protected Image image;
        
        public GameObject(int x, int y, int width, int height, Image image) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.image = image;
        }
        
        public Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }
        
        public void draw(Graphics g) {
            g.drawImage(image, x, y, width, height, null);
        }
    }
    
    class Player extends GameObject {
        private int velocityY = 0;
        private boolean isOnGround = false;
        private boolean movingLeft = false;
        private boolean movingRight = false;
        
        public Player(int x, int y, int width, int height) {
            super(x, y, width, height, playerImage);
        }
        
        public void update(ArrayList<Platform> platforms) {
            // Horizontal movement
            if (movingLeft) x -= MOVE_SPEED;
            if (movingRight) x += MOVE_SPEED;
            
            // Apply gravity
            velocityY += GRAVITY;
            y += velocityY;
            
            // Check collisions with platforms
            isOnGround = false;
            Rectangle playerBounds = getBounds();
            
            for (Platform platform : platforms) {
                if (playerBounds.intersects(platform.getBounds())) {
                    // Land on top of platform
                    if (velocityY > 0 && y + height <= platform.y + 10) {
                        y = platform.y - height;
                        velocityY = 0;
                        isOnGround = true;
                    }
                }
            }
            
            // Keep player in bounds
            if (x < 0) x = 0;
            if (x > WIDTH - width) x = WIDTH - width;
            if (y > HEIGHT - height) {
                y = HEIGHT - height;
                velocityY = 0;
                isOnGround = true;
            }
        }
        
        public void jump() {
            if (isOnGround) {
                velocityY = -JUMP_STRENGTH;
                isOnGround = false;
            }
        }
        
        public boolean isOnGround() {
            return isOnGround;
        }
        
        public void setMovingLeft(boolean movingLeft) {
            this.movingLeft = movingLeft;
        }
        
        public void setMovingRight(boolean movingRight) {
            this.movingRight = movingRight;
        }
    }
    
    class Platform extends GameObject {
        public Platform(int x, int y, int width, int height, Image image) {
            super(x, y, width, height, image);
        }
    }
    
    class Enemy extends GameObject {
        private int moveSpeed = 2;
        private int leftBound, rightBound;
        
        public Enemy(int x, int y, int width, int height, Image image, int leftBound, int rightBound) {
            super(x, y, width, height, image);
            this.leftBound = leftBound;
            this.rightBound = rightBound;
        }
        
        public void update() {
            x += moveSpeed;
            
            // Reverse direction at bounds
            if (x <= leftBound || x + width >= rightBound) {
                moveSpeed *= -1;
            }
        }
    }
    
    class Coin extends GameObject {
        public Coin(int x, int y, int width, int height, Image image) {
            super(x, y, width, height, image);
        }
    }
    
    class Goal extends GameObject {
        public Goal(int x, int y, int width, int height, Image image) {
            super(x, y, width, height, image);
        }
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Enhanced Platformer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new EnhancedPlatformer());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
