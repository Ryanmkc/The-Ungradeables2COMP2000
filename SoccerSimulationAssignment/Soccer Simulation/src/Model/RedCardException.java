package Model;

// arpan doing this custom exception part for the referee fouls and red cards
/*
 * i made this exception so when a defender does a bad slide tackle or gets 
 * two yellow cards the ref can send them off the field.
 * it keeps the jersey number so we know which player got kicked out, and 
 * William can just print the message in his mini log box on the gui.
 */
public class RedCardException extends Exception {

    private final int jerseyNumber;

    public RedCardException(int jerseyNumber, String reason) {
        super("Red Card! Player #" + jerseyNumber + " sent off: " + reason);
        this.jerseyNumber = jerseyNumber;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }
}