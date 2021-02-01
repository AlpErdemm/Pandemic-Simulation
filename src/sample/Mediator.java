package sample;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * This is a class implementing the Mediator Pattern
 */
public class Mediator {
    public ArrayList<Human> alive;

    /**
     * This class represents the socializing process.
     */
    class Socialization{
        Socialization(int i1, int i2, int start){
            index1 = i1;
            index2 = i2;
            startTime = start;
        }
        int index1;
        int index2;
        int startTime;
    }

    ArrayList<Socialization> socialization = new ArrayList<>();

    Mediator(){
        alive = new ArrayList<>();
    }

    /**
     * Humans notify Mediator of their coordinates
     * @param index
     * @param xx
     * @param yy
     */
    public void notifyMediatorLocation(int index, int xx, int yy){
        alive.get(index).x = xx;
        alive.get(index).y = yy;
    }

    public void notifyAdded(Human human){
        alive.add(human);
    }

    /**
     * Mediator looks for a suitable location on the board for a human
     * @return
     */
    public ArrayList<Integer> findMeLocation(){
        ArrayList<Integer> temp = new ArrayList<>();

        int xx = (int) (Math.random() * Main.maxX);
        int yy = (int) (Math.random() * Main.maxY);

        if(alive.size() == 0){
            temp.add(xx);
            temp.add(yy);
            return temp;
        }

        boolean flag = true;
        int i = 0;
        int j = 0;

        while(true){
            if(i == Main.maxX)
                return null;
            if(j == Main.maxY){
                j = 0;
                i++;
            }
            for(int k = 0; k < alive.size(); k++){
                if (((Math.abs(alive.get(k).x - xx+i)) < Main.SOCIALDISTANCE) || ((Math.abs(alive.get(k).y - yy+j)) < Main.SOCIALDISTANCE)) {
                    temp.add(xx+i);
                    temp.add(yy+j);
                    return temp;
                }
            }
            j++;
        }
    }

    /**
     * Mediator checks people who're close to each other and command them to go into socializing state
     */
    public void CheckOnPeople(){
        for(int i = 0; i < alive.size();i++){
            for(int j = 0; j < alive.size(); j++){
                if(i != j) {
                    int distance = (int) Math.round(Math.pow((Main.mediator.alive.get(i).x - Main.mediator.alive.get(j).x), 2) + Math.pow((Main.mediator.alive.get(i).y - Main.mediator.alive.get(j).y), 2));
                    if(distance <= Main.SOCIALDISTANCE && alive.get(i).activity
                            != Main.Activity.socializing && alive.get(j).activity != Main.Activity.socializing
                            && alive.get(i).lastSocializedWith != j){
                        socialize(i, j, Main.secondsPassed);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Socializing process initiated here
     * @param i
     * @param j
     * @param time
     */
    public void socialize(int i, int j, int time){
        if((alive.get(i).state == Main.State.infected ||  alive.get(i).state == Main.State.evil) && (alive.get(j).state == Main.State.infected ||  alive.get(j).state == Main.State.evil))
            return;
        socialization.add(new Socialization(i, j, time));
        alive.get(i).activity = Main.Activity.socializing;
        alive.get(j).activity = Main.Activity.socializing;
        alive.get(i).lastSocializedWith = j;
        alive.get(j).lastSocializedWith = i;

        if(alive.get(i).state == Main.State.infected || alive.get(i).state == Main.State.evil){
            double  P = Main.SPREADINGFACTOR * (1+((double)Main.socializingTime/10)) * alive.get(i).mask *
                    alive.get(j).mask * (1-((double)Main.SOCIALDISTANCE/10));

                alive.get(j).state = Main.State.infected;
                alive.get(j).gotInfectedAt = Main.secondsPassed;
        }
    }

    /**
     * Checks the 'socializers' if they're done
     */
    public void CheckSocializeStatus(){
        ArrayList<Integer> toDelete = new ArrayList<>();
        for(int i = 0; i < socialization.size(); i++){
            if(Main.secondsPassed - socialization.get(i).startTime >= Main.socializingTime){
                alive.get(socialization.get(i).index1).activity = Main.Activity.moving;
                alive.get(socialization.get(i).index2).activity = Main.Activity.moving;
                socialization.remove(i);
            }
        }
    }

    /**
     * Checks the infected if they're ready to be hospitalized or dying
     */
    public void CheckInfectionStatus(){

        for(int i = 0; i < alive.size(); i++){
            if(alive.get(i).state == Main.State.infected){
                if(Main.secondsPassed - alive.get(i).gotInfectedAt >= 25){
                    if(Main.hospital.count < Main.ventilators){
                        Main.hospital.count++;
                        alive.get(i).state = Main.State.hospitalized;
                        alive.get(i).hospitalizedAt = Main.secondsPassed;
                    }
                }
                if(Main.secondsPassed - alive.get(i).gotInfectedAt >= 100){
                    alive.get(i).state = Main.State.dead;
                }
            }
            if(alive.get(i).state == Main.State.hospitalized){
                if(Main.secondsPassed - alive.get(i).hospitalizedAt >= 10){
                    alive.get(i).state = Main.State.spirit;
                    Main.hospital.count--;
                    Main.addSinglePerson();
                }
            }
        }
    }

    public int getHealthy(){
        int number = 0;
        for(int i = 0; i < alive.size(); i++){
            if(alive.get(i).state == Main.State.healthy)
                number++;
        }
        return number;
    }

    public int getInfected(){
        int number = 0;
        for(int i = 0; i < alive.size(); i++){
            if(alive.get(i).state == Main.State.infected || alive.get(i).state == Main.State.evil)
                number++;
        }
        return number;
    }

    public int getHospitilized(){
        int number = 0;
        for(int i = 0; i < alive.size(); i++){
            if(alive.get(i).state == Main.State.hospitalized)
                number++;
        }
        return number;
    }

    public int getDead(){
        int number = 0;
        for(int i = 0; i < alive.size(); i++){
            if(alive.get(i).state == Main.State.dead)
                number++;
        }
        return number;
    }

}
