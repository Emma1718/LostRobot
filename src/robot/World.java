package robot;

import java.io.*;
import java.util.*;

import static java.lang.Math.abs;

/**
 * Created by Paulina on 2015-04-23.
 */
public class World {

    List<Coords> landmarksList = new ArrayList<>();
    Robot robot;
    public List<Particle> particles = new ArrayList<>();
    public static int HEIGHT = 700;
    public static int WIDTH = 700;
    public static int PARTICLES_NUMBER = 10000;
    public static double NOISE_LEVEL = 0.001;
    public static double SIGMA = 3.0;
    Writer writer = null;

    public World() {
        Random r = new Random();
        robot = new Robot(new Coords((double) WIDTH * r.nextDouble(), (double) HEIGHT * r.nextDouble()));
        generateParticles();
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("C:\\Users\\Paulina\\Desktop\\filename.txt"), "utf-8"));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public Robot getRobot() {
        return robot;
    }

    public void moveRobot(double dx, double dy) {

    }

    public void interact(int angle) throws IOException {
        double newX = -3 * Math.sin(angle * Math.PI / 180);
        double newY = -3 * Math.cos(angle * Math.PI / 180);

        Random r = new Random();
        double noiseX = r.nextGaussian() * Math.sqrt(SIGMA) - 0.5 + newX;
        double noiseY = r.nextGaussian() * Math.sqrt(SIGMA) -0.5 + newY;
        if (getRobot().getX() + noiseX < 0 || getRobot().getX() + noiseX > WIDTH) {
            noiseX = 0;
        }
        if (getRobot().getY() + noiseY < 0 || getRobot().getY() + noiseY > HEIGHT) {
            noiseY = 0;
        }
        robot.move(noiseX, noiseY);
        for (Particle p : particles) {
            double noiseXX = r.nextGaussian() * Math.sqrt(SIGMA) - 0.5 + newX;
            double noiseYY = r.nextGaussian() * Math.sqrt(SIGMA) - 0.5 + newY;
            p.move(noiseXX, noiseYY);
        }
        robot.sense(countDistance(robot.getCoords(), landmarksList.get(0)));
        // System.out.println("DISTANCE: " + robot.getSensorMeasurement());

        writer.write("DISTANCE: " + robot.getSensorMeasurement());

        for (Particle p : particles) {
            double dist = countDistance(p.getCoords(), landmarksList.get(0));
            //System.out.println("coords: " + p.getCoords() + " dist: " + dist);
            //writer.write("coords: " + p.getCoords() + " dist: " + dist + "\n");
            p.setWeight(wGauss(dist, robot.getSensorMeasurement()));
        }
        double W = 0.0;
        for (Particle p : particles) {
            W += p.getWeight();
        }
        for (Particle p : particles) {
            double newW = p.getWeight() / W;
            p.setWeight(newW);
        }


//        Collections.sort(particles, new Comparator<Particle>() {
//            @Override
//            public int compare(Particle o1, Particle o2) {
//                return Double.compare(o2.getWeight(), o1.getWeight());
//            }
//        });
//        for (Particle p : particles) {
//          //  System.out.println("newW: " + p.getWeight());
//        }
        resample();


//
//        refresh();
//        for (Particle p : particles) {
//            System.out.println("p: " + p);
//            System.out.println("p.getX: " + p.getX());
//            System.out.println("p.getY: " + p.getY());
//            System.out.println("p.getSensorMeasurement: " + p.getSensorMeasurement());
//            System.out.println("p.getTempWeight: " + p.getTempWeight());
//            System.out.println("p.getWeight: " + p.getWeight());
//            System.out.println("p.getCoords.getX: " + p.getCoords().getX());
//            System.out.println("p.getCoords.getY: " + p.getCoords().getY());
//            System.out.println("robot.getSensorMeasurement: " + robot.getSensorMeasurement());
//            System.out.println("--------");
//
//        }
    }

    private void resample() throws IOException {

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
        //List<Particle> particlesCopy = new ArrayList<>(particles.size());
        for (Particle p : particles) {
            sum += p.getWeight();
            dist[i++] = sum;
            //Particle part = new Particle(p);
            // particlesCopy.add(part);
        }

//        for (int j = 0; j < dist.length; j++) {
//            dist[j] *= 100;
//        }
        //System.out.println(Arrays.toString(dist));
        // System.out.println(1.0 / particles.size());
        Map<Integer, Integer> repeated = new HashMap<>();

        for (int j = 0; j < particles.size(); j++) {
            double d = r.nextDouble();
            //System.out.println(d);
            index = abs(Arrays.binarySearch(dist, d)) % particles.size();
            //System.out.println("index: " + index);
            if (repeated.containsKey(index)) {
                repeated.put(index, repeated.get(index) + 1);
            } else {
                repeated.put(index, 1);
            }


        }

        List<Particle> newParticles = new ArrayList<>();
        for (int xx : repeated.keySet()) {
            // System.out.println("key: " + xx + " " + repeated.get(xx));
            //            particles.get(j).setWeight(1.0 / particles.size());
            for (int z = 0; z < repeated.get(xx); z++) {
                Coords coords = particles.get(xx).getCoords();

                //coords.setX(coords.getX() + r.nextGaussian()*2);// * Math.sqrt(SIGMA) - 0.5);
                //coords.setY(coords.getY() + r.nextGaussian()*2);// * Math.sqrt(SIGMA) - 0.5);
                Random rr = new Random();
                double rrGauss = r.nextGaussian() * Math.sqrt(SIGMA) - 0.5;
                //System.out.println("rrGauss: " + rrGauss);
                Particle p = new Particle();

                p.setCoords(new Coords(coords.getX() + rrGauss, coords.getY() + rrGauss));
                //System.out.println("from: " + coords + " to: " + p.getCoords());
                newParticles.add(p);
            }
//            Coords coords = particlesCopy.get(index).getCoords();
//            coords.setX(coords.getX() + r.nextGaussian() * Math.sqrt(SIGMA) - 0.5);
//            coords.setY(coords.getY() + r.nextGaussian() * Math.sqrt(SIGMA) - 0.5);
//            particles.get(j).setCoords(coords);
//           // System.out.println(particles.get(j).getCoords());
//        }
        }
        particles.clear();
        particles = new ArrayList<>(newParticles);
        for (Particle pp : particles) {
            //    System.out.println("new particle: " + pp.getCoords() + " " + countDistance(pp.getCoords(), landmarksList.get(0)));
            // writer.write("new particle: " + pp.getCoords() + " " + countDistance(pp.getCoords(), landmarksList.get(0)) + "\n");
        }
//writer.write("------------------------------------------------------------------------");
    }

    public double wGauss(double a, double b) {
        double error = a - b;
        // System.out.println("error: " + error);
        double w = Math.exp(-((Math.pow(error, 2)) / (2 * Math.pow(0.9, 2))));
        //  System.out.println("w: " + w);
        return w;
    }

    public void refresh() {
        double W = 0.0;
        double sigma = Math.pow(0.9, 2);
        for (Particle p : particles) {
            for (Coords coords : landmarksList) {
                //System.out.println("Landmark: " + coords);
                p.sense(countDistance(p.getCoords(), coords));
                // System.out.println("Distance: " + p.getSensorMeasurement());
                double xx = 1;/// Math.sqrt(sigma * 2 * Math.PI);
                double yy = (-1 * Math.pow((p.getSensorMeasurement() - robot.getSensorMeasurement()) / (WIDTH * Math.sqrt(0.05)), 2)) / (2 * sigma);
                //     System.out.println("yy: " + yy);
                //  p.setTempWeight(p.getTempWeight() * xx * Math.exp(yy));
                p.setTempWeight(xx * Math.exp(yy));
                W += p.getTempWeight();
                //System.out.println("p.tempweight: " + p.getTempWeight());
            }
//            if (p.getX()<0 || p.getX()>WIDTH ||p.getY()<0 || p.getY()>HEIGHT)
//                p.setTempWeight(0.0);
        }
        //System.out.println("w: " + W);

        if (W == 0.0) {
            for (Particle p : particles) {
                p.setWeight(1.0 / particles.size());
//                particles = new ArrayList<>();
//                generateParticles();
            }
        } else {
            for (Particle p : particles) {
                p.setWeight(p.getTempWeight() / W);
            }
        }

        //    resample();
    }

    public double countDistance(Coords c1, Coords c2) {
        double distance = Math.sqrt(Math.pow(abs(c1.getX() - c2.getX()), 2) + Math.pow(abs(c1.getY() - c2.getY()), 2));
        Random r = new Random();
        distance = r.nextGaussian() * Math.sqrt(SIGMA) - 0.5 + distance;
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
            //System.out.println("generate particle " + i + ": " + x + " " + y);
            particle.setCoords(new Coords(x, y));
            particles.add(particle);
        }
    }
}
