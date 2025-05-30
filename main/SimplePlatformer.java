import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SimplePlatformer extends JPanel implements ActionListener, KeyListener {
    // Game constants
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int GROUND = 500;
    private static final int PLAYER_SIZE = 30;
    private static final int GRAVITY = 1;
    private static final int JUMP_STRENGTH = 15;
    
    // Player variables
    private int playerX = 100;
    private int playerY = GROUND - PLAYER_SIZE;
    private int playerVelocityY = 0;
    private boolean isJumping = false;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    
    // Platforms
    private ArrayList<Rectangle> platforms;
    
    public SimplePlatformer() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.CYAN);
        addKeyListener(this);
        setFocusable(true);
        
        // Initialize platforms
        platforms = new ArrayList<>();
        platforms.add(new Rectangle(0, GROUND, WIDTH, 20)); // Ground
        platforms.add(new Rectangle(200, 400, 100, 20));    // Platform 1
        platforms.add(new Rectangle(400, 300, 100, 20));    // Platform 2
        platforms.add(new Rectangle(600, 200, 100, 20));    // Platform 3
        
        // Game timer
        Timer timer = new Timer(16, this); // ~60 FPS
        timer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw platforms
        g.setColor(Color.GREEN);
        for (Rectangle platform : platforms) {
            g.fillRect(platform.x, platform.y, platform.width, platform.height);
        }
        
        // Draw player
        g.setColor(Color.RED);
        g.fillRect(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);
        
        // Draw instructions
        g.setColor(Color.BLACK);
        g.drawString("Use LEFT/RIGHT arrows to move, SPACE to jump", 10, 20);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle player movement
        if (movingLeft) playerX -= 5;
        if (movingRight) playerX += 5;
        
        // Apply gravity
        playerVelocityY += GRAVITY;
        playerY += playerVelocityY;
        
        // Check for collisions with platforms
        Rectangle playerRect = new Rectangle(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);
        boolean onPlatform = false;
        
        for (Rectangle platform : platforms) {
            if (playerRect.intersects(platform)) {
                // Land on top of platform
                if (playerVelocityY > 0 && playerY + PLAYER_SIZE <= platform.y + 10) {
                    playerY = platform.y - PLAYER_SIZE;
                    playerVelocityY = 0;
                    isJumping = false;
                    onPlatform = true;
                }
            }
        }
        
        // Keep player in bounds
        if (playerX < 0) playerX = 0;
        if (playerX > WIDTH - PLAYER_SIZE) playerX = WIDTH - PLAYER_SIZE;
        if (playerY > HEIGHT - PLAYER_SIZE) {
            playerY = HEIGHT - PLAYER_SIZE;
            playerVelocityY = 0;
            isJumping = false;
        }
        
        repaint();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                movingLeft = true;
                break;
            case KeyEvent.VK_RIGHT:
                movingRight = true;
                break;
            case KeyEvent.VK_SPACE:
                if (!isJumping) {
                    playerVelocityY = -JUMP_STRENGTH;
                    isJumping = true;
                }
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                movingLeft = false;
                break;
            case KeyEvent.VK_RIGHT:
                movingRight = false;
                break;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Platformer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new SimplePlatformer());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
