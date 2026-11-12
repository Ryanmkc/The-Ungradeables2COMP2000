package Model;

public class FormationStrategy implements PlayerMovementStrategy {
    @Override
    public void move(Match match, Player player, int targetX, int targetY) {
        match.moveIntoShape(player, targetX, targetY);
    }
}