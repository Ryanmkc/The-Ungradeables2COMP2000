package Model;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class TimerPanel extends JPanel {
    private MatchTimer matchTimer;
    private JLabel timerLabel;
    private Timer timer;
    private Match match;

    public TimerPanel(){
        this(null, 90);
    }

    public TimerPanel(Match match) {
        this(match, 90);
    }

    public TimerPanel(Match match, int startingSeconds) {
        this.match = match;
        matchTimer = new MatchTimer(startingSeconds);
        timerLabel = new JLabel(matchTimer.getTime(), SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerLabel.setForeground(Color.BLACK);
        add(timerLabel);

        timer = new Timer(1000, e -> {
            matchTimer.tick();
            timerLabel.setText(matchTimer.getTime());

            if (matchTimer.getSeconds() == 0) {
                timer.stop();

                if (match != null) {
                    match.finishMatch();
                }
            }
        });

        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }

    public int getSecondsRemaining() {
        return matchTimer.getSeconds();
        }
    }
