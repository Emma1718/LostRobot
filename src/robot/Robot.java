package robot;

/**
 * Created by Paulina on 2015-04-23.
 */
public class Robot {
    private double distance;
    private double x;
    private double y;

    public void sense(double distance) {
        this.distance = distance;
    }

    public double getSensorMeasurement() {
        return distance;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
