package EnhancedIP.gameobjects;

import java.awt.*;

public class Enemy extends GameObject {
    private int moveSpeed;
    private int leftBound, rightBound;
    private boolean facingRight;
    private AnimatedSprite walkAnimation;
    
    public Enemy(int x, int y, int width, int height, Image image, int leftBound, int rightBound) {
        super(x, y, width, height, image);
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.moveSpeed = 2;
        this.facingRight = true;
        
        // Initialize animation (if using animated sprites)
        // walkAnimation = new AnimatedSprite("/sprites/enemy/walk_", 4, 150);
    }
    
    public void update() {
        // Basic patrol movement
        if (facingRight) {
            x += moveSpeed;
            if (x >= rightBound - width) {
                facingRight = false;
            }
        } else {
            x -= moveSpeed;
            if (x <= leftBound) {
                facingRight = true;
            }
        }
    }
    
    @Override
    public void draw(Graphics g) {
        if (walkAnimation != null) {
            Image frame = walkAnimation.getCurrentFrame();
            if (facingRight) {
                g.drawImage(frame, x, y, width, height, null);
            } else {
                g.drawImage(frame, x + width, y, -width, height, null);
            }
        } else {
            super.draw(g);
        }
    }
    
    // Enemy-specific methods
    public boolean isFacingRight() {
        return facingRight;
    }
    
    public int getDamage() {
        return 1; // Damage dealt to player on contact
    }
}
