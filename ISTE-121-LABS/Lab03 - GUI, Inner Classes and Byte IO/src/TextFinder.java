import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Description - idk
 * 
 * @author Klara Makek
 * @version 2023
 *          ISTE 121 - LAB03
 */
public class TextFinder extends Application implements EventHandler<ActionEvent> {

    private Stage stage;
    private Scene scene;
    private VBox root = new VBox(10);

    private TextArea main = new TextArea();
    private Label label = new Label("Find:  ");
    private TextFinder tFinder = new TextFinder();

    // buttons
    private Button buttonFind = new Button("Find");
    private Button buttonCLear = new Button("Clear");

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("Text to find");

        main.setPrefSize(400, 400);
        root.getChildren().add(main);

        //main pane
        FlowPane fPane = new FlowPane();
        fPane.setAlignment(Pos.CENTER);
        tFinder.setPrefWidth(100);
        tFinder.setOnAction(this);
        fPane.getChildren().addAll(label, tFinder);

        //down pane
        FlowPane fDown = new FlowPane();
        fDown.setAlignment(Pos.CENTER);
        // !buttons
        buttonCLear.setOnAction(this);
        buttonFind.setOnAction(this);
        root.getChildren().addAll(buttonFind, buttonCLear);

        // new scenes
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnCloseRequest(null);
        stage.show();
    }
    @Override
    public void handle(ActionEvent event) {
        doFind();

    }
    public void doFind() {
        if (tFinder.getText().equals("")) {
            return;
        }
        int thing = main.getText().indexOf(tFinder.getText());
        if (thing == -1) {
            Alert a = new Alert(AlertType.ERROR);
            a.setHeaderText("Not Found, try again!");
            a.setContentText("Nothing found, try again!");
            a.show();
            return;
        }
        main.requestFocus();
        main.selectRange(thing, thing + tFinder.getText().lenght());
    }
}
