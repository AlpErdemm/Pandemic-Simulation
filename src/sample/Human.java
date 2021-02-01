package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

/**
 * Single human represented in the board.
 */
public class Human implements Humanity{
    /**
     * coordinates
     */
    int x;
    int y;
    /**
     * index of the person
     */
    int index;

    /**
     * former coordinates to erase
     */
    int formerX;
    int formerY;

    /**
     * More necessary informations
     */
    int lastSocializedWith = -1;
    int socializingStartedAt;
    int changedDirectionAt;
    int gotInfectedAt;
    int hospitalizedAt;

    /**
     * Mask value
     */
    double mask;


    /**
     * Direction, State and Activity informations
     */
    Main.Direction direction = null;
    Main.State state = Main.State.healthy;
    Main.Activity activity = Main.Activity.moving;

    /**
     * Constructor for infected
     * @param ignore
     */
    public Human(int ignore){
        index = Main.count;
        state = Main.State.evil;
        setMask();
        findLocation();
        Main.mediator.notifyAdded(this);
        Main.mediator.notifyMediatorLocation(index, x, y);
        Main.count++;
    }

    /**
     * Constructor for innocent
     */
    public Human(){
        index = Main.count;
        findLocation();
        setMask();
        Main.mediator.notifyAdded(this);
        Main.mediator.notifyMediatorLocation(index, x, y);
        Main.count++;
    }

    public Human(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Decides and assigns mask wearing status.
     */
    public void setMask(){
        int number = (int) ((Math.random()) * 2);
        if(number == 0)
            mask = 0.2;
        else
            mask = 1;
    }

    /**
     * Initially finds a location for itself in the board, away from others.
     */
    public void findLocation(){
        ArrayList<Integer> result = Main.mediator.findMeLocation();
        while(result == null)
            result = Main.mediator.findMeLocation();

        x = result.get(0);
        y = result.get(1);
    }

    /**
     * Draw yourself into the frame
     */
    public void drawYourself(){
        if(activity == Main.Activity.socializing)
            Main.gc.setFill(Color.CYAN);
        else if(state == Main.State.healthy)
            Main.gc.setFill(Color.GREEN);
        else if(state == Main.State.evil)
            Main.gc.setFill(Color.CRIMSON);
        else if(state == Main.State.infected)
            Main.gc.setFill(Color.DEEPPINK);

        Main.gc.fillRect(x, y, Main.HUMANSIZE - 1, Main.HUMANSIZE - 1);
    }

    /**
     * Erase your afterimage
     */
    public void eraseFormer(){
        Main.gc.setFill(Color.WHITESMOKE);
        Main.gc.fillRect(formerX, formerY, Main.HUMANSIZE - 1, Main.HUMANSIZE - 1);
    }

    /**
     * Called every frame, make a decision on the board
     */
    public void move(){
        if(state == Main.State.hospitalized || state == Main.State.dead || state == Main.State.spirit){
            formerX = x;
            formerY = y;
            eraseFormer();
            x = -99;
            y = -99;
        }
        else if(activity == Main.Activity.moving){
            if(Main.secondsPassed - changedDirectionAt >= Math.random() * 8)
                direction = changeDirection(direction);
            formerX = x;
            formerY = y;
            eraseFormer();
            if(direction == Main.Direction.down){
                if(y < Main.maxY){
                    y++;
                }
            }
            else if(direction == Main.Direction.left){
                if(x > 0){
                    x--;
                }
            }
            else if(direction == Main.Direction.up){
                if(y > 0){
                    y--;
                }
            }
            else if(direction == Main.Direction.right){
                if(x < Main.maxX){
                    x++;
                }

            }
        }
        else if(activity == Main.Activity.socializing) {
            if(Main.secondsPassed - changedDirectionAt >= Math.random() * 8)
                direction = changeDirection(direction);
        }
        Main.mediator.notifyMediatorLocation(index, x, y);
        activity = Main.mediator.alive.get(index).activity;
        state = Main.mediator.alive.get(index).state;
    }

    public void getHealthy(){

    }

    public void getHospitalized(){

    }

    public void getInfected(){

    }

    public void die(){

    }

    /**
     * Change the direction you're heading towards
     * @param avoidDirection The randomizer may avoid a specific direction (stuck)
     * @return
     */
    private Main.Direction changeDirection(Main.Direction avoidDirection){
        changedDirectionAt = Main.secondsPassed;
        int shuffle = 4;
        if(avoidDirection != null){
            shuffle = 3;
        }

        ArrayList<Main.Direction> temp = new ArrayList<>();
        if(Main.Direction.down != avoidDirection)
            temp.add(Main.Direction.down);
        if(Main.Direction.left != avoidDirection)
            temp.add(Main.Direction.left);
        if(Main.Direction.up != avoidDirection)
            temp.add(Main.Direction.up);
        if(Main.Direction.right != avoidDirection)
            temp.add(Main.Direction.right);



        int number = (int) ((Math.random()) * shuffle);
        return temp.get(number);
    }
}
