// Actor.java
package Model;

import java.awt.Color;

public interface Actor {
    // movement events
    void moveUp(int distance);
    void moveDown(int distance);
    void moveLeft(int distance);
    void moveRight(int distance);

    // coordinates
    int getX();
    int getY();

    // visual
    Color getColor();
    ActorShapeEnum getShape();
}