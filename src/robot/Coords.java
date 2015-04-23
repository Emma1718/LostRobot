package robot;

/**
 * Created by Paulina on 2015-04-23.
 */
public class Coords {
    double x;
    double y;

    private Coords(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }


}
