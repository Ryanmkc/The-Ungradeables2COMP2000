package Model;

import java.awt.Color;

public abstract class AbstractActor implements Actor {
    protected int x;
    protected int y;
    protected Color color;
    protected ActorShapeEnum shape;

    public AbstractActor(int startX, int startY, Color color, ActorShapeEnum shape) {
        this.x = startX;
        this.y = startY;
        this.color = color;
        this.shape = shape;
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
        return this.color;
    }

    @Override
    public ActorShapeEnum getShape() {
        return this.shape;
    }

    @Override
    public void moveUp(int distance) {
        this.y -= distance;
    }

    @Override
    public void moveDown(int distance) {
        this.y += distance;
    }

    @Override
    public void moveLeft(int distance) {
        this.x -= distance;
    }

    @Override
    public void moveRight(int distance) {
        this.x += distance;
    }
}