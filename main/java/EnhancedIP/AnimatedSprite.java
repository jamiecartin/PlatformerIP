class AnimatedSprite {
    private Image[] frames;
    private int currentFrame = 0;
    private long lastUpdate;
    private int frameDelay; // milliseconds
    
    public AnimatedSprite(String basePath, int frameCount, int delay) {
        frames = new Image[frameCount];
        frameDelay = delay;
        
        try {
            for (int i = 0; i < frameCount; i++) {
                frames[i] = ImageIO.read(getClass().getResource(basePath + i + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Image getCurrentFrame() {
        if (System.currentTimeMillis() - lastUpdate > frameDelay) {
            currentFrame = (currentFrame + 1) % frames.length;
            lastUpdate = System.currentTimeMillis();
        }
        return frames[currentFrame];
    }
}

// Usage in Player class:
private AnimatedSprite runAnimation;
private AnimatedSprite idleAnimation;
private boolean facingRight = true;

// In constructor:
runAnimation = new AnimatedSprite("/sprites/player_run_", 6, 100);
idleAnimation = new AnimatedSprite("/sprites/player_idle_", 4, 200);

// In draw method:
Image currentImage = (movingLeft || movingRight) ? runAnimation.getCurrentFrame() : idleAnimation.getCurrentFrame();
if (!facingRight) {
    // Flip image horizontally
    g.drawImage(currentImage, x + width, y, -width, height, null);
} else {
    g.drawImage(currentImage, x, y, width, height, null);
}
