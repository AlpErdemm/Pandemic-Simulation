package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Composite pattern class of multiple humans, it usually calls human methods in an iterator.
 */
public class People implements Humanity{
    ArrayList<Human> people = new ArrayList<>();

    public void add(Human humanity){
        people.add(humanity);
    }

    public void drawYourself(){
        for (Humanity human : people) {
            Human temp = (Human) human;
            temp.drawYourself();
        }
    }

    @Override
    public void move() {
        for (Humanity human : people) {
            Human temp = (Human) human;
            temp.move();
        }
    }

    @Override
    public void getHealthy() {

    }

    @Override
    public void getHospitalized() {

    }

    @Override
    public void getInfected() {

    }

    @Override
    public void die() {

    }
}
