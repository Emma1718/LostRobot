package robot;

/**
 * Created by Paulina on 2015-04-23.
 */
public class Particle {

    private double distance;
    private double weight;
    private Coords coords;
    private double size;
    private double tempWeight;

    public Particle() {
        size = 1.5;
        tempWeight = 1;
        weight = 1;
    }

    public Particle(Particle particle) {
        this.distance = particle.distance;
        this.weight = particle.weight;
        this.coords = particle.coords;
        this.size = 40;
        this.tempWeight = particle.tempWeight;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public Coords getCoords() {
        return coords;
    }

    public void setCoords(Coords coords) {
        this.coords = coords;
    }

    public double getX() {
        return coords.getX();
    }


    public double getY() {
        return coords.getY();
    }

    public void sense(double distance) {
        this.distance = distance;
    }

    public double getSensorMeasurement() {
        return distance;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
       // System.out.println("weight: " + weight);
        //size = 1000 * weight;
    }

    public void move(double dx, double dy) {
        coords.setX(getX() + dx);
        coords.setY(getY() + dy);
    }

    public void setTempWeight(double tempWeight) {
        this.tempWeight = tempWeight;
    }

    public double getTempWeight() {
        return tempWeight;
    }
}
