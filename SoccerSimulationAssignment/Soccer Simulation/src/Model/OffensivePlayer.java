package Model;
import java.awt.Color;

public class OffensivePlayer extends Player {

    private int shootingAbility;

    public OffensivePlayer(int x, int y, int jerseyNumber, Color colour, int shootingAbility) {
        super(x, y, jerseyNumber, colour);
        this.shootingAbility = shootingAbility;
    }

    public int getShootingAbility() {
        return shootingAbility;
    }
}
