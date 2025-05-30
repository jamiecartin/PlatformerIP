package EnhancedIP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.sound.sampled.*;
import java.io.File;

public class EnhancedPlatformer extends JPanel implements ActionListener, KeyListener {
    // Game constants
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PLAYER_SIZE = 32;
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
        
        // Load resources
        loadImages();
        loadSounds();
        
        // Initialize game objects
        initLevel(currentLevel);
        
        // Game timer
        Timer timer = new Timer(16, this); // ~60 FPS
        timer.start();
    }
    
    private void loadImages() {
        try {
            // Load sprite images (replace with your actual files)
            playerImage = ImageIO.read(getClass().getResource("/sprites/player.png"));
            enemyImage = ImageIO.read(getClass().getResource("/sprites/enemy.png"));
            coinImage = ImageIO.read(getClass().getResource("/sprites/coin.png"));
            goalImage = ImageIO.read(getClass().getResource("/sprites/goal.png"));
            platformImage = ImageIO.read(getClass().getResource("/sprites/platform.png"));
        } catch (IOException e) {
            e.printStackTrace();
            // Fallback to colored rectangles
            playerImage = createColoredImage(Color.RED, PLAYER_SIZE, PLAYER_SIZE);
            enemyImage = createColoredImage(Color.BLACK, PLAYER_SIZE, PLAYER_SIZE);
            coinImage = createColoredImage(Color.YELLOW, 20, 20);
            goalImage = createColoredImage(Color.MAGENTA, 40, 60);
            platformImage = createColoredImage(new Color(34, 139, 34), 100, 20);
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
            // Load sound files (replace with your actual files)
            jumpSound = AudioSystem.getClip();
            jumpSound.open(AudioSystem.getAudioInputStream(getClass().getResource("/sounds/jump.wav")));
            
            coinSound = AudioSystem.getClip();
            coinSound.open(AudioSystem.getAudioInputStream(getClass().getResource("/sounds/coin.wav")));
            
            winSound = AudioSystem.getClip();
            winSound.open(AudioSystem.getAudioInputStream(getClass().getResource("/sounds/win.wav")));
            
            gameOverSound = AudioSystem.getClip();
            gameOverSound.open(AudioSystem.getAudioInputStream(getClass().getResource("/sounds/gameover.wav")));
        } catch (Exception e) {
            System.err.println("Error loading sounds: " + e.getMessage());
        }
    }
    
    private void initLevel(int level) {
        // Create player
        player = new Player(100, 100, PLAYER_SIZE, PLAYER_SIZE, playerImage);
        
        // Initialize object lists
        platforms = new ArrayList<>();
        enemies = new ArrayList<>();
        coins = new ArrayList<>();
        
        // Common ground platform
        platforms.add(new Platform(0, HEIGHT - 40, WIDTH, 40, platformImage));
        
        // Level-specific design
        switch (level) {
            case 1:
                // Platforms
                platforms.add(new Platform(200, 450, 100, 20, platformImage));
                platforms.add(new Platform(400, 350, 100, 20, platformImage));
                platforms.add(new Platform(600, 250, 100, 20, platformImage));
                
                // Enemies
                enemies.add(new Enemy(300, HEIGHT - 40 - PLAYER_SIZE, PLAYER_SIZE, PLAYER_SIZE, enemyImage, 250, 400));
                enemies.add(new Enemy(500, 250 - PLAYER_SIZE, PLAYER_SIZE, PLAYER_SIZE, enemyImage, 450, 550));
                
                // Coins
                coins.add(new Coin(250, 400, 20, 20, coinImage));
                coins.add(new Coin(450, 300, 20, 20, coinImage));
                coins.add(new Coin(650, 200, 20, 20, coinImage));
                
                // Goal
                goal = new Goal(700, HEIGHT - 40 - 60, 40, 60, goalImage);
                break;
                
            case 2:
                // More challenging level...
                break;
                
            case 3:
                // Even more challenging level...
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
        
        // Game over/level complete screens
        if (gameOver) {
            drawCenteredScreen(g, "GAME OVER", "Final Score: " + score, "Press R to Restart");
        } else if (levelComplete) {
            if (currentLevel < 3) {
                drawCenteredScreen(g, "LEVEL COMPLETE!", "Score: " + score, "Press N for Next Level");
            } else {
                drawCenteredScreen(g, "YOU WIN!", "Final Score: " + score, "Press R to Restart");
            }
        }
    }
    
    private void drawCenteredScreen(Graphics g, String title, String subtitle, String instruction) {
        Graphics2D g2d = (Graphics2D) g;
        
        // Semi-transparent overlay
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        
        // Title
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2d.drawString(title, WIDTH/2 - titleWidth/2, HEIGHT/2 - 30);
        
        // Subtitle
        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
        fm = g2d.getFontMetrics();
        int subWidth = fm.stringWidth(subtitle);
        g2d.drawString(subtitle, WIDTH/2 - subWidth/2, HEIGHT/2 + 20);
        
        // Instruction
        int instWidth = fm.stringWidth(instruction);
        g2d.drawString(instruction, WIDTH/2 - instWidth/2, HEIGHT/2 + 60);
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
                if (!coin.isCollected() && player.getBounds().intersects(coin.getBounds())) {
                    coin.collect();
                    coins.remove(i);
                    score += coin.getValue();
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
