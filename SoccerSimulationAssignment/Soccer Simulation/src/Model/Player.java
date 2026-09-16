package Model;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class Player implements Actor {
    private String name;
    private int x;
    private int y;
    private int jerseyNumber;
    private Color colour;
    private boolean isGoalkeeper;

    // retain original formation pos
    private final int homeX;
    private final int homeY;

    // render specific jersy colour
    public Player(int x, int y, int jerseyNumber, Color colour) {
        this("", x, y, jerseyNumber, colour);
    }

    public Player(String name, int x, int y, Color colour) {
        this(name, x, y, 0, colour);
    }

    // Used by Match for the goalkeeper 
    public Player(String name, int x, int y, Color colour, boolean isGoalkeeper) {
        this(name, x, y, 0, colour);
        this.isGoalkeeper = isGoalkeeper;
    }

    // Shared constructor
    private Player(String name, int x, int y, int jerseyNumber, Color colour) {
        this.name = name;
        this.x = x;
        this.y = y;

        // Save og formation position
        this.homeX = x;
        this.homeY = y;

        this.jerseyNumber = jerseyNumber;
        this.colour = colour;
    }

    public String getName() {
        return name;
    }

    public boolean isGoalkeeper() {
        return isGoalkeeper;
    }

    // og formation X position
    public int getHomeX() {
        return homeX;
    }

    // og formation Y position
    public int getHomeY() {
        return homeY;
    }

    public void draw(Graphics g) {
        g.setColor(colour);
        g.fillOval(x - 9, y - 9, 18, 18);
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        String number = String.valueOf(jerseyNumber);
        FontMetrics metrics = g.getFontMetrics();
        int textX = x - metrics.stringWidth(number) / 2;
        int textY = y + metrics.getHeight() / 2 - metrics.getDescent();
        g.drawString(number, textX, textY);
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public Color getColor() {
        return this.colour;
    }

    @Override
    public ActorShapeEnum getShape() {
        return ActorShapeEnum.SQUARE;
    }

    @Override
    public void moveUp(int distance) {
        if (this.y - distance >= 0) {
            this.y -= distance;
        }
    }

    @Override
    public void moveDown(int distance) {
        if (this.y + distance <= Rendering.ScreenSize.height) {
            this.y += distance;
        }
    }

    @Override
    public void moveLeft(int distance) {
        if (this.x - distance >= 0) {
            this.x -= distance;
        }
    }

    @Override
    public void moveRight(int distance) {
        if (this.x + distance <= Rendering.ScreenSize.width) {
            this.x += distance;
        }
    }
}