package robot;

import java.util.*;

import static java.lang.Math.abs;

/**
 * Created by Paulina on 2015-04-23.
 */
public class World {

    List<Coords> landmarksList = new ArrayList<>();
    Robot robot;
    public List<Particle> particles = new ArrayList<>();
    public static int HEIGHT = 400;
    public static int WIDTH = 500;
    public static int PARTICLES_NUMBER = 20000;
    public static double NOISE_LEVEL = 0.1;

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
        double noiseX = r.nextGaussian() * Math.sqrt(1) * newX * NOISE_LEVEL + newX;
        double noiseY = r.nextGaussian() * Math.sqrt(1) * newY * NOISE_LEVEL + newY;

        robot.move(noiseX, noiseY);
        for (Particle p : particles) {
            double noiseXX = r.nextGaussian() * Math.sqrt(1) * newX * NOISE_LEVEL + newX;
            double noiseYY = r.nextGaussian() * Math.sqrt(1) * newY * NOISE_LEVEL + newY;
            p.move(noiseXX, noiseYY);
        }

        refresh();
        resample();
        for (Particle p : particles) {
            System.out.println("p: " + p);
            System.out.println("p.getX: " + p.getX());
            System.out.println("p.getY: " + p.getY());
            System.out.println("p.getSensorMeasurement: " + p.getSensorMeasurement());
            System.out.println("p.getTempWeight: " + p.getTempWeight());
            System.out.println("p.getWeight: " + p.getWeight());
            System.out.println("p.getCoords.getX: " + p.getCoords().getX());
            System.out.println("p.getCoords.getY: " + p.getCoords().getY());
            System.out.println("--------");

        }
    }

    private void resample() {
        Random r = new Random();
        double sum = 0.0;
        double dist[] = new double[PARTICLES_NUMBER];
        int i = 0;
        int index = 0;
        List<Particle> particlesCopy = new ArrayList<>(particles.size());
        for (Particle p : particles) {
            sum += p.getWeight();
            dist[i++] = sum;
            Particle part = new Particle(p);
            particlesCopy.add(part);
        }
        System.out.println(1.0 / particles.size());
        System.out.println(r.nextDouble());
        for (int j = 0; j < particles.size(); j++) {
            index = abs(Arrays.binarySearch(dist, r.nextDouble())) % particles.size();
            System.out.println("index: " + index);
            particles.get(j).setWeight(1.0 / particles.size());
            Coords coords = particlesCopy.get(index).getCoords();
            coords.setX(coords.getX() + r.nextGaussian() * Math.sqrt(1) * coords.getX() * NOISE_LEVEL);
            coords.setY(coords.getY() + r.nextGaussian() * Math.sqrt(1) * coords.getY() * NOISE_LEVEL);
            particles.get(j).setCoords(coords);
        }
    }

    public void refresh() {
        double W = 0.0;
        double sigma = Math.pow(0.9, 2);
        for (Particle p : particles) {
            for (Coords coords : landmarksList) {
                p.sense(countDistance(p.getCoords(), coords));
                double xx = 1 / Math.sqrt(2 * Math.PI);
                double yy = (-1 * Math.pow((p.getSensorMeasurement() - countDistance(robot.getCoords(), coords)), 2)) / (2 * sigma);
                p.setTempWeight(xx * Math.exp(yy));
                W += p.getTempWeight();
                System.out.println("p.tempweight: " + p.getTempWeight());
            }
        }
        System.out.println("w: " + W);
        if (W == 0.0) {
            for (Particle p : particles) {
                p.setWeight(1.0 / particles.size());
            }
        } else {
            for (Particle p : particles) {
                p.setWeight(p.getTempWeight() / W);
            }
        }

        Collections.sort(particles, new Comparator<Particle>() {
            @Override
            public int compare(Particle o1, Particle o2) {
                return Double.compare(o2.getWeight(), o1.getWeight());
            }
        });

    }

    public double countDistance(Coords c1, Coords c2) {
        double distance = Math.sqrt(Math.pow(abs(c1.getX() - c2.getX()), 2) + Math.pow(abs(c1.getY() - c2.getY()), 2));
        Random r = new Random();
        distance = r.nextGaussian() * Math.sqrt(1.2) * distance * NOISE_LEVEL + distance;
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
        for (int i = 0; i < PARTICLES_NUMBER; i++) {
            Particle particle = new Particle();
            Random r = new Random();
            double x = (double) WIDTH * r.nextDouble();
            double y = (double) HEIGHT * r.nextDouble();
            particle.setCoords(new Coords(x, y));
            particles.add(particle);
        }
    }
}
