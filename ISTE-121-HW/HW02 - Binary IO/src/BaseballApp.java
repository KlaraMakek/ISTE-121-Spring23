import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Baseball App
 * 
 * @author Klara Makek
 * @version 2023
 *          ISTE 121 HW02
 */
public class BaseballApp extends Application {

    // stage
    private Stage stage;
    private Scene scene;

    // the alignments
    private VBox root = new VBox(10);
    private FlowPane fpTop = new FlowPane(10, 10);
    private FlowPane fpBottom = new FlowPane(10, 10);

    // the first part
    private Label labelFile = new Label("text1");
    private TextField tfName = new TextField();
    private Button buttonOpen = new Button("Open");

    /**
     * the second part
     * in label and textfld
     * out label nad textfld
     * data
     */
    private Label labelRIn = new Label("Record In:");
    private TextField tfRIn = new TextField();

    private Label labelROut = new Label("Record out:");
    private TextField tfROut = new TextField();

    private TextArea data = new TextArea();

    FileOutputStream fileOut = null;
    DataOutputStream dataOut = null;
    private TextInputControl date;

    // the main so it runs
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("Baseball APP"); // title

        tfName.setPrefWidth(250);
        tfName.setEditable(false);

        tfRIn.setPrefWidth(100);
        tfRIn.setEditable(false);

        tfROut.setPrefWidth(100);
        tfROut.setEditable(false);

        fpTop.getChildren().addAll(labelFile, tfName, buttonOpen);
        fpBottom.getChildren().addAll(labelRIn, tfRIn, labelROut, tfROut);

        data.setPrefSize(250, 250);

        // !set font
        // alignment
        root.getChildren().addAll(fpTop, fpBottom);
        // to make it show
        scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * to reed a csv file
     */
    public void reedCSV() {
        data.setText("");
        // create scanner
        Scanner sc = null;
        String fileName = null;

        FileChooser choose = new FileChooser();
        choose.setTitle("Select the SCV file!");
        // **set the filter
        // try to scan for the file
        // if not found throw exception
        try {
            File file = choose.showOpenDialog(stage);
            fileName = file.getName();
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.print("File not found!");
        }
        if (sc == null) {
            return;
        }
        tfName.setText(fileName);
        sc.nextLine();

        int recIC = 1;
        int recOC = 0;
        while (sc.hasNext()) {
            recIC++;
            String line = sc.nextLine();
            String[] value = line.split(",");
            if (value.length < 7) {
                if (line.length() > 0) {
                    data.appendText("Error no file found!");
                    data.appendText("Error2");
                }
                continue;
            }
            String fname = value[0].trim();
            String lname = value[1].trim();
            int bday, bmonth, byear, hevi;
            double height;
            String labelDate = "Bday";
            try {
                bday = Integer.parseInt(value[2]);
                labelDate = "Birth month";
                bmonth = Integer.parseInt(value[3]);
                labelDate = "Birth year";
                byear = Integer.parseInt(value[4]);
                labelDate = "Weight";
                hevi = Integer.parseInt(value[5]);
                labelDate = "Height";
                height = Double.parseDouble(value[6]);
            } catch (NumberFormatException e) {
                date.appendText("Error! " + line + "\n");
                data.appendText("Offend item somewhere. " + labelDate + "\n");
                continue;
            }
            writeToDataFile(fname, lname, bday, bmonth, byear, hevi, height);
            recOC++;
        }
        tfRIn.setText(""+recIC);
        tfROut.setText(""+recOC);
        }

    private void writeToDataFile(String fname, String lname, int bday, int bmonth, int byear, int hevi, double height) {
    try{
        dataOut.writeUTF(fname);
        dataOut.writeUTF(lname);
        dataOut.writeInt(bday);
        dataOut.writeInt(bmonth);
        dataOut.writeInt(byear);
        dataOut.writeInt(hevi);
        dataOut.writeDouble(height);

    }catch(IOException e){
        System.out.println("Some error, idk witch one!");
    }
    }

    public void reedDataFile() {
        // ?STYLESHEET
        try {
            FileInputStream FileIS = new FileInputStream(new File(STYLESHEET_CASPIAN));
            DataInputStream DataIS = new DataInputStream(FileIS);
            // TODO: TEXT ADD
            data.appendText("\n\nFirst and Last name    Birth date   Weight    Height\n");

            while (true) {
                try {
                    String name2 = DataIS.readUTF();
                    String name3 = DataIS.readUTF();
                    int bday = DataIS.readInt();
                    int bmonth = DataIS.readInt();
                    int byear = DataIS.readInt();
                    int hevi = DataIS.readInt();
                    double height = DataIS.readDouble();
                    data.appendText(
                            String.format("%-20s %10s %10d %10.1d\n", name2 + " " + name3,
                                    bmonth + "/" + bday + "/" + byear, hevi, height));
                } catch (EOFException e) {
                    break;
                } catch (IOException e) {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.print("File not found!");
        }
    }

    public void openDataFile() {
        // ?stilesheet
        try {
            fileOut = new FileOutputStream(new File(STYLESHEET_CASPIAN));
            dataOut = new DataOutputStream(fileOut);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    public void closeDataFile() {
        try {
            fileOut.close();
            dataOut.close();
        } catch (IOException e) {
            System.out.println("Can't clos data!F");
        }
    }
}
