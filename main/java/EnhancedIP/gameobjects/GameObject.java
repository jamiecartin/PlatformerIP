package EnhancedIP.gameobjects;

import java.awt.*;

public abstract class GameObject {
    protected int x, y;
    protected int width, height;
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
    
    // Getters and setters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
}
