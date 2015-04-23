package robot;

/**
 * Created by Paulina on 2015-04-23.
 */
public class Robot {
    private double distance;
    private Coords coords;

    public Robot(Coords coords) {
        this.coords = coords;
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

    public void move(double dx, double dy) {
        coords.setX(getX() + dx);
        coords.setY(getY() + dy);
    }
}
