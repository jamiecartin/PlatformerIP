package EnhancedIP.gameobjects;

public class Player extends GameObject {
    private AnimatedSprite runAnimation;
    private AnimatedSprite idleAnimation;
    private AnimatedSprite jumpAnimation;
    private boolean facingRight = true;
    
    public Player(int x, int y) {
        super(x, y, 32, 32);
        
        // Initialize animations
        runAnimation = new AnimatedSprite("/sprites/player/run_", 6, 80);
        idleAnimation = new AnimatedSprite("/sprites/player/idle_", 4, 150);
        jumpAnimation = new AnimatedSprite("/sprites/player/jump_", 2, 100);
    }
    
    @Override
    public void draw(Graphics g) {
        Image currentImage;
        
        if (!isOnGround) {
            currentImage = jumpAnimation.getCurrentFrame();
        } else if (movingLeft || movingRight) {
            currentImage = runAnimation.getCurrentFrame();
        } else {
            currentImage = idleAnimation.getCurrentFrame();
        }
        
        // Flip image if facing left
        if (facingRight) {
            g.drawImage(currentImage, x, y, width, height, null);
        } else {
            g.drawImage(currentImage, x + width, y, -width, height, null);
        }
    }
    
    // Update movement to set facing direction
    public void update(ArrayList<Platform> platforms) {
        if (movingLeft) facingRight = false;
        if (movingRight) facingRight = true;
        // ... rest of update logic
    }
}
