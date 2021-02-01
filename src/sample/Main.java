package sample;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * The main class that handles the Graphics and General Control
 */
public class Main extends Application {
    static int SPEED = 7;
    static int HUMANSIZE = 8;
    static int SOCIALDISTANCE = 9;   // D
    static double SPREADINGFACTOR = 0.5;
    static double MORTALITYRATE = 0.1;
    static int count = 0;
    static int secondsPassed = 0;
    static int socializingTime = 3;
    static int ventilators = 5;

    static int minX = 0;
    static int maxX = 1000 - HUMANSIZE + 1;

    static int minY = 0;
    static int maxY = 600 - HUMANSIZE + 1;

    static int formersecs = 0;
    static int formerhealthy = 0;
    static int formerinfected = 0;
    static int formerhospitalized = 0;
    static int formerdead = 0;
    static boolean paused = false;

    /**
     * Interface array to represent both humans and people
     */
    static List<Humanity> humans = new ArrayList<>();
    static GraphicsContext gc;

    /**
     * Mediator Controller initiated.
     */
    static Mediator mediator = new Mediator();
    /**
     * Hospital initiated.
     */
    static Hospital hospital = new Hospital();

    /**
     * Movement directions
     */
    public enum Direction {
        left, right, up, down
    }

    /**
     * Person states
     */
    public enum State{
        healthy, infected, hospitalized, evil, dead, spirit
    }

    /**
     * Person activities
     */
    public enum Activity{
        moving, socializing
    }


    /**
     * Start method that builds the graphics up
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox root = new VBox(10);
        Canvas c = new Canvas(1000, 600);

        gc = c.getGraphicsContext2D();
        root.getChildren().add(c);

        VBox root2 = new VBox(10);

        Button play = new Button("PLAY");
        play.setText("Play");
        play.setOnAction(actionEvent ->  {
            paused = false;
        });

        Button pause = new Button("PAUSE");
        pause.setText("Pause");
        pause.setOnAction(actionEvent ->  {
            paused = true;
        });

        root2.getChildren().add(play);
        root2.getChildren().add(pause);


        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.getChildren().addAll(root, root2);


        /**
         * The actual frame creator
         */
        new AnimationTimer() {
            long lastRefreshTick = 0;
            long lastSecondsTick = 0;


            public void handle(long now) {
                if (lastRefreshTick == 0) {
                    lastRefreshTick = now;
                    refreshFrame(gc);
                    return;
                }

                if (lastSecondsTick == 0) {
                    lastSecondsTick = now;
                    return;
                }

                /**
                 * Pause control
                 */
                if(!paused){
                    if (now - lastSecondsTick > 1000000000) {
                        lastSecondsTick = now;
                        secondsPassed++;
                    }

                    if (now - lastRefreshTick > 2000000000 / (SPEED * 100)) {
                        lastRefreshTick = now;
                        refreshFrame(gc);
                    }
                }
            }
        }.start();

        /**
         * Demo here
         */

        /**
         * Composite Pattern, single Human
         */
        addSinglePerson();

        /**
         * Composite Pattern, bulk people
         */
        addPeople(500);

        primaryStage.setTitle("Epidemic Simulation");
        primaryStage.setScene(new Scene(new StackPane( hBox), 1100, 600));
        primaryStage.show();


    }

    /**
     * Since the humans ArrayList contains Humanity interface we are free to use composite pattern.
     * @param number
     */
    public static void addPeople(int number){
        People group = new People();
        ArrayList<Human> temp = new ArrayList<>();
        for(int i = 0; i < number; i++){
            if(i == 0 && humans.size() == 0){   // infected
                Human h = new Human(1);
                temp.add(h);
                group.add(h);
            }
            else{
                Human h = new Human();
                temp.add(h);
                group.add(h);
            }
        }

        for(int i = 0; i < number; i++){
            humans.add(temp.get(i));
        }
    }

    /**
     * Add a single person into a random location
     */
    public static void addSinglePerson(){
        if(humans.size() == 0){
            Human temp = new Human(1);  // infected
            humans.add(temp);
        }
        else{
            Human temp = new Human();
            humans.add(temp);
        }
    }

    /**
     * This method is called every frame
     * @param graph
     */
    public static void refreshFrame(GraphicsContext graph){
        Main.gc.setFill(Color.WHITE);
        graph.setFont(new Font("", 20));
        graph.fillText("Time passed: " + formersecs + " secs", 800, 50);

        graph.setFill(Color.BLACK);
        graph.setFont(new Font("", 20));
        graph.fillText("Time passed: " + secondsPassed + " secs", 800, 50);
        formersecs = secondsPassed;

        Main.gc.setFill(Color.WHITE);
        graph.setFont(new Font("", 20));
        graph.fillText("Healthy: " + formerhealthy, 800, 80);

        graph.setFill(Color.BLACK);
        graph.setFont(new Font("", 20));
        graph.fillText("Healthy: " + mediator.getHealthy(), 800, 80);
        formerhealthy = mediator.getHealthy();

        graph.setFill(Color.WHITE);
        graph.setFont(new Font("", 20));
        graph.fillText("Infected: "+ formerinfected, 800, 110);

        graph.setFill(Color.BLACK);
        graph.setFont(new Font("", 20));
        graph.fillText("Infected: "+ mediator.getInfected(), 800, 110);
        formerinfected = mediator.getInfected();

        graph.setFill(Color.WHITE);
        graph.setFont(new Font("", 20));
        graph.fillText("Hospitalized: " + formerhospitalized, 800, 140);

        graph.setFill(Color.BLACK);
        graph.setFont(new Font("", 20));
        graph.fillText("Hospitalized: " + mediator.getHospitilized(), 800, 140);
        formerhospitalized = mediator.getHospitilized();

        graph.setFill(Color.WHITE);
        graph.setFont(new Font("", 20));
        graph.fillText("Dead: " + formerdead, 800, 170);

        graph.setFill(Color.BLACK);
        graph.setFont(new Font("", 20));
        graph.fillText("Dead: " + mediator.getDead(), 800, 170);
        formerdead = mediator.getDead();

        /**
         * Updates human changes.
         */
        updateHumans(graph);

        /**
         * Mediator is updated every frame as well.
         */
        mediator.CheckOnPeople();
        mediator.CheckSocializeStatus();
        mediator.CheckInfectionStatus();
    }

    public static void updateHumans(GraphicsContext graph){
        for (Humanity person : humans) {
            person.move();
            person.drawYourself();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
