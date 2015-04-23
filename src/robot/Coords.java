package robot;

/**
 * Created by Paulina on 2015-04-23.
 */
public class Coords {
    double x;
    double y;

    public Coords(double x, double y) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coords coords = (Coords) o;

        return Double.compare(coords.x, x) == 0 && Double.compare(coords.y, y) == 0;

    }

}
