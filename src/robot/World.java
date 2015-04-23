package robot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Paulina on 2015-04-23.
 */
public class World {

    List<Coords> landmarksList = new ArrayList<>();
    Robot robot;
    List<Particle> particles = new ArrayList<>();
    public static int HEIGHT = 700;
    public static int WIDTH = 1000;

    public World() {
        Random r = new Random();
        robot = new Robot(new Coords((double)WIDTH * r.nextDouble(), (double)HEIGHT * r.nextDouble()));
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public Robot getRobot() {
        return robot;
    }

    public void moveRobot(double dx, double dy) {

    }

    public void interact(int angle) {
        System.out.println(Math.sin(angle*Math.PI/180) + " " + Math.cos(angle*Math.PI/180));
        double newX = -3*Math.sin(angle*Math.PI/180);
        double newY = -3*Math.cos(angle*Math.PI/180);
        if(getRobot().getX() + newX < 0 || getRobot().getX() + newX > WIDTH) {
            newX = 0;
        }
        if(getRobot().getY() + newY < 0 || getRobot().getY() + newY > HEIGHT) {
            newY = 0;
        }
        robot.move(newX, newY);
    }

    public void addLandmark(double x, double y) {
        landmarksList.add(new Coords(x,y));
    }

    public void removeLanmark(double x, double y) {
        Coords tmpCoords = new Coords(x, y);
        landmarksList.remove(tmpCoords);
        System.out.println(landmarksList);
    }

    public List<Particle> generateParticles() {
        for(int i = 0; i < 5000; i++) {
            Particle particle = new Particle();
            Random r = new Random();
            double x = (double)WIDTH * r.nextDouble();
            double y = (double)HEIGHT * r.nextDouble();
            particle.setCoords(new Coords(x, y));
            particles.add(particle);
        }
        return particles;
    }


}
