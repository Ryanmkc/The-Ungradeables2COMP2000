package Model;
import java.awt.Color;

public class Goalkeeper extends Player{
    
    private int saveAbility;

    public Goalkeeper(int x, int y, int jerseyNumber, Color colour, int saveAbility) {
        super(x, y, jerseyNumber, colour);
        this.saveAbility = saveAbility;
    }

    public int getSaveAbility() {
        return saveAbility;
    }
}
