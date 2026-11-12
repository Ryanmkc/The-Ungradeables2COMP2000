package Model;

public class ChaseStrategy implements PlayerMovementStrategy {
    @Override
    public void move(Match match, Player player, int targetX, int targetY) {
        match.moveToward(player, targetX, targetY, 2);
    }
}