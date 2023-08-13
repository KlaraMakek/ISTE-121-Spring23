import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.collections.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * StudentClient - class for Student's record.
 * 
 * @author Klara Makek
 *         Course/Section: ISTE-121 800
 *         Practical #2
 *         Date: 31.3.2023.
 */
public class MyClientForStudentsGUIStarter extends Application implements EventHandler<ActionEvent> {
   // Window attributes
   private Stage stage;
   private Scene scene;
   private VBox root = null;

   // GUI components
   private MenuBar mbBar = new MenuBar();
   private Menu mnuOrders = new Menu("Database");
   private MenuItem mnuiConnect = new MenuItem("Connect");
   private TextField tfName = null;
   private TextField tfGPA = null;
   private TextField tfListOfAwards = null;

   // filter
   private TextField tfFilterName = null;
   private TextField tfFilterGPA = null;

   private ListView<String> listView = null;

   private Button btnSend = null;
   private Button btnFilter = null;

   // Other attributes
   public static final int SERVER_PORT = 12345;
   private static final Long recordTimeStamp = null;
   private Socket socket = null;
   private ObjectInputStream ois = null;
   private ObjectOutputStream oos = null;

   /** main program */
   public static void main(String[] args) {
      launch(args);
   }

   /** constructor */
   public void start(Stage _stage) {
      // Set up window
      stage = _stage;
      stage.setTitle("Student Database: ");

      stage.setOnCloseRequest(
            new EventHandler<WindowEvent>() {
               public void handle(WindowEvent evt) {
                  terminateClient();
               }
            });

      // Menu setup
      mnuOrders.getItems().addAll(mnuiConnect);
      mbBar.getMenus().add(mnuOrders);
      root = new VBox(mbBar);
      mnuiConnect.setOnAction(this);

      GridPane gridPane = new GridPane();
      root.getChildren().add(gridPane);

      this.tfName = new TextField("Alice Scott");
      this.tfName.setPrefColumnCount(30);
      this.tfGPA = new TextField("3.4");
      this.tfListOfAwards = new TextField("Academic Star, Awesome Attitude");

      // Setting the padding
      gridPane.setPadding(new Insets(10, 10, 10, 10));
      gridPane.setVgap(5);
      gridPane.setHgap(5);

      gridPane.add(new Label("Name: "), 0, 0);
      gridPane.add(tfName, 1, 0);

      gridPane.add(new Label("GPA: "), 0, 1);
      gridPane.add(tfGPA, 1, 1);

      gridPane.add(new Label("List of awards: "), 0, 3);
      gridPane.add(tfListOfAwards, 1, 3);

      gridPane.add(new Label("    "), 0, 4);

      // Add buttons
      btnSend = new Button("Place");
      btnSend.setOnAction(this);
      // Filtering
      btnFilter = new Button("Filter");
      btnFilter.setOnAction(this);

      btnSend.setDisable(true);
      btnFilter.setDisable(true);

      tfFilterName = new TextField("JO");
      tfFilterGPA = new TextField("3.0");

      gridPane.add(btnSend, 0, 5);

      FlowPane filterPane = new FlowPane(10, 10);
      filterPane.getChildren().addAll(btnFilter, new Label("Name:"), tfFilterName, new Label("GPA:"), tfFilterGPA);

      ObservableList<String> studentList = FXCollections.observableArrayList("Student list");
      listView = new ListView<String>(studentList);
      listView.setMaxSize(700, 200);

      root.getChildren().add(listView);
      root.getChildren().add(filterPane);

      // Set the scene and show the window
      scene = new Scene(root, 700, 500);
      stage.setScene(scene);
      stage.setX(100);
      stage.setY(100);
      stage.show();
   }

   public void handle(ActionEvent e) {

      switch (e.getSource()) {
         case "Connect":
            doConnect();
            break;
         case "Disconnect":
            doDisconnect();
            break;

      } switch () {
         case "Place":
         doPlace();
         break;
      case "Flter":
         doFilter();
         break;
      }

   }

   private void doConnect() {
      TextInputDialog input = new TextInputDialog();
      input.setContentText("Server IP: ");
      input.setHeaderText("Enter the server's IP address.");
      input.showAndWait(); 
      String server = input.getEditor().getText();
      // sets the button name to disconnect
      mnuiConnect.setText("Disconect");
      btnSend.setDisable(false);
      btnFilter.setDisable(false);
   }

   private void doDisconnect() {
      try {
         socket.close();
         ois.close();
         oos.close();
      } catch (Exception e) {
         Alert alert = new Alert(AlertType.ERROR, "Unexpected Exception (doDisconnect)n: " + e);
         alert.setHeaderText("Exception");
         alert.showAndWait();
         return;
      }
      socket = null;
      oos = null;
      ois = null;
      btnSend.setDisable(true);
      btnFilter.setDisable(true);
      mnuiConnect.setText("Connect");
   }

   private void doPlace() {
      String name = tfName.getText();
      Double gpa = Double.parseDouble(tfGPA.getText());
      ArrayList<String> list = new ArrayList<>();
      String awards = tfListOfAwards.getText();
      StudentClass student = new StudentClass(name,gpa, recordTimeStamp, list);

      try{
         oos.writeObject(student);
         student = (StudentClass) ois.readObject();

      }catch(IOException e){
         e.printStackTrace();
      }catch(ClassNotFoundException ef){
         ef.printStackTrace();
      }
   }

   public void doFilter() {
      String filterName = tfFilterName.getText();
      Double filterGpa =Double.parseDouble(tfFilterGPA.getText());

   }

   public void terminateClient() {
      try {
         if (oos != null) {
            oos.writeObject("EXIT");
            oos.flush();
            socket.close();
            oos.close();
            ois.close();
         }
      } catch (Exception e) {
         Alert alert = new Alert(AlertType.ERROR, "Unexpected Exception (terminateClient): " + e);
         alert.setHeaderText("Exception");
         alert.showAndWait();
      }
      System.exit(0);
   }
}