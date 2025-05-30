package yourgamepackage.gameobjects;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Coin extends GameObject {
    private float rotationAngle = 0;
    private boolean collected = false;
    
    public Coin(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
    }
    
    @Override
    public void draw(Graphics g) {
        if (!collected) {
            Graphics2D g2d = (Graphics2D) g.create();
            
            // Rotate around center
            AffineTransform at = new AffineTransform();
            at.translate(x + width/2, y + height/2);
            at.rotate(rotationAngle);
            at.translate(-width/2, -height/2);
            
            g2d.drawImage(image, at, null);
            g2d.dispose();
            
            rotationAngle += 0.1; // Increment rotation
        }
    }
    
    public boolean isCollected() {
        return collected;
    }
    
    public void collect() {
        collected = true;
    }
    
    public int getValue() {
        return 10; // Points awarded when collected
    }
}
