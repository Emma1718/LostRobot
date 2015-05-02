package robot;

import java.util.List;

/**
 * Created by Paulina on 2015-04-23.
 */
public class Robot {
    private double distance;
    private Coords coords;
    List<Double> distances;

    public Robot(Coords coords) {
        this.coords = coords;
    }

    public Coords getCoords() {
        return coords;
    }

    public double getX() {
        return coords.getX();
    }

    public double getY() {
        return coords.getY();
    }

    public void sense(List<Double> distance) {
        this.distances = distance;
    }

    public List<Double> getSensorMeasurement() {
        return distances;
    }

    public void move(double dx, double dy) {
        coords.setX(getX() + dx);
        coords.setY(getY() + dy);
    }
}
