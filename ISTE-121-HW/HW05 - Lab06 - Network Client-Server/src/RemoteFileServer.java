import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Server - Cilent
 * 
 * @author Klara Makek and Samuel Spence
 * @version 2023
 *          ISTE 121 LAB06 & HW05
 */
public class RemoteFileServer extends Application implements EventHandler<ActionEvent> {

    public static void main(String[] args) {
        launch(args);
    }

    // set the stage
    Stage stage;
    Scene scene;
    TextField tf = new TextField();
    TextArea ta = new TextArea();

    String server = tf.getText();
    Button btn = new Button("Start");

    private ServerSocket sSocket = null;
    private static final int SERVER_PORT = 42069;

    private int clientCounter = 1;

    // threads
    private ServerThread st = null;

    class ServerThread extends Thread {
        private boolean running = false;
        private boolean waiting = false; // awaiting client connection
        public void run() {
            try {
                running = true;
                sSocket = new ServerSocket(SERVER_PORT);
                ta.appendText("Server started, awaiting client connections\n");
                
                while (running) { // copied from in-class exercise
                    if(!waiting) { // allows server to be stopped while still awaiting client connections
                        System.out.println("waiting for client");
                        waiting = true;
                        new Runnable() {
                            public void run() {
                                Socket cSocket = null;
                                try{
                                    cSocket = sSocket.accept();// blocking point
                                } catch(IOException e) {
                                    // dont need to handle this exception afaik
                                }
                                if(running) {
                                    ClientThread cT = new ClientThread(cSocket, "Client" + clientCounter);
                                    cT.start();
                                    clientCounter++;
                                }
                                waiting = false;
                            }
                        }.run();
                    } // end if
                    
                } // end while loop
                ta.appendText("Server stopped\n");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void stopServer() {
            running = false;
            try {
                sSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ClientThread extends Thread {
        Socket cSocket = null;

        public ClientThread(Socket sock2, String name) {
            this.cSocket = sock2;
            setName(name);
        }

        public void run() {
            final String name = getName();
            Platform.runLater(
                    new Runnable() {
                        public void run() {
                            ta.appendText(name + " connecting...\n");
                        }
                    });

            String workingDirectory = new File("").getAbsolutePath();

            DataInputStream dis = null;
            DataOutputStream dos = null;
            try {
                dis = new DataInputStream(cSocket.getInputStream());
                dos = new DataOutputStream(cSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // server has been stopped
            }

            try {
                boolean connected = true;
                String cmd;
                while(connected) {
                    cmd = dis.readUTF().toUpperCase();
                    ta.appendText(name + ": "); // log who sent the command
                    switch(cmd) {
                        // list command -----------------------------------------------------------
                        case "LIST":
                            ta.appendText("list\n");
                            dos.writeUTF(workingDirectory); // send working directory -->
                            File curFolder = new File(workingDirectory);
                            File[] files = curFolder.listFiles();

                            dos.writeInt(files.length); // send how many files -->
                            for(File f : files) { // send file names -->
                                dos.writeUTF(f.getName());
                            }
                            dos.flush();
                            break;
                        
                        // download command -------------------------------------------------------
                        case "DOWNLOAD":
                            String fileName = dis.readUTF(); // <-- receive requested file name
                            ta.appendText("download " + fileName + "\n");
                            File fileToSend = new File(workingDirectory + "\\" + fileName);
                            if(!fileToSend.exists()) {
                                dos.writeLong(-1); // if file doesnt exist, send length of -1 -->
                                break;
                            }
                            long dlLength = fileToSend.length();
                            dos.writeLong(dlLength); // send file length -->
                            DataInputStream fileSender = new DataInputStream(new FileInputStream(fileToSend));
                            for(int i = 0; i < dlLength; i++) { // send file bytes -->
                                byte b = fileSender.readByte();
                                dos.writeByte(b);
                            }
                            fileSender.close();
                            dos.flush();
                            break;
    
                        // upload command ---------------------------------------------------------
                        case "UPLOAD":
                            ta.appendText("uploading ");
                            String uplFileName = dis.readUTF(); // <-- receive file name
                            long uplLength = dis.readLong(); // <-- receive file length
                            ta.appendText(String.format("%s (%d bytes)\n", uplFileName, uplLength));
                            DataOutputStream uplDos = new DataOutputStream(new FileOutputStream(new File(workingDirectory + "\\" + uplFileName)));

                            for(int i=0; i<uplLength; i++) { // <-- receive file bytes
                                byte b = dis.readByte();
                                uplDos.writeByte(b);
                            }
                            uplDos.flush();
                            uplDos.close();
                            ta.appendText("upload complete\n");
                            break;
    
                        // disconnect command -----------------------------------------------------
                        case "DISCONNECT":
                            dos.close();
                            dis.close();
                            cSocket.close();
                            ta.appendText("Client disconnected (disconnect)\n");
                            connected = false;
                            break;
                        
                        // change directory command -----------------------------------------------
                        case "CD":
                            ta.appendText("cd ");
                            String newDirectory = dis.readUTF(); // <-- receive requested directory
                            ta.appendText(newDirectory + "\n");
                            
                            newDirectory = workingDirectory + "\\" + newDirectory; // append the new directory to the end of the working directory
                            workingDirectory = new File(newDirectory).getCanonicalPath();

                            dos.writeUTF(workingDirectory); // send new working directory -->
                            dos.flush();
                            break;
                        
                        // log command ------------------------------------------------------------
                        case "LOG":
                            ta.appendText("log\n");
                            dos.writeUTF(ta.getText()); // send server log -->
                            break;
                    } // end switch
                } // end while loop
            } catch (EOFException e) {
                // in case client disconnects without notifying server
                ta.appendText("Client disconnected (eof)\n");
            } catch (SocketException e) {
                // ditto
                ta.appendText("Client disconnected (SocketException)\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void handle(ActionEvent event) {
        Button btn = (Button) event.getSource();
        switch (btn.getText()) {
            case "Start":
                btn.setText("Stop");
                st = new ServerThread();
                st.start();
                break;
            case "Stop":
                btn.setText("Start");
                st.stopServer();
                //st.join();
                break;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        VBox root = new VBox();

        // flow panes
        // top pane
        FlowPane fpTop = new FlowPane();

        fpTop.setAlignment(Pos.TOP_RIGHT);
        fpTop.getChildren().add(btn);
        btn.setOnAction(this);

        // bottom pane
        FlowPane ftBottom = new FlowPane();
        ftBottom.setAlignment(Pos.BOTTOM_CENTER);
        ta.setPrefWidth(700);
        ta.setPrefHeight(320);
        ftBottom.getChildren().addAll(ta);

        // Create a scene and add the layout to it
        Scene scene = new Scene(root, 700, 350);

        // Set the stage's title and scene, and show the stage
        primaryStage.setTitle("Remote File Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        root.getChildren().addAll(fpTop, ftBottom);

        // make sure no background stuff is running afer close
        primaryStage.setOnCloseRequest((evt) -> {
            st.stopServer();
            Platform.exit();
            System.exit(0);
        });
    }
}
