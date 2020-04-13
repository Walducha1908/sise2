package Model;

public class MeasurementPoint {
    private double x;
    private double y;

    public MeasurementPoint() {
        this.x = 0;
        this.y = 0;
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

    @Override
    public String toString() {
        return "M("+ x + ", " + y + ")";
    }
}