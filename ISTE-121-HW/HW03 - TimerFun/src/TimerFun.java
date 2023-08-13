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
 */
public class TimerFun extends Application implements EventHandler<ActionEvent> {

    // GUI attributes
    private Stage stage;
    private Scene scene;

    // attributes for timer

    Timer timer;
    Timer timerG;

    int couter = 0;

    // lables
    Label timeLabel;
    Label[] lblColor = new Label[NUM_BARS];

    // format
    SimpleDateFormat formatTime = new SimpleDateFormat("EEE dd MMM YYYY HH:mm:ss");

    // to make the colors
    Color[] color = {
            Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.INDIGO, Color.VIOLET };
    private static final int NUM_BARS = 7;

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

        FlowPane clock = new FlowPane(10, 10);
        timeLabel = new Label(formatTime.format(new Date()));
        // font taken from:
        // https://www.oreilly.com/library/view/learning-java/1565927184/ch17s06.html
        timeLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 22));
        timeLabel.setTextFill(Color.RED);

        clock.getChildren().addAll(timeLabel);
        clock.setPadding(new Insets(10, 10, 10, 10));
        clock.setAlignment(Pos.CENTER);

        // the timer
        // ? Implement time java.util.Timer();
        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 0, 1000);

        timerG = new java.util.Timer();
        timerG.scheduleAtFixedRate(new MyTimerTask2(), 2000, 500);

        // ** THE RAINBOW
        FlowPane rainbow = new FlowPane(0, 0);
        showColors();
        rainbow.getChildren().addAll(lblColor);

        // ** THE THREADS
        InnerProgress IP1 = new InnerProgress("UnabridgedDictionary.txt");
        // if i ever forget to add this thing again im dropping out
        Thread thread1 = new Thread(IP1);
        thread1.start();

        InnerProgress IP2 = new InnerProgress("words.txt");
        Thread thread2 = new Thread(IP2);
        thread2.start();
/*
        InnerProgress IP3 = new InnerProgress("words1500.txt");
        Thread thread3 = new Thread(IP3);
        thread3.start();

        InnerProgress IP4 = new InnerProgress("words3000.txt");
        Thread thread4 = new Thread(IP4);
        thread4.start();

        InnerProgress IP5 = new InnerProgress("words5000.txt");
        Thread thread5 = new Thread(IP5);
        thread5.start();

        InnerProgress IP6 = new InnerProgress("words20000.txt");
        Thread thread6 = new Thread(IP6);
        thread6.start();

        InnerProgress IP7 = new InnerProgress("words50000.txt");
        Thread thread7 = new Thread(IP7);
        thread7.start();

        InnerProgress IP8 = new InnerProgress("words100000.txt");
        Thread thread8 = new Thread(IP8);
        thread8.start();

        InnerProgress IP9 = new InnerProgress("words125000.txt");
        Thread thread9 = new Thread(IP9);
        thread9.start();

        InnerProgress IP0 = new InnerProgress("words150000.txt");
        Thread thread0 = new Thread(IP0);
        thread0.start();
 */
        root.getChildren().addAll(mb, clock, rainbow, IP1, IP2);
      //  root.getChildren().addAll(IP3, IP4, IP5, IP6, IP7, IP8, IP9, IP0);
        scene = new Scene(root, 400, 350);
        stage.setScene(scene);
        stage.show();
    }

    // to make the clock work
    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            // new runnable
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    // set font once again
                    timeLabel.setText(formatTime.format(new Date()));
                }
            });
        }

    }

    /**
     * class makes the color change
     * 
     */
    class MyTimerTask2 extends TimerTask {

        @Override
        public void run() {
            // new runnable
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (couter == 6) {
                        couter = 0;
                    }
                    for (int i = 0; i < color.length; i++) {
                        if (i == 6) {
                            lblColor[i].setBackground(new Background(new BackgroundFill(color[i], null, null)));
                        } else {
                            Color thing = color[i];
                            color[i] = color[i + 1];
                            lblColor[i].setBackground(new Background(new BackgroundFill(color[i + 1], null, null)));
                            color[i + 1] = thing;
                        }
                    }
                    couter++;
                }
            });
        }

    }

    // for the colors
    public void showColors() {
        for (int i = 0; i < color.length; i++) {
            lblColor[i] = new Label("");
            lblColor[i].setBackground(new Background(new BackgroundFill(color[i], null, null)));
            lblColor[i].setPrefWidth(400);
        }
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
            this.prefWidth(400);
            this.setAlignment(Pos.CENTER_LEFT);
            this.setPadding(new Insets(10, 0, 0, 0));
            threadBar.setText(filename + " progress:");
            threadBar.setAlignment(Pos.CENTER_RIGHT);

            progBar.setPrefWidth(150);
            this.setVgap(8);
            this.setHgap(10);
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
