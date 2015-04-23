package robot;

/**
 * Created by Paulina on 2015-04-23.
 */
public class Particle {

    private double distance;
    private double weight;
    private Coords coords;

    public Coords getCoords() {
        return coords;
    }

    public void setCoords(Coords coords) {
        this.coords = coords;
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
    }
}
