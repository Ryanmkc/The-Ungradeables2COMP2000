package Model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;

import Rendering.ScreenSize;

public class SoccerPitch extends JPanel {

    public static final int PENALTY_WIDTH = 100;
    public static final int PENALTY_HEIGHT = 200;
    public static final int GOAL_WIDTH = 50;
    public static final int GOAL_HEIGHT = 100;

    private static final class ActorFrame {
        private final int x;
        private final int y;
        private final Color color;

        private ActorFrame(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }

    private volatile List<ActorFrame> actorFrames = Collections.emptyList();
    private volatile int homeScore = 0;
    private volatile int awayScore = 0;
    private volatile String matchMessage = "";

    public SoccerPitch() {
        setPreferredSize(new Dimension(ScreenSize.width, ScreenSize.height));
    }

    public void setActors(List<Actor> actors) {
        List<ActorFrame> nextFrame = new ArrayList<>(actors.size());

        for (Actor actor : actors) {
            nextFrame.add(new ActorFrame(
                    actor.getX(), actor.getY(), actor.getColor()));
        }

        // Publish the whole frame after all actor positions have been copied.
        actorFrames = Collections.unmodifiableList(nextFrame);
        repaint();
    }

    public void setScore(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        repaint();
    }

    public void setMatchMessage(String matchMessage) {
        this.matchMessage = matchMessage == null ? "" : matchMessage;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        List<ActorFrame> frame = actorFrames;
        String message = matchMessage;

        g.setColor(new Color(50, 150, 50));
        g.fillRect(0, 0, ScreenSize.width, ScreenSize.height);

        g.setColor(Color.WHITE);

        // Outer border
        g.drawRect(0, 0, ScreenSize.width, ScreenSize.height);

        // Halfway line
        g.drawLine(ScreenSize.width / 2, 0, ScreenSize.width / 2, ScreenSize.height);

        // Centre circle
        int circleRadius = 60;
        g.drawOval(ScreenSize.width / 2 - circleRadius,
                ScreenSize.height / 2 - circleRadius, circleRadius * 2, circleRadius * 2);

        // Centre spot
        g.fillOval(ScreenSize.width / 2 - 4, ScreenSize.height / 2 - 4, 8, 8);

        // Penalty areas
        int penaltyY = (ScreenSize.height - PENALTY_HEIGHT) / 2;
        g.drawRect(0, penaltyY, PENALTY_WIDTH, PENALTY_HEIGHT);
        g.drawRect(ScreenSize.width - PENALTY_WIDTH, penaltyY, PENALTY_WIDTH, PENALTY_HEIGHT);

        // Goals
        int goalY = (ScreenSize.height - GOAL_HEIGHT) / 2;
        g.drawRect(0, goalY, GOAL_WIDTH, GOAL_HEIGHT);
        g.drawRect(ScreenSize.width - GOAL_WIDTH, goalY, GOAL_WIDTH, GOAL_HEIGHT);

        // Penalty spots
        int spotOffset = 79;
        g.fillOval(spotOffset - 4, ScreenSize.height / 2 - 4, 8, 8);
        g.fillOval(ScreenSize.width - spotOffset - 4, ScreenSize.height / 2 - 4, 8, 8);

        // Players and ball from the published frame
        for (ActorFrame actor : frame) {
            g.setColor(actor.color);
            g.fillOval(actor.x - 9, actor.y - 9, 18, 18);
        }

        // Scoreboard
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        String scoreText = "HOME  " + homeScore + "  -  " + awayScore + "  AWAY";
        FontMetrics scoreMetrics = g.getFontMetrics();
        int scoreX = (ScreenSize.width - scoreMetrics.stringWidth(scoreText)) / 2;
        g.drawString(scoreText, scoreX, 88);

        // Temporary goal message or final full-time result
        if (!message.isEmpty()) {
            g.setFont(new Font("Arial", Font.BOLD, 28));
            FontMetrics messageMetrics = g.getFontMetrics();
            int messageWidth = messageMetrics.stringWidth(message);
            int messageX = (ScreenSize.width - messageWidth) / 2;
            int messageY = ScreenSize.height / 2;

            g.setColor(new Color(0, 0, 0, 170));
            g.fillRoundRect(messageX - 18, messageY - 34, messageWidth + 36, 48, 16, 16);
            g.setColor(Color.WHITE);
            g.drawString(message, messageX, messageY);
        }
    }
}