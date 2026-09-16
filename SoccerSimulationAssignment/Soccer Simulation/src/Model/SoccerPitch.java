package Model;

import java.awt.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

import Rendering.ScreenSize;

public class SoccerPitch extends JPanel {

    public static final int PENALTY_WIDTH = 100;
    public static final int PENALTY_HEIGHT = 200;
    public static final int GOAL_WIDTH = 50;
    public static final int GOAL_HEIGHT = 100;

    private List<Actor> actors = new ArrayList<>();
    private volatile int homeScore = 0;
    private volatile int awayScore = 0;
    private volatile String matchMessage = "";

    public SoccerPitch() {
        setPreferredSize(new Dimension(ScreenSize.width, ScreenSize.height));
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
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

        // Actors (players + ball)
        for (Actor a : actors) {
            g.setColor(a.getColor());
            g.fillOval(a.getX() - 9, a.getY() - 9, 18, 18);
        }

        // Scoreboard
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        String scoreText = "HOME  " + homeScore + "  -  " + awayScore + "  AWAY";
        FontMetrics scoreMetrics = g.getFontMetrics();
        int scoreX = (ScreenSize.width - scoreMetrics.stringWidth(scoreText)) / 2;
        g.drawString(scoreText, scoreX, 88);

        // Temporary goal message or final full-time result
        if (!matchMessage.isEmpty()) {
            g.setFont(new Font("Arial", Font.BOLD, 28));
            FontMetrics messageMetrics = g.getFontMetrics();
            int messageWidth = messageMetrics.stringWidth(matchMessage);
            int messageX = (ScreenSize.width - messageWidth) / 2;
            int messageY = ScreenSize.height / 2;

            g.setColor(new Color(0, 0, 0, 170));
            g.fillRoundRect(messageX - 18, messageY - 34, messageWidth + 36, 48, 16, 16);
            g.setColor(Color.WHITE);
            g.drawString(matchMessage, messageX, messageY);
        }
    }
}
