package EnhancedIP.gameobjects;

import java.awt.*;

public class Goal extends GameObject {
    private boolean reached = false;
    
    public Goal(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
    }
    
    @Override
    public void draw(Graphics g) {
        if (!reached) {
            // Add pulsing effect
            float pulse = (float) (0.9f + 0.1f * Math.sin(System.currentTimeMillis() / 200.0));
            int newWidth = (int)(width * pulse);
            int newHeight = (int)(height * pulse);
            
            int offsetX = (width - newWidth) / 2;
            int offsetY = (height - newHeight) / 2;
            
            g.drawImage(image, x + offsetX, y + offsetY, newWidth, newHeight, null);
        } else {
            super.draw(g);
        }
    }
    
    public boolean isReached() {
        return reached;
    }
    
    public void setReached(boolean reached) {
        this.reached = reached;
    }
}
