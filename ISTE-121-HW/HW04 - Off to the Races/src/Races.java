import java.time.Duration;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 * Race
 * 
 * @author Klara Makek
 * @version 7, 2023
 *          ISTE 121 HW04
 */
public class Races extends Application {

    // initilise importatnt things
    /**
     * to make the height of gui the height of images
     */
    private final int NUM_RACES = 5;
    private final int IMG_WIDTH = 32;
    private final double fl = 0.9;
    private final double GUI_HEIGHT = IMG_WIDTH * 2;

    private boolean raceF = false;
    private Label wiLabel;
    private String winnerName;

    // the GUI
    @Override
    public void start(Stage _stage) throws Exception {
        Pane root = new Pane();
        Scene scene = new Scene(root, IMG_WIDTH * 20, GUI_HEIGHT * NUM_RACES);
        _stage.setTitle("Off to the Races - Klara Makek");
        _stage.setScene(scene);
        _stage.show();

        // to add the racers
        VBox racers = new VBox();
        for (int i = 0; i < NUM_RACES; i++) {
            Pane racer = new RacerPane(scene.getWidth(), i + 1);
            racers.getChildren().add(racer);
        }
        root.getChildren().add(racers);

        // the finish line
        double FINISH_LINE_X = scene.getWidth() * fl;
        Line finish = new Line(FINISH_LINE_X, 0, FINISH_LINE_X, scene.getHeight());
        // sets the finish line black
        finish.setStroke(Color.BLACK);
        root.getChildren().add(finish);

        /**
         * to make the animation delay for 2 seconds
         */
        Thread.sleep(Duration.ofSeconds(1).toMillis());

        /**
         * Animation timer
         * it folows the threatds and moves the images
         */
        AnimationTimer at = new AnimationTimer() {
            private boolean stop = false;
            private RacerPane winner;

            @Override
            public void handle(long now) {

                boolean fin = true;
                for (int i = 0; i < racers.getChildren().size(); i++) {
                    RacerPane racer = (RacerPane) racers.getChildren().get(i);

                    if (!racer.finished() && !stop) {
                        racer.move();
                        fin = false;
                        if (racer.getX() >= FINISH_LINE_X && !raceF) {
                            raceF = true;

                            winnerName = "racer " + (i + 1) + ".";
                            winner = racer;

                            System.out.println("Winner is " + winnerName);
                        }
                    }
                }

                if (fin) {
                    stop();

                } else if (raceF && !stop) {
                    stop = true;
                    for (int i = 0; i < racers.getChildren().size(); i++) {
                        RacerPane racer = (RacerPane) racers.getChildren().get(i);
                        if (racer != winner) {
                            racer.stop();
                        }
                    }
                }
            }
        };
        at.start();
    }

    /**
     * the pane is made for every racer
     * the racers size is determent
     */
    public class RacerPane extends Pane {

        private double width;
        private int racerNum;
        private ImageView racer;
        private Label winLab;

        private boolean done = false;
        private double position = 0;

        public RacerPane(double width, int racerNum) {
            this.width = width;
            this.racerNum = racerNum;
            // the image of the racer
            racer = new ImageView("embars.gif");

            racer.setFitWidth(IMG_WIDTH);
            racer.setFitHeight(IMG_WIDTH);
            getChildren().add(racer);

            // the label of the winner

            winLab = new Label();
            winLab.setLayoutX(GUI_HEIGHT / 2 - 10);
            winLab.setLayoutY(IMG_WIDTH);
            getChildren().add(winLab);
        }

        /**
         * It makes the racer winner random
         * When cross the finish line it gives the winner
         */
        public void move() {
            if (!done) {
                position += Math.random() * 5;
                racer.setLayoutX(position);
                winLab.setLayoutX(winLab.getWidth());
                if (position >= width * fl) {
                    done = true;
                    racer.setLayoutX(width * fl);
                    winLab.setText("Winner is " + racerNum);

                }
            }
        }

        public double getX() {
            return position;
        }

        public boolean finished() {
            return false;
        }

        public void stop() {
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
