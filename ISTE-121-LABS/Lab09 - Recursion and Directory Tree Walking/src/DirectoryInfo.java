import java.io.*;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.geometry.*;

/**
 * DirectoryInfo - Class to implement DirectoryInfo solution to Lab09 -
 * Recursion.
 * Does a tree walk starting at a selected directory.
 * Counts files, directories and bytes and displays them in the GUI.
 * 
 * @author Klara Makek
 * @version 2023, 2
 * ISTE 121 LAB09
 */

public class DirectoryInfo extends Application implements EventHandler<ActionEvent> {
   // Window attributes
   private Stage stage;
   private Scene scene;
   private VBox root = new VBox(8);

   // GUI Components

   Button select = new Button("Select");
   Label lblDirec = new Label("Directory:");
   TextArea direc = new TextArea();
   Label lblFiles = new Label("Files:");
   TextArea files = new TextArea();
   Label lblDS = new Label("Directories:");
   TextArea ds = new TextArea();
   Label lblByte = new Label("Bytes:");
   TextArea Byte = new TextArea();
   TextArea txt = new TextArea();

   FlowPane top = new FlowPane();
   FlowPane bottom = new FlowPane();

   // The directory chosen with JFileChooser
   private String chosenDirectory;

   // cunners
   private int dirCont = 0;
   private int fileCont = 0;
   private int byteCont = 0;

   /** main program */
   public static void main(String[] args) {
      launch(args);
   }

   /** Constructor (effectively the main) */
   public void start(Stage _stage) {
      // Setup window
      stage = _stage;
      stage.setTitle("Directory Info");
      stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
         public void handle(WindowEvent evt) {
            System.exit(0);
         }
      });

      // Set the scene and show the stage
      direc.setPrefHeight(10);
      direc.setPrefWidth(550);
      files.setPrefHeight(10);
      files.setPrefWidth(200);
      ds.setPrefHeight(10);
      ds.setPrefWidth(200);
      Byte.setPrefHeight(10);
      Byte.setPrefWidth(200);
      top.getChildren().addAll(lblDirec, direc);
      bottom.getChildren().addAll(lblFiles, files, lblDS, ds, lblByte, Byte);

      root.getChildren().addAll(select, top, bottom, txt);

      scene = new Scene(root, 800, 400);
      stage.setScene(scene);
      stage.show();

      // ! don't forget to add the action to the button, for the 100 times

      select.setOnAction(this);
   }

   // Only one button in this app - select a directory
   public void handle(ActionEvent ae) {
      if (ae.getSource() == select) {
         // directory chooser created
         DirectoryChooser dc = new DirectoryChooser();
         File directory = dc.showDialog(stage);
         if (directory == null) {
            // no directory chosen
            return;
         }
         // when directory chosen it gets the path
         chosenDirectory = directory.getAbsolutePath();
         // sets the name of the directory in the textArea
         direc.setText(chosenDirectory);

         DirInfoGetter dirInfoGetter = new DirInfoGetter(chosenDirectory);
         dirInfoGetter.start();
      }
   }

   // The thread for getting dir info
   class DirInfoGetter extends Thread {

      private String startDir;

      // Store the starting directory
      public DirInfoGetter(String _dName) {
         startDir = _dName;
      }

      public void run() {
         // call recursive method to process directories
         getDirInfo("", startDir);
      }

      private void getDirInfo(String indent, String dName) {

         File nf = new File(dName);
         if (!nf.isDirectory()) {
            return;
         }
         dirCont++;
         File[] list = nf.listFiles();
         if (list == null) {
            return;
         }
         String dots = "";
         for (int i = 0; i < indent.length(); i += 2) {
            dots += ".";
         }
         for (File file : list) {
            if (file.isDirectory()) {
               // https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java
               // https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java
               // https://docs.oracle.com/javase/tutorial/essential/io/dirs.html
               getDirInfo(indent + " ", file.getAbsolutePath());
               String name = String.format("d-%s%s %s\n", indent, dots, file.getName());
               Platform.runLater(() -> txt.appendText(name));
            } else {
               fileCont++;
               byteCont += file.length();
               String name = String.format("f-%s%s %s\n", indent, dots, file.getName());
               Platform.runLater(() -> txt.appendText(name));
            }
         }
         Platform.runLater(() -> files.setText(String.valueOf(fileCont)));
         Platform.runLater(() -> ds.setText(String.valueOf(dirCont)));
         Platform.runLater(() -> Byte.setText(String.valueOf(byteCont)));
      }
   }
}