package yourgamepackage;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.IOException;

public class AnimatedSprite {
    private Image[] frames;
    private int currentFrame = 0;
    private long lastUpdate;
    private int frameDelay; // milliseconds
    private boolean looping = true;
    
    public AnimatedSprite(String basePath, int frameCount, int delay) {
        frames = new Image[frameCount];
        frameDelay = delay;
        
        try {
            for (int i = 0; i < frameCount; i++) {
                String path = basePath + i + ".png";
                frames[i] = ImageIO.read(getClass().getResource(path));
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Fallback to a single colored rectangle if images fail to load
            frames = new Image[1];
            frames[0] = createColoredImage(Color.RED, 32, 32);
        }
    }
    
    public Image getCurrentFrame() {
        if (frames.length > 1 && System.currentTimeMillis() - lastUpdate > frameDelay) {
            currentFrame = (currentFrame + 1) % frames.length;
            if (!looping && currentFrame == frames.length - 1) {
                return frames[currentFrame]; // Stay on last frame
            }
            lastUpdate = System.currentTimeMillis();
        }
        return frames[currentFrame];
    }
    
    public void reset() {
        currentFrame = 0;
        lastUpdate = System.currentTimeMillis();
    }
    
    public void setLooping(boolean looping) {
        this.looping = looping;
    }
    
    // Helper method for fallback
    private Image createColoredImage(Color color, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        return image;
    }
}
