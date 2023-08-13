import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.geometry.*;

/**
 * Stopwatch - starter class to model a stop watch using JavaFX
 * 
 * @author D. Patric
 * @version 2205
 */

public class Stopwatch extends Application implements EventHandler<ActionEvent> {
   // Attributes are GUI components (buttons, text fields, etc.)
   // are declared here.
   private Stage stage; // The entire window, including title bar and borders
   private Scene scene; // Interior of window
   // Choose a pane ... VBox used here
   private VBox root = new VBox(8);

   FlowPane fpTop = new FlowPane();
   FlowPane fpCenter = new FlowPane();
   FlowPane fpBot = new FlowPane(8, 8);

   // Text field for time
   private TextField tfTime = new TextField();
   private TextArea tfArea = new TextArea();

   // Start button
   private Button btnStart = new Button("Start");

   // Lap button
   private Button btnLap = new Button("Lap");

   // Timer
   private java.util.Timer timer = null;
   private long currentTime = 0L;

   // Main just instantiates an instance of this GUI class
   public static void main(String[] args) {
      launch(args);
   }

   // Called automatically after launch sets up javaFX
   public void start(Stage _stage) throws Exception {
      stage = _stage; // save stage as an attribute
      stage.setTitle("Stop Watch"); // set the text in the title bar

      // Set up TIME in 1st part of VBOX
      tfTime.setPrefWidth(250);
      tfArea.setPrefWidth(250);

      tfArea.setEditable(false);

      tfTime.setAlignment(Pos.CENTER);
      Font currentFont = tfTime.getFont();
      tfTime.setFont(Font.font(currentFont.getName(), FontWeight.BOLD, 48));
      tfTime.setText(
            String.format("%02d:%02d.%d",
                  currentTime / 60000, (currentTime % 60000) / 1000, (currentTime % 1000) / 100));

      // dummy lable
      Label dummy = new Label("     ");

      fpTop.getChildren().add(tfTime); // for time
      fpCenter.getChildren().add(tfArea); // for area
      fpBot.getChildren().addAll(btnLap, btnStart, dummy); // for start button and dummy

      root.getChildren().add(fpTop);
      root.getChildren().add(fpCenter);
      root.getChildren().add(fpBot);
      // Set up button in second part

      fpTop.setAlignment(Pos.CENTER);
      fpCenter.setAlignment(Pos.CENTER);
      fpBot.setAlignment(Pos.BOTTOM_RIGHT);

      // Handle button clicks
      btnStart.setOnAction(this);
      btnLap.setOnAction(
         new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent that){
               doLap();
            }
         }
      );
      btnLap.setVisible(false);// make the button "Lap" invisible

      scene = new Scene(root, 300, 350); // create scene of specified size
                                         // with given layout
      stage.setScene(scene); // associate the scene with the stage
      stage.show();// display the stage (window)
   }

   public void handle(ActionEvent evt) {
      // Get the button that was clicked
      Button btn = (Button) evt.getSource();

      // Switch on its name
      switch (btn.getText()) {
         case "Start":
            doStart();
            break;
         case "Stop":
            doStop();
            break;
         case "Lap":
            doLap();
            break;
      }
   }

   // When start is clicked
   public void doStart() {
      btnStart.setText("Stop");
      currentTime = 0L;
      tfTime.setText(
            String.format("%02d:%02d.%d",
                  currentTime / 60000, (currentTime % 60000) / 1000, (currentTime % 1000) / 100));
      timer = new java.util.Timer();
      timer.scheduleAtFixedRate(new MyTimerTask(), 0, 100);
      // make the button "Lap" visible
      btnLap.setVisible(true);
   }

   // When clicked it prints laps
   public void doLap() {
      tfArea.appendText(tfTime.getText() + "\n");
   }

   // When stop is clicked
   public void doStop() {
      timer.cancel();
      btnStart.setText("Start");
      btnLap.setVisible(false); // make the button "Lap" again invisible
   }

   class MyTimerTask extends java.util.TimerTask {
      public void run() {
         currentTime += 100;
         tfTime.setText(
               String.format("%02d:%02d.%d",
                     currentTime / 60000, (currentTime % 60000) / 1000, (currentTime % 1000) / 100));
      }
   }
}
