package sample;

/**
 * This interface is created for Composite Pattern
 */
public interface Humanity {
    boolean wearingMask = false;
    int C_SocialInteraction = 5;

    public void drawYourself();

    public void move();

    public void getHealthy();

    public void getHospitalized();

    public void getInfected();

    public void die();
}
