/*
WILLIAM IS DOING THIS PART
The gui will be the what displays eveything it ties it all together, 
one feature ive been told to add is while simulation is running a mini print log 

NEEDS:
- main menu
    - background splash graphic 
    - start button 
        - display an overview graphic of pitch with players on it with the current formation (default formation if no changes made)
        - formation change button 
            - list of players for one team
            - displays a graphic of a pitch with symbols of what each player postion is
            - swap team formation button (arrows on each side that swaps between premade formations)
            - swap side button 
                - changes what side the user will play as, also saves the formation of the current team so that users can change what formations each team will use 
            - save button locks in the current settings (also sends the user back, no default back button so the user always saves current formations)
       
        - match settings button 
            - player emotion selection button 
                - three states default, off and extreme
            - simulation type 
                - time limit
                    - match time input selection  
                - goal limit
                    - goal limit input box (sets a max number of goals to be scored for simulation to end)
            - save button locks in the current settings (also sends the user back, no default back button so the user always saves current formations)

        - start match button 
            - begins the simulation by displaying the pitch, players and match stuff
            - menu button in the top corner 
                - pauses the simulation when clicked on
                - resume button
                - exit button 
        - return to main menu button 
    - settings button (doesnt have to be implemented settings)
        - debug mode button
                - makes it run in debug mode
        - save button
    - exit button 

 */

 package Rendering;

 import java.awt.*;
 import java.util.ArrayList;
 
 import javax.swing.*;
 
 import Model.TimerPanel;
 import Model.SoccerPitch;
 import Model.Player;
 import Model.Match;
 
 class UIWindow {
     JFrame frame;
     Menus currentMenu;
     CircleList<String> formationCircle;
     CircleList<String> sidesCircle;
     SoccerPitch pitch;
     VerticalPitch vertPitch;
     boolean debugMode = false;
     String backgroundColour = "#329632";
     String btnColour = "#404143";
 
     public UIWindow() {
         frame = new JFrame("Soccer Simulation");
         frame.setSize(ScreenSize.width, ScreenSize.height);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setLocationRelativeTo(null);
         formationCircle = new CircleList<>(new String[] { "4-4-2", "4-3-3", "3-5-2" });
         sidesCircle = new CircleList<>(new String[] { "Home", "Away" });
         pitch = new SoccerPitch();
         vertPitch = new VerticalPitch();
 
     }
 
     void setMenu(Menus menu) {
         currentMenu = menu;
         frame.setContentPane(menu);
         frame.revalidate();
         frame.repaint();
         frame.getContentPane().setBackground(Color.decode(backgroundColour));
     }
 
     void start() {
         setMenu(new MainMenu(this));
         frame.setVisible(true);
     }
 
     boolean getDebug() {
         return this.debugMode;
     }
 
     void setDebug(boolean b) {
         this.debugMode = b;
         frame.setTitle("Soccer Simulation" + debugString());
     }
 
     private String debugString() {
         if (this.getDebug() == true)
             return " (Debug)";
 
         return "";
     }
 
     void displayButtons(JButton[] buttons) {
         for (JButton btn : buttons) {
             btn.setBackground(Color.decode(btnColour));
             btn.setForeground(Color.WHITE);
             btn.setFont(new Font("Lexend", Font.BOLD, 16));
             btn.setBorderPainted(false);
             btn.setFocusPainted(false);
             btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
         }
     }
 
     void displayCheckBox(JCheckBox[] boxes) {
         for (JCheckBox cbx : boxes) {
             cbx.setBackground(Color.decode(btnColour));
             cbx.setForeground(Color.WHITE);
             cbx.setFont(new Font("Lexend", Font.BOLD, 16));
             cbx.setBorderPainted(false);
             cbx.setFocusPainted(false);
             cbx.setCursor(new Cursor(Cursor.HAND_CURSOR));
         }
     }
 
     void displayLabel(JLabel[] labels) {
         for (JLabel lbl : labels) {
             lbl.setBackground(Color.decode(btnColour));
             lbl.setForeground(Color.WHITE);
             lbl.setFont(new Font("Lexend", Font.BOLD, 16));
             lbl.setOpaque(true);
             lbl.setHorizontalAlignment(JLabel.CENTER);
             lbl.setVerticalAlignment(JLabel.CENTER);
         }
     }
 
     
 
 }
 
 /*
  * DONE
  */
 abstract class Menus extends JPanel {
     protected UIWindow window;
 
     Menus(UIWindow window) {
         this.window = window;
     }
 
     abstract void next1();
 
     abstract void next2();
 
     abstract void next3();
 
     abstract void back();
 }
 
 /*
  * DONE
  */
 class MainMenu extends Menus {
     MainMenu(UIWindow window) {
         super(window);
         setLayout(null);
         window.pitch.setBounds(0, 0, ScreenSize.width, ScreenSize.height);
 
         JButton startButton = new JButton("Start");
         startButton.addActionListener(e -> next1());
 
         JButton settingsButton = new JButton("Settings");
         settingsButton.addActionListener(e -> next2());
 
         JButton exitButton = new JButton("Exit");
         exitButton.addActionListener(e -> back());
 
         JButton[] buttons = { startButton, settingsButton, exitButton };
 
         window.displayButtons(buttons);
 
         startButton.setBounds(ScreenSize.width / 2 - 125, ScreenSize.height / 2 - 100, 250, 50);
         settingsButton.setBounds(ScreenSize.width / 2 - 125, ScreenSize.height / 2 - 30, 250, 50);
         exitButton.setBounds(ScreenSize.width / 2 - 125, ScreenSize.height / 2 + 40, 250, 50);
 
         add(startButton);
         add(settingsButton);
         add(exitButton);
         add(window.pitch);
     }
 
     @Override
     void next1() {
         window.setMenu(new StartMenu(window));
     }
 
     @Override
     void next2() {
         window.setMenu(new SettingsMenu(window));
     }
 
     @Override
     void next3() {
     }
 
     @Override
     void back() {
         System.exit(0);
     }
 
 }
 
 /*
  * DONE
  */
 class StartMenu extends Menus {
     StartMenu(UIWindow window) {
         super(window);
         setLayout(null);
         window.vertPitch.setBounds(0, 0, ScreenSize.width, ScreenSize.height);
 
         JButton startButton = new JButton("Start");
         startButton.addActionListener(e -> next1());
 
         JButton formationButton = new JButton("Formation Settings");
         formationButton.addActionListener(e -> next2());
 
         JButton matchSettingsButton = new JButton("Match Settings");
         matchSettingsButton.addActionListener(e -> next3());
 
         JButton exitButton = new JButton("Back");
         exitButton.addActionListener(e -> back());
 
         JButton[] buttons = { startButton, formationButton, matchSettingsButton, exitButton };
 
         window.displayButtons(buttons);
 
         startButton.setBounds(ScreenSize.width / 4 - 125, ScreenSize.height / 2 - 140, 250, 50);
         formationButton.setBounds(ScreenSize.width / 4 - 125, ScreenSize.height / 2 - 70, 250, 50);
         matchSettingsButton.setBounds(ScreenSize.width / 4 - 125, ScreenSize.height / 2, 250, 50);
         exitButton.setBounds(ScreenSize.width / 4 - 125, ScreenSize.height / 2 + 70, 250, 50);
 
         add(startButton);
         add(formationButton);
         add(matchSettingsButton);
         add(exitButton);
         add(window.vertPitch);
 
         window.vertPitch.drawPlayers(window);
 
     }
 
     @Override
     void next1() {
         window.setMenu(new SimWindow(window));
     }
 
     @Override
     void next2() {
         window.setMenu(new FormationMenu(window));
     }
 
     @Override
     void next3() {
         window.setMenu(new MatchSettingsMenu(window));
     }
 
     @Override
     void back() {
         window.setMenu(new MainMenu(window));
     }
 
 }
 
 /*
  * NEEDS, to have the debug output make a pop-up window that prints the log of
  * everything that happens
  */
 class SettingsMenu extends Menus {
     SettingsMenu(UIWindow window) {
         super(window);
         setLayout(null);
 
         JCheckBox debugButton = new JCheckBox("Debug Mode", window.getDebug());
         debugButton.addActionListener(e -> window.setDebug(debugButton.isSelected()));
 
         JButton exitButton = new JButton("Save");
         exitButton.addActionListener(e -> back());
 
         JButton[] buttons = { exitButton };
         JCheckBox[] boxes = { debugButton };
         window.displayCheckBox(boxes);
         window.displayButtons(buttons);
 
         debugButton.setBounds(ScreenSize.width / 2 - 125, ScreenSize.height / 2 - 100, 250, 50);
         exitButton.setBounds(ScreenSize.width / 2 - 125, ScreenSize.height / 2 - 30, 250, 50);
 
         add(debugButton);
         add(exitButton);
     }
 
     @Override
     void next1() {
     }
 
     @Override
     void next2() {
 
     }
 
     @Override
     void next3() {
 
     }
 
     @Override
     void back() {
         window.setMenu(new MainMenu(window));
     }
 }
 
 /*
  * DONE
  */
 class FormationMenu extends Menus {
     CircleList<String> formationCircle;
     CircleList<String> sidesCircle;
 
     FormationMenu(UIWindow window) {
         super(window);
         setLayout(null);
         window.vertPitch.setBounds(0, 0, 700, 400);
 
         this.formationCircle = window.formationCircle;
         this.sidesCircle = window.sidesCircle;
         JButton formPrevBtn = new JButton("<");
         JButton formNextBtn = new JButton(">");
 
         JLabel formationLabel = new JLabel(formationCircle.getCurrent().toString());
 
         formPrevBtn.addActionListener(e -> {
             formationCircle.previous();
             formationLabel.setText(formationCircle.getCurrent().toString());
             window.vertPitch.drawPlayers(window);
         });
 
         formNextBtn.addActionListener(e -> {
             formationCircle.next();
             formationLabel.setText(formationCircle.getCurrent().toString());
             window.vertPitch.drawPlayers(window);
         });
 
         JButton swapPrevBtn = new JButton("<");
         JButton swapNextBtn = new JButton(">");
 
         JLabel swapLabel = new JLabel(sidesCircle.getCurrent().toString());
 
         swapPrevBtn.addActionListener(e -> {
             window.vertPitch.swapSides(window);
             swapLabel.setText(sidesCircle.getCurrent().toString());
             window.vertPitch.drawPlayers(window);
         });
 
         swapNextBtn.addActionListener(e -> {
             window.vertPitch.swapSides(window);
             swapLabel.setText(sidesCircle.getCurrent().toString());
             window.vertPitch.drawPlayers(window);
         });
 
         JButton exitButton = new JButton("Save");
         exitButton.addActionListener(e -> back());
 
         JButton[] buttons = { formPrevBtn, formNextBtn, swapPrevBtn, swapNextBtn, exitButton };
         JLabel[] labels = { formationLabel, swapLabel };
         window.displayLabel(labels);
 
         window.displayButtons(buttons);
 
         formPrevBtn.setBounds(ScreenSize.width / 4 - 125, ScreenSize.height / 2 - 140, 45, 50);
         formationLabel.setBounds(ScreenSize.width / 4 - 75, ScreenSize.height / 2 - 140, 150, 50);
         formNextBtn.setBounds(ScreenSize.width / 4 + 80, ScreenSize.height / 2 - 140, 45, 50);
 
         swapPrevBtn.setBounds(ScreenSize.width / 4 - 125, ScreenSize.height / 2 - 70, 45, 50);
         swapLabel.setBounds(ScreenSize.width / 4 - 75, ScreenSize.height / 2 - 70, 150, 50);
         swapNextBtn.setBounds(ScreenSize.width / 4 + 80, ScreenSize.height / 2 - 70, 45, 50);
 
         exitButton.setBounds(ScreenSize.width / 4 - 125, ScreenSize.height / 2, 250, 50);
 
         add(formPrevBtn);
         add(formationLabel);
         add(formNextBtn);
 
         add(swapPrevBtn);
         add(swapLabel);
         add(swapNextBtn);
 
         add(exitButton);
 
         add(window.vertPitch);
 
         window.vertPitch.drawPlayers(window);
     }
 
     @Override
     void next1() {
     }
 
     @Override
     void next2() {
 
     }
 
     @Override
     void next3() {
 
     }
 
     @Override
     void back() {
         window.setMenu(new StartMenu(window));
     }
 }
 
 /*
  * DONE
  */
 class MatchSettingsMenu extends Menus {
     MatchSettingsMenu(UIWindow window) {
         super(window);
         setLayout(null);
 
         JButton exitButton = new JButton("Save");
         exitButton.addActionListener(e -> back());
 
         JButton[] buttons = { exitButton };
 
         window.displayButtons(buttons);
 
         exitButton.setBounds(ScreenSize.width / 2 - 125, ScreenSize.height / 2 - 100, 250, 50);
 
         add(exitButton);
     }
 
     @Override
     void next1() {
         // empty for the time being
     }
 
     @Override
     void next2() {
         // empty for the time being
 
     }
 
     @Override
     void next3() {
         // empty for the time being
     }
 
     @Override
     void back() {
         window.setMenu(new StartMenu(window));
     }
 }
 
 /*
  * NEEDS, this is temporary set up need to make it play the actual match
  * simulation but
  * i got to build that first so this is just a place holder
  * 
  */
 class SimWindow extends Menus {
     Match match;
     SimWindow(UIWindow window) {
         super(window);
         setLayout(null);
 
         match = new Match();
 
         match.setupAndStartSimulation(window.formationCircle.getCurrent(), window.formationCircle.getCurrent());
 
 
         JButton menuButton = new JButton("Menu");
         menuButton.addActionListener(e -> back());
 
         JButton[] buttons = { menuButton };
 
         window.displayButtons(buttons);
 
         menuButton.setBounds(ScreenSize.width / 5 - 125, ScreenSize.height - 385, 80, 40);
         
         TimerPanel timerPanel = new TimerPanel();
         timerPanel.setBounds(ScreenSize.width / 2 - 75, 20, 150, 40);
 
         match.pitch.setBounds(0, 0, ScreenSize.width, ScreenSize.height);
         add(timerPanel);
         add(menuButton);
         add(match.pitch);
 
     }
 
     // Overlapping children (the full-screen pitch sits behind the timer/menu)
     // need this override, otherwise Swing assumes nothing overlaps and skips
     // repainting the timer label whenever the pitch redraws underneath it —
     // that's what was causing the timer to blink in and out.
     @Override
     public boolean isOptimizedDrawingEnabled() {
         return false;
     }
 
     @Override
     void next1() {
         // empty for the time being
     }
 
     @Override
     void next2() {
         // empty for the time being
 
     }
 
     @Override
     void next3() {
         // empty for the time being
     }
 
     @Override
     void back() {
         window.setMenu(new SimWindowMenu(window));
     }
 }
 
 /*
  * NEEDS to pause the simulation then start it again just go to test it does
  * that after i implement the match class
  * DEPENDENT on MATCH class
  */
 class SimWindowMenu extends Menus {
     SimWindowMenu(UIWindow window) {
         super(window);
         setLayout(null);
 
         JButton resumeButton = new JButton("Resume");
         resumeButton.addActionListener(e -> next1());
 
         JButton exitButton = new JButton("Exit");
         exitButton.addActionListener(e -> back());
 
         JButton[] buttons = { resumeButton, exitButton };
 
         window.displayButtons(buttons);
 
         resumeButton.setBounds(ScreenSize.width / 2 - 75, ScreenSize.height / 2 - 100, 150, 50);
         exitButton.setBounds(ScreenSize.width / 2 - 75, ScreenSize.height / 2 - 30, 150, 50);
 
         add(resumeButton);
         add(exitButton);
     }
 
     @Override
     void next1() {
         window.setMenu(new SimWindow(window));
     }
 
     @Override
     void next2() {
         // empty for the time being
 
     }
 
     @Override
     void next3() {
         // empty for the time being
     }
 
     @Override
     void back() {
         window.setMenu(new StartMenu(window));
     }
 }
 
 /*
  * DONE
  */
 class CircleNode<T> {
     T value;
     CircleNode<T> next;
     CircleNode<T> prev;
 
     CircleNode(T value) {
         this.value = value;
     }
 }
 
 /*
  * DONE
  */
 class CircleList<T> {
     private CircleNode<T> current;
 
     CircleList(T[] values) {
         CircleNode<T> start = new CircleNode<T>(values[0]);
         CircleNode<T> prevNode = start;
 
         for (int i = 1; i < values.length; i++) {
             CircleNode<T> node = new CircleNode<>(values[i]);
             prevNode.next = node;
             node.prev = prevNode;
             prevNode = node;
         }
 
         prevNode.next = start;
         start.prev = prevNode;
 
         this.current = start;
 
     }
 
     T getCurrent() {
         return current.value;
     }
 
     void next() {
         this.current = this.current.next;
     }
 
     void previous() {
         this.current = this.current.prev;
     }
 }
 
 class VerticalPitch extends JPanel {
     private int width;
     private int height;
     private ArrayList<Player> players;
     private Color sideColour = Color.BLUE;
     private int rectX, rectY, rectW, rectH;
 
     VerticalPitch() {
         setPreferredSize(new Dimension(ScreenSize.width, ScreenSize.height));
         setOpaque(false);
         this.height = ScreenSize.height / 2 + 130;
         this.width = (int) (this.height * 0.65);
         players = new ArrayList<>();
 
         rectX = ScreenSize.width - width - 50;
         rectY = height - 310;
         rectW = width;
         rectH = height;
 
     }
 
     @Override
     protected void paintComponent(Graphics g) {
         super.paintComponent(g);
 
         g.setColor(new Color(50, 150, 50));
         g.fillRect(rectX, rectY, rectW, rectH);
 
         g.setColor(Color.WHITE);
         g.drawRect(rectX, rectY, rectW, rectH);
         g.drawLine(rectX, rectY + rectH / 2, rectX + rectW, rectY + rectH / 2);
 
         int penaltySpan = (int) (rectW * 0.59);
         int penaltyDepth = (int) (rectH * 0.16);
         int penaltyX = rectX + (rectW - penaltySpan) / 2;
         g.drawRect(penaltyX, rectY, penaltySpan, penaltyDepth);
         g.drawRect(penaltyX, rectY + rectH - penaltyDepth, penaltySpan, penaltyDepth);
 
         int centerX = rectX + rectW / 2;
         int centerY = rectY + rectH / 2;
         int circleRadius = (int) (rectW * 0.135);
         g.drawOval(centerX - circleRadius, centerY - circleRadius, circleRadius * 2, circleRadius * 2);
         g.fillOval(centerX - 4, centerY - 4, 8, 8);
 
         int goalSpan = (int) (rectW * 0.27);
         int goalDepth = (int) (rectH * 0.05);
         int goalX = rectX + (rectW - goalSpan) / 2;
         g.drawRect(goalX, rectY, goalSpan, goalDepth);
         g.drawRect(goalX, rectY + rectH - goalDepth, goalSpan, goalDepth);
 
         int spotOffset = (int) (rectH * 0.105);
         g.fillOval(centerX - 4, rectY + spotOffset - 4, 8, 8);
         g.fillOval(centerX - 4, rectY + rectH - spotOffset - 4, 8, 8);
 
         for (Player p : players) {
             p.draw(g);
         }
     }
 
     private void addPlayer(int jerseyNumber, double nx, double ny) {
         int x = rectX + (int) (nx * rectW);
         int y = rectY + (int) (ny * rectH);
         players.add(new Player(x, y, jerseyNumber, sideColour));
     }
 
     void drawPlayers(UIWindow window) {
         players.clear(); // avoid stacking duplicates on repeated calls
 
         String formation = window.formationCircle.getCurrent();
 
         addPlayer(1, 0.5, 0.92); // goalkeeper, same for every formation
 
         if (formation.equals("4-4-2")) {
             addPlayer(2, 0.15, 0.75);
             addPlayer(3, 0.38, 0.75);
             addPlayer(4, 0.62, 0.75);
             addPlayer(5, 0.85, 0.75);
             addPlayer(6, 0.15, 0.50);
             addPlayer(7, 0.38, 0.50);
             addPlayer(8, 0.62, 0.50);
             addPlayer(9, 0.85, 0.50);
             addPlayer(10, 0.35, 0.22);
             addPlayer(11, 0.65, 0.22);
 
         } else if (formation.equals("4-3-3")) {
             addPlayer(2, 0.15, 0.75);
             addPlayer(3, 0.38, 0.75);
             addPlayer(4, 0.62, 0.75);
             addPlayer(5, 0.85, 0.75);
             addPlayer(6, 0.25, 0.50);
             addPlayer(7, 0.50, 0.50);
             addPlayer(8, 0.75, 0.50);
             addPlayer(9, 0.15, 0.20);
             addPlayer(10, 0.50, 0.18);
             addPlayer(11, 0.85, 0.20);
 
         } else if (formation.equals("3-5-2")) {
             addPlayer(2, 0.25, 0.78);
             addPlayer(3, 0.50, 0.78);
             addPlayer(4, 0.75, 0.78);
             addPlayer(5, 0.08, 0.52);
             addPlayer(6, 0.30, 0.52);
             addPlayer(7, 0.50, 0.52);
             addPlayer(8, 0.70, 0.52);
             addPlayer(9, 0.92, 0.52);
             addPlayer(10, 0.35, 0.22);
             addPlayer(11, 0.65, 0.22);
         }
 
         repaint();
     }
 
     void swapSides(UIWindow window) {
         if (window.sidesCircle.getCurrent().equals("Home")) {
             sideColour = Color.RED;
             window.sidesCircle.next();
         } else {
             sideColour = Color.BLUE;
             window.sidesCircle.next();
         }
 
     }
 
 }
 
 public class SimulationUI {
 
     public static void main(String[] args) {
         new UIWindow().start();
     }
 
 }