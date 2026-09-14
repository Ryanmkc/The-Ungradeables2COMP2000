package Model;

import java.awt.Color;

public class OffensivePlayer extends Player {

    private int shootingAbility;

    public OffensivePlayer(int x, int y, int jerseyNumber, Color colour, int shootingAbility) {
        super(x, y, jerseyNumber, colour);

        if (shootingAbility < 0 || shootingAbility > 100) {
            throw new IllegalArgumentException("Shooting ability must be between 0 and 100.");
        }

        this.shootingAbility = shootingAbility;
    }

    public int getShootingAbility() {
        return shootingAbility;
    }
}