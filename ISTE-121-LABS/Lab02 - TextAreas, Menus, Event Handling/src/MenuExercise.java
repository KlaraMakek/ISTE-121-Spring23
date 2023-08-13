import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 * Stopwatch - starter class to model a stop watch using JavaFX
 * 
 * @author D. Patric
 * @version 2205
 */

public class MenuExercise extends Application implements EventHandler<ActionEvent> {
    // Attributes are GUI components (buttons, text fields, etc.)
    // are declared here.
    private Stage stage; // The entire window, including title bar and borders
    private Scene scene; // Interior of window

    // Text Area
    private TextArea tfArea = new TextArea();
    
    private int counter = 0;
    // Main just instantiates an instance of this GUI class
    public static void main(String[] args) {
        launch(args);
    }

    // Called automatically after launch sets up javaFX
    public void start(Stage _stage) throws Exception {

        stage = _stage; // save stage as an attribute
        stage.setTitle("Menu Exercise"); // set the text in the title bar
        // Create the new Menu
        Menu menu = new Menu("Counter");
        // Crate the new Menu bar
        MenuBar mb = new MenuBar();
        // Crates the items for the menu bar
        MenuItem mbInc = new MenuItem("Increase");
        MenuItem mbDec = new MenuItem("Decrease");
        MenuItem mbRes = new MenuItem("Reset");
        MenuItem mbEx = new MenuItem("Exit");

        // Adds the items to the menu
        menu.getItems().add(mbInc);
        menu.getItems().add(mbDec);
        menu.getItems().add(mbRes);
        menu.getItems().add(mbEx);

        // Adds menu to menubar
        mb.getMenus().add(menu);
        //adding evebnts
        mbInc.setOnAction(this);
        mbDec.setOnAction(this);
        mbRes.setOnAction(this);
        mbEx.setOnAction(this);

        tfArea.setPrefHeight(400);

                // VBox implemet
        VBox root = new VBox(mb);
        // Setting scene
        scene = new Scene(root, 300, 400);
        // with given layout



        //root
        root.getChildren().add(tfArea);
        tfArea.setEditable(false);
        stage.setScene(scene); // associate the scene with the stage
        stage.show();// display the stage (window)
    }

 
    public void handle(ActionEvent evt) {
        // Get the button that was clicked
        MenuItem btn = (MenuItem) evt.getSource();

        // Switch on its name
        switch (btn.getText()) {
            case "Increase":
                doPlus();
                break;
            case "Decrease":
                doMinus();
                break;
            case "Reset":
                doReset();
                break;
            case "Exit":
                doQuit();
                break;
        }
    }

    // When plus is clicked it ads one number
    public void doPlus() {
        counter++;
        tfArea.setText("\n" + counter);
    }

    // When minus clicked it take on number down
    public void doMinus() {
        counter--;
        tfArea.setText("\n" + counter);
    }

    // When resets is clicked it resets the value
    public void doReset() {
        counter = 0;
        tfArea.appendText("\n" + counter);
    }

    // When quit is clicked it quits
    public void doQuit() {
        System.exit(counter);
    }

}
