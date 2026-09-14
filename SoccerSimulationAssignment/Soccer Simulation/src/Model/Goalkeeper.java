package Model;

import java.awt.Color;

public class Goalkeeper extends Player {

    private int saveAbility;

    public Goalkeeper(int x, int y, int jerseyNumber, Color colour, int saveAbility) {
        super(x, y, jerseyNumber, colour);

        if (saveAbility < 0 || saveAbility > 100) {
            throw new IllegalArgumentException("Save ability must be between 0 and 100.");
        }

        this.saveAbility = saveAbility;
    }

    public int getSaveAbility() {
        return saveAbility;
    }
}
