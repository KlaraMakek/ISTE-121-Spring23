
/**
* JavaFX Symple GUI, example 1.
* 
* @author Alan Mutka
* @version 2225
*/
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
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
        amount = new TextField();

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
        button.setAlignment(Pos.CENTER);
        Button buttonCalculate = new Button("Calculate");
        Button buttonSave = new Button("Save");
        Button buttonClear = new Button("Clear");
        Button buttonExit = new Button("Exit");
        button.getChildren().addAll(buttonCalculate, buttonClear, buttonSave, buttonExit);

        // !must implement
        // *implemented
        buttonCalculate.setOnAction(this);
        buttonClear.setOnAction(this);
        buttonSave.setOnAction(this);
        buttonExit.setOnAction(this);

        root.getChildren().addAll(topPane, button);

        // create a scene with a specific size (width, height), connnect with the layout
        Scene scene = new Scene(root, 500, 250);

        // connect stage with the Scene and show it, finalization
        _stage.setScene(scene);
        _stage.show();
    }

    private void write() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(new File("121Lab1.csv"), true));
            String NamePW = name.getText();
            String numPW = num.getText();
            String cosPW = cos.getText();
            String amountPW = amount.getText();
            pw.printf("\nItem name:\"%s\", \nNumber: %s, \nCost: %s, \nAmount: %s\n", NamePW, numPW, cosPW, amountPW);
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

        switch (button.getText()) {
            case "Exit":
                System.exit(0);
                break;
            case "Clear":
                name.setText(null);
                num.setText(null);
                cos.setText(null);
                amount.setText("");
                break;
            case "Calculate":
                String StrNum = num.getText();
                int intBr = Integer.parseInt(StrNum);
                String StrCos = cos.getText();
                double DobCos = Double.parseDouble(StrCos);

                double Amount = intBr * DobCos;
                amount.setText(String.format("%.2f", Amount));
                break;
            case "Save":
                String StrNumS = num.getText();
                int intBrS = Integer.parseInt(StrNumS);
                String StrCosS = cos.getText();
                double DobCosS = Double.parseDouble(StrCosS);

                double AmountS = intBrS * DobCosS;
                amount.setText(String.format("%.2f", AmountS));
                write();
                break;
        }

    }

}
