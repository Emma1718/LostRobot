package robot;

import java.io.*;
import java.util.*;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 * Created by Paulina on 2015-04-23.
 */
public class World {

    List<Coords> landmarksList = new ArrayList<>();
    Robot robot;
    public List<Particle> particles = new ArrayList<>();
    public static int HEIGHT = 600;
    public static int WIDTH = 900;
    public static int PARTICLES_NUMBER = 10000;
    public static double SIGMA = 1.0;

    public World() {
        Random r = new Random();
        robot = new Robot(new Coords(600, 400));
        //robot = new Robot(new Coords((double) WIDTH * r.nextDouble(), (double) HEIGHT * r.nextDouble()));
        generateParticles();
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public Robot getRobot() {
        return robot;
    }


    public void interact(int angle) {
        Random r = new Random();
        double mod = r.nextDouble();
        if (mod > 0.15) {
            mod = 1;
        }
        double newX = -3 * Math.sin(angle * mod * Math.PI / 180);
        double newY = -3 * Math.cos(angle * mod * Math.PI / 180);

        double noiseX = r.nextGaussian() * Math.sqrt(SIGMA) + newX;
        double noiseY = r.nextGaussian() * Math.sqrt(SIGMA) + newY;
        if (getRobot().getX() + noiseX < 0 || getRobot().getX() + noiseX > WIDTH) {
            noiseX = 0;
        }
        if (getRobot().getY() + noiseY < 0 || getRobot().getY() + noiseY > HEIGHT) {
            noiseY = 0;
        }
        robot.move(noiseX, noiseY);
        Coords[] poss = new Coords[]{new Coords(-newX, -newY), new Coords(newX, -newY), new Coords(-newX, newY), new Coords(newX, newY),
                new Coords(-newY, -newX), new Coords(newY, -newX), new Coords(-newY, newX), new Coords(newY, newX)};
        double mod2 = r.nextDouble();


//        if(mod2 < 0.2) {

        // }

        // double newXX = -3 * Math.sin(angle*mod*mod2 * Math.PI / 180);
        // double newYY = -3 * Math.cos(angle*mod * mod2 *Math.PI / 180);

        for (Particle p : particles) {
            Random newRand = new Random();
            double ifRand = newRand.nextDouble();
            //int ind = 3;
           // if(ifRand <= 0.3) {
             int   ind = newRand.nextInt(8);
           // }
            double newXX = 2*poss[ind].getX();
            double newYY = 2*poss[ind].getY();
            double noiseXX = newXX + r.nextGaussian() * Math.sqrt(SIGMA);//- 0.5 + newXX;
            double noiseYY = newYY + r.nextGaussian() * Math.sqrt(SIGMA);// - 0.5 + newYY;
            if (p.getX() + noiseXX < 0 || p.getX() + noiseXX > WIDTH) {
                noiseXX = 0;
            }
            if (p.getY() + noiseYY < 0 || p.getY() + noiseYY > HEIGHT) {
                noiseYY = 0;
            }
            p.move(noiseXX, noiseYY);
        }
        refresh();
    }

    private void resample() {
        Collections.sort(particles, new Comparator<Particle>() {
            @Override
            public int compare(Particle o1, Particle o2) {
                return Double.compare(o2.getWeight(), o1.getWeight());
            }
        });

        Random r = new Random();
        double sum = 0.0;
        double dist[] = new double[PARTICLES_NUMBER];
        int i = 0;
        int index = 0;
        List<Particle> moreThanZro = new ArrayList<>();
        for (Particle p : particles) {
            moreThanZro.add(p);
        }
        for (Particle p : moreThanZro) {
            sum += p.getWeight();
            dist[i++] = sum;
        }

        if (dist.length == 0) {
            generateParticles();
        }
        Map<Integer, Integer> repeated = new HashMap<>();

        for (int j = 0; j < particles.size(); j++) {
            double d = r.nextDouble();
            index = abs(Arrays.binarySearch(dist, d)) % particles.size();
            if (repeated.containsKey(index)) {
                repeated.put(index, repeated.get(index) + 1);
            } else {
                repeated.put(index, 1);
            }
        }

        List<Particle> newParticles = new ArrayList<>();
        for (int xx : repeated.keySet()) {
            for (int z = 0; z < repeated.get(xx); z++) {
                Coords coords = moreThanZro.get(xx).getCoords();
                Random rr = new Random();
                double rrGauss = rr.nextGaussian() * Math.sqrt(SIGMA);
                double rrGauss2 = rr.nextGaussian() * Math.sqrt(SIGMA);
                Particle p = new Particle();
                p.setCoords(new Coords(coords.getX() + rrGauss, coords.getY() + rrGauss2));
                newParticles.add(p);
            }
        }
        particles.clear();
        particles = new ArrayList<>(newParticles);
    }

    public double wGauss(double a, double b, double actual) {
        double result = 1;

        double error = a - b;
        double w = (1 / (sqrt(2 * SIGMA * PI))) * Math.exp(-((Math.pow(error, 2) / 1000) / (2 * SIGMA)));
        result = actual * w;

        return result;
    }

    public void refresh() {

        List<Double> distances = new ArrayList<>();
        for (Coords coords : landmarksList) {
            distances.add(countDistance(robot.getCoords(), coords));
        }
        robot.sense(distances);
        for (Particle p : particles) {
            p.setWeight(1);
        }
        for (Particle p : particles) {
            for (Coords c : landmarksList) {
                double dist = countDistance(p.getCoords(), c);
                p.setWeight(wGauss(dist, robot.getSensorMeasurement().get(landmarksList.indexOf(c)), p.getWeight()));
            }
        }
        double W = 0.0;
        for (Particle p : particles) {
            W += p.getWeight();
        }
        if (W <= 0) {
            for (Particle p : particles) {
                double newW = 1 / W;
                p.setWeight(newW);
            }
        } else {
            for (Particle p : particles) {
                double newW = p.getWeight() / W;
                p.setWeight(newW);
            }
        }
        if (!landmarksList.isEmpty()) {
            resample();
        }
    }

    public double countDistance(Coords c1, Coords c2) {
        double distance = Math.sqrt(Math.pow(abs(c1.getX() - c2.getX()), 2) + Math.pow(abs(c1.getY() - c2.getY()), 2));
        Random r = new Random();
        distance = r.nextGaussian() * Math.sqrt(SIGMA) + distance;
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
        particles = new ArrayList<>();
        for (int i = 0; i < PARTICLES_NUMBER; i++) {
            Particle particle = new Particle();
            Random r = new Random();
            double x = (double) WIDTH * r.nextDouble();
            double y = (double) HEIGHT * r.nextDouble();
            particle.setCoords(new Coords(x, y));
            particle.setWeight(1);
            particles.add(particle);
        }
    }
}
