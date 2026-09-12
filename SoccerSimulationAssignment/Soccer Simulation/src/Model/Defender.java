package Model;

import java.awt.Color;
import java.awt.Graphics;

// arpan making the defender subclass
/*
 * this is my subclass extending Player for the defenders in the formations (like 4-4-2).
 * it has tackle power and tracks yellow cards.
 * if the tackle power is way too high (>95) or if they get 2 yellow cards,
 * it throws my RedCardException and kicks them out so they dont get drawn on the pitch anymore.
 * guys can just put this into the Player[] array in Match or VerticalPitch since it extends Player.
 */
public class Defender extends Player {

    private int tacklePower;
    private int yellowCards;
    private boolean isSentOff;

    // default constructor
    public Defender(int x, int y, int jerseyNumber, Color colour) {
        super(x, y, jerseyNumber, colour);
        this.tacklePower = 70; // decent default tackle skill
        this.yellowCards = 0;
        this.isSentOff = false;
    }

    // constructor if we want to set a custom tackle power
    public Defender(int x, int y, int jerseyNumber, Color colour, int tacklePower) {
        super(x, y, jerseyNumber, colour);
        this.tacklePower = tacklePower;
        this.yellowCards = 0;
        this.isSentOff = false;
    }

    public int getTacklePower() {
        return tacklePower;
    }

    public void setTacklePower(int tacklePower) {
        this.tacklePower = tacklePower;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public boolean isSentOff() {
        return isSentOff;
    }

    // tackling logic against an attacker dribbling
    public boolean attemptTackle(int opponentDribble) throws RedCardException {
        // cant do anything if already sent off
        if (isSentOff) {
            throw new RedCardException(1, "Player is already off the pitch");
        }

        // reckless slide tackle straight red card
        if (this.tacklePower > 95) {
            this.isSentOff = true;
            throw new RedCardException(1, "Dangerous slide tackle straight red!");
        }

        // won the ball
        if (this.tacklePower >= opponentDribble) {
            return true;
        } else {
            // missed tackle and gave away a foul
            this.yellowCards++;
            if (this.yellowCards >= 2) {
                this.isSentOff = true;
                throw new RedCardException(1, "Second yellow card, player is gone!");
            }
            return false;
        }
    }

    // override draw so if the player got a red card they disappear from the pitch
    @Override
    public void draw(Graphics g) {
        if (!isSentOff) {
            super.draw(g);
        }
    }

    @Override
    public String toString() {
        return "Defender [Tackle=" + tacklePower + ", Yellows=" + yellowCards + ", Out=" + isSentOff + "]";
    }
}