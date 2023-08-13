import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;

/**
 * Counter 2 / part 2
 * 
 * @author Klara Makek
 * @version 30.1.2023.
 * ISTE 121 - LAB
 */

public class Counter extends Application implements EventHandler<ActionEvent> {
    // Attributes are GUI components (buttons, text fields, etc.)
    // are declared here.
    private Stage stage; // The entire window, including title bar and borders
    private Scene scene; // Interior of window
    // Choose a pane ... VBox used here
    private VBox root = new VBox(8);

    private int counter = 0;

    FlowPane fpTop = new FlowPane();
    FlowPane fpCenter = new FlowPane();
    FlowPane fpBot = new FlowPane(8, 8);

    // Text field for Input and area fo button
    private TextField tfInput = new TextField();
    private TextArea tfArea = new TextArea();

    Label lable2 = new Label("Current Value: ");

    // Plus button
    private Button btnPlus = new Button("+");

    // Minus button
    private Button btnMinus = new Button("-");

    // Reset button
    private Button btnReset = new Button("Reset");

    // Quit button
    private Button btnQuit = new Button("Quit");


    // Main just instantiates an instance of this GUI class
    public static void main(String[] args) {
        launch(args);
    }

    // Called automatically after launch sets up javaFX
    public void start(Stage _stage) throws Exception {
        stage = _stage; // save stage as an attribute
        stage.setTitle("Exercise 2 part 1"); // set the text in the title bar

        // Set up counter in 1st part of VBOX
        tfInput.setPrefWidth(50);
        tfArea.setPrefWidth(250);

        tfArea.setEditable(false);

        tfInput.setAlignment(Pos.TOP_RIGHT);
        lable2.setAlignment(Pos.TOP_LEFT);

        tfInput.setEditable(false);

        fpTop.getChildren().addAll(lable2, tfInput); // for time
        fpBot.getChildren().addAll(btnPlus, btnMinus, btnReset, btnQuit); // for start button and dummy

        root.getChildren().add(fpTop);
        root.getChildren().add(fpBot);
        // Set up button in second part

        fpTop.setAlignment(Pos.CENTER);
        fpBot.setAlignment(Pos.CENTER);

        // Handle button clicks
        btnPlus.setOnAction(this);
        btnMinus.setOnAction(this);
        btnReset.setOnAction(this);
        btnQuit.setOnAction(this);

        scene = new Scene(root, 300, 60); // create scene of specified size
                                          // with given layout
        stage.setScene(scene); // associate the scene with the stage
        stage.show();// display the stage (window)
    }

    public void handle(ActionEvent evt) {
        // Get the button that was clicked
        Button btn = (Button) evt.getSource();

        // Switch on its name
        switch (btn.getText()) {
            case "+":
                doPlus();
                break;
            case "-":
                doMinus();
                break;
            case "Reset":
                doReset();
                break;
            case "Quit":
                doQuit();
                break;
        }
    }

    // When plus is clicked it ads one number
    public void doPlus() {
        counter++;
        tfInput.setText("" + counter);
    }

    // When minus clicked it take on number down
    public void doMinus() {
        counter--;
        tfInput.setText("" + counter);
    }

    // When resets is clicked it resets the value
    public void doReset() {
        counter = 0;
        tfInput.setText("" + counter);
    }

    // When quit is clicked it quits
    public void doQuit() {
        System.exit(counter);
    }

}
