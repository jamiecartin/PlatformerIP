package EnhancedIP.gameobjects;

import java.awt.*;

public class Platform extends GameObject {
    public Platform(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
    }
    
    // Additional platform-specific methods can be added here
    public boolean isBreakable() {
        return false; // Override in subclasses for breakable platforms
    }
}
