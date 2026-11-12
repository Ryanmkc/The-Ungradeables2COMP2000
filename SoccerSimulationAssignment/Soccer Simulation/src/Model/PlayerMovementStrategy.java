package Model;

public interface PlayerMovementStrategy {
    void move(Match match, Player player, int targetX, int targetY);
}