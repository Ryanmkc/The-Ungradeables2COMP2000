package Model;

// Thrown when a movement would push a Player outside the pitch boundaries.
// This protects the invariant that every Player must always have valid,
// on-pitch coordinates.
public class InvalidPositionException extends RuntimeException {
    public InvalidPositionException(String message) {
        super(message);
    }
}
