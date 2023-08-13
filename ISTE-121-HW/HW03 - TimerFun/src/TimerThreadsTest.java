
    import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Funny progress bars and colors
 * 
 * @author Klara Makek
 * @version 2023
 *          ISTE121 - HW03
 * 
 * 
 * THIS was streactly to find a mistage
 */
public class TimerThreadsTest extends TimerFun{

    // GUI attributes
    private Stage stage;
    private Scene scene;

    // attributes for timer

    Timer timer;
    Timer timerG;

    private boolean cont = true;

    public static void main(String args[]) {
        launch(args);
    }

    // first make the stage

    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("Timer Fun!");
        VBox root = new VBox(0);

        MenuBar mb = new MenuBar();
        Menu menu1 = new Menu("File");
        MenuItem file = new MenuItem("Exit");
        Menu menu2 = new Menu("About");
        MenuItem about = new MenuItem("Help");

        // addint to menu
        menu1.getItems().addAll(file);
        menu2.getItems().addAll(about);

        file.setOnAction(this);
        about.setOnAction(this);

        // to add to the bar
        mb.getMenus().addAll(menu1, menu2);

        // **THE CLOCK


        // ** THE THREADS
        InnerProgress IP1 = new InnerProgress("words1500.txt");
        // new thread
        Thread thread1 = new Thread(IP1);
        //! MISTAKE NO IP1
        thread1.start();

        InnerProgress IP2 = new InnerProgress("words3000.txt");
        // new thread
        Thread thread2 = new Thread(IP2);
         //! MISTAKE NO IP2
        thread2.start();

        root.getChildren().addAll(mb,IP1, IP2);
        scene = new Scene(root, 350, 500);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * In the main menu, drop down of File gets the option Exit
     * when case 'exit' the program exits and shuts down
     * In the dro down of About gets the option Help
     * when case 'help' alert with message pops up, oke exits the alert
     */
    public void handle(ActionEvent ae) {
        MenuItem menuI = (MenuItem) ae.getSource();
        switch (menuI.getText()) {
            case "Exit":
                System.exit(1);
                break;
            case "Help":
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Message");
                alert.setContentText("Fun with Timers and Threads\nby: Dave Patric");
                alert.showAndWait();
                break;
        }
    }

    // the thread progress line of the text it reads
    public class InnerProgress extends FlowPane implements Runnable {
        private ProgressBar progBar = new ProgressBar(0);
        private Label threadBar = new Label("");
        private Label valueBar = new Label("0");
        private StackPane pbPane = new StackPane();
        long size = 0;
        double perc;
        long reed = 0;
        BufferedReader br;

        private String txt = "";
        private String str;
        int counter1 = 0;

        // makes the progress bars visible
        public InnerProgress(String filename) {
            this.prefWidth(350);
            this.setAlignment(Pos.CENTER);
            this.setPadding(new Insets(10, 0, 0, 0));
            threadBar.setText(filename + " progress:");
            threadBar.setAlignment(Pos.CENTER_LEFT);

            progBar.setPrefWidth(180);
            this.setVgap(8);
            this.setHgap(40);
            pbPane.getChildren().addAll(progBar, valueBar);
            this.getChildren().addAll(threadBar, pbPane);

            size = new File(filename).length();
            perc = 100.0 / size;
            System.out.println(size);
            try {
                br = new BufferedReader(new FileReader(new File(filename)), 17280);
            } catch (Exception e) {
                // System.out.println("Error");
                e.printStackTrace();
            }

        }

        // to make it work
        @Override
        public void run() {
            valueBar.setText("Opening file....");
            try {
                Thread.sleep((int) (2000));
            } catch (InterruptedException e) {
                // System.out.println("Thread interrupted.");
                e.printStackTrace();
            }
            try {
                while ((str = br.readLine()) != null && cont) {
                    txt += str;
                    try {
                        Thread.sleep((int) (1));

                    } catch (InterruptedException e) {
                        // System.out.println("Thread interrupted.");
                        e.printStackTrace();
                    }

                    reed += str.getBytes("UTF-8").length + 2;
                    // System.out.println(reed);
                    final long value = (int) Math.round(reed * 100 / size);
                    // System.out.println(value);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            progBar.setProgress(value / 100.0);
                            valueBar.setText(value + "%");
                        }
                    });
                }
                if (cont) {
                    try {
                        Thread.sleep((int) (2000));
                    } catch (InterruptedException e) {
                        // System.out.println("Thread interrupted.");
                        e.printStackTrace();
                    }
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        progBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                    }
                });

                br.close();
                timerG.cancel();
                cont = false;
            } catch (Exception e) {
                // System.out.println("Error");
            }
        }

    }

}

