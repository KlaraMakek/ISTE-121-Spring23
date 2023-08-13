import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/**
 * Server - Cilent
 * 
 * @author Klara Makek and Samuel Spence
 * @version 2023
 *          ISTE 121 LAB06 & HW05
 */
public class RemoteFileClient extends Application implements EventHandler<ActionEvent> {

    // set up the stage
    Stage stage;
    Scene scene;
    Label sLabel = new Label("Server");
    TextField tf = new TextField();
    TextArea ta = new TextArea();
    private Socket sock;
    private static final int SERVER_PORT = 42069;
    DataInputStream dis = null;
    DataOutputStream dos = null;

    // to make all the buttons

    Button btCon = new Button("Connect");
    Button btLi = new Button("List");
    Button btCd = new Button("CD");
    Button btDl = new Button("Download");
    Button btUp = new Button("Upload");
    Button btLog = new Button("Log");

    public static void main(String[] args) {
        launch(args);
    }

    // simple code that I made for simple GUI
    @Override
    public void start(Stage primaryStage) throws Exception {

        stage = primaryStage;
        VBox root = new VBox();

        // flow panes

        // top pane
        FlowPane fpTop = new FlowPane();
        fpTop.setAlignment(Pos.TOP_CENTER);
        tf.setPrefWidth(500);
        tf.setOnAction(this);
        fpTop.getChildren().addAll(sLabel, tf, btCon);

        // mid pane

        FlowPane ftMid = new FlowPane();
        ftMid.setAlignment(Pos.CENTER);
        ftMid.getChildren().addAll(btLi, btCd, btDl, btUp, btLog);

        // bottom pane

        FlowPane ftBottom = new FlowPane();
        ftBottom.setAlignment(Pos.BOTTOM_CENTER);
        ta.setPrefWidth(700);
        ta.setPrefHeight(200);
        ftBottom.getChildren().addAll(ta);

        // Create a scene and add the layout to it
        Scene scene = new Scene(root, 700, 350);

        // Set the stage's title and scene, and show the stage
        primaryStage.setTitle("Remote File Browser(Klara&Sam");
        primaryStage.setScene(scene);
        primaryStage.show();

        root.getChildren().addAll(fpTop, ftMid, ftBottom);

        // buttons action
        btCon.setOnAction(this);
        btLi.setOnAction(this);
        btCd.setOnAction(this);
        btDl.setOnAction(this);
        btUp.setOnAction(this);
        btLog.setOnAction(this);

        btLi.setDisable(true);
        btCd.setDisable(true);
        btDl.setDisable(true);
        btUp.setDisable(true);
        btLog.setDisable(true);

        tf.setText("localhost"); // makes debugging faster
    }

    @Override
    public void handle(ActionEvent event) {
        // to make buttons work
        Object src = event.getSource();
        Button bt = null;
        if(src instanceof Button) bt = (Button) src;
        else bt = new Button("Connect"); // allows user to press enter on ip text field instead of clicking

        try {
            String server = tf.getText();
            switch (bt.getText().toUpperCase()) {
                // the connect button -------------------------------------------------------------
                case "CONNECT":
                    if(bt.getText().equals("Disconnect")) break; // do nothing if already conencted

                    try{
                        this.sock = new Socket(server, SERVER_PORT);
                    } catch (ConnectException e) {
                        ta.appendText("Could not connect to server\n");
                        break;
                    } 
                    ta.appendText("Conecting...\n");

                    try {
                        dos = new DataOutputStream(this.sock.getOutputStream());
                        dis = new DataInputStream(this.sock.getInputStream());
                    } catch (SocketException e) {
                        ta.appendText("Could not connect to server -- server closed\n");
                        break;
                    }
                    btCon.setText("Disconnect");

                    // enable function buttons
                    btLi.setDisable(false);
                    btCd.setDisable(false);
                    btDl.setDisable(false);
                    btUp.setDisable(false);
                    btLog.setDisable(false);
                    ta.appendText("Connected to "+ server +"\n");
                    break;

                // the list -----------------------------------------------------------------------
                case "LIST":
                    dos.writeUTF("LIST"); // send list command -->
                    dos.flush();
                    ta.appendText(dis.readUTF() + "\\\n"); // <-- receive working directory
                    int file = dis.readInt();
                    for (int i = 0; i < file; i++) { // <-- receive file names
                        String name = dis.readUTF();
                        ta.appendText("   " + name + "\n");
                    }
                    break;

                // the download button ------------------------------------------------------------
                case "DOWNLOAD":
                    TextInputDialog tidDownload = new TextInputDialog();
                    tidDownload.setContentText("File name to download");
                    tidDownload.showAndWait();
                    String fileName = tidDownload.getResult();
                    if (fileName == null) break; // if user presses cancel, break

                    ta.appendText("downloading\n");
                    dos.writeUTF("DOWNLOAD"); // send download command -->

                    dos.writeUTF(fileName); // send filename -->
                    dos.flush();

                    FileChooser downloadChooser = new FileChooser();
                    downloadChooser.setInitialDirectory(new File("."));

                    File downloadFile = downloadChooser.showSaveDialog(stage);//new File(fileName);
                    DataOutputStream dos2 = new DataOutputStream(new FileOutputStream(downloadFile));
                    long ln = dis.readLong(); // <-- receive file length

                    if(ln < 0) { // if length is -1, inform user that the file does not exist
                        ta.appendText("Error file not found: " + fileName + "\n");
                        dos2.close();
                        break;
                    }

                    for (int i = 0; i < ln; i++) { // <-- receive file bytes
                        byte bit = dis.readByte();
                        dos2.writeByte(bit);
                    }
                    dos2.flush();
                    dos2.close();
                    ta.appendText("download complete\n");
                    break;

                // the upload button --------------------------------------------------------------
                case "UPLOAD":
                    dos.writeUTF("UPLOAD"); // send upload command -->
                    TextInputDialog tidUpload = new TextInputDialog();
                    tidUpload.setContentText("File name on server");
                    tidUpload.showAndWait();

                    String fName = tidUpload.getResult();
                    if(fName == null) break;
                    ta.appendText("uploading " + fName + "...\n");
                    dos.writeUTF(fName); // send filename -->
                    FileChooser fc = new FileChooser();
                    fc.getExtensionFilters().addAll(new ExtensionFilter("Files: ", "*.*"));
                    fc.setInitialDirectory(new File("."));

                    // files
                    File file2 = fc.showOpenDialog(stage);
                    File upload = new File(file2.getAbsolutePath());
                    DataInputStream dis2 = new DataInputStream(new FileInputStream(upload));
                    //dos.writeUTF(file2.getAbsolutePath());
                    long len2 = upload.length();
                    dos.writeLong(len2); // send file length -->

                    for (int i = 0; i < len2; i++) { // send file bytes -->
                        byte bit = dis2.readByte();
                        dos.writeByte(bit);
                    }
                    dos.flush();
                    dis2.close();
                    ta.appendText("upload complete\n");
                    break;

                // request log from server --------------------------------------------------------
                case "LOG": // the example doesnt exist on mycourses so I'm just guessing what this is supposed to do
                    dos.writeUTF("LOG"); // send log command -->
                    dos.flush();

                    ta.appendText("Server log:\n");
                    ta.appendText(dis.readUTF()); // <-- receive server log
                    break;

                // change directory ---------------------------------------------------------------
                case "CD":
                    dos.writeUTF("CD"); // send cd command -->
                    TextInputDialog tidCD = new TextInputDialog();
                    tidCD.setContentText("new directory");
                    tidCD.showAndWait();

                    dos.writeUTF(tidCD.getResult()); // send new directory -->
                    dos.flush();
                    
                    String cwd = dis.readUTF(); // <-- receive current working directory
                    ta.appendText(cwd + "\n");
                    break;

                // disconnect form server ---------------------------------------------------------
                case "DISCONNECT":
                    // inform server of disconnect
                    dos.writeUTF("DISCONNECT"); // send disconnect command -->
                    dos.flush();

                    ta.appendText("Disconnecting\n");
                    
                    // close connection
                    dis.close();
                    dos.close();
                    sock.close();
                    btCon.setText("Connect");

                    // disable function buttons
                    btLi.setDisable(true);
                    btCd.setDisable(true);
                    btDl.setDisable(true);
                    btUp.setDisable(true);
                    btLog.setDisable(true);
                    break;
            } // end switch
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
