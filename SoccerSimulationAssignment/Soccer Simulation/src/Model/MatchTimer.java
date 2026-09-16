package Model;

public class MatchTimer {
    private int seconds;

public MatchTimer(int startingSeconds) {
    seconds = startingSeconds;
}    
public void tick() {
    if (seconds > 0) {
        seconds--;
    }
}
public int getSeconds() {
    return seconds;
}
public String getTime(){
    int minutes = seconds /60;
    int remainingSeconds = seconds % 60;

    return String.format("%02d:%02d", minutes, remainingSeconds);
}
}