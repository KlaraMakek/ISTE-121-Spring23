
/**
* JavaFX Symple GUI, example 1.
* 
* @author Alan Mutka
* @version 2225
*/

/**
 * Java counter/info saver
 * 
 * @author Klara Makek
 * @version 27.1.2023.
 * 
 * ISTE 121 - HW1
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.event.*;
import javafx.geometry.*;

public class Orders extends Application implements EventHandler<ActionEvent> {
    public static void main(String[] args) {
        // method inside the Application class, it will setup our program as a JavaFX
        // application
        // then the JavaFX is ready, the "start" method will be called automatically
        launch(args);
    }

    private TextField name = null;
    private TextField num = null;
    private TextField cos = null;
    private TextField amount = null;

    // scanner for load
    Scanner scan = null;
    private ArrayList<String> saved;
    private int counter = 0;

    @Override
    public void start(Stage _stage) throws Exception {

        ///////////////////////// Setting window properties
        // set the window title
        _stage.setTitle("Welcome to JavaFX");

        /*
         * // create the Layout where we can put scene elements, the main layout
         * // layout are used for automatic positioning elements inside the scene, for
         * // example, TOP, LEFT, RIGHT, CENTER
         * BorderPane root = new BorderPane();
         */

        // HBox root = new HBox(8);// DAY02 - HBOX
        VBox root = new VBox(8);
        root.setAlignment(Pos.CENTER);

        // the thing
        GridPane topPane = new GridPane();
        topPane.setAlignment(Pos.CENTER);
        topPane.setHgap(8);
        topPane.setVgap(1);
        // labels; name, number, cost and amount
        Label nameLabel = new Label("Item name:");
        name = new TextField();
        Label numLabel = new Label("Number:");
        num = new TextField();

        Label cosLabel = new Label("Cost:");
        cos = new TextField();

        Label amountLabel = new Label("Amount owned:");
        amount = new TextField();
        amount.setEditable(false);

        // to make it to do the thingy thing idk
        GridPane.setHalignment(nameLabel, HPos.RIGHT);
        GridPane.setHalignment(numLabel, HPos.RIGHT);
        GridPane.setHalignment(cosLabel, HPos.RIGHT);
        GridPane.setHalignment(amountLabel, HPos.RIGHT);

        // to make it be there
        topPane.addRow(0, nameLabel, name);
        topPane.addRow(1, numLabel, num);
        topPane.addRow(2, cosLabel, cos);
        topPane.addRow(3, amountLabel, amount);

        // buttons
        FlowPane button = new FlowPane(10, 0);
        FlowPane button2 = new FlowPane(10, 0);
        button.setAlignment(Pos.CENTER);
        button2.setAlignment(Pos.CENTER);
        Button buttonCalculate = new Button("Calculate");
        Button buttonSave = new Button("Save");
        Button buttonClear = new Button("Clear");
        Button buttonExit = new Button("Exit");

        // second buttton
        Button buttonLoad = new Button("Load");
        Button buttonNext = new Button("Next>");
        Button buttonPrev = new Button("<Prev");
        button.getChildren().addAll(buttonCalculate, buttonClear, buttonSave, buttonExit);
        button2.getChildren().addAll(buttonLoad, buttonNext, buttonPrev);

        // !must implement
        // *implemented
        buttonCalculate.setOnAction(this);
        buttonClear.setOnAction(this);
        buttonSave.setOnAction(this);
        buttonExit.setOnAction(this);

        // second button inplement
        buttonLoad.setOnAction(this);
        buttonNext.setOnAction(this);
        buttonPrev.setOnAction(this);

        //! tool tip, shows the button description
        buttonCalculate.setTooltip(new Tooltip("It calculates the amout."));
        buttonClear.setTooltip(new Tooltip("It clears the text blocks."));
        buttonSave.setTooltip(new Tooltip("It saves the info in file."));
        buttonExit.setTooltip(new Tooltip("It exists the window."));
        buttonLoad.setTooltip(new Tooltip("It makes the info load."));
        buttonNext.setTooltip(new Tooltip("It shows next loaded informatio."));
        buttonPrev.setTooltip(new Tooltip("It shows previus loaded informatio."));


        root.getChildren().addAll(topPane, button, button2);

        // create a scene with a specific size (width, height), connnect with the layout
        Scene scene = new Scene(root, 500, 250);

        // connect stage with the Scene and show it, finalization
        _stage.setScene(scene);
        _stage.show();
    }

    // to find and read from the given file
    private void write() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(new File("121Lab1.csv"), true));
            String NamePW = name.getText();
            String numPW = num.getText();
            String cosPW = cos.getText();
            String amountPW = amount.getText();
            pw.printf("\n\"%s\", %s, %s, %s", NamePW, numPW, cosPW, amountPW);
            pw.flush();
            pw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // to make buttons work

    @Override
    public void handle(ActionEvent event) {
        Button button = (Button) event.getSource();
        // exit button
        switch (button.getText()) {
            case "Exit":
                System.exit(0);
                break;
            // clears the info in boxes
            case "Clear":
                name.setText(null);
                num.setText(null);
                cos.setText(null);
                amount.setText("");
                break;
            // to calculate the amount
            case "Calculate":
                String StrNum = num.getText();
                int intBr = Integer.parseInt(StrNum);
                String StrCos = cos.getText();
                double DobCos = Double.parseDouble(StrCos);

                // math for the amout
                double Amount = intBr * DobCos;
                amount.setText(String.format("%.2f", Amount));
                break;
            // to save it in the designated file
            case "Save":
                String StrNumS = num.getText();
                int intBrS = Integer.parseInt(StrNumS);
                String StrCosS = cos.getText();
                double DobCosS = Double.parseDouble(StrCosS);
                // math for the amout
                double AmountS = intBrS * DobCosS;
                amount.setText(String.format("%.2f", AmountS));
                // writes it in the file
                write();
                break;
            // to load the info from file
            case "Load":
                try {
                    // scanns the file
                    scan = new Scanner(new FileReader(new File("./121Lab1.csv")));

                    // array list
                    saved = new ArrayList<String>();
                    while (scan.hasNextLine()) {
                        String line = scan.nextLine();
                        saved.add(line);
                    }
                    Alert notifAlert = new Alert(AlertType.INFORMATION, "" + saved.size());
                    notifAlert.setHeaderText("File");
                    notifAlert.showAndWait();

                } catch (FileNotFoundException e) {
                    Alert notifAlert = new Alert(AlertType.ERROR, "" + e);
                    notifAlert.show();
                    e.printStackTrace();
                    counter--;
                } finally {
                    if (scan != null) {
                        scan.close();
                        counter = +saved.size();
                    }
                }
                // to load next info
            case "Next>":
                counter++;
                // if saved size on lines in the file is less and or equal to counter then...
                if (saved.size() <= counter) {
                    String line = saved.get(counter);
                    String elemt[] = line.split(",");
                    // if lenght is 4 then we load
                    if (elemt.length == 4) {
                        this.name.setText(elemt[0]);
                        this.num.setText(elemt[1]);
                        this.cos.setText(elemt[2]);
                        this.amount.setText(elemt[3]);
                        // else show error
                    } else {
                        Alert notifAlert = new Alert(AlertType.ERROR, "ERROR");
                        notifAlert.show();
                    }
                }
                // if saved size of lines in the file is biget then counter...
                if (saved.size() > counter) {
                    counter++; // counter adds 1
                } else {// else error if we try to go after last saved info
                    Alert notifAlert = new Alert(AlertType.INFORMATION, "Thats the last saved info.");
                    notifAlert.show();
                }

            case "<Prev":
                counter--;
                // if counter is biget and or equal to 0 then...
                if (counter >= 0) {
                    String line = saved.get(counter);
                    String elemt[] = line.split(",");
                    // if 4 then good
                    if (elemt.length == 4) {
                        this.name.setText(elemt[0]);
                        this.num.setText(elemt[1]);
                        this.cos.setText(elemt[2]);
                        this.amount.setText(elemt[3]);
                    } else { // else error
                        Alert notifAlert = new Alert(AlertType.ERROR, "ERROR");
                        notifAlert.show();
                    }
                } else {// else message if we try to go before first saved info
                    Alert notifAlert = new Alert(AlertType.INFORMATION, "Thats the first saved info.");
                    notifAlert.show();
                    counter = -saved.size(); //
                }
        }

    }

}
