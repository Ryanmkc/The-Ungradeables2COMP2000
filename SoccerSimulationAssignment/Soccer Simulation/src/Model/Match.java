// WILLIAM + RYAN IS DOING THIS
/*
 * use the display already made in the soccer pitch class,
 * the match logic, goal scoring and ref stuff is being done by other people
 * all this class will do will be to put players on the pitch in certain
 * formations
 * i also have to handle the gui for everything while simulation is running a
 * mini print log
 * execute events just be an area for stuff to happen like pull everything
 * together
 */
package Model;

import java.awt.*;
import javax.swing.*;
import Rendering.ScreenSize;
import java.util.*;

public class Match {
    public SoccerPitch pitch;
    public Ball ball;

    // Main Arrays
    private ArrayList<Actor> gameActors;
    private ArrayList<Player> players;
    private volatile boolean isRunning;
    private Thread simulationThread;

    // Controls passing decisions and prevents a player immediately
    // collecting the ball again after making a pass
    private final Random random = new Random();
    private int ticksUntilPass = 70;
    private Player recentPasser;
    private int recentPasserCooldown;
    private int possessionTicks;

    private static final int MIN_TICKS_BEFORE_RANDOM_KICK = 30;
    private static final int MAX_POSSESSION_TICKS = 120;
    private static final int RANDOM_KICK_CHANCE = 180;

    // Players briefly pause after losing possession
    private final Map<Player, Integer> stunnedPlayers = new HashMap<>();
    private static final int STUN_TICKS = 25;
    private static final int TACKLE_DISTANCE = 22;
    private static final int TACKLE_CHANCE = 45;

    // Match score and the short pause used after a goal
    private int homeScore;
    private int awayScore;
    private int goalPauseTicks;

    public Match() {
        this.pitch = new SoccerPitch();
        this.gameActors = new ArrayList<>();
        this.players = new ArrayList<>();
        this.isRunning = false;
    }

    // spawn entities
    public void setupAndStartSimulation(String homeFormation, String awayFormation) {
        // Stop an old loop before starting a new match
        stopSimulation();

        gameActors.clear();
        players.clear();
        recentPasser = null;
        recentPasserCooldown = 0;
        ticksUntilPass = 70;
        possessionTicks = 0;
        stunnedPlayers.clear();
        homeScore = 0;
        awayScore = 0;
        goalPauseTicks = 0;
        pitch.setScore(homeScore, awayScore);
        pitch.setMatchMessage("");

        // summon ball in centre
        ball = new Ball(null);
        int centerX = ScreenSize.width / 2;
        int centerY = ScreenSize.height / 2;

        // Force initial placement on kickoff
        ball.moveRight(centerX);
        ball.moveDown(centerY);

        // spawn and map formations
        generateTeamFormation(homeFormation, Color.BLUE, true);
        generateTeamFormation(awayFormation, Color.RED, false);

        // register everything to the render loop
        gameActors.addAll(players);
        gameActors.add(ball);

        // push the initial state to the pitch immediately, so something
        // shows up before the simulation loop's first tick
        pitch.setActors(gameActors);

        // begin processing thread loop
        this.isRunning = true;
        simulationThread = new Thread(this::runSimulationEngineLoop, "soccer-simulation");
        simulationThread.start();
    }

    // specific grid patterns depending on chosen config
    private void generateTeamFormation(String formationType, Color teamColor, boolean isHomeTeam) {
        int centerY = ScreenSize.height / 2;
        int directionX = isHomeTeam ? 1 : -1;
        int baseLineX = isHomeTeam ? 80 : ScreenSize.width - 80;

        // main structural nodes depending on selected configuration parameters
        int defX = baseLineX + (120 * directionX);
        int midX = baseLineX + (280 * directionX);
        int fwdX = baseLineX + (420 * directionX);

        String prefix = isHomeTeam ? "Home_" : "Away_";

        // Always spawn Goalkeeper at base line
        players.add(new Player(prefix + "GK", baseLineX, centerY, teamColor, true));

        if ("4-3-3".equals(formationType)) {
            // 4 Defenders
            players.add(new Player(prefix + "DF1", defX, centerY - 150, teamColor));
            players.add(new Player(prefix + "DF2", defX, centerY - 50, teamColor));
            players.add(new Player(prefix + "DF3", defX, centerY + 50, teamColor));
            players.add(new Player(prefix + "DF4", defX, centerY + 150, teamColor));
            // 3 Midfielders
            players.add(new Player(prefix + "MF1", midX, centerY - 100, teamColor));
            players.add(new Player(prefix + "MF2", midX, centerY, teamColor));
            players.add(new Player(prefix + "MF3", midX, centerY + 100, teamColor));
            // 3 Forwards
            players.add(new Player(prefix + "FW1", fwdX, centerY - 120, teamColor));
            players.add(new Player(prefix + "FW2", fwdX, centerY, teamColor));
            players.add(new Player(prefix + "FW3", fwdX, centerY + 120, teamColor));
        } else if ("3-5-2".equals(formationType)) {
            // 3 Defenders
            players.add(new Player(prefix + "DF1", defX, centerY - 120, teamColor));
            players.add(new Player(prefix + "DF2", defX, centerY, teamColor));
            players.add(new Player(prefix + "DF3", defX, centerY + 120, teamColor));
            // 5 Midfielders
            players.add(new Player(prefix + "MF1", midX, centerY - 160, teamColor));
            players.add(new Player(prefix + "MF2", midX, centerY - 80, teamColor));
            players.add(new Player(prefix + "MF3", midX, centerY, teamColor));
            players.add(new Player(prefix + "MF4", midX, centerY + 80, teamColor));
            players.add(new Player(prefix + "MF5", midX, centerY + 160, teamColor));
            // 2 Forwards
            players.add(new Player(prefix + "FW1", fwdX, centerY - 60, teamColor));
            players.add(new Player(prefix + "FW2", fwdX, centerY + 60, teamColor));
        } else { // fallback standard "4-4-2"
            // 4 Defenders
            players.add(new Player(prefix + "DF1", defX, centerY - 150, teamColor));
            players.add(new Player(prefix + "DF2", defX, centerY - 50, teamColor));
            players.add(new Player(prefix + "DF3", defX, centerY + 50, teamColor));
            players.add(new Player(prefix + "DF4", defX, centerY + 150, teamColor));
            // 4 Midfielders
            players.add(new Player(prefix + "MF1", midX, centerY - 150, teamColor));
            players.add(new Player(prefix + "MF2", midX, centerY - 50, teamColor));
            players.add(new Player(prefix + "MF3", midX, centerY + 50, teamColor));
            players.add(new Player(prefix + "MF4", midX, centerY + 150, teamColor));
            // 2 Forwards
            players.add(new Player(prefix + "FW1", fwdX, centerY - 60, teamColor));
            players.add(new Player(prefix + "FW2", fwdX, centerY + 60, teamColor));
        }
    }

    // background engine checking frame
    private void runSimulationEngineLoop() {
        while (isRunning) {
            updateStunnedPlayers();

            // Briefly hold the reset formation after a goal before restarting
            if (goalPauseTicks > 0) {
                goalPauseTicks--;

                if (goalPauseTicks == 0) {
                    pitch.setMatchMessage("");
                }

                pitch.setActors(gameActors);

                if (!pauseForNextFrame()) {
                    break;
                }

                continue;
            }

            // Move a loose ball, or sync an owned ball with its player
            ball.update();

            // A goal ends this passage of play and resets everyone for kickoff
            if (checkForGoal()) {
                pitch.setActors(gameActors);

                if (!pauseForNextFrame()) {
                    break;
                }

                continue;
            }

            int ballX = ball.getX();
            int ballY = ball.getY();
            Player owner = ball.getOwner();

            Player homeChaser = null;
            Player awayChaser = null;
            Player presser = null;

            if (owner == null) {
                // Only the closest outfield player from each team chase loose ball
                homeChaser = findClosestPlayer(true, ballX, ballY);
                awayChaser = findClosestPlayer(false, ballX, ballY);
            } else {
                // When ball is owned, closest opponent presses
                presser = findClosestOpponent(owner);
            }

            // player ai
            for (Player p : players) {
                if (isStunned(p)) {
                    // A player who has just lost the ball cannot instantly steal it back
                    continue;
                } else if (p.isGoalkeeper()) {
                    updateGoalkeeper(p, ballY);
                } else if (owner == null) {
                    if (p == homeChaser || p == awayChaser) {
                        moveToward(p, ballX, ballY, 2);
                    } else {
                        // Everyone else shuffles with the play but keeps formation
                        moveIntoShape(p, ballX, ballY);
                    }
                } else if (p == owner) {
                    // The player in possession dribbles toward the opposing goal
                    int direction = isHome(p) ? 1 : -1;
                    moveToward(p, p.getX() + (direction * 25), p.getY(), 1);
                } else if (p == presser) {
                    moveToward(p, owner.getX(), owner.getY(), 2);
                } else {
                    // Everyone else shuffles with the play but keeps formation
                    moveIntoShape(p, ballX, ballY);
                }
            }

            resolvePlayerCollisions();
            attemptTackle();

            if (recentPasserCooldown > 0) {
                recentPasserCooldown--;
                if (recentPasserCooldown == 0) {
                    recentPasser = null;
                }
            }

            // collision: whichever eligible player is closest takes possession
            if (ball.getOwner() == null) {
                int pickupRadius = 18; // matches ~9px draw radius of circle, so they visually touch
                Player collector = findPlayerTouchingBall(pickupRadius);

                if (collector != null) {
                    ball.setOwner(collector);
                    possessionTicks = 0;
                    // Hold ball for roughly 1-2 seconds before pass
                    ticksUntilPass = 50 + random.nextInt(70);
                }
            } else {
                ticksUntilPass--;
                possessionTicks++;

                Player passer = ball.getOwner();
                boolean randomKick = possessionTicks >= MIN_TICKS_BEFORE_RANDOM_KICK
                        && random.nextInt(RANDOM_KICK_CHANCE) == 0;
                boolean heldTooLong = possessionTicks >= MAX_POSSESSION_TICKS;

                if (randomKick || heldTooLong) {
                    kickBallRandomly(passer);
                    rememberKicker(passer);
                    ticksUntilPass = 50 + random.nextInt(70);
                } else if (ticksUntilPass <= 0) {
                    // ormal pass first, if nobody is available kick
                    if (!passBall(passer)) {
                        kickBallRandomly(passer);
                    }

                    rememberKicker(passer);
                    ticksUntilPass = 50 + random.nextInt(70);
                }
            }

            // push updated positions to the pitch so it redraws
            // this frame's state, repaint() is safe to call from a
            // background thread, it just schedules the redraw on the EDT
            pitch.setActors(gameActors);

            if (!pauseForNextFrame()) {
                break;
            }
        }
    }

    //PAUSE CAPTURE FOR REPAINT PHASES
    private boolean pauseForNextFrame() {
        try {
            Thread.sleep(16); // Standard 60 fps
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    // Check whether the centre of the ball has entered either goal area
    private boolean checkForGoal() {
        int goalTop = (ScreenSize.height - SoccerPitch.GOAL_HEIGHT) / 2;
        int goalBottom = goalTop + SoccerPitch.GOAL_HEIGHT;
        int ballX = ball.getX();
        int ballY = ball.getY();

        if (ballY < goalTop || ballY > goalBottom) {
            return false;
        }

        String scoringTeam;

        // Home attacks the goal on the right Away attacks the goal on the left
        if (ballX >= ScreenSize.width - SoccerPitch.GOAL_WIDTH) {
            homeScore++;
            scoringTeam = "HOME";
        } else if (ballX <= SoccerPitch.GOAL_WIDTH) {
            awayScore++;
            scoringTeam = "AWAY";
        } else {
            return false;
        }

        pitch.setScore(homeScore, awayScore);
        pitch.setMatchMessage("GOAL! " + scoringTeam);
        resetForKickoff();
        goalPauseTicks = 60; // about one second at 60 fps
        return true;
    }

    // Return the players and ball to their starting positions after a goal
    private void resetForKickoff() {
        for (Player p : players) {
            moveToward(p, p.getHomeX(), p.getHomeY(), ScreenSize.width);
        }

        // Release the ball, remove its velocity and move it back to the centre
        ball.setOwner(null);
        ball.kick(0, 0);

        int centerX = ScreenSize.width / 2;
        int centerY = ScreenSize.height / 2;

        if (ball.getX() < centerX) {
            ball.moveRight(centerX - ball.getX());
        } else if (ball.getX() > centerX) {
            ball.moveLeft(ball.getX() - centerX);
        }

        if (ball.getY() < centerY) {
            ball.moveDown(centerY - ball.getY());
        } else if (ball.getY() > centerY) {
            ball.moveUp(ball.getY() - centerY);
        }

        recentPasser = null;
        recentPasserCooldown = 0;
        ticksUntilPass = 70;
        possessionTicks = 0;
        stunnedPlayers.clear();
    }

    private void updateStunnedPlayers() {
        Iterator<Map.Entry<Player, Integer>> iterator = stunnedPlayers.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Player, Integer> entry = iterator.next();
            int ticksRemaining = entry.getValue() - 1;

            if (ticksRemaining <= 0) {
                iterator.remove();
            } else {
                entry.setValue(ticksRemaining);
            }
        }
    }

    private boolean isStunned(Player p) {
        return stunnedPlayers.containsKey(p);
    }

    // A nearby opponent occasionally takes possession from the current owner
    private void attemptTackle() {
        Player owner = ball.getOwner();

        if (owner == null) {
            return;
        }

        Player closestTackler = null;
        double closestDistance = Double.MAX_VALUE;

        for (Player opponent : players) {
            if (opponent.isGoalkeeper() || isStunned(opponent)
                    || isHome(opponent) == isHome(owner)) {
                continue;
            }

            double distance = distanceBetween(
                    opponent.getX(),
                    opponent.getY(),
                    owner.getX(),
                    owner.getY()
            );

            if (distance <= TACKLE_DISTANCE && distance < closestDistance) {
                closestDistance = distance;
                closestTackler = opponent;
            }
        }

        if (closestTackler != null && random.nextInt(TACKLE_CHANCE) == 0) {
            completeTackle(owner, closestTackler);
        }
    }

    private void completeTackle(Player playerWhoLostBall, Player tackler) {
        ball.setOwner(tackler);
        stunnedPlayers.put(playerWhoLostBall, STUN_TICKS);

        possessionTicks = 0;
        ticksUntilPass = 50 + random.nextInt(70);
    }

    private boolean isHome(Player p) {
        return p.getName().startsWith("Home_");
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private void moveToward(Player p, int targetX, int targetY, int speed) {
        if (p.getX() < targetX) {
            p.moveRight(Math.min(speed, targetX - p.getX()));
        } else if (p.getX() > targetX) {
            p.moveLeft(Math.min(speed, p.getX() - targetX));
        }

        if (p.getY() < targetY) {
            p.moveDown(Math.min(speed, targetY - p.getY()));
        } else if (p.getY() > targetY) {
            p.moveUp(Math.min(speed, p.getY() - targetY));
        }
    }

    // Players move with the play without abandoning their original positions
    private void moveIntoShape(Player p, int ballX, int ballY) {
        int centerX = ScreenSize.width / 2;
        int centerY = ScreenSize.height / 2;

        int xShift = clamp((ballX - centerX) / 8, -45, 45);
        int yShift = clamp((ballY - centerY) / 10, -25, 25);

        int targetX = clamp(p.getHomeX() + xShift, 20, ScreenSize.width - 20);
        int targetY = clamp(p.getHomeY() + yShift, 20, ScreenSize.height - 20);

        // Nearby players move slowly into supporting positions around the ball
        // The influence fades with distance, so the whole team does not swarm it
        double distanceToBall = distanceBetween(p.getX(), p.getY(), ballX, ballY);
        int supportRange = 220;

        if (distanceToBall < supportRange) {
            double influence = (supportRange - distanceToBall) / supportRange;
            double pullStrength = 0.10 + (influence * 0.15);

            int supportX = clamp(
                    (int) Math.round((ballX - targetX) * pullStrength),
                    -40,
                    40
            );
            int supportY = clamp(
                    (int) Math.round((ballY - targetY) * pullStrength),
                    -30,
                    30
            );

            targetX = clamp(targetX + supportX, 20, ScreenSize.width - 20);
            targetY = clamp(targetY + supportY, 20, ScreenSize.height - 20);
        }

        moveToward(p, targetX, targetY, 1);
    }

    private Player findClosestPlayer(boolean homeTeam, int targetX, int targetY) {
        Player closest = null;
        double closestDistance = Double.MAX_VALUE;

        for (Player p : players) {
            if (p.isGoalkeeper() || isStunned(p) || isHome(p) != homeTeam) {
                continue;
            }

            double distance = distanceBetween(p.getX(), p.getY(), targetX, targetY);

            if (distance < closestDistance) {
                closestDistance = distance;
                closest = p;
            }
        }

        return closest;
    }

    private Player findClosestOpponent(Player owner) {
        Player closest = null;
        double closestDistance = Double.MAX_VALUE;
        boolean ownerIsHome = isHome(owner);

        for (Player p : players) {
            if (p.isGoalkeeper() || isStunned(p) || isHome(p) == ownerIsHome) {
                continue;
            }

            double distance = distanceBetween(p.getX(), p.getY(), owner.getX(), owner.getY());

            if (distance < closestDistance) {
                closestDistance = distance;
                closest = p;
            }
        }

        return closest;
    }

    private Player findPlayerTouchingBall(int pickupRadius) {
        Player closest = null;
        double closestDistance = Double.MAX_VALUE;
        int equallyClosePlayers = 0;

        for (Player p : players) {
            if (isStunned(p) || (p == recentPasser && recentPasserCooldown > 0)) {
                continue;
            }

            double distance = distanceBetween(p.getX(), p.getY(), ball.getX(), ball.getY());

            if (distance <= pickupRadius && distance < closestDistance - 0.001) {
                closestDistance = distance;
                closest = p;
                equallyClosePlayers = 1;
            } else if (distance <= pickupRadius
                    && Math.abs(distance - closestDistance) < 0.001) {
                // Resolve exact ties randomly instead of favouring Home,
                // whose players happen to appear first in the array
                equallyClosePlayers++;

                if (random.nextInt(equallyClosePlayers) == 0) {
                    closest = p;
                }
            }
        }

        return closest;
    }

    private double distanceBetween(int x1, int y1, int x2, int y2) {
        long dx = (long) x2 - x1;
        long dy = (long) y2 - y1;
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    // If no useful pass is available, kick random direction for variation
    private void kickBallRandomly(Player kicker) {
        double randomAngle = random.nextDouble() * Math.PI * 2;
        int kickSpeed = 6 + random.nextInt(4);

        int velocityX = (int) Math.round(Math.cos(randomAngle) * kickSpeed);
        int velocityY = (int) Math.round(Math.sin(randomAngle) * kickSpeed);

        ball.kick(velocityX, velocityY);
    }

    private void rememberKicker(Player kicker) {
        recentPasser = kicker;
        recentPasserCooldown = 12;
        possessionTicks = 0;
    }

    private boolean passBall(Player passer) {
        boolean homeTeam = isHome(passer);
        int attackDirection = homeTeam ? 1 : -1;
        Player bestTarget = null;
        double bestScore = -Double.MAX_VALUE;

        for (Player teammate : players) {
            if (teammate == passer || teammate.isGoalkeeper()
                    || isStunned(teammate) || isHome(teammate) != homeTeam) {
                continue;
            }

            int dx = teammate.getX() - passer.getX();
            int dy = teammate.getY() - passer.getY();
            double distance = distanceBetween(passer.getX(), passer.getY(), teammate.getX(), teammate.getY());

            // Avoid passes to somebody on top of the passer or too far away
            if (distance < 35 || distance > 260) {
                continue;
            }

            // Prefer a forward pass but keep some variation
            int forwardDistance = attackDirection * dx;
            double score = (forwardDistance * 1.5)
                    - (Math.abs(dy) * 0.25)
                    - (distance * 0.10)
                    + (random.nextDouble() * 40);

            if (score > bestScore) {
                bestScore = score;
                bestTarget = teammate;
            }
        }

        if (bestTarget == null) {
            return false;
        }

        int dx = bestTarget.getX() - ball.getX();
        int dy = bestTarget.getY() - ball.getY();
        double distance = distanceBetween(ball.getX(), ball.getY(), bestTarget.getX(), bestTarget.getY());

        if (distance == 0) {
            return false;
        }

        int passSpeed = 7;
        int velocityX = (int) Math.round((dx / distance) * passSpeed);
        int velocityY = (int) Math.round((dy / distance) * passSpeed);

        ball.kick(velocityX, velocityY);
        return true;
    }

    // Goalkeepers only track the ball vertically, and stay clamped inside their box
    private void updateGoalkeeper(Player gk, int ballY) {
        if (gk.getY() < ballY) gk.moveDown(1);
        if (gk.getY() > ballY) gk.moveUp(1);

        int margin = 15; // keeps them off the very edge of the box
        int minY = (ScreenSize.height - SoccerPitch.PENALTY_HEIGHT) / 2 + margin;
        int maxY = minY + SoccerPitch.PENALTY_HEIGHT - (margin * 2);

        if (gk.getY() < minY) gk.moveDown(minY - gk.getY());
        if (gk.getY() > maxY) gk.moveUp(gk.getY() - maxY);
    }

    private void resolvePlayerCollisions() {
        int minDistance = 20; // slightly more than ~18px draw diameter, so they touch but don't overlap

        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                Player a = players.get(i);
                Player b = players.get(j);

                int dx = b.getX() - a.getX();
                int dy = b.getY() - a.getY();
                double distance = Math.sqrt((dx * dx) + (dy * dy));

                // If two players are in exactly the same spot, separate them first
                if (distance == 0) {
                    int push = minDistance / 2;
                    a.moveLeft(push);
                    b.moveRight(push);
                } else if (distance < minDistance) {
                    double overlap = (minDistance - distance) / 2;
                    int pushX = (int) Math.round(overlap * (dx / distance));
                    int pushY = (int) Math.round(overlap * (dy / distance));

                    // push a and b apart along the line connecting them
                    if (pushX > 0) { a.moveLeft(pushX); b.moveRight(pushX); }
                    else if (pushX < 0) { a.moveRight(-pushX); b.moveLeft(-pushX); }

                    if (pushY > 0) { a.moveUp(pushY); b.moveDown(pushY); }
                    else if (pushY < 0) { a.moveDown(-pushY); b.moveUp(-pushY); }
                }
            }
        }
    }

    public void stopSimulation() {
        this.isRunning = false;

        if (simulationThread != null && simulationThread != Thread.currentThread()) {
            simulationThread.interrupt();
        }
    }

    // Called by TimerPanel when the match clock reaches 00:00
    public void finishMatch() {
        stopSimulation();
        pitch.setScore(homeScore, awayScore);
        pitch.setMatchMessage("FULL TIME: HOME " + homeScore + " - " + awayScore + " AWAY");
        pitch.setActors(gameActors);
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public ArrayList<Actor> getGameActors() {
        return this.gameActors;
    }
}

// teammate logic
class Engine extends Match {
    Team teamA;
    Team teamB;
    Match match;
    int maxPlayers = 22;
    private Random random;
    ArrayList<Team> matchPlayers;

    Engine(Team a, Team b, Match m) {
        this.teamA = a;
        this.teamB = b;
        this.match = m;
        this.matchPlayers = new ArrayList<>();
        this.matchPlayers.add(a);
        this.matchPlayers.add(b);
        this.random = new Random();
    }

    void movePlayers() {
        for (Team teams : matchPlayers) {
            for (Player player : teams.getPlayers()) {
                // Shared mathematical engine logic needs to go here
            }
        }
    }
}

class Team {
    private Player[] players;
    private boolean win;

    Team(Player[] p) {
        this.players = p;
        this.win = false;
    }

    public boolean getWin() { return this.win; }
    public void setWin(boolean w) { this.win = w; }
    public Player[] getPlayers() { return players; }
    public void setPlayers(Player[] players) { this.players = players; }
}
