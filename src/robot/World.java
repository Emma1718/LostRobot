package robot;

import java.text.ParseException;
import java.util.*;

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
        robot = new Robot(new Coords((double) WIDTH * r.nextDouble(), (double) HEIGHT * r.nextDouble()));
        generateParticles();
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
        double newX = -3 * Math.sin(angle * Math.PI / 180);
        double newY = -3 * Math.cos(angle * Math.PI / 180);
        if (getRobot().getX() + newX < 0 || getRobot().getX() + newX > WIDTH) {
            newX = 0;
        }
        if (getRobot().getY() + newY < 0 || getRobot().getY() + newY > HEIGHT) {
            newY = 0;
        }
        Random r = new Random();
        double noiseX = r.nextGaussian() * Math.sqrt(1) + newX;
        double noiseY = r.nextGaussian() * Math.sqrt(1) + newY;

        robot.move(noiseX, noiseY);
        for (Particle p : particles) {
            double noiseXX = r.nextGaussian() * Math.sqrt(1) + newX;
            double noiseYY = r.nextGaussian() * Math.sqrt(1) + newY;
            p.move(noiseXX, noiseYY);
        }

        refresh();
    }

    public void refresh() {
        double W = 0.0;
        for (Particle p : particles) {
            for (Coords coords : landmarksList) {
                p.sense(countDistance(p.getCoords(), coords));
                double xx = 1 / Math.sqrt(2 * Math.PI);
                double yy = (-1 * Math.pow((p.getSensorMeasurement() - countDistance(robot.getCoords(), coords)), 2)) / (2);
                p.setTempWeight(xx * Math.exp(yy));
                W += p.getTempWeight();
            }
        }
        for (Particle p : particles) {
            p.setWeight(p.getTempWeight() / W);
        }

        Collections.sort(particles, new Comparator<Particle>() {
            @Override
            public int compare(Particle o1, Particle o2) {
                return Double.compare(o2.getWeight(), o1.getWeight());
            }
        });

        for (Particle p : particles) {
            System.out.println(p.getWeight());
        }

    }


    public double countDistance(Coords c1, Coords c2) {
        double distance = Math.sqrt(Math.pow(Math.abs(c1.getX() - c2.getX()), 2) + Math.pow(Math.abs(c1.getY() - c2.getY()), 2));
        Random r = new Random();
        distance = r.nextGaussian() * Math.sqrt(1) + distance;
        return distance;
    }

    public void addLandmark(double x, double y) {
        landmarksList.add(new Coords(x, y));
    }

    public void removeLandmark(double x, double y) {
        Coords tmpCoords = new Coords(x, y);
        landmarksList.remove(tmpCoords);
    }

    public void generateParticles() {
        for (int i = 0; i < 4000; i++) {
            Particle particle = new Particle();
            Random r = new Random();
            double x = (double) WIDTH * r.nextDouble();
            double y = (double) HEIGHT * r.nextDouble();
            particle.setCoords(new Coords(x, y));
            particles.add(particle);
        }
    }


}
